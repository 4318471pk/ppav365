package com.live.fox.entity;


import com.live.fox.AppConfig;
import com.live.fox.utils.StringUtils;
import com.live.fox.utils.Utils;

import java.io.Serializable;
import java.util.ArrayList;

public class User implements Serializable {

    private Float anchorCoin;  // 主播金币
    private Long anchorExp;  //主播经验
    private Integer anchorLevel;  //主播等级
    private Integer auth;  //0:否 1:認證中 2:認證成功
    private String avatar;  //头像
    private String constellation;  //星座
    private Float goldCoin ; //金币
    private String hobby;  // 爱好
    private String nickname; //昵称
    private String phone;  // 手机号
    private Integer sex;  //性别0未知1男2女
    private String signature;  //个性签名
    private Long uid;  //用户唯一标识
    private Long userCoin;  //用户币
    private int userLevel;  //用户等级
    private String city;  //地理位置
    private Long fans;  //粉丝数
    private Long follows;  //关注数
    private Boolean isReject = false;  //是否拉黑
    private Boolean isFollow = false;  //是否关注
    private Boolean isRename = false;  //是否改过名
    private Boolean isFans = false; //是否互为关注
    private String imToken;
    private Boolean isFirstLogin = false;  //直播间活动充值按钮是否展示
    private Boolean isSignIn = false; //今日是否签到
    private Integer signInNum;  //连续签到次数
    public Integer manage = 0; //是否超管：0否 1是
    public Integer surplusMovTime = 0;  //剩余观影次数
    public Integer totalMovTime = 0; //总观影次数
    public Integer chatHide = 0;
    public Integer roomHide = 0;
    public Integer rankHide = 0;
    public Long destUid;
    private Boolean isVip = false;
    private Long vipExp;
    private Long receiveCoin = 0L;
    private Long sendCoin = 0L;
    private String gxAvatar;
    private String gxId;
    private String shAvatar;
    private String shId;
    private String area;
    private Integer gxLevel = 0;
    public Long vipUid;
    private Integer shType = 0;
    private Boolean isNotification;
    private ArrayList<Integer> badgeList;
    String address;
    String province;
    Boolean isBlackChat;
    Integer autoUpdownBalance; //是否自动上下分 1 不自动 2自动
    Boolean come = false;
    Integer hasPayPwd;//是否有支付密码 1就是有

    public Integer getHasPayPwd() {
        return hasPayPwd;
    }

    public void setHasPayPwd(Integer hasPayPwd) {
        this.hasPayPwd = hasPayPwd;
    }

    public boolean isCome() {
        return come;
    }

    public void setCome(boolean come) {
        this.come = come;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public ArrayList<Integer> getBadgeList() {
        return badgeList;
    }

    public void setBadgeList(ArrayList<Integer> badgeList) {
        this.badgeList = badgeList;
    }

    public boolean isFirstLogin() {
        return isFirstLogin;
    }

    public void setFirstLogin(Boolean firstLogin) {
        isFirstLogin = firstLogin;
    }

    public boolean isFans() {
        return isFans;
    }

    public void setFans(Boolean fans) {
        isFans = fans;
    }

    public Float getAnchorCoin() {
        return  anchorCoin;
    }

    public void setAnchorCoin(Float anchorCoin) {
        this.anchorCoin = anchorCoin;
    }

    public Long getAnchorExp() {
        return anchorExp;
    }

    public void setAnchorExp(Long anchorExp) {
        this.anchorExp = anchorExp;
    }

    public Integer getAnchorLevel() {
        return anchorLevel;
    }

    public void setAnchorLevel(Integer anchorLevel) {
        this.anchorLevel = anchorLevel;
    }

    public Integer getAuth() {
        return auth;
    }

    public Integer getSurplusMovTime() {
        return surplusMovTime;
    }

    public void setSurplusMovTime(Integer surplusMovTime) {
        this.surplusMovTime = surplusMovTime;
    }

    public Integer getTotalMovTime() {
        return totalMovTime;
    }

    public void setTotalMovTime(Integer totalMovTime) {
        this.totalMovTime = totalMovTime;
    }

    public boolean isVip() {
        return isVip;
    }

    public void setVip(boolean vip) {
        isVip = vip;
    }

    public String getAuthStatus() {
        String str = null;
        switch (auth) {
            case 0:
                str = "未认证";
                break;
            case 1:
                str = "审核中";
                break;
            case 2:
                str = "已认证";
                break;
        }
        return str;
    }

    public void setAuth(Integer auth) {
        this.auth = auth;
    }

    public String getAvatar() {
        if (StringUtils.isEmpty(avatar) || avatar.contains("live-api") || avatar.contains("www.baidu.com")) {
            return "";
        }
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }


    public Float getGoldCoin() {
        return goldCoin;
    }

    public void setGoldCoin(Float goldCoin) {
        this.goldCoin = goldCoin;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Integer getSex() {
        return sex == null ? 1 : sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public Long getUserCoin() {
        return userCoin;
    }

    public void setUserCoin(Long userCoin) {
        this.userCoin = userCoin;
    }

    public Integer getUserLevel() {
        return userLevel;
    }

    public void setUserLevel(Integer userLevel) {
        this.userLevel = userLevel;
    }

    public Long getFans() {
        return fans;
    }

    public void setFans(Long fans) {
        this.fans = fans;
    }

    public Long getFollows() {
        return follows;
    }

    public void setFollows(Long follows) {
        this.follows = follows;
    }

    public boolean isReject() {
        return isReject == null ? false : isReject.booleanValue();
    }

    public void setReject(Boolean reject) {
        isReject = reject;
    }

    public boolean isFollow() {
        return isFollow == null ? false : isFollow.booleanValue();
    }

    public void setFollow(Boolean follow) {
        isFollow = follow;
    }


    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getImToken() {
        return imToken;
    }

    public void setImToken(String imToken) {
        this.imToken = imToken;
    }

    public boolean isSignIn() {
        return isSignIn;
    }

    public void setSignIn(Boolean signIn) {
        isSignIn = signIn;
    }


    public Long getReceiveCoin() {
        return receiveCoin;
    }

    public void setReceiveCoin(Long receiveCoin) {
        this.receiveCoin = receiveCoin;
    }

    public Long getSendCoin() {
        return sendCoin;
    }

    public void setSendCoin(Long sendCoin) {
        this.sendCoin = sendCoin;
    }

    public String getGxAvatar() {
        return gxAvatar;
    }

    public void setGxAvatar(String gxAvatar) {
        this.gxAvatar = gxAvatar;
    }

    public String getGxId() {
        return gxId;
    }

    public void setGxId(String gxId) {
        this.gxId = gxId;
    }

    public String getShAvatar() {
        return shAvatar;
    }

    public void setShAvatar(String shAvatar) {
        this.shAvatar = shAvatar;
    }

    public String getShId() {
        return shId;
    }

    public void setShId(String shId) {
        this.shId = shId;
    }

    public void setVipExp(Long vipExp) {
        this.vipExp = vipExp;
    }

    public Integer getManage() {
        return manage == null ? 0 : manage;
    }

    public void setManage(Integer manage) {
        this.manage = manage;
    }

    public Boolean getFollow() {
        return isFollow;
    }

    public Boolean getFirstLogin() {
        return isFirstLogin;
    }

    public Boolean getSignIn() {
        return isSignIn;
    }

    public Integer getGxLevel() {
        return gxLevel;
    }

    public void setGxLevel(Integer gxLevel) {
        this.gxLevel = gxLevel;
    }

    public Integer getShType() {
        return shType;
    }

    public void setShType(Integer shType) {
        this.shType = shType;
    }

    public Boolean getRename() {
        return isRename;
    }

    public void setRename(Boolean rename) {
        isRename = rename;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public boolean isBlackChat() {
        return isBlackChat;
    }

    public void setBlackChat(boolean blackChat) {
        isBlackChat = blackChat;
    }

    public boolean isNotification() {
        return isNotification;
    }

    public void setNotification(boolean notification) {
        isNotification = notification;
    }

    public Long getDestUid() {
        return destUid;
    }

    public void setDestUid(Long destUid) {
        this.destUid = destUid;
    }

    public Anchor covertToAnchor() {
        Anchor anchor = new Anchor();
        anchor.setAnchorId(getUid());
        anchor.setAvatar(getAvatar());
        anchor.setNickname(getNickname());
        return anchor;
    }

    //登录是用户是否是超管
    public boolean isSuperManager() {
        return manage == 1;
    }

    //登录是用户是否是家族长
    // 2主播 ，3 家族长 ，4超管
    public boolean isFamilyManager() {
        for (Integer i : badgeList) {
            if (i == 3) {
                return true;
            }
        }
        return false;
    }


    public Integer getAutoUpdownBalance() {
        return autoUpdownBalance;
    }

    public void setAutoUpdownBalance(Integer autoUpdownBalance) {
        this.autoUpdownBalance = autoUpdownBalance;
    }



}
