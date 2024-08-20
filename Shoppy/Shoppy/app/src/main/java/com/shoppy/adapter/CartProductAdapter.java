package com.shoppy.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import com.google.android.material.snackbar.Snackbar;
import com.shoppy.R;
import com.shoppy.activity.HomeActivity;
import com.shoppy.databinding.ItemCartProductBinding;
import com.shoppy.global.Pref;
import com.shoppy.listener.ItemOnClickListener;
import com.shoppy.model.CartProductsItem;

import java.util.ArrayList;

public class CartProductAdapter extends RecyclerView.Adapter<CartProductAdapter.MyViewHolder> {
    private HomeActivity activity;
    private ArrayList<CartProductsItem> productList;
    private ItemOnClickListener itemOnClickListener;
    private int callFrom; // 1 for cart , 2 for Confirm Order

    public CartProductAdapter(HomeActivity activity, ArrayList<CartProductsItem> productList, ItemOnClickListener itemOnClickListener, int callFrom) {
        this.activity = activity;
        this.productList = productList;
        this.itemOnClickListener = itemOnClickListener;
        this.callFrom = callFrom;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.item_cart_product, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        //Common Data
        holder.dataBinding.tvProductName.setText(productList.get(position).getName());
        holder.dataBinding.tvAttributes.setText(productList.get(position).getEnumValue());
        holder.dataBinding.tvPrice.setText(activity.getString(R.string.rupee_symbol) + productList.get(position).getSellingPrice() + "");
        holder.dataBinding.tvQuantity.setText(productList.get(position).getQuantity() + "");
        holder.dataBinding.tvProductTotalPrice.setText(activity.getString(R.string.rupee_symbol) + activity.getFormated(productList.get(position).getQuantity() * productList.get(position).getSellingPrice()) + "");
        Glide.with(activity)
                .load(R.drawable.tshirt_image_test)
                .apply(new RequestOptions().centerInside().error(R.drawable.no_image))
                .into(holder.dataBinding.ivProductImage);

        holder.dataBinding.ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemOnClickListener.onItemClicked(position, v, 2);//type 2 for delete product
            }
        });

        // In Stock Product
        if (productList.get(position).getAvailableStock() != 0) {
            holder.dataBinding.getRoot().setAlpha(1f);
            holder.dataBinding.tvOutOfStock.setVisibility(View.GONE);
            holder.dataBinding.tvMinusQnty.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (productList.get(position).getQuantity() > 1) {
                        productList.get(position).setQuantity(productList.get(position).getQuantity() - 1);
                        notifyDataSetChanged();
                        itemOnClickListener.onItemClicked(position, v, 0);//type 0 for Minus
                    }
                }
            });
            holder.dataBinding.tvPlusQnty.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (productList.get(position).getQuantity() < 10) {
                        productList.get(position).setQuantity(productList.get(position).getQuantity() + 1);
                        notifyDataSetChanged();
                        itemOnClickListener.onItemClicked(position,v, 1);//type 1 for Minus
                    } else {
                        activity.showSnackbar("You can not order more then " + 10+ " products", holder.dataBinding.getRoot(), Snackbar.LENGTH_SHORT);
                    }
                }
            });



        } else {        // Out of Stock Product
            holder.dataBinding.ivProductImage.setAlpha(0.5f);
            holder.dataBinding.tvOutOfStock.setVisibility(View.VISIBLE);
            holder.dataBinding.tvMinusQnty.setOnClickListener(null);
            holder.dataBinding.tvPlusQnty.setOnClickListener(null);
        }

        if (productList.get(position).getDiscountPrice() > 0) {
            holder.dataBinding.tvDiscount.setVisibility(View.VISIBLE);
            holder.dataBinding.tvDiscount.setText(activity.getString(R.string.rupee_symbol) + productList.get(position).getDiscountPrice() + " OFF");
        } else {
            holder.dataBinding.tvDiscount.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ItemCartProductBinding dataBinding;

        public MyViewHolder(View itemView) {
            super(itemView);
            dataBinding = DataBindingUtil.bind(itemView);
        }
    }
}
