package com.shoppy.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shoppy.R;
import com.shoppy.activity.HomeActivity;
import com.shoppy.adapter.OrderListAdapter;
import com.shoppy.baseclass.BaseFragment;
import com.shoppy.databinding.FragmentMyOrderBinding;
import com.shoppy.global.Constants;
import com.shoppy.listener.ItemOnClickListener;
import com.shoppy.model.OrderListModel;
import com.shoppy.retrofit.MethodName;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MyOrderFragment extends BaseFragment implements View.OnClickListener, ItemOnClickListener {
    private HomeActivity activity;
    private ArrayList<OrderListModel> orderList = new ArrayList<>();
    private FragmentMyOrderBinding dataBinding;
    private OrderListAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_my_order, container, false);
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
        dataBinding.actionBar.tvTitle.setText(getString(R.string.title_my_order));
        dataBinding.actionBar.ivDrawerMenu.setVisibility(View.VISIBLE);
        dataBinding.actionBar.ivBack.setVisibility(View.GONE);
        dataBinding.actionBar.ivDrawerMenu.setOnClickListener(this);
        dataBinding.noResult.tvNoResult.setText(getString(R.string.text_no_order));
        getUserOrders();
    }

    private void getUserOrders() {
        activity.callWb(activity, Constants.USER_ORDER_CRUD_URL, Constants.GET, MethodName.GET_USER_ORDERS, new JSONObject(), false, false, this, null);
    }

    @Override
    public void onClick(View v) {
        if (activity.isOffline(activity)) {
            activity.showSnackbar(getString(R.string.warn_no_internet), dataBinding.getRoot(), Snackbar.LENGTH_LONG);
            return;
        }
        switch (v.getId()) {
            case R.id.ivDrawerMenu:
                activity.openCloseDrawer();
                break;
        }
    }

    @Override
    public void onResponse(String response, String methodName, int responseCode) {
        super.onResponse(response, methodName, responseCode);
        try {
            JSONObject jsonObject = new JSONObject(response);
            if (jsonObject.getBoolean(Constants.SUCCESS_KEY)) {
                ArrayList<OrderListModel> tempList = new Gson().fromJson(jsonObject.getJSONArray("data").toString(), new TypeToken<ArrayList<OrderListModel>>() {
                }.getType());
                orderList.clear();
                orderList.addAll(tempList);
                setAdapter();
            } else {
                setAdapter();
                Snackbar.make(dataBinding.getRoot(), jsonObject.getString(Constants.MESSAGE_KEY), Snackbar.LENGTH_LONG).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setAdapter() {
        if (orderList.size() == 0) {
            dataBinding.noResult.getRoot().setVisibility(View.VISIBLE);
            dataBinding.rvOrders.setVisibility(View.GONE);
        } else {
            dataBinding.noResult.getRoot().setVisibility(View.GONE);
            dataBinding.rvOrders.setVisibility(View.VISIBLE);
            if (mAdapter == null) {
                mAdapter = new OrderListAdapter(activity, orderList, this);
                dataBinding.rvOrders.setLayoutManager(new LinearLayoutManager(activity));
                dataBinding.rvOrders.setItemAnimator(new DefaultItemAnimator());
                dataBinding.rvOrders.setAdapter(mAdapter);
                dataBinding.rvOrders.addItemDecoration(new RecyclerView.ItemDecoration() {
                    @Override
                    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                        super.getItemOffsets(outRect, view, parent, state);
                        outRect.set(30, 30, 30, 0);
                    }
                });
            } else {
                mAdapter.notifyDataSetChanged();
            }

        }
    }

    @Override
    public void onItemClicked(int position, View v, int type) {
        if (activity.isOffline(activity)) {
            activity.showSnackbar(getString(R.string.warn_no_internet), dataBinding.getRoot(), Snackbar.LENGTH_LONG);
            return;
        }
        OrderDetailsFragment fragment = OrderDetailsFragment.newInstance(orderList.get(position).getId());
        fragment.setTargetFragment(this, 122);
        activity.setFragment(fragment, R.id.framLay, true, true, null);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 122) {
            getUserOrders();
        }
    }
}
