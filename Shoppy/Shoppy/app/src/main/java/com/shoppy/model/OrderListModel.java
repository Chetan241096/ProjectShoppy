package com.shoppy.model;

import com.google.gson.annotations.SerializedName;

public class OrderListModel {

    @SerializedName("order_delivery_type")
    private int orderDeliveryType;

    @SerializedName("seller_business_name")
    private String sellerBusinessName;

    @SerializedName("order_total_weight")
    private float orderTotalWeight;

    @SerializedName("order_number")
    private String orderNumber;

    @SerializedName("user_full_name")
    private String userFullName;

    @SerializedName("total_products")
    private int noOfProducts;

    @SerializedName("order_status")
    private int orderStatusId;

    @SerializedName("order_total")
    private float orderTotal;

    @SerializedName("id")
    private int id;
    @SerializedName("order_date")
    private String orderDate;

    public void setOrderDeliveryType(int orderDeliveryType) {
        this.orderDeliveryType = orderDeliveryType;
    }

    public int getOrderDeliveryType() {
        return orderDeliveryType;
    }

    public void setSellerBusinessName(String sellerBusinessName) {
        this.sellerBusinessName = sellerBusinessName;
    }

    public String getSellerBusinessName() {
        return sellerBusinessName;
    }

    public void setOrderTotalWeight(float orderTotalWeight) {
        this.orderTotalWeight = orderTotalWeight;
    }

    public float getOrderTotalWeight() {
        return orderTotalWeight;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setUserFullName(String userFullName) {
        this.userFullName = userFullName;
    }

    public String getUserFullName() {
        return userFullName;
    }

    public void setNoOfProducts(int noOfProducts) {
        this.noOfProducts = noOfProducts;
    }

    public int getNoOfProducts() {
        return noOfProducts;
    }

    public void setOrderStatusId(int orderStatusId) {
        this.orderStatusId = orderStatusId;
    }

    public int getOrderStatusId() {
        return orderStatusId;
    }

    public void setOrderTotal(float orderTotal) {
        this.orderTotal = orderTotal;
    }

    public float getOrderTotal() {
        return orderTotal;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }
}