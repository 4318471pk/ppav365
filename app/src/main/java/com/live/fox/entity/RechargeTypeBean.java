package com.live.fox.entity;

public class RechargeTypeBean {

    //[{"id":1,"name":"微信","ename":"wechatpay","code":"wechatpay","logs":null,"type":2,"status":1,"sort":1,"createTime":1666700735000,"updateTime":1666700735000,"operator":"admin","remark":null},{"id":2,"name":"支付宝","ename":"alipay","code":"alipay","logs":null,"type":1,"status":1,"sort":2,"createTime":1666700735000,"updateTime":1666700735000,"operator":"admin","remark":"1"},{"id":3,"name":"银行转卡","ename":"bank_transfer","code":"bank_transfer","logs":null,"type":3,"status":1,"sort":3,"createTime":1666700735000,"updateTime":1666700735000,"operator":"admin","remark":null},{"id":4,"name":"泰达币","ename":"wallet_usdt","code":"wallet_usdt","logs":null,"type":4,"status":1,"sort":4,"createTime":1666700735000,"updateTime":1666700735000,"operator":"admin","remark":null},{"id":5,"name":"银行汇款","ename":"yhhk","code":"yhhk","logs":null,"type":5,"status":1,"sort":5,"createTime":1666700735000,"updateTime":1666700735000,"operator":"admin","remark":null}]

    int id;
    String name;
    String ename;
    String code;
    String logs;
    int sort;
    int type;
    int status;
    String remark;
    long updateTime;
    boolean isSelect = false;


    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEname() {
        return ename;
    }

    public void setEname(String ename) {
        this.ename = ename;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getLogs() {
        return logs;
    }

    public void setLogs(String logs) {
        this.logs = logs;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }
}
