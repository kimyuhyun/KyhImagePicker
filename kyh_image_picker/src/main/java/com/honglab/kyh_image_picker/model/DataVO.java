package com.honglab.kyh_image_picker.model;

import android.graphics.Bitmap;
import android.net.Uri;

import com.honglab.kyh_image_picker.utils.PinchImageView;

public class DataVO {
    private int seq = -1;
    private long id;
    private Uri uri;
    private String path;
    private Bitmap thumb;
    private boolean isToogle = false;
    private boolean isChoose = false;
    public PinchImageView pinchImageView;


    public boolean isChoose() {
        return isChoose;
    }

    public void setChoose(boolean choose) {
        isChoose = choose;
    }

    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }

    public boolean isToogle() {
        return isToogle;
    }

    public void setToogle(boolean toogle) {
        isToogle = toogle;
    }

    public Uri getUri() {
        return uri;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Bitmap getThumb() {
        return thumb;
    }

    public void setThumb(Bitmap thumb) {
        this.thumb = thumb;
    }
}
