package com.shoppy.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class OrderDetailsModel {

    @SerializedName("order_id")
    @Expose
    private int id;
    @SerializedName("order_number")
    @Expose
    private String orderNumber;
    @SerializedName("user_name")
    @Expose
    private String userName;
    @SerializedName("order_subtotal")
    @Expose
    private double orderSubtotal;
    @SerializedName("coupan_discount")
    @Expose
    private double coupanDiscount;
    @SerializedName("user_address")
    @Expose
    private String address;
    @SerializedName("order_total")
    @Expose
    private double orderTotal;
    @SerializedName("order_status")
    @Expose
    private int orderStatus;
    @SerializedName("payment_method")
    @Expose
    private int paymentMethod;
    @SerializedName("delivered_at")
    @Expose
    private String deliveredAt;
    @SerializedName("order_date")
    @Expose
    private String orderDate;
    @SerializedName("product_name")
    @Expose
    private String productName;
    @SerializedName("price")
    @Expose
    private int price;
    @SerializedName("discount")
    @Expose
    private double discount;
    @SerializedName("quantity")
    @Expose
    private int quantity;
    @SerializedName("enum_value")
    @Expose
    private String enumValue;
    @SerializedName("order_detail_id")
    @Expose
    private int orderDetailId;
    @SerializedName("is_reviewd")
    @Expose
    private boolean isReviewd;
    @SerializedName("cancel_user")
    @Expose
    private int cancelUser;
    @SerializedName("cancel_reason")
    @Expose
    private String cancelReason;

    @SerializedName("products")
    private ArrayList<OrderDetailsProductItem> products;

    @SerializedName("delivery_charges")
    private double deliveryCharges;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public double getOrderSubtotal() {
        return orderSubtotal;
    }

    public void setOrderSubtotal(double orderSubtotal) {
        this.orderSubtotal = orderSubtotal;
    }

    public double getCoupanDiscount() {
        return coupanDiscount;
    }

    public void setCoupanDiscount(double coupanDiscount) {
        this.coupanDiscount = coupanDiscount;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getOrderTotal() {
        return orderTotal;
    }

    public void setOrderTotal(double orderTotal) {
        this.orderTotal = orderTotal;
    }

    public int getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(int orderStatus) {
        this.orderStatus = orderStatus;
    }

    public int getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(int paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getDeliveredAt() {
        return deliveredAt;
    }

    public void setDeliveredAt(String deliveredAt) {
        this.deliveredAt = deliveredAt;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getEnumValue() {
        return enumValue;
    }

    public void setEnumValue(String enumValue) {
        this.enumValue = enumValue;
    }

    public int getOrderDetailId() {
        return orderDetailId;
    }

    public void setOrderDetailId(int orderDetailId) {
        this.orderDetailId = orderDetailId;
    }

    public boolean isReviewd() {
        return isReviewd;
    }

    public void setReviewd(boolean reviewd) {
        isReviewd = reviewd;
    }

    public int getCancelUser() {
        return cancelUser;
    }

    public void setCancelUser(int cancelUser) {
        this.cancelUser = cancelUser;
    }

    public String getCancelReason() {
        return cancelReason;
    }

    public void setCancelReason(String cancelReason) {
        this.cancelReason = cancelReason;
    }

    public ArrayList<OrderDetailsProductItem> getProducts() {
        return products;
    }

    public void setProducts(ArrayList<OrderDetailsProductItem> products) {
        this.products = products;
    }

    public double getDeliveryCharges() {
        return deliveryCharges;
    }

    public void setDeliveryCharges(double deliveryCharges) {
        this.deliveryCharges = deliveryCharges;
    }
}