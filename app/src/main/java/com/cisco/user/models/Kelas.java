package com.cisco.user.models;

public class Kelas {
    private  String idinstrukturkelas,nm_kelas, namadetailkelas, harga,namains, alamat, nohp, email, minkelas, maxkelas,jumlahkelas, ketwaktu, tanggal;

    public Kelas(String idinstrukturkelas, String nm_kelas, String namadetailkelas, String harga, String namains, String alamat, String nohp, String email,
                 String minkelas, String maxkelas, String jumlahkelas, String ketwaktu, String tanggal) {
        this.idinstrukturkelas = idinstrukturkelas;
        this.nm_kelas = nm_kelas;
        this.namadetailkelas = namadetailkelas;
        this.harga = harga;
        this.namains = namains;
        this.alamat = alamat;
        this.nohp = nohp;
        this.email = email;
        this.minkelas = minkelas;
        this.maxkelas = maxkelas;
        this.jumlahkelas = jumlahkelas;
        this.ketwaktu = ketwaktu;
        this.tanggal = tanggal;
    }

    public Kelas() {

    }

    public String getIdinstrukturkelas() {
        return idinstrukturkelas;
    }

    public void setIdinstrukturkelas(String idinstrukturkelas) {
        this.idinstrukturkelas = idinstrukturkelas;
    }

    public String getNm_kelas() {
        return nm_kelas;
    }

    public void setNm_kelas(String nm_kelas) {
        this.nm_kelas = nm_kelas;
    }

    public String getNamadetailkelas() {
        return namadetailkelas;
    }

    public void setNamadetailkelas(String namadetailkelas) {
        this.namadetailkelas = namadetailkelas;
    }

    public String getHarga() {
        return harga;
    }

    public void setHarga(String harga) {
        this.harga = harga;
    }

    public String getNamains() {
        return namains;
    }

    public void setNamains(String namains) {
        this.namains = namains;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getNohp() {
        return nohp;
    }

    public void setNohp(String nohp) {
        this.nohp = nohp;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMinkelas() {
        return minkelas;
    }

    public void setMinkelas(String minkelas) {
        this.minkelas = minkelas;
    }

    public String getMaxkelas() {
        return maxkelas;
    }

    public void setMaxkelas(String maxkelas) {
        this.maxkelas = maxkelas;
    }

    public String getJumlahkelas() {
        return jumlahkelas;
    }

    public void setJumlahkelas(String jumlahkelas) {
        this.jumlahkelas = jumlahkelas;
    }

    public String getKetwaktu() {
        return ketwaktu;
    }

    public void setKetwaktu(String ketwaktu) {
        this.ketwaktu = ketwaktu;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }
}
