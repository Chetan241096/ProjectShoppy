package com.shoppy.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;

import com.google.android.material.snackbar.Snackbar;
import com.shoppy.R;
import com.shoppy.baseclass.BaseActivity;
import com.shoppy.databinding.ActivityChangePasswordBinding;
import com.shoppy.global.Constants;
import com.shoppy.retrofit.MethodName;

import org.json.JSONException;
import org.json.JSONObject;

public class ChangePasswordActivity extends BaseActivity implements View.OnClickListener {
    private ActivityChangePasswordBinding dataBinding;
    private int userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dataBinding = DataBindingUtil.setContentView(this, R.layout.activity_change_password);
        userID = getIntent().getIntExtra(Constants.USER_ID_KEY, 0);
        dataBinding.btnSubmit.setOnClickListener(this);
        dataBinding.etCurrentPassword.setVisibility(View.GONE);
        dataBinding.ivBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSubmit:
                if (dataBinding.etNewPassword.getText().toString().trim().length() == 0) {
                    Toast.makeText(this, getString(R.string.warn_empty_password), Toast.LENGTH_SHORT).show();
                } else if (dataBinding.etNewPassword.getText().toString().trim().length() < 8) {
                    Toast.makeText(this, getString(R.string.password_length_msg), Toast.LENGTH_SHORT).show();
                } else if (dataBinding.etConfPassword.getText().toString().trim().length() == 0) {
                    Toast.makeText(this, getString(R.string.empty_confirm_password_msg), Toast.LENGTH_SHORT).show();
                } else if (!dataBinding.etConfPassword.getText().toString().equals(dataBinding.etNewPassword.getText().toString())) {
                    Toast.makeText(this, getString(R.string.password_not_match_msg), Toast.LENGTH_SHORT).show();
                } else {
                    changePassword();
                }
                break;
            case R.id.ivBack:
                onBackPressed();
                break;
        }
    }

    private void changePassword() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("new_password", dataBinding.etNewPassword.getText().toString().trim());
            jsonObject.put("phone", getIntent().getStringExtra(Constants.MOBILE_NUMBER_KEY));
            callWb(this, Constants.CHANGE_PASSWORD_URL, Constants.GET, MethodName.CHANGE_PASSWORD, jsonObject, true, false, this, null);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResponse(String response, String methodName, int responseCode) {
        super.onResponse(response, methodName, responseCode);
        if (methodName.equals(MethodName.CHANGE_PASSWORD)) {
            try {
                JSONObject jsonObject = new JSONObject(response);
                if (jsonObject.getBoolean(Constants.SUCCESS_KEY)) {
                    Intent intent = new Intent(this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                }
                showSnackbar(jsonObject.getString(Constants.MESSAGE_KEY), dataBinding.getRoot(), Snackbar.LENGTH_SHORT);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
