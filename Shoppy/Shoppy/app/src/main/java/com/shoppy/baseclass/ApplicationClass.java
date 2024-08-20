package com.shoppy.baseclass;

import android.content.Context;
import android.os.StrictMode;

import androidx.multidex.MultiDexApplication;


public class ApplicationClass extends MultiDexApplication {
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

    }

    public static Context getContext() {
        return context;
    }
}
