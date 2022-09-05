package com.live.fox.entity;

import java.util.List;

public class ZbjlBean{

    /**
     * heartTime : 00:18:1
     * toTalMl : 131.4
     * toTalcpStatement : 0.07
     * toTalffml : 0.1
     * jsons : [{"ffml":0.1,"uid":1028924022,"start_time":1616602079642,"cp_statement":0.02,"heart_time":1616602109878,"nickname":"123更丰富","end_time":1616602114055,"avatar":"http://bw18.oss-cn-hongkong.aliyuncs.com/1028924022_1615968319_avatar.png","totalstartTime":"00:00:34","ml":0.1,"status":0}]
     */

    private String heartTime;
    private double toTalMl;
    private double toTalcpStatement;
    private double toTalffml;
    private List<JsonsBean> jsons;

    public String getHeartTime() {
        return heartTime;
    }

    public void setHeartTime(String heartTime) {
        this.heartTime = heartTime;
    }

    public double getToTalMl() {
        return toTalMl;
    }

    public void setToTalMl(double toTalMl) {
        this.toTalMl = toTalMl;
    }

    public double getToTalcpStatement() {
        return toTalcpStatement;
    }

    public void setToTalcpStatement(double toTalcpStatement) {
        this.toTalcpStatement = toTalcpStatement;
    }

    public double getToTalffml() {
        return toTalffml;
    }

    public void setToTalffml(double toTalffml) {
        this.toTalffml = toTalffml;
    }

    public List<JsonsBean> getJsons() {
        return jsons;
    }

    public void setJsons(List<JsonsBean> jsons) {
        this.jsons = jsons;
    }

    public static class JsonsBean {
        /**
         * ffml : 0.1
         * uid : 1028924022
         * start_time : 1616602079642
         * cp_statement : 0.02
         * heart_time : 1616602109878
         * nickname : 123更丰富
         * end_time : 1616602114055
         * avatar : http://bw18.oss-cn-hongkong.aliyuncs.com/1028924022_1615968319_avatar.png
         * totalstartTime : 00:00:34
         * ml : 0.1
         * status : 0
         */

        private double ffml;
        private long uid;
        private long start_time;
        private double cp_statement;
        private long heart_time;
        private String nickname;
        private long end_time;
        private String avatar;
        private String totalstartTime;
        private double ml;
        private int status;

        public double getFfml() {
            return ffml;
        }

        public void setFfml(double ffml) {
            this.ffml = ffml;
        }

        public long getUid() {
            return uid;
        }

        public void setUid(long uid) {
            this.uid = uid;
        }

        public long getStart_time() {
            return start_time;
        }

        public void setStart_time(long start_time) {
            this.start_time = start_time;
        }

        public double getCp_statement() {
            return cp_statement;
        }

        public void setCp_statement(double cp_statement) {
            this.cp_statement = cp_statement;
        }

        public long getHeart_time() {
            return heart_time;
        }

        public void setHeart_time(long heart_time) {
            this.heart_time = heart_time;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public long getEnd_time() {
            return end_time;
        }

        public void setEnd_time(long end_time) {
            this.end_time = end_time;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getTotalstartTime() {
            return totalstartTime;
        }

        public void setTotalstartTime(String totalstartTime) {
            this.totalstartTime = totalstartTime;
        }

        public double getMl() {
            return ml;
        }

        public void setMl(double ml) {
            this.ml = ml;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }
    }
}
