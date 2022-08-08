package com.hongslab.kyh_image_picker;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import androidx.exifinterface.media.ExifInterface;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.hongslab.kyh_image_picker.adapter.GalleryAdapter;
import com.hongslab.kyh_image_picker.model.DataVO;
import com.hongslab.kyh_image_picker.utils.Dlog;
import com.hongslab.kyh_image_picker.utils.GridSpacingItemDecoration;
import com.hongslab.kyh_image_picker.utils.PinchImageView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class KyhImagePickerAC extends BaseAC {
    private int screenWidth = 0;
    private int width = 0;
    private ArrayList<DataVO> mList = new ArrayList<>();
    private GalleryAdapter mGalleryAdapter;
    private int mLimitCount = 1;
    private String mLimitMessage = "";

    Toolbar tool_bar;
    FrameLayout fl_preview;
    RecyclerView recycler_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kyh_image_picker);

        tool_bar = (Toolbar) findViewById(R.id.tool_bar);
        fl_preview = (FrameLayout) findViewById(R.id.fl_preview);
        recycler_view = (RecyclerView) findViewById(R.id.recycler_view);

        mLimitCount = getIntent().getIntExtra("limit_count", 1);
        mLimitMessage = getIntent().getStringExtra("limit_message");

        tool_bar.setTitle(getIntent().getStringExtra("title"));
        setSupportActionBar(tool_bar);

        //백버튼임..
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_round_arrow_back_24);

        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);
        screenWidth = outMetrics.widthPixels;
        width = (outMetrics.widthPixels - 3) / 4;

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(outMetrics.widthPixels, outMetrics.widthPixels);
        params.addRule(RelativeLayout.BELOW, R.id.tool_bar);
        fl_preview.setLayoutParams(params);

        recycler_view.setLayoutManager(new GridLayoutManager(this, 4));
        recycler_view.addItemDecoration(new GridSpacingItemDecoration(4, 1, false));
        mGalleryAdapter = new GalleryAdapter(this, new GalleryAdapter.AdapterClickListener() {
            @Override
            public void onClick(int pos) {
                if (mList.get(pos).isChoose() && !mList.get(pos).isToogle()) {
                    //DESC 정렬해서 가장 큰수를 가져온다!
                    int seq = Collections.max(mList, new BaseAC.compPopulation()).getSeq();
                    if (seq >= mLimitCount - 1) {
                        Toast.makeText(getApplicationContext(), mLimitMessage, Toast.LENGTH_SHORT).show();
                        return;
                    }

                    mList.get(pos).setToogle(true);

                    seq++;

                    mList.get(pos).setSeq(seq);

                } else if (mList.get(pos).isChoose() && mList.get(pos).isToogle()) {
                    mList.get(pos).setToogle(false);

                    int seq = mList.get(pos).getSeq();
                    for (DataVO row : mList) {
                        if (row.getSeq() > seq) {
                            row.setSeq(row.getSeq() - 1);
                        }
                    }
                    mList.get(pos).setSeq(-1);
                }

                for (DataVO row : mList) {
                    row.setChoose(false);
                }

                mList.get(pos).setChoose(true);
                showImage(mList.get(pos));
                mGalleryAdapter.notifyDataSetChanged();


            }
        }, mList, width);
        recycler_view.setAdapter(mGalleryAdapter);


    }

    @Override
    protected void onResume() {
        super.onResume();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 101);
        } else {
            loadGallery();
        }
    }

    private void loadGallery() {
        mList.clear();
        Dlog.d("loadGallery");

        String[] what = new String[]{
                MediaStore.Images.ImageColumns._ID,
                MediaStore.Images.ImageColumns.DATA,
                MediaStore.Images.ImageColumns.DATE_ADDED
        };

        String where = "";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            where = MediaStore.Images.Media.SIZE + " > 0";
        }

        Cursor cursor = getContentResolver()
                .query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        what,
                        where,
                        null,
                        MediaStore.Images.ImageColumns.DATE_ADDED + " DESC");

        int dataColumnIndex = cursor.getColumnIndex(what[1]);
        while (cursor.moveToNext()) {
            String absolutePathOfImage = cursor.getString(dataColumnIndex);
            if (!TextUtils.isEmpty(absolutePathOfImage)) {

                if (!absolutePathOfImage.contains("bbiribbabba")) {
                    DataVO vo = new DataVO();
                    vo.setPath(absolutePathOfImage);
                    mList.add(vo);
                }
            }
        }

        Dlog.d("" + mList.size());
        mGalleryAdapter.notifyDataSetChanged();

    }


    private void showImage(DataVO item) {
        fl_preview.removeAllViews();

        //이미 생성되었다면..
        if (item.pinchImageView != null) {
            fl_preview.addView(item.pinchImageView);
            return;
        }

        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 2;
            Bitmap image = BitmapFactory.decodeFile(item.getPath(), options);

            if (image.getWidth() < screenWidth) {
                image = BitmapFactory.decodeFile(item.getPath());
            }

            // 이미지를 상황에 맞게 회전시킨다
            ExifInterface exif = new ExifInterface(item.getPath());
            int exifOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            int exifDegree = exifOrientationToDegrees(exifOrientation);
            image = rotate(image, exifDegree);

            // 변환된 이미지 사용
            item.pinchImageView = new PinchImageView(getApplicationContext());
            item.pinchImageView.setImageBitmap(image);

            fl_preview.addView(item.pinchImageView);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == android.R.id.home) {
            onBackPressed();
            return true;
        } else if (itemId == R.id.action_camera) {
            try {
                Intent i = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                PackageManager pm = getPackageManager();
                final ResolveInfo mInfo = pm.resolveActivity(i, 0);
                Intent intent = new Intent();
                intent.setComponent(new ComponentName(mInfo.activityInfo.packageName, mInfo.activityInfo.name));
                intent.setAction(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_LAUNCHER);
                startActivity(intent);
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "There is no default camera app.", Toast.LENGTH_SHORT).show();
            }
            return true;
        } else if (itemId == R.id.action_ok) {
            ArrayList<DataVO> list = new ArrayList<>();

            for (DataVO row : mList) {
                if (row.getSeq() > -1) {
                    list.add(row);
                }
            }

            //ASC 정렬!
            Collections.sort(list, new Comparator<DataVO>() {
                @Override
                public int compare(DataVO s1, DataVO s2) {
                    if (s1.getSeq() < s2.getSeq()) {
                        return -1;
                    } else if (s1.getSeq() > s2.getSeq()) {
                        return 1;
                    }
                    return 0;
                }
            });

            ArrayList<String> list2 = new ArrayList<>();
            for (int i = 0; i < list.size(); i++) {
                list.get(i).pinchImageView.buildDrawingCache();
                Bitmap bmp = list.get(i).pinchImageView.getDrawingCache();
                String filePath = saveImage(bmp);
                list2.add(filePath);
            }
            Intent intent = new Intent();
            intent.putStringArrayListExtra("kyh_image_picked_list", list2);
            setResult(RESULT_OK, intent);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.tool_bar_menu, menu);
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 101: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    loadGallery();
                }
                return;
            }
        }
    }
}