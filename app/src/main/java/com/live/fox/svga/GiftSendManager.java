package com.live.fox.svga;


import com.live.fox.entity.GiftSendBase;
import com.live.fox.manager.DataCenter;
import com.live.fox.utils.ClickUtil;
import com.live.fox.manager.AppUserManger;

public class GiftSendManager extends GiftSendBase {

    private GiftSendManager() {
    }

    private static class SingletonHolder {
        private static final GiftSendManager INTANCE = new GiftSendManager();
    }

    public static GiftSendManager ins() {
        return SingletonHolder.INTANCE;
    }

    /**
     * type=0 禮物 =1 背包
     */
    private int giftType;
    private int gType = 0;
    private int money = 0;
    private int maxCount = 0;


    public void reset() {
        this.count = 1;
        this.combo = 0;
        this.gid = -9999;
        this.giftType = 0;
        this.gType = 0;
        this.money = 0;
        this.maxCount = 0;
    }

    public void prepare() {
        if (gType == 0) {
            combo++;
            if (combo == 1) {
                setSendStartTime(System.currentTimeMillis());
            }
        } else {
            combo = 1;
        }
    }

    public boolean isSend2Anchor() {
        return artistid == null || anchoruid.equals(artistid);
    }

    public int getGiftType() {
        return giftType;
    }

    public void setGiftType(int giftType) {
        this.giftType = giftType;
    }

    public int getMaxCount() {
        return maxCount;
    }

    public void setMaxCount(int maxCount) {
        this.maxCount = maxCount;
    }

    public int getgType() {
        return gType;
    }

    public void setgType(int gType) {
        this.gType = gType;
    }

    public int getMoney() {
        return money;
    }

    public boolean isPackageGiftCanSend() {
        return maxCount >= count;
    }

    public boolean isGiftCanSend() {
        return DataCenter.getInstance().getUserInfo().getUser().getGoldCoin() >= money * count;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public void sendGift(String tag, final int gid, final SendGiftRequestListener callback) {
        if (ClickUtil.isGiftFastClick()) {
            return;
        }
        prepare();
//        Api_Live.ins().sendGift(new JsonCallback<String>() {
//            @Override
//            public void onSuccess(int code, String msg, String result) {
////                LogUtils.e("json : " + json);
//                if (code == HttpResponseCode.HTTP_SUCCESS) {
//                    //服務端從2018-11-30開始不再對此接口做金幣返回的信息，所有金幣變動走PROTOCOL_BALANCE_CHANGE消息
////                    if (json != null && json instanceof Integer) {
////                        int surplus = (int) json;
////                        UserManager.ins().getLoginUser().setGoldCoin(Long.valueOf(surplus));
////                    }
//                    callback.success(gid, GiftSendManager.ins().getGiftType() == 1, GiftSendManager.ins().getGiftType() == 1 ? (GiftSendManager.ins().getMaxCount() - GiftSendManager.ins().getCount()) : 0);
//                } else {
//                    ToastUtils.showShort(msg);
//                }
//            }
//        });
    }

    public interface SendGiftRequestListener {
        void success(int gid, boolean isPackage, int rest);
    }



}
