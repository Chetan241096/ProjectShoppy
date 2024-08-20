package com.shoppy.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProductModel implements Parcelable {

    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("mrp")
    @Expose
    private double mrp;
    @SerializedName("selling_price")
    @Expose
    private double sellingPrice;
    @SerializedName("rating")
    @Expose
    private float rating;
    @SerializedName("rating_count")
    @Expose
    private int ratingCount;
    @SerializedName("enum_value")
    @Expose
    private String enumValue;
    @SerializedName("image_thumbnail")
    @Expose
    private String imageThumbnail;

    private double discountPrice;

    @SerializedName("available_stock")
    private int availableStock;

    protected ProductModel(Parcel in) {
        id = in.readInt();
        name = in.readString();
        description = in.readString();
        mrp = in.readDouble();
        sellingPrice = in.readDouble();
        rating = in.readFloat();
        ratingCount = in.readInt();
        enumValue = in.readString();
        imageThumbnail = in.readString();
        discountPrice = in.readDouble();
        availableStock = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(description);
        dest.writeDouble(mrp);
        dest.writeDouble(sellingPrice);
        dest.writeFloat(rating);
        dest.writeInt(ratingCount);
        dest.writeString(enumValue);
        dest.writeString(imageThumbnail);
        dest.writeDouble(discountPrice);
        dest.writeInt(availableStock);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ProductModel> CREATOR = new Creator<ProductModel>() {
        @Override
        public ProductModel createFromParcel(Parcel in) {
            return new ProductModel(in);
        }

        @Override
        public ProductModel[] newArray(int size) {
            return new ProductModel[size];
        }
    };

    public double getDiscountPrice(){
        return this.mrp - this.sellingPrice;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getMrp() {
        return mrp;
    }

    public void setMrp(double mrp) {
        this.mrp = mrp;
    }

    public double getSellingPrice() {
        return sellingPrice;
    }

    public void setSellingPrice(double sellingPrice) {
        this.sellingPrice = sellingPrice;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public int getRatingCount() {
        return ratingCount;
    }

    public void setRatingCount(int ratingCount) {
        this.ratingCount = ratingCount;
    }

    public String getEnumValue() {
        return enumValue;
    }

    public void setEnumValue(String enumValue) {
        this.enumValue = enumValue;
    }

    public String getImageThumbnail() {
        return imageThumbnail;
    }

    public void setImageThumbnail(String imageThumbnail) {
        this.imageThumbnail = imageThumbnail;
    }

    public int getAvailableStock() {
        return availableStock;
    }

    public void setAvailableStock(int availableStock) {
        this.availableStock = availableStock;
    }
}