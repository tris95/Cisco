package com.cisco.user.models;

public class DataBukti {
    private String idinstrukturkelas,namains,buktipembayaran;

    public DataBukti(String idinstrukturkelas, String namains, String buktipembayaran) {
        this.idinstrukturkelas = idinstrukturkelas;
        this.namains = namains;
        this.buktipembayaran = buktipembayaran;
    }

    public DataBukti() {

    }

    public String getIdinstrukturkelas() {
        return idinstrukturkelas;
    }

    public void setIdinstrukturkelas(String idinstrukturkelas) {
        this.idinstrukturkelas = idinstrukturkelas;
    }

    public String getNamains() {
        return namains;
    }

    public void setNamains(String namains) {
        this.namains = namains;
    }

    public String getBuktipembayaran() {
        return buktipembayaran;
    }

    public void setBuktipembayaran(String buktipembayaran) {
        this.buktipembayaran = buktipembayaran;
    }
}
