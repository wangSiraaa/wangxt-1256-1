package com.fatigue.risk.controller;

import com.fatigue.risk.common.PageResult;
import com.fatigue.risk.common.Result;
import com.fatigue.risk.dto.OnlineHoursImportDTO;
import com.fatigue.risk.dto.RestrictionQueryDTO;
import com.fatigue.risk.entity.DriverOnlineHours;
import com.fatigue.risk.service.DriverOnlineHoursService;
import com.fatigue.risk.service.RiskRestrictionService;
import com.fatigue.risk.vo.RiskRestrictionVO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/risk")
@RequiredArgsConstructor
public class RiskController {

    private final DriverOnlineHoursService driverOnlineHoursService;
    private final RiskRestrictionService riskRestrictionService;

    @PostMapping("/hours/import")
    public Result<DriverOnlineHours> importHours(@Valid @RequestBody OnlineHoursImportDTO dto) {
        return Result.success(driverOnlineHoursService.importHours(dto));
    }

    @PostMapping("/hours/batch-import")
    public Result<List<DriverOnlineHours>> batchImportHours(
            @Valid @RequestBody List<OnlineHoursImportDTO> dtoList) {
        return Result.success(driverOnlineHoursService.batchImportHours(dtoList));
    }

    @GetMapping("/hours/list")
    public Result<List<DriverOnlineHours>> listHours(
            @RequestParam Long driverId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end) {
        return Result.success(driverOnlineHoursService.listByDriverAndDate(driverId, start, end));
    }

    @PostMapping("/restriction/page")
    public Result<PageResult<RiskRestrictionVO>> restrictionPage(
            @RequestBody RestrictionQueryDTO query) {
        return Result.success(riskRestrictionService.queryPage(query));
    }

    @GetMapping("/restriction/{id}")
    public Result<RiskRestrictionVO> restrictionDetail(@PathVariable Long id) {
        return Result.success(riskRestrictionService.getDetail(id));
    }

    @GetMapping("/restriction/active/{driverId}")
    public Result<List<RiskRestrictionVO>> activeRestrictions(@PathVariable Long driverId) {
        return Result.success(riskRestrictionService.listActiveByDriver(driverId));
    }

    @GetMapping("/restriction/history/{driverId}")
    public Result<List<RiskRestrictionVO>> historyRestrictions(@PathVariable Long driverId) {
        return Result.success(riskRestrictionService.listHistoryByDriver(driverId));
    }
}
