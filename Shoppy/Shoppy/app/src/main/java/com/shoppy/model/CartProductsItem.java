package com.shoppy.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CartProductsItem implements Parcelable {

    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("user_id")
    @Expose
    private int userId;
    @SerializedName("product_id")
    @Expose
    private int productId;
    @SerializedName("quantity")
    @Expose
    private int quantity;
    @SerializedName("name")
    @Expose
    private String name;
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

    @SerializedName("available_stock")
    private int availableStock;

    protected CartProductsItem(Parcel in) {
        id = in.readInt();
        userId = in.readInt();
        productId = in.readInt();
        quantity = in.readInt();
        name = in.readString();
        mrp = in.readDouble();
        sellingPrice = in.readDouble();
        rating = in.readFloat();
        ratingCount = in.readInt();
        enumValue = in.readString();
        imageThumbnail = in.readString();
        availableStock = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(userId);
        dest.writeInt(productId);
        dest.writeInt(quantity);
        dest.writeString(name);
        dest.writeDouble(mrp);
        dest.writeDouble(sellingPrice);
        dest.writeFloat(rating);
        dest.writeInt(ratingCount);
        dest.writeString(enumValue);
        dest.writeString(imageThumbnail);
        dest.writeInt(availableStock);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<CartProductsItem> CREATOR = new Creator<CartProductsItem>() {
        @Override
        public CartProductsItem createFromParcel(Parcel in) {
            return new CartProductsItem(in);
        }

        @Override
        public CartProductsItem[] newArray(int size) {
            return new CartProductsItem[size];
        }
    };
    public double getDiscountPrice(){
        return mrp - sellingPrice;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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