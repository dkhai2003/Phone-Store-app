package com.example.duan1.model;

public class Product_Type {
    private String hinhLoai, tenLoai;
    private Product product;

    public Product_Type(String hinhLoai, String tenLoai, Product product) {
        this.hinhLoai = hinhLoai;
        this.tenLoai = tenLoai;
        this.product = product;
    }

    public Product_Type() {
    }

    public String getHinhLoai() {
        return hinhLoai;
    }

    public void setHinhLoai(String hinhLoai) {
        this.hinhLoai = hinhLoai;
    }

    public String getTenLoai() {
        return tenLoai;
    }

    public void setTenLoai(String tenLoai) {
        this.tenLoai = tenLoai;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
