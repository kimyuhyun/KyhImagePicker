package com.hongslab.kyh_image_picker.utils;

import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.hongslab.kyh_image_picker.R;

public class TouchEffect {
    public static TouchEffect mTouchEffect;
    private Animation scaleUp, scaleDown;

    public static TouchEffect getInstance() {
        if (mTouchEffect == null) {
            mTouchEffect = new TouchEffect();
        }

        return mTouchEffect;
    }

    public void apply(View view) {
        scaleUp = AnimationUtils.loadAnimation(view.getContext(), R.anim.scale_up);
        scaleDown = AnimationUtils.loadAnimation(view.getContext(), R.anim.scale_down);

        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    view.startAnimation(scaleDown);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    view.startAnimation(scaleUp);
                }
                return false;
            }
        });
    }

}

