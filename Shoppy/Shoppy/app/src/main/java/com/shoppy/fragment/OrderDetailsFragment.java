package com.shoppy.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.shoppy.R;
import com.shoppy.activity.HomeActivity;
import com.shoppy.adapter.OrderDetailsProductListAdapter;
import com.shoppy.baseclass.BaseFragment;
import com.shoppy.databinding.BottomDialogForCancelBinding;
import com.shoppy.databinding.BottomDialogForReviewBinding;
import com.shoppy.databinding.FragmentOrderDetailsBinding;
import com.shoppy.global.Constants;
import com.shoppy.listener.ItemOnClickListener;
import com.shoppy.model.OrderDetailsModel;
import com.shoppy.retrofit.MethodName;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.TimeUnit;

public class OrderDetailsFragment extends BaseFragment implements View.OnClickListener, ItemOnClickListener {

    private static final String ORDER_ID_KEY = "ORDER_ID";
    private static final int RETURN_APPLY_CODE = 111;
    private FragmentOrderDetailsBinding dataBinding;
    private HomeActivity activity;
    private int orderId;
    private OrderDetailsModel orderDetailsModel;
    private int lastProductReturnPosition = 0;
    private boolean isOTPConfirmationAsked = false;


    public static OrderDetailsFragment newInstance(int orderId) {
        Bundle bundle = new Bundle();
        bundle.putInt(ORDER_ID_KEY, orderId);
        OrderDetailsFragment fragment = new OrderDetailsFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (HomeActivity) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        setTargetFragment(null, 122);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_order_details, container, false);
        return dataBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        dataBinding.actionBar.ivBack.setVisibility(View.VISIBLE);
        dataBinding.actionBar.ivDrawerMenu.setVisibility(View.GONE);
        dataBinding.actionBar.ivBack.setOnClickListener(this);
        dataBinding.actionBar.tvTitle.setText(getString(R.string.title_order_details));
        dataBinding.btnReorder.setOnClickListener(this);
        dataBinding.btnGetInvoice.setOnClickListener(this);
        orderId = getArguments().getInt(ORDER_ID_KEY);
        getOrderDetails();
    }

    private void getOrderDetails() {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("order_id", orderId);
            activity.callWb(activity, Constants.GET_ORDER_DETAILS, Constants.GET, MethodName.GET_ORDER_DETAILS, jsonObject, false, false, this, null);
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
                if (methodName.equals(MethodName.GET_ORDER_DETAILS)) {
                    orderDetailsModel = new Gson().fromJson(jsonObject.getJSONObject("data").toString(), OrderDetailsModel.class);
                    setDeta();
                } else if (methodName.equals(MethodName.ADD_PRODUCT_REVIEW)) {
                    getOrderDetails();
                } else if (methodName.equals(MethodName.CANCEL_ORDER)) {
                    activity.showSnackbar(jsonObject.getString(Constants.MESSAGE_KEY), dataBinding.getRoot(), Snackbar.LENGTH_SHORT);
                    getOrderDetails();
                    if (getTargetFragment() != null)
                        getTargetFragment().onActivityResult(122, 1, null);
                } else if (methodName.equals(MethodName.GET_INVOICE)) {
                    activity.showSnackbar(jsonObject.getString(Constants.MESSAGE_KEY), dataBinding.getRoot(), Snackbar.LENGTH_SHORT);
                }
            } else {
                activity.showSnackbar(jsonObject.getString(Constants.MESSAGE_KEY), dataBinding.getRoot(), Snackbar.LENGTH_LONG);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("SetTextI18n")
    private void setDeta() {
        if (orderDetailsModel == null) {
            dataBinding.mainScrollView.setVisibility(View.GONE);
            dataBinding.noResult.getRoot().setVisibility(View.VISIBLE);
            return;
        } else {
            dataBinding.mainScrollView.setVisibility(View.VISIBLE);
            dataBinding.noResult.getRoot().setVisibility(View.GONE);
        }
        dataBinding.tvOrderNumber.setText(orderDetailsModel.getOrderNumber());
        dataBinding.tvAddress.setText(orderDetailsModel.getAddress());
        dataBinding.tvSubtotal.setText(getString(R.string.rupee_symbol) + orderDetailsModel.getOrderSubtotal() + "");
        dataBinding.tvTotal.setText(getString(R.string.rupee_symbol) + orderDetailsModel.getOrderTotal() + "");
        dataBinding.tvOrderDate.setText("Placed on " + orderDetailsModel.getOrderDate());
        dataBinding.tvUserName.setText(orderDetailsModel.getUserName());
        dataBinding.tvTotalProducts.setText("x" + orderDetailsModel.getProducts().size());

        if (orderDetailsModel.getPaymentMethod() == 3) {
            dataBinding.tvPaymentMode.setText("Paid via Wallet");
        } else if (orderDetailsModel.getPaymentMethod() == 2) {
            dataBinding.tvPaymentMode.setText("Paid via Paypal");
        } else {
            dataBinding.tvPaymentMode.setText("Pay via Cash");
        }

        dataBinding.tvDelivery.setText(activity.getString(R.string.rupee_symbol) + orderDetailsModel.getDeliveryCharges());

        OrderDetailsProductListAdapter adapter = new OrderDetailsProductListAdapter(activity, orderDetailsModel.getProducts(), this, orderDetailsModel.getOrderStatus());
        dataBinding.rvCartProducts.setLayoutManager(new LinearLayoutManager(activity));
        dataBinding.rvCartProducts.setItemAnimator(new DefaultItemAnimator());
        dataBinding.rvCartProducts.setAdapter(adapter);


        dataBinding.tvSavingTotal.setVisibility((orderDetailsModel.getDiscount() + orderDetailsModel.getCoupanDiscount()) > 0 ? View.VISIBLE : View.GONE);
        dataBinding.tvSavingTotal.setText(activity.getString(R.string.text_total_saving_order_details, activity.getFormated(orderDetailsModel.getDiscount() + orderDetailsModel.getCoupanDiscount())));


        dataBinding.btnGetInvoice.setVisibility(View.GONE);
        dataBinding.btnReorder.setVisibility(View.GONE);

        dataBinding.tvOrderStatus.setText(activity.getOrderStatus(orderDetailsModel.getOrderStatus()));
        if (orderDetailsModel.getOrderStatus() == 1 || orderDetailsModel.getOrderStatus() == 2) {
            dataBinding.btnReorder.setVisibility(View.VISIBLE);
            dataBinding.btnReorder.setText("Cancel");
        }
    }

    @Override
    public void onClick(View v) {
        if (activity.isOffline(activity)) {
            activity.showSnackbar(getString(R.string.warn_no_internet), dataBinding.getRoot(), Snackbar.LENGTH_SHORT);
            return;
        }
        switch (v.getId()) {
            case R.id.ivBack:
                activity.onBackPressed();
                break;
            case R.id.btnReorder:
                if (orderDetailsModel.getOrderStatus() == 1 || orderDetailsModel.getOrderStatus() == 2) {
                    cancelOrder();
                }
                break;
            case R.id.btnGetInvoice:
        }
    }

    private void cancelOrder() {
        final BottomSheetDialog mBottomSheetDialog = new BottomSheetDialog(activity);
        final BottomDialogForCancelBinding cancelBinding = DataBindingUtil.inflate(activity.getLayoutInflater(), R.layout.bottom_dialog_for_cancel, null, false);
        mBottomSheetDialog.setContentView(cancelBinding.getRoot());
        mBottomSheetDialog.show();

        cancelBinding.btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cancelBinding.etReason.getText().toString().trim().length() == 0) {
                    Snackbar.make(cancelBinding.getRoot(), "Please give proper reason for cancel order.", Snackbar.LENGTH_SHORT).show();
                } else {
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("order_id", orderDetailsModel.getId());
                        jsonObject.put("cancel_reason", cancelBinding.etReason.getText().toString().trim());
                        activity.callWb(activity, Constants.CANCEL_ORDER_URL, Constants.POST, MethodName.CANCEL_ORDER, jsonObject, true, true, OrderDetailsFragment.this, null);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    mBottomSheetDialog.dismiss();
                }
            }
        });
    }

    private void openReviewBottom(final int position) {
        final BottomSheetDialog mBottomSheetDialog = new BottomSheetDialog(activity);
        final BottomDialogForReviewBinding reviewBinding = DataBindingUtil.inflate(activity.getLayoutInflater(), R.layout.bottom_dialog_for_review, null, false);
        mBottomSheetDialog.setContentView(reviewBinding.getRoot());
        mBottomSheetDialog.show();

        reviewBinding.btnAddReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (reviewBinding.rating.getRating() == 0) {
                    Snackbar.make(reviewBinding.getRoot(), "Please give rating for submit review", Snackbar.LENGTH_SHORT).show();
                } else {
                    mBottomSheetDialog.dismiss();
                    sendProductReview(position, reviewBinding.rating.getRating(), reviewBinding.etReview.getText().toString().trim());
                }
            }
        });
    }

    private void sendProductReview(int position, float rating, String review) {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("product_id", orderDetailsModel.getProducts().get(position).getProductId());
            jsonObject.put("rating", rating);
            jsonObject.put("review", review);
            jsonObject.put("order_id", orderDetailsModel.getId());
            activity.callWb(activity, Constants.ADD_REVIEW_RATING, Constants.POST, MethodName.ADD_PRODUCT_REVIEW, jsonObject, true, false, this, null);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onItemClicked(int position, View v, int type) {
        if (activity.isOffline(activity)) {
            activity.showSnackbar(getString(R.string.warn_no_internet), dataBinding.getRoot(), Snackbar.LENGTH_SHORT);
            return;
        }
        if (type == 1) { // 1 for add review rating  , 2 for return
            openReviewBottom(position);
        }
    }

}
