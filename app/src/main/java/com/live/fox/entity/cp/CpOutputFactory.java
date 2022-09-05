package com.live.fox.entity.cp;

import android.content.Context;

import com.live.fox.entity.response.LotteryCpVO;


public abstract class CpOutputFactory {
    public static final String TYPE_CP_TXSSC = "txssc";//一分时时彩
    public static final String TYPE_CP_SSC = "ssc";
    public static final String TYPE_CP_PK = "pk10";//北京赛车
    public static final String TYPE_CP_XYFT = "xyft10";
    public static final String TYPE_CP_JSKS = "jsks";//一分快三
    public static final String TYPE_CP_GXKS = "gxks";
    public static final String TYPE_CP_BJ28 = "bj28";
    public static final String TYPE_CP_JX11 = "jx11";
    public static final String TYPE_CP_JS11 = "js11";
    public static final String TYPE_CP_LHC = "yflhc";
    public static final String TYPE_CP_HNCP = "yn_hncp";//河内彩票
    public static final String TYPE_CP_FF="yuxx";
    protected String type;

    public CpOutputFactory() {
    }

    public CpOutputFactory(String type) {
        this.type = type;
    }


    public abstract LotteryCpVO getCpVoByType(Context context);

}
