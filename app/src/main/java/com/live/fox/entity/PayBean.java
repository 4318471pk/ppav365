package com.live.fox.entity;

public class PayBean {


    /**
     * mode : url
     * rebate : 14
     * sign : b17751837b637543fe3bc9c31a234031
     * payUrl : http://8.212.20.55:8666/html/index8.html?t=99&appid=2021003154687926&order_id=20221208163710191488&sdk=alipay_root_cert_sn%3d687b59193f3f462dd5336e5abf83c5d8_02941eef3187dddf3d3b83462e1dfcf6%26alipay_sdk%3dalipay-sdk-php-easyalipay-20191227%26app_cert_sn%3d36960de74865f945ec5613c01f78fca7%26app_id%3d2021002175695232%26biz_content%3d%257B%2522biz_scene%2522%253A%2522PERSONAL_PAY%2522%252C%2522business_params%2522%253A%2522%257B%255C%2522payer_binded_alipay_uid%255C%2522%253A%255C%25222088222354004402%255C%2522%252C%255C%2522sub_biz_scene%255C%2522%253A%255C%2522REDPACKET%255C%2522%257D%2522%252C%2522order_title%2522%253A%252220221208163710191488%2522%252C%2522out_biz_no%2522%253A%252220221208163710191488%2522%252C%2522product_code%2522%253A%2522STD_RED_PACKET%2522%252C%2522trans_amount%2522%253A%252299.99%2522%257D%26charset%3dutf-8%26format%3dJSON%26method%3dalipay.fund.trans.app.pay%26sign%3dLfPintlsb7v7q5c8zfDy6SqEVEeSWc6YXthVXl1XhLlHjN3Lrg%252FCUgDRBidYsnFVOxPbff%252BiH%252F5Kfiq9jSh8J%252BTTA0mO4dyFIAkTXS9sj5gb5cT1kF%252FDPomCciS6lKuxHzF3vwyhIc1j0svur%252BX5aqDPZqX87dDQ1w50WpDaLtbjpc6yOJGnsGphsNXVAri0UxCkQYc4xpiQjwj6qv4GA5EVeWY89QjsQW3ckI1khXxf%252BD0gttjtOj8j46BogZ%252FL3hGZhhnwZ%252B2DccZDSY7GVfV2YtvLFwSHR%252FUgjYBPy%252Fl1xEycCsRDitnLHSRwe065nKtlpleGFMaeVOH6oqrbNA%253D%253D%26sign_type%3dRSA2%26timestamp%3d2022-12-08%2b16%253A37%253A10%26version%3d1.0
     * oid : WNSY120816370936951682
     */

    private MsgBean msg;
    /**
     * msg : {"mode":"url","rebate":"14","sign":"b17751837b637543fe3bc9c31a234031","payUrl":"http://8.212.20.55:8666/html/index8.html?t=99&appid=2021003154687926&order_id=20221208163710191488&sdk=alipay_root_cert_sn%3d687b59193f3f462dd5336e5abf83c5d8_02941eef3187dddf3d3b83462e1dfcf6%26alipay_sdk%3dalipay-sdk-php-easyalipay-20191227%26app_cert_sn%3d36960de74865f945ec5613c01f78fca7%26app_id%3d2021002175695232%26biz_content%3d%257B%2522biz_scene%2522%253A%2522PERSONAL_PAY%2522%252C%2522business_params%2522%253A%2522%257B%255C%2522payer_binded_alipay_uid%255C%2522%253A%255C%25222088222354004402%255C%2522%252C%255C%2522sub_biz_scene%255C%2522%253A%255C%2522REDPACKET%255C%2522%257D%2522%252C%2522order_title%2522%253A%252220221208163710191488%2522%252C%2522out_biz_no%2522%253A%252220221208163710191488%2522%252C%2522product_code%2522%253A%2522STD_RED_PACKET%2522%252C%2522trans_amount%2522%253A%252299.99%2522%257D%26charset%3dutf-8%26format%3dJSON%26method%3dalipay.fund.trans.app.pay%26sign%3dLfPintlsb7v7q5c8zfDy6SqEVEeSWc6YXthVXl1XhLlHjN3Lrg%252FCUgDRBidYsnFVOxPbff%252BiH%252F5Kfiq9jSh8J%252BTTA0mO4dyFIAkTXS9sj5gb5cT1kF%252FDPomCciS6lKuxHzF3vwyhIc1j0svur%252BX5aqDPZqX87dDQ1w50WpDaLtbjpc6yOJGnsGphsNXVAri0UxCkQYc4xpiQjwj6qv4GA5EVeWY89QjsQW3ckI1khXxf%252BD0gttjtOj8j46BogZ%252FL3hGZhhnwZ%252B2DccZDSY7GVfV2YtvLFwSHR%252FUgjYBPy%252Fl1xEycCsRDitnLHSRwe065nKtlpleGFMaeVOH6oqrbNA%253D%253D%26sign_type%3dRSA2%26timestamp%3d2022-12-08%2b16%253A37%253A10%26version%3d1.0","oid":"WNSY120816370936951682"}
     * code : 200
     */

    private int code;

    public MsgBean getMsg() {
        return msg;
    }

    public void setMsg(MsgBean msg) {
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public static class MsgBean {
        private String mode;
        private String rebate;
        private String sign;
        private String payUrl;
        private String oid;

        public String getMode() {
            return mode;
        }

        public void setMode(String mode) {
            this.mode = mode;
        }

        public String getRebate() {
            return rebate;
        }

        public void setRebate(String rebate) {
            this.rebate = rebate;
        }

        public String getSign() {
            return sign;
        }

        public void setSign(String sign) {
            this.sign = sign;
        }

        public String getPayUrl() {
            return payUrl;
        }

        public void setPayUrl(String payUrl) {
            this.payUrl = payUrl;
        }

        public String getOid() {
            return oid;
        }

        public void setOid(String oid) {
            this.oid = oid;
        }
    }
}
