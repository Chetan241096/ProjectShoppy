package com.shoppy.baseclass;

import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.inputmethod.InputMethodManager;


import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shoppy.activity.HomeActivity;
import com.shoppy.activity.LoginActivity;
import com.shoppy.dialog.CustomLoaderDialog;
import com.shoppy.global.Constants;
import com.shoppy.global.Pref;
import com.shoppy.retrofit.MethodName;
import com.shoppy.retrofit.RetrofitClient;
import com.shoppy.retrofit.RetrofitResponse;

import org.json.JSONObject;

import java.io.File;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BaseActivity extends AppCompatActivity implements RetrofitResponse {
    private CustomLoaderDialog loaderDialog;

    public void callWb(final Context context, String url, String methofType, final String methodName, JSONObject params, boolean loaderShow, boolean isCancelable, final RetrofitResponse retrofitResponse, File file) {

        Log.d("Reqrl>>", Constants.BASE_URL + url);
        Log.d("Req>>", methodName + ">> " + params.toString());
        Call<String> call = null;
        HashMap<String, String> headers = new HashMap<>();

        if (!Pref.getAccessToken(context).equals("")) {
            headers.put("Authorization", "Bearer " + Pref.getAccessToken(context));
        }
        if (loaderShow) {
            if (loaderDialog == null)
                loaderDialog = new CustomLoaderDialog(this);
            loaderDialog.show(isCancelable);
        }

        switch (methofType) {
            case Constants.POST:
                headers.put("Content-Type", "application/json");
                call = RetrofitClient.getRetroFitClient().getPostService(headers,  url, params.toString());
                break;
            case Constants.GET: {
                headers.put("Content-Type", "application/json");
                HashMap<String, Object> paramsHashMap = new Gson().fromJson(params.toString(), new TypeToken<HashMap<String, String>>() {
                }.getType());
                call = RetrofitClient.getRetroFitClient().getGETService(headers, url, paramsHashMap);
                break;
            }
            case Constants.DELETE: {
                headers.put("Content-Type", "application/json");
                HashMap<String, Object> paramsHashMap = new Gson().fromJson(params.toString(), new TypeToken<HashMap<String, String>>() {
                }.getType());
                call = RetrofitClient.getRetroFitClient().getDeleteService(headers, url, paramsHashMap);
                break;
            }
            case Constants.PUT:
                headers.put("Content-Type", "application/json");
                call = RetrofitClient.getRetroFitClient().getPutService(headers, url, params.toString());
                break;
            case Constants.MULTIPART:
                HashMap<String, String> paramsHashMap = new Gson().fromJson(params.toString(), new TypeToken<HashMap<String, String>>() {
                }.getType());

                MultipartBody.Part body = null;
                if (file != null) {
                    RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), file);
                    body = MultipartBody.Part.createFormData("file", file.getName(), reqFile);
                }

                if (methodName.equals(MethodName.POST_SEND_FEEDBACK)) {
                    call = RetrofitClient.getRetroFitClient().requestWithFilePost(headers, url, paramsHashMap, body);
                } else {
                    call = RetrofitClient.getRetroFitClient().requestWithFile(headers, url, paramsHashMap, body);
                }
                break;
        }

        if (call != null) {
            call.enqueue(new Callback<String>() {

                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    try {
                        if (loaderDialog != null && loaderDialog.isShowing()) {
                            loaderDialog.hide();
                        }

                        if (response.code() == Constants.BLOCK_USER_CODE) {
                            Pref.clearPrefs(context);
                            Intent intent1 = new Intent(context, LoginActivity.class);
                            intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent1);
                            finish();
                            return;
                        }
                        if (response.body() != null) {
                            Log.d("Res>>", response.body());
                            JSONObject jsonObject = new JSONObject(response.body());
                            retrofitResponse.onResponse(response.body(), jsonObject.getString("method"), response.code());
//                            retrofitResponse.onResponse(response.body(), methodName, response.code());

                        } else if (response.errorBody() != null) {
                            String errorBody = response.errorBody().string();
                            JSONObject jsonObject = new JSONObject(errorBody);
                            retrofitResponse.onResponse(errorBody, jsonObject.getString("method"), response.code());
//                            retrofitResponse.onResponse(errorBody, methodName, response.code());
                        } else {
                            retrofitResponse.onResponseFail(methodName, response.code());
                        }
                    } catch (Exception e) {

                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    Log.d("Res>>", "error");
                    try {
                        if (loaderDialog != null && loaderDialog.isShowing()) {
                            loaderDialog.hide();
                        }
                        retrofitResponse.onResponseFail(methodName, 401);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    @Override
    public void onResponse(String response, String methodName, int responseCode) {

    }

    @Override
    public void onResponseFail(String methodName, int responseCode) {

    }


    public Fragment getCurrentFragment(int contsinerId) {
        return getSupportFragmentManager().findFragmentById(contsinerId);
    }

    public void hideKeyboard(View view) {
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public void setFragment(Fragment fragment, int container, boolean isAdd, boolean isBackStack, int anim[]) {
        try {
            hideKeyboard(findViewById(container));
            HomeActivity.BACKPRESS_COUNT = 0;
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();

            if (anim != null) {
                ft.setCustomAnimations(anim[0], anim[1]);
            }

            if (isAdd) {
                ft.add(container, fragment, fragment.getClass().getSimpleName());
            } else {
                ft.replace(container, fragment, fragment.getClass().getSimpleName());
            }


            if (isBackStack) {
                ft.addToBackStack(fragment.getClass().getName());
            }


            Fragment currentFragment = getCurrentFragment(container);
            if (currentFragment != null) {
                ft.hide(currentFragment);
            }


            ft.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void setChildFragment(FragmentManager childFragmentManager, Fragment fragment, int container, boolean isAdd, boolean isBackStack) {
        FragmentTransaction ft = childFragmentManager.beginTransaction();
        if (isAdd) {
            ft.add(container, fragment, fragment.getClass().getSimpleName());
        } else {
            ft.replace(container, fragment, fragment.getClass().getSimpleName());
        }

        if (isBackStack) {
            ft.addToBackStack(null);
        }

        if (isAdd) {
            Fragment currentFragment = getCurrentFrgChild(childFragmentManager, container);
            if (currentFragment != null) {
                ft.hide(currentFragment);
            }
        }
        ft.commit();
    }

    public Fragment getCurrentFrgChild(FragmentManager childFragmentManager, int containerID) {
        return childFragmentManager.findFragmentById(containerID);
    }


    public boolean isOffline(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity == null) {
            return true;
        } else {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) {
                for (NetworkInfo anInfo : info) {
                    if (anInfo.isConnected()) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public int getDisplayWidth() {
        final Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size.x;
    }

    public int getDisplayHeight() {
        final Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size.y;
    }

    public void popAllFragment() {
        for (int i = 0; i < getSupportFragmentManager().getBackStackEntryCount(); ++i) {
            getSupportFragmentManager().popBackStack();
        }
    }
    public void openKeyboard(View view) {
        try {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(view, InputMethodManager.SHOW_FORCED);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public String getOrderStatus(int statusId) {
        switch (statusId) {
            case 1:
                return Constants.ORDER_PENDING;
            case 2:
                return Constants.ORDER_CONFIRMED;
            case 3:
                return Constants.ORDER_COMPLETED;
            case 4:
                return Constants.ORDER_CANCELED;
        }
        return "";
    }
    public Date getDateFormat(String date) {
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
            return simpleDateFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
    public void showSnackbar(String string, View view, int timeLength) {
        Snackbar.make(view, string, timeLength).show();
    }
    public String getStringDate(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        return simpleDateFormat.format(date);
    }
    public String getFormated(double value) {
        return new DecimalFormat("##.##").format(value);
    }
}