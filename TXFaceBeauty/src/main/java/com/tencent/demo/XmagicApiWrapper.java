package com.tencent.demo;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.tencent.demo.module.XmagicResParser;
import com.tencent.demo.module.XmagicUIProperty;
import com.tencent.demo.panel.XmagicPanelDataManager;
import com.tencent.demo.widget.XmagicToast;
import com.tencent.xmagic.XmagicApi;
import com.tencent.xmagic.XmagicProperty;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Iterator;
import java.util.List;

public class XmagicApiWrapper {

    private static String TAG = XmagicApiWrapper.class.getSimpleName();

    public static void checkBeautyAuth(XmagicApi mXmagicApi, OnCheckBeautyAuthComplete onCheckBeautyAuthComplete) {
        new Thread(() -> {
            List<XmagicProperty<?>> motionProperties = XmagicPanelDataManager.getInstance().getMotionXmagicProperties();
            if (motionProperties != null) {
                //本地集成资源检测兼容性方案
                //将动效资源列表传入sdk中做检测，执行后XmagicProperty.isSupport字段标识该原子能力是否可用。
                //针对不支持本设备的设备进行检索，根据XmagicProperty.isSupport 可UI层控制点击限制，或者直接从资源列表删除
                // Check if the phone supports motion
                mXmagicApi.isDeviceSupport(motionProperties);
                for (XmagicProperty<?> property : motionProperties) {//兼容性检测结果debug
                    Log.d("LocalIsDeviceSupport", "id=" + property.id + "-----IsSupport=" + property.isSupport);
                }
            }
            //检测美颜权限是否可用 Check if beauty permission is available
            List<XmagicProperty<?>> beautyProperties = XmagicPanelDataManager.getInstance().getBeautyXmagicProperties();
            // 开始验证，结果会赋值给XmagicProperty.isAuth字段 start check, the result will be assigned to the XmagicProperty.isAuth field
            mXmagicApi.isBeautyAuthorized(beautyProperties);
            //移除无权限的美颜项  Remove unauthorized beauty items
            List<XmagicUIProperty<?>> beautyList = XmagicPanelDataManager.getInstance().getProperties(XmagicUIProperty.UICategory.BEAUTY);
            if (beautyList != null && beautyList.size() > 0) {
                removeItem(beautyList.iterator());
            }
            //检测美体权限是否可用  Check if body permission is available
            List<XmagicProperty<?>> bodyProperties = XmagicPanelDataManager.getInstance().getBodyXmagicProperties();
            //开始验证，结果会赋值给XmagicProperty.isAuth字段  start check, the result will be assigned to the XmagicProperty.isAuth field
            mXmagicApi.isBeautyAuthorized(bodyProperties);
            //移除无权限的美体项  Remove unauthorized body items
            List<XmagicUIProperty<?>> bodyList = XmagicPanelDataManager.getInstance().getProperties(XmagicUIProperty.UICategory.BODY_BEAUTY);
            if (bodyList != null && bodyList.size() > 0) {
                removeItem(bodyList.iterator());
            }
            new Handler(Looper.getMainLooper()).post(() -> onCheckBeautyAuthComplete.onCheckComplete());
        }).start();


    }

    private static void removeItem(Iterator<XmagicUIProperty<?>> iterator) {
        while (iterator.hasNext()) {
            XmagicUIProperty<?> data = iterator.next();
            if (data != null && data.property != null && !data.property.isAuth) {
                iterator.remove();
            } else if (data != null && data.xmagicUIPropertyList != null && data.xmagicUIPropertyList.size() > 0) {
                Iterator<? extends XmagicUIProperty<?>> iteratorItem = data.xmagicUIPropertyList.iterator();
                while (iteratorItem.hasNext()) {
                    XmagicUIProperty<?> property = iteratorItem.next();
                    if (property != null && property.property != null && !property.property.isAuth) {
                        iteratorItem.remove();
                        if (data.xmagicUIPropertyList.size() == 0) {
                            iterator.remove();
                        }
                    }
                }
            }
        }
    }


    public static XmagicApi createXmagicApi(Context context, boolean isAddDefaultBeauty, XmagicApi.OnXmagicPropertyErrorListener xmagicListener) {
        XmagicApi commonXmagicApi = new XmagicApi(context, XmagicResParser.getResPath(), xmagicListener);
        //开发调试时，可以把日志级别设置为DEBUG，发布包请设置为 WARN，否则会影响性能
        commonXmagicApi.setXmagicLogLevel(Log.WARN);
        if (isAddDefaultBeauty) {
            commonXmagicApi.updateProperties(XmagicPanelDataManager.getInstance().getDefaultBeautyData());
        }
//        commonXmagicApi.setAIDataListener(new XmagicApi.XmagicAIDataListener() {
//            @Override
//            public void onBodyDataUpdated(Object bodyDataList) {
////                Log.d(TAG, "onBodyDataUpdated");
//            }
//
//            @Override
//            public void onHandDataUpdated(Object handDataList) {
////                Log.d(TAG, "onHandDataUpdated");
//            }
//
//            @Override
//            public void onFaceDataUpdated(Object faceDataList) {
//                //日志太多，影响性能，注释掉，需要时再打开
////                Log.d(TAG, "onFaceDataUpdated");
//            }
//        });
//
//        commonXmagicApi.setYTDataListener(new XmagicApi.XmagicYTDataListener() {
//            @Override
//            public void onYTDataUpdate(String data) {
////                Log.d(TAG, "onYTDataUpdate，data=" + data);
//                //log太长，需要时请打开下面的开关，写入文件，便于测试
//                if (false) {
//                    writeFaceData(data);
//                }
//            }
//        });

        commonXmagicApi.setTipsListener(new XmagicApi.XmagicTipsListener() {
            final XmagicToast mToast = new XmagicToast();

            @Override
            public void tipsNeedShow(String tips, String tipsIcon, int type, int duration) {
                mToast.show(context, tips, duration);
            }

            @Override
            public void tipsNeedHide(String tips, String tipsIcon, int type) {
                mToast.dismiss();
            }
        });
        return commonXmagicApi;
    }


    public interface OnCheckBeautyAuthComplete {
        void onCheckComplete();
    }
}
