package com.fatigue.risk.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fatigue.risk.common.PageResult;
import com.fatigue.risk.dto.AppealSubmitDTO;
import com.fatigue.risk.dto.AppealQueryDTO;
import com.fatigue.risk.dto.AppealSupplementDTO;
import com.fatigue.risk.dto.MaterialCheckDTO;
import com.fatigue.risk.entity.AppealMaterialRecord;
import com.fatigue.risk.entity.Driver;
import com.fatigue.risk.entity.DriverAppeal;
import com.fatigue.risk.entity.RiskRestriction;
import com.fatigue.risk.enums.AppealStatusEnum;
import com.fatigue.risk.enums.MaterialRecordTypeEnum;
import com.fatigue.risk.enums.RestrictionStatusEnum;
import com.fatigue.risk.mapper.AppealMaterialRecordMapper;
import com.fatigue.risk.mapper.DriverAppealMapper;
import com.fatigue.risk.vo.AppealMaterialRecordVO;
import com.fatigue.risk.vo.DriverAppealVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class DriverAppealService extends ServiceImpl<DriverAppealMapper, DriverAppeal> {

    private final DriverAppealMapper driverAppealMapper;
    private final AppealMaterialRecordMapper appealMaterialRecordMapper;
    private final RiskRestrictionService riskRestrictionService;
    private final DriverService driverService;

    public DriverAppealService(DriverAppealMapper driverAppealMapper,
                               AppealMaterialRecordMapper appealMaterialRecordMapper,
                               RiskRestrictionService riskRestrictionService,
                               DriverService driverService) {
        this.driverAppealMapper = driverAppealMapper;
        this.appealMaterialRecordMapper = appealMaterialRecordMapper;
        this.riskRestrictionService = riskRestrictionService;
        this.driverService = driverService;
    }

    @Transactional(rollbackFor = Exception.class)
    public DriverAppeal submitAppeal(AppealSubmitDTO dto) {
        RiskRestriction restriction = riskRestrictionService.getById(dto.getRestrictionId());
        if (restriction == null) {
            throw new RuntimeException("风控限制记录不存在");
        }
        if (restriction.getRestrictionStatus().equals(RestrictionStatusEnum.RELEASED.getCode())
                || restriction.getRestrictionStatus().equals(RestrictionStatusEnum.MAINTAINED.getCode())) {
            throw new RuntimeException("该限制已处理完成，无法申诉");
        }
        if (restriction.getAppealId() != null) {
            throw new RuntimeException("该限制已存在申诉记录");
        }

        DriverAppeal appeal = new DriverAppeal();
        appeal.setRestrictionId(dto.getRestrictionId());
        appeal.setDriverId(dto.getDriverId());
        appeal.setAppealNo(genAppealNo());
        appeal.setAppealReason(dto.getAppealReason());
        appeal.setRestProofUrl(dto.getRestProofUrl());
        appeal.setRestProofDesc(dto.getRestProofDesc());
        appeal.setRestStartTime(dto.getRestStartTime());
        appeal.setRestEndTime(dto.getRestEndTime());
        appeal.setRestMinutes(dto.getRestMinutes());
        appeal.setMaterialComplete(checkMaterialComplete(dto) ? 1 : 0);
        if (appeal.getMaterialComplete() == 0) {
            appeal.setIncompleteReason("休息证明文件或休息时段信息不完整");
        }
        appeal.setAppealStatus(appeal.getMaterialComplete() == 1
                ? AppealStatusEnum.PENDING_AUDIT.getCode()
                : AppealStatusEnum.MATERIAL_INCOMPLETE.getCode());
        appeal.setSubmitTime(LocalDateTime.now());
        appeal.setCreateTime(LocalDateTime.now());
        appeal.setUpdateTime(LocalDateTime.now());
        save(appeal);

        riskRestrictionService.bindAppeal(dto.getRestrictionId(), appeal.getId());

        saveMaterialRecord(appeal.getId(), dto.getDriverId(), MaterialRecordTypeEnum.FIRST_SUBMIT,
                dto.getRestProofUrl(), dto.getRestProofDesc(), dto.getRestStartTime(),
                dto.getRestEndTime(), dto.getRestMinutes(), dto.getAppealReason(), null);

        return appeal;
    }

    public boolean checkMaterialComplete(AppealSubmitDTO dto) {
        boolean hasProof = StringUtils.hasText(dto.getRestProofUrl());
        boolean hasTimeRange = dto.getRestStartTime() != null && dto.getRestEndTime() != null;
        boolean hasMinutes = dto.getRestMinutes() != null && dto.getRestMinutes() > 0;
        boolean hasReason = StringUtils.hasText(dto.getAppealReason());
        return hasProof && hasTimeRange && hasMinutes && hasReason;
    }

    @Transactional(rollbackFor = Exception.class)
    public DriverAppeal checkMaterial(MaterialCheckDTO dto) {
        DriverAppeal appeal = getById(dto.getAppealId());
        if (appeal == null) {
            throw new RuntimeException("申诉记录不存在");
        }
        if (!appeal.getAppealStatus().equals(AppealStatusEnum.MATERIAL_INCOMPLETE.getCode())
                && !appeal.getAppealStatus().equals(AppealStatusEnum.PENDING_AUDIT.getCode())) {
            throw new RuntimeException("申诉状态不支持材料检查");
        }

        lambdaUpdate()
                .eq(DriverAppeal::getId, dto.getAppealId())
                .set(DriverAppeal::getMaterialComplete, dto.getMaterialComplete())
                .set(DriverAppeal::getIncompleteReason, dto.getIncompleteReason())
                .set(DriverAppeal::getAuditorId, dto.getAuditorId())
                .set(DriverAppeal::getAuditRemark, dto.getAuditRemark())
                .set(DriverAppeal::getAuditTime, LocalDateTime.now())
                .set(DriverAppeal::getAppealStatus, dto.getMaterialComplete() == 1
                        ? AppealStatusEnum.AUDITING.getCode()
                        : AppealStatusEnum.MATERIAL_INCOMPLETE.getCode())
                .set(DriverAppeal::getUpdateTime, LocalDateTime.now())
                .update();

        if (dto.getMaterialComplete() != 1) {
            riskRestrictionService.updateRestrictionStatus(appeal.getRestrictionId(), RestrictionStatusEnum.RESTRICTING);

            saveMaterialRecord(dto.getAppealId(), dto.getAuditorId(), MaterialRecordTypeEnum.RETURN_INCOMPLETE,
                    null, null, null, null, null, null, dto.getIncompleteReason());
        }
        return getById(dto.getAppealId());
    }

    @Transactional(rollbackFor = Exception.class)
    public DriverAppeal supplementMaterial(AppealSupplementDTO dto) {
        DriverAppeal appeal = getById(dto.getAppealId());
        if (appeal == null) {
            throw new RuntimeException("申诉记录不存在");
        }
        if (!appeal.getAppealStatus().equals(AppealStatusEnum.MATERIAL_INCOMPLETE.getCode())) {
            throw new RuntimeException("当前申诉状态不支持补充材料，仅材料不完整时可补充");
        }
        if (!appeal.getDriverId().equals(dto.getDriverId())) {
            throw new RuntimeException("只能补充本人的申诉材料");
        }

        boolean hasProof = StringUtils.hasText(dto.getRestProofUrl());
        boolean hasTimeRange = dto.getRestStartTime() != null && dto.getRestEndTime() != null;
        boolean hasMinutes = dto.getRestMinutes() != null && dto.getRestMinutes() > 0;
        boolean hasReason = StringUtils.hasText(dto.getAppealReason());
        boolean complete = hasProof && hasTimeRange && hasMinutes && hasReason;

        lambdaUpdate()
                .eq(DriverAppeal::getId, dto.getAppealId())
                .set(hasProof && StringUtils.hasText(dto.getRestProofUrl()), DriverAppeal::getRestProofUrl, dto.getRestProofUrl())
                .set(StringUtils.hasText(dto.getRestProofDesc()), DriverAppeal::getRestProofDesc, dto.getRestProofDesc())
                .set(dto.getRestStartTime() != null, DriverAppeal::getRestStartTime, dto.getRestStartTime())
                .set(dto.getRestEndTime() != null, DriverAppeal::getRestEndTime, dto.getRestEndTime())
                .set(dto.getRestMinutes() != null, DriverAppeal::getRestMinutes, dto.getRestMinutes())
                .set(hasReason, DriverAppeal::getAppealReason, dto.getAppealReason())
                .set(DriverAppeal::getMaterialComplete, complete ? 1 : 0)
                .set(DriverAppeal::getIncompleteReason, complete ? null : "补充材料仍不完整，请检查证明文件、休息时段和申诉理由")
                .set(DriverAppeal::getAppealStatus, complete
                        ? AppealStatusEnum.PENDING_AUDIT.getCode()
                        : AppealStatusEnum.MATERIAL_INCOMPLETE.getCode())
                .set(DriverAppeal::getSubmitTime, LocalDateTime.now())
                .set(DriverAppeal::getUpdateTime, LocalDateTime.now())
                .update();

        saveMaterialRecord(dto.getAppealId(), dto.getDriverId(), MaterialRecordTypeEnum.SUPPLEMENT,
                dto.getRestProofUrl(), dto.getRestProofDesc(), dto.getRestStartTime(),
                dto.getRestEndTime(), dto.getRestMinutes(), dto.getAppealReason(), null);

        return getById(dto.getAppealId());
    }

    public List<AppealMaterialRecordVO> listMaterialRecords(Long appealId) {
        List<AppealMaterialRecordVO> records = appealMaterialRecordMapper.selectByAppealId(appealId);
        if (records != null) {
            for (AppealMaterialRecordVO vo : records) {
                vo.setRecordTypeDesc(MaterialRecordTypeEnum.getDescByCode(vo.getRecordType()));
                if (!StringUtils.hasText(vo.getOperatorName()) && StringUtils.hasText(vo.getDefaultOperatorName())) {
                    vo.setOperatorName(vo.getDefaultOperatorName());
                }
            }
        }
        return records;
    }

    public PageResult<DriverAppealVO> queryPage(AppealQueryDTO query) {
        Page<DriverAppealVO> page = new Page<>(query.getPageNum(), query.getPageSize());
        IPage<DriverAppealVO> result = driverAppealMapper.selectAppealPage(page, query);
        enrichDesc(result.getRecords());
        return PageResult.of(result.getTotal(), result.getRecords());
    }

    public DriverAppealVO getDetail(Long id) {
        DriverAppealVO vo = driverAppealMapper.selectAppealDetail(id);
        if (vo != null) {
            enrichDesc(List.of(vo));
            vo.setMaterialRecords(listMaterialRecords(id));
        }
        return vo;
    }

    public void updateAppealStatus(Long appealId, AppealStatusEnum status) {
        lambdaUpdate()
                .eq(DriverAppeal::getId, appealId)
                .set(DriverAppeal::getAppealStatus, status.getCode())
                .set(DriverAppeal::getUpdateTime, LocalDateTime.now())
                .update();
    }

    private String genAppealNo() {
        return "AP" + System.currentTimeMillis() + new Random().nextInt(1000);
    }

    private void enrichDesc(List<DriverAppealVO> vos) {
        if (vos == null || vos.isEmpty()) return;
        for (DriverAppealVO vo : vos) {
            vo.setAppealStatusDesc(AppealStatusEnum.getDescByCode(vo.getAppealStatus()));
        }
    }

    public List<DriverAppealVO> listByDriver(Long driverId) {
        List<DriverAppeal> list = list(new LambdaQueryWrapper<DriverAppeal>()
                .eq(DriverAppeal::getDriverId, driverId)
                .orderByDesc(DriverAppeal::getCreateTime));
        return list.stream().map(a -> {
            DriverAppealVO vo = toVo(a);
            vo.setMaterialRecords(listMaterialRecords(a.getId()));
            return vo;
        }).collect(Collectors.toList());
    }

    private DriverAppealVO toVo(DriverAppeal a) {
        DriverAppealVO vo = new DriverAppealVO();
        vo.setId(a.getId());
        vo.setRestrictionId(a.getRestrictionId());
        vo.setDriverId(a.getDriverId());
        vo.setAppealNo(a.getAppealNo());
        vo.setAppealReason(a.getAppealReason());
        vo.setRestProofUrl(a.getRestProofUrl());
        vo.setRestProofDesc(a.getRestProofDesc());
        vo.setRestStartTime(a.getRestStartTime());
        vo.setRestEndTime(a.getRestEndTime());
        vo.setRestMinutes(a.getRestMinutes());
        vo.setMaterialComplete(a.getMaterialComplete());
        vo.setIncompleteReason(a.getIncompleteReason());
        vo.setAppealStatus(a.getAppealStatus());
        vo.setAppealStatusDesc(AppealStatusEnum.getDescByCode(a.getAppealStatus()));
        vo.setSubmitTime(a.getSubmitTime());
        vo.setAuditorId(a.getAuditorId());
        vo.setAuditTime(a.getAuditTime());
        vo.setAuditRemark(a.getAuditRemark());
        vo.setCreateTime(a.getCreateTime());
        return vo;
    }

    private void saveMaterialRecord(Long appealId, Long operatorId, MaterialRecordTypeEnum type,
                                    String proofUrl, String proofDesc, LocalDateTime startTime,
                                    LocalDateTime endTime, Integer minutes, String reason,
                                    String remark) {
        AppealMaterialRecord record = new AppealMaterialRecord();
        record.setAppealId(appealId);
        record.setRecordType(type.getCode());
        record.setRestProofUrl(proofUrl);
        record.setRestProofDesc(proofDesc);
        record.setRestStartTime(startTime);
        record.setRestEndTime(endTime);
        record.setRestMinutes(minutes);
        record.setAppealReason(reason);
        record.setOperateRemark(remark);
        record.setOperatorId(operatorId);
        if (operatorId != null) {
            Driver driver = driverService.getById(operatorId);
            if (driver != null) {
                record.setOperatorName(driver.getDriverName());
            }
        }
        record.setSubmitTime(LocalDateTime.now());
        record.setCreateTime(LocalDateTime.now());
        record.setUpdateTime(LocalDateTime.now());
        appealMaterialRecordMapper.insert(record);
    }
}
