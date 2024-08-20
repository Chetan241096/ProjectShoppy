package com.shoppy.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import androidx.annotation.RequiresApi;
import androidx.databinding.DataBindingUtil;

import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.shoppy.R;
import com.shoppy.activity.HomeActivity;
import com.shoppy.adapter.CartProductAdapter;
import com.shoppy.baseclass.BaseFragment;
import com.shoppy.databinding.FragmentMyCartBinding;
import com.shoppy.global.Constants;
import com.shoppy.listener.ItemOnClickListener;
import com.shoppy.model.AddressModel;
import com.shoppy.model.CartProductsItem;
import com.shoppy.retrofit.MethodName;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;

public class MyCartFragment extends BaseFragment implements View.OnClickListener, ItemOnClickListener {
    private static final String CALLED_FROM = "CALLED_FROM";
    private static final int LOAD_PAYMENT_DATA_REQUEST_CODE = 125;
    private static final String PRODUCT_ID = "PRODUCT_ID";
    private HomeActivity activity;
    private FragmentMyCartBinding dataBinding;
    private CartProductAdapter mAdapter;
    private ArrayList<CartProductsItem> cartProductsItems = new ArrayList<>();
    private int lastSelectedShop = -1;
    private AddressModel addressModel = null;
    private int callFrom;


    public static MyCartFragment getInstance(int callFrom, int id) { //callFrom 1 = Menu, 2 for BuyNow
        Bundle bundle = new Bundle();
        bundle.putInt(CALLED_FROM, callFrom);
        bundle.putInt(PRODUCT_ID, id);
        MyCartFragment fragment = new MyCartFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_my_cart, container, false);
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
        dataBinding.actionBar.ivBack.setVisibility(View.GONE);
        dataBinding.actionBar.ivDrawerMenu.setVisibility(View.VISIBLE);
        dataBinding.actionBar.ivDrawerMenu.setOnClickListener(this);
        dataBinding.actionBar.tvTitle.setText(getString(R.string.title_my_cart));
        dataBinding.btnContinue.setOnClickListener(this);
        dataBinding.tvChangeText.setOnClickListener(this);
        callFrom = getArguments().getInt(CALLED_FROM);
        getUserCart();

    }


    private void getUserCart() {
        if (callFrom == 1) {
            activity.callWb(activity, Constants.CART_CRUD_URL, Constants.GET, MethodName.GET_USER_CART, new JSONObject(), false, false, this, null);
        } else if (callFrom == 2) {
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("product_id", getArguments().getInt(PRODUCT_ID));
                activity.callWb(activity, Constants.CALL_BUY_NOW, Constants.GET, MethodName.BUY_NOW_PRODUCT, new JSONObject(), false, false, this, null);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onResponse(String response, String methodName, int responseCode) {
        super.onResponse(response, methodName, responseCode);
        try {
            JSONObject jsonObject = new JSONObject(response);
            if (jsonObject.getBoolean(Constants.SUCCESS_KEY)) {
                if (methodName.equals(MethodName.GET_USER_CART)) {
                    ArrayList<CartProductsItem> tempList = new Gson().fromJson(jsonObject.getJSONObject("data").getJSONArray("cart_data").toString(), new TypeToken<ArrayList<CartProductsItem>>() {
                    }.getType());
                    cartProductsItems.clear();
                    cartProductsItems.addAll(tempList);
                    setAdapter();

                    addressModel = new Gson().fromJson(jsonObject.getJSONObject("data").getJSONObject("user_address").toString(), AddressModel.class);
                    setAddressDetails();
                } else if (methodName.equals(MethodName.DELETE_PRODUCT_FROM_CART)) {
                    ArrayList<CartProductsItem> tempList = new Gson().fromJson(jsonObject.getJSONObject("data").getJSONArray("cart_data").toString(), new TypeToken<ArrayList<CartProductsItem>>() {
                    }.getType());
                    cartProductsItems.clear();
                    cartProductsItems.addAll(tempList);
                    setAdapter();

                    addressModel = new Gson().fromJson(jsonObject.getJSONObject("data").getJSONObject("user_address").toString(), AddressModel.class);
                    setAddressDetails();
                } else if (methodName.equals(MethodName.UPDATE_QUANTITY_IN_CART)) {
                    activity.showSnackbar(jsonObject.getString(Constants.MESSAGE_KEY), dataBinding.getRoot(), Snackbar.LENGTH_SHORT);
                } else if (methodName.equals(MethodName.PLACE_ORDER)) {
                    activity.setFragment(OrderPlacedFragment.getInstance(1), R.id.framLay, true, true, null);
                }
            } else {
                activity.showSnackbar(jsonObject.getString(Constants.MESSAGE_KEY), dataBinding.getRoot(), Snackbar.LENGTH_SHORT);
                setAdapter();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setAddressDetails() {
        if (addressModel != null && addressModel.getId() != 0) {
            dataBinding.tvUserName.setText(addressModel.getUserName());
            dataBinding.tvAddress.setText(addressModel.getHouseNumber() + ", " + addressModel.getSocietyName() + ", " + addressModel.getLandmark() + ", " + addressModel.getCity() + ", " + addressModel.getState());
            dataBinding.tvChangeText.setText("Change Address");

        } else {
            dataBinding.tvUserName.setVisibility(View.GONE);
            dataBinding.tvChangeText.setText("Add Address");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        setTargetFragment(null, 0);

    }

    private void setAdapter() {
        if (cartProductsItems.size() == 0) {
            dataBinding.noResult.getRoot().setVisibility(View.VISIBLE);
            dataBinding.groupData.setVisibility(View.GONE);
            dataBinding.mainScroll.setVisibility(View.GONE);
            dataBinding.noResult.tvNoResult.setText(getString(R.string.msg_empty_cart));
        } else {
            dataBinding.noResult.getRoot().setVisibility(View.GONE);
            dataBinding.mainScroll.setVisibility(View.VISIBLE);
            dataBinding.groupData.setVisibility(View.VISIBLE);

            if (mAdapter == null) {
                Log.d("setAdapter", cartProductsItems.size() + "");
                mAdapter = new CartProductAdapter(activity, cartProductsItems, this, 1);
                dataBinding.rvCartList.setLayoutManager(new LinearLayoutManager(activity));
                dataBinding.rvCartList.setItemAnimator(new DefaultItemAnimator());
                dataBinding.rvCartList.setAdapter(mAdapter);
                dataBinding.rvCartList.addItemDecoration(new RecyclerView.ItemDecoration() {
                    @Override
                    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                        super.getItemOffsets(outRect, view, parent, state);
                        outRect.set(15, 15, 15, 0);
                    }
                });
            } else {
                mAdapter.notifyDataSetChanged();
            }
            dataBinding.rvCartList.post(new Runnable() {
                @Override
                public void run() {
                    updatePrice();
                }
            });
        }
    }

    private void updatePrice() {
        Log.d("UpdatePrice", cartProductsItems.size() + "");
        double subTotal = 0;
        double discount = 0;
        for (int i = 0; i < cartProductsItems.size(); i++) {
            if (cartProductsItems.get(i).getAvailableStock() != 0) {
                subTotal = subTotal + (cartProductsItems.get(i).getQuantity() * cartProductsItems.get(i).getSellingPrice());
                discount = discount + (cartProductsItems.get(i).getDiscountPrice() * cartProductsItems.get(i).getQuantity());
            }
        }

        dataBinding.tvSubtotal.setText(activity.getString(R.string.rupee_symbol) + activity.getFormated(subTotal));
        dataBinding.tvDelivery.setText(activity.getString(R.string.rupee_symbol) + 2.99);
        dataBinding.tvTotal.setText(activity.getString(R.string.rupee_symbol) + activity.getFormated(subTotal + 2.99));
        dataBinding.tvSavingTotal.setVisibility(discount > 0 ? View.VISIBLE : View.GONE);
        dataBinding.tvSavingTotal.setText(activity.getString(R.string.text_total_saving, activity.getFormated(discount)));
        mAdapter.notifyDataSetChanged();
    }


    private void deleteProductOrShop(int cart_id) {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id", cart_id);
            activity.callWb(activity, Constants.DELETE_FROM_CART, Constants.DELETE, MethodName.DELETE_PRODUCT_FROM_CART, jsonObject, true, false, this, null);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onClick(View v) {
        if (activity.isOffline(activity)) {
            activity.showSnackbar(getString(R.string.warn_no_internet), dataBinding.getRoot(), Snackbar.LENGTH_LONG);
            return;
        }
        switch (v.getId()) {
            case R.id.ivDrawerMenu:
                activity.openCloseDrawer();
                break;

            case R.id.btnContinue:
                if (addressModel == null || addressModel.getId() == 0) {
                    Toast.makeText(activity, "Please enter your address.", Toast.LENGTH_SHORT).show();
                } else if (checkOutofStock()) {
                    Toast.makeText(activity, "All Products in your cart are not in stock. Please wait until come back in stock", Toast.LENGTH_LONG).show();
                } else if (!dataBinding.rbCOD.isChecked() && !dataBinding.rbGooglePay.isChecked()) {
                    Toast.makeText(activity, "Please select payment method", Toast.LENGTH_SHORT).show();
                } else {
                    if (dataBinding.rbGooglePay.isChecked()) {
                        payWithGoogle();
                    } else {
                        placeOrder();
                    }

                }


                break;
            case R.id.tvChangeText:
                if (addressModel == null || addressModel.getId() == 0) {
                    AddAddressFragment fragment = AddAddressFragment.getInstance(addressModel, 1);
                    fragment.setTargetFragment(this, 121);
                    activity.setFragment(fragment, R.id.framLay, true, true, null);
                } else {
                    AddAddressFragment fragment = AddAddressFragment.getInstance(addressModel, 2);
                    fragment.setTargetFragment(this, 121);
                    activity.setFragment(fragment, R.id.framLay, true, true, null);
                }
                break;
        }
    }

    private void placeOrder() {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("user_name", addressModel.getUserName());
            jsonObject.put("address", addressModel.getAddress());
            jsonObject.put("order_total", dataBinding.tvTotal.getText().toString().trim().replace("$", ""));
            jsonObject.put("order_subtotal", dataBinding.tvSubtotal.getText().toString().trim().replace("$", ""));
            jsonObject.put("delivery_charges", dataBinding.tvDelivery.getText().toString().trim().replace("$", ""));
            jsonObject.put("payment_method", dataBinding.rbCOD.isChecked() ? 1 : 2);
            jsonObject.put("total_products", cartProductsItems.size());
            JSONArray jsonArray = new JSONArray();
            for (int i = 0; i < cartProductsItems.size(); i++) {
                JSONObject jsonObject1 = new JSONObject();
                jsonObject1.put("product_id", cartProductsItems.get(i).getProductId());
                jsonObject1.put("cart_id", cartProductsItems.get(i).getId());
                jsonObject1.put("price", cartProductsItems.get(i).getSellingPrice());
                jsonObject1.put("product_name", cartProductsItems.get(i).getName());
                jsonObject1.put("discount", cartProductsItems.get(i).getDiscountPrice());
                jsonObject1.put("quantity", cartProductsItems.get(i).getQuantity());
                jsonObject1.put("enum_value", cartProductsItems.get(i).getEnumValue());
                jsonArray.put(jsonObject1);
            }
            jsonObject.put("order_details", jsonArray);

            activity.callWb(activity, Constants.PLACE_ORDER, Constants.POST, MethodName.PLACE_ORDER, jsonObject, true, false, this, null);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void payWithGoogle() {
//        PaymentsClient mPaymentsClient = PaymentsUtil.createPaymentsClient(activity);
//        // Disables the button to prevent multiple clicks.
////            mGooglePayButton.setClickable(false);
//
//        // The price provided to the API should include taxes and shipping.
//        // This price is not displayed to the user.
////        String price = PaymentsUtil.microsToString(Long.parseLong(dataBinding.tvTotal.getText().toString().trim().replace("$", "")));
//        String price = dataBinding.tvTotal.getText().toString().trim().replace("$", "");
//
//        // TransactionInfo transaction = PaymentsUtil.createTransaction(price);
//        Optional<JSONObject> paymentDataRequestJson = PaymentsUtil.getPaymentDataRequest("0.01");
//        if (!paymentDataRequestJson.isPresent()) {
//            return;
//        }
//        PaymentDataRequest request =
//                PaymentDataRequest.fromJson(paymentDataRequestJson.get().toString());
//
//        // Since loadPaymentData may show the UI asking the user to select a payment method, we use
//        // AutoResolveHelper to wait for the user interacting with it. Once completed,
//        // onActivityResult will be called with the result.
//        if (request != null) {
//            AutoResolveHelper.resolveTask(
//                    mPaymentsClient.loadPaymentData(request), activity, LOAD_PAYMENT_DATA_REQUEST_CODE);
//        }


        // PAYMENT_INTENT_SALE will cause the payment to complete immediately.
        // Change PAYMENT_INTENT_SALE to
        //   - PAYMENT_INTENT_AUTHORIZE to only authorize payment and capture funds later.
        //   - PAYMENT_INTENT_ORDER to create a payment for authorization and capture
        //     later via calls from your server.
        //Default USD  its change "CAD" for canadian dollar.
        PayPalPayment payment = new PayPalPayment(new BigDecimal(dataBinding.tvTotal.getText().toString().replace("$", "")), "AUD", "Shoppy Order",
                PayPalPayment.PAYMENT_INTENT_SALE);

        Intent intent = new Intent(activity, com.paypal.android.sdk.payments.PaymentActivity.class);

        // send the same configuration for restart resiliency

        PayPalConfiguration config = new PayPalConfiguration()

                // Start with mock environment.  When ready, switch to sandbox (ENVIRONMENT_SANDBOX)
                // or live (ENVIRONMENT_PRODUCTION)
//                .environment(PayPalConfiguration.ENVIRONMENT_PRODUCTION)
                .environment(PayPalConfiguration.ENVIRONMENT_NO_NETWORK)

                .clientId(Constants.PAYPAL_CLIENT_ID);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);

        intent.putExtra(com.paypal.android.sdk.payments.PaymentActivity.EXTRA_PAYMENT, payment);

        activity.startActivityForResult(intent, LOAD_PAYMENT_DATA_REQUEST_CODE);
    }

    private boolean checkOutofStock() {
        for (CartProductsItem cartItem : cartProductsItems) {
            if (cartItem.getAvailableStock() > 0) {
                return false;
            }
        }
        return true;
    }


    private void callUpdateQuantity(int id, int productQty) {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id", id);
            jsonObject.put("quantity", productQty);
            activity.callWb(activity, Constants.UPDATE_QUANTITY_IN_CART, Constants.PUT, MethodName.UPDATE_QUANTITY_IN_CART, jsonObject, false, false, this, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 121) {
            addressModel = data.getParcelableExtra("ADDRESS_MODEL");
            setAddressDetails();
        } else if (requestCode == LOAD_PAYMENT_DATA_REQUEST_CODE) {
            if (resultCode == com.paypal.android.sdk.payments.PaymentActivity.RESULT_OK) {
                placeOrder();
            } else if (resultCode == com.paypal.android.sdk.payments.PaymentActivity.RESULT_CANCELED) {
                Toast.makeText(activity, "Payment canceled", Toast.LENGTH_SHORT).show();
            } else if (resultCode == com.paypal.android.sdk.payments.PaymentActivity.RESULT_EXTRAS_INVALID) {
                Toast.makeText(activity, "Payment fail", Toast.LENGTH_SHORT).show();
            }

        }
    }

    @Override
    public void onItemClicked(final int position, View v, int type) {
        // 0 for minus Quantity , 1 for plus Quantity, 2 for delete product, 3 for delete shop , 4 for check box shop , 5 for checkbox product

        if (activity.isOffline(activity)) {
            activity.showSnackbar(getString(R.string.warn_no_internet), dataBinding.getRoot(), Snackbar.LENGTH_LONG);
            return;
        }
        switch (type) {
            case 0:
                mAdapter.notifyItemChanged(position);
                updatePrice();
                callUpdateQuantity(cartProductsItems.get(position).getId(), cartProductsItems.get(position).getQuantity());
                break;

            case 1:
                mAdapter.notifyItemChanged(position);
                updatePrice();
                callUpdateQuantity(cartProductsItems.get(position).getId(), cartProductsItems.get(position).getQuantity());

                break;
            case 2:
                new AlertDialog.Builder(activity).setMessage(getString(R.string.delete_product_from_cart_msg))
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                deleteProductOrShop(cartProductsItems.get(position).getId()); //deleteProduct
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).create().show();
                break;
            case 3:
                break;
            case 4:
                break;
            case 5:
                break;
        }
    }
}