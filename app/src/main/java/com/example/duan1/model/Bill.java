package com.example.duan1.model;

import java.io.Serializable;
import java.util.Date;

public class Bill implements Serializable {
    private String date, maHoaDon, toTal;
    private int soLuong;


//
    public Bill() {
    }

    public Bill(String date, String maHoaDon, String toTal, int soLuong) {
        this.date = date;
        this.maHoaDon = maHoaDon;
        this.toTal = toTal;
        this.soLuong = soLuong;
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

    public String getToTal() {
        return toTal;
    }

    public void setToTal(String toTal) {
        this.toTal = toTal;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }
}
