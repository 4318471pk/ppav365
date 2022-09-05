package com.live.fox.base;


import com.live.fox.Constant;
import com.live.fox.contract.MyNobleContract;
import com.live.fox.entity.Noble;
import com.live.fox.server.Api_User;
import com.live.fox.common.JsonCallback;


public class MyNoblePrestener extends MyNobleContract.Presenter {

    public MyNoblePrestener(MyNobleContract.View view) {
        attachView(view);
    }

    @Override
    public void loadData() {
        getVipInnfo();
    }

    @Override
    public void getVipInnfo() {
        Api_User.ins().getVipInnfo(new JsonCallback<Noble>() {
            @Override
            public void onSuccess(int code, String msg, Noble data) {
                if (code == Constant.Code.SUCCESS) {
                    mView.onVipInfo(data);
                } else {
                    mView.showToastTip(false,msg);
                }

            }
        });
    }

    @Override
    public void cancelAllHttpAndDialog() {

    }

    @Override
    public void doVipHide(Noble mNoble) {//榜单隐身
        Api_User.ins().doVipHide(mNoble, new JsonCallback<String>() {
            @Override
            public void onSuccess(int code, String msg, String data) {
                if (code == Constant.Code.SUCCESS) {
                    mView.onVipHide(data);
                } else {
                    mView.showToastTip(false,msg);
                }
            }
        });
    }

    @Override
    public void doVipUp(int levelId) {
        Api_User.ins().doVipUp(levelId, new JsonCallback<String>() {
            @Override
            public void onSuccess(int code, String msg, String data) {
                if (code == Constant.Code.SUCCESS) {
                    mView.onVipUp(data);
                    mView.showToastTip(true,"Thăng cấp thành công");
                } else {
                    mView.showToastTip(false,msg);
                }
            }
        });
    }

    @Override
    public void doVipPay(int levelId) {
        Api_User.ins().doReNewVip(levelId, new JsonCallback<String>() {
            @Override
            public void onSuccess(int code, String msg, String data) {
                if (code == Constant.Code.SUCCESS) {
                    mView.showToastTip(true,"Gia hạn thành công");
                    mView.onVipPay(data);
                } else {
                    mView.showToastTip(false,msg);
                }
            }
        });
    }
}
