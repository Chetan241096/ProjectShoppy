package com.shoppy.fragment;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shoppy.R;
import com.shoppy.activity.HomeActivity;
import com.shoppy.adapter.ProductListAdapter;
import com.shoppy.baseclass.BaseFragment;
import com.shoppy.databinding.FragmentProductListBinding;
import com.shoppy.global.Constants;
import com.shoppy.listener.ItemOnClickListener;
import com.shoppy.model.ProductModel;
import com.shoppy.retrofit.MethodName;
import com.shoppy.retrofit.RetrofitResponse;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ProductListFragment extends BaseFragment implements ItemOnClickListener, View.OnClickListener {

    private static final String CATE_ID = "CATE_ID";
    private static final String CATE_NAME = "CATE_NAME";
    private HomeActivity activity;
    private FragmentProductListBinding dataBinding;
    private int cateId;
    private ArrayList<ProductModel> productList = new ArrayList<>();
    private ArrayList<ProductModel> tempList = new ArrayList<>();
    private ProductListAdapter mAdapter;
    private LinearLayoutManager mLinearLayoutManager;
    private boolean isCalling;
    private int pageNumber = 1;
    private int recordsInOnePage;


    public static ProductListFragment getInstance(int cateId, String cateName) {
        Bundle bundle = new Bundle();
        bundle.putInt(CATE_ID, cateId);
        bundle.putString(CATE_NAME, cateName);
        ProductListFragment fragment = new ProductListFragment();
        fragment.setArguments(bundle);
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_product_list, container, false);
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
        cateId = getArguments().getInt(CATE_ID);

        dataBinding.rvProductList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                int lastvisibleitemposition = mLinearLayoutManager.findLastVisibleItemPosition();
                if (mAdapter != null && lastvisibleitemposition == mAdapter.getItemCount() - 1) {
                    if (!isCalling && productList.size() > 0) {
                        if (tempList.size() >= recordsInOnePage) {
                            pageNumber++;
                            getProductByCategory();
                        }
                    }
                }
            }
        });

        getProductByCategory();
    }

    private void getProductByCategory() {
        isCalling = true;
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("category_id", cateId);
            jsonObject.put("order_by", "mrp");
            jsonObject.put("order_type", "asc");
            jsonObject.put("page_number", pageNumber);
            activity.callWb(activity, Constants.GET_PRODUCT_BY_CATEGORIES, Constants.POST, MethodName.GET_PRODUCT_BY_CATEGORY, jsonObject, pageNumber > 1, false, new RetrofitResponse() {
                @Override
                public void onResponse(String response, String methodName, int responseCode) {
                    try {
                        isCalling = false;
                        JSONObject jsonObject = new JSONObject(response);
                        if (jsonObject.getBoolean(Constants.SUCCESS_KEY)) {
                            if (methodName.equals(MethodName.GET_PRODUCT_BY_CATEGORY)) {
                                JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                                recordsInOnePage = jsonObject1.getInt("records_limit");
                                ArrayList<ProductModel> temp = new Gson().fromJson(jsonObject1.getJSONArray("products").toString(), new TypeToken<ArrayList<ProductModel>>() {
                                }.getType());

                                if (pageNumber <= 1) {
                                    productList.clear();
                                }
                                tempList.clear();
                                tempList.addAll(temp);

                                productList.addAll(tempList);
                                setAdapter();
                            }
                        } else {
                            tempList.clear();
                            setAdapter();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onResponseFail(String methodName, int responseCode) {

                }
            }, null);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    private void setAdapter() {
        if (productList == null || productList.size() == 0) {
            dataBinding.rvProductList.setVisibility(View.GONE);
            dataBinding.noResult.getRoot().setVisibility(View.VISIBLE);
            dataBinding.noResult.tvNoResult.setText(getString(R.string.mdg_no_product_available));
        } else {
            dataBinding.rvProductList.setVisibility(View.VISIBLE);
            dataBinding.noResult.getRoot().setVisibility(View.GONE);

            if (mAdapter == null) {
                mLinearLayoutManager = new LinearLayoutManager(activity);
                mAdapter = new ProductListAdapter(activity, productList, this, 1);
                dataBinding.rvProductList.setItemAnimator(new DefaultItemAnimator());
                dataBinding.rvProductList.setLayoutManager(mLinearLayoutManager);
                dataBinding.rvProductList.setAdapter(mAdapter);
                dataBinding.rvProductList.addItemDecoration(new RecyclerView.ItemDecoration() {
                    @Override
                    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                        super.getItemOffsets(outRect, view, parent, state);
                        outRect.set(0, 15, 0, 0);
                    }
                });
            } else {
                mAdapter.notifyDataSetChanged();
            }
        }

    }

    @Override
    public void onResponseFail(String methodName, int responseCode) {
        super.onResponseFail(methodName, responseCode);
    }

    @Override
    public void onItemClicked(int position, View v, int type) {
        if (activity.isOffline(activity)) {
            activity.showSnackbar(getString(R.string.warn_no_internet), dataBinding.getRoot(), Snackbar.LENGTH_LONG);
            return;
        }
        activity.setFragment(ProductDetailsFragment.getInstance(productList.get(position).getId()), R.id.framLay, true, true, null);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivBack:
                activity.onBackPressed();
                break;
        }
    }

    public void initFragment() {
        pageNumber = 1;
        productList.clear();
        mAdapter = null;
        getProductByCategory();
    }
}

