package com.live.fox.entity;

import android.os.Parcel;
import android.os.Parcelable;

public class FilterItemEntity implements Parcelable {


    private int id;
    private String name;
    private int type;
    private int stauts;
    private int queryType;
    private long createTime;
    private boolean isSelect;

    public FilterItemEntity() {

    }

    protected FilterItemEntity(Parcel in) {
        id = in.readInt();
        name = in.readString();
        type = in.readInt();
        stauts = in.readInt();
        queryType = in.readInt();
        createTime = in.readLong();
        isSelect = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeInt(type);
        dest.writeInt(stauts);
        dest.writeInt(queryType);
        dest.writeLong(createTime);
        dest.writeByte((byte) (isSelect ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<FilterItemEntity> CREATOR = new Creator<FilterItemEntity>() {
        @Override
        public FilterItemEntity createFromParcel(Parcel in) {
            return new FilterItemEntity(in);
        }

        @Override
        public FilterItemEntity[] newArray(int size) {
            return new FilterItemEntity[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getStauts() {
        return stauts;
    }

    public void setStauts(int stauts) {
        this.stauts = stauts;
    }

    public int getQueryType() {
        return queryType;
    }

    public void setQueryType(int queryType) {
        this.queryType = queryType;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }
}
