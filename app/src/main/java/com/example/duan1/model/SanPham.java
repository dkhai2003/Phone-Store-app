package com.example.duan1.model;

public class SanPham {
    private int imgAllItem;
    private String nameItems;
    private String priceItems;

    public SanPham(int imgAllItem, String nameItems, String priceItems) {
        this.imgAllItem = imgAllItem;
        this.nameItems = nameItems;
        this.priceItems = priceItems;
    }

    public int getImgAllItem() {
        return imgAllItem;
    }

    public void setImgAllItem(int imgAllItem) {
        this.imgAllItem = imgAllItem;
    }

    public String getNameItems() {
        return nameItems;
    }

    public void setNameItems(String nameItems) {
        this.nameItems = nameItems;
    }

    public String getPriceItems() {
        return priceItems;
    }

    public void setPriceItems(String priceItems) {
        this.priceItems = priceItems;
    }
}
