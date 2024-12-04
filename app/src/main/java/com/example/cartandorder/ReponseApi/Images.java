package com.example.cartandorder.ReponseApi;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class Images implements Parcelable {
    private long imageId;
    private String url;


    public Images(long imageId,String url) {
        this.url = url;
        this.imageId = imageId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public long getImageId() {
        return imageId;
    }

    public void setImageId(long imageId) {
        this.imageId = imageId;
    }

    protected Images(Parcel in) {
        imageId = in.readLong();
        url = in.readString();
    }

    public static final Creator<Images> CREATOR = new Creator<Images>() {
        @Override
        public Images createFromParcel(Parcel in) {
            return new Images(in);
        }

        @Override
        public Images[] newArray(int size) {
            return new Images[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(imageId);
        dest.writeString(url);
    }

    @Override
    public int describeContents() {
        return 0;
    }


}
