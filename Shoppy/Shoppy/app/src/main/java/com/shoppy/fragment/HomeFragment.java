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
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.shoppy.R;
import com.shoppy.activity.HomeActivity;
import com.shoppy.adapter.HomeCategoriesAdapter;
import com.shoppy.adapter.ProductListAdapter;
import com.shoppy.baseclass.BaseFragment;
import com.shoppy.databinding.FragmentHomeBinding;
import com.shoppy.global.Constants;
import com.shoppy.listener.ItemOnClickListener;
import com.shoppy.model.HomeModel;
import com.shoppy.retrofit.MethodName;

import org.json.JSONObject;

public class HomeFragment extends BaseFragment implements ItemOnClickListener, View.OnClickListener {

    private HomeActivity activity;
    private FragmentHomeBinding dataBinding;
    private HomeModel homeModel;
    private ProductListAdapter topRatingAdapter;
    private ProductListAdapter topSellingAdapter;
    private HomeCategoriesAdapter categoryAdapter;

    public static HomeFragment getInstance() {
        Bundle bundle = new Bundle();
        HomeFragment fragment = new HomeFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);
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
        dataBinding.tvSearch.setOnClickListener(this);
        dataBinding.ivMenu.setOnClickListener(this);
        dataBinding.ivCart.setOnClickListener(this);
        getHomeDetails();
//        setDetails();
    }

    private void getHomeDetails() {
        activity.callWb(activity, Constants.GET_USER_HOME, Constants.GET, MethodName.GET_HOME, new JSONObject(), true, false, this, null);
    }

    private void setDetails() {
        if (homeModel != null) {
            if (categoryAdapter == null) {
                categoryAdapter = new HomeCategoriesAdapter(activity, this, homeModel.getCategories());
                dataBinding.rvHomeCategory.setItemAnimator(new DefaultItemAnimator());
                dataBinding.rvHomeCategory.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false));
                dataBinding.rvHomeCategory.setAdapter(categoryAdapter);
            } else {
                categoryAdapter.notifyDataSetChanged();
            }

            if (topSellingAdapter == null) {
                topSellingAdapter = new ProductListAdapter(activity, homeModel.getHighestSelling(), this, 2);
                topSellingAdapter.isProductViewAsList(false);
                dataBinding.rvTopSelling.setItemAnimator(new DefaultItemAnimator());
                dataBinding.rvTopSelling.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false));
                dataBinding.rvTopSelling.setAdapter(topSellingAdapter);
                dataBinding.rvTopSelling.addItemDecoration(new RecyclerView.ItemDecoration() {
                    @Override
                    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                        super.getItemOffsets(outRect, view, parent, state);
                        outRect.set(10, 0, 0, 0);
                    }
                });
            } else {
                topSellingAdapter.notifyDataSetChanged();
            }


            if (topRatingAdapter == null) {
                topRatingAdapter = new ProductListAdapter(activity, homeModel.getHighestRating(), this, 3);
                topRatingAdapter.isProductViewAsList(false);
                dataBinding.rvTopRating.setItemAnimator(new DefaultItemAnimator());
                dataBinding.rvTopRating.setLayoutManager(new GridLayoutManager(activity, 2));
                dataBinding.rvTopRating.setAdapter(topRatingAdapter);
                dataBinding.rvTopRating.addItemDecoration(new RecyclerView.ItemDecoration() {
                    @Override
                    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                        super.getItemOffsets(outRect, view, parent, state);
                        outRect.set(10, 0, 0, 10);
                    }
                });
            } else {
                topRatingAdapter.notifyDataSetChanged();
            }
            Glide.with(activity)
                    .load(R.drawable.home_main_image)
//                    .apply(new RequestOptions().error(R.drawable.home_main_image).placeholder(R.drawable.home_main_image))
                    .into(dataBinding.ivMainOffer);
        }
    }

    @Override
    public void onItemClicked(int position, View v, int type) {
        switch (type) {
            case 1:
                activity.setFragment(ProductWallFragment.getInstance(homeModel.getCategories().get(position).getCategoryName(), homeModel.getCategories().get(position).getId(), 2), R.id.framLay, true, true, null);
                break;
            case 2:
                activity.setFragment(ProductDetailsFragment.getInstance(homeModel.getHighestSelling().get(position).getId()), R.id.framLay, true, true, null);
                break;
            case 3:
                activity.setFragment(ProductDetailsFragment.getInstance(homeModel.getHighestRating().get(position).getId()), R.id.framLay, true, true, null);
                break;
        }
    }

    @Override
    public void onResponse(String response, String methodName, int responseCode) {
        super.onResponse(response, methodName, responseCode);
        try {
            JSONObject jsonObject = new JSONObject(response);
            if (jsonObject.getBoolean(Constants.SUCCESS_KEY) && methodName.equals(MethodName.GET_HOME)) {
                homeModel = new Gson().fromJson(jsonObject.getJSONObject("data").toString(), HomeModel.class);
                setDetails();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvSearch:
                activity.setFragment(new SearchFragment(), R.id.framLay, true, true, null);
                break;
            case R.id.ivMenu:
                activity.openCloseDrawer();
                break;
            case R.id.ivCart:
                activity.setFragment(MyCartFragment.getInstance(1, 0), R.id.framLay, true, true, null);
                break;
        }
    }
}
