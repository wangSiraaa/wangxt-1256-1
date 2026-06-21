package com.fatigue.risk.enums;

public enum MaterialRecordTypeEnum {
    FIRST_SUBMIT(1, "首次提交"),
    SUPPLEMENT(2, "补充材料"),
    RETURN_INCOMPLETE(3, "退回不完整");

    private final Integer code;
    private final String desc;

    MaterialRecordTypeEnum(Integer code, String desc) {
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
        for (MaterialRecordTypeEnum e : values()) {
            if (e.getCode().equals(code)) {
                return e.getDesc();
            }
        }
        return null;
    }
}
