package com.shoppy.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class HomeModel {

    @SerializedName("banner_image_path")
    @Expose
    private String bannerImagePath;
    @SerializedName("categories")
    @Expose
    private ArrayList<CategoryModel> categories = null;
    @SerializedName("highest_selling")
    @Expose
    private ArrayList<ProductModel> highestSelling = null;
    @SerializedName("highest_rating")
    @Expose
    private ArrayList<ProductModel> highestRating = null;

    public String getBannerImagePath() {
        return bannerImagePath;
    }

    public void setBannerImagePath(String bannerImagePath) {
        this.bannerImagePath = bannerImagePath;
    }

    public ArrayList<CategoryModel> getCategories() {
        return categories;
    }

    public void setCategories(ArrayList<CategoryModel> categories) {
        this.categories = categories;
    }

    public ArrayList<ProductModel> getHighestSelling() {
        return highestSelling;
    }

    public void setHighestSelling(ArrayList<ProductModel> highestSelling) {
        this.highestSelling = highestSelling;
    }

    public ArrayList<ProductModel> getHighestRating() {
        return highestRating;
    }

    public void setHighestRating(ArrayList<ProductModel> highestRating) {
        this.highestRating = highestRating;
    }
}