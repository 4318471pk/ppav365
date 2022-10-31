package com.live.fox.entity;

public class DiamondListBean {

    //[{"id":10018,"groupId":10010,"title":"233钻石","code":"233","value":"30","type":2,"remark":"233钻石，键对应钻石,值对应金币","status":1,"createTime":1665547916000,"updateTime":1665547916000,"operator":"admin","showFront":1,"sort":1},{"id":10019,"groupId":10010,"title":"388钻石","code":"388","value":"50","type":2,"remark":"1","status":1,"createTime":1665547916000,"updateTime":1665547916000,"operator":"admin","showFront":1,"sort":1},{"id":10020,"groupId":10010,"title":"1000钻石","code":"1000","value":"100","type":2,"remark":"1000钻石100元","status":1,"createTime":1665547916000,"updateTime":1665547916000,"operator":"admin","showFront":1,"sort":1},{"id":10021,"groupId":10010,"title":"2000钻石","code":"2000","value":"200","type":2,"remark":"2000钻石200元","status":1,"createTime":1665547916000,"updateTime":1665547916000,"operator":"admin","showFront":1,"sort":1}]
    int id;
    int groupId;
    String title;
    String code; //钻
    String value;
    int type;
    String remark;
    int status;
    int showFront;
    boolean isSelect = false;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getShowFront() {
        return showFront;
    }

    public void setShowFront(int showFront) {
        this.showFront = showFront;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }
}
