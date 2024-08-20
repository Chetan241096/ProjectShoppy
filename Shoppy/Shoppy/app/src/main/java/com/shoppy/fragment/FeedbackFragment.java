package com.shoppy.fragment;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import com.google.android.material.snackbar.Snackbar;
import com.shoppy.R;
import com.shoppy.activity.HomeActivity;
import com.shoppy.baseclass.BaseFragment;
import com.shoppy.databinding.FragmentFeedbackBinding;
import com.shoppy.global.Constants;
import com.shoppy.global.Pref;
import com.shoppy.listener.ItemOnClickListener;
import com.shoppy.retrofit.MethodName;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class FeedbackFragment extends BaseFragment implements View.OnClickListener, ItemOnClickListener, AdapterView.OnItemSelectedListener {
    private FragmentFeedbackBinding dataBinding;
    private HomeActivity activity;

    List<String> spinnerArray;
    private String selectedTopic = "";
    private int selectedPos = 0;
    private final int STORAGE_PERMISSION_CODE = 11;
    private int GALLERY_CODE = 12;
    private Uri selectedImageURI;
    private File selectedFile;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_feedback, container, false);
        return dataBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        dataBinding.actionBar.tvTitle.setText(getString(R.string.title_Feedback));
        dataBinding.actionBar.ivBack.setVisibility(View.GONE);
        dataBinding.actionBar.ivDrawerMenu.setVisibility(View.VISIBLE);
        dataBinding.actionBar.ivDrawerMenu.setOnClickListener(this);
        dataBinding.etUserFeedbackPhno.setText(Pref.getContactNo(activity));
        spinnerArray = new ArrayList<>();
        spinnerArray.add("App Crashes or Performance");
        spinnerArray.add("Suggestions");

        ArrayAdapter adapter = new ArrayAdapter<>(
                activity, android.R.layout.simple_spinner_item, spinnerArray);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dataBinding.spinner.setAdapter(adapter);
        dataBinding.spinner.setOnItemSelectedListener(this);
        dataBinding.btnUserFeedbackSend.setOnClickListener(this);
        dataBinding.ivAttachedImage1.setOnClickListener(this);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (HomeActivity) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
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
            case R.id.iv_attached_image_1:
                if (checkPermission()) {
                    gallaryIntent();
                }
                break;
            case R.id.btn_user_feedback_send:
                sendFeedback();
                break;
        }
    }

    @Override
    public void onItemClicked(int position, View v, int type) {

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        selectedTopic = spinnerArray.get(position);
        selectedPos = position;

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_CODE && resultCode == RESULT_OK) {
            Bitmap bitmap = null;
            if (data != null) {
                try {
                    selectedImageURI = data.getData();

                    selectedFile = new File(activity.getFilesDir() + File.separator + "temporary_file.jpg");

                    bitmap = MediaStore.Images.Media.getBitmap(activity.getContentResolver(), data.getData());
                    ByteArrayOutputStream byteOutput = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 90, byteOutput);

                    try {
                        FileOutputStream fo = new FileOutputStream(selectedFile);
                        fo.write(byteOutput.toByteArray());
                        fo.flush();
                        fo.close();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    dataBinding.ivAttachedImage1.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private boolean checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) != (PackageManager.PERMISSION_GRANTED)) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case STORAGE_PERMISSION_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    gallaryIntent();
                } else if (!ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    openDialogForSetting();
                } else {
                    activity.showSnackbar(getString(R.string.msg_permission_needed), dataBinding.getRoot(), Snackbar.LENGTH_SHORT);
                }
                break;
        }
    }

    private void openDialogForSetting() {
        AlertDialog alertDialog = new AlertDialog.Builder(activity)
                .setTitle("Open Settings to grant permission?")
                .setMessage("It seems the permission access was permanently denied on this device. Kindly grant permission manually to proceed further.")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent();
                        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", activity.getPackageName(), null);
                        intent.setData(uri);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        activity.showSnackbar(getString(R.string.msg_permission_needed), dataBinding.getRoot(), Snackbar.LENGTH_SHORT);
                    }
                })
                .create();
        alertDialog.show();
    }

    //Gallery Intent Method
    private void gallaryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Image "), GALLERY_CODE);
    }

    private void sendFeedback() {
        if (dataBinding.etUserFeedbackPhno.getText().toString().trim().length() == 0 || dataBinding.etUserFeedbackPhno.getText().toString().trim().length() != 10) {
            activity.showSnackbar(getString(R.string.warn_valid_mobile_number), dataBinding.getRoot(), Snackbar.LENGTH_SHORT);
            return;
        }
        Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
        emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, "help@shoppy.com");
        emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, dataBinding.etFeedback.getText().toString());
        emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Feedback for Shoppy");
        emailIntent.setType("application/image");

        emailIntent.putExtra(Intent.EXTRA_STREAM, selectedImageURI);
        startActivity(emailIntent);
    }
}
