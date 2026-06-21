package com.fatigue.risk.controller;

import com.fatigue.risk.common.PageResult;
import com.fatigue.risk.common.Result;
import com.fatigue.risk.dto.AppealSubmitDTO;
import com.fatigue.risk.dto.AppealQueryDTO;
import com.fatigue.risk.dto.AppealSupplementDTO;
import com.fatigue.risk.dto.MaterialCheckDTO;
import com.fatigue.risk.entity.DriverAppeal;
import com.fatigue.risk.service.DriverAppealService;
import com.fatigue.risk.vo.AppealMaterialRecordVO;
import com.fatigue.risk.vo.DriverAppealVO;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/appeal")
public class DriverAppealController {

    private final DriverAppealService driverAppealService;

    public DriverAppealController(DriverAppealService driverAppealService) {
        this.driverAppealService = driverAppealService;
    }

    @PostMapping("/submit")
    public Result<DriverAppeal> submit(@Valid @RequestBody AppealSubmitDTO dto) {
        return Result.success(driverAppealService.submitAppeal(dto));
    }

    @PostMapping("/supplement")
    public Result<DriverAppeal> supplement(@Valid @RequestBody AppealSupplementDTO dto) {
        return Result.success(driverAppealService.supplementMaterial(dto));
    }

    @PostMapping("/material-check")
    public Result<DriverAppeal> materialCheck(@Valid @RequestBody MaterialCheckDTO dto) {
        return Result.success(driverAppealService.checkMaterial(dto));
    }

    @PostMapping("/page")
    public Result<PageResult<DriverAppealVO>> page(@RequestBody AppealQueryDTO query) {
        return Result.success(driverAppealService.queryPage(query));
    }

    @GetMapping("/{id}")
    public Result<DriverAppealVO> detail(@PathVariable Long id) {
        return Result.success(driverAppealService.getDetail(id));
    }

    @GetMapping("/driver/{driverId}")
    public Result<List<DriverAppealVO>> listByDriver(@PathVariable Long driverId) {
        return Result.success(driverAppealService.listByDriver(driverId));
    }

    @GetMapping("/{id}/material-records")
    public Result<List<AppealMaterialRecordVO>> listMaterialRecords(@PathVariable Long id) {
        return Result.success(driverAppealService.listMaterialRecords(id));
    }
}
