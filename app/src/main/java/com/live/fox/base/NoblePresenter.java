package com.live.fox.base;

import com.live.fox.Constant;
import com.live.fox.contract.NobleContract;
import com.live.fox.server.Api_User;
import com.live.fox.common.JsonCallback;

public class NoblePresenter extends NobleContract.Presenter {

    @Override
    public void doBuyVip(int leveId) {
        Api_User.ins().doBuyVip(leveId, new JsonCallback<String>() {
            @Override
            public void onSuccess(int code, String msg, String data) {
                mView.onBuyVip(data);
                if (code == Constant.Code.SUCCESS) {
                    mView.onBuyVip(data);

                } else {
                    mView.onError(msg);
                }
            }
        });
    }


}
