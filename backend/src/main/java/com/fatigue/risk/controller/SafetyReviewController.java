package com.fatigue.risk.controller;

import com.fatigue.risk.common.PageQuery;
import com.fatigue.risk.common.PageResult;
import com.fatigue.risk.common.Result;
import com.fatigue.risk.dto.SafetyReviewDTO;
import com.fatigue.risk.entity.SafetyReview;
import com.fatigue.risk.service.SafetyReviewService;
import com.fatigue.risk.vo.SafetyReviewVO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/review")
@RequiredArgsConstructor
public class SafetyReviewController {

    private final SafetyReviewService safetyReviewService;

    @PostMapping("/do")
    public Result<SafetyReview> doReview(@Valid @RequestBody SafetyReviewDTO dto) {
        return Result.success(safetyReviewService.doReview(dto));
    }

    @PostMapping("/page")
    public Result<PageResult<SafetyReviewVO>> page(@RequestBody PageQuery query) {
        return Result.success(safetyReviewService.queryPage(query));
    }

    @GetMapping("/{id}")
    public Result<SafetyReviewVO> detail(@PathVariable Long id) {
        return Result.success(safetyReviewService.getDetail(id));
    }

    @GetMapping("/appeal/{appealId}")
    public Result<SafetyReviewVO> getByAppeal(@PathVariable Long appealId) {
        return Result.success(safetyReviewService.getByAppealId(appealId));
    }
}
