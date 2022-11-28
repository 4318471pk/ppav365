package com.tencent.demo.avatar;

import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tencent.demo.XMagicImpl;
import com.tencent.demo.avatar.model.AvatarItem;
import com.tencent.demo.avatar.model.BindData;
import com.tencent.demo.avatar.model.MainTab;
import com.tencent.demo.avatar.model.SubTab;
import com.tencent.demo.log.LogUtils;
import com.tencent.demo.module.XmagicResParser;
import com.tencent.xmagic.listener.UpdatePropertyListener;
import com.tencent.xmagic.XmagicApi;
import com.tencent.xmagic.XmagicProperty;
import com.tencent.xmagic.avatar.AvatarCategory;
import com.tencent.xmagic.avatar.AvatarData;
import com.tencent.xmagic.util.FileUtil;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 使用avatar素材的辅助类
 * 用于加载avatar素材
 * 用于获取面板数据
 * 用于从面板数据中获取需要保存的数据
 */
public class AvatarResManager {

    public static final String AVATAR_RES_MALE = "avatar_v2.0_male";
    public static final String AVATAR_RES_FEMALE = "avatar_v2.0_female";
    public static final String AVATAR_RES_BODY = "ch_0705_1";


    private static final String TAG = "AvatarResManager";

    //存放状态信息和面板信息的文件夹，面板信息存放的是.json文件  状态信息存放的是.txt文件
    private static final String XMAGIC_AVATAR_DATA = "xmagic_avatar_data";

    //assets中存放面板数据的文件夹
    private static final String PANEL_JSON_DIR_NAME = "avatar_panel";

    private static final String PANEL_JSON_FILE_NAME = "_panel.json";

    //avatar素材文件夹地址
    private String avatarResPath = null;

    private Map<String, List<AvatarData>> avatarTypeMap = new ConcurrentHashMap<>();
    private final Handler handler = new android.os.Handler(Looper.getMainLooper());

    private AvatarResManager() {
        avatarResPath = XmagicResParser.getResPath() + "MotionRes/avatarRes/";
    }

    static class ClassHolder {

        static final AvatarResManager AVATAR_RES_MANAGER = new AvatarResManager();
    }

    public static AvatarResManager getInstance() {
        return ClassHolder.AVATAR_RES_MANAGER;
    }


    public String getAvatarResPath() {
        return avatarResPath;
    }

    /**
     * 用于加载Avatar 资源
     *
     * @param xmagicApi      XmagicApi对象
     * @param avatarResName  名称
     * @param avatarSaveData 加载模型的默认配置，如果没有则传null
     */
    public void loadAvatarRes(XmagicApi xmagicApi, String avatarResName, String avatarSaveData) {
        loadAvatarRes(xmagicApi,avatarResName,avatarSaveData,new UpdatePropertyListener() {
            final Gson gson = new Gson();

            @Override
            public void onAvatarCustomConfigParsingFailed(List<XmagicProperty<?>> parsingFailed) {
                LogUtils.e(TAG, "onAvatarCustomConfigParsingFailed:" + gson.toJson(parsingFailed));
            }

            @Override
            public void onPropertyInvalid(List<XmagicProperty<?>> invalidList) {
                LogUtils.e(TAG, "onPropertyInvalid  " + gson.toJson(invalidList));
            }

            @Override
            public void onPropertyNotSupport(List<XmagicProperty<?>> notSupportList) {
                Toast.makeText(XMagicImpl.applicationContext, "this device is not support avatar ", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onAvatarDataInvalid(List<AvatarData> failedAvatarDatas) {
                LogUtils.e(TAG, "onAvatarDataInvalid  " + gson.toJson(failedAvatarDatas));
            }

            @Override
            public void onAssetLoadFinish(String resPath, boolean success) {
                LogUtils.d(TAG, "onAssetLoadFinish, resPath=" + resPath + ",success=" + success);
            }
        });
    }


    /**
     * 用于加载Avatar 资源
     *
     * @param xmagicApi      XmagicApi对象
     * @param avatarResName  名称
     * @param avatarSaveData 加载模型的默认配置，如果没有则传null
     * @param updatePropertyListener 素材加载回调
     */
    public void loadAvatarRes(XmagicApi xmagicApi, String avatarResName, String avatarSaveData, UpdatePropertyListener updatePropertyListener) {
        String avatarAssetPath = avatarResPath + avatarResName;
        XmagicProperty<?> property = new XmagicProperty<>(XmagicProperty.Category.MOTION, avatarResName, avatarAssetPath, null, null);
        property.customConfigs = avatarSaveData;
        LogUtils.d(TAG,"start load avatar");
        xmagicApi.loadAvatar(property, updatePropertyListener);
    }


    /**
     * 获取avatar面板数据，
     *
     * @param avatarResName      avatar素材名称
     * @param avatarDataCallBack 由于此方法会访问文件，所以会在子线程中进行文件操作，获取到数据后会在主线程回调
     *                           返回的数据是已经包含了resources文件夹下的数据
     */
    public void getAvatarData(String avatarResName, String avatarSaveData, LoadAvatarDataCallBack avatarDataCallBack) {
        new Thread(() -> {
            long time = System.currentTimeMillis();
            List<MainTab> tabList = loadAvatarPanelData(avatarResName, avatarSaveData);;
            LogUtils.d(TAG, "getAvatarData time :" + (System.currentTimeMillis() - time));
            handler.post(() -> {
                if (avatarDataCallBack != null) {
                    avatarDataCallBack.onLoaded(tabList);
                }
            });
        }).start();
    }

    public interface LoadAvatarDataCallBack {
        void onLoaded(List<MainTab> allData);
    }



    private String getAvatarPanelFilePath(String avatarResName) {
        String panelFileDir = AvatarResManager.getAvatarConfigsDir();
        if (!isChinese()) {
            avatarResName = "EN_" + avatarResName;
        }
        String panelFilePath = panelFileDir + File.separator + avatarResName + PANEL_JSON_FILE_NAME;
        if (new File(panelFilePath).exists()) {
            return panelFilePath;
        }
        File panelDir = new File(panelFileDir);
        if (!panelDir.exists()) {
            panelDir.mkdirs();
        }
        //开始从assets中复制文件
        FileUtil.copyAssetFile(XMagicImpl.applicationContext, PANEL_JSON_DIR_NAME + File.separator + avatarResName + PANEL_JSON_FILE_NAME, panelFilePath);
        return panelFilePath;
    }

    private List<MainTab> loadAvatarPanelData(String avatarResName, String avatarSaveData) {
        String path = getAvatarPanelFilePath(avatarResName);
        String configStr = FileUtil.readFile(path);
        List<MainTab> mainTabList = null;
        if (!TextUtils.isEmpty(configStr)) {
            Type type = new TypeToken<List<MainTab>>() {
            }.getType();
            mainTabList = new Gson().fromJson(configStr, type);
            if (mainTabList == null) {
                return null;
            }
            //通过SDK方法获取素材属性信息
            Map<String, List<AvatarData>> avatarMap = XmagicApi.getAvatarConfig(avatarResPath + avatarResName, avatarSaveData);
            if (avatarMap == null) {
                return null;
            }
            Map<String, Map<String, AvatarData>> convertMap = convertAvatarMap(avatarMap);
            for (MainTab mainTab : mainTabList) {
                for (SubTab subTab : mainTab.subTabs) {
                    produceAvatarIcons(subTab.items, subTab.category, subTab.type, convertMap);
                }
            }
            List<AvatarData> planeAvatarList = avatarMap.get(AvatarCategory.BACKGROUND_PLANE);
            if (planeAvatarList != null) {
                avatarTypeMap.remove(avatarResName);
                avatarTypeMap.put(avatarResName, planeAvatarList);
            }
        }
        return mainTabList;
    }

    private Map<String, Map<String, AvatarData>> convertAvatarMap(Map<String, List<AvatarData>> avatarMap) {
        Map<String, Map<String, AvatarData>> resultDataMap = new ArrayMap<>();
        for (Map.Entry<String, List<AvatarData>> entry : avatarMap.entrySet()) {
            if (entry == null || TextUtils.isEmpty(entry.getKey())) {
                continue;
            }
            Map<String, AvatarData> avatarDataMap = new ArrayMap<>();
            for (AvatarData avatarData : entry.getValue()) {
                avatarDataMap.put(avatarData.id, avatarData);
            }
            resultDataMap.put(entry.getKey(), avatarDataMap);
        }
        return resultDataMap;
    }

    private void produceAvatarIcons(List<AvatarItem> avatarItems, String category, int UiType, Map<String, Map<String, AvatarData>> convertMap) {
        if (avatarItems == null) {
            return;
        }
        Map<String, AvatarData> avatarDataMap = convertMap.get(category);
        for (AvatarItem avatarItem : avatarItems) {
            if (avatarItem == null) {
                continue;
            }
            avatarItem.category = category;
            avatarItem.type = UiType;
            if (avatarDataMap == null) {
                continue;
            }
            AvatarData avatarData = avatarDataMap.get(avatarItem.id);
            if (avatarData != null) {
                avatarItem.avatarData = avatarData;
                avatarItem.selected = avatarData.selected;
            }
            if (avatarItem.bindData != null) {
                produceBindData(avatarItem.bindData, convertMap);
            }
        }
    }


    private void produceBindData(List<BindData> bindDataList, Map<String, Map<String, AvatarData>> convertMap) {
        for (BindData bindData : bindDataList) {
            if (bindData == null || TextUtils.isEmpty(bindData.category) || TextUtils.isEmpty(bindData.id)) {
                continue;
            }
            Map<String, AvatarData> avatarDataList = convertMap.get(bindData.category);
            if (avatarDataList == null) {
                continue;
            }
            bindData.avatarData = avatarDataList.get(bindData.id);
        }
    }


    /**
     * 获取对应的plane Config数据
     *
     * @param avatarResName 资源名称
     * @param avatarBgType  背景类型
     * @return
     */
    public AvatarData getAvatarPlaneTypeConfig(String avatarResName, AvatarBgType avatarBgType) {
        if (avatarTypeMap != null && !TextUtils.isEmpty(avatarResName)) {
            List<AvatarData> avatarDataList = avatarTypeMap.get(avatarResName);
            if (avatarDataList == null) {
                return null;
            }
            for (AvatarData avatarData : avatarDataList) {
                if (avatarData.id.equals(avatarBgType.getPlaneId())) {
                    return avatarData;
                }
            }
        }
        return null;
    }


    public enum AvatarBgType {
        VIRTUAL_BG {
            public String getPlaneId() {
                return "1549ef04ca0c5110389a684dfe95ad9a";
            }
        }, REAL_BG {
            public String getPlaneId() {
                return "none";
            }
        };

        public String getPlaneId() {
            return "none";
        }
    }


    public enum AvatarGender {
        MALE, FEMALE
    }


    /**
     * 从面板的配置文件中解析出用户设置的属性或默认属性
     *
     * @return
     */
    public static List<AvatarData> getUsedAvatarData(List<MainTab> mainTabList) {
        if (mainTabList == null) {
            return null;
        }

        List<AvatarData> relatedList = new ArrayList<>();
        List<AvatarData> commonList = new ArrayList<>();
        for (MainTab mainTab : mainTabList) {
            if (mainTab == null || mainTab.subTabs == null || mainTab.subTabs.size() == 0) {
                continue;
            }
            for (SubTab subTab : mainTab.subTabs) {
                if (subTab == null || TextUtils.isEmpty(subTab.label) || subTab.items == null || subTab.items.size() == 0) {
                    continue;
                }
                getUsedAvatarData(subTab, commonList, relatedList);
            }
        }
        List<AvatarData> resultList = new ArrayList<>();
        resultList.addAll(relatedList);
        resultList.addAll(commonList);
        return resultList;
    }


    private static void getUsedAvatarData(SubTab subTab, List<AvatarData> commonList, List<AvatarData> relatedList) {
        for (AvatarItem avatarItem : subTab.items) {
            if (avatarItem == null) {
                continue;
            }
            if (avatarItem.avatarData == null) {
                continue;
            }
            if (AvatarData.TYPE_SELECTOR == avatarItem.type && avatarItem.selected) {
                if (!TextUtils.isEmpty(subTab.relatedCategory)) {  //此种情况为依赖项
                    relatedList.add(avatarItem.avatarData);
                } else {
                    commonList.add(avatarItem.avatarData);
                }
                List<AvatarData> bindAvatarData = getAvatarDataByBindData(avatarItem.bindData);
                if (bindAvatarData != null && bindAvatarData.size() > 0) {
                    commonList.addAll(bindAvatarData);
                }
            } else if (AvatarData.TYPE_SLIDER == avatarItem.type) {
                commonList.add(avatarItem.avatarData);
            }
        }
    }

    public static String getAvatarConfigsDir() {
        String dir = XMagicImpl.applicationContext.getFilesDir() + File.separator + XMAGIC_AVATAR_DATA;
        File dirFile = new File(dir);
        if (!dirFile.exists()) {
            dirFile.mkdirs();
        }
        return dir;
    }

    public static String getAvatarConfigsFileName(String avatarResName) {
        return avatarResName + ".txt";
    }


    /**
     * 从bindData中解析出对应的avatarData
     *
     * @param bindDataList 绑定管理列表
     * @return 返回对应的avatarData数据列表
     */
    public static List<AvatarData> getAvatarDataByBindData(List<BindData> bindDataList) {
        if (bindDataList == null || bindDataList.size() == 0) {
            return null;
        }
        List<AvatarData> resultData = new ArrayList<>();
        for (BindData bindData : bindDataList) {
            if (bindData == null || TextUtils.isEmpty(bindData.category) || TextUtils.isEmpty(bindData.id) || bindData.avatarData == null) {
                continue;
            }
            resultData.add(bindData.avatarData);
        }
        return resultData;
    }



    /**
     * 判断是否是中文
     * @return
     */
    private boolean isChinese(){
        return XMagicImpl.applicationContext.getResources().getConfiguration().locale.getCountry().equals("CN");
    }

}
