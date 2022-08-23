package com.live.fox.entity;


import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * 交易记录详情页面
 */
public class TransactionEntity {

    private double totalIncome;
    private double totalExpenditure;
    private List<CenterUserAssetsPlusVOSDTO> centerUserAssetsPlusVOS;

    public double getTotalIncome() {
        return totalIncome;
    }

    public void setTotalIncome(double totalIncome) {
        this.totalIncome = totalIncome;
    }

    public double getTotalExpenditure() {
        return totalExpenditure;
    }

    public void setTotalExpenditure(double totalExpenditure) {
        this.totalExpenditure = totalExpenditure;
    }

    public List<CenterUserAssetsPlusVOSDTO> getCenterUserAssetsPlusVOS() {
        return centerUserAssetsPlusVOS;
    }

    public void setCenterUserAssetsPlusVOS(List<CenterUserAssetsPlusVOSDTO> centerUserAssetsPlusVOS) {
        this.centerUserAssetsPlusVOS = centerUserAssetsPlusVOS;
    }

    public static class CenterUserAssetsPlusVOSDTO implements Parcelable {

        private Object id;
        private long uid;
        private Object type;
        private Object changeCoin;
        private double goldCoin;
        private int isIncrease;
        private long gmtCreate;
        private String name;
        private String trn;
        private double betAmoney;
        private String expect;


        protected CenterUserAssetsPlusVOSDTO(Parcel in) {
            uid = in.readLong();
            goldCoin = in.readDouble();
            isIncrease = in.readInt();
            gmtCreate = in.readLong();
            name = in.readString();
            trn = in.readString();
            betAmoney = in.readDouble();
            expect = in.readString();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeLong(uid);
            dest.writeDouble(goldCoin);
            dest.writeInt(isIncrease);
            dest.writeLong(gmtCreate);
            dest.writeString(name);
            dest.writeString(trn);
            dest.writeDouble(betAmoney);
            dest.writeString(expect);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        public static final Creator<CenterUserAssetsPlusVOSDTO> CREATOR = new Creator<CenterUserAssetsPlusVOSDTO>() {
            @Override
            public CenterUserAssetsPlusVOSDTO createFromParcel(Parcel in) {
                return new CenterUserAssetsPlusVOSDTO(in);
            }

            @Override
            public CenterUserAssetsPlusVOSDTO[] newArray(int size) {
                return new CenterUserAssetsPlusVOSDTO[size];
            }
        };

        public Object getId() {
            return id;
        }

        public void setId(Object id) {
            this.id = id;
        }

        public long getUid() {
            return uid;
        }

        public void setUid(long uid) {
            this.uid = uid;
        }

        public Object getType() {
            return type;
        }

        public void setType(Object type) {
            this.type = type;
        }

        public Object getChangeCoin() {
            return changeCoin;
        }

        public void setChangeCoin(Object changeCoin) {
            this.changeCoin = changeCoin;
        }

        public double getGoldCoin() {
            return goldCoin;
        }

        public void setGoldCoin(double goldCoin) {
            this.goldCoin = goldCoin;
        }

        public int getIsIncrease() {
            return isIncrease;
        }

        public void setIsIncrease(int isIncrease) {
            this.isIncrease = isIncrease;
        }

        public long getGmtCreate() {
            return gmtCreate;
        }

        public void setGmtCreate(long gmtCreate) {
            this.gmtCreate = gmtCreate;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getTrn() {
            return trn;
        }

        public void setTrn(String trn) {
            this.trn = trn;
        }

        public double getBetAmoney() {
            return betAmoney;
        }

        public void setBetAmoney(double betAmoney) {
            this.betAmoney = betAmoney;
        }

        public String getExpect() {
            return expect;
        }

        public void setExpect(String expect) {
            this.expect = expect;
        }
    }
}
