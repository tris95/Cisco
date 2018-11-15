package com.cisco.user.models;

public class User {
    private String  nim, firstname, lastname, email, prodi,gambar;

    public User(String nim, String firstname, String lastname, String email, String prodi,String gambar) {
        this.nim = nim;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.prodi = prodi;
        this.gambar = gambar;
    }

    public String getNim() {
        return nim;
    }

    public void setNim(String nim) {
        this.nim = nim;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProdi() {
        return prodi;
    }

    public void setProdi(String prodi) {
        this.prodi = prodi;
    }

    public String getGambar() {
        return gambar;
    }

    public void setGambar(String gambar) {
        this.gambar = gambar;
    }
}
