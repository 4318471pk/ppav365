package com.live.fox.network;


import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.live.fox.R;
import com.live.fox.utils.LogUtils;

import java.util.logging.Level;
import java.util.logging.Logger;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;


public abstract class BaseObserver<T> implements Observer<NetworkBaseEntity<T>> {

    private final String TAG = "BaseObserver";
    private final int success = 0;
    private final int paramErr = 1;
    private final int reLogin = 3;
    private Logger mLogger;
    private AppCompatActivity mActivity;
    private Gson mGson;

    public BaseObserver(AppCompatActivity activity) {
        mActivity = activity;
        mGson = new Gson();
        mLogger = Logger.getLogger("BaseObserver");
    }

    @Override
    public void onSubscribe(Disposable d) {
    }

    @Override
    public void onComplete() {
        mLogger.log(Level.ALL, "onComplete");
    }

    @Override
    public void onNext(NetworkBaseEntity<T> baseEntity) {
        if (baseEntity != null) {
            switch (baseEntity.getCode()) {
                case success:  // 请求成功
                    mLogger.log(Level.FINE, "网络请求成功");
                    T t = baseEntity.getData();
                    if (t != null) {
                        onSuccess(t);
                    } else {
                        LogUtils.d(TAG, "获取数据成：data 数据解析失败");
                    }
                    break;
                case paramErr:
                    LogUtils.d(TAG, baseEntity.getMsg());
                    break;
                case reLogin:   //重新登录
                    showToast(mActivity.getString(R.string.app_user_re_login));
                    break;
                default:
                    showToast(baseEntity.getMsg());
                    break;
            }
        } else {
            LogUtils.d(TAG, "数据获取失败");
        }
    }

    @Override
    public void onError(Throwable e) {
        e.printStackTrace();
        showToast(NetworkException.getExceptInfo(e, mActivity));
    }

    /**
     * 显示
     *
     * @param string 显示内容
     */
    private void showToast(String string) {
        Snackbar.make(
                mActivity.getWindow().getDecorView(),
                string,
                Snackbar.LENGTH_SHORT
        ).show();
    }

    public abstract void onSuccess(T t);
}
