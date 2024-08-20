package com.shoppy.baseclass;


import androidx.fragment.app.Fragment;

import com.shoppy.retrofit.RetrofitResponse;

public class BaseFragment extends Fragment implements RetrofitResponse {

    @Override
    public void onResponse(String response, String methodName, int responseCode) {

    }

    @Override
    public void onResponseFail(String methodName, int responseCode) {

    }

    public interface Callback {

        void onFragmentAttached();

        void onFragmentDetached(String tag);
    }

}
