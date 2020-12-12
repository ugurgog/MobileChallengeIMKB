package com.example.veriparkimkb.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class HandshakeResponseModel{
    @SerializedName("aesKey")
    @Expose
    private String aesKey;
    @SerializedName("aesIV")
    @Expose
    private String aesIV;
    @SerializedName("authorization")
    @Expose
    private String authorization;
    @SerializedName("lifeTime")
    @Expose
    private String lifeTime;
    @SerializedName("status")
    @Expose
    private Status status;

    public String getAesKey() {
        return aesKey;
    }

    public void setAesKey(String aesKey) {
        this.aesKey = aesKey;
    }

    public String getAesIV() {
        return aesIV;
    }

    public void setAesIV(String aesIV) {
        this.aesIV = aesIV;
    }

    public String getAuthorization() {
        return authorization;
    }

    public void setAuthorization(String authorization) {
        this.authorization = authorization;
    }

    public String getLifeTime() {
        return lifeTime;
    }

    public void setLifeTime(String lifeTime) {
        this.lifeTime = lifeTime;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
