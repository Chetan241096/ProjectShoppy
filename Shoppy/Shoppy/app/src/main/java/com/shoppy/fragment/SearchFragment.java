package com.shoppy.fragment;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shoppy.R;
import com.shoppy.activity.HomeActivity;
import com.shoppy.adapter.SearchListAdapter;
import com.shoppy.baseclass.BaseFragment;
import com.shoppy.databinding.FragmentSearchBinding;
import com.shoppy.global.Constants;
import com.shoppy.listener.ItemOnClickListener;
import com.shoppy.model.SearchProductModel;
import com.shoppy.retrofit.MethodName;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class SearchFragment extends BaseFragment implements View.OnClickListener, ItemOnClickListener {
    private FragmentSearchBinding dataBinding;
    private HomeActivity activity;
    private boolean is_shop = false;
    private Timer timer;
    private ArrayList<SearchProductModel> searchProductList = new ArrayList<>();
    private SearchListAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_search, container, false);
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
        dataBinding.etSearch.requestFocus();
        activity.openKeyboard(dataBinding.etSearch);
        dataBinding.ivBack.setOnClickListener(this);

        dataBinding.etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (timer != null) {
                    timer.cancel();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        if (dataBinding.etSearch.getText().toString().trim().length() > 1)
                            callSearch();
                    }
                }, 200);
            }
        });
        dataBinding.etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH && dataBinding.etSearch.getText().toString().trim().length() > 0 && !is_shop) {
                    activity.setFragment(ProductWallFragment.getInstance(dataBinding.etSearch.getText().toString().trim(), 0, 1), R.id.framLay, true, true, null);
                    return true;
                }
                return false;
            }
        });
        dataBinding.etSearch.requestFocus();
    }


    private void setAdapter() {
        if (searchProductList.size() == 0) {
            dataBinding.tvNoResult.setText("No Products Found");
            dataBinding.tvNoResult.setVisibility(View.VISIBLE);
            dataBinding.rvSearchList.setVisibility(View.GONE);
        } else {
            dataBinding.tvNoResult.setVisibility(View.GONE);
            dataBinding.rvSearchList.setVisibility(View.VISIBLE);
            if (mAdapter == null) {
                mAdapter = new SearchListAdapter(searchProductList, this);
                dataBinding.rvSearchList.setLayoutManager(new LinearLayoutManager(activity));
                dataBinding.rvSearchList.setItemAnimator(new DefaultItemAnimator());
                dataBinding.rvSearchList.setAdapter(mAdapter);
            } else {
                mAdapter.notifyDataSetChanged();
            }
        }

    }

    private void callSearch() {
        try {
            //Event Fabric Search
            String searchKey = dataBinding.etSearch.getText().toString().trim();
            Log.d("Search_KEY", searchKey);

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("product_name", dataBinding.etSearch.getText().toString());
            activity.callWb(activity, Constants.SEARCH_PRODUCT_URL, Constants.GET, MethodName.SEARCH_PRODUCT, jsonObject, false, false, this, null);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResponse(String response, String methodName, int responseCode) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            if (methodName.equals(MethodName.SEARCH_PRODUCT)) {
                if (jsonObject.get("data") instanceof JSONArray && jsonObject.getJSONArray("data").length() != 0) {
                    ArrayList<SearchProductModel> tempList = new Gson().fromJson(jsonObject.getJSONArray("data").toString(), new TypeToken<ArrayList<SearchProductModel>>() {
                    }.getType());
                    searchProductList.clear();
                    searchProductList.addAll(tempList);
                } else {
                    searchProductList.clear();
                }
                setAdapter();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onResponseFail(String methodName, int responseCode) {

    }

    @Override
    public void onItemClicked(int position, View v, int type) {
        if (activity.isOffline(activity)) {
            activity.showSnackbar(getString(R.string.warn_no_internet), dataBinding.getRoot(), Snackbar.LENGTH_LONG);
            return;
        }
        activity.setFragment(ProductWallFragment.getInstance(searchProductList.get(position).getProductName(), searchProductList.get(position).getCategoryId(),1), R.id.framLay, true, true, null);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivBack:
                activity.onBackPressed();
                break;
        }
    }


}