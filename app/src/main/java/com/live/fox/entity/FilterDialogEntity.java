package com.live.fox.entity;


import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * 弹窗过滤的实体
 */
public class FilterDialogEntity implements Parcelable {

    private int type;  //0:交易类型，1：交易时间
    private int position;
    private List<FilterItemEntity> filterItems;

    public FilterDialogEntity() {
    }

    protected FilterDialogEntity(Parcel in) {
        type = in.readInt();
        position = in.readInt();
        filterItems = in.createTypedArrayList(FilterItemEntity.CREATOR);
    }

    public static final Creator<FilterDialogEntity> CREATOR = new Creator<FilterDialogEntity>() {
        @Override
        public FilterDialogEntity createFromParcel(Parcel in) {
            return new FilterDialogEntity(in);
        }

        @Override
        public FilterDialogEntity[] newArray(int size) {
            return new FilterDialogEntity[size];
        }
    };

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public List<FilterItemEntity> getFilterItems() {
        return filterItems;
    }

    public void setFilterItems(List<FilterItemEntity> filterItems) {
        this.filterItems = filterItems;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(type);
        parcel.writeInt(position);
        parcel.writeTypedList(filterItems);
    }
}
