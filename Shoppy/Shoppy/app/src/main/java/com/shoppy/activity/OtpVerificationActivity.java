package com.shoppy.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;


import com.alimuzaffar.lib.pin.PinEntryEditText;
import com.google.android.material.snackbar.Snackbar;
import com.shoppy.R;
import com.shoppy.baseclass.BaseActivity;
import com.shoppy.databinding.ActivityOtpVerificationBinding;
import com.shoppy.global.Constants;
import com.shoppy.global.Pref;
import com.shoppy.retrofit.MethodName;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.TimeUnit;

public class OtpVerificationActivity extends BaseActivity {
    private int userID;
    private String mobileNumber;
    private ActivityOtpVerificationBinding dataBinding;
    private int callFrom; // 1 for forgotPass, 2 for Login, 3 for signUp
    private String password;
    private CountDownTimer optTimer;
    private boolean isTimerFinished = false;
//    private LoadingEventDialog loadingEventDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dataBinding = DataBindingUtil.setContentView(this, R.layout.activity_otp_verification);
        userID = getIntent().getIntExtra(Constants.USER_ID_KEY, 0);
        mobileNumber = getIntent().getStringExtra(Constants.MOBILE_NUMBER_KEY);
        callFrom = getIntent().getIntExtra(Constants.CALLED_FROM_KEY, 0);
        password = getIntent().hasExtra(Constants.PASSWORD_KEY) ? getIntent().getStringExtra(Constants.PASSWORD_KEY) : "";
//        loadingEventDialog = new LoadingEventDialog(this, "Fetching OTP...");
//        loadingEventDialog.setCancelable(false);
        //OTP Auto detect
//        startListener();
//
//        dataBinding.tvResend.setTextColor(ContextCompat.getColor(this, R.color.gray_text));
//        dataBinding.tvResend.setEnabled(false);
//        optTimer = new CountDownTimer(120000, 1000) {
//            @Override
//            public void onTick(long millisUntilFinished) {
//                dataBinding.tvTimer.setText(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) + ":" + (TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));
//            }
//
//            @Override
//            public void onFinish() {
//                isTimerFinished = true;
//                dataBinding.tvResend.setEnabled(true);
//                dataBinding.tvResend.setTextColor(ContextCompat.getColor(OtpVerificationActivity.this, R.color.orange_dark));
//
//            }
//        };
//        optTimer.start();

//        dataBinding.tvOtpMsg.setText(getString(R.string.msg_enter_otp, mobileNumber));
        dataBinding.pinEntryEditText.setOnPinEnteredListener(new PinEntryEditText.OnPinEnteredListener() {
            @Override
            public void onPinEntered(CharSequence str) {
                callVerifyOTP();
            }
        });
//        dataBinding.tvResend.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                try {
//                    JSONObject jsonObject = new JSONObject();
//                    jsonObject.put("phone", mobileNumber);
//                    jsonObject.put("request_type", 1); // 2 for seller 1 for User
//                    callWb(OtpVerificationActivity.this, Constants.OTP_SEND_URL, "POST", MethodName.SEND_OTP, jsonObject, true, false, OtpVerificationActivity.this, null, null);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        });

        dataBinding.btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dataBinding.pinEntryEditText.getText().toString().length() == 4) {
                    callVerifyOTP();
                } else {
                    Toast.makeText(OtpVerificationActivity.this, "Please enter OTP for verify your mobile number.", Toast.LENGTH_SHORT).show();
                }
            }
        });

//        IntentFilter intentFilter = new IntentFilter();
//        intentFilter.addAction(Constants.RECEIVE_OTP_ACTION);
//
//        LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(this);
//        localBroadcastManager.registerReceiver(new BroadcastReceiver() {
//            @Override
//            public void onReceive(Context context, Intent intent) {
//                if (intent.getAction() != null && intent.getAction().equals(Constants.RECEIVE_OTP_ACTION)) {
//                    if (intent.getStringExtra(Constants.OTP_KEY).equals("")) {
//                        loadingEventDialog.dismiss();
//                        Toast.makeText(OtpVerificationActivity.this, "Fail to fetch OTP please enter manually.", Toast.LENGTH_SHORT).show();
//                    } else {
//                        loadingEventDialog.dismiss();
//                        dataBinding.pinEntryEditText.setText(intent.getStringExtra(Constants.OTP_KEY));
//                    }
//                }
//            }
//        }, intentFilter);
    }

    private void callVerifyOTP() {
        if(dataBinding.pinEntryEditText.getText().toString().trim().equals("1234")){
            redirectAfterOtp();
        }
        else {
            Toast.makeText(this, "Please enter valid OTP.", Toast.LENGTH_SHORT).show();
        }
//        JSONObject jsonObject = new JSONObject();
//        try {
//            jsonObject.put("otp_number", dataBinding.pinEntryEditText.getText().toString().trim());
////            jsonObject.put("phone", mobileNumber);
//            jsonObject.put("id", userID);
//            jsonObject.put("request_type", 1);
//            callWb(this, Constants.OTP_VERIFY_URL, Constants.POST, MethodName.VERIFY_OTP, jsonObject, true, false, this, null);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
    }

    @Override
    public void onResponse(String response, String methodName, int responseCode) {
        super.onResponse(response, methodName, responseCode);
        try {
            JSONObject jsonObject = new JSONObject(response);
            if (jsonObject.getBoolean(Constants.SUCCESS_KEY)) {
                if (methodName.equals(MethodName.VERIFY_OTP)) {
                    Log.d("VerifyOTP", jsonObject.getString(Constants.MESSAGE_KEY));
                    Toast.makeText(this, jsonObject.getString(Constants.MESSAGE_KEY), Toast.LENGTH_SHORT).show();
                    redirectAfterOtp();
                } else if (methodName.equals(MethodName.LOGIN)) {
                    JSONObject userInfo = jsonObject.getJSONObject("userInfo");
                    Pref.prefsLoginUser(this,
                            jsonObject.getString("Token"),
                            true,
                            userInfo.getInt("id"),
                            userInfo.getString("email"),
                            userInfo.getString("phone"),
                            "",
                            false,
                            jsonObject.getString("refreshToken"),
                            userInfo.getString("base_path"),
                            ""
                    );

                    Intent intent1 = new Intent(this, HomeActivity.class);
                    intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent1);
                    setResult(200);
                    finish();
                }
            } else {
                showSnackbar(jsonObject.getString(Constants.MESSAGE_KEY), dataBinding.getRoot(), Snackbar.LENGTH_LONG);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

//    private void startListener() {
//        SmsRetrieverClient client = SmsRetriever.getClient(this /* context */);
//        Task<Void> task = client.startSmsRetriever();
//        // Listen for success/failure of the start Task. If in a background thread, this
//        // can be made blocking using Tasks.await(task, [timeout]);
//        task.addOnSuccessListener(new OnSuccessListener<Void>() {
//            @Override
//            public void onSuccess(Void aVoid) {
//                startFetching();
//            }
//        });
//        task.addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                loadingEventDialog.dismiss();
//                Toast.makeText(OtpVerificationActivity.this, "Fail to fetch OTP please enter manually.", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }

//    private void startFetching() {
//        loadingEventDialog.show();
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                if (loadingEventDialog != null)
//                    loadingEventDialog.dismiss();
//                Toast.makeText(OtpVerificationActivity.this, "Fail to fetch OTP please enter manually.", Toast.LENGTH_SHORT).show();
//            }
//        }, 30000);
//    }

    @Override
    public void onResponseFail(String methodName, int responseCode) {
        super.onResponseFail(methodName, responseCode);
        Toast.makeText(this, "Something going wrong please try again.", Toast.LENGTH_SHORT).show();
    }

    private void redirectAfterOtp() {
        switch (callFrom) {// 1 for forgotPass, 2 for Login, 3 for signUp
            case 1:
                Intent intent = new Intent(this, ChangePasswordActivity.class);
                intent.putExtra(Constants.USER_ID_KEY, userID);
                intent.putExtra(Constants.MOBILE_NUMBER_KEY, mobileNumber);
                startActivity(intent);
                finish();
                break;
            case 2:
                try {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put(Constants.GRANT_TYPE, "password");
                    jsonObject.put(Constants.CLIENT_ID, "mobileapp");
                    jsonObject.put(Constants.CLIENT_SECRET, "zapplermobile");
                    jsonObject.put("username", mobileNumber);
                    jsonObject.put("password", password);
                    callWb(this, Constants.LOGIN_URL, "POST", MethodName.LOGIN, jsonObject, true, false, this, null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case 3:
                Log.d("redirectAfterOtp", callFrom + " ");
                Intent intent2 = new Intent(this, LoginActivity.class);
                intent2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent2);
                finish();
                break;
        }
    }
}
