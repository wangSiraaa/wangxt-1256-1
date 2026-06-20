package com.fatigue.risk.enums;

import lombok.Getter;

@Getter
public enum AppealStatusEnum {
    PENDING_AUDIT(1, "待审核"),
    MATERIAL_INCOMPLETE(2, "材料不完整"),
    AUDITING(3, "审核中"),
    APPEAL_PASSED(4, "申诉通过"),
    APPEAL_REJECTED(5, "申诉驳回");

    private final Integer code;
    private final String desc;

    AppealStatusEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static String getDescByCode(Integer code) {
        for (AppealStatusEnum e : values()) {
            if (e.getCode().equals(code)) {
                return e.getDesc();
            }
        }
        return null;
    }
}
