package com.fatigue.risk.enums;

public enum RestrictionStatusEnum {
    RESTRICTING(1, "限制中"),
    APPEALING(2, "申诉中"),
    RELEASED(3, "解除限制"),
    MAINTAINED(4, "维持限制");

    private final Integer code;
    private final String desc;

    RestrictionStatusEnum(Integer code, String desc) {
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
        for (RestrictionStatusEnum e : values()) {
            if (e.getCode().equals(code)) {
                return e.getDesc();
            }
        }
        return null;
    }
}
