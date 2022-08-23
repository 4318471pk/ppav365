/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.live.fox.zfb;

import android.text.TextUtils;

/**
 *
 * @author zwxiang
 */
public class AlipayResponse {
    private String partner;
    private String seller_id;
    private String out_trade_no;
    private String subject;
    private String body;
    private String total_fee;
    private String notify_url;
    private String service;
    private String payment_type;
    private String _input_charset;
    private String it_b_pay;
    private String return_url;
    private String success;
    private String sign;

    public AlipayResponse(String rawResult) {
        if (TextUtils.isEmpty(rawResult))
            return;
        String[] resultParams = rawResult.split("&");//字符串分割成数组
        for (String resultParam : resultParams) {//遍历数组
            if (resultParam.startsWith("success")) {//判断是否是以“”内的字符开头
                success = gatValue(resultParam, "success");//取值 赋值
            }
            if (resultParam.startsWith("return_url")) {
                return_url = gatValue(resultParam, "return_url");
            }
            if (resultParam.startsWith("sign")) {
                sign = gatValue(resultParam, "sign");
            }
            if (resultParam.startsWith("it_b_pay")) {
                it_b_pay = gatValue(resultParam, "it_b_pay");
            }
            if (resultParam.startsWith("_input_charset")) {
                _input_charset = gatValue(resultParam, "_input_charset");
            }
            if (resultParam.startsWith("partner")) {
                partner = gatValue(resultParam, "partner");
            }
            if (resultParam.startsWith("subject")) {
                subject = gatValue(resultParam, "subject");
            }
            if (resultParam.startsWith("body")) {
                body = gatValue(resultParam, "body");
            }
            if (resultParam.startsWith("out_trade_no")) {
                out_trade_no = gatValue(resultParam, "out_trade_no");
            }
            if (resultParam.startsWith("total_fee")) {
                total_fee = gatValue(resultParam, "total_fee");
            }
            if (resultParam.startsWith("notify_url")) {
                notify_url = gatValue(resultParam, "notify_url");
            }
            if (resultParam.startsWith("seller_id")) {
                seller_id = gatValue(resultParam, "seller_id");
            }
            if (resultParam.startsWith("service")) {
                service = gatValue(resultParam, "service");
            }
            if (resultParam.startsWith("payment_type")) {
                payment_type = gatValue(resultParam, "payment_type");
            }
        }

    }
    private String gatValue(String content, String key) {
        String prefix = key + "=\"";
        return content.substring(content.indexOf(prefix) + prefix.length(),
                content.lastIndexOf("\""));
    }

    public String getPartner() {
        return partner;
    }

    public void setPartner(String partner) {
        this.partner = partner;
    }

    public String getSeller_id() {
        return seller_id;
    }

    public void setSeller_id(String seller_id) {
        this.seller_id = seller_id;
    }

    public String getOut_trade_no() {
        return out_trade_no;
    }

    public void setOut_trade_no(String out_trade_no) {
        this.out_trade_no = out_trade_no;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getTotal_fee() {
        return total_fee;
    }

    public void setTotal_fee(String total_fee) {
        this.total_fee = total_fee;
    }

    public String getNotify_url() {
        return notify_url;
    }

    public void setNotify_url(String notify_url) {
        this.notify_url = notify_url;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getPayment_type() {
        return payment_type;
    }

    public void setPayment_type(String payment_type) {
        this.payment_type = payment_type;
    }

    public String get_input_charset() {
        return _input_charset;
    }

    public void set_input_charset(String _input_charset) {
        this._input_charset = _input_charset;
    }

    public String getIt_b_pay() {
        return it_b_pay;
    }

    public void setIt_b_pay(String it_b_pay) {
        this.it_b_pay = it_b_pay;
    }

    public String getReturn_url() {
        return return_url;
    }

    public void setReturn_url(String return_url) {
        this.return_url = return_url;
    }

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}
