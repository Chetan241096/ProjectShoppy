package com.shoppy.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.shoppy.R;
import com.shoppy.baseclass.BaseActivity;
import com.shoppy.databinding.ItemDrawerMenuBinding;
import com.shoppy.listener.ItemOnClickListener;
import com.shoppy.model.DrawerItem;

import java.util.ArrayList;

public class DrawerMenuRecyclerAdapter extends RecyclerView.Adapter<DrawerMenuRecyclerAdapter.MyViewHolder> {
    private BaseActivity activity;
    private ItemOnClickListener itemOnClickListener;
    private ArrayList<DrawerItem> drawerItems;
    private int lastSelectedItem = 0;


    public DrawerMenuRecyclerAdapter(BaseActivity activity, ItemOnClickListener itemOnClickListener, ArrayList<DrawerItem> drawerItems) {
        this.activity = activity;
        this.itemOnClickListener = itemOnClickListener;
        this.drawerItems = drawerItems;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.item_drawer_menu, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        holder.dataBinding.tvMenuName.setText(drawerItems.get(position).getTitle());
        //holder.dataBinding.ivIcon.setImageDrawable(ContextCompat.getDrawable(activity, drawerItems.get(position).getImgResID()));
        if (drawerItems.get(position).isSelected()) {
            lastSelectedItem = position;
            holder.dataBinding.tvMenuName.setTextColor(ContextCompat.getColor(activity, R.color.colorPrimary));
        } else {
            holder.dataBinding.tvMenuName.setTextColor(ContextCompat.getColor(activity, R.color.black));
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (position != lastSelectedItem) {
                    drawerItems.get(position).setSelected(true);
                    drawerItems.get(lastSelectedItem).setSelected(false);
                    lastSelectedItem = position;
                }
                itemOnClickListener.onItemClicked(position, v, 0);
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return drawerItems.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ItemDrawerMenuBinding dataBinding;

        public MyViewHolder(View itemView) {
            super(itemView);
            dataBinding = DataBindingUtil.bind(itemView);
        }
    }
}
