package com.fatigue.risk.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fatigue.risk.common.PageQuery;
import com.fatigue.risk.common.PageResult;
import com.fatigue.risk.dto.SafetyReviewDTO;
import com.fatigue.risk.entity.DriverAppeal;
import com.fatigue.risk.entity.SafetyReview;
import com.fatigue.risk.enums.AppealStatusEnum;
import com.fatigue.risk.enums.ReviewResultEnum;
import com.fatigue.risk.mapper.SafetyReviewMapper;
import com.fatigue.risk.vo.SafetyReviewVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

@Service
public class SafetyReviewService extends ServiceImpl<SafetyReviewMapper, SafetyReview> {

    private final SafetyReviewMapper safetyReviewMapper;
    private final DriverAppealService driverAppealService;
    private final RiskRestrictionService riskRestrictionService;

    public SafetyReviewService(SafetyReviewMapper safetyReviewMapper,
                               DriverAppealService driverAppealService,
                               RiskRestrictionService riskRestrictionService) {
        this.safetyReviewMapper = safetyReviewMapper;
        this.driverAppealService = driverAppealService;
        this.riskRestrictionService = riskRestrictionService;
    }

    @Transactional(rollbackFor = Exception.class)
    public SafetyReview doReview(SafetyReviewDTO dto) {
        DriverAppeal appeal = driverAppealService.getById(dto.getAppealId());
        if (appeal == null) {
            throw new RuntimeException("申诉记录不存在");
        }
        if (appeal.getMaterialComplete() == null || !appeal.getMaterialComplete().equals(1)) {
            throw new RuntimeException("申诉材料不完整，无法进入安全复核流程");
        }
        if (appeal.getAppealStatus().equals(AppealStatusEnum.APPEAL_PASSED.getCode())
                || appeal.getAppealStatus().equals(AppealStatusEnum.APPEAL_REJECTED.getCode())) {
            throw new RuntimeException("该申诉已完成复核");
        }

        SafetyReview review = new SafetyReview();
        review.setAppealId(dto.getAppealId());
        review.setRestrictionId(dto.getRestrictionId());
        review.setDriverId(appeal.getDriverId());
        review.setReviewNo(genReviewNo());
        review.setReviewerId(dto.getReviewerId());
        review.setReviewerName(dto.getReviewerName());
        review.setReviewResult(dto.getReviewResult());
        review.setReviewRemark(dto.getReviewRemark());
        review.setVerifyEvidence(dto.getVerifyEvidence());
        review.setRestVerifiedMinutes(dto.getRestVerifiedMinutes());
        review.setReviewTime(LocalDateTime.now());
        review.setKeepEvidence(dto.getKeepEvidence() != null ? dto.getKeepEvidence() : 1);
        review.setCreateTime(LocalDateTime.now());
        review.setUpdateTime(LocalDateTime.now());
        save(review);

        if (ReviewResultEnum.RELEASE_RESTRICTION.getCode().equals(dto.getReviewResult())) {
            driverAppealService.updateAppealStatus(dto.getAppealId(), AppealStatusEnum.APPEAL_PASSED);
            boolean keepEv = review.getKeepEvidence() != null && review.getKeepEvidence() == 1;
            riskRestrictionService.releaseRestriction(dto.getRestrictionId(), review.getId(), keepEv);
        } else {
            driverAppealService.updateAppealStatus(dto.getAppealId(), AppealStatusEnum.APPEAL_REJECTED);
            riskRestrictionService.maintainRestriction(dto.getRestrictionId(), review.getId());
        }
        return review;
    }

    public PageResult<SafetyReviewVO> queryPage(PageQuery query) {
        Page<SafetyReviewVO> page = new Page<>(query.getPageNum(), query.getPageSize());
        IPage<SafetyReviewVO> result = safetyReviewMapper.selectReviewPage(page, query);
        enrichDesc(result.getRecords());
        return PageResult.of(result.getTotal(), result.getRecords());
    }

    public SafetyReviewVO getDetail(Long id) {
        SafetyReviewVO vo = safetyReviewMapper.selectReviewDetail(id);
        if (vo != null) {
            enrichDesc(List.of(vo));
        }
        return vo;
    }

    public SafetyReviewVO getByAppealId(Long appealId) {
        SafetyReview review = lambdaQuery()
                .eq(SafetyReview::getAppealId, appealId)
                .one();
        if (review == null) return null;
        return getDetail(review.getId());
    }

    private String genReviewNo() {
        return "RV" + System.currentTimeMillis() + new Random().nextInt(1000);
    }

    private void enrichDesc(List<SafetyReviewVO> vos) {
        if (vos == null || vos.isEmpty()) return;
        for (SafetyReviewVO vo : vos) {
            vo.setReviewResultDesc(ReviewResultEnum.getDescByCode(vo.getReviewResult()));
        }
    }
}
