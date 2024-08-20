package com.shoppy.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.shoppy.R;
import com.shoppy.baseclass.BaseActivity;
import com.shoppy.baseclass.BaseFragment;
import com.shoppy.databinding.FragmentOrderPlacedBinding;


public class OrderPlacedFragment extends BaseFragment {
    private static final String CALL_FROM = "CALL_FROM";
    private BaseActivity activity;
    private FragmentOrderPlacedBinding dataBinding;


    public static OrderPlacedFragment getInstance(int callFrom) {
        Bundle bundle = new Bundle();
        bundle.putInt(CALL_FROM, callFrom); // 1 for Normal Order, 2 for Daily needs
        OrderPlacedFragment fragment = new OrderPlacedFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_order_placed, container, false);
        return dataBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        dataBinding.checkView.check();
        int callFrom = getArguments().getInt(CALL_FROM);
        dataBinding.btnOrders.setText("Go To Orders");
        dataBinding.tvOrderText.setText(getString(R.string.text_order_placed));
        dataBinding.btnOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.popAllFragment();
                activity.setFragment(new MyOrderFragment(), R.id.framLay, false, false, null);
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (BaseActivity) context;
    }
}
