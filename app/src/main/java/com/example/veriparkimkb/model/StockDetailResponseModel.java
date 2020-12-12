package com.example.veriparkimkb.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class StockDetailResponseModel {
    @SerializedName("isDown")
    @Expose
    private Boolean isDown;
    @SerializedName("isUp")
    @Expose
    private Boolean isUp;
    @SerializedName("bid")
    @Expose
    private Double bid;
    @SerializedName("channge")
    @Expose
    private Double channge;
    @SerializedName("count")
    @Expose
    private Integer count;
    @SerializedName("difference")
    @Expose
    private Double difference;
    @SerializedName("offer")
    @Expose
    private Double offer;
    @SerializedName("highest")
    @Expose
    private Double highest;
    @SerializedName("lowest")
    @Expose
    private Double lowest;
    @SerializedName("maximum")
    @Expose
    private Double maximum;
    @SerializedName("minimum")
    @Expose
    private Double minimum;
    @SerializedName("price")
    @Expose
    private Double price;
    @SerializedName("volume")
    @Expose
    private Double volume;
    @SerializedName("symbol")
    @Expose
    private String symbol;
    @SerializedName("graphicData")
    @Expose
    private List<GraphicData> graphicData = null;
    @SerializedName("status")
    @Expose
    private Status status;

    public Boolean getIsDown() {
        return isDown;
    }

    public void setIsDown(Boolean isDown) {
        this.isDown = isDown;
    }

    public Boolean getIsUp() {
        return isUp;
    }

    public void setIsUp(Boolean isUp) {
        this.isUp = isUp;
    }

    public Double getBid() {
        return bid;
    }

    public void setBid(Double bid) {
        this.bid = bid;
    }

    public Double getChannge() {
        return channge;
    }

    public void setChannge(Double channge) {
        this.channge = channge;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Double getDifference() {
        return difference;
    }

    public void setDifference(Double difference) {
        this.difference = difference;
    }

    public Double getOffer() {
        return offer;
    }

    public void setOffer(Double offer) {
        this.offer = offer;
    }

    public Double getHighest() {
        return highest;
    }

    public void setHighest(Double highest) {
        this.highest = highest;
    }

    public Double getLowest() {
        return lowest;
    }

    public void setLowest(Double lowest) {
        this.lowest = lowest;
    }

    public Double getMaximum() {
        return maximum;
    }

    public void setMaximum(Double maximum) {
        this.maximum = maximum;
    }

    public Double getMinimum() {
        return minimum;
    }

    public void setMinimum(Double minimum) {
        this.minimum = minimum;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getVolume() {
        return volume;
    }

    public void setVolume(Double volume) {
        this.volume = volume;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public List<GraphicData> getGraphicData() {
        return graphicData;
    }

    public void setGraphicData(List<GraphicData> graphicData) {
        this.graphicData = graphicData;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
