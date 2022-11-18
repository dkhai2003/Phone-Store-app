package com.example.duan1.model;

public class User {
    private String userName;
    private String phoneNumber;
    private String address;
    private String sex;
    private String email;
    private String job;
    private String age;
    private Boolean verifyEmail;
    private String userId;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public User() {
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public Boolean getVerifyEmail() {
        return verifyEmail;
    }

    public void setVerifyEmail(Boolean verifyEmail) {
        this.verifyEmail = verifyEmail;
    }

    public User(String email, String userName, String phoneNumber, String address, String sex, String job, String age, Boolean verifyEmail) {
        this.userName = userName;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.sex = sex;
        this.job = job;
        this.age = age;
        this.verifyEmail = verifyEmail;
        this.email = email;
    }
}
