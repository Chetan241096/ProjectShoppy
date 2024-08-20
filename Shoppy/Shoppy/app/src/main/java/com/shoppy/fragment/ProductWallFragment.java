package com.shoppy.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shoppy.R;
import com.shoppy.activity.HomeActivity;
import com.shoppy.adapter.ProductListAdapter;
import com.shoppy.baseclass.BaseFragment;
import com.shoppy.databinding.BottomSheetDialogSortBinding;
import com.shoppy.databinding.FragmentProductwallBinding;
import com.shoppy.global.Constants;
import com.shoppy.listener.ItemOnClickListener;
import com.shoppy.model.CategoryModel;
import com.shoppy.model.ProductModel;
import com.shoppy.retrofit.MethodName;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ProductWallFragment extends BaseFragment implements ItemOnClickListener, View.OnClickListener {
    private static final String SEARCH_KEY = "SEARCH_KEY";
    private static final String CATEGORY_ID = "CATEGORY_ID";
    private static final String CALLED_FROM = "CALLED_FROM";
    private FragmentProductwallBinding dataBinding;
    private String searchKey;
    private HomeActivity activity;
    private ArrayList<ProductModel> productWallList = new ArrayList<>();
    private ArrayList<ProductModel> tempList = new ArrayList<>();
    private ProductListAdapter mAdapter;
    private int cat_id = 0;
    private int pageNumber = 1;
    private String sortType = Constants.SORT_TYPE_PRICE_DESC;
    private int cartCount = 0;
    private int callFrom = 1;
    private LinearLayoutManager mLinearLayoutManager;
    private GridLayoutManager mGridLayoutManager;
    private int recordsInOnePage;
    private String orderBy;
    private boolean isCalling;

    public static ProductWallFragment getInstance(String searchKey, int cat_id, int callFrom) { //1 for product wall 2 for category
        Bundle bundle = new Bundle();
        bundle.putString(SEARCH_KEY, searchKey);
        bundle.putInt(CATEGORY_ID, cat_id);
        bundle.putInt(CALLED_FROM, callFrom);
        ProductWallFragment fragment = new ProductWallFragment();
        fragment.setArguments(bundle);
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
        dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_productwall, container, false);
        return dataBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        dataBinding.actionBar.ivBack.setVisibility(View.VISIBLE);
        dataBinding.actionBar.ivDrawerMenu.setVisibility(View.GONE);
        dataBinding.actionBar.ivBack.setOnClickListener(this);
        searchKey = getArguments().getString(SEARCH_KEY);
        cat_id = getArguments().getInt(CATEGORY_ID);
        callFrom = getArguments().getInt(CALLED_FROM);

        dataBinding.actionBar.tvTitle.setText(searchKey);
        if (callFrom == 1) {
            orderBy = cat_id == 0 ? "mrp" : "x";
            callProductWall();
        } else if (callFrom == 2) {
            orderBy = "mrp";
            callProductWall();
        }
        dataBinding.tvSort.setOnClickListener(this);
        dataBinding.tvFilter.setOnClickListener(this);

        dataBinding.rvProductWall.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                int lastvisibleitemposition = isProductViewAsList ? mLinearLayoutManager.findLastVisibleItemPosition() : mGridLayoutManager.findLastVisibleItemPosition();
                if (mAdapter != null && lastvisibleitemposition == mAdapter.getItemCount() - 1) {
                    if (!isCalling && productWallList.size() > 0) {
                        if (tempList.size() >= recordsInOnePage) {
                            pageNumber++;
                            callProductWall();
                        }
                    }
                }
            }
        });
    }

    public void setAdapter() {
        if (productWallList.size() == 0) {
            dataBinding.noResult.getRoot().setVisibility(View.VISIBLE);
            dataBinding.noResult.tvNoResult.setText(getString(R.string.mdg_no_product_available));
            dataBinding.rvProductWall.setVisibility(View.GONE);
        } else {
            dataBinding.noResult.getRoot().setVisibility(View.GONE);
            dataBinding.rvProductWall.setVisibility(View.VISIBLE);
            if (mAdapter == null) {
                mAdapter = new ProductListAdapter(activity, productWallList, this, 1);

                mLinearLayoutManager = new LinearLayoutManager(activity);
                mGridLayoutManager = new GridLayoutManager(activity, 2);

                dataBinding.rvProductWall.setLayoutManager(mLinearLayoutManager);
                dataBinding.rvProductWall.setItemAnimator(new DefaultItemAnimator());
                dataBinding.rvProductWall.setAdapter(mAdapter);
                dataBinding.rvProductWall.addItemDecoration(new RecyclerView.ItemDecoration() {
                    @Override
                    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                        super.getItemOffsets(outRect, view, parent, state);
                        outRect.set(5, 10, 5, 0);
                    }
                });
            } else {
                mAdapter.notifyDataSetChanged();
            }
        }
    }

    public boolean isProductViewAsList = true;

    private void changeLayout() {
        if (mAdapter != null && productWallList.size() > 0) {
            isProductViewAsList = !isProductViewAsList;
            if (isProductViewAsList) {
                dataBinding.tvFilter.setText("Grid");
                dataBinding.tvFilter.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.product_grid_view, 0, 0, 0);
            } else {
                dataBinding.tvFilter.setText("List");
                dataBinding.tvFilter.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.product_list_view, 0, 0, 0);
            }

            mAdapter.isProductViewAsList(isProductViewAsList);
            dataBinding.rvProductWall.setLayoutManager(isProductViewAsList ? mLinearLayoutManager : mGridLayoutManager);
            dataBinding.rvProductWall.setAdapter(mAdapter);
        }
    }

    private void callProductWall() {
        try {
            isCalling = true;
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("category_id", cat_id);
            jsonObject.put("order_by", orderBy);
            jsonObject.put("order_type", sortType);
            jsonObject.put("page_number", pageNumber);
            if (callFrom == 1) {
                jsonObject.put("product_name", searchKey);
                activity.callWb(activity, Constants.PRODUCT_WALL_URL, Constants.POST, MethodName.PRODUCT_WALL, jsonObject, pageNumber > 1, false, this, null);
            } else {
                activity.callWb(activity, Constants.GET_PRODUCT_BY_CATEGORIES, Constants.POST, MethodName.GET_PRODUCT_BY_CATEGORY, jsonObject, pageNumber > 1, false, this, null);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResponse(String response, String methodName, int responseCode) {
        super.onResponse(response, methodName, responseCode);
        try {
            isCalling = false;
            JSONObject jsonObject = new JSONObject(response);
            if (jsonObject.getBoolean(Constants.SUCCESS_KEY)) {
                if (methodName.equals(MethodName.PRODUCT_WALL) || methodName.equals(MethodName.GET_PRODUCT_BY_CATEGORY)) {
                    JSONObject bodyJson = jsonObject.getJSONObject("data");
                    recordsInOnePage = bodyJson.getInt("records_limit");

                    ArrayList<ProductModel> tempLIst2 = new Gson().fromJson(bodyJson.getJSONArray("products").toString(), new TypeToken<ArrayList<ProductModel>>() {
                    }.getType());
                    if (pageNumber <= 1) {
                        productWallList.clear();
                    }
                    tempList.clear();
                    tempList.addAll(tempLIst2);

                    productWallList.addAll(tempList);
                    setAdapter();
                    cartCount = bodyJson.getInt("cart_count");
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
        super.onResponseFail(methodName, responseCode);
    }

    @Override
    public void onItemClicked(int position, View v, int type) {
        if (activity.isOffline(activity)) {
            activity.showSnackbar(getString(R.string.warn_no_internet), dataBinding.getRoot(), Snackbar.LENGTH_SHORT);
            return;
        }
        activity.setFragment(ProductDetailsFragment.getInstance(productWallList.get(position).getId()), R.id.framLay, true, true, null);
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
            case R.id.tvSort:
                final BottomSheetDialog mBottomSheetDialog = new BottomSheetDialog(activity);
                BottomSheetDialogSortBinding sortBinding = DataBindingUtil.inflate(activity.getLayoutInflater(), R.layout.bottom_sheet_dialog_sort, null, false);
                mBottomSheetDialog.setContentView(sortBinding.getRoot());
                mBottomSheetDialog.show();
                if (sortType.equals(Constants.SORT_TYPE_PRICE_ACS)) {
                    sortBinding.rbSortASC.setChecked(true);
                } else {
                    sortBinding.rbSortDESC.setChecked(true);
                }
                sortBinding.ivClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mBottomSheetDialog.dismiss();
                    }
                });

                sortBinding.rbSortDESC.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sortType = Constants.SORT_TYPE_PRICE_DESC;
                        pageNumber = 1;
                        mBottomSheetDialog.dismiss();
                        orderBy = "mrp";
                        callProductWall();
                    }
                });
                sortBinding.rbSortASC.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sortType = Constants.SORT_TYPE_PRICE_ACS;
                        pageNumber = 1;
                        mBottomSheetDialog.dismiss();
                        orderBy = "mrp";
                        callProductWall();
                    }
                });
                break;
            case R.id.tvFilter:
                changeLayout();
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 11) {
            callProductWall();
        }
    }
}
