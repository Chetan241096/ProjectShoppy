package com.shoppy.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Window;
import android.view.WindowManager;

import com.shoppy.R;


public class CustomLoaderDialog {
    private Dialog dialog = null;

    public CustomLoaderDialog(Context context) {
        dialog = new Dialog(context/*, R.style.DialogTheme*/);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

    }

    public void show(Boolean isCancelable) {
        //dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        dialog.setCancelable(isCancelable);
        dialog.setContentView(R.layout.progress_layout);
        dialog.show();
    }

    public Boolean isShowing() {
        return dialog.isShowing();
    }

    public void hide() {
        if (dialog != null) {
            dialog.cancel();
            dialog.dismiss();
        }
    }
}