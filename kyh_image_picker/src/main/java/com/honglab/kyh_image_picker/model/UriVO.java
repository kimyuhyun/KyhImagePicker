package com.honglab.kyh_image_picker.model;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

public class UriVO implements Parcelable {
    public Uri uri;

    public UriVO(Uri uri) {
        this.uri = uri;
    }

    protected UriVO(Parcel in) {
        uri = in.readParcelable(Uri.class.getClassLoader());
    }

    public static final Creator<UriVO> CREATOR = new Creator<UriVO>() {
        @Override
        public UriVO createFromParcel(Parcel in) {
            return new UriVO(in);
        }

        @Override
        public UriVO[] newArray(int size) {
            return new UriVO[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeParcelable(uri, i);
    }
}
