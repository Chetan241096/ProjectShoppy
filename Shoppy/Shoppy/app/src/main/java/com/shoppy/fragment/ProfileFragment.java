package com.shoppy.fragment;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.shoppy.R;
import com.shoppy.activity.HomeActivity;
import com.shoppy.activity.LoginActivity;
import com.shoppy.baseclass.BaseFragment;
import com.shoppy.databinding.FragmentProfileBinding;
import com.shoppy.global.Constants;
import com.shoppy.global.Pref;
import com.shoppy.model.UserModel;
import com.shoppy.retrofit.MethodName;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

public class ProfileFragment extends BaseFragment implements View.OnClickListener {
    private static final String ARG_COLUMN_COUNT = "ARGS";
    private FragmentProfileBinding dataBinding;
    private HomeActivity activity;
    private UserModel userModel;
    private int GALLERY_CODE = 12;


    public static ProfileFragment getInstance(String args1) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_COLUMN_COUNT, args1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (HomeActivity) context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false);

        return dataBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        dataBinding.tvEdit.setOnClickListener(this);
        dataBinding.tvCancel.setOnClickListener(this);
        dataBinding.btnUpdate.setOnClickListener(this);
        dataBinding.etDob.setOnClickListener(this);
        dataBinding.ivMenu.setOnClickListener(this);
        dataBinding.tvReview.setOnClickListener(this);
        dataBinding.tvChangePassword.setOnClickListener(this);
        dataBinding.tvChangePassword.setOnClickListener(this);
        dataBinding.ivUserImage.setOnClickListener(this);
        dataBinding.tvLogout.setOnClickListener(this);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(activity, R.layout.item_single_list, R.id.tvTextSingle, new String[]{"Select Gender", "Male", "Female", "Others"});
        dataBinding.spinnerGender.setAdapter(adapter);
        callUserDetails();

//        RequestOptions requestOptions = new RequestOptions();
//        requestOptions.placeholder(R.drawable.man);
//        requestOptions.error(R.drawable.man);
    }

    private void callUserDetails() {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("phone", Pref.getContactNo(activity));
            activity.callWb(activity, Constants.GET_USER_BY_PHONE, Constants.GET, MethodName.GET_USER_BY_PHONE, jsonObject, false, false, this, null);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResponse(String response, String methodName, int responseCode) {
        super.onResponse(response, methodName, responseCode);
        try {
            JSONObject jsonObject = new JSONObject(response);
            if (jsonObject.getBoolean(Constants.SUCCESS_KEY)) {
                if (methodName.equals(MethodName.GET_USER_BY_PHONE)) {
                    updateUI(jsonObject.getJSONObject("data"));
                } else if (methodName.equals(MethodName.USER_UPDATE)) {
                    setUIforEdit(false);
                    JSONObject jsonObject1 = jsonObject.getJSONObject("data");

                    Pref.setContactNo(activity, jsonObject1.getString("phone"));
                    Pref.setUserEmail(activity, jsonObject1.getString("email"));
                    Pref.setUserName(activity, jsonObject1.getString("full_name"));
                    Pref.setUserGender(activity, jsonObject1.getString("gender"));
                    updateUI(jsonObject1);
                    activity.showSnackbar(jsonObject.getString(Constants.MESSAGE_KEY), dataBinding.getRoot(), Snackbar.LENGTH_SHORT);
                } else if (methodName.equals(MethodName.LOGOUT)) {
                    Pref.clearPrefs(activity);
                    startActivity(new Intent(activity, LoginActivity.class));
                    activity.finish();
                }
            } else {
                activity.showSnackbar(jsonObject.getString(Constants.MESSAGE_KEY), dataBinding.getRoot(), Snackbar.LENGTH_SHORT);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void updateUI(JSONObject data) {
        dataBinding.mainScrollView.setVisibility(View.VISIBLE);
        userModel = new Gson().fromJson(data.toString(), UserModel.class);
        dataBinding.tvUserName.setText(userModel.getFirstName() + " " + userModel.getLastName());

        if (userModel.getGender() == null || userModel.getGender().equals("")) {
            dataBinding.spinnerGender.setSelection(0);
        } else if (userModel.getGender().equalsIgnoreCase("Male")) {
            dataBinding.spinnerGender.setSelection(1);
        } else if (userModel.getGender().equalsIgnoreCase("Female")) {
            dataBinding.spinnerGender.setSelection(2);
        } else if (userModel.getGender().equalsIgnoreCase("Others")) {
            dataBinding.spinnerGender.setSelection(3);
        }

        dataBinding.etMobileNumber.setText(userModel.getPhone());
        dataBinding.etEmail.setText(userModel.getEmail());
        dataBinding.etFname.setText(userModel.getFirstName());
        dataBinding.etLname.setText(userModel.getLastName());
        dataBinding.etDob.setText(userModel.getBirthDate());

        if (userModel.getImage_url() != null && !userModel.getImage_url().equals("")) {
            dataBinding.progress.setVisibility(View.VISIBLE);
            Glide.with(activity)
                    .load(Pref.getImageUrl(activity) + userModel.getImage_url())
                    .apply(new RequestOptions().circleCrop().error(R.drawable.man))
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            dataBinding.progress.setVisibility(View.GONE);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            dataBinding.progress.setVisibility(View.GONE);
                            return false;

                        }
                    })
                    .into(dataBinding.ivUserImage);
        } else {

            Glide.with(activity)
                    .load(userModel != null && userModel.getGender() != null && userModel.getGender().equalsIgnoreCase("Female") ? R.drawable.female_avatar : R.drawable.man)
                    .into(dataBinding.ivUserImage);
        }
        setUIforEdit(false);

    }

    @Override
    public void onClick(View v) {
        if (activity.isOffline(activity)) {
            activity.showSnackbar(getString(R.string.warn_no_internet), dataBinding.getRoot(), Snackbar.LENGTH_SHORT);
            return;
        }
        switch (v.getId()) {
            case R.id.tvEdit:
                setUIforEdit(true);
                break;
            case R.id.tvCancel:
                setUIforEdit(false);
                break;
            case R.id.ivMenu:
                activity.openCloseDrawer();
                break;
            case R.id.btnUpdate:
                if (dataBinding.etEmail.getText().toString().length() == 0) {
                    activity.showSnackbar(getString(R.string.warn_empty_email), dataBinding.getRoot(), Snackbar.LENGTH_SHORT);

                } else if (dataBinding.etFname.getText().toString().length() == 0) {
                    activity.showSnackbar(getString(R.string.warn_empty_first_name), dataBinding.getRoot(), Snackbar.LENGTH_SHORT);

                } else if (dataBinding.etLname.getText().toString().length() == 0) {
                    activity.showSnackbar(getString(R.string.warn_empty_last_name), dataBinding.getRoot(), Snackbar.LENGTH_SHORT);

                } else if (!(Patterns.EMAIL_ADDRESS.matcher(dataBinding.etEmail.getText().toString().trim()).matches())) {
                    activity.showSnackbar(getString(R.string.warn_invalid_email), dataBinding.getRoot(), Snackbar.LENGTH_SHORT);
                } else if (dataBinding.spinnerGender.getSelectedItemPosition() == 0) {
                    activity.showSnackbar(getString(R.string.warn_select_gender), dataBinding.getRoot(), Snackbar.LENGTH_SHORT);
                } else {
                    updateProfile();
                }
                break;
            case R.id.etDob:
                final Calendar calendar = Calendar.getInstance();
                if (!dataBinding.etDob.getText().toString().trim().equals("")) {
                    calendar.setTime(activity.getDateFormat(dataBinding.etDob.getText().toString().trim()));
                }

                DatePickerDialog datePickerDialog = new DatePickerDialog(activity, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        calendar.set(year, month, dayOfMonth);
                        dataBinding.etDob.setText(activity.getStringDate(calendar.getTime()));
                    }
                }, calendar
                        .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.getDatePicker().setMinDate(-696988800000L);
                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                datePickerDialog.show();
                break;


            case R.id.tvChangePassword:
                if (dataBinding.btnUpdate.getVisibility() == View.GONE)
                    activity.setFragment(new ChangePasswordFragment(), R.id.framLay, true, true, null);
                break;
            case R.id.tvReview:
                if (dataBinding.btnUpdate.getVisibility() == View.GONE)
                    activity.setFragment(new MyReviewFragment(), R.id.framLay, true, true, null);
                break;
            case R.id.tvLogout:
                if (dataBinding.btnUpdate.getVisibility() == View.GONE) {
                    new AlertDialog.Builder(activity).setMessage(getString(R.string.msg_logout))
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    callLogout();
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            }).create().show();
                }
                break;
        }
    }

    private void callLogout() {
        Pref.clearPrefs(activity);
        startActivity(new Intent(activity, LoginActivity.class));
        activity.finish();

//        activity.callWb(activity, Constants.LOGOUT_URL, Constants.GET, MethodName.LOGOUT, new JSONObject(), true, false, this, null);
    }

    private void updateProfile() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("first_name", dataBinding.etFname.getText().toString().trim());
            jsonObject.put("last_name", dataBinding.etLname.getText().toString().trim());
            jsonObject.put("email", dataBinding.etEmail.getText().toString().trim());
            jsonObject.put("birth_date", dataBinding.etDob.getText().toString().trim());
            jsonObject.put("gender", dataBinding.spinnerGender.getSelectedItem());
            activity.callWb(activity, Constants.UPDATE_USER_URL, Constants.PUT, MethodName.USER_UPDATE, jsonObject, true, false, this, null);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setUIforEdit(boolean UIforEdit) {
        if (UIforEdit) {
            dataBinding.tvCancel.setVisibility(View.VISIBLE);
            dataBinding.tvEdit.setVisibility(View.GONE);
            dataBinding.btnUpdate.setVisibility(View.VISIBLE);
            dataBinding.etFname.setText(userModel.getFirstName());
            dataBinding.etLname.setText(userModel.getLastName());
            dataBinding.etFname.setEnabled(true);
            dataBinding.etEmail.setEnabled(true);
            dataBinding.etLname.setEnabled(true);
            dataBinding.etFname.setVisibility(View.VISIBLE);
            dataBinding.etLname.setVisibility(View.VISIBLE);
            dataBinding.tvUserName.setVisibility(View.GONE);
            dataBinding.etDob.setEnabled(true);
            dataBinding.etDob.setFocusable(false);
            dataBinding.spinnerGender.setEnabled(true);
            dataBinding.spinnerGender.setBackground(ContextCompat.getDrawable(activity, R.drawable.spinner_drop));
            dataBinding.etEmail.setText(userModel.getEmail());

            dataBinding.tvLogout.setTextColor(ContextCompat.getColor(activity, R.color.light_gray));
            dataBinding.tvChangePassword.setTextColor(ContextCompat.getColor(activity, R.color.light_gray));
            dataBinding.tvReview.setTextColor(ContextCompat.getColor(activity, R.color.light_gray));

        } else {
            dataBinding.tvEdit.setVisibility(View.VISIBLE);
            dataBinding.tvCancel.setVisibility(View.GONE);
            dataBinding.btnUpdate.setVisibility(View.GONE);
            dataBinding.etFname.setVisibility(View.GONE);
            dataBinding.etLname.setVisibility(View.GONE);
            dataBinding.tvUserName.setVisibility(View.VISIBLE);
            dataBinding.etFname.setEnabled(false);
            dataBinding.etEmail.setEnabled(false);
            dataBinding.etLname.setEnabled(false);
            dataBinding.etDob.setEnabled(false);
            dataBinding.spinnerGender.setEnabled(false);
            dataBinding.spinnerGender.setBackgroundColor(ContextCompat.getColor(activity, R.color.white));
            dataBinding.etEmail.setText(userModel.getEmail());

            dataBinding.tvLogout.setTextColor(ContextCompat.getColor(activity, R.color.black));
            dataBinding.tvChangePassword.setTextColor(ContextCompat.getColor(activity, R.color.black));
            dataBinding.tvReview.setTextColor(ContextCompat.getColor(activity, R.color.black));
        }
    }
}
