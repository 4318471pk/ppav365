package com.tencent.demo.module;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.tencent.demo.R;
import com.tencent.demo.config.TEResourceDLUtils;
import com.tencent.demo.config.TEResourceType;
import com.tencent.demo.download.MotionDLModel;
import com.tencent.demo.module.XmagicUIProperty.UICategory;
import com.tencent.demo.panel.XmagicPanelDataManager;
import com.tencent.xmagic.XmagicApi;
import com.tencent.xmagic.XmagicConstant;
import com.tencent.xmagic.XmagicConstant.BeautyConstant;
import com.tencent.xmagic.XmagicProperty;
import com.tencent.xmagic.XmagicProperty.XmagicPropertyValues;
import com.tencent.xmagic.util.FileUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 美颜数据封装示例代码类。
 * 注意事项：
 * 本类数据构造默认会通过工程内所持有的原子能力资源来构造全量美颜能力与分类。使用者无需根据自己使用的套餐再做代码调整。
 * 不同套餐对应的原子能力明细如下：
 * <p>
 * A1-00
 * 基础美颜：美白、磨皮、红润
 * 滤镜特效
 * ***********************************************************************************
 * A1-01
 * 基础美颜：美白、磨皮、红润
 * 画面调整：对比度、饱和度、清晰度
 * 高级美颜(人像属性识别能力)：大眼、瘦脸（自然、女神、英俊）
 * 滤镜特效
 * ***********************************************************************************
 * A1-02
 * 涵盖A1-01所有功能
 * 2D特效能力、2D特效素材资源包
 * ***********************************************************************************
 * A1-03
 * 涵盖A1-01所有功能
 * 2D特效能力、2D特效素材资源包
 * 高级美颜(人像属性识别能力)：增加窄脸/下巴/发际线/瘦鼻
 * ***********************************************************************************
 * A1-04
 * 涵盖A1-01所有功能
 * 2D特效能力、2D特效素材资源包
 * 手势识别能力、手势特效素材资源包
 * ***********************************************************************************
 * A1-05
 * 涵盖A1-01所有功能
 * 2D特效能力、2D特效素材资源包
 * 人像分割所有能力、人像分割特效
 * ***********************************************************************************
 * A1-06
 * 涵盖A1-01所有功能
 * 2D特效能力、2D特效素材资源包
 * 美妆能力、美妆特效素材资源包
 * **********************************************************************************
 * S1-00
 * 基础美颜美颜：美白、磨皮、红润
 * 画面调整：对比度、饱和度、锐化
 * 高级美颜(人像属性识别能力)：大眼、窄脸、瘦脸（自然、女神、英俊）、V 脸、下巴、短脸、脸型、发际线、亮眼、眼距、眼角、瘦鼻、鼻翼、鼻子位置、白牙、祛皱、祛法令纹、祛眼袋、嘴型、嘴唇厚度、口红、腮红、立体
 * 滤镜特效
 * ***********************************************************************************
 * S1-01
 * 基础美颜美颜：美颜：美白、磨皮、红润
 * 画面调整：对比度、饱和度、锐化
 * 高级美颜(人像属性识别能力)：大眼、窄脸、瘦脸（自然、女神、英俊）、V 脸、下巴、短脸、脸型、发际线、亮眼、眼距、眼角、瘦鼻、鼻翼、鼻子位置、白牙、祛皱、祛法令纹、祛眼袋、嘴型、嘴唇厚度、口红、腮红、立体
 * 滤镜特效
 * 2D特效能力、2D特效素材资源包
 * 3D特效能力、3D特效素材资源包
 * 美妆能力、美妆特效素材资源包
 * ***********************************************************************************
 * S1-02
 * 涵盖S1-01所有功能
 * 手势识别能力、手势特效素材资源包
 * ***********************************************************************************
 * S1-03
 * 涵盖S1-01所有功能
 * 人像分割所有能力、人像分割特效素材资源包
 * ***********************************************************************************
 * S1-04
 * 涵盖S1-01所有功能
 * 手势识别能力、手势特效素材资源包
 * 人像分割所有能力、人像分割特效
 * <p>
 * <p>
 * Sample code class for beauty data encapsulation.
 * Precautions:
 * By default, this type of data structure will use the atomic power resources held in the project to construct
 * the full amount of beauty capabilities and classifications. Users do not need to make
 * code adjustments according to the packages they use.
 * The atomic capabilities corresponding to different packages are detailed as follows:
 * <p>
 * A1-00
 * Basic beauty: whitening, microdermabrasion, ruddy
 * Filter effects
 * ************************************************ ************************************
 * A1-01
 * Basic beauty: whitening, microdermabrasion, ruddy
 * Picture adjustment: contrast, saturation, sharpness
 * Advanced beauty (portrait attribute recognition ability): big eyes, thin face (natural, goddess, handsome)
 * Filter effects
 * ************************************************ ************************************
 * A1-02
 * Covers all functions of A1-01
 * 2D special effect capability, 2D special effect material resource package
 * ************************************************ ************************************
 * A1-03
 * Covers all functions of A1-01
 * 2D special effect capability, 2D special effect material resource package
 * Advanced beauty (portrait attribute recognition ability): increase narrow face/chin/hairline/slim nose
 * ************************************************ ************************************
 * A1-04
 * Covers all functions of A1-01
 * 2D special effect capability, 2D special effect material resource package
 * Gesture recognition ability, gesture special effect resource package
 * ************************************************ ************************************
 * A1-05
 * Covers all functions of A1-01
 * 2D special effect capability, 2D special effect material resource package
 * Portrait segmentation all abilities, portrait segmentation effects
 * ************************************************ ************************************
 * A1-06
 * Covers all functions of A1-01
 * 2D special effect capability, 2D special effect material resource package
 * Resource packs for beauty abilities and beauty effects
 * ************************************************ ********************************
 * S1-00
 * Basic beauty beauty: whitening, microdermabrasion, ruddy
 * Picture adjustment: contrast, saturation, sharpening
 * Advanced beauty (portrait attribute recognition ability): big eyes, narrow face,
 * thin face (natural, goddess, handsome), V face, chin, short face, face shape, hairline, bright eyes,
 * eye distance, eye corners, thin nose , nose wing, nose position, white teeth, wrinkle removal,
 * nasolabial wrinkle removal, eye bag removal, mouth shape, lip thickness, lipstick, blush, three-dimensional
 * Filter effects
 * ************************************************ ************************************
 * S1-01
 * Basic beauty beauty: beauty: whitening, microdermabrasion, rosy
 * Picture adjustment: contrast, saturation, sharpening
 * Advanced beauty (portrait attribute recognition ability): big eyes, narrow face,
 * thin face (natural, goddess, handsome), V face, chin, short face, face shape, hairline,
 * bright eyes, eye distance, eye corners, thin nose , nose wing, nose position, white teeth,
 * wrinkle removal, nasolabial wrinkle removal, eye bag removal, mouth shape, lip thickness,
 * lipstick, blush, three-dimensional
 * Filter effects
 * 2D special effect capability, 2D special effect material resource package
 * 3D special effect capability, 3D special effect material resource package
 * Resource packs for beauty abilities and beauty effects
 * ************************************************ ************************************
 * S1-02
 * Covers all functions of S1-01
 * Gesture recognition ability, gesture special effect resource package
 * ************************************************ ************************************
 * S1-03
 * Covers all functions of S1-01
 * All abilities of portrait segmentation, portrait segmentation special effects resource package
 * ************************************************ ************************************
 * S1-04
 * Covers all functions of S1-01
 * Gesture recognition ability, gesture special effect resource package
 * Portrait segmentation all abilities, portrait segmentation effects
 */
public class XmagicResParser {

    private static final String TAG = XmagicResParser.class.getSimpleName();

    /**
     * 约定以 "/" 结尾, 方便拼接
     * xmagic resource local path
     */
    private static String sResPath;


    /**
     * 直接使用此类的方法，需要注意使用顺序
     * 1. 调用setResPath（）设置存放资源的路径
     * 2. copyRes(Context context) 将asset中的资源文件复制到 第一步设置的路径下
     * 3. 调用parseRes()方法对资源进行分类处理
     * 4. 之后就可以使用XmagicPanelView和XmagicPanelDataManager.getInstance()类的方法
     * Direct use of such methods, need to pay attention to the order of use
     * 1. Call setResPath() to set the path for storing resources
     * 2. copyRes(Context context) Copy the resource file in the asset to the path set in the first step
     * 3. Call the parseRes() method to classify the resources
     * 4. Then you can use the methods of XmagicPanelView and XmagicPanelDataManager.getInstance() classes
     */
    private XmagicResParser() {/*nothing*/}

    /**
     * 设置asset 资源存放的位置
     * set the asset path
     *
     * @param path
     */
    public static void setResPath(String path) {
        if (!path.endsWith(File.separator)) {
            path = path + File.separator;
        }
        sResPath = path;
    }

    public static String getResPath() {
        ensureResPathAlreadySet();
        return sResPath;
    }

//    public static void test2(XmagicApi commonXmagicApi){
//        //TODO 测试代码开始
//        Map<String,String> agents = new ArrayMap<>();
//        String resDir =XmagicResParser.getResPath();
//        agents.put(XmagicConstant.AgentType.BG_SEG_AGENT, resDir + "light_assets_test/models/LightSegmentBody.bundle");
//        agents.put(XmagicConstant.AgentType.HAIR_SEG_AGENT, resDir + "light_assets_test/models/LightSegmentHair.bundle");
//        agents.put(XmagicConstant.AgentType.HEAD_SEG_AGENT, resDir + "light_assets_test/models/LightSegmentHead.bundle");
//        agents.put(XmagicConstant.AgentType.HAND_AGENT, resDir + "light_assets_test/models/LightHandModel.bundle");
//        agents.put(XmagicConstant.AgentType.BODY_AGENT, resDir + "light_assets_test/models/LightBodyModel.bundle");
//        agents.put(XmagicConstant.AgentType.ACE_3D_AGENT, resDir + "light_assets_test");
//        commonXmagicApi.setBundleToLightEngine(agents, resDir, copyResult -> {
//            for (Map.Entry<String,Boolean> entry:copyResult.entrySet()){
//                Log.e("setBundleToLightEngine",entry.getKey()+"  "+entry.getValue());
//            }
//        });
//        //TODO 测试代码结束
//    }
//
//
//    public static void test(Context context){
//        //TODO 测试代码
//        for (String path : XmagicResourceUtil.AI_MODE_DIR_NAMES) {
//            boolean result=false;
//            if (XmagicResourceUtil.LIGHT_CORE.equals(path)) {
//                result = FileUtil.copyAssets(context, path, sResPath + File.separator + XmagicResourceUtil.DL_DIRECTORY_ASSETS_LIGHT_ASSETS);
//            } else {
//                result = FileUtil.copyAssets(context, path, sResPath + File.separator + XmagicResourceUtil.DL_DIRECTORY_ASSETS_LIGHT_ASSETS + "_test");
//            }
//        }
//    }

    /**
     * 从 apk 的 assets 解压资源文件到指定路径, 需要先设置路径: {@link #setResPath(String)} <br>
     * 首次安装 App, 或 App 升级后调用一次即可.
     * copy xmagic resource from assets to local path
     */
    public static boolean copyRes(Context context) {
        ensureResPathAlreadySet();

        int addResult = XmagicApi.addAiModeFilesFromAssets(context, sResPath);
        Log.e(TAG, "add ai model files result = " + addResult);
        for (String path : new String[]{"lut"}) {
            boolean result = FileUtil.copyAssets(context, path, sResPath + "light_material" + File.separator + path);
            if (!result) {
                Log.d(TAG, "copyRes: fail,path=" + path + ",new path=" + sResPath + "light_material" + File.separator + path);
                return false;
            }
        }

        for (String path : new String[]{"MotionRes"}) {
            boolean result = FileUtil.copyAssets(context, path, sResPath + path);
            if (!result) {
                Log.d(TAG, "copyRes: fail,path=" + path + ",new path=" + sResPath + path);
                return false;
            }
        }

        return true;
    }


    private static void ensureResPathAlreadySet() {
        if (TextUtils.isEmpty(sResPath)) {
            throw new IllegalStateException("resource path not set, call XmagicResParser.setResPath() first.");
        }
    }


    /**
     * 对已经复制到本地的资源进行分类处理
     * Classify resources that have been copied to the local
     */
    public static void parseRes(Context context) {
        File file = new File(sResPath);
        String[] list = file.list();
        if (!file.exists() || list == null || list.length == 0) {
            throw new IllegalStateException("resource dir not found or empty, call XmagicResParser.copyRes first.");
        }

        List<XmagicUIProperty<?>> beautyList = new ArrayList<>();
        XmagicPanelDataManager.getInstance().addAllDataItem(UICategory.BEAUTY, beautyList);
        parseBeauty(context, beautyList);

        List<XmagicUIProperty<?>> bodyBeautyList = new ArrayList<>();
        XmagicPanelDataManager.getInstance().addAllDataItem(UICategory.BODY_BEAUTY, bodyBeautyList);
        parseBodyBeauty(context, bodyBeautyList);

        List<XmagicUIProperty<?>> lutList = new ArrayList<>();
        XmagicPanelDataManager.getInstance().addAllDataItem(UICategory.LUT, lutList);
        parseLutProperty(context, lutList);

        List<XmagicUIProperty<?>> motionList = new ArrayList<>();
        XmagicPanelDataManager.getInstance().addAllDataItem(UICategory.MOTION, motionList);
        parseMotionData(context, motionList);

        List<XmagicUIProperty<?>> makeUpList = new ArrayList<>();
        XmagicPanelDataManager.getInstance().addAllDataItem(UICategory.MAKEUP, makeUpList);
        parseMakeUpData(context, makeUpList);

        List<XmagicUIProperty<?>> segList = new ArrayList<>();
        XmagicPanelDataManager.getInstance().addAllDataItem(UICategory.SEGMENTATION, segList);
        parseSegData(context, segList);

    }

    /**
     * 美体数据构造
     * create body data
     */
    private static void parseBodyBeauty(Context context, List<XmagicUIProperty<?>> allProperties) {
        allProperties.add(new XmagicUIProperty<>(UICategory.BODY_BEAUTY,
                context.getString(R.string.autohtin_body_strength_label), R.mipmap.body_autohtin_body_strength,
                BeautyConstant.BODY_AUTOTHIN_BODY_STRENGTH, new XmagicPropertyValues(0, 100, 0, 0, 1)));
        allProperties.add(new XmagicUIProperty<>(UICategory.BODY_BEAUTY,
                context.getString(R.string.body_leg_stretch_label), R.mipmap.body_leg_stretch,
                BeautyConstant.BODY_LEG_STRETCH, new XmagicPropertyValues(0, 100, 0, 0, 1)));
        allProperties.add(new XmagicUIProperty<>(UICategory.BODY_BEAUTY,
                context.getString(R.string.body_slim_leg_strength_label), R.mipmap.body_slim_leg_strength,
                BeautyConstant.BODY_SLIM_LEG_STRENGTH, new XmagicPropertyValues(0, 100, 0, 0, 1)));
        allProperties.add(new XmagicUIProperty<>(UICategory.BODY_BEAUTY,
                context.getString(R.string.body_waish_strength_label), R.mipmap.body_waish_strength,
                BeautyConstant.BODY_WAIST_STRENGTH, new XmagicPropertyValues(0, 100, 0, 0, 1)));
        allProperties.add(new XmagicUIProperty<>(UICategory.BODY_BEAUTY,
                context.getString(R.string.body_thin_shoulder_strength_label), R.mipmap.body_thin_shoulder_strength, BeautyConstant.BODY_THIN_SHOULDER_STRENGTH,
                new XmagicPropertyValues(0, 100, 0, 0, 1)));
        allProperties.add(new XmagicUIProperty<>(UICategory.BODY_BEAUTY,
                context.getString(R.string.body_slim_head_strength_label), R.mipmap.body_slim_head_strength, BeautyConstant.BODY_SLIM_HEAD_STRENGTH,
                new XmagicPropertyValues(0, 100, 0, 0, 1)));
    }

    /**
     * 美颜数据构造
     * create beauty data
     */
    private static void parseBeauty(Context context, List<XmagicUIProperty<?>> beautyList) {
        String effDirs = "/images/beauty/";
        Map<String, String> effs = new HashMap<>();
        for (String effPath : new File(sResPath + "light_assets" + effDirs).list()) {
            effs.put(new File(effPath).getName(), effPath);
        }

        if (beautyList == null) {
            return;
        }
        XmagicUIProperty<?> beautyProperty = new XmagicUIProperty<>(UICategory.KV, null, 0, null, null);
        beautyList.add(beautyProperty);
        XmagicUIProperty<?> xmagicUIProperty = new XmagicUIProperty<>(UICategory.BEAUTY,
                context.getString(R.string.beauty_whiten_label), R.mipmap.beauty_whiten, BeautyConstant.BEAUTY_WHITEN,
                new XmagicPropertyValues(0, 100, 40, 0, 1));
        XmagicPanelDataManager.getInstance().getSelectedItems().put(xmagicUIProperty.uiCategory.getDescription(), xmagicUIProperty);
        beautyList.add(xmagicUIProperty);
        beautyList.add(new XmagicUIProperty<>(UICategory.BEAUTY, context.getString(R.string.beauty_smooth_label),
                R.mipmap.beauty_smooth, BeautyConstant.BEAUTY_SMOOTH, new XmagicPropertyValues(0, 100, 30, 0, 1)));
        beautyList.add(new XmagicUIProperty<>(UICategory.BEAUTY, context.getString(R.string.beauty_ruddy_label),
                R.mipmap.beauty_ruddy, BeautyConstant.BEAUTY_ROSY, new XmagicPropertyValues(0, 100, 30, 0, 2)));
        beautyList.add(new XmagicUIProperty<>(UICategory.BEAUTY, context.getString(R.string.image_contrast_label),
                R.mipmap.image_contrast, BeautyConstant.BEAUTY_CONTRAST, new XmagicPropertyValues(-100, 100, 0, -1, 1)));
        beautyList.add(new XmagicUIProperty<>(UICategory.BEAUTY, context.getString(R.string.image_saturation_label),
                R.mipmap.image_saturation, BeautyConstant.BEAUTY_SATURATION,
                new XmagicPropertyValues(-100, 100, 0, -1, 1)));
        beautyList.add(new XmagicUIProperty<>(UICategory.BEAUTY, context.getString(R.string.image_sharpen_label),
                R.mipmap.image_sharpen, BeautyConstant.BEAUTY_CLEAR, new XmagicPropertyValues(0, 100, 0, 0, 2)));
        beautyList.add(new XmagicUIProperty<>(UICategory.BEAUTY, context.getString(R.string.beauty_enlarge_eye_label),
                R.mipmap.beauty_enlarge_eye, BeautyConstant.BEAUTY_ENLARGE_EYE,
                new XmagicPropertyValues(0, 100, 20, 0, 1)));

        XmagicUIProperty thinFace = new XmagicUIProperty(context.getString(R.string.beauty_thin_face_label), R.mipmap.beauty_thin_face, UICategory.BEAUTY);
        beautyList.add(thinFace);
        List<XmagicUIProperty<?>> slList = new ArrayList<>();
        slList.add(new XmagicUIProperty<>(UICategory.BEAUTY, context.getString(R.string.beauty_thin_face1_label),
                BeautyConstant.BEAUTY_FACE_NATURE_ID, null, R.mipmap.beauty_thin_face1,
                BeautyConstant.BEAUTY_FACE_NATURE, new XmagicPropertyValues(0, 100, 30, 0, 1), "瘦脸"));
        slList.add(new XmagicUIProperty<>(UICategory.BEAUTY, context.getString(R.string.beauty_thin_face2_label),
                BeautyConstant.BEAUTY_FACE_FEMALE_GOD_ID, null, R.mipmap.beauty_thin_face2,
                BeautyConstant.BEAUTY_FACE_GODNESS, new XmagicPropertyValues(0, 100, 0, 0, 1), "瘦脸"));
        slList.add(new XmagicUIProperty<>(UICategory.BEAUTY, context.getString(R.string.beauty_thin_face3_label),
                BeautyConstant.BEAUTY_FACE_MALE_GOD_ID, null, R.mipmap.beauty_thin_face3,
                BeautyConstant.BEAUTY_FACE_MALE_GOD, new XmagicPropertyValues(0, 100, 0, 0, 1), "瘦脸"));
        thinFace.xmagicUIPropertyList = slList;

        beautyList.add(new XmagicUIProperty<>(UICategory.BEAUTY, context.getString(R.string.beauty_v_face_label),
                R.mipmap.beauty_v_face, BeautyConstant.BEAUTY_FACE_V,
                new XmagicPropertyValues(0, 100, 30, 0, 1)));
        beautyList.add(new XmagicUIProperty<>(UICategory.BEAUTY, context.getString(R.string.beauty_narrow_face_label),
                R.mipmap.beauty_narrow_face, BeautyConstant.BEAUTY_FACE_THIN,
                new XmagicPropertyValues(0, 100, 5, 0, 1)));
        beautyList.add(new XmagicUIProperty<>(UICategory.BEAUTY, context.getString(R.string.beauty_short_face_label),
                R.mipmap.beauty_short_face, BeautyConstant.BEAUTY_FACE_SHORT,
                new XmagicPropertyValues(0, 100, 0, 0, 1)));
        beautyList.add(new XmagicUIProperty<>(UICategory.BEAUTY, context.getString(R.string.beauty_basic_face_label),
                R.mipmap.beauty_basic_face, BeautyConstant.BEAUTY_FACE_BASIC,
                new XmagicPropertyValues(0, 100, 0, 0, 1)));

        Map<String, String> lipsResPathNames = new LinkedHashMap<>();
        lipsResPathNames.put("lips_fuguhong.png", context.getString(R.string.beauty_lips1_label));
        lipsResPathNames.put("lips_mitaose.png", context.getString(R.string.beauty_lips2_label));
        lipsResPathNames.put("lips_shanhuju.png", context.getString(R.string.beauty_lips3_label));
        lipsResPathNames.put("lips_wenroufen.png", context.getString(R.string.beauty_lips4_label));
        lipsResPathNames.put("lips_huolicheng.png", context.getString(R.string.beauty_lips5_label));
        List<XmagicUIProperty<?>> itemLipsPropertys = new ArrayList<>();

        for (String ids : lipsResPathNames.keySet()) {
            itemLipsPropertys.add(new XmagicUIProperty<>(UICategory.BEAUTY, lipsResPathNames.get(ids),
                    XmagicConstant.BeautyConstant.BEAUTY_LIPS_LIPS_MASK, effDirs + effs.get(ids),
                    R.mipmap.beauty_lips, BeautyConstant.BEAUTY_MOUTH_LIPSTICK,
                    new XmagicPropertyValues(0, 100, 50, 0, 1), "口红"));
        }
        XmagicUIProperty itemLips = new XmagicUIProperty<>(context.getString(R.string.beauty_lips_label), R.mipmap.beauty_lips, UICategory.BEAUTY);
        itemLips.xmagicUIPropertyList = itemLipsPropertys;
        beautyList.add(itemLips);

        Map<String, String> redcheeksResPathNames = new LinkedHashMap<>();
        redcheeksResPathNames.put("saihong_jianyue.png", context.getString(R.string.beauty_redcheeks1_label));
        redcheeksResPathNames.put("saihong_shengxia.png", context.getString(R.string.beauty_redcheeks2_label));
        redcheeksResPathNames.put("saihong_haixiu.png", context.getString(R.string.beauty_redcheeks3_label));
        redcheeksResPathNames.put("saihong_chengshu.png", context.getString(R.string.beauty_redcheeks4_label));
        redcheeksResPathNames.put("saihong_queban.png", context.getString(R.string.beauty_redcheeks5_label));

        List<XmagicUIProperty<?>> itemRedcheekPropertys = new ArrayList<>();

        for (String ids : redcheeksResPathNames.keySet()) {
            itemRedcheekPropertys.add(new XmagicUIProperty<>(UICategory.BEAUTY, redcheeksResPathNames.get(ids),
                    XmagicConstant.BeautyConstant.BEAUTY_MAKEUP_MULTIPLY_MULTIPLY_MASK,
                    effDirs + effs.get(ids), R.mipmap.beauty_redcheeks, BeautyConstant.BEAUTY_FACE_RED_CHEEK,
                    new XmagicPropertyValues(0, 100, 50, 0, 1), "腮红"));
        }
        XmagicUIProperty itemRedcheeks = new XmagicUIProperty<>(context.getString(R.string.beauty_redcheeks_label), R.mipmap.beauty_redcheeks, UICategory.BEAUTY);
        itemRedcheeks.xmagicUIPropertyList = itemRedcheekPropertys;
        beautyList.add(itemRedcheeks);

        Map<String, String> litisResPathNames = new LinkedHashMap<>();
        litisResPathNames.put("liti_ziran.png", context.getString(R.string.beauty_liti1_label));
        litisResPathNames.put("liti_junlang.png", context.getString(R.string.beauty_liti2_label));
        litisResPathNames.put("liti_guangmang.png", context.getString(R.string.beauty_liti3_label));
        litisResPathNames.put("liti_qingxin.png", context.getString(R.string.beauty_liti4_label));

        List<XmagicUIProperty<?>> liTiItems = new ArrayList<>();
        for (String ids : litisResPathNames.keySet()) {
            liTiItems.add(new XmagicUIProperty<>(UICategory.BEAUTY, litisResPathNames.get(ids),
                    XmagicConstant.BeautyConstant.BEAUTY_SOFTLIGHT_SOFTLIGHT_MASK,
                    effDirs + effs.get(ids), R.mipmap.beauty_liti, BeautyConstant.BEAUTY_FACE_SOFTLIGHT,
                    new XmagicPropertyValues(0, 100, 50, 0, 1), "立体"));
        }
        XmagicUIProperty itemLitis = new XmagicUIProperty(context.getString(R.string.beauty_liti_label), R.mipmap.beauty_liti, UICategory.BEAUTY);
        itemLitis.xmagicUIPropertyList = liTiItems;
        beautyList.add(itemLitis);

        beautyList.add(new XmagicUIProperty<>(UICategory.BEAUTY, context.getString(R.string.beauty_thin_cheek_label),
                R.mipmap.beauty_thin_cheek, BeautyConstant.BEAUTY_FACE_THIN_CHEEKBONE,
                new XmagicPropertyValues(-100, 100, 0, -1, 1)));
        beautyList.add(new XmagicUIProperty<>(UICategory.BEAUTY, context.getString(R.string.beauty_chin_label),
                R.mipmap.beauty_chin, BeautyConstant.BEAUTY_FACE_THIN_CHIN,
                new XmagicPropertyValues(-100, 100, 0, -1, 1)));
        beautyList.add(new XmagicUIProperty<>(UICategory.BEAUTY, context.getString(R.string.beauty_forehead_label),
                R.mipmap.beauty_forehead, BeautyConstant.BEAUTY_FACE_FOREHEAD,
                new XmagicPropertyValues(-100, 100, 0, -1, 1)));
        beautyList.add(new XmagicUIProperty<>(UICategory.BEAUTY, context.getString(R.string.beauty_eye_lighten_label),
                R.mipmap.beauty_eye_lighten, BeautyConstant.BEAUTY_EYE_LIGHTEN,
                new XmagicPropertyValues(0, 100, 20, 0, 1)));
        beautyList.add(new XmagicUIProperty<>(UICategory.BEAUTY, context.getString(R.string.beauty_eye_distance_label),
                R.mipmap.beauty_eye_distance, BeautyConstant.BEAUTY_EYE_DISTANCE,
                new XmagicPropertyValues(-100, 100, 0, -1, 1)));
        beautyList.add(new XmagicUIProperty<>(UICategory.BEAUTY, context.getString(R.string.beauty_eye_angle_label),
                R.mipmap.beauty_eye_angle, BeautyConstant.BEAUTY_EYE_ANGLE,
                new XmagicPropertyValues(-100, 100, 0, -1, 1)));
        beautyList.add(new XmagicUIProperty<>(UICategory.BEAUTY, context.getString(R.string.beauty_thin_nose_label),
                R.mipmap.beauty_thin_nose, BeautyConstant.BEAUTY_NOSE_THIN,
                new XmagicPropertyValues(0, 100, 0, 0, 1)));
        beautyList.add(new XmagicUIProperty<>(UICategory.BEAUTY, context.getString(R.string.beauty_nose_wing_label),
                R.mipmap.beauty_nose_wing, BeautyConstant.BEAUTY_NOSE_WING,
                new XmagicPropertyValues(-100, 100, 0, -1, 1)));
        beautyList.add(new XmagicUIProperty<>(UICategory.BEAUTY, context.getString(R.string.beauty_nose_position_label),
                R.mipmap.beauty_nose_position, BeautyConstant.BEAUTY_NOSE_HEIGHT,
                new XmagicPropertyValues(-100, 100, 0, -1, 1)));
        beautyList.add(new XmagicUIProperty<>(UICategory.BEAUTY, context.getString(R.string.beauty_tooth_beauty_label),
                R.mipmap.beauty_tooth_beauty, BeautyConstant.BEAUTY_TOOTH_WHITEN,
                new XmagicPropertyValues(0, 100, 0, 0, 1)));
        beautyList.add(new XmagicUIProperty<>(UICategory.BEAUTY, context.getString(R.string.beauty_remove_pounch_label),
                R.mipmap.beauty_remove_pounch, BeautyConstant.BEAUTY_FACE_REMOVE_WRINKLE,
                new XmagicPropertyValues(0, 100, 0, 0, 1)));
        beautyList.add(new XmagicUIProperty<>(UICategory.BEAUTY, context.getString(R.string.beauty_wrinkle_smooth_label),
                R.mipmap.beauty_wrinkle_smooth, BeautyConstant.BEAUTY_FACE_REMOVE_LAW_LINE,
                new XmagicPropertyValues(0, 100, 0, 0, 1)));
        beautyList.add(new XmagicUIProperty<>(UICategory.BEAUTY, context.getString(R.string.beauty_remove_eye_pouch_label),
                R.mipmap.beauty_remove_eye_pouch, BeautyConstant.BEAUTY_FACE_REMOVE_EYE_BAGS,
                new XmagicPropertyValues(0, 100, 0, 0, 1)));
        beautyList.add(new XmagicUIProperty<>(UICategory.BEAUTY, context.getString(R.string.beauty_mouth_size_label),
                R.mipmap.beauty_mouth_size, BeautyConstant.BEAUTY_MOUTH_SIZE,
                new XmagicPropertyValues(-100, 100, 0, -1, 1)));
        beautyList.add(new XmagicUIProperty<>(UICategory.BEAUTY, context.getString(R.string.beauty_mouth_height_label),
                R.mipmap.beauty_mouth_height, BeautyConstant.BEAUTY_MOUTH_HEIGHT,
                new XmagicPropertyValues(-100, 100, 0, -1, 1)));
    }

    /**
     * 滤镜数据构造
     * create lut data
     *
     * @param lutList
     */
    private static void parseLutProperty(Context context, List<XmagicUIProperty<?>> lutList) {
        //收集全部滤镜
        String lutDir = sResPath + "light_material/lut/";
        LinkedHashMap<String, String> map = getMaterialDataByStr(context.getString(R.string.lut_resource_name_str));
        lutList.add(new XmagicUIProperty<XmagicPropertyValues>(UICategory.LUT,
                context.getString(R.string.item_none_label), XmagicProperty.ID_NONE, null, R.mipmap.naught, null, null));
        List<MotionDLModel> MotionDLModels = TEResourceDLUtils.getTEResDLModel(context, TEResourceType.Lut);
        for (MotionDLModel motionDLModel : MotionDLModels) {
            String fileName = motionDLModel.getName();
            XmagicUIProperty<?> uiProperty = new XmagicUIProperty<>(UICategory.LUT, map.get(fileName),
                    fileName, lutDir + fileName, 0, null,
                    new XmagicPropertyValues(0, 100, 60, 0, 1));
            uiProperty.thumbImagePath = TEResourceDLUtils.getLutIconUrlByName(context, fileName);
            if (!new File(lutDir + fileName).exists()) {
                uiProperty.dlModel = motionDLModel;
            }
            lutList.add(uiProperty);
        }
    }

    /**
     * 生成动效资源
     * create motion data
     */
    private static void parseMotionData(Context context, List<XmagicUIProperty<?>> motionsData) {
        if (motionsData == null) {
            return;
        }
        List<XmagicUIProperty<?>> dynamicData = new ArrayList<>();
        LinkedHashMap<String, String> motionPropertyNameConverter =
                getMaterialDataByStr(context.getString(R.string.motion_resource_str));

        //2D素材
        String motion2dResPath = sResPath + "MotionRes/2dMotionRes/";
        XmagicUIProperty item2dMotions = new XmagicUIProperty<>(context.getString(R.string.motion_2d_label), R.mipmap.motion_2d, UICategory.MOTION);
        item2dMotions.xmagicUIPropertyList = new ArrayList<XmagicUIProperty<XmagicPropertyValues>>();
        parseMotion(context, motion2dResPath, motionPropertyNameConverter, context.getString(R.string.motion_3d_label),
                TEResourceType.MotionRes2D, item2dMotions.xmagicUIPropertyList);
        if (item2dMotions.xmagicUIPropertyList.size() > 0) {
            dynamicData.add(item2dMotions);
        }
        //3D素材
        String motion3dResPath = sResPath + "MotionRes/3dMotionRes/";
        XmagicUIProperty item3dMotions = new XmagicUIProperty<>(context.getString(R.string.motion_3d_label), R.mipmap.motion_3d, UICategory.MOTION);
        item3dMotions.xmagicUIPropertyList = new ArrayList<XmagicUIProperty<XmagicPropertyValues>>();
        parseMotion(context, motion3dResPath, motionPropertyNameConverter, context.getString(R.string.motion_3d_label),
                TEResourceType.MotionRes3D, item3dMotions.xmagicUIPropertyList);
        if (item3dMotions.xmagicUIPropertyList.size() > 0) {
            dynamicData.add(item3dMotions);
        }
        //构造手势素材数据
        String handMotionResPath = sResPath + "MotionRes/handMotionRes/";
        XmagicUIProperty itemHandMotions = new XmagicUIProperty<>(context.getString(R.string.motion_hand_label), R.mipmap.motion_hand, UICategory.MOTION);
        itemHandMotions.xmagicUIPropertyList = new ArrayList<XmagicUIProperty<XmagicPropertyValues>>();
        parseMotion(context, handMotionResPath, motionPropertyNameConverter,
                context.getString(R.string.motion_hand_label),
                TEResourceType.MotionResHand, itemHandMotions.xmagicUIPropertyList);
        if (itemHandMotions.xmagicUIPropertyList.size() > 0) {
            dynamicData.add(itemHandMotions);
        }
        //趣味素材数据
        String ganMotionResPath = sResPath + "MotionRes/ganMotionRes/";
        XmagicUIProperty itemGanMotions = new XmagicUIProperty<>(context.getString(R.string.motion_gan_label), R.mipmap.motion_gan, UICategory.MOTION);
        itemGanMotions.xmagicUIPropertyList = new ArrayList<XmagicUIProperty<XmagicPropertyValues>>();
        parseMotion(context, ganMotionResPath, motionPropertyNameConverter,
                context.getString(R.string.motion_gan_label),
                TEResourceType.MotionResGan,
                itemGanMotions.xmagicUIPropertyList);
        if (itemGanMotions.xmagicUIPropertyList.size() > 0) {
            dynamicData.add(itemGanMotions);
        }
        if (dynamicData.size() > 0) {   //如果有数据才添加无的选项，没有数据则不添加此选项
            motionsData.add(new XmagicUIProperty(UICategory.MOTION, context.getString(R.string.item_none_label), XmagicProperty.ID_NONE,
                    sResPath + "light_assets/template.json", R.mipmap.naught, null, null));
            motionsData.addAll(dynamicData);
        }
    }


    private static void parseMotion(Context context, String filePath, LinkedHashMap<String, String> converter,
                                    String name, TEResourceType resourceType,
                                    List<XmagicUIProperty<XmagicPropertyValues>> list) {
        List<MotionDLModel> MotionDLModels = TEResourceDLUtils.getTEResDLModel(context, resourceType);
        File[] ganMotionFiles = getFilesByPath(filePath);
        if (ganMotionFiles != null) {
            for (File file : ganMotionFiles) {
                if (file.isDirectory()) {
                    String id = file.getName();
                    MotionDLModel MotionDLModel = removeHasExitItem(MotionDLModels, id);
                    XmagicUIProperty uiProperty = createMotionUiProperty(id, converter, filePath, name);
                    uiProperty.thumbImagePath = MotionDLModel != null ?
                            TEResourceDLUtils.getMotionIconUrlByName(context, MotionDLModel.getName()) :
                            getTemplateImgPath(resourceType.getName() + File.separator + id);
                    list.add(uiProperty);
                }
            }
        }
        //开始处理需要下载的item
        for (MotionDLModel MotionDLModel : MotionDLModels) {
            XmagicUIProperty uiProperty = createMotionUiProperty(MotionDLModel.getName(), converter, filePath, name);
            uiProperty.thumbImagePath = TEResourceDLUtils.getMotionIconUrlByName(context, MotionDLModel.getName());
            uiProperty.dlModel = MotionDLModel;
            list.add(uiProperty);
        }
    }


    private static XmagicUIProperty createMotionUiProperty(String id, LinkedHashMap<String, String> NameConverter,
                                                           String resourcePath, String rootDisplayName) {
        String name = NameConverter.get(id);
        String materialPath = resourcePath + id;
        name = (name != null ? name : id);
        return new XmagicUIProperty<>(UICategory.MOTION, name, id, materialPath, 0, null, null, rootDisplayName);
    }


    /**
     * 构造美妆数据
     * create makeup data
     */
    private static void parseMakeUpData(Context context, List<XmagicUIProperty<?>> makeupData) {
        if (makeupData == null) {
            return;
        }
        String makeupResPath = sResPath + "MotionRes/makeupRes/";
        File[] makeupFiles = getFilesByPath(makeupResPath);
        List<MotionDLModel> MotionDLModelList = TEResourceDLUtils.getTEResDLModel(context, TEResourceType.MotionResMakeup);
        if ((makeupFiles == null || makeupFiles.length == 0) && (MotionDLModelList == null || MotionDLModelList.size() == 0)) {
            return;
        }
        makeupData.add(new XmagicUIProperty(UICategory.MAKEUP, context.getString(R.string.item_none_label),
                XmagicProperty.ID_NONE, sResPath + "light_assets/template.json", R.mipmap.naught, null, null));
        LinkedHashMap<String, String> makeupNameConverter =
                getMaterialDataByStr(context.getString(R.string.makeup_resource_str));
        //构造整妆素材数据
        if (makeupFiles != null) {
            for (File file : makeupFiles) {
                if (file.isDirectory()) {
                    String id = file.getName();
                    MotionDLModel MotionDLModel = removeHasExitItem(MotionDLModelList, id);
                    String materialPath = makeupResPath + id;
                    String name = makeupNameConverter.get(id);
                    name = (name != null ? name : id);
                    XmagicPropertyValues xmagicPropertyValues = new XmagicPropertyValues(0, 100, 60, 0, 1);
                    XmagicUIProperty xmagicUIProperty = new XmagicUIProperty<>(UICategory.MAKEUP, name, id,
                            materialPath, 0, XmagicConstant.MakeUpEffKey.MAKEUP_EFF_KEY, xmagicPropertyValues);
                    xmagicUIProperty.thumbImagePath = MotionDLModel != null ?
                            TEResourceDLUtils.getMotionIconUrlByName(context, MotionDLModel.getName()) :
                            getTemplateImgPath("makeupRes/" + id);
                    makeupData.add(xmagicUIProperty);
                }
            }
        }
        //开始处理需要下载的item
        for (MotionDLModel MotionDLModel : MotionDLModelList) {
            String id = MotionDLModel.getName();
            String name = makeupNameConverter.get(id);
            name = (name != null ? name : id);
            XmagicPropertyValues xmagicPropertyValues = new XmagicPropertyValues(0, 100, 60, 0, 1);
            XmagicUIProperty<XmagicPropertyValues> uiProperty = new XmagicUIProperty<>(UICategory.MAKEUP, name, id,
                    makeupResPath + id, 0, XmagicConstant.MakeUpEffKey.MAKEUP_EFF_KEY, xmagicPropertyValues);
            uiProperty.thumbImagePath = TEResourceDLUtils.getMotionIconUrlByName(context, MotionDLModel.getName());
            uiProperty.dlModel = MotionDLModel;
            makeupData.add(uiProperty);
        }

    }

    /**
     * 移除本地已有的资源名称
     *
     * @param MotionDLModelList
     * @param id
     */
    private static MotionDLModel removeHasExitItem(List<MotionDLModel> MotionDLModelList, String id) {
        Iterator<MotionDLModel> motionDLModelIterator = MotionDLModelList.iterator();
        while (motionDLModelIterator.hasNext()) {
            MotionDLModel item = motionDLModelIterator.next();
            if (id.equals(item.getName())) {
                motionDLModelIterator.remove();
                return item;
            }
        }
        return null;
    }

    /**
     * 构造分割 数据
     * create segmentation data
     */
    private static void parseSegData(Context context, List<XmagicUIProperty<?>> segListData) {
        if (segListData == null) {
            return;
        }
        List<MotionDLModel> MotionDLModelList = TEResourceDLUtils.getTEResDLModel(context, TEResourceType.MotionResSegment);
        String segmentMotionResPath = sResPath + "MotionRes/segmentMotionRes/";
        File[] segmentMotionFiles = getFilesByPath(segmentMotionResPath);
        if ((segmentMotionFiles == null || segmentMotionFiles.length == 0) && (MotionDLModelList == null || MotionDLModelList.size() == 0)) {
            return;
        }
        LinkedHashMap<String, String> segmentationNameConverter = getMaterialDataByStr(
                context.getString(R.string.segmentation_resource_str));
        segListData.add(new XmagicUIProperty(UICategory.SEGMENTATION, context.getString(R.string.item_none_label),
                XmagicProperty.ID_NONE, sResPath + "light_assets/template.json", R.mipmap.naught, null, null));

        //构造分割素材数据
        if (segmentMotionFiles != null) {
            for (File file : segmentMotionFiles) {
                if (file.isDirectory()) {
                    String id = file.getName();
                    MotionDLModel MotionDLModel = removeHasExitItem(MotionDLModelList, id);
                    String materialPath = segmentMotionResPath + id;
                    String name = segmentationNameConverter.get(id);
                    name = (name != null ? name : id);
                    if (XmagicConstant.SegmentationId.CUSTOM_SEG_ID.equals(id)) {
                        segListData.add(new XmagicUIProperty<>(UICategory.SEGMENTATION,
                                context.getString(R.string.segmentation_custom_label), id, materialPath,
                                R.mipmap.segmentation_formulate, null, null));
                    } else {
                        XmagicUIProperty<XmagicPropertyValues> uiProperty = new XmagicUIProperty<>(
                                UICategory.SEGMENTATION, name, id, materialPath, 0, null, null);
                        uiProperty.thumbImagePath = MotionDLModel != null ?
                                TEResourceDLUtils.getMotionIconUrlByName(context, MotionDLModel.getName()) :
                                getTemplateImgPath("segmentMotionRes/" + id);
                        segListData.add(uiProperty);
                    }
                }
            }
        }
        //开始处理需要下载的item
        for (MotionDLModel motionDLModel : MotionDLModelList) {
            String id = motionDLModel.getName();
            String name = segmentationNameConverter.get(id);
            name = (name != null ? name : id);
            if (XmagicConstant.SegmentationId.CUSTOM_SEG_ID.equals(id)) {
                name = context.getString(R.string.segmentation_custom_label);
            }
            XmagicUIProperty<XmagicPropertyValues> uiProperty = new XmagicUIProperty<>(UICategory.SEGMENTATION, name, id,
                    segmentMotionResPath + id, 0, null, null);
            uiProperty.thumbImagePath = TEResourceDLUtils.getMotionIconUrlByName(context, motionDLModel.getName());
            uiProperty.dlModel = motionDLModel;
            segListData.add(uiProperty);
        }
    }


    private static File[] getFilesByPath(String path) {
        File[] fileList = new File(path).listFiles();
        if (fileList != null && fileList.length > 0) {
            List fileNames = Arrays.asList(fileList);
            Collections.sort(fileNames, new Comparator<File>() {
                @Override
                public int compare(File o1, File o2) {
                    if (o1.isDirectory() && o2.isFile()) {
                        return -1;
                    }
                    if (o1.isFile() && o2.isDirectory()) {
                        return 1;
                    }
                    return o1.getName().compareTo(o2.getName());
                }
            });
            return fileList;
        }
        return null;
    }

    private static LinkedHashMap<String, String> getMaterialDataByStr(String str) {
        LinkedHashMap<String, String> propertyNameConverter = new LinkedHashMap<>();
        String[] pairs = str.split(",");
        for (int i = 0; i < pairs.length; i++) {
            String pair = pairs[i];
            String[] keyValue = pair.split(":");
            propertyNameConverter.put(keyValue[0].trim(), keyValue[1].trim());
        }
        return propertyNameConverter;
    }


    private static String getTemplateImgPath(String motionName) {
        return sResPath + "MotionRes" + File.separator + motionName + File.separator + "template.png";
    }

    /**
     * 判断文件是否存在
     *
     * @param path 文件路径
     * @return true为文件存在，false为文件不存在
     */
    private static boolean exists(String path) {
        if (TextUtils.isEmpty(path)) {
            return false;
        }
        // assets中的文件，默认一定存在；非assets中的文件，需要正常判断是否存在
        return path.indexOf("assets") >= 0 || new File(path).exists();
    }


}
