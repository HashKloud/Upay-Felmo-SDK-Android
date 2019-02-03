package com.upay.upay_felmo_lib.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserAuth {
    @SerializedName("signature")
    @Expose
    private String signature;

    @SerializedName("client")
    @Expose
    private String clientKey;

    @SerializedName("Access-Country")
    @Expose
    private String countryCode;

    public UserAuth(String singnature, String clientKey, String countryCode) {
        this.signature = singnature;
        this.clientKey = clientKey;
        this.countryCode = countryCode;
    }

    public String getSingnature() {
        return signature;
    }

    public void setSingnature(String singnature) {
        this.signature = singnature;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getClientKey() {
        return clientKey;
    }

    public void setClientKey(String clientKey) {
        this.clientKey = clientKey;
    }
}
