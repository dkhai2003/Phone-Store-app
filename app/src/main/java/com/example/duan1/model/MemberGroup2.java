package com.example.duan1.model;

public class MemberGroup2 {
    private String name;
    private String dob;
    private String address;
    private String role;
    private String spe;
    private int img;
    private String link;

    public MemberGroup2(String name, String dob, String address, String role, String spe, int img, String link) {
        this.name = name;
        this.dob = dob;
        this.address = address;
        this.role = role;
        this.spe = spe;
        this.img = img;
        this.link = link;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getSpe() {
        return spe;
    }

    public void setSpe(String spe) {
        this.spe = spe;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }

}
