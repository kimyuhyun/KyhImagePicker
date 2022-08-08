package com.hongslab.kyh_image_picker;

import android.content.Context;
import android.content.Intent;

import androidx.activity.result.ActivityResultLauncher;


public class KyhImagePicker {
    private Context mContext;
    private Intent mIntent;

    public static KyhImagePicker of(Context context) {
        return new KyhImagePicker(context);
    }

    private KyhImagePicker(Context context) {
        mContext = context;
        mIntent = new Intent();
        mIntent.setClass(mContext, KyhImagePickerAC.class);
    }

    public KyhImagePicker setTitle(String title) {
        mIntent.putExtra("title", title);
        return this;
    }

    public KyhImagePicker setLimitCount(int limitCount) {
        mIntent.putExtra("limit_count", limitCount);
        return this;
    }

    public KyhImagePicker setLimitMessage(String msg) {
        mIntent.putExtra("limit_message", msg);
        return this;
    }

    public void open(ActivityResultLauncher<Intent> startActivityResult) {
        startActivityResult.launch(mIntent);
    }


}
