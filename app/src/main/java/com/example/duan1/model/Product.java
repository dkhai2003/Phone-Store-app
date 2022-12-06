package com.example.duan1.model;

import java.io.Serializable;

public class Product implements Serializable {
    private int giaSP,soLuong;
    private String hinhSP, tenSP,moTa;
    private String maSP;
    private int fav = 0;


    private Product_Images Product_Images;


    public Product(int giaSP, int soLuong, String hinhSP, String tenSP, String moTa, String maSP, int fav, Product_Images Product_Images) {
        this.giaSP = giaSP;
        this.soLuong = soLuong;
        this.hinhSP = hinhSP;
        this.tenSP = tenSP;
        this.moTa = moTa;
        this.maSP = maSP;
        this.fav = fav;
        this.Product_Images = Product_Images;
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

    public String getTenSPSubString17() {
        if (tenSP.length() >= 17) {
            return tenSP.substring(0, 15)+"...";
        }
        return tenSP;
    }
    public String getTenSPSubString15() {
        if (tenSP.length() > 15) {
            return tenSP.substring(0, 14)+"...";
        }
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

    public Product_Images getSpct() {
        return Product_Images;
    }

    public void setSpct(Product_Images Product_Images) {
        this.Product_Images = Product_Images;
    }
}

//        if (tenSP.length() > 5) {
//            this.tenSP = tenSP.substring(0, 7) + "...";
//        } else {
//            this.tenSP = tenSP;
//        }