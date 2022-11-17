package com.example.duan1.model;

public class Product {
    private String motTa, tenSanPham, image;
    private int gia, soLuong;

    public Product(String motTa, String tenSanPham, String image, int gia, int soLuong) {
        this.motTa = motTa;
        this.tenSanPham = tenSanPham;
        this.image = image;
        this.gia = gia;
        this.soLuong = soLuong;
    }

    public Product() {
    }

    public String getMotTa() {
        return motTa;
    }

    public void setMotTa(String motTa) {
        this.motTa = motTa;
    }

    public String getTenSanPham() {
        return tenSanPham;
    }

    public void setTenSanPham(String tenSanPham) {
        this.tenSanPham = tenSanPham;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getGia() {
        return gia;
    }

    public void setGia(int gia) {
        this.gia = gia;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }
}
