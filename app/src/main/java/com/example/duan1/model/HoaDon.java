package com.example.duan1.model;

import java.util.Date;

public class HoaDon {
    private String date, maHoaDon;
    private int soLuong;
    private double Total;


    public HoaDon() {
    }

    public HoaDon(String date, String maHoaDon, int soLuong, double total) {
        this.date = date;
        this.maHoaDon = maHoaDon;
        this.soLuong = soLuong;
        Total = total;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMaHoaDon() {
        return maHoaDon;
    }

    public void setMaHoaDon(String maHoaDon) {
        this.maHoaDon = maHoaDon;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }

    public double getTotal() {
        return Total;
    }

    public void setTotal(double total) {
        Total = total;
    }
}
