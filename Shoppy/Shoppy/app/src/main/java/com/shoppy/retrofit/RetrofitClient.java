package com.shoppy.retrofit;



import com.shoppy.global.Constants;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.Multipart;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;

public class RetrofitClient {
    public static getService getRetroFitClient() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .authenticator(new TokenAuthenticator())
                .connectTimeout(5, TimeUnit.MINUTES)
                .readTimeout(5, TimeUnit.MINUTES)
                .writeTimeout(5, TimeUnit.MINUTES)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(new ToStringConverterFactory())
                .client(client)

                .build();
        return retrofit.create(getService.class);
    }



    public interface getService {
        @FormUrlEncoded
        @POST
        Call<String> getPaytmService(@Url String url, @FieldMap Map<String, String> params);

        @POST
        Call<String> getPostService(@HeaderMap HashMap<String, String> headers, @Url String url, @Body String parmas);

        @GET
        Call<String> getGETService(@HeaderMap HashMap<String, String> headers, @Url String url, @QueryMap HashMap<String, Object> queryParams);

        //        @HTTP(method = "DELETE", hasBody = true)
        @DELETE
        Call<String> getDeleteService(@HeaderMap HashMap<String, String> headers, @Url String url, @QueryMap HashMap<String, Object> queryParams);

        @PUT
        Call<String> getPutService(@HeaderMap HashMap<String, String> headers, @Url String url, @Body String parmas);

        @PATCH
        Call<String> getPatchService(@HeaderMap HashMap<String, String> headers, @Url String url, @Body String parmas);


        @Multipart
        @PUT
        Call<String> requestWithFile(@HeaderMap HashMap<String, String> headers, @Url String url, @PartMap Map<String, String> parts, @Part MultipartBody.Part file);

        @Multipart
        @POST
        Call<String> requestWithFilePost(@HeaderMap HashMap<String, String> headers, @Url String url, @PartMap Map<String, String> parts, @Part MultipartBody.Part file);
    }
}
