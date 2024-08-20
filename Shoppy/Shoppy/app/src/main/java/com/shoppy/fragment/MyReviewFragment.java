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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shoppy.R;
import com.shoppy.activity.HomeActivity;
import com.shoppy.adapter.ReviewsListAdapter;
import com.shoppy.baseclass.BaseFragment;
import com.shoppy.databinding.FragmentMyReviewsBinding;
import com.shoppy.global.Constants;
import com.shoppy.listener.ItemOnClickListener;
import com.shoppy.model.GeneralReviewModel;
import com.shoppy.retrofit.MethodName;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MyReviewFragment extends BaseFragment implements ItemOnClickListener, View.OnClickListener {
    private static final String REVIEWSLIST = "REVIEWLIST";
    private FragmentMyReviewsBinding dataBinding;
    private HomeActivity activity;
    private ArrayList<GeneralReviewModel> reviewList = new ArrayList<>();
    private ReviewsListAdapter mAdapter;


    public static MyReviewFragment newInstance(ArrayList<GeneralReviewModel> productReview) {
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(REVIEWSLIST, productReview);
        MyReviewFragment fragment = new MyReviewFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_my_reviews, container, false);
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
        dataBinding.actionBar.ivBack.setVisibility(View.VISIBLE);
        dataBinding.actionBar.ivDrawerMenu.setVisibility(View.GONE);
        dataBinding.actionBar.tvTitle.setText("My Reviews");
        dataBinding.actionBar.ivBack.setOnClickListener(this);
        getReviews();
    }

    private void getReviews() {
        activity.callWb(activity, Constants.GET_REVIEWS, Constants.GET, MethodName.GET_USER_REVIEW, new JSONObject(), true, false, this, null);
    }

    @Override
    public void onResponse(String response, String methodName, int responseCode) {
        super.onResponse(response, methodName, responseCode);
        try {
            JSONObject jsonObject = new JSONObject(response);
            if (jsonObject.getBoolean(Constants.SUCCESS_KEY)) {
                ArrayList<GeneralReviewModel> temp = new Gson().fromJson(jsonObject.getJSONArray("data").toString(), new TypeToken<ArrayList<GeneralReviewModel>>() {
                }.getType());
                reviewList.clear();
                reviewList.addAll(temp);
                setAdapter();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void setAdapter() {
        if (reviewList.size() == 0) {
            dataBinding.noResult.getRoot().setVisibility(View.VISIBLE);
            dataBinding.rvReviews.setVisibility(View.GONE);
            dataBinding.noResult.tvNoResult.setText(getString(R.string.msg_no_review_for_product));
        } else {
            dataBinding.noResult.getRoot().setVisibility(View.GONE);
            dataBinding.rvReviews.setVisibility(View.VISIBLE);
            if (mAdapter == null) {
                mAdapter = new ReviewsListAdapter(activity, reviewList, this,2);
                dataBinding.rvReviews.setLayoutManager(new LinearLayoutManager(activity));
                dataBinding.rvReviews.setItemAnimator(new DefaultItemAnimator());
                dataBinding.rvReviews.addItemDecoration(new RecyclerView.ItemDecoration() {
                    @Override
                    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                        super.getItemOffsets(outRect, view, parent, state);
                        outRect.set(20, 20, 20, 0);
                    }
                });
                dataBinding.rvReviews.setAdapter(mAdapter);
            } else {
                mAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onItemClicked(int position, View v, int type) {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivBack:
                activity.onBackPressed();
                break;
        }
    }
}
