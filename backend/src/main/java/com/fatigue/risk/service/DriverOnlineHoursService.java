package com.fatigue.risk.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fatigue.risk.config.FatigueProperties;
import com.fatigue.risk.dto.OnlineHoursImportDTO;
import com.fatigue.risk.entity.DriverOnlineHours;
import com.fatigue.risk.entity.RiskRestriction;
import com.fatigue.risk.enums.RestrictionStatusEnum;
import com.fatigue.risk.enums.RestrictionTypeEnum;
import com.fatigue.risk.mapper.DriverOnlineHoursMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
public class DriverOnlineHoursService extends ServiceImpl<DriverOnlineHoursMapper, DriverOnlineHours> {

    private static final Logger log = LoggerFactory.getLogger(DriverOnlineHoursService.class);

    private final DriverOnlineHoursMapper driverOnlineHoursMapper;
    private final RiskRestrictionService riskRestrictionService;
    private final FatigueProperties fatigueProperties;
    private final ObjectMapper objectMapper;

    public DriverOnlineHoursService(DriverOnlineHoursMapper driverOnlineHoursMapper,
                                    RiskRestrictionService riskRestrictionService,
                                    FatigueProperties fatigueProperties,
                                    ObjectMapper objectMapper) {
        this.driverOnlineHoursMapper = driverOnlineHoursMapper;
        this.riskRestrictionService = riskRestrictionService;
        this.fatigueProperties = fatigueProperties;
        this.objectMapper = objectMapper;
    }

    @Transactional(rollbackFor = Exception.class)
    public DriverOnlineHours importHours(OnlineHoursImportDTO dto) {
        DriverOnlineHours hours = new DriverOnlineHours();
        hours.setDriverId(dto.getDriverId());
        hours.setWorkDate(dto.getWorkDate());
        hours.setOnlineStart(dto.getOnlineStart());
        hours.setOnlineEnd(dto.getOnlineEnd());

        if (dto.getOnlineMinutes() != null) {
            hours.setOnlineMinutes(dto.getOnlineMinutes());
        } else {
            long minutes = ChronoUnit.MINUTES.between(dto.getOnlineStart(), dto.getOnlineEnd());
            hours.setOnlineMinutes((int) minutes);
        }
        hours.setWorkMinutes(dto.getWorkMinutes() != null ? dto.getWorkMinutes() : hours.getOnlineMinutes());
        hours.setRestMinutes(dto.getRestMinutes() != null ? dto.getRestMinutes() : 0);
        hours.setOrderCount(dto.getOrderCount() != null ? dto.getOrderCount() : 0);
        hours.setBatchNo(dto.getBatchNo());
        hours.setCreateTime(LocalDateTime.now());

        save(hours);
        checkAndTriggerRiskControl(dto.getDriverId(), dto.getWorkDate());
        return hours;
    }

    @Transactional(rollbackFor = Exception.class)
    public List<DriverOnlineHours> batchImportHours(List<OnlineHoursImportDTO> dtoList) {
        Map<Long, Set<LocalDate>> driverDateMap = new HashMap<>();
        List<DriverOnlineHours> result = new ArrayList<>();
        String batchNo = "BATCH_" + System.currentTimeMillis();

        for (OnlineHoursImportDTO dto : dtoList) {
            dto.setBatchNo(batchNo);
            DriverOnlineHours hours = importHours(dto);
            result.add(hours);
            driverDateMap.computeIfAbsent(dto.getDriverId(), k -> new HashSet<>()).add(dto.getWorkDate());
        }
        return result;
    }

    public void checkAndTriggerRiskControl(Long driverId, LocalDate workDate) {
        if (riskRestrictionService.hasActiveRestriction(driverId)) {
            log.info("司机 {} 已有生效中的限制，跳过风控判定", driverId);
            return;
        }
        LocalDate startDate = workDate.minusDays(2);
        checkConsecutiveOrders(driverId, startDate, workDate);
        checkOnlineHours(driverId, startDate, workDate);
        checkContinuousWorkHours(driverId, workDate);
    }

    private void checkConsecutiveOrders(Long driverId, LocalDate startDate, LocalDate endDate) {
        Integer totalOrders = driverOnlineHoursMapper.sumOrderCountByDateRange(driverId, startDate, endDate);
        if (totalOrders != null && totalOrders >= fatigueProperties.getMaxConsecutiveOrders()) {
            int threshold = fatigueProperties.getMaxConsecutiveOrders();
            log.info("司机 {} 连续接单 {} 单，超过阈值 {}", driverId, totalOrders, threshold);
            createRestriction(driverId, RestrictionTypeEnum.CONSECUTIVE_ORDERS,
                    String.format("近3日累计接单%d单，超过阈值%d单", totalOrders, threshold),
                    new BigDecimal(totalOrders), new BigDecimal(threshold),
                    buildConsecutiveOrdersEvidence(driverId, startDate, endDate, totalOrders));
        }
    }

    private void checkOnlineHours(Long driverId, LocalDate startDate, LocalDate endDate) {
        Integer totalMinutes = driverOnlineHoursMapper.sumOnlineMinutesByDateRange(driverId, startDate, endDate);
        if (totalMinutes != null) {
            BigDecimal hours = new BigDecimal(totalMinutes)
                    .divide(new BigDecimal(60), 2, RoundingMode.HALF_UP);
            BigDecimal threshold = new BigDecimal(fatigueProperties.getMaxOnlineHours());
            if (hours.compareTo(threshold) >= 0) {
                log.info("司机 {} 在线时长 {} 小时，超过阈值 {} 小时", driverId, hours, threshold);
                createRestriction(driverId, RestrictionTypeEnum.ONLINE_HOURS,
                        String.format("近3日累计在线%.2f小时，超过阈值%d小时", hours, fatigueProperties.getMaxOnlineHours()),
                        hours, threshold,
                        buildOnlineHoursEvidence(driverId, startDate, endDate, totalMinutes));
            }
        }
    }

    private void checkContinuousWorkHours(Long driverId, LocalDate workDate) {
        List<DriverOnlineHours> todayHours = list(new LambdaQueryWrapper<DriverOnlineHours>()
                .eq(DriverOnlineHours::getDriverId, driverId)
                .eq(DriverOnlineHours::getWorkDate, workDate)
                .orderByAsc(DriverOnlineHours::getOnlineStart));

        int maxContinuousMinutes = 0;
        int currentContinuousMinutes = 0;
        DriverOnlineHours lastHours = null;

        for (DriverOnlineHours h : todayHours) {
            if (lastHours != null) {
                long gapMinutes = ChronoUnit.MINUTES.between(lastHours.getOnlineEnd(), h.getOnlineStart());
                if (gapMinutes < fatigueProperties.getRequireRestMinutes()) {
                    currentContinuousMinutes += h.getWorkMinutes();
                } else {
                    currentContinuousMinutes = h.getWorkMinutes();
                }
            } else {
                currentContinuousMinutes = h.getWorkMinutes();
            }
            maxContinuousMinutes = Math.max(maxContinuousMinutes, currentContinuousMinutes);
            lastHours = h;
        }

        int thresholdMinutes = fatigueProperties.getConsecutiveHoursThreshold() * 60;
        if (maxContinuousMinutes >= thresholdMinutes) {
            BigDecimal hours = new BigDecimal(maxContinuousMinutes)
                    .divide(new BigDecimal(60), 2, RoundingMode.HALF_UP);
            BigDecimal threshold = new BigDecimal(fatigueProperties.getConsecutiveHoursThreshold());
            log.info("司机 {} 连续工作时长 {} 小时，超过阈值 {} 小时", driverId, hours, threshold);
            createRestriction(driverId, RestrictionTypeEnum.CONTINUOUS_WORK,
                    String.format("当日连续工作%.2f小时，超过阈值%d小时（未达到%d分钟休息）",
                            hours, fatigueProperties.getConsecutiveHoursThreshold(),
                            fatigueProperties.getRequireRestMinutes()),
                    hours, threshold,
                    buildContinuousWorkEvidence(driverId, workDate, maxContinuousMinutes, todayHours));
        }
    }

    private void createRestriction(Long driverId, RestrictionTypeEnum type, String reason,
                                   BigDecimal triggerValue, BigDecimal threshold, String evidence) {
        RiskRestriction restriction = new RiskRestriction();
        restriction.setDriverId(driverId);
        restriction.setRestrictionNo(genRestrictionNo());
        restriction.setRestrictionType(type.getCode());
        restriction.setTriggerReason(reason);
        restriction.setTriggerValue(triggerValue);
        restriction.setThresholdValue(threshold);
        restriction.setRiskLevel(calcRiskLevel(type, triggerValue, threshold));
        restriction.setRestrictionStatus(RestrictionStatusEnum.RESTRICTING.getCode());
        restriction.setStartTime(LocalDateTime.now());
        restriction.setEvidenceData(evidence);
        restriction.setCreateTime(LocalDateTime.now());
        restriction.setUpdateTime(LocalDateTime.now());
        riskRestrictionService.save(restriction);
        log.info("创建司机 {} 风控限制：{} - {}", driverId, type.getDesc(), reason);
    }

    private Integer calcRiskLevel(RestrictionTypeEnum type, BigDecimal trigger, BigDecimal threshold) {
        if (threshold.compareTo(BigDecimal.ZERO) == 0) return 2;
        BigDecimal ratio = trigger.divide(threshold, 2, RoundingMode.HALF_UP);
        if (ratio.compareTo(new BigDecimal("1.5")) >= 0) return 3;
        if (ratio.compareTo(new BigDecimal("1.2")) >= 0) return 2;
        return 1;
    }

    private String genRestrictionNo() {
        return "RS" + System.currentTimeMillis() + new Random().nextInt(1000);
    }

    private String buildConsecutiveOrdersEvidence(Long driverId, LocalDate start, LocalDate end, Integer total) {
        try {
            List<DriverOnlineHours> list = driverOnlineHoursMapper.selectByDriverAndDateRange(driverId, start, end);
            Map<String, Object> evidence = new LinkedHashMap<>();
            evidence.put("driverId", driverId);
            evidence.put("dateRange", start + " ~ " + end);
            evidence.put("totalOrders", total);
            evidence.put("threshold", fatigueProperties.getMaxConsecutiveOrders());
            List<Map<String, Object>> details = new ArrayList<>();
            for (DriverOnlineHours h : list) {
                Map<String, Object> d = new LinkedHashMap<>();
                d.put("date", h.getWorkDate());
                d.put("orderCount", h.getOrderCount());
                d.put("onlinePeriod", h.getOnlineStart() + " ~ " + h.getOnlineEnd());
                details.add(d);
            }
            evidence.put("dailyDetails", details);
            return objectMapper.writeValueAsString(evidence);
        } catch (Exception e) {
            return "{\"error\":\"evidence build failed\"}";
        }
    }

    private String buildOnlineHoursEvidence(Long driverId, LocalDate start, LocalDate end, Integer totalMinutes) {
        try {
            List<DriverOnlineHours> list = driverOnlineHoursMapper.selectByDriverAndDateRange(driverId, start, end);
            Map<String, Object> evidence = new LinkedHashMap<>();
            evidence.put("driverId", driverId);
            evidence.put("dateRange", start + " ~ " + end);
            evidence.put("totalOnlineMinutes", totalMinutes);
            evidence.put("totalOnlineHours", new BigDecimal(totalMinutes).divide(new BigDecimal(60), 2, RoundingMode.HALF_UP));
            evidence.put("thresholdHours", fatigueProperties.getMaxOnlineHours());
            List<Map<String, Object>> details = new ArrayList<>();
            for (DriverOnlineHours h : list) {
                Map<String, Object> d = new LinkedHashMap<>();
                d.put("date", h.getWorkDate());
                d.put("onlineMinutes", h.getOnlineMinutes());
                d.put("onlineHours", new BigDecimal(h.getOnlineMinutes()).divide(new BigDecimal(60), 2, RoundingMode.HALF_UP));
                d.put("workMinutes", h.getWorkMinutes());
                details.add(d);
            }
            evidence.put("dailyDetails", details);
            return objectMapper.writeValueAsString(evidence);
        } catch (Exception e) {
            return "{\"error\":\"evidence build failed\"}";
        }
    }

    private String buildContinuousWorkEvidence(Long driverId, LocalDate date, Integer maxMinutes,
                                                List<DriverOnlineHours> todayHours) {
        try {
            Map<String, Object> evidence = new LinkedHashMap<>();
            evidence.put("driverId", driverId);
            evidence.put("workDate", date);
            evidence.put("maxContinuousWorkMinutes", maxMinutes);
            evidence.put("maxContinuousWorkHours", new BigDecimal(maxMinutes).divide(new BigDecimal(60), 2, RoundingMode.HALF_UP));
            evidence.put("thresholdHours", fatigueProperties.getConsecutiveHoursThreshold());
            evidence.put("requireRestMinutes", fatigueProperties.getRequireRestMinutes());
            List<Map<String, Object>> shifts = new ArrayList<>();
            for (DriverOnlineHours h : todayHours) {
                Map<String, Object> s = new LinkedHashMap<>();
                s.put("onlineStart", h.getOnlineStart());
                s.put("onlineEnd", h.getOnlineEnd());
                s.put("workMinutes", h.getWorkMinutes());
                s.put("restMinutes", h.getRestMinutes());
                shifts.add(s);
            }
            evidence.put("shiftDetails", shifts);
            return objectMapper.writeValueAsString(evidence);
        } catch (Exception e) {
            return "{\"error\":\"evidence build failed\"}";
        }
    }

    public List<DriverOnlineHours> listByDriverAndDate(Long driverId, LocalDate start, LocalDate end) {
        return driverOnlineHoursMapper.selectByDriverAndDateRange(driverId, start, end);
    }
}
