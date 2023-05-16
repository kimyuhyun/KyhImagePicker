package com.honglab.kyh_image_picker;

import android.content.Intent;

public class ActivityRequest {
    private Intent intent;
    private OnACResultListener listener;

    public ActivityRequest(Intent intent, OnACResultListener listener) {
        this.intent = intent;
        this.listener = listener;
    }

    public Intent getIntent() {
        return intent;
    }

    public OnACResultListener getListener() {
        return listener;
    }
}
