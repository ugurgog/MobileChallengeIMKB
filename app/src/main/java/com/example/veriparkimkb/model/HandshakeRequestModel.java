package com.example.veriparkimkb.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class HandshakeRequestModel {
    @SerializedName("deviceId")
    @Expose
    private String deviceId;
    @SerializedName("systemVersion")
    @Expose
    private String systemVersion;
    @SerializedName("platformName")
    @Expose
    private String platformName;
    @SerializedName("deviceModel")
    @Expose
    private String deviceModel;
    @SerializedName("manifacturer")
    @Expose
    private String manifacturer;

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getSystemVersion() {
        return systemVersion;
    }

    public void setSystemVersion(String systemVersion) {
        this.systemVersion = systemVersion;
    }

    public String getPlatformName() {
        return platformName;
    }

    public void setPlatformName(String platformName) {
        this.platformName = platformName;
    }

    public String getDeviceModel() {
        return deviceModel;
    }

    public void setDeviceModel(String deviceModel) {
        this.deviceModel = deviceModel;
    }

    public String getManifacturer() {
        return manifacturer;
    }

    public void setManifacturer(String manifacturer) {
        this.manifacturer = manifacturer;
    }
}
