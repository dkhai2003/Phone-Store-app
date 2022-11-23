package com.example.duan1.model;

import java.util.HashMap;
import java.util.Map;

public class Product_Type {
    private String hinhLoai, tenLoai, maLoai;
    private Product product;


    public Product_Type(String hinhLoai, String tenLoai, String maLoai, Product product) {
        this.hinhLoai = hinhLoai;
        this.tenLoai = tenLoai;
        this.maLoai = maLoai;
        this.product = product;
    }
    public Map<String, Object> toMap(){
        HashMap<String, Object> result = new HashMap<>();
        result.get("maLoai");
        return result;
    }

    public String getMaLoai() {
        return maLoai;
    }

    public void setMaLoai(String maLoai) {
        this.maLoai = maLoai;
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
