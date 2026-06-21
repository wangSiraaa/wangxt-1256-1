package com.fatigue.risk.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "fatigue.risk")
public class FatigueProperties {
    private int maxConsecutiveOrders = 8;
    private int maxOnlineHours = 10;
    private int consecutiveHoursThreshold = 4;
    private int requireRestMinutes = 30;

    public int getMaxConsecutiveOrders() {
        return maxConsecutiveOrders;
    }

    public void setMaxConsecutiveOrders(int maxConsecutiveOrders) {
        this.maxConsecutiveOrders = maxConsecutiveOrders;
    }

    public int getMaxOnlineHours() {
        return maxOnlineHours;
    }

    public void setMaxOnlineHours(int maxOnlineHours) {
        this.maxOnlineHours = maxOnlineHours;
    }

    public int getConsecutiveHoursThreshold() {
        return consecutiveHoursThreshold;
    }

    public void setConsecutiveHoursThreshold(int consecutiveHoursThreshold) {
        this.consecutiveHoursThreshold = consecutiveHoursThreshold;
    }

    public int getRequireRestMinutes() {
        return requireRestMinutes;
    }

    public void setRequireRestMinutes(int requireRestMinutes) {
        this.requireRestMinutes = requireRestMinutes;
    }
}
