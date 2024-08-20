package com.shoppy.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AddressModel implements Parcelable {

    @SerializedName("id")
    private int id;
    @SerializedName("user_name")
    @Expose
    private String userName = "";
    @SerializedName("house_number")
    @Expose
    private String houseNumber;
    @SerializedName("society_name")
    @Expose
    private String societyName;
    @SerializedName("landmark")
    @Expose
    private String landmark;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("is_default")
    @Expose
    private Integer isDefault;
    @SerializedName("city")
    @Expose
    private String city;
    @SerializedName("state")
    @Expose
    private String state;

    protected AddressModel(Parcel in) {
        userName = in.readString();
        houseNumber = in.readString();
        societyName = in.readString();
        landmark = in.readString();
        address = in.readString();
        if (in.readByte() == 0) {
            isDefault = null;
        } else {
            isDefault = in.readInt();
        }
        city = in.readString();
        state = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(userName);
        dest.writeString(houseNumber);
        dest.writeString(societyName);
        dest.writeString(landmark);
        dest.writeString(address);
        if (isDefault == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(isDefault);
        }
        dest.writeString(city);
        dest.writeString(state);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<AddressModel> CREATOR = new Creator<AddressModel>() {
        @Override
        public AddressModel createFromParcel(Parcel in) {
            return new AddressModel(in);
        }

        @Override
        public AddressModel[] newArray(int size) {
            return new AddressModel[size];
        }
    };

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getHouseNumber() {
        return houseNumber;
    }

    public void setHouseNumber(String houseNumber) {
        this.houseNumber = houseNumber;
    }

    public String getSocietyName() {
        return societyName;
    }

    public void setSocietyName(String societyName) {
        this.societyName = societyName;
    }

    public String getLandmark() {
        return landmark;
    }

    public void setLandmark(String landmark) {
        this.landmark = landmark;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(Integer isDefault) {
        this.isDefault = isDefault;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}