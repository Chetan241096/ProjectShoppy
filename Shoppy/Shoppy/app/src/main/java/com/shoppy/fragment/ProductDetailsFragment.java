package com.shoppy.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shoppy.R;
import com.shoppy.activity.HomeActivity;
import com.shoppy.adapter.ReviewsListAdapter;
import com.shoppy.baseclass.BaseFragment;
import com.shoppy.databinding.FragmentProductDetailsBinding;
import com.shoppy.global.Constants;
import com.shoppy.global.Pref;
import com.shoppy.listener.ItemOnClickListener;
import com.shoppy.listener.ItemOnClickListenerChild;
import com.shoppy.model.GeneralReviewModel;
import com.shoppy.model.ProductModel;
import com.shoppy.retrofit.MethodName;
import com.shoppy.retrofit.RetrofitResponse;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Currency;

public class ProductDetailsFragment extends BaseFragment implements RetrofitResponse, View.OnClickListener, ItemOnClickListener {
    private static final String PRODUCT_ID = "PRODUCT_ID";
    private static final String SELLER_ID = "SELLER_ID";
    private FragmentProductDetailsBinding dataBinding;
    private HomeActivity activity;
    int quantity = 1;
    private int productId;
    private int sellerId;
    private ProductModel productDetailsModel;
    private boolean isProductOutOfStock;
    private boolean isAddedInCart = false;
    private ReviewsListAdapter reviewsListAdapter;
    private ArrayList<GeneralReviewModel> reviewList = new ArrayList<>();


    public static ProductDetailsFragment getInstance(int productId) {
        Bundle bundle = new Bundle();
        bundle.putInt(PRODUCT_ID, productId);
        ProductDetailsFragment fragment = new ProductDetailsFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (HomeActivity) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_product_details, container, false);
        return dataBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        dataBinding.tvProductReviewLink.setOnClickListener(this);
        dataBinding.btnAddToCart.setOnClickListener(this);
        dataBinding.btnBuyNow.setOnClickListener(this);
        dataBinding.btnBuyNow.setOnClickListener(this);
        productId = getArguments().getInt(PRODUCT_ID);
        sellerId = getArguments().getInt(SELLER_ID);
        dataBinding.ivBack.setOnClickListener(this);
        getProductDetails();
        setProductDetails();

    }

    private void getProductDetails() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("product_id", productId);
            activity.callWb(activity, Constants.GET_PRODUCT_DETAILS, Constants.GET, MethodName.GET_PRODUCT_DETAILS, jsonObject, false, false, this, null);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onResponse(String response, String methodName, int responseCode) {
        try {
            JSONObject jsonResponse = new JSONObject(response);
            if (jsonResponse.getBoolean(Constants.SUCCESS_KEY)) {
                if (methodName.equalsIgnoreCase(MethodName.GET_PRODUCT_DETAILS)) {
                    JSONObject jsonBody = jsonResponse.getJSONObject("data");
                    JSONObject jsonProductDetails = jsonBody.getJSONObject("product");
                    productDetailsModel = new Gson().fromJson(jsonProductDetails.toString(), ProductModel.class);
                    ArrayList<GeneralReviewModel> tempList = new Gson().fromJson(jsonBody.getJSONArray("rating_reviews").toString(), new TypeToken<ArrayList<GeneralReviewModel>>() {
                    }.getType());
                    reviewList.clear();
                    reviewList.addAll(tempList);
                    setProductDetails();

                } else if (methodName.equalsIgnoreCase(MethodName.ADD_TO_CART)) {
                    Toast.makeText(activity, jsonResponse.getString(Constants.MESSAGE_KEY), Toast.LENGTH_SHORT).show();
                    isAddedInCart = true;
                    dataBinding.btnAddToCart.setText("Go to Cart");
                }
//                else if (methodName.equals(MethodName.NOTIFY_USER)) {
//                    dataBinding.btnBuyNow.setText("Marked Down");
//                    activity.showSnackbar(jsonResponse.getString(Constants.MESSAGE_KEY), dataBinding.getRoot(), Snackbar.LENGTH_SHORT);
//                }
            } else {
                activity.showSnackbar(jsonResponse.getString(Constants.MESSAGE_KEY), dataBinding.getRoot(), Snackbar.LENGTH_SHORT);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setProductDetails() {
//        productDetailsModel = new ProductModel(20 , "", 19 , 2, "", 4, 1, 2, 5, 10, "Product ",  2, false, "Xl", 1, false, "Size");

        if (productDetailsModel == null) {
            dataBinding.noResult.getRoot().setVisibility(View.VISIBLE);
            dataBinding.groupData.setVisibility(View.GONE);
            return;
        } else {
            dataBinding.noResult.getRoot().setVisibility(View.GONE);
            dataBinding.groupData.setVisibility(View.VISIBLE);
        }
        isProductOutOfStock = productDetailsModel.getAvailableStock() == 0;

        if (isProductOutOfStock) {
            dataBinding.btnAddToCart.setText("Out of Stock");
            dataBinding.btnBuyNow.setText("Notify Me");
        } else {
            dataBinding.btnAddToCart.setText("Add to Cart");
            dataBinding.btnBuyNow.setText("Buy Now");
        }
        dataBinding.tvEnumValue.setText(productDetailsModel.getEnumValue());
        dataBinding.tvProductName.setText(productDetailsModel.getName());
        dataBinding.tvDescription.setText(productDetailsModel.getDescription());
//        dataBinding.tvSortDescription.setText(productDetailsModel.getShortDescription());
        dataBinding.tvRatingCount.setText(productDetailsModel.getRatingCount() == 0 ? "(No Reviews)" : "(" + productDetailsModel.getRatingCount() + ")");
        dataBinding.productRating.setRating(productDetailsModel.getRating());

        Glide.with(activity)
//                .load(Pref.getImageUrl(activity) + shopDetailsModel.getImageUrl())
                .load(R.drawable.tshirt_image_test)
                .into(dataBinding.ivProductImage);

        dataBinding.tvPrice.setText(activity.getString(R.string.rupee_symbol) + activity.getFormated(productDetailsModel.getSellingPrice()));
        if (productDetailsModel.getDiscountPrice() != 0) {
            dataBinding.tvDiscount.setVisibility(View.VISIBLE);
            dataBinding.tvProductMRP.setVisibility(View.VISIBLE);
            dataBinding.tvDiscount.setText(activity.getString(R.string.rupee_symbol) + activity.getFormated(productDetailsModel.getDiscountPrice()) + " OFF");
            dataBinding.tvProductMRP.setPaintFlags(dataBinding.tvProductMRP.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            dataBinding.tvProductMRP.setText(activity.getString(R.string.rupee_symbol) + activity.getFormated(productDetailsModel.getSellingPrice() + productDetailsModel.getDiscountPrice()) + "");
        } else {
            dataBinding.tvDiscount.setVisibility(View.GONE);
            dataBinding.tvProductMRP.setVisibility(View.GONE);
        }

        if (reviewList == null || reviewList.size() == 0) {
            dataBinding.tvNoReviews.setVisibility(View.VISIBLE);
        } else {
            dataBinding.tvNoReviews.setVisibility(View.GONE);
            if (reviewsListAdapter == null) {
                reviewsListAdapter = new ReviewsListAdapter(activity, reviewList, this,1);
                dataBinding.rvReviews.setItemAnimator(new DefaultItemAnimator());
                dataBinding.rvReviews.setLayoutManager(new LinearLayoutManager(activity));
                dataBinding.rvReviews.setAdapter(reviewsListAdapter);
            } else {
                reviewsListAdapter.notifyDataSetChanged();
            }
        }
    }


    @Override
    public void onResponseFail(String methodName, int responseCode) {
        activity.showSnackbar("Something going wrong!", dataBinding.getRoot(), Snackbar.LENGTH_SHORT);
    }

    @Override
    public void onClick(View view) {
        if (activity.isOffline(activity)) {
            activity.showSnackbar(getString(R.string.warn_no_internet), dataBinding.getRoot(), Snackbar.LENGTH_SHORT);
            return;
        }
        switch (view.getId()) {
            case R.id.ivBack:
                activity.onBackPressed();
                break;
            case R.id.btnAddToCart:
                if (isAddedInCart) {
                    activity.setFragment(MyCartFragment.getInstance(1, productDetailsModel.getId()), R.id.framLay, true, true, null);
                } else if (!isProductOutOfStock)
                    addToCart();
                else
                    Toast.makeText(activity, "Product out of stock", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btnBuyNow:
                if (!isProductOutOfStock)
                    callBuyNow();
                else
                    Toast.makeText(activity, "You will be notified once product back in stock.", Toast.LENGTH_LONG).show();
                break;
        }
    }

    private void callBuyNow() {
        String attributeName = "";
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("product_id", productDetailsModel.getId());
            activity.setFragment(MyCartFragment.getInstance(2, productDetailsModel.getId()), R.id.framLay, true, true, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addToCart() {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("product_id", productDetailsModel.getId());
            jsonObject.put("quantity", 1);
            activity.callWb(activity, Constants.ADD_TO_CART_URL, Constants.POST, MethodName.ADD_TO_CART, jsonObject, true, false, this, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onItemClicked(int position, View v, int type) {

    }
}