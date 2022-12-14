package com.honglab.kyh_image_picker;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.exifinterface.media.ExifInterface;

import com.honglab.kyh_image_picker.model.DataVO;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Comparator;

public class BaseAC extends AppCompatActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public static class compPopulation implements Comparator<DataVO> {
        public int compare(DataVO a, DataVO b) {
            if (a.getSeq() < b.getSeq())
                return -1; // highest value first
            if (a.getSeq() == b.getSeq())
                return 0;
            return 1;
        }
    }

    public static int exifOrientationToDegrees(int exifOrientation) {
        if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) {
            return 90;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {
            return 180;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {
            return 270;
        }
        return 0;
    }

    public static String getRealPathFromURI(Context context, Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
        cursor.moveToNext();
        @SuppressLint("Range") String path = cursor.getString(cursor.getColumnIndex(MediaStore.MediaColumns.DATA));
        cursor.close();
        return path;
    }

    public static InputStream getImageInputStram(Bitmap bmp) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        byte[] bitmapData = bytes.toByteArray();
        ByteArrayInputStream bs = new ByteArrayInputStream(bitmapData);
        return bs;
    }

    public static byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }

    /**
     * ???????????? ??????????????????.
     *
     * @param bitmap  ????????? ?????????
     * @param degrees ?????? ??????
     * @return ????????? ?????????
     */
    public static Bitmap rotate(Bitmap bitmap, int degrees) {
        if (degrees != 0 && bitmap != null) {
            Matrix m = new Matrix();
            m.setRotate(degrees, (float) bitmap.getWidth() / 2, (float) bitmap.getHeight() / 2);
            try {
                Bitmap converted = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), m, true);
                if (bitmap != converted) {
                    bitmap.recycle();
                    bitmap = converted;
                }
            } catch (OutOfMemoryError ex) {
                // ???????????? ???????????? ????????? ????????? ?????? ?????? ?????? ????????? ???????????????.
            }
        }
        return bitmap;
    }

    /**
     * Bitmap???????????? ??????, ?????? ???????????? ???????????? ??????.
     *
     * @param source        ?????? Bitmap ??????
     * @param maxResolution ?????? ?????????
     * @return ??????????????? ????????? Bitmap ??????
     */
    public static Bitmap resizeBitmapImage(Bitmap source, int maxResolution) {
        int width = source.getWidth();
        int height = source.getHeight();
        int newWidth = width;
        int newHeight = height;
        float rate = 0.0f;

        if (width > height) {
            if (maxResolution < width) {
                rate = maxResolution / (float) width;
                newHeight = (int) (height * rate);
                newWidth = maxResolution;
            }
        } else {
            if (maxResolution < height) {
                rate = maxResolution / (float) height;
                newWidth = (int) (width * rate);
                newHeight = maxResolution;
            }
        }

        source.recycle();


        return Bitmap.createScaledBitmap(source, newWidth, newHeight, true);
    }

    public String saveImage(Bitmap bmp) {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.DISPLAY_NAME, "bbiribbabba" + System.currentTimeMillis() + ".png");
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/*");
        values.put(MediaStore.Images.Media.IS_PENDING, 1);

        ContentResolver contentResolver = getContentResolver();
        Uri collection = MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY);
        Uri uri = contentResolver.insert(collection, values);

        try {
            ParcelFileDescriptor pfd = contentResolver.openFileDescriptor(uri, "w", null);
            if (pfd != null) {
                InputStream inputStream = getImageInputStram(bmp);
                byte[] strToByte = getBytes(inputStream);
                FileOutputStream fos = new FileOutputStream(pfd.getFileDescriptor());
                fos.write(strToByte);
                fos.close();
                inputStream.close();
                pfd.close();
                contentResolver.update(uri, values, null, null);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        values.clear();
        // ????????? ?????? write?????? ??????????????? ????????? ??? ????????? 0?????? ??????????????? ????????????.
        values.put(MediaStore.Images.Media.IS_PENDING, 0);
        contentResolver.update(uri, values, null, null);

        String filePath = getRealPathFromURI(getApplicationContext(), uri);

        //???????????? ??????
        sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse(filePath)));
        //

        return filePath;
    }
}
