package com.shoppy.retrofit;

public interface RetrofitResponse {
    void onResponse(String response, String methodName, int responseCode);
    void onResponseFail(String methodName, int responseCode);
}
