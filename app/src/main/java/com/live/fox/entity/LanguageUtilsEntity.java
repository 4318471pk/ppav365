package com.live.fox.entity;


import com.live.fox.language.MultiLanguageUtils;

public class LanguageUtilsEntity {

    private String EN;
    private String TW;
    private String CN;
    private String YN;
    private String THAI;

    public String getEN() {
        return EN;
    }

    public void setEN(String EN) {
        this.EN = EN;
    }

    public String getYN() {
        return YN;
    }

    public void setYN(String YN) {
        this.YN = YN;
    }

    public String getTHAI() {
        return THAI;
    }

    public void setTHAI(String THAI) {
        this.THAI = THAI;
    }

    public String getTW() {
        return TW;
    }

    public void setTW(String TW) {
        this.TW = TW;
    }

    public String getCN() {
        return CN;
    }

    public void setCN(String CN) {
        this.CN = CN;
    }

    public static String getLanguage(LanguageUtilsEntity languageEntity){
        String adStr = "";
        String language = MultiLanguageUtils.getRequestHeader();
        switch (language) {
            case "YN":
                adStr = languageEntity.getYN();
                break;

            case "THAI":
                adStr =languageEntity.getTHAI();
                break;

            case "TW":
                adStr = languageEntity.getTW();
                break;

            case "CN":
                adStr = languageEntity.getCN();
                break;

            case "EN":
                adStr = languageEntity.getEN();
                break;
        }
        return adStr;
    }
}
