package com.shoppy.model;

import com.google.gson.annotations.SerializedName;

public class CategoryModel {
    @SerializedName("category_name")
    private String categoryName;
    @SerializedName("image")
    private String cateImage;
    @SerializedName("id")
    private int id;

    public CategoryModel(String categoryName, String cateImage, int id) {
        this.categoryName = categoryName;
        this.cateImage = cateImage;
        this.id = id;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCateImage() {
        return cateImage;
    }

    public void setCateImage(String cateImage) {
        this.cateImage = cateImage;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
