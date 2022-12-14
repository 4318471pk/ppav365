package com.live.fox.entity;

import java.io.Serializable;

/**
 * 第三方登录 QQ返回数据 对应的实体类
 */

public class QQLogin implements Serializable {

    private int ret;
    private String msg;
    private int is_lost;
    private String nickname;
    private String gender;
    private String province;
    private String city;
    private String figureurl;
    private String figureurl_1;
    private String figureurl_2;
    private String figureurl_qq_1;
    private String figureurl_qq_2;
    private String is_yellow_vip;
    private String vip;
    private String yellow_vip_level;
    private String level;
    private String is_yellow_year_vip;



    public int getRet() {
        return ret;
    }

    public void setRet(int ret) {
        this.ret = ret;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getIs_lost() {
        return is_lost;
    }

    public void setIs_lost(int is_lost) {
        this.is_lost = is_lost;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getFigureurl() {
        return figureurl;
    }

    public void setFigureurl(String figureurl) {
        this.figureurl = figureurl;
    }

    public String getFigureurl_1() {
        return figureurl_1;
    }

    public void setFigureurl_1(String figureurl_1) {
        this.figureurl_1 = figureurl_1;
    }

    public String getFigureurl_2() {
        return figureurl_2;
    }

    public void setFigureurl_2(String figureurl_2) {
        this.figureurl_2 = figureurl_2;
    }

    public String getFigureurl_qq_1() {
        return figureurl_qq_1;
    }

    public void setFigureurl_qq_1(String figureurl_qq_1) {
        this.figureurl_qq_1 = figureurl_qq_1;
    }

    public String getFigureurl_qq_2() {
        return figureurl_qq_2;
    }

    public void setFigureurl_qq_2(String figureurl_qq_2) {
        this.figureurl_qq_2 = figureurl_qq_2;
    }

    public String getIs_yellow_vip() {
        return is_yellow_vip;
    }

    public void setIs_yellow_vip(String is_yellow_vip) {
        this.is_yellow_vip = is_yellow_vip;
    }

    public String getVip() {
        return vip;
    }

    public void setVip(String vip) {
        this.vip = vip;
    }

    public String getYellow_vip_level() {
        return yellow_vip_level;
    }

    public void setYellow_vip_level(String yellow_vip_level) {
        this.yellow_vip_level = yellow_vip_level;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getIs_yellow_year_vip() {
        return is_yellow_year_vip;
    }

    public void setIs_yellow_year_vip(String is_yellow_year_vip) {
        this.is_yellow_year_vip = is_yellow_year_vip;
    }



//    //
//    public static void saveUserInfo(Context context, String json){
//        QQLogin qqLogin = new Gson().fromJson(json, QQLogin.class);
//        //获取SharedPreferences对象
//        SharedPreferences sharedPre=context.getSharedPreferences("qquser", context.MODE_PRIVATE);
//        //获取Editor对象
//        SharedPreferences.Editor editor=sharedPre.edit();
//        //设置参数
//        editor.putString("username", qqLogin.getNickname());
//        editor.putString("thumhead", qqLogin.getFigureurl_1());
//        editor.putString("head", qqLogin.getFigureurl_2());
//        //提交
//        editor.commit();
//    }
//
//    public static void clearUserInfo(Context context){
//        //获取SharedPreferences对象
//        SharedPreferences sharedPre=context.getSharedPreferences("qquser", context.MODE_PRIVATE);
//        //获取Editor对象
//        SharedPreferences.Editor editor=sharedPre.edit();
//        editor.clear();
//        editor.commit();
//
//    }
//
//    public static QQLogin getUserInfo(Context context){
//        QQLogin qqLogin = new QQLogin();
//        //获取SharedPreferences对象
//        SharedPreferences sharedPre=context.getSharedPreferences("qquser", context.MODE_PRIVATE);
//        qqLogin.setNickname(sharedPre.getString("username", " "));
//        qqLogin.setFigureurl_1(sharedPre.getString("thumhead", " "));
//        qqLogin.setFigureurl_2(sharedPre.getString("head", " "));
//        return qqLogin;
//    }
//
//    public static String getQQName(Context context){
//        QQLogin qqLogin = getUserInfo(context);
//        return qqLogin.getNickname();
//    }
//
//    public static String getQQThumHeadUrl(Context context){
//        QQLogin qqLogin = getUserInfo(context);
//        return qqLogin.getFigureurl_1();
//    }
//
//    public static String getQQHeadUrl(Context context){
//        QQLogin qqLogin = getUserInfo(context);
//        return qqLogin.getFigureurl_2();
//    }
//
//
//
//    public static boolean isLogin(Context context){
//        SharedPreferences sharedPre=context.getSharedPreferences("qquser", context.MODE_PRIVATE);
//        return !StringUtils.isEmpty(sharedPre.getString("username", " "));
//    }


}
