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
import com.shoppy.activity.HomeActivity;
import com.shoppy.databinding.ItemOrderdetailsProductBinding;
import com.shoppy.global.Constants;
import com.shoppy.global.Pref;
import com.shoppy.listener.ItemOnClickListener;
import com.shoppy.model.OrderDetailsProductItem;


import java.util.ArrayList;

public class OrderDetailsProductListAdapter extends RecyclerView.Adapter<OrderDetailsProductListAdapter.MyViewHolder> {
    private HomeActivity activity;
    private ArrayList<OrderDetailsProductItem> productList;
    private ItemOnClickListener itemOnClickListener;
    private int orderStatus;

    public OrderDetailsProductListAdapter(HomeActivity activity, ArrayList<OrderDetailsProductItem> productList, ItemOnClickListener itemOnClickListener, int orderStatus) {
        this.activity = activity;
        this.productList = productList;
        this.itemOnClickListener = itemOnClickListener;
        this.orderStatus = orderStatus;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.item_orderdetails_product, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        holder.dataBinding.tvProductName.setText(productList.get(position).getProductName());
        holder.dataBinding.tvAttributes.setText(productList.get(position).getAttributeName() + " : " + productList.get(position).getAttributeValue());
        holder.dataBinding.tvPrice.setText(activity.getString(R.string.rupee_symbol) + productList.get(position).getSellingPrice() + "");
        holder.dataBinding.tvQuantity.setText(" X " + productList.get(position).getQuantity() + "");
        holder.dataBinding.tvProductTotalPrice.setText(productList.get(position).getQuantity() * productList.get(position).getSellingPrice() + "");

        Glide.with(activity)
                .load(Pref.getImageUrl(activity) + productList.get(position).getImageUrl())
                .apply(new RequestOptions().error(R.drawable.no_image))
                .into(holder.dataBinding.ivProductImage);

        if (activity.getOrderStatus(orderStatus).equals(Constants.ORDER_COMPLETED) && !productList.get(position).isProductReviewed()) {
            holder.dataBinding.tvAddReview.setVisibility(View.VISIBLE);
            holder.dataBinding.tvAddReview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemOnClickListener.onItemClicked(position, v, 1); // type 1 for review
                }
            });


        } else {
            holder.dataBinding.tvAddReview.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ItemOrderdetailsProductBinding dataBinding;

        public MyViewHolder(View itemView) {
            super(itemView);
            dataBinding = DataBindingUtil.bind(itemView);
        }
    }
}
