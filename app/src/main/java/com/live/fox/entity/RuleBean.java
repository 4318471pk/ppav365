package com.live.fox.entity;

public class RuleBean {

    /**
     * id : 1
     * type : 1
     * content : 你好我是波波1号
     * createTime : 1616138676888
     * operatorva : null
     */

    private int id;
    private int type;
    private String content;
    private String createTime;
    private Object operatorva;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public Object getOperatorva() {
        return operatorva;
    }

    public void setOperatorva(Object operatorva) {
        this.operatorva = operatorva;
    }
}
