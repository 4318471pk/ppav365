package com.live.fox.entity.cp;

import static com.live.fox.entity.cp.CpOutputFactory.TYPE_CP_BJ28;
import static com.live.fox.entity.cp.CpOutputFactory.TYPE_CP_FF;
import static com.live.fox.entity.cp.CpOutputFactory.TYPE_CP_GXKS;
import static com.live.fox.entity.cp.CpOutputFactory.TYPE_CP_HNCP;
import static com.live.fox.entity.cp.CpOutputFactory.TYPE_CP_JS11;
import static com.live.fox.entity.cp.CpOutputFactory.TYPE_CP_JSKS;
import static com.live.fox.entity.cp.CpOutputFactory.TYPE_CP_JX11;
import static com.live.fox.entity.cp.CpOutputFactory.TYPE_CP_LHC;
import static com.live.fox.entity.cp.CpOutputFactory.TYPE_CP_PK;
import static com.live.fox.entity.cp.CpOutputFactory.TYPE_CP_SSC;
import static com.live.fox.entity.cp.CpOutputFactory.TYPE_CP_TXSSC;
import static com.live.fox.entity.cp.CpOutputFactory.TYPE_CP_XYFT;


public class CpFactory {
    public CpOutputFactory createFactory(String type) {
        if (TYPE_CP_TXSSC.equals(type) || TYPE_CP_SSC.equals(type)) {
            return new TxOrSscOutputFactory(type);
        } else if (TYPE_CP_PK.equals(type)) {
            return new PkOutputFactory(type);
        }  else if (TYPE_CP_XYFT.equals(type)) {
            return new YfktOutputFactory(type);
        } else if (TYPE_CP_JSKS.equals(type)) {
            return new JsksOutputFactory(type);
        } else if (TYPE_CP_GXKS.equals(type)) {
            return new GxksOutputFactory(type);
        } else if (TYPE_CP_BJ28.equals(type)) {
            return new Bj28OutputFactory(type);
        } else if (TYPE_CP_JX11.equals(type)) {
            return new Jx11OutputFactory(type);
        } else if (TYPE_CP_JS11.equals(type)) {
            return new Js11OutputFactory(type);
        }else if (TYPE_CP_LHC.equals(type)){
            return new LHCOutputFactory(type);
        }else if (TYPE_CP_HNCP.equals(type)){
            return new HNCPOutputFactory(type);
        }else if (TYPE_CP_FF.equals(type)){
            return new FFOutputFactory(type);
        }
        return new TxOrSscOutputFactory(type);
    }
}
