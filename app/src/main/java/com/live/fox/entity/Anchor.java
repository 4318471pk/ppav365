package com.live.fox.entity;


import java.io.Serializable;
import java.util.List;


public class Anchor implements Serializable {

    private long anchorId = 0;  //主播ID
    private long uid;

    private String avatar = "";  //头像

    private String loc = "";  //城市或者距離

    int liveId = 0;
    private String nickname = "";  //昵称

    private String pullStreamUrl = "";  //直播拉流地址
    private String pushStreamUrl;

    private boolean follow = false;  //是否关注

    private int zb = 0;

    private boolean leave = false;  //是否离开

    private int num = 0;  //人数

    private String tags = "-1"; //标签 1--颜值  2--热舞  3--声控  4--才艺

    //房间类型 0免费 1付费
    private int type = 0;
    private int price = 0;

    private long rq = 0;

    //广告类需要用到的属性
    String imgUrl;
    String jumpUrl;
    String title;

    String adJumpUrl;
    int isAd = 0; ////0否1是

    //房间类型 0普通房间 2广告Banner
    int roomType = 0;

    private boolean pking;
    private String signature;
    int carId;

    //Banner广告类需要用到的属性
    String content;
    int openWay = 1;
    int pid;

    int toy = 0; //0否1是
    boolean roomManager = false; //是否是房管
    private List<LiveStartLotteryEntity> liveStartLottery;
    private int liveStatus = 3;
    private int isPreview = 3;  //1为不可预览
    private boolean isFromMessage;
    private int showType; //是否展示入场动画：0展示，1不展示
    private int level;
    private int isRoomPreview; //0:正常房间，1：预览直播间
    private int roomHide = -1;  //0:正常房间，1：预览直播间

    public Anchor() {
    }

    public int getIsPreview() {
        return isPreview;
    }

    public void setIsPreview(int isPreview) {
        this.isPreview = isPreview;
    }

    public int getLiveStatus() {
        return liveStatus;
    }

    public void setLiveStatus(int liveStatus) {
        this.liveStatus = liveStatus;
    }

    public long getAnchorId() {
        return anchorId;
    }

    public void setAnchorId(long anchorId) {
        this.anchorId = anchorId;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getLoc() {
        return loc;
    }

    public void setLoc(String loc) {
        this.loc = loc;
    }

    public int getLiveId() {
        return liveId;
    }

    public void setLiveId(int liveId) {
        this.liveId = liveId;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPullStreamUrl() {
        return pullStreamUrl;
    }

    public void setPullStreamUrl(String pullStreamUrl) {
        this.pullStreamUrl = pullStreamUrl;
    }

    public String getPushStreamUrl() {
        return pushStreamUrl;
    }

    public void setPushStreamUrl(String pushStreamUrl) {
        this.pushStreamUrl = pushStreamUrl;
    }

    public boolean isFollow() {
        return follow;
    }

    public void setFollow(boolean follow) {
        this.follow = follow;
    }

    public int getZb() {
        return zb;
    }

    public void setZb(int zb) {
        this.zb = zb;
    }

    public boolean isLeave() {
        return leave;
    }

    public void setLeave(boolean leave) {
        this.leave = leave;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public long getRq() {
        return rq;
    }

    public void setRq(long rq) {
        this.rq = rq;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getJumpUrl() {
        return jumpUrl;
    }

    public void setJumpUrl(String jumpUrl) {
        this.jumpUrl = jumpUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAdJumpUrl() {
        return adJumpUrl;
    }

    public void setAdJumpUrl(String adJumpUrl) {
        this.adJumpUrl = adJumpUrl;
    }

    public int getIsAd() {
        return isAd;
    }

    public void setIsAd(int isAd) {
        this.isAd = isAd;
    }

    public int getRoomType() {
        return roomType;
    }

    public void setRoomType(int roomType) {
        this.roomType = roomType;
    }

    public boolean isPking() {
        return pking;
    }

    public void setPking(boolean pking) {
        this.pking = pking;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public int getCarId() {
        return carId;
    }

    public void setCarId(int carId) {
        this.carId = carId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getOpenWay() {
        return openWay;
    }

    public void setOpenWay(int openWay) {
        this.openWay = openWay;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public int getToy() {
        return toy;
    }

    public void setToy(int toy) {
        this.toy = toy;
    }

    public boolean isRoomManager() {
        return roomManager;
    }

    public void setRoomManager(boolean roomManager) {
        this.roomManager = roomManager;
    }

    public boolean isFromMessage() {
        return isFromMessage;
    }

    public void setFromMessage(boolean fromMessage) {
        isFromMessage = fromMessage;
    }

    public int getShowType() {
        return showType;
    }

    public void setShowType(int showType) {
        this.showType = showType;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getIsRoomPreview() {
        return isRoomPreview;
    }

    public void setIsRoomPreview(int isRoomPreview) {
        this.isRoomPreview = isRoomPreview;
    }


    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public int getRoomHide() {
        return roomHide;
    }

    public void setRoomHide(int roomHide) {
        this.roomHide = roomHide;
    }

    public List<LiveStartLotteryEntity> getLiveStartLottery() {
        return liveStartLottery;
    }

    public void setLiveStartLottery(List<LiveStartLotteryEntity> liveStartLottery) {//LiveStartLotteryBean
        this.liveStartLottery = liveStartLottery;
    }

}
