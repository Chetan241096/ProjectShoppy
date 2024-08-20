package com.shoppy.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.google.gson.Gson;
import com.shoppy.R;
import com.shoppy.activity.HomeActivity;
import com.shoppy.baseclass.BaseFragment;
import com.shoppy.databinding.FragmentAddaddressBinding;
import com.shoppy.global.Constants;
import com.shoppy.model.AddressModel;
import com.shoppy.retrofit.MethodName;

import org.json.JSONException;
import org.json.JSONObject;

public class AddAddressFragment extends BaseFragment implements View.OnClickListener {
    private static final String ADDRESS_KEY = "ADDRESS_MODEL";
    private HomeActivity activity;
    private AddressModel addressModel;
    private FragmentAddaddressBinding dataBinding;
    private static String CALL_FROM = "CALL_FROM";
    private int callFrom;

    public static AddAddressFragment getInstance(AddressModel addressModel, int callFrom) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(ADDRESS_KEY, addressModel);
        bundle.putInt(CALL_FROM, callFrom);
        AddAddressFragment fragment = new AddAddressFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_addaddress, container, false);
        return dataBinding.getRoot();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (HomeActivity) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        setTargetFragment(null, 121);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        dataBinding.actionBar.ivBack.setVisibility(View.VISIBLE);
        dataBinding.actionBar.ivDrawerMenu.setVisibility(View.GONE);
        dataBinding.actionBar.tvTitle.setText("Address");
        dataBinding.actionBar.ivBack.setOnClickListener(this);
        dataBinding.btnAddAddress.setOnClickListener(this);
        callFrom = getArguments().getInt(CALL_FROM);
        if (getArguments() != null && getArguments().getParcelable(ADDRESS_KEY) != null) {
            addressModel = getArguments().getParcelable(ADDRESS_KEY);
            setDetails();
        }
    }

    private void setDetails() {
        if (addressModel != null) {
            dataBinding.etUserName.setText(addressModel.getUserName());
            dataBinding.etHouseNumber.setText(addressModel.getHouseNumber());
            dataBinding.etStreetName.setText(addressModel.getSocietyName());
            dataBinding.etLandmark.setText(addressModel.getLandmark());
            dataBinding.etCity.setText(addressModel.getCity());
            dataBinding.etState.setText(addressModel.getState());
        }
        dataBinding.btnAddAddress.setText(callFrom == 1 ? "Add Address" : "Update Address");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivBack:
                activity.onBackPressed();
                break;
            case R.id.btnAddAddress:
                if (validateAddress()) {
                    addAddress();
                }
                break;
        }
    }

    @Override
    public void onResponse(String response, String methodName, int responseCode) {
        super.onResponse(response, methodName, responseCode);
        try {
            JSONObject jsonObject = new JSONObject(response);
            if (jsonObject.getBoolean(Constants.SUCCESS_KEY)) {
                addressModel = new Gson().fromJson(jsonObject.getJSONObject("data").toString(), AddressModel.class);
                Intent intent = new Intent();
                intent.putExtra(ADDRESS_KEY, addressModel);
                getTargetFragment().onActivityResult(121, 200, intent);
                activity.onBackPressed();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResponseFail(String methodName, int responseCode) {
        super.onResponseFail(methodName, responseCode);
    }

    private void addAddress() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user_name", dataBinding.etUserName.getText().toString().trim());
            jsonObject.put("house_number", dataBinding.etHouseNumber.getText().toString().trim());
            jsonObject.put("society_name", dataBinding.etStreetName.getText().toString().trim());
            jsonObject.put("landmark", dataBinding.etLandmark.getText().toString().trim());
            jsonObject.put("address", dataBinding.etHouseNumber.getText().toString().trim() + "," + dataBinding.etStreetName.getText().toString().trim() + "," + dataBinding.etLandmark.getText().toString().trim() + "," + dataBinding.etCity.getText().toString().trim() + "," + dataBinding.etState.getText().toString().trim());
            jsonObject.put("is_default", 1);
            jsonObject.put("city", dataBinding.etCity.getText().toString().trim());
            jsonObject.put("state", dataBinding.etState.getText().toString().trim());
            if (callFrom == 1) {
                activity.callWb(activity, Constants.ADD_ADDRESS_URL, Constants.POST, MethodName.ADD_ADDRESS, jsonObject, true, false, this, null);
            } else {
                activity.callWb(activity, Constants.UPDATE_ADDRESS_URL, Constants.PUT, MethodName.UPDATE_ADDRESS, jsonObject, true, false, this, null);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private boolean validateAddress() {
        if (dataBinding.etUserName.getText().toString().trim().length() == 0) {
            Toast.makeText(activity, "Please enter user name", Toast.LENGTH_SHORT).show();
            return false;
        } else if (dataBinding.etHouseNumber.getText().toString().trim().length() == 0) {
            Toast.makeText(activity, "Please enter House Number", Toast.LENGTH_SHORT).show();
            return false;
        } else if (dataBinding.etStreetName.getText().toString().trim().length() == 0) {
            Toast.makeText(activity, "Please enter Street Name", Toast.LENGTH_SHORT).show();
            return false;
        } else if (dataBinding.etLandmark.getText().toString().trim().length() == 0) {
            Toast.makeText(activity, "Please enter LandMark", Toast.LENGTH_SHORT).show();
            return false;
        } else if (dataBinding.etCity.getText().toString().trim().length() == 0) {
            Toast.makeText(activity, "Please enter City", Toast.LENGTH_SHORT).show();
            return false;
        } else if (dataBinding.etState.getText().toString().trim().length() == 0) {
            Toast.makeText(activity, "Please enter State", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}
