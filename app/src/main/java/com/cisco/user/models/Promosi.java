package com.cisco.user.models;

public class Promosi {
    private String id, judul, penulis, tanggal,gambar;

    public Promosi(String id, String judul, String penulis, String tanggal, String gambar) {
        this.id = id;
        this.judul = judul;
        this.penulis = penulis;
        this.tanggal = tanggal;
        this.gambar = gambar;
    }

    public Promosi() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getJudul() {
        return judul;
    }

    public void setJudul(String judul) {
        this.judul = judul;
    }

    public String getPenulis() {
        return penulis;
    }

    public void setPenulis(String penulis) {
        this.penulis = penulis;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public String getGambar() {
        return gambar;
    }

    public void setGambar(String gambar) {
        this.gambar = gambar;
    }
}
