package com.shoppy.activity;

import androidx.annotation.Nullable;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import com.google.android.material.snackbar.Snackbar;
import com.shoppy.R;
import com.shoppy.adapter.DrawerMenuRecyclerAdapter;
import com.shoppy.baseclass.BaseActivity;
import com.shoppy.databinding.ActivityHomeBinding;
import com.shoppy.fragment.AllCategoryFragment;
import com.shoppy.fragment.FeedbackFragment;
import com.shoppy.fragment.HomeFragment;
import com.shoppy.fragment.MyCartFragment;
import com.shoppy.fragment.MyOrderFragment;
import com.shoppy.fragment.ProfileFragment;
import com.shoppy.global.Pref;
import com.shoppy.listener.ItemOnClickListener;
import com.shoppy.model.DrawerItem;

import java.util.ArrayList;


public class HomeActivity extends BaseActivity implements ItemOnClickListener {

    private static final int OPEN_SETTINGS_FOR_PERMISSION = 123;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 124;
    private static final int REQUEST_CHECK_SETTINGS = 125;
    private static final int LOCATION_SETTING_REQUEST_CODE = 127;
    public int GET_LOCATION_CODE = 126;
    private ActivityHomeBinding dataBinding;
    private ArrayList<DrawerItem> itemList = new ArrayList<>();
    public static int BACKPRESS_COUNT = 0;
    private DrawerMenuRecyclerAdapter drawerAdapter;
    private int lastSelection;
    private int locationSettingCount = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dataBinding = DataBindingUtil.setContentView(this, R.layout.activity_home);
        //Home fragment Set
        lastSelection = 0;
        setFragment(new HomeFragment(), R.id.framLay, false, false, null);
//        setUserName();
        //getCurrentLocation();
        createDrawerMenu();
    }


//    public void setUserName() {
//        dataBinding.navBottomLayout.tvUserName.setText(Pref.getUserName(this));
//        dataBinding.navBottomLayout.tvUserEmail.setText(Pref.getContactNo(this));
//
//        Glide.with(this)
//                .load(Pref.getImageUrl(this) + Pref.getUserImage(this))
//                .apply(new RequestOptions().circleCrop().error(Pref.getUserGender(this).equalsIgnoreCase("Female") ? R.drawable.female_avatar : R.drawable.man))
//                .into(dataBinding.navBottomLayout.ivProfile);
//
//        dataBinding.drawerLayout.setScrimColor(ContextCompat.getColor(this, android.R.color.transparent));
//
//        dataBinding.drawerLayout.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
//            @Override
//            public void onDrawerSlide(View drawerView, float slideOffset) {
//                super.onDrawerSlide(drawerView, slideOffset);
//                dataBinding.viewTrans.setAlpha((float) (slideOffset * 0.60));
//
//            }
//        });
//
//    }

    @Override
    public void onBackPressed() {
        if (dataBinding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            dataBinding.drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            if (getSupportFragmentManager().getBackStackEntryCount() == 0 && BACKPRESS_COUNT == 0) {
                if (getCurrentFragment(R.id.framLay) instanceof HomeFragment) {
                    showSnackbar("Please press back again for close application.", dataBinding.getRoot(), Snackbar.LENGTH_SHORT);
                    BACKPRESS_COUNT++;
                } else {
                    itemList.get(0).setSelected(true);
                    itemList.get(lastSelection).setSelected(false);
                    drawerAdapter.notifyDataSetChanged();
                    setFragment(HomeFragment.getInstance(), R.id.framLay, false, false, null);
                }

            } else {
                hideKeyboard(dataBinding.framLay);
                super.onBackPressed();
            }
        }

    }

    public void openCloseDrawer() {
        if (dataBinding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            dataBinding.drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            dataBinding.drawerLayout.openDrawer(GravityCompat.START);
        }
    }


    private void createDrawerMenu() {
        itemList.add(new DrawerItem(getString(R.string.title_home), true));//0
        itemList.add(new DrawerItem(getString(R.string.title_categories), false));//1
        itemList.add(new DrawerItem(getString(R.string.title_my_cart), false));//2
        itemList.add(new DrawerItem(getString(R.string.title_my_order), false));//3
        itemList.add(new DrawerItem(getString(R.string.title_profile), false));//4
        itemList.add(new DrawerItem(getString(R.string.title_Feedback), false));//5
        itemList.add(new DrawerItem(getString(R.string.title_help), false));//6
        itemList.add(new DrawerItem(getString(R.string.title_share), false));//7
        drawerAdapter = new DrawerMenuRecyclerAdapter(this, this, itemList);

        dataBinding.rvManu.setLayoutManager(new LinearLayoutManager(this));
        dataBinding.rvManu.setItemAnimator(new DefaultItemAnimator());
        dataBinding.rvManu.setAdapter(drawerAdapter);
    }

    @Override
    public void onItemClicked(int position, View v, int type) {
        Fragment fragment = null;
        BACKPRESS_COUNT = 0;
        dataBinding.drawerLayout.closeDrawer(GravityCompat.START);
        if (isOffline(this)) {
            showSnackbar(getString(R.string.warn_no_internet), dataBinding.getRoot(), Snackbar.LENGTH_LONG);
            return;
        }
        switch (position) {
            case 0:
                lastSelection = 0;
                fragment = HomeFragment.getInstance();
                break;
            case 1:
                lastSelection = 1;
                fragment = AllCategoryFragment.getInstance(0);
                break;
            case 2:
                lastSelection = 2;
                fragment = MyCartFragment.getInstance(1, 0);
                break;
            case 3:
                lastSelection = 3;
                fragment = new MyOrderFragment();
                break;
            case 4:
                lastSelection = 4;
                fragment = ProfileFragment.getInstance("");
                break;
            case 5:
                lastSelection = 5;
                fragment = new FeedbackFragment();
                break;
            case 6:
                lastSelection = 6;
                try {
                    Intent dialIntent = new Intent(Intent.ACTION_DIAL);
                    dialIntent.setData(Uri.parse("tel:180018001800"));
                    startActivity(dialIntent);
                } catch (Exception e) {
                    Snackbar.make(dataBinding.getRoot(), "Supported application not found", Snackbar.LENGTH_SHORT).show();
                }
                break;
            case 7:
                lastSelection = 7;
//                new AlertDialog.Builder(this).setMessage(getString(R.string.msg_logout))
//                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                dialog.dismiss();
//                                Pref.clearPrefs(HomeActivity.this);
//                                startActivity(new Intent(HomeActivity.this, LoginActivity.class));
//                                finish();
//                            }
//                        })
//                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                dialog.dismiss();
//                            }
//                        }).create().show();

                try {
                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_TEXT,
                            getString(R.string.share_app_msg));
                    sendIntent.setType("text/plain");
                    startActivity(sendIntent);
                } catch (Exception e) {
                    Snackbar.make(dataBinding.getRoot(), "Supported application not found", Snackbar.LENGTH_SHORT).show();

                }
                break;
        }

        final Fragment finalFragment = fragment;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (finalFragment != null) {
                    popAllFragment();
                    setFragment(finalFragment, R.id.framLay, false, false, null);
                }
            }
        }, 200);
    }


    public void disableSwipeMenu(boolean lock) {
        if (lock)
            dataBinding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (getCurrentFragment(R.id.framLay) != null)
            getCurrentFragment(R.id.framLay).onActivityResult(requestCode, resultCode, data);
        Log.d("onActivityResult", "HomeActivity");
    }
}