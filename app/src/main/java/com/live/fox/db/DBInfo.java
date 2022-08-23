package com.live.fox.db;


public class DBInfo {

    public static final String DB_NAME = "hd.db";
    public static final int DB_VERSION = 2;

    public static class Table {
        public static final String User = "user";
        public static final String Gift = "gift";
        public static final String Badge = "badge";
        public static final String CHAT_LIST = "chat_list";
        public static final String CHAT_CONTENT = "chat";
        public static final String ADMISSION_ANIMATION = "admission_animation";

        public static final String GIFT_CREATE =
                " CREATE TABLE IF NOT EXISTS " + Gift + " (id integer primary key autoincrement, " +
                        " bimgs integer not null, " +
                        " cover text not null, " +
                        " descript text not null, " +
                        " duration real not null, " +
                        " gid integer not null, " +
                        " gname text not null, " +
                        " goldCoin integer not null, " +
                        " isShow integer not null," +
                        " pimgs integer not null, " +
                        " playType integer not null, " +
                        " resourceUrl text not null, " +
                        " simgs integer not null," +
                        " sort integer not null, " +
                        " tags text not null, " +
                        " type integer not null, " +
                        " version integer not null ); ";

        public static final String BADGE_CREATE =
                " CREATE TABLE IF NOT EXISTS " + Badge + " (id integer primary key autoincrement, " +
                        " bid integer not null, " +
                        " descript text not null, " +
                        " logoUrl text not null, " +
                        " name text not null); ";

        public static final String CHAT_LIST_CREATE =
                " CREATE TABLE IF NOT EXISTS " + CHAT_LIST +
                        " (otherUid long primary key, " +
                        " avatar text not null, " +
                        " letterId long not null, " +
                        " loginUid long not null, " +
                        " nickname text not null, " +
                        " sex int not null, " +
                        " content text not null, " +
                        " userLevel integer not null," +
                        " timestamp long not null," +
                        " unReadCount integer not null); ";

        /*
         * SendUid是发送人Id  OtherUid是目标Id  SendUid和OtherUid有些情况下是相等的
         */
        public static final String CHAT_CONTENT_CREATE =
                " CREATE TABLE IF NOT EXISTS " + CHAT_CONTENT + " (id integer primary key autoincrement, " +
                        " sendUid long not null, " +
                        " otherUid long not null," +
                        " avatar text not null, " +
                        " letterId long not null, " +
                        " nickname text not null, " +
                        " sex integer not null, " +
                        " userLevel integer not null," +
                        " content text not null," +
                        " msgtype integer not null," +
                        " timestamp long not null); ";

        public static final String ADMISSION_ANIMATION_CREATE =
                " CREATE TABLE IF NOT EXISTS " + ADMISSION_ANIMATION + " (id integer primary key autoincrement, " +
                        " cover text not null, " +
                        " duration integer not null," +
                        " gid integer not null, " +
                        " gname text not null, " +
                        " level integer not null, " +
                        " playType integer not null, " +
                        " resourceUrl text not null," +
                        " type integer not null); ";


        public static final String USER_INFO =
                " CREATE TABLE IF NOT EXISTS " + User + " (id integer primary key autoincrement, "
                        + "user text not null) ";
    }


}
