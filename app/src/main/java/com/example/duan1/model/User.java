package com.example.duan1.model;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class User {
    private String maUser;
    private String matKhau;
    private String tenDangNhap;

    public User(String maUser, String matKhau, String tenDangNhap) {
        this.maUser = maUser;
        this.matKhau = matKhau;
        this.tenDangNhap = tenDangNhap;
    }

    public User() {
    }

    public String getMaUser() {
        return maUser;
    }

    public void setMaUser(String maUser) {
        this.maUser = maUser;
    }

    public String getMatKhau() {
        return matKhau;
    }

    public void setMatKhau(String matKhau) {
        this.matKhau = matKhau;
    }

    public String getTenDangNhap() {
        return tenDangNhap;
    }

    public void setTenDangNhap(String tenDangNhap) {
        this.tenDangNhap = tenDangNhap;
    }

//    @Exclude
//    public Map<String, Object> toMap() {
//        HashMap<String, Object> result = new HashMap<>();
//        result.put("maUser", maUser);
//        result.put("matKhau", matKhau);
//        result.put("tenDangNhap", tenDangNhap);
//
//        return result;
//    }

    @Override
    public String toString() {
        return "User{" +
                "maUser='" + maUser + '\'' +
                ", matKhau='" + matKhau + '\'' +
                ", tenDangNhap='" + tenDangNhap + '\'' +
                '}';
    }
}
