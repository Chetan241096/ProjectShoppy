package com.shoppy.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;


import com.shoppy.R;
import com.shoppy.databinding.ItemSearchListBinding;
import com.shoppy.listener.ItemOnClickListener;
import com.shoppy.model.SearchProductModel;

import java.util.ArrayList;

public class SearchListAdapter extends RecyclerView.Adapter<SearchListAdapter.MyViewHolder> {
    private ArrayList<SearchProductModel> searchProductList;
    private ItemOnClickListener itemOnClickListener;

    public SearchListAdapter(ArrayList<SearchProductModel> searchProductList, ItemOnClickListener itemOnClickListener) {
        this.searchProductList = searchProductList;
        this.itemOnClickListener = itemOnClickListener;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search_list, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        holder.dataBinding.tvSearchName.setText(searchProductList.get(position).getProductName());
        holder.dataBinding.tvCategoryName.setText("in " + searchProductList.get(position).getCategoryName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemOnClickListener.onItemClicked(position, v, 0);
            }
        });
    }

    @Override
    public int getItemCount() {
        return searchProductList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ItemSearchListBinding dataBinding;

        public MyViewHolder(View itemView) {
            super(itemView);
            dataBinding = DataBindingUtil.bind(itemView);
        }
    }
}
