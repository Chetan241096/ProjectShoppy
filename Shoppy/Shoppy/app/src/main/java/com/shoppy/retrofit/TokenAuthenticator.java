package com.shoppy.retrofit;



import com.shoppy.baseclass.ApplicationClass;
import com.shoppy.global.Constants;
import com.shoppy.global.Pref;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.Authenticator;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;
import retrofit2.Call;


public class TokenAuthenticator implements Authenticator {

    @Override
    public Request authenticate(Route route, final Response response) throws IOException {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("refresh_token", Pref.getRefreshToken(ApplicationClass.getContext()));
            jsonObject.put(Constants.GRANT_TYPE, "refresh_token");
            jsonObject.put(Constants.CLIENT_ID, "mobileapp");
            jsonObject.put(Constants.CLIENT_SECRET, "zapplermobile");


            HashMap<String, String> headers = new HashMap<>();
            headers.put("Content-Type", "application/json");

            Call<String> callToken = RetrofitClient.getRetroFitClient().getPostService(headers, Constants.LOGIN_URL, jsonObject.toString());
            String responseToken = callToken.execute().body();
            try {
                JSONObject jsonObject1 = new JSONObject(responseToken);
                Pref.setRefreshToken(ApplicationClass.getContext(), jsonObject1.getString("refreshToken"));
                Pref.setAccessToken(ApplicationClass.getContext(), jsonObject1.getString("Token"));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return response.request().newBuilder()
                    .method(response.request().method(), response.request().body())
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer " + Pref.getAccessToken(ApplicationClass.getContext()))
                    .build();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}