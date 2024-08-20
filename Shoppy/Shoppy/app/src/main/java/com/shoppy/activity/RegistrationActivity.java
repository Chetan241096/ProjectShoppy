package com.shoppy.activity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.util.Patterns;
import android.view.View;


import androidx.databinding.DataBindingUtil;

import com.google.android.material.snackbar.Snackbar;
import com.shoppy.R;
import com.shoppy.baseclass.BaseActivity;
import com.shoppy.databinding.ActivityRegistrationBinding;
import com.shoppy.global.Constants;
import com.shoppy.retrofit.MethodName;

import org.json.JSONException;
import org.json.JSONObject;

public class RegistrationActivity extends BaseActivity implements View.OnClickListener {

    private ActivityRegistrationBinding dataBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dataBinding = DataBindingUtil.setContentView(this, R.layout.activity_registration);
        dataBinding.btnSubmit.setOnClickListener(this);
        dataBinding.tvLoginLink.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSubmit:
                if (validate()) {
//                    Intent intent = new Intent(this, OtpVerificationActivity.class);
////                intent.putExtra(Constants.MOBILE_NUMBER_KEY, "");
////                intent.putExtra(Constants.USER_ID_KEY, dataJson.getInt("id"));
//                    intent.putExtra(Constants.CALLED_FROM_KEY, 3);
//                    startActivity(intent);
//                    finish();
                    callRegistration();
                }
                break;
            case R.id.tvLoginLink:
                finish();
                break;
        }
    }

    private void callRegistration() {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("first_name", dataBinding.etFname.getText().toString().trim());
            jsonObject.put("last_name", dataBinding.etLname.getText().toString().trim());
            jsonObject.put("email", dataBinding.etEmail.getText().toString().trim());
            jsonObject.put("password", dataBinding.etPassword.getText().toString().trim());
            jsonObject.put("phone", dataBinding.etPhone.getText().toString().trim());

            callWb(this, Constants.REGISTRATION_URL, "POST", MethodName.REGISTRATION, jsonObject, true, false, this, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResponse(String response, String methodName, int responseCode) {
        super.onResponse(response, methodName, responseCode);
        try {
            JSONObject jsonObject = new JSONObject(response);
            if (jsonObject.getBoolean(Constants.SUCCESS_KEY)) {
//                JSONObject dataJson = jsonObject.getJSONObject("data");

                showSnackbar(jsonObject.getString(Constants.MESSAGE_KEY), dataBinding.getRoot(), Snackbar.LENGTH_SHORT);
                Intent intent = new Intent(this, OtpVerificationActivity.class);
//                intent.putExtra(Constants.MOBILE_NUMBER_KEY, "");
//                intent.putExtra(Constants.USER_ID_KEY, dataJson.getInt("id"));
                intent.putExtra(Constants.CALLED_FROM_KEY, 3);
                startActivity(intent);
                finish();
            } else {
                showSnackbar(jsonObject.getString(Constants.MESSAGE_KEY), dataBinding.getRoot(), Snackbar.LENGTH_LONG);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private boolean validate() {
        if (dataBinding.etFname.getText().toString().length() == 0) {
            showSnackbar(getString(R.string.warn_empty_first_name), dataBinding.getRoot(), Snackbar.LENGTH_SHORT);
            return false;
        } else if (dataBinding.etLname.getText().toString().length() == 0) {
            showSnackbar(getString(R.string.warn_empty_last_name), dataBinding.getRoot(), Snackbar.LENGTH_SHORT);
            return false;
        } else if (dataBinding.etEmail.getText().toString().length() == 0) {
            showSnackbar(getString(R.string.warn_empty_email), dataBinding.getRoot(), Snackbar.LENGTH_SHORT);
            return false;
        } else if (dataBinding.etPhone.getText().toString().trim().length() == 0) {
            showSnackbar(getString(R.string.warn_empty_phone), dataBinding.getRoot(), Snackbar.LENGTH_SHORT);
            return false;
        } else if ((dataBinding.etPhone.getText().toString().trim().length() != 10 || !dataBinding.etPhone.getText().toString().trim().matches("[0-9]+"))) {
            showSnackbar(getString(R.string.warn_invalid_phone_number), dataBinding.getRoot(), Snackbar.LENGTH_SHORT);
            return false;
        } else if (dataBinding.etPassword.getText().toString().trim().length() == 0) {
            showSnackbar(getString(R.string.warn_empty_password), dataBinding.getRoot(), Snackbar.LENGTH_SHORT);
            return false;
        } else if (dataBinding.etPassword.getText().toString().trim().length() < 8) {
            showSnackbar(getString(R.string.password_length_msg), dataBinding.getRoot(), Snackbar.LENGTH_SHORT);
            return false;
        } else if (!(Patterns.EMAIL_ADDRESS.matcher(dataBinding.etEmail.getText().toString().trim()).matches())) {
            showSnackbar(getString(R.string.warn_invalid_email), dataBinding.getRoot(), Snackbar.LENGTH_SHORT);
            return false;
        }  else {
            return true;
        }
    }
}
