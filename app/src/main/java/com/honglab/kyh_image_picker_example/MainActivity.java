package com.honglab.kyh_image_picker_example;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;

import com.honglab.kyh_image_picker.KyhImagePicker;
import com.honglab.kyh_image_picker.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {


    @BindViews({R.id.iv_0, R.id.iv_1, R.id.iv_2, R.id.iv_3, R.id.iv_4})
    List<ImageView> ivs;

    @OnClick(R.id.btn_open_gallery)
    public void click() {

        for (ImageView iv : ivs) {
            iv.setImageBitmap(null);
        }

        KyhImagePicker.of(getApplicationContext())
                .setTitle("사진선택")
                .setLimitCount(5)
                .setLimitMessage("이미지는 5개까지 선택 할 수 있습니다.")
                .open(startActivityResult);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }


    public ActivityResultLauncher<Intent> startActivityResult = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        ArrayList<String> list = result.getData().getStringArrayListExtra("kyh_image_picked_list");

                        int i = 0;
                        for (String path : list) {
                            ivs.get(i).setImageURI(Uri.parse(path));
                            i++;
                        }

                    }
                }
            });
}