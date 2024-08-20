package com.shoppy.model;


import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class OrderDetailsProductItem implements Parcelable {

    @SerializedName("discount")
    private double discount;

    @SerializedName("selling_price")
    private double sellingPrice;

    @SerializedName("quantity")
    private int quantity;

    @SerializedName("order_details_id")
    private int orderDetailsId;

    @SerializedName("image_thumbnail")
    private String imageUrl;

    @SerializedName("product_id")
    private int productId;

    @SerializedName("attribute_name")
    private String attributeName;

    @SerializedName("order_product_status")
    private int orderProductStatus;

    @SerializedName("enum_value")
    private String attributeValue;

    @SerializedName("is_returnable")
    private boolean isReturnable;

    @SerializedName("name")
    private String productName;

    @SerializedName("seller_product_id")
    private int sellerProductId;

    @SerializedName("enum_id")
    private int enumId;

    private String review;
    private float rating;

    @SerializedName("is_product_reviewed")
    private boolean isProductReviewed;

    protected OrderDetailsProductItem(Parcel in) {
        sellingPrice = in.readDouble();
        quantity = in.readInt();
        orderDetailsId = in.readInt();
        imageUrl = in.readString();
        productId = in.readInt();
        attributeName = in.readString();
        orderProductStatus = in.readInt();
        attributeValue = in.readString();
        isReturnable = in.readByte() != 0;
        productName = in.readString();
        sellerProductId = in.readInt();
        enumId = in.readInt();
        review = in.readString();
        rating = in.readFloat();
        isProductReviewed = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(sellingPrice);
        dest.writeInt(quantity);
        dest.writeInt(orderDetailsId);
        dest.writeString(imageUrl);
        dest.writeInt(productId);
        dest.writeString(attributeName);
        dest.writeInt(orderProductStatus);
        dest.writeString(attributeValue);
        dest.writeByte((byte) (isReturnable ? 1 : 0));
        dest.writeString(productName);
        dest.writeInt(sellerProductId);
        dest.writeInt(enumId);
        dest.writeString(review);
        dest.writeFloat(rating);
        dest.writeByte((byte) (isProductReviewed ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<OrderDetailsProductItem> CREATOR = new Creator<OrderDetailsProductItem>() {
        @Override
        public OrderDetailsProductItem createFromParcel(Parcel in) {
            return new OrderDetailsProductItem(in);
        }

        @Override
        public OrderDetailsProductItem[] newArray(int size) {
            return new OrderDetailsProductItem[size];
        }
    };

    public void setSellingPrice(double sellingPrice) {
        this.sellingPrice = sellingPrice;
    }

    public double getSellingPrice() {
        return sellingPrice;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setOrderDetailsId(int orderDetailsId) {
        this.orderDetailsId = orderDetailsId;
    }

    public int getOrderDetailsId() {
        return orderDetailsId;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getProductId() {
        return productId;
    }

    public void setAttributeName(String attributeName) {
        this.attributeName = attributeName;
    }

    public String getAttributeName() {
        return attributeName;
    }

    public void setOrderProductStatus(int orderProductStatus) {
        this.orderProductStatus = orderProductStatus;
    }

    public int getOrderProductStatus() {
        return orderProductStatus;
    }

    public void setAttributeValue(String attributeValue) {
        this.attributeValue = attributeValue;
    }

    public String getAttributeValue() {
        return attributeValue;
    }

    public void setIsReturnable(boolean isReturnable) {
        this.isReturnable = isReturnable;
    }

    public boolean isIsReturnable() {
        return isReturnable;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductName() {
        return productName;
    }

    public void setSellerProductId(int sellerProductId) {
        this.sellerProductId = sellerProductId;
    }

    public int getSellerProductId() {
        return sellerProductId;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public boolean isProductReviewed() {
        return isProductReviewed;
    }

    public void setProductReviewed(boolean productReviewed) {
        isProductReviewed = productReviewed;
    }

    public int getEnumId() {
        return enumId;
    }

    public void setEnumId(int enumId) {
        this.enumId = enumId;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }
}