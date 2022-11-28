package com.tencent.demo.download;

import java.util.ArrayList;
import java.util.List;

public class ResDownloadConfig {
    //压缩包文件组织结构如下
    //
    //---mylibs.zip(这个名字可以随便命名)
    //------liblibpag.so
    //------liblight-sdk.so
    //------libv8jni.so
    //
    //TODO
    public static final String DOWNLOAD_URL_LIBS_V8A = "https://服务器地址/libs-v8a.zip";
    public static final String DOWNLOAD_URL_LIBS_V7A = "https://服务器地址/libs-v7a.zip";
    //TODO
    public static final String DOWNLOAD_MD5_LIBS_V8A = "libs-v8a.zip的MD5";
    public static final String DOWNLOAD_MD5_LIBS_V7A = "libs-v7a.zip的MD5";


    //压缩包文件组织结构如下
    //
    //---myassets.zip(这个名字可以随便命名)
    //------Light3DPlugin
    //------LightCore
    //------LightHandPlugin
    //------LightSegmentPlugin
    //------lut
    //TODO
    public static final String DOWNLOAD_URL_ASSETS = "https://服务器地址/assets.zip";
    //TODO
    public static final String DOWNLOAD_MD5_ASSETS = "assets.zip的MD5";

    //TODO 
    private static final String MOTION_RES_DOWNLOAD_PREFIX = "https://服务器地址/";

    //Motion动效的一个压缩包是一个动效，下载时逐个下载
    //压缩时把一个动效文件夹压缩成zip即可
    public static List<MotionDLModel> getMotionList(){
        List<MotionDLModel> motionList = new ArrayList<>();
        motionList.addAll(addModelList("2dMotionRes",MotionRes2D));
        motionList.addAll(addModelList("3dMotionRes",MotionRes3D));
        motionList.addAll(addModelList("handMotionRes",MotionResHand));
        motionList.addAll(addModelList("makeupRes",MotionResMakeup));
        motionList.addAll(addModelList("segmentMotionRes",MotionResSegment));
        motionList.addAll(addModelList("ganMotionRes",MotionResGan));
        return motionList;
    }

    private static List<MotionDLModel> addModelList(String category, String[] motionList) {
        List<MotionDLModel> list = new ArrayList<>();
        for (int i = 0; i < motionList.length; i++ ){
            list.add(new MotionDLModel(category,motionList[i],MOTION_RES_DOWNLOAD_PREFIX+motionList[i]+".zip"));
        }
        return list;
    }

    private static final String[] MotionResGan = new String[]{
            "video_bubblegum"
    };

    private static final String[] MotionResSegment = new String[]{
            "video_empty_segmentation",
            "video_guaishoutuya",
            "video_bgvideo_segmentation",
            "video_segmentation_blur_45",
            "video_segmentation_blur_75"
    };

    private static final String[] MotionResMakeup = new String[]{
            "video_fenfenxia",
            "video_guajiezhuang",
            "video_hongjiuzhuang",
            "video_nvtuanzhuang",
            "video_shaishangzhuang",
            "video_shuimitao",
            "video_xiaohuazhuang",
            "video_xuejiezhuang",
            "video_zhiganzhuang"
    };

    private static final String[] MotionResHand = new String[]{
            "video_sakuragirl",
            "video_shoushiwu"
    };

    private static final String[] MotionRes3D = new String[]{
            "video_3DFace_springflower",
            "video_feitianzhuzhu",
            "video_hudiejie",
            "video_jinli",
            "video_maoxinvhai",
            "video_ningmengyayamao",
            "video_tantanfagu",
            "video_tiankulamei",
            "video_yazi",
            "video_zhixingmeigui",
            "video_tonghuagushi"
    };

    private static final String[] MotionRes2D = new String[]{
                "video_aixinyanhua",
                "video_aiyimanman",
                "video_baozilian",
                "video_biaobai",
                "video_bingjingaixin",
                "video_boom",
                "video_boys",
                "video_cherries",
                "video_chudao",
                "video_dongriliange",
                "video_egaoshuangwanzi",
                "video_fenweiqiehuan",
                "video_fugu_dv",
                "video_guifeiface",
                "video_heimaomi",
                "video_kaixueqianhou",
                "video_kangnaixin",
                "video_kawayixiaoxiong",
                "video_keaituya",
                "video_lengliebingmo",
                "video_lianliancaomei",
                "video_litihuaduo",
                "video_liuhaifadai",
                "video_mengmengxiong",
                "video_naipingmianmo",
                "video_nightgown",
                "video_otwogirl",
                "video_qipaoshui",
                "video_qiqiupaidui",
                "video_quebanzhuang",
                "video_rixishaonv",
                "video_shangtoule",
                "video_shuangmahua",
                "video_tianxinmengniiu",
                "video_tutujiang",
                "video_xiangsuyuzhou",
                "video_xiaohonghua",
                "video_xiaohongxing",
                "video_xiaohuangmao",
                "video_xingganxiaochou",
                "video_xuancainihong",
                "video_xuanmeizhuang",
                "video_yaogunyue",
                "video_zuijiuweixun"
    };


}
