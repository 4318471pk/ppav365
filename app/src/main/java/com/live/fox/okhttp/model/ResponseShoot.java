package com.live.fox.okhttp.model;

/**
 * User: wy
 * Date: 2017/10/25
 * Time: 13:16
 * 视频数据返回格式
 */
public class ResponseShoot {

    private String result;
    private String code;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }


    public String getErrorcode() {
        return getCode();
    }
}
