package com.live.fox.entity;

import java.util.List;

public class LivingLotteryListBean {


    private List<ItemsBean> items;

    public List<ItemsBean> getItems() {
        return items;
    }

    public void setItems(List<ItemsBean> items) {
        this.items = items;
    }

    public static class ItemsBean {
        /**
         * id : 2
         * name : 热门
         * ename : hot
         * code : hot
         * smallImg : null
         * bigImg : null
         * status : 1
         * type : 3
         * sort : 11
         * gameList : 8,9,10
         * createTime : 1
         * updateTime : 1
         * operator : 1
         * configGameBaseList : [{"id":8,"platform":"zy","gameCode":"qznn","gameName":"强庄牛牛","selfmode":1,"smallImg":"1","logImg":"1","bigImg":"1","controllKill":1,"killRate":10,"style":1,"closeBefore":5,"delaySettle":0,"betSingleMin":1,"betSingleMax":1000000,"betCountMax":1000000,"gameType":1,"sort":1,"status":1,"createTime":1,"updateTime":1,"operator":"1"},{"id":9,"platform":"zy","gameCode":"zjh","gameName":"扎金花","selfmode":1,"smallImg":"1","logImg":"1","bigImg":"1","controllKill":1,"killRate":10,"style":1,"closeBefore":5,"delaySettle":0,"betSingleMin":1,"betSingleMax":1000000,"betCountMax":1000000,"gameType":1,"sort":1,"status":1,"createTime":1,"updateTime":1,"operator":"1"},{"id":10,"platform":"zy","gameCode":"ermj","gameName":"二人麻将","selfmode":1,"smallImg":"1","logImg":"1","bigImg":"1","controllKill":1,"killRate":10,"style":1,"closeBefore":5,"delaySettle":0,"betSingleMin":1,"betSingleMax":1000000,"betCountMax":1000000,"gameType":1,"sort":1,"status":1,"createTime":1,"updateTime":1,"operator":"1"}]
         */

        private int id;
        private String name;
        private String ename;
        private String code;
        private Object smallImg;
        private Object bigImg;
        private int status;
        private int type;
        private int sort;
        private String gameList;
        private long createTime;
        private long updateTime;
        private String operator;
        private List<ConfigGameBaseListBean> configGameBaseList;


        private boolean isSelect = false;

        public long getCreateTime() {
            return createTime;
        }

        public void setCreateTime(long createTime) {
            this.createTime = createTime;
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

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
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

        public Object getSmallImg() {
            return smallImg;
        }

        public void setSmallImg(Object smallImg) {
            this.smallImg = smallImg;
        }

        public Object getBigImg() {
            return bigImg;
        }

        public void setBigImg(Object bigImg) {
            this.bigImg = bigImg;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public int getSort() {
            return sort;
        }

        public void setSort(int sort) {
            this.sort = sort;
        }

        public String getGameList() {
            return gameList;
        }

        public void setGameList(String gameList) {
            this.gameList = gameList;
        }


        public String getOperator() {
            return operator;
        }

        public void setOperator(String operator) {
            this.operator = operator;
        }

        public List<ConfigGameBaseListBean> getConfigGameBaseList() {
            return configGameBaseList;
        }

        public void setConfigGameBaseList(List<ConfigGameBaseListBean> configGameBaseList) {
            this.configGameBaseList = configGameBaseList;
        }

        public static class ConfigGameBaseListBean {
            /**
             * id : 8
             * platform : zy
             * gameCode : qznn
             * gameName : 强庄牛牛
             * selfmode : 1
             * smallImg : 1
             * logImg : 1
             * bigImg : 1
             * controllKill : 1
             * killRate : 10
             * style : 1
             * closeBefore : 5
             * delaySettle : 0
             * betSingleMin : 1
             * betSingleMax : 1000000
             * betCountMax : 1000000
             * gameType : 1
             * sort : 1
             * status : 1
             * createTime : 1
             * updateTime : 1
             * operator : 1
             */

            private int id;
            private String platform;
            private String gameCode;
            private String gameName;
            private int selfmode;
            private String smallImg;
            private String logImg;
            private String bigImg;
            private int controllKill;
            private int killRate;
            private int style;
            private int closeBefore;
            private int delaySettle;
            private int betSingleMin;
            private int betSingleMax;
            private int betCountMax;
            private int gameType;
            private int sort;
            private int status;
            private long createTime;
            private long updateTime;
            private String operator;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getPlatform() {
                return platform;
            }

            public void setPlatform(String platform) {
                this.platform = platform;
            }

            public String getGameCode() {
                return gameCode;
            }

            public void setGameCode(String gameCode) {
                this.gameCode = gameCode;
            }

            public String getGameName() {
                return gameName;
            }

            public void setGameName(String gameName) {
                this.gameName = gameName;
            }

            public int getSelfmode() {
                return selfmode;
            }

            public void setSelfmode(int selfmode) {
                this.selfmode = selfmode;
            }

            public String getSmallImg() {
                return smallImg;
            }

            public void setSmallImg(String smallImg) {
                this.smallImg = smallImg;
            }

            public String getLogImg() {
                return logImg;
            }

            public void setLogImg(String logImg) {
                this.logImg = logImg;
            }

            public String getBigImg() {
                return bigImg;
            }

            public void setBigImg(String bigImg) {
                this.bigImg = bigImg;
            }

            public int getControllKill() {
                return controllKill;
            }

            public void setControllKill(int controllKill) {
                this.controllKill = controllKill;
            }

            public int getKillRate() {
                return killRate;
            }

            public void setKillRate(int killRate) {
                this.killRate = killRate;
            }

            public int getStyle() {
                return style;
            }

            public void setStyle(int style) {
                this.style = style;
            }

            public int getCloseBefore() {
                return closeBefore;
            }

            public void setCloseBefore(int closeBefore) {
                this.closeBefore = closeBefore;
            }

            public int getDelaySettle() {
                return delaySettle;
            }

            public void setDelaySettle(int delaySettle) {
                this.delaySettle = delaySettle;
            }

            public int getBetSingleMin() {
                return betSingleMin;
            }

            public void setBetSingleMin(int betSingleMin) {
                this.betSingleMin = betSingleMin;
            }

            public int getBetSingleMax() {
                return betSingleMax;
            }

            public void setBetSingleMax(int betSingleMax) {
                this.betSingleMax = betSingleMax;
            }

            public int getBetCountMax() {
                return betCountMax;
            }

            public void setBetCountMax(int betCountMax) {
                this.betCountMax = betCountMax;
            }

            public int getGameType() {
                return gameType;
            }

            public void setGameType(int gameType) {
                this.gameType = gameType;
            }

            public int getSort() {
                return sort;
            }

            public void setSort(int sort) {
                this.sort = sort;
            }

            public int getStatus() {
                return status;
            }

            public void setStatus(int status) {
                this.status = status;
            }



            public String getOperator() {
                return operator;
            }

            public void setOperator(String operator) {
                this.operator = operator;
            }
        }
    }
}
