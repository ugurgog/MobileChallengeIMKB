package com.example.veriparkimkb.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class StockListResponseModel {
    @SerializedName("stocks")
    @Expose
    private List<StockListItemModel> stocks = null;
    @SerializedName("status")
    @Expose
    private Status status;

    public List<StockListItemModel> getStocks() {
        return stocks;
    }

    public void setStocks(List<StockListItemModel> stocks) {
        this.stocks = stocks;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
