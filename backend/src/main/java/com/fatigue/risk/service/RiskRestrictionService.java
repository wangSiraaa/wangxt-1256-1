package com.fatigue.risk.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fatigue.risk.common.PageResult;
import com.fatigue.risk.dto.RestrictionQueryDTO;
import com.fatigue.risk.entity.RiskRestriction;
import com.fatigue.risk.enums.RestrictionStatusEnum;
import com.fatigue.risk.enums.RestrictionTypeEnum;
import com.fatigue.risk.mapper.RiskRestrictionMapper;
import com.fatigue.risk.vo.RiskRestrictionVO;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RiskRestrictionService extends ServiceImpl<RiskRestrictionMapper, RiskRestriction> {

    private final RiskRestrictionMapper riskRestrictionMapper;

    public RiskRestrictionService(RiskRestrictionMapper riskRestrictionMapper) {
        this.riskRestrictionMapper = riskRestrictionMapper;
    }

    public PageResult<RiskRestrictionVO> queryPage(RestrictionQueryDTO query) {
        Page<RiskRestrictionVO> page = new Page<>(query.getPageNum(), query.getPageSize());
        IPage<RiskRestrictionVO> result = riskRestrictionMapper.selectRestrictionPage(page, query);
        enrichDesc(result.getRecords());
        return PageResult.of(result.getTotal(), result.getRecords());
    }

    public RiskRestrictionVO getDetail(Long id) {
        RiskRestrictionVO vo = riskRestrictionMapper.selectRestrictionDetail(id);
        if (vo != null) {
            enrichDesc(List.of(vo));
        }
        return vo;
    }

    public boolean hasActiveRestriction(Long driverId) {
        return riskRestrictionMapper.countActiveRestriction(driverId) > 0;
    }

    public List<RiskRestrictionVO> listActiveByDriver(Long driverId) {
        List<RiskRestriction> list = list(new LambdaQueryWrapper<RiskRestriction>()
                .eq(RiskRestriction::getDriverId, driverId)
                .in(RiskRestriction::getRestrictionStatus,
                        List.of(RestrictionStatusEnum.RESTRICTING.getCode(),
                                RestrictionStatusEnum.APPEALING.getCode()))
                .orderByDesc(RiskRestriction::getCreateTime));
        List<RiskRestrictionVO> vos = convertToVO(list);
        enrichDesc(vos);
        return vos;
    }

    public List<RiskRestrictionVO> listHistoryByDriver(Long driverId) {
        List<RiskRestriction> list = list(new LambdaQueryWrapper<RiskRestriction>()
                .eq(RiskRestriction::getDriverId, driverId)
                .orderByDesc(RiskRestriction::getCreateTime));
        List<RiskRestrictionVO> vos = convertToVO(list);
        enrichDesc(vos);
        return vos;
    }

    public void updateRestrictionStatus(Long id, RestrictionStatusEnum status) {
        lambdaUpdate()
                .eq(RiskRestriction::getId, id)
                .set(RiskRestriction::getRestrictionStatus, status.getCode())
                .set(RiskRestriction::getUpdateTime, LocalDateTime.now())
                .update();
    }

    public void releaseRestriction(Long restrictionId, Long reviewId, boolean keepEvidence) {
        lambdaUpdate()
                .eq(RiskRestriction::getId, restrictionId)
                .set(RiskRestriction::getRestrictionStatus, RestrictionStatusEnum.RELEASED.getCode())
                .set(RiskRestriction::getEndTime, LocalDateTime.now())
                .set(RiskRestriction::getReviewId, reviewId)
                .set(RiskRestriction::getUpdateTime, LocalDateTime.now())
                .update();
    }

    public void maintainRestriction(Long restrictionId, Long reviewId) {
        lambdaUpdate()
                .eq(RiskRestriction::getId, restrictionId)
                .set(RiskRestriction::getRestrictionStatus, RestrictionStatusEnum.MAINTAINED.getCode())
                .set(RiskRestriction::getReviewId, reviewId)
                .set(RiskRestriction::getUpdateTime, LocalDateTime.now())
                .update();
    }

    public void bindAppeal(Long restrictionId, Long appealId) {
        lambdaUpdate()
                .eq(RiskRestriction::getId, restrictionId)
                .set(RiskRestriction::getRestrictionStatus, RestrictionStatusEnum.APPEALING.getCode())
                .set(RiskRestriction::getAppealId, appealId)
                .set(RiskRestriction::getUpdateTime, LocalDateTime.now())
                .update();
    }

    private List<RiskRestrictionVO> convertToVO(List<RiskRestriction> list) {
        List<RiskRestrictionVO> vos = new ArrayList<>();
        for (RiskRestriction r : list) {
            RiskRestrictionVO vo = new RiskRestrictionVO();
            vo.setId(r.getId());
            vo.setDriverId(r.getDriverId());
            vo.setRestrictionNo(r.getRestrictionNo());
            vo.setRestrictionType(r.getRestrictionType());
            vo.setTriggerReason(r.getTriggerReason());
            vo.setTriggerValue(r.getTriggerValue());
            vo.setThresholdValue(r.getThresholdValue());
            vo.setRiskLevel(r.getRiskLevel());
            vo.setRestrictionStatus(r.getRestrictionStatus());
            vo.setStartTime(r.getStartTime());
            vo.setEndTime(r.getEndTime());
            vo.setAppealId(r.getAppealId());
            vo.setReviewId(r.getReviewId());
            vo.setEvidenceData(r.getEvidenceData());
            vo.setCreateTime(r.getCreateTime());
            vos.add(vo);
        }
        return vos;
    }

    private void enrichDesc(List<RiskRestrictionVO> vos) {
        if (vos == null || vos.isEmpty()) return;
        Map<Integer, String> riskLevelMap = new HashMap<>();
        riskLevelMap.put(1, "低风险");
        riskLevelMap.put(2, "中风险");
        riskLevelMap.put(3, "高风险");
        for (RiskRestrictionVO vo : vos) {
            vo.setRestrictionTypeDesc(RestrictionTypeEnum.getDescByCode(vo.getRestrictionType()));
            vo.setRestrictionStatusDesc(RestrictionStatusEnum.getDescByCode(vo.getRestrictionStatus()));
            vo.setRiskLevelDesc(riskLevelMap.get(vo.getRiskLevel()));
        }
    }
}
