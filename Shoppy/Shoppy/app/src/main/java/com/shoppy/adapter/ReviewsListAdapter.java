package com.shoppy.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;


import com.shoppy.R;
import com.shoppy.activity.HomeActivity;
import com.shoppy.databinding.ItemProductReviewBinding;
import com.shoppy.listener.ItemOnClickListener;
import com.shoppy.model.GeneralReviewModel;

import java.util.ArrayList;

public class ReviewsListAdapter extends RecyclerView.Adapter<ReviewsListAdapter.MyViewHolder> {
    private HomeActivity activity;
    private ArrayList<GeneralReviewModel> reviewsList;
    private ItemOnClickListener itemOnClickListener;
    private int callFrom;

    public ReviewsListAdapter(HomeActivity activity, ArrayList<GeneralReviewModel> reviewsList, ItemOnClickListener itemOnClickListener,int callFrom) {
        this.activity = activity;
        this.reviewsList = reviewsList;
        this.itemOnClickListener = itemOnClickListener;
        this.callFrom = callFrom;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.item_product_review, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        holder.dataBinding.rating.setRating(reviewsList.get(position).getRating());
        holder.dataBinding.tvReview.setText(reviewsList.get(position).getReview());
        holder.dataBinding.tvUserName.setText(callFrom == 1 ? reviewsList.get(position).getFullName() : reviewsList.get(position).getProductName());
        holder.dataBinding.tvReviewDate.setText(reviewsList.get(position).getReviewDate());
    }

    @Override
    public int getItemCount() {
        return reviewsList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ItemProductReviewBinding dataBinding;

        public MyViewHolder(View itemView) {
            super(itemView);
            dataBinding = DataBindingUtil.bind(itemView);
        }
    }
}
