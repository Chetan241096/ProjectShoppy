package com.shoppy.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SearchProductModel {

    public SearchProductModel(String categoryName, int categoryId, int id, String productName) {
        this.categoryName = categoryName;
        this.categoryId = categoryId;
        this.id = id;
        this.productName = productName;
    }

    @SerializedName("category_name")
    private String categoryName;

    @SerializedName("category_id")
    private int categoryId;

    @SerializedName("id")
    private int id;

    @SerializedName("name")
    private String productName;

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductName() {
        return productName;
    }
}