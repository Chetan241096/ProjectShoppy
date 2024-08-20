package com.shoppy.model;

public class DrawerItem {
//    private int imgResID;
    private String title;
    private boolean isSelected;

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public DrawerItem(String title, boolean isSelected) {
//        this.imgResID = imgResID;
        this.title = title;
        this.isSelected = isSelected;
    }

//    public int getImgResID() {
//        return imgResID;
//    }
//
//    public void setImgResID(int imgResID) {
//        this.imgResID = imgResID;
//    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}