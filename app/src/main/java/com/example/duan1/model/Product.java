package com.example.duan1.model;

import java.io.Serializable;

public class Product implements Serializable {
    private int giaSP,soLuong;
    private String hinhSP, tenSP,moTa;
    private String maSP;
    private int fav = 0;


    private spct spct;


    public Product(int giaSP, int soLuong, String hinhSP, String tenSP, String moTa, String maSP, int fav, com.example.duan1.model.spct spct) {
        this.giaSP = giaSP;
        this.soLuong = soLuong;
        this.hinhSP = hinhSP;
        this.tenSP = tenSP;
        this.moTa = moTa;
        this.maSP = maSP;
        this.fav = fav;
        this.spct = spct;
    }

    public Product() {
    }

    public int getGiaSP() {
        return giaSP;
    }

    public void setGiaSP(int giaSP) {
        this.giaSP = giaSP;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }

    public String getHinhSP() {
        return hinhSP;
    }

    public void setHinhSP(String hinhSP) {
        this.hinhSP = hinhSP;
    }

    public String getTenSP() {
        return tenSP;
    }

    public void setTenSP(String tenSP) {
        this.tenSP = tenSP;
    }

    public String getMoTa() {
        return moTa;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }

    public String getMaSP() {
        return maSP;
    }

    public void setMaSP(String maSP) {
        this.maSP = maSP;
    }

    public int getFav() {
        return fav;
    }

    public void setFav(int fav) {
        this.fav = fav;
    }

    public com.example.duan1.model.spct getSpct() {
        return spct;
    }

    public void setSpct(com.example.duan1.model.spct spct) {
        this.spct = spct;
    }
}

//        if (tenSP.length() > 5) {
//            this.tenSP = tenSP.substring(0, 7) + "...";
//        } else {
//            this.tenSP = tenSP;
//        }