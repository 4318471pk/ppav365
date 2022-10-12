package com.live.fox.utils.pickphoto;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * ImageBean PickPhoto图片展示时使用的类
 * Created by cheng on 2017/8/13.
 */

public class MultiplexImage implements Parcelable {
    /**
     * original image 原始图
     */
    private String imagePath;
    /**
     * thumbnails image 缩略图
     */
    private String thumImagePath;


    public MultiplexImage(String imagePath){
        this.imagePath = imagePath;
    }

    public MultiplexImage(String imagePath, String thumImagePath) {
        this.imagePath = imagePath;
        this.thumImagePath = thumImagePath;
    }



    public static final class ImageType{
        public static final int NORMAL = 1;
        public static final int GIF = 2;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getThumImagePath() {
        return thumImagePath;
    }

    public void setThumImagePath(String thumImagePath) {
        this.thumImagePath = thumImagePath;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this. imagePath);
        dest.writeString(this. thumImagePath);
    }

    protected MultiplexImage(Parcel in) {
        this.imagePath = in.readString();
        this.thumImagePath = in.readString();
    }

    public static final Creator<MultiplexImage> CREATOR = new Creator<MultiplexImage>() {
        @Override
        public MultiplexImage createFromParcel(Parcel source) {
            return new MultiplexImage(source);
        }

        @Override
        public MultiplexImage[] newArray(int size) {
            return new MultiplexImage[size];
        }
};
}
