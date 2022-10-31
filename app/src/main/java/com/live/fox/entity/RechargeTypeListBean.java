package com.live.fox.entity;

import java.util.ArrayList;
import java.util.List;

public class RechargeTypeListBean {

    //[{"id":902,"name":"微信小额","ename":"wechatpay_small","type":2,"merId":"20001","channelCode":"wechatpay1","status":0,"logs":"图片","limitVip":0,"lowest":0,"highest":10000000,"transferBank":null,"amountList":"10,20,50","rate":0,"reward":0,"sort":1,"createTime":1,"updateTime":1,"operator":"admin","supportBank":null,"hierarchyId":"","payFlag":null,"mark":null},{"id":903,"name":"微信扫码","ename":"wechatpay_qr","type":2,"merId":"20002","channelCode":"wechat","status":0,"logs":"1","limitVip":0,"lowest":0,"highest":1000000,"transferBank":null,"amountList":"1000,2000,5000,10000,20000,30000,50000","rate":0,"reward":0,"sort":1,"createTime":1664457539881,"updateTime":1664457539881,"operator":"admin","supportBank":"1,2,3,4,5","hierarchyId":"1,2,3","payFlag":null,"mark":null}]
    int id;
    String merId;
    String name;
    String ename;
    String code;
    String channelCode;
    String logs;
    int sort;
    int type;
    int status;
    int limitVip;
    int lowest;
    int highest;
    String transferBank;
    String amountList;
    String supportBank ;
    String hierarchyId;

    String mark;
    long updateTime;
    boolean isSelect = false;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMerId() {
        return merId;
    }

    public void setMerId(String merId) {
        this.merId = merId;
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

    public String getChannelCode() {
        return channelCode;
    }

    public void setChannelCode(String channelCode) {
        this.channelCode = channelCode;
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

    public int getLimitVip() {
        return limitVip;
    }

    public void setLimitVip(int limitVip) {
        this.limitVip = limitVip;
    }

    public int getLowest() {
        return lowest;
    }

    public void setLowest(int lowest) {
        this.lowest = lowest;
    }

    public int getHighest() {
        return highest;
    }

    public void setHighest(int highest) {
        this.highest = highest;
    }

    public String getTransferBank() {
        return transferBank;
    }

    public void setTransferBank(String transferBank) {
        this.transferBank = transferBank;
    }


    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public String getLogs() {
        return logs;
    }

    public void setLogs(String logs) {
        this.logs = logs;
    }

    public String getAmountList() {
        return amountList;
    }

    public void setAmountList(String amountList) {
        this.amountList = amountList;
    }

    public String getSupportBank() {
        return supportBank;
    }

    public void setSupportBank(String supportBank) {
        this.supportBank = supportBank;
    }

    public String getHierarchyId() {
        return hierarchyId;
    }

    public void setHierarchyId(String hierarchyId) {
        this.hierarchyId = hierarchyId;
    }
}
