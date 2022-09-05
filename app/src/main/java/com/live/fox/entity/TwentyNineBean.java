package com.live.fox.entity;

import java.util.List;

public class TwentyNineBean {

    /**
     * expect : 202101150828
     * name : pk10
     * nickName : 北京赛车
     * protocol : 29
     * resultList : [5,6,1,7,2,8,3,9,4,10]
     */

    private String expect;
    private String name;
    private String nickName;
    private int protocol;
    private List<Integer> resultList;

    public String getExpect() {
        return expect;
    }

    public void setExpect(String expect) {
        this.expect = expect;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public int getProtocol() {
        return protocol;
    }

    public void setProtocol(int protocol) {
        this.protocol = protocol;
    }

    public List<Integer> getResultList() {
        return resultList;
    }

    public void setResultList(List<Integer> resultList) {
        this.resultList = resultList;
    }
}
