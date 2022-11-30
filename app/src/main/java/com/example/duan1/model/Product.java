package com.example.duan1.model;

import java.io.Serializable;

public class Product implements Serializable {
    private int soLuong;
    private String hinhSP, tenSP;
    private String maSP;
    private double giaSP;

    private spct spct;


    public Product(double giaSP, int soLuong, String hinhSP, String tenSP, String maSP, com.example.duan1.model.spct spct) {
        this.giaSP = giaSP;
        this.soLuong = soLuong;
        this.hinhSP = hinhSP;
        this.tenSP = tenSP;
        this.maSP = maSP;
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
    public String getTenSPsubstring() {
        if (tenSP.length() >= 15) {
            return tenSP.substring(0, 11) + "...";
        }
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

    public com.example.duan1.model.spct getSpct() {
        return spct;
    }

    public void setSpct(com.example.duan1.model.spct spct) {
        this.spct = spct;
    }
}