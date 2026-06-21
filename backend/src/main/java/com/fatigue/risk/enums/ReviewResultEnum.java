package com.fatigue.risk.enums;

public enum ReviewResultEnum {
    RELEASE_RESTRICTION(1, "解除限制"),
    MAINTAIN_RESTRICTION(2, "维持限制");

    private final Integer code;
    private final String desc;

    ReviewResultEnum(Integer code, String desc) {
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
        for (ReviewResultEnum e : values()) {
            if (e.getCode().equals(code)) {
                return e.getDesc();
            }
        }
        return null;
    }
}
