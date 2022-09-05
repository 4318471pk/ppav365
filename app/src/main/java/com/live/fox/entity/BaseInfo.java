
package com.live.fox.entity;

/**
 * 配置信息
 */
public class BaseInfo {

    private String appServiceUrl;
    private String domain;
    private String domainThree;
    private String domainTwo;
    private String h5Url;
    private boolean maintain;
    private String openScreen;
    private String openScreenUrl;
    private String shareUrl;
    private int isGameStart = 0;//0=开启，1=不开启
    private int isCpStart = 1;//0=开启，1=不开启
    private int isCpButton = 1;//0=开启，1=不开启
    private String sdkappid;
    private String currencySymbol; //货币符号
    private String currency;   //换算比例
    private String currencyCode; //货币代码
    private String minWithdraw;  //最低提款金额
    private String androidTpnsAccessId;
    private String androidTpnsAccessKey;
    private String loginUrl; //login logo
    private String floorUrl;  //落地页
    private String defaultLanguage;

    public String getSdkappid() {
        return sdkappid;
    }

    public void setSdkappid(String sdkappid) {
        this.sdkappid = sdkappid;
    }

    public void setAppServiceUrl(String appServiceUrl) {
        this.appServiceUrl = appServiceUrl;
    }

    public String getAppServiceUrl() {
        return appServiceUrl;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomainThree(String domainThree) {
        this.domainThree = domainThree;
    }

    public String getDomainThree() {
        return domainThree;
    }

    public void setDomainTwo(String domainTwo) {
        this.domainTwo = domainTwo;
    }

    public String getDomainTwo() {
        return domainTwo;
    }

    public void setH5Url(String h5Url) {
        this.h5Url = h5Url;
    }

    public String getH5Url() {
        return h5Url;
    }

    public void setMaintain(boolean maintain) {
        this.maintain = maintain;
    }

    public boolean getMaintain() {
        return maintain;
    }

    public void setOpenScreen(String openScreen) {
        this.openScreen = openScreen;
    }

    public String getOpenScreen() {
        return openScreen;
    }

    public void setOpenScreenUrl(String openScreenUrl) {
        this.openScreenUrl = openScreenUrl;
    }

    public String getOpenScreenUrl() {
        return openScreenUrl;
    }

    public void setShareUrl(String shareUrl) {
        this.shareUrl = shareUrl;
    }

    public String getShareUrl() {
        return shareUrl;
    }

    public boolean isMaintain() {
        return maintain;
    }

    public int getIsGameStart() {
        return isGameStart;
    }

    public void setIsGameStart(int isGameStart) {
        this.isGameStart = isGameStart;
    }

    public int getIsCpStart() {
        return isCpStart;
    }

    public void setIsCpStart(int isCpStart) {
        this.isCpStart = isCpStart;
    }

    public int getIsCpButton() {
        return isCpButton;
    }

    public void setIsCpButton(int isCpButton) {
        this.isCpButton = isCpButton;
    }

    public String getAndroidTpnsAccessId() {
        return androidTpnsAccessId;
    }

    public void setAndroidTpnsAccessId(String androidTpnsAccessId) {
        this.androidTpnsAccessId = androidTpnsAccessId;
    }

    public String getAndroidTpnsAccessKey() {
        return androidTpnsAccessKey;
    }

    public void setAndroidTpnsAccessKey(String androidTpnsAccessKey) {
        this.androidTpnsAccessKey = androidTpnsAccessKey;
    }

    public String getCurrencySymbol() {
        return currencySymbol;
    }

    public void setCurrencySymbol(String currencySymbol) {
        this.currencySymbol = currencySymbol;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getLoginUrl() {
        return loginUrl;
    }

    public void setLoginUrl(String loginUrl) {
        this.loginUrl = loginUrl;
    }

    public String getFloorUrl() {
        return floorUrl;
    }

    public void setFloorUrl(String floorUrl) {
        this.floorUrl = floorUrl;
    }

    public String getMinWithdraw() {
        return minWithdraw;
    }

    public void setMinWithdraw(String minWithdraw) {
        this.minWithdraw = minWithdraw;
    }

    public String getDefaultLanguage() {
        return defaultLanguage;
    }

    public void setDefaultLanguage(String defaultLanguage) {
        this.defaultLanguage = defaultLanguage;
    }
}