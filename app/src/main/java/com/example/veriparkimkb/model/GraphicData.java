package com.example.veriparkimkb.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GraphicData {
    @SerializedName("day")
    @Expose
    private Integer day;
    @SerializedName("value")
    @Expose
    private Double value;

    public Integer getDay() {
        return day;
    }

    public void setDay(Integer day) {
        this.day = day;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }
}
