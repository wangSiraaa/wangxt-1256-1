package com.fatigue.risk.enums;

public enum RestrictionTypeEnum {
    CONSECUTIVE_ORDERS(1, "连续接单超限"),
    ONLINE_HOURS(2, "在线时长超限"),
    CONTINUOUS_WORK(3, "连续工作超时");

    private final Integer code;
    private final String desc;

    RestrictionTypeEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public Integer getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public static String getDescByCode(Integer code) {
        for (RestrictionTypeEnum e : values()) {
            if (e.getCode().equals(code)) {
                return e.getDesc();
            }
        }
        return null;
    }
}
