package com.example.veriparkimkb.enums;

public enum PeriodTypeEnum {
    PERIOD_ALL("all"),
    PERIOD_INCREASING("increasing"),
    PERIOD_DECREASING("decreasing"),
    PERIOD_VOLUME30("volume30"),
    PERIOD_VOLUME50("volume50"),
    PERIOD_VOLUME100("volume100");

    private String value;

    PeriodTypeEnum(String value) {
        this.value = value;
    }

    public static PeriodTypeEnum getByValue(String value) {
        for (PeriodTypeEnum e : values()) {
            if (e.value.equals(value))
                return e;
        }
        return null;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
