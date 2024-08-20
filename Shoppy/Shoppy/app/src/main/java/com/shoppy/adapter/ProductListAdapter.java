package com.shoppy.adapter;

import android.graphics.Paint;
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
import com.shoppy.databinding.GeneralProductLayoutBinding;
import com.shoppy.databinding.GeneralProductVerticalLayoutBinding;
import com.shoppy.global.Pref;
import com.shoppy.listener.ItemOnClickListener;
import com.shoppy.model.ProductModel;


import java.util.ArrayList;

public class ProductListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<ProductModel> productsList;
    private ItemOnClickListener itemOnClickListener;
    private HomeActivity activity;
    private boolean isProductViewAsList = true;
    private int callFrom; //2 for top selling, 3 for top rating


    public ProductListAdapter(HomeActivity activity, ArrayList<ProductModel> productsList, ItemOnClickListener itemOnClickListener, int callFrom) {
        this.productsList = productsList;
        this.itemOnClickListener = itemOnClickListener;
        this.activity = activity;
        this.callFrom = callFrom;
    }


    public void isProductViewAsList(boolean isProductViewAsList) {
        this.isProductViewAsList = isProductViewAsList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (isProductViewAsList) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.general_product_layout, parent, false);
            return new MyViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.general_product_vertical_layout, parent, false);
            return new MyVerticalHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        if (isProductViewAsList) {
            MyViewHolder viewHolder = (MyViewHolder) holder;
            viewHolder.dataBinding.tvTitle.setText(productsList.get(position).getName());
            viewHolder.dataBinding.ratingbar.setRating((float) productsList.get(position).getRating());
            viewHolder.dataBinding.tvAttributesValue.setText(productsList.get(position).getEnumValue());
            if (productsList.get(position).getDiscountPrice() != 0) {
                viewHolder.dataBinding.tvDiscount.setVisibility(View.VISIBLE);
                viewHolder.dataBinding.tvCutPrice.setVisibility(View.VISIBLE);
                viewHolder.dataBinding.tvPrice.setText(activity.getString(R.string.rupee_symbol) + (productsList.get(position).getMrp() - productsList.get(position).getDiscountPrice()) + "");
                viewHolder.dataBinding.tvDiscount.setText(activity.getString(R.string.rupee_symbol) + productsList.get(position).getDiscountPrice() + " OFF");
                viewHolder.dataBinding.tvCutPrice.setPaintFlags(viewHolder.dataBinding.tvCutPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                viewHolder.dataBinding.tvCutPrice.setText(activity.getString(R.string.rupee_symbol) + productsList.get(position).getMrp() + "");
            } else {
                viewHolder.dataBinding.tvPrice.setText(activity.getString(R.string.rupee_symbol) + productsList.get(position).getMrp() + "");
                viewHolder.dataBinding.tvDiscount.setVisibility(View.GONE);
                viewHolder.dataBinding.tvCutPrice.setVisibility(View.GONE);
            }
            if (productsList.get(position).getRatingCount() == 0) {
                viewHolder.dataBinding.ratingCount.setText("(" + activity.getString(R.string.text_no_reviews) + ")");
            } else {
                viewHolder.dataBinding.ratingCount.setText("(" + productsList.get(position).getRatingCount() + ")");
            }

            viewHolder.dataBinding.ratingbar.setRating((float) productsList.get(position).getRating());

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemOnClickListener.onItemClicked(position, v, callFrom);
                }
            });
            Glide.with(activity)
                    .load(Pref.getImageUrl(activity) + productsList.get(position).getImageThumbnail())
                    .apply(new RequestOptions().centerInside().error(R.drawable.tshirt_image_test))
                    .into(viewHolder.dataBinding.ivProduct);

        } else {
            MyVerticalHolder viewHolder = (MyVerticalHolder) holder;
            viewHolder.dataBinding.tvTitle.setText(productsList.get(position).getName());
            viewHolder.dataBinding.tvPrice.setText(activity.getString(R.string.rupee_symbol) + (productsList.get(position).getMrp() - productsList.get(position).getDiscountPrice()) + "");
            viewHolder.dataBinding.ratingbar.setRating((float) productsList.get(position).getRating());
            viewHolder.dataBinding.tvAttributesValue.setText(productsList.get(position).getEnumValue());

            if (productsList.get(position).getDiscountPrice() != 0) {
                viewHolder.dataBinding.tvDiscount.setVisibility(View.VISIBLE);
                viewHolder.dataBinding.tvCutPrice.setVisibility(View.VISIBLE);

                viewHolder.dataBinding.tvDiscount.setText(activity.getString(R.string.rupee_symbol) + productsList.get(position).getDiscountPrice() + " OFF");
                viewHolder.dataBinding.tvCutPrice.setPaintFlags(viewHolder.dataBinding.tvCutPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                viewHolder.dataBinding.tvCutPrice.setText(activity.getString(R.string.rupee_symbol) + productsList.get(position).getMrp() + "");
            } else {
                viewHolder.dataBinding.tvDiscount.setVisibility(View.GONE);
                viewHolder.dataBinding.tvCutPrice.setVisibility(View.GONE);
            }
            if (productsList.get(position).getRatingCount() == 0) {
                viewHolder.dataBinding.ratingCount.setText("(" + activity.getString(R.string.text_no_reviews) + ")");
            } else {
                viewHolder.dataBinding.ratingCount.setText("(" + productsList.get(position).getRatingCount() + ")");
            }

            viewHolder.dataBinding.ratingbar.setRating((float) productsList.get(position).getRating());

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemOnClickListener.onItemClicked(position, v, callFrom);
                }
            });
            Glide.with(activity)
                    .load(Pref.getImageUrl(activity) + productsList.get(position).getImageThumbnail())
                    .apply(new RequestOptions().centerInside().error(R.drawable.tshirt_image_test))
                    .into(viewHolder.dataBinding.ivProduct);
        }

    }


    @Override
    public int getItemCount() {
        return productsList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        GeneralProductLayoutBinding dataBinding;

        public MyViewHolder(View itemView) {
            super(itemView);
            dataBinding = DataBindingUtil.bind(itemView);
        }
    }

    public class MyVerticalHolder extends RecyclerView.ViewHolder {
        GeneralProductVerticalLayoutBinding dataBinding;

        public MyVerticalHolder(View itemView) {
            super(itemView);
            dataBinding = DataBindingUtil.bind(itemView);
        }
    }
}