package com.namazvakitleri.app.entity;

import com.google.gson.annotations.SerializedName;

public class ulkeler {
    @SerializedName("UlkeAdi")
    private String UlkeAdi;
    private String UlkeAdiEn;
    private String UlkeID;

    public String getUlkeAdi() {
        return UlkeAdi;
    }

    public String getUlkeAdiEn() {
        return UlkeAdiEn;
    }

    public String getUlkeID() {
        return UlkeID;
    }

    // Setter Methods

    public void setUlkeAdi(String UlkeAdi) {
        this.UlkeAdi = UlkeAdi;
    }

    public void setUlkeAdiEn(String UlkeAdiEn) {
        this.UlkeAdiEn = UlkeAdiEn;
    }

    public void setUlkeID(String UlkeID) {
        this.UlkeID = UlkeID;
    }
}
