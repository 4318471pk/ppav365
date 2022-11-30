package com.live.fox.entity;

public class ActBean {

    //[{"id":4,"activityName":"1","activityCode":"1","activityType":2,"activityStatus":1,"beginTime":1666108800000,"endTime":1666972800000,"iconUrl":"https:\/\/alive13-test.oss-cn-hongkong.aliyuncs.com\/live\/819e0405-5223-4910-80a7-db058a70fa59.jpg","imgUrl":"https:\/\/alive13-test.oss-cn-hongkong.aliyuncs.com\/live\/c142e7b4-515c-40fb-964f-79b74a5de544.jpg","activityMoudle":null,"activityCategory":2,"jumpType":1,"jumpCodeType":1,"content":"1","auth":1,"sort":1,"remark":"1","createTime":1667228586836,"updateTime":1667228586836,"operator":"zhaojun"}]    int id;
    String activityName;
    int activityStatus;
    int activityType; //活动类型 1长期活动 2特殊活动
    String content;
    int type;
    String iconUrl;
    String imgUrl;

    Integer jumpCodeType; //1.内链:模块:充值模块?type=1、直播间type=2、游戏type=3      |2.外链:5.网页
    Integer jumpType;	//跳转类型:1内链 2外链

    long beginTime;
    long endTime;

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public int getActivityStatus() {
        return activityStatus;
    }

    public void setActivityStatus(int activityStatus) {
        this.activityStatus = activityStatus;
    }

    public int getActivityType() {
        return activityType;
    }

    public void setActivityType(int activityType) {
        this.activityType = activityType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public int getJumpCodeType() {
        return jumpCodeType==null?0:jumpCodeType;
    }

    public void setJumpCodeType(int jumpCodeType) {
        this.jumpCodeType = jumpCodeType;
    }

    public int getJumpType() {
        return jumpType==null?0:jumpType;
    }

    public void setJumpType(int jumpType) {
        this.jumpType = jumpType;
    }

    public long getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(long beginTime) {
        this.beginTime = beginTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }
}
