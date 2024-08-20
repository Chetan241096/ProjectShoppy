package com.shoppy.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;

import com.shoppy.R;
import com.shoppy.baseclass.BaseActivity;
import com.shoppy.databinding.ActivityLoginBinding;
import com.shoppy.global.Constants;
import com.shoppy.global.Pref;
import com.shoppy.retrofit.MethodName;
import com.shoppy.retrofit.RetrofitResponse;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends BaseActivity implements RetrofitResponse, View.OnClickListener {

    private ActivityLoginBinding dataBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dataBinding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        dataBinding.btnLogin.setOnClickListener(this);
        dataBinding.tvSignUpLink.setOnClickListener(this);
        dataBinding.tvForgotText.setOnClickListener(this);
    }

    @Override
    public void onResponse(String response, String methodName, int responseCode) {
        super.onResponse(response, methodName, responseCode);
        try {
            JSONObject jsonObject = new JSONObject(response);
            if (jsonObject.getBoolean(Constants.SUCCESS_KEY)) {
                Log.d("LoginResponse>>", jsonObject.toString());
                JSONObject userInfo = jsonObject.getJSONObject("userInfo");

                Pref.prefsLoginUser(this, jsonObject.getString("Token"),
                        true,
                        userInfo.getInt("id"),
                        userInfo.getString("email"),
                        userInfo.getString("phone"),
                        "",
                        false,
                        jsonObject.getString("refreshToken"),
                        "",
                        ""
                );

                Intent intent = new Intent(this, HomeActivity.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(this, jsonObject.getString(Constants.MESSAGE_KEY), Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onResponseFail(String methodName, int responseCode) {
        super.onResponseFail(methodName, responseCode);
        Toast.makeText(this, "Error in Login. Please try again.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnLogin:
                if (validate()) {
//                    Intent intent = new Intent(this, HomeActivity.class);
//                    startActivity(intent);
//                    finish();
                    callLogin();
                }
                break;
            case R.id.tvSignUpLink:
                Intent intent = new Intent(LoginActivity.this, RegistrationActivity.class);
                startActivity(intent);
                break;
            case R.id.tvForgotText:
                Intent intent2 = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
                startActivity(intent2);
                break;
        }
    }

    private boolean validate() {
        if (dataBinding.etMobile.getText().toString().trim().length() == 0) {
            Toast.makeText(this, "PLease enter email.", Toast.LENGTH_SHORT).show();
            return false;
        } else if (dataBinding.etPassword.getText().toString().trim().length() == 0) {
            Toast.makeText(this, "Please enter password.", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }

    private void callLogin() {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(Constants.GRANT_TYPE, "password");
            jsonObject.put(Constants.CLIENT_ID, "mobileapp");
            jsonObject.put(Constants.CLIENT_SECRET, "zapplermobile");
            jsonObject.put("username", dataBinding.etMobile.getText().toString().trim());
            jsonObject.put("password", dataBinding.etPassword.getText().toString().trim());
            callWb(this, Constants.LOGIN_URL, "POST", MethodName.LOGIN, jsonObject, true, false, this, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}