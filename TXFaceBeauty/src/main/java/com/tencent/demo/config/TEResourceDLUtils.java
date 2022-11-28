package com.tencent.demo.config;

import android.content.Context;
import android.content.res.AssetManager;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.tencent.demo.download.MotionDLModel;
import com.tencent.xmagic.util.FileUtil;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * 需要下载的资源信息
 */
public class TEResourceDLUtils {


    private static TEDLResModel tedlResModel = null;


    private static void readMotions(Context context) {
        if (tedlResModel == null) {
            String motionsFileName = "";
            AssetManager assetManager = context.getAssets();
            try {
                String[] names = assetManager.list("json");
                if (names != null && names.length > 0) {
                    Arrays.sort(names);   //进行升序排列
                    for (String name : names) {
                        motionsFileName = name;
                    }
                }
            } catch (Exception e) {
                motionsFileName = "";
            }
            String xmagic_motions_str_json = "{}";
            if (!TextUtils.isEmpty(motionsFileName)) {
                xmagic_motions_str_json = FileUtil.readAssetFile(context, "json/" + motionsFileName);
            }
            tedlResModel = new Gson().fromJson(xmagic_motions_str_json.trim(), TEDLResModel.class);
        }
    }


    public static List<MotionDLModel> getTEResDLModel(Context context, TEResourceType type) {
        readMotions(context);
        List<String> resList = null;
        switch (type) {
            case MotionRes2D:
                resList = tedlResModel.motion2d;
                break;
            case MotionRes3D:
                resList = tedlResModel.motion3d;
                break;
            case MotionResHand:
                resList = tedlResModel.motionhand;
                break;
            case MotionResGan:
                resList = tedlResModel.motiongan;
                break;
            case MotionResMakeup:
                resList = tedlResModel.motionmakeup;
                break;
            case MotionResSegment:
                resList = tedlResModel.motionseg;
                break;
            case Lut:
                resList = tedlResModel.lut;
                break;
        }
        return addModelList(type.getName(), resList);
    }

    /**
     * 用于构造下载对象
     *
     * @param category
     * @param motionList
     * @return
     */
    private static List<MotionDLModel> addModelList(String category, List<String> motionList) {
        List<MotionDLModel> list = new ArrayList<>();
        if (motionList != null) {
            if (TEResourceType.Lut.getName().equals(category)) {
                for (int i = 0; i < motionList.size(); i++) {
                    list.add(new MotionDLModel(category, motionList.get(i), tedlResModel.lutBaseUrl + motionList.get(i)));
                }
            } else {
                for (int i = 0; i < motionList.size(); i++) {
                    list.add(new MotionDLModel(category, motionList.get(i), tedlResModel.motionBaseUrl + motionList.get(i) + ".zip"));
                }
            }
        }
        return list;
    }




    public static String getMotionIconUrlByName(Context context, String resourceName) {
        readMotions(context);
        String ICON_URL_BASE = tedlResModel.motionIconBaseUrl;
        if (!TextUtils.isEmpty(resourceName)) {
            return ICON_URL_BASE + resourceName + ".png";
        }
        return null;
    }

    public static String getLutIconUrlByName(Context context, String resourceName) {
        readMotions(context);
        String ICON_URL_BASE = tedlResModel.lutIconBaseUrl;
        if (!TextUtils.isEmpty(resourceName)) {
            return ICON_URL_BASE + resourceName ;
        }
        return null;
    }


    static class TEDLResModel {
        public String lutBaseUrl;
        public String lutIconBaseUrl;
        public String motionBaseUrl;
        public String motionIconBaseUrl;
        public List<String> motion2d;
        public List<String> motion3d;
        public List<String> motionhand;
        public List<String> motiongan;
        public List<String> motionmakeup;
        public List<String> motionseg;
        public List<String> lut;

    }
}
