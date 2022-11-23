package com.example.duan1.model;

public class CategoryProduct {
    private int pic;
    private String name;

    public int getPic() {
        return pic;
    }

    public void setPic(int pic) {
        this.pic = pic;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CategoryProduct(int pic, String name) {
        this.pic = pic;
        this.name = name;
    }
}
