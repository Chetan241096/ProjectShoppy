package com.shoppy.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class GeneralReviewModel implements Parcelable {

    public GeneralReviewModel(String review, float rating, int id, int helpfulCount, int sellerProductId, boolean isRatingReview, String fullName, String reviewDate) {
        this.review = review;
        this.rating = rating;
        this.id = id;
        this.helpfulCount = helpfulCount;
        this.sellerProductId = sellerProductId;
        this.isRatingReview = isRatingReview;
        this.fullName = fullName;
        this.reviewDate = reviewDate;
    }

    @SerializedName("review")
    private String review;

    @SerializedName("rating")
    private float rating;

    @SerializedName("id")
    private int id;

    @SerializedName("helpful_count")
    private int helpfulCount;

    @SerializedName("seller_product_id")
    private int sellerProductId;

    @SerializedName("is_rating_review")
    private boolean isRatingReview;
    @SerializedName("user_name")
    private String fullName;
    @SerializedName("review_date")
    private String reviewDate;

    @SerializedName("name")
    private String productName;

    protected GeneralReviewModel(Parcel in) {
        review = in.readString();
        rating = in.readFloat();
        id = in.readInt();
        helpfulCount = in.readInt();
        sellerProductId = in.readInt();
        isRatingReview = in.readByte() != 0;
        fullName = in.readString();
        reviewDate = in.readString();
    }

    public static final Creator<GeneralReviewModel> CREATOR = new Creator<GeneralReviewModel>() {
        @Override
        public GeneralReviewModel createFromParcel(Parcel in) {
            return new GeneralReviewModel(in);
        }

        @Override
        public GeneralReviewModel[] newArray(int size) {
            return new GeneralReviewModel[size];
        }
    };

    public void setReview(String review) {
        this.review = review;
    }

    public String getReview() {
        return review;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public float getRating() {
        return rating;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setHelpfulCount(int helpfulCount) {
        this.helpfulCount = helpfulCount;
    }

    public int getHelpfulCount() {
        return helpfulCount;
    }

    public void setSellerProductId(int sellerProductId) {
        this.sellerProductId = sellerProductId;
    }

    public int getSellerProductId() {
        return sellerProductId;
    }

    public void setIsRatingReview(boolean isRatingReview) {
        this.isRatingReview = isRatingReview;
    }

    public boolean isIsRatingReview() {
        return isRatingReview;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getReviewDate() {
        return reviewDate;
    }

    public void setReviewDate(String reviewDate) {
        this.reviewDate = reviewDate;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(review);
        parcel.writeFloat(rating);
        parcel.writeInt(id);
        parcel.writeInt(helpfulCount);
        parcel.writeInt(sellerProductId);
        parcel.writeByte((byte) (isRatingReview ? 1 : 0));
        parcel.writeString(fullName);
        parcel.writeString(reviewDate);
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }
}