package com.example.duan1.model;

import java.io.Serializable;

public class Product implements Serializable {
    private int soLuong;
    private String hinhSP, tenSP;
    private String maSP;
    private int fav = 0;
    private double giaSP;

    private spct spct;


    public Product(double giaSP, int soLuong, String hinhSP, String tenSP, String maSP, int fav, com.example.duan1.model.spct spct) {
        this.giaSP = giaSP;
        this.soLuong = soLuong;
        this.hinhSP = hinhSP;
        this.tenSP = tenSP;
        this.maSP = maSP;
        this.fav = fav;
        this.spct = spct;
    }

    public Product() {
    }

    public double getGiaSP() {
        return giaSP;
    }

    public void setGiaSP(double giaSP) {
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