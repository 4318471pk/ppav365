package com.live.fox.entity.response;

public class Nums {
    public Integer num = 1;
    public boolean check = false;//是否选择

    public Nums(Integer num, boolean check) {
        this.num = num;
        this.check = check;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }
}
