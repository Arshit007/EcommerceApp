package com.example.arshit.ecommerceapp.Model;

public class Category {
    private String CategoryName;
    private int CategoryImage;


    public Category() {
    }

    public Category(String categoryName, int categoryImage) {
        CategoryName = categoryName;
        CategoryImage = categoryImage;
    }

    public String getCategoryName() {
        return CategoryName;
    }

    public void setCategoryName(String categoryName) {
        CategoryName = categoryName;
    }

    public int getCategoryImage() {
        return CategoryImage;
    }

    public void setCategoryImage(int categoryImage) {
        CategoryImage = categoryImage;
    }
}
