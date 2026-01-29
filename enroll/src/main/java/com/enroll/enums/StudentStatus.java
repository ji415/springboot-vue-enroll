package com.enroll.enums;

import java.util.Arrays;

public enum StudentStatus {
    PENDING(0, "PENDING", "待审核"),
    APPROVED(1, "APPROVED", "通过"),
    REJECTED(2, "REJECTED", "驳回");

    private final int code;        // 数据库存的值
    private final String key;      // 给前端/日志用的稳定字符串
    private final String labelCn;  // 展示用中文

    StudentStatus(int code, String key, String labelCn) {
        this.code = code;
        this.key = key;
        this.labelCn = labelCn;
    }

    public int getCode() {
        return code;
    }

    public String getKey() {
        return key;
    }

    public String getLabelCn() {
        return labelCn;
    }

    public static StudentStatus fromCode(Integer code) {
        if (code == null) return null;
        return Arrays.stream(values())
                .filter(s -> s.code == code)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown StudentStatus code: " + code));
    }

    public static StudentStatus fromKey(String key) {
        if (key == null) return null;
        return Arrays.stream(values())
                .filter(s -> s.key.equalsIgnoreCase(key))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown StudentStatus key: " + key));
    }
}
