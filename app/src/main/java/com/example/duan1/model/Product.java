package com.example.duan1.model;

public class Product {
    private int giaSP;
    private String hinhSP, tenSP;

    public Product(int giaSP, String hinhSP, String tenSP) {
        this.giaSP = giaSP;
        this.hinhSP = hinhSP;
        this.tenSP = tenSP;
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
//        if (tenSP.length() > 5) {
//            this.tenSP = tenSP.substring(0, 7) + "...";
//        } else {
//            this.tenSP = tenSP;
//        }
        return tenSP;
    }

    public void setTenSP(String tenSP) {
        this.tenSP = tenSP;
    }
}
