package com.honglab.kyh_image_picker_example;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import com.honglab.kyh_image_picker.KyhImagePicker;
import com.honglab.kyh_image_picker.model.UriVO;


import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    ImageView iv0;
    ImageView iv1;
    ImageView iv2;
    ImageView iv3;
    ImageView iv4;
    Button btnOpenGallery;

    ArrayList<ImageView> imageViewArrayList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        iv0 = (ImageView) findViewById(R.id.iv_0);
        iv1 = (ImageView) findViewById(R.id.iv_1);
        iv2 = (ImageView) findViewById(R.id.iv_2);
        iv3 = (ImageView) findViewById(R.id.iv_3);
        iv4 = (ImageView) findViewById(R.id.iv_4);

        imageViewArrayList = new ArrayList<>();
        imageViewArrayList.add(iv0);
        imageViewArrayList.add(iv1);
        imageViewArrayList.add(iv2);
        imageViewArrayList.add(iv3);
        imageViewArrayList.add(iv4);

        btnOpenGallery = (Button) findViewById(R.id.btn_open_gallery);

        btnOpenGallery.setOnClickListener(v -> {
            iv0.setImageBitmap(null);
            iv1.setImageBitmap(null);
            iv2.setImageBitmap(null);
            iv3.setImageBitmap(null);
            iv4.setImageBitmap(null);

            KyhImagePicker.of(getApplicationContext())
                    .setTitle("사진선택")
                    .setLimitCount(5)
                    .setLimitMessage("이미지는 5개까지 선택 할 수 있습니다.")
                    .setNoSelectedMessage("Please select image.")
                    .setListener((resultCode, data) -> {
                        if (resultCode == Activity.RESULT_OK) {
                            ArrayList<UriVO> list = data.getParcelableArrayListExtra("kyh_image_picked_list");
                            for (int i = 0; i < list.size(); i++) {
                                imageViewArrayList.get(i).setImageURI(list.get(i).uri);
                            }
                        }
                    }).startActivityForResult();
        });
    }


}