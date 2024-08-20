package com.shoppy.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.shoppy.R;
import com.shoppy.baseclass.BaseActivity;
import com.shoppy.databinding.ItemHomeCategoriesBinding;
import com.shoppy.listener.ItemOnClickListener;
import com.shoppy.model.CategoryModel;

import java.util.ArrayList;

public class HomeCategoriesAdapter extends RecyclerView.Adapter<HomeCategoriesAdapter.MyViewHolder> {
    private BaseActivity activity;
    private ItemOnClickListener itemOnClickListener;
    private ArrayList<CategoryModel> categoryList;

    public HomeCategoriesAdapter(BaseActivity activity, ItemOnClickListener itemOnClickListener, ArrayList<CategoryModel> categoryList) {
        this.activity = activity;
        this.itemOnClickListener = itemOnClickListener;
        this.categoryList = categoryList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.item_home_categories, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        Glide.with(activity)
                .load(categoryList.get(position).getCateImage())
                .apply(new RequestOptions().placeholder(R.drawable.tshirt).error(R.drawable.tshirt))
                .into(holder.dataBinding.ivCateImage);

        holder.dataBinding.tvCategoryName.setText(categoryList.get(position).getCategoryName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemOnClickListener.onItemClicked(position,view,1);
            }
        });
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ItemHomeCategoriesBinding dataBinding;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            dataBinding = DataBindingUtil.bind(itemView);
        }
    }
}
