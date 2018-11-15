package com.cisco.user.models;

public class Jadwal {
    private String idinstrukturkelas,namains,ketwaktu;

    public Jadwal(String idinstrukturkelas, String namains, String ketwaktu) {
        this.idinstrukturkelas = idinstrukturkelas;
        this.namains = namains;
        this.ketwaktu = ketwaktu;
    }

    public Jadwal() {

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

    public String getKetwaktu() {
        return ketwaktu;
    }

    public void setKetwaktu(String ketwaktu) {
        this.ketwaktu = ketwaktu;
    }
}
