package com.shoppy.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.viewpager.widget.ViewPager;


import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shoppy.R;
import com.shoppy.activity.HomeActivity;
import com.shoppy.adapter.ViewPagerFragmentAdapter;
import com.shoppy.baseclass.BaseFragment;
import com.shoppy.databinding.FragmentAllCategoryBinding;
import com.shoppy.global.Constants;
import com.shoppy.listener.ItemOnClickListenerChild;
import com.shoppy.model.CategoryModel;
import com.shoppy.retrofit.MethodName;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class AllCategoryFragment extends BaseFragment implements View.OnClickListener {
    private static final String CATEGORY_ID = "CATEGORY_ID";
    private HomeActivity activity;
    private FragmentAllCategoryBinding dataBinding;
    private ArrayList<CategoryModel> categoriesList;
    private ViewPagerFragmentAdapter adapter;
    private int categoryId;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_all_category, container, false);
        return dataBinding.getRoot();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (HomeActivity) context;
    }

    public static AllCategoryFragment getInstance(int categoryId) {
        Bundle bundle = new Bundle();
        bundle.putInt(CATEGORY_ID, categoryId);
        AllCategoryFragment fragment = new AllCategoryFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        dataBinding.actionBar.ivBack.setVisibility(View.GONE);
        dataBinding.actionBar.ivDrawerMenu.setVisibility(View.VISIBLE);
        dataBinding.actionBar.ivDrawerMenu.setOnClickListener(this);
        dataBinding.actionBar.tvTitle.setText(getString(R.string.title_categories));
        categoryId = getArguments().getInt(CATEGORY_ID);
        getCategoryList();
    }

    private void getCategoryList() {
        activity.callWb(activity, Constants.GET_ALL_SUB_CATEGORIES_URL, Constants.GET, MethodName.GET_USER_CATEGORIES, new JSONObject(), true, false, this, null);
    }

    @Override
    public void onResponse(String response, String methodName, int responseCode) {
        super.onResponse(response, methodName, responseCode);
        try {
            JSONObject jsonObject = new JSONObject(response);
            if (jsonObject.getBoolean(Constants.SUCCESS_KEY)) {
                categoriesList = new Gson().fromJson(jsonObject.getJSONArray("data").toString(), new TypeToken<ArrayList<CategoryModel>>() {
                }.getType());
                setViewPagerAdapter();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void setViewPagerAdapter() {
        adapter = new ViewPagerFragmentAdapter(getChildFragmentManager());
        int selectedTab = 0;
        for (int i = 0; i < categoriesList.size(); i++) {
            CategoryModel categoryModel = categoriesList.get(i);
            if (categoryId == categoryModel.getId()) {
                selectedTab = i;
            }
            adapter.addFragment(ProductListFragment.getInstance(categoryModel.getId(), categoryModel.getCategoryName()), categoryModel.getCategoryName());
        }

        dataBinding.viewPager.setAdapter(adapter);
        dataBinding.viewPager.setCurrentItem(selectedTab);
//        dataBinding.viewPager.setOffscreenPageLimit(0);

        dataBinding.tabLayout.setupWithViewPager(dataBinding.viewPager);

//        dataBinding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
//            @Override
//            public void onTabSelected(TabLayout.Tab tab) {
//                dataBinding.viewPager.setCurrentItem(tab.getPosition());
////                ((ProductListFragment) adapter.getItem(position)).initFragment();
//            }
//
//            @Override
//            public void onTabUnselected(TabLayout.Tab tab) {
//
//            }
//
//            @Override
//            public void onTabReselected(TabLayout.Tab tab) {
//
//            }
//        });

        dataBinding.viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                ((ProductListFragment) adapter.getItem(position)).initFragment();

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
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


}

