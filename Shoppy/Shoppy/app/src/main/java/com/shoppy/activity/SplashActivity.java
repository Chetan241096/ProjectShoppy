package com.shoppy.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.shoppy.R;
import com.shoppy.baseclass.BaseActivity;
import com.shoppy.global.Pref;


public class SplashActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Animation animation = AnimationUtils.loadAnimation(this, R.anim.splash_anim);
        findViewById(R.id.ivLogo).setAnimation(animation);

        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (Pref.getIsLogin(SplashActivity.this)) {
                    startActivity(new Intent(SplashActivity.this, HomeActivity.class));
                    //startActivity(new Intent(SplashActivity.this, ProductReviewActivity.class));
                    //startActivity(new Intent(SplashActivity.this, SellerReviewActivity.class));
                    finish();
                } else {
                    startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                    finish();
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
}
