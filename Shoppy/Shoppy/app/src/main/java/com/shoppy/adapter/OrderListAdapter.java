package com.shoppy.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;


import com.shoppy.R;
import com.shoppy.activity.HomeActivity;
import com.shoppy.databinding.ItemOrdersLayoutBinding;
import com.shoppy.listener.ItemOnClickListener;
import com.shoppy.model.OrderListModel;

import java.util.ArrayList;

public class OrderListAdapter extends RecyclerView.Adapter<OrderListAdapter.MyViewHolder> {
    private HomeActivity activity;
    private ArrayList<OrderListModel> orderList;
    private ItemOnClickListener itemOnClickListener;

    public OrderListAdapter(HomeActivity activity, ArrayList<OrderListModel> orderList, ItemOnClickListener itemOnClickListener) {
        this.activity = activity;
        this.orderList = orderList;
        this.itemOnClickListener = itemOnClickListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.item_orders_layout, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        holder.dataBinding.tvOrderNumber.setText(orderList.get(position).getOrderNumber());
        holder.dataBinding.tvOrderDate.setText(orderList.get(position).getOrderDate());
        holder.dataBinding.tvTotal.setText(activity.getString(R.string.rupee_symbol) + orderList.get(position).getOrderTotal() + "");
        holder.dataBinding.tvTotalProducts.setText("x " + orderList.get(position).getNoOfProducts());
        holder.dataBinding.tvOrderStatus.setText(activity.getOrderStatus(orderList.get(position).getOrderStatusId()));
        if (orderList.get(position).getOrderStatusId() == 1 || orderList.get(position).getOrderStatusId() == 2 || orderList.get(position).getOrderStatusId() == 3) {
//            holder.dataBinding.tvOrderStatus.setBackground(ContextCompat.getDrawable(activity, R.drawable.gradiant_orange_round_corner));
        } else if (orderList.get(position).getOrderStatusId() == 4) {
//            holder.dataBinding.tvOrderStatus.setBackground(ContextCompat.getDrawable(activity, R.drawable.gradiant_green_round_corner));
        } else if (orderList.get(position).getOrderStatusId() == 5) {
//            holder.dataBinding.tvOrderStatus.setBackground(ContextCompat.getDrawable(activity, R.drawable.gradiant_red_round_corner));
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemOnClickListener.onItemClicked(position, v, 0);
            }
        });

    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ItemOrdersLayoutBinding dataBinding;

        public MyViewHolder(View itemView) {
            super(itemView);
            dataBinding = DataBindingUtil.bind(itemView);
        }
    }
}
