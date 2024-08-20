package com.shoppy.global;

import android.content.Context;
import android.content.SharedPreferences;

public class Pref {

    private static String KEY_TOKEN = "TOKEN";
    private static String KEY_IS_LOGIN = "IS_LOGIN";
    private static String KEY_USER_ID = "USER_ID";
    private static String KEY_USER_NAME = "USER_NAME";
    private static String KEY_USER_EMAIL = "USER_EMAIL";
    private static String KEY_USER_CONTACT = "USER_CONTACT";
    private static String IMAGE_BASE_URL = "IMAGE_BASE_URL";
    private static String KEY_REFRESH_TOKEN = "REFRESH_TOKEN";
    private static String USER_GENDER = "USER_GENDER";

    private static SharedPreferences getPref(Context context) {
        return context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
    }

    public static void setAccessToken(Context context, String token) {
        getPref(context).edit().putString(KEY_TOKEN, token).apply();
    }

    public static String getAccessToken(Context context) {
        return getPref(context).getString(KEY_TOKEN, "");
    }
    public static void setRefreshToken(Context context, String token) {
        getPref(context).edit().putString(KEY_REFRESH_TOKEN, token).apply();
    }

    public static String getRefreshToken(Context context) {
        return getPref(context).getString(KEY_REFRESH_TOKEN, "");
    }
    public static void setIsLogin(Context context, boolean flag) {
        getPref(context).edit().putBoolean(KEY_IS_LOGIN, flag).apply();
    }

    public static boolean getIsLogin(Context context) {
        return getPref(context).getBoolean(KEY_IS_LOGIN, false);
    }

    public static void setUserId(Context context, int token) {
        getPref(context).edit().putInt(KEY_USER_ID, token).apply();
    }

    public static int getUserId(Context context) {
        return getPref(context).getInt(KEY_USER_ID, 0);
    }

    public static void setUserName(Context context, String token) {
        getPref(context).edit().putString(KEY_USER_NAME, token).apply();
    }

    public static String getUserName(Context context) {
        return getPref(context).getString(KEY_USER_NAME, "");
    }

    public static void setUserEmail(Context context, String token) {
        getPref(context).edit().putString(KEY_USER_EMAIL, token).apply();
    }

    public static String getUserEmail(Context context) {
        return getPref(context).getString(KEY_USER_EMAIL, "");
    }

    public static void setContactNo(Context context, String token) {
        getPref(context).edit().putString(KEY_USER_CONTACT, token).apply();
    }

    public static String getContactNo(Context context) {
        return getPref(context).getString(KEY_USER_CONTACT, "");
    }

    public static void setImageUrl(Context context, String url) {
        getPref(context).edit().putString(IMAGE_BASE_URL, url).apply();
    }

    public static String getImageUrl(Context context) {
        return getPref(context).getString(IMAGE_BASE_URL, "");
    }

    public static void setUserGender(Context context, String flag) {
        getPref(context).edit().putString(USER_GENDER, flag).apply();
    }

    public static String getUserGender(Context context) {
        return getPref(context).getString(USER_GENDER, "");
    }

    public static void prefsLoginUser(Context context, String accessToken, boolean isLoggedIn, int userId, String userEmail,
                                      String contactNo, String userName, Boolean isEmailVerified, String refreshToken, String userImage, String basePath) {
        setAccessToken(context, accessToken);
        setIsLogin(context, isLoggedIn);
        setUserId(context, userId);
        setUserEmail(context, userEmail);
        setContactNo(context, contactNo);
        setUserName(context, userName);
//        setIsEmailVerified(context, isEmailVerified);
        setRefreshToken(context, refreshToken);
        setImageUrl(context, basePath);
//        setUserImage(context, userImage);
    }

    public static void clearPrefs(Context context) {
//        String deviceId = getDeviceToken(context);
        getPref(context).edit().clear().apply();
//        setDeviceToken(context, deviceId);
    }
}
