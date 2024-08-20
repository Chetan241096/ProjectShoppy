package com.shoppy.fragment;

import android.content.Context;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.google.android.material.snackbar.Snackbar;
import com.shoppy.R;
import com.shoppy.activity.HomeActivity;
import com.shoppy.baseclass.BaseFragment;
import com.shoppy.databinding.ActivityChangePasswordBinding;
import com.shoppy.global.Constants;
import com.shoppy.global.Pref;
import com.shoppy.retrofit.MethodName;

import org.json.JSONException;
import org.json.JSONObject;

public class ChangePasswordFragment extends BaseFragment implements View.OnClickListener{
    private HomeActivity activity;
    private ActivityChangePasswordBinding dataBinding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        dataBinding = DataBindingUtil.inflate(inflater, R.layout.activity_change_password, container, false);
        return dataBinding.getRoot();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (HomeActivity) context;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        dataBinding.etCurrentPassword.setVisibility(View.VISIBLE);
        dataBinding.ivBack.setOnClickListener(this);
        dataBinding.btnSubmit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (activity.isOffline(activity)) {
            activity.showSnackbar(getString(R.string.warn_no_internet), dataBinding.getRoot(), Snackbar.LENGTH_LONG);
            return;
        }
        switch (v.getId()) {
            case R.id.btnSubmit:
                if (dataBinding.etCurrentPassword.getText().toString().trim().length() == 0) {
                    activity.showSnackbar(getString(R.string.empty_current_password), dataBinding.getRoot(), Snackbar.LENGTH_SHORT);

                } else if (dataBinding.etNewPassword.getText().toString().trim().length() == 0) {
                    activity.showSnackbar(getString(R.string.empty_password_msg), dataBinding.getRoot(), Snackbar.LENGTH_SHORT);

                } else if (dataBinding.etNewPassword.getText().toString().trim().length() < 8) {
                    activity.showSnackbar(getString(R.string.password_length_msg), dataBinding.getRoot(), Snackbar.LENGTH_SHORT);

                } else if (dataBinding.etConfPassword.getText().toString().trim().length() == 0) {
                    activity.showSnackbar(getString(R.string.empty_confirm_password_msg), dataBinding.getRoot(), Snackbar.LENGTH_SHORT);

                } else if (!dataBinding.etConfPassword.getText().toString().equals(dataBinding.etNewPassword.getText().toString())) {
                    activity.showSnackbar(getString(R.string.password_not_match_msg), dataBinding.getRoot(), Snackbar.LENGTH_SHORT);

                } else {
                    changePassword();
                }
                break;
            case R.id.ivBack:
                activity.onBackPressed();
                break;
        }
    }

    private void changePassword() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("phone", Pref.getContactNo(activity));
            jsonObject.put("new_password", dataBinding.etNewPassword.getText().toString().trim());
            activity.callWb(activity, Constants.CHANGE_PASSWORD_URL, Constants.GET, MethodName.CHANGE_PASSWORD, jsonObject, true, false, this, null);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResponse(String response, String methodName, int responseCode) {
        super.onResponse(response, methodName, responseCode);
        try {
            JSONObject jsonObject = new JSONObject(response);
            activity.showSnackbar(jsonObject.getString(Constants.MESSAGE_KEY), dataBinding.getRoot(), Snackbar.LENGTH_SHORT);
            if (jsonObject.getBoolean(Constants.SUCCESS_KEY)) {
                activity.onBackPressed();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
