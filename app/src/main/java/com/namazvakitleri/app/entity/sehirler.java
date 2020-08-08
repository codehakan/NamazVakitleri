package com.namazvakitleri.app.entity;

public class sehirler {
    private String SehirAdi;
    private String SehirAdiEn;
    private String SehirID;


    // Getter Methods

    public String getSehirAdi() {
        return SehirAdi;
    }

    public String getSehirAdiEn() {
        return SehirAdiEn;
    }

    public String getSehirID() {
        return SehirID;
    }

    // Setter Methods

    public void setSehirAdi(String SehirAdi) {
        this.SehirAdi = SehirAdi;
    }

    public void setSehirAdiEn(String SehirAdiEn) {
        this.SehirAdiEn = SehirAdiEn;
    }

    public void setSehirID(String SehirID) {
        this.SehirID = SehirID;
    }
}
