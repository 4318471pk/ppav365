package com.live.fox.ui.circle;

import android.content.Context;
import android.webkit.JavascriptInterface;

import com.live.fox.utils.LogUtils;


public class JavaScriptInterface {

    private Context context;

    private OnJsCallListener jsCallListener;


    public JavaScriptInterface(Context c) {
        context = c;
    }

    public JavaScriptInterface(Context c, OnJsCallListener jsCallListener) {
        context = c;
        this.jsCallListener = jsCallListener;
    }

    /**
     * 与js交互时用到的方法，在js里直接调用的
     */
    @JavascriptInterface
    public void androidRecharge(int type, String data) {
        LogUtils.e(type + "," + data);
    }
//
//
//    public void isLive(String liveId) {
//        Anchor anchorInfo = new Anchor();
//        anchorInfo.setLiveId(Integer.valueOf(liveId));
//        PlayerActivity.noticestart((Activity) context, anchorInfo);
//    }
//
//    public void Follow(String data) {
//        Api_Live.ins().follow( data + "", true, new JsonCallback<String>() {
//            @Override
//            public void onSuccess(int code, String msg, String result) {
//                LogUtils.e("follow result : " + result);
//                if (code == HttpResponseCode.HTTP_SUCCESS) {
//                }
//            }
//        });
//    }
//

    @JavascriptInterface
    public void onJsCalled(int type, String data) {
        if(jsCallListener!=null){
            jsCallListener.onJsCalled(type, data);
        }
    }

    @JavascriptInterface
    public void androidRecharge() {
        LogUtils.e("androidRecharge");
    }


    public interface OnJsCallListener {
        void onJsCalled(int type, String data);
    }


}
