package com.live.fox.entity;


import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * 充值价格
 */
public class RecharegPrice implements MultiItemEntity {

    private int id;
    public final static int VIEW_OFFICAL_RECHARGE = 2;
    public final static int VIEW_UNOFFICAL_RECHARGE = 3;
    private String code;
    private int type;
    private double goldCoin;
    private long userRmb;
    private String remark;
    private int status;
    private int sort;
    private long createTime;
    private long updateTime;
    private int viewTpye = VIEW_UNOFFICAL_RECHARGE;
    private String operator;

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public void setGoldCoin(double goldCoin) {
        this.goldCoin = goldCoin;
    }

    public double getGoldCoin() {
        return goldCoin;
    }

    public long getUserRmb() {
        return userRmb;
    }

    public void setUserRmb(long userRmb) {
        this.userRmb = userRmb;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getRemark() {
        return remark;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public int getSort() {
        return sort;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getOperator() {
        return operator;
    }

    @Override
    public int getItemType() {
        return this.viewTpye;
    }

    public void setViewTpye(int viewTpye) {
        this.viewTpye = viewTpye;
    }

    public static List<Integer> getOffcialRecharge() {
        List list = new ArrayList<Integer>();
        list.add(50000);
        list.add(100000);
        list.add(200000);
        list.add(500000);
        list.add(1000000);
        list.add(5000000);
        list.add(10000000);
        list.add(100000000);
        return list;

    }


}