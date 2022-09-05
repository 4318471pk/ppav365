package com.live.fox.ui.rank;

import com.live.fox.entity.RankListEntity;


public interface IRankView {

    void error(String msg);

    /**
     * 返回数据说明
     * 这里最多返回四个头部的前三名
     * 根据类型不同返回不同的列表
     * type 1：有四个类别
     * type 2--3：有三个类别
     *
     * @param rankListEntity 返回顶部数据
     */
    void requestResult(RankListEntity rankListEntity);

}
