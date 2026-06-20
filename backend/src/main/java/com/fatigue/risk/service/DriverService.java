package com.fatigue.risk.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fatigue.risk.common.PageResult;
import com.fatigue.risk.entity.Driver;
import com.fatigue.risk.mapper.DriverMapper;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class DriverService extends ServiceImpl<DriverMapper, Driver> {

    public Driver getByCode(String driverCode) {
        return getOne(new LambdaQueryWrapper<Driver>()
                .eq(Driver::getDriverCode, driverCode)
                .eq(Driver::getStatus, 1));
    }

    public List<Driver> listAllActive() {
        return list(new LambdaQueryWrapper<Driver>()
                .eq(Driver::getStatus, 1)
                .orderByAsc(Driver::getDriverCode));
    }

    public List<Driver> searchByKeyword(String keyword) {
        LambdaQueryWrapper<Driver> wrapper = new LambdaQueryWrapper<Driver>()
                .eq(Driver::getStatus, 1);
        if (StringUtils.hasText(keyword)) {
            wrapper.and(w -> w
                    .like(Driver::getDriverCode, keyword)
                    .or()
                    .like(Driver::getDriverName, keyword)
                    .or()
                    .like(Driver::getPhone, keyword));
        }
        wrapper.orderByAsc(Driver::getDriverCode);
        wrapper.last("LIMIT 50");
        return list(wrapper);
    }
}
