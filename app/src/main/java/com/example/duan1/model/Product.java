package com.example.duan1.model;

import java.io.Serializable;

public class Product implements Serializable {
    private int giaSP;
    private String hinhSP, tenSP;
    private int fav = 0;
    private spct spct;

    public com.example.duan1.model.spct getSpct() {
        return spct;
    }

    public void setSpct(com.example.duan1.model.spct spct) {
        this.spct = spct;
    }

    public Product(int giaSP, String hinhSP, String tenSP, int fav, com.example.duan1.model.spct spct) {
        this.giaSP = giaSP;
        this.hinhSP = hinhSP;
        this.tenSP = tenSP;
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

    public int getFav() {
        return fav;
    }

    public void setFav(int fav) {
        this.fav = fav;
    }

    public Product(int giaSP, String hinhSP, String tenSP, int fav) {
        this.giaSP = giaSP;
        this.hinhSP = hinhSP;
        this.tenSP = tenSP;
        this.fav = fav;
    }
}

//        if (tenSP.length() > 5) {
//            this.tenSP = tenSP.substring(0, 7) + "...";
//        } else {
//            this.tenSP = tenSP;
//        }