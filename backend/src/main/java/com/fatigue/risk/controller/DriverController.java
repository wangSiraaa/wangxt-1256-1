package com.fatigue.risk.controller;

import com.fatigue.risk.common.Result;
import com.fatigue.risk.entity.Driver;
import com.fatigue.risk.service.DriverService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/driver")
@RequiredArgsConstructor
public class DriverController {

    private final DriverService driverService;

    @GetMapping("/list")
    public Result<List<Driver>> list() {
        return Result.success(driverService.listAllActive());
    }

    @GetMapping("/search")
    public Result<List<Driver>> search(@RequestParam(required = false) String keyword) {
        return Result.success(driverService.searchByKeyword(keyword));
    }

    @GetMapping("/{id}")
    public Result<Driver> getById(@PathVariable Long id) {
        return Result.success(driverService.getById(id));
    }

    @GetMapping("/code/{driverCode}")
    public Result<Driver> getByCode(@PathVariable String driverCode) {
        return Result.success(driverService.getByCode(driverCode));
    }
}
