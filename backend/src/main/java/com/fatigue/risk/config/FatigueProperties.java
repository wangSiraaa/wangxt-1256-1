package com.fatigue.risk.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "fatigue.risk")
public class FatigueProperties {
    private int maxConsecutiveOrders = 8;
    private int maxOnlineHours = 10;
    private int consecutiveHoursThreshold = 4;
    private int requireRestMinutes = 30;
}
