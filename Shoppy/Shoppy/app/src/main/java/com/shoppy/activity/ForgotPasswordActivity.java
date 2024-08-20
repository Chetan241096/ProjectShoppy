package com.shoppy.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;


import androidx.databinding.DataBindingUtil;

import com.google.android.material.snackbar.Snackbar;
import com.shoppy.R;
import com.shoppy.baseclass.BaseActivity;
import com.shoppy.databinding.ActivityForgotPasswordBinding;
import com.shoppy.global.Constants;
import com.shoppy.retrofit.MethodName;

import org.json.JSONException;
import org.json.JSONObject;

public class ForgotPasswordActivity extends BaseActivity implements View.OnClickListener {

    private ActivityForgotPasswordBinding dataBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dataBinding = DataBindingUtil.setContentView(this, R.layout.activity_forgot_password);
        dataBinding.btnSubmit.setOnClickListener(this);
        dataBinding.tvRememberedLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSubmit:
                callForgotApi();
                break;
            case R.id.tvRememberedLogin:
                finish();
                break;
        }
    }

    private void callForgotApi() {
        if (dataBinding.etMobile.getText().toString().trim().length() == 0) {
            Toast.makeText(this, getString(R.string.warn_empty_phone), Toast.LENGTH_SHORT).show();

        } else if (dataBinding.etMobile.getText().toString().trim().length() != 10 || !dataBinding.etMobile.getText().toString().trim().matches("[0-9]+")) {
            Toast.makeText(this, getString(R.string.warn_invalid_phone_number), Toast.LENGTH_SHORT).show();
        } else {
            try {
//                Intent intent = new Intent(this, OtpVerificationActivity.class);
//                intent.putExtra(Constants.MOBILE_NUMBER_KEY, dataBinding.etMobile.getText().toString().trim());
//                intent.putExtra(Constants.USER_ID_KEY, 0);
//                intent.putExtra(Constants.CALLED_FROM_KEY, 1);
//                startActivity(intent);
//                finish();

                JSONObject jsonObject = new JSONObject();
                jsonObject.put("phone", dataBinding.etMobile.getText().toString());
                callWb(this, Constants.GET_USER_BY_PHONE, Constants.GET, MethodName.GET_USER_BY_PHONE, jsonObject, false, false, this, null);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onResponse(String response, String methodName, int responseCode) {
        super.onResponse(response, methodName, responseCode);
        try {
            JSONObject jsonObject = new JSONObject(response);
            if (jsonObject.getBoolean(Constants.SUCCESS_KEY)) {
                Intent intent = new Intent(this, OtpVerificationActivity.class);
                intent.putExtra(Constants.MOBILE_NUMBER_KEY, dataBinding.etMobile.getText().toString().trim());
                intent.putExtra(Constants.USER_ID_KEY, 0);
                intent.putExtra(Constants.CALLED_FROM_KEY, 1);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(this, jsonObject.getString(Constants.MESSAGE_KEY), Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d("Res", response);
    }

    @Override
    public void onResponseFail(String methodName, int responseCode) {
        super.onResponseFail(methodName, responseCode);
    }
}