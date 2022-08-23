package com.live.fox.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.live.fox.common.CommonApp;
import com.live.fox.entity.AdmissionEntity;
import com.live.fox.entity.Badge;
import com.live.fox.entity.Gift;
import com.live.fox.entity.Letter;
import com.live.fox.entity.LetterList;
import com.live.fox.entity.User;
import com.live.fox.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * User: hqs
 */
public class DataBase {

    private static volatile DataBase dbInstance = null;
    private final DBHelper dbHelper;
    private SQLiteDatabase sqLiteDatabase;
    private final AtomicInteger mOpenCounter = new AtomicInteger();

    private DataBase(Context context) {
        dbHelper = new DBHelper(context);
    }

    public static DataBase getDbInstance() {
        if (dbInstance == null) {
            synchronized (DataBase.class) {
                if (dbInstance == null) {
                    dbInstance = new DataBase(CommonApp.getInstance());
                }
            }
        }
        return dbInstance;
    }

    private synchronized void open() {
        if (mOpenCounter.incrementAndGet() == 1) {
            sqLiteDatabase = dbHelper.getWritableDatabase();
        }
    }

    private synchronized void close() {
        if (mOpenCounter.decrementAndGet() == 0) {
            sqLiteDatabase.close();
        }
    }

    /**
     * 存储用户数据
     *
     * @param userJson 用户数据json
     */
    public void insertUser(String userJson) {
        open();
        ContentValues values = new ContentValues();
        values.put("user", userJson);
        sqLiteDatabase.replaceOrThrow(DBInfo.Table.User, null, values);
        close();
    }

    /**
     * 获取用户
     */
    public String getUser() {
        open();
        String sql = " select * from " + DBInfo.Table.User;
        String userJson = "";
        Cursor cursor = sqLiteDatabase.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            userJson = cursor.getString(cursor.getColumnIndexOrThrow("user"));
        }
        cursor.close();
        close();
        return userJson;
    }

    /**
     * 删除用户
     */
    public void deleteUserInfo() {
        open();
        sqLiteDatabase.delete(DBInfo.Table.User, null, null);
        close();
    }


    public void insertGift(Gift gift) {
        open();
        ContentValues values = new ContentValues();
        values.put("bimgs", gift.getBimgs());
        values.put("cover", gift.getCover());
        values.put("descript", gift.getDescript());
        values.put("duration", gift.getDuration());
        values.put("gid", gift.getGid());
        values.put("gname", gift.getGname().toString());
        values.put("goldCoin", gift.getGoldCoin());
        values.put("isShow", gift.getIsShow());
        values.put("pimgs", gift.getPimgs());
        values.put("playType", gift.getPlayType());
        values.put("resourceUrl", gift.getResourceUrl());
        values.put("simgs", gift.getSimgs());
        values.put("sort", gift.getSort());
        values.put("tags", gift.getTags());
        values.put("type", gift.getType());
        values.put("version", gift.getVersion());

        sqLiteDatabase.replaceOrThrow(DBInfo.Table.Gift, null, values);
        close();
    }

    /**
     * 更新数据库
     *
     * @param giftList 礼物
     */
    public void insertGiftList(List<Gift> giftList) {
        open();
        for (int i = 0; i < giftList.size(); i++) {
            Gift gift = giftList.get(i);
            ContentValues values = new ContentValues();
            values.put("bimgs", gift.getBimgs());
            values.put("cover", gift.getCover());
            if (TextUtils.isEmpty(gift.getDescript())) {
                values.put("descript", "no");
            } else {
                values.put("descript", gift.getDescript());
            }
            values.put("duration", gift.getDuration());
            values.put("gid", gift.getGid());
            values.put("gname", gift.getGname());
            values.put("goldCoin", gift.getGoldCoin());
            values.put("isShow", gift.getIsShow());
            values.put("pimgs", gift.getPimgs());
            values.put("playType", gift.getPlayType());
            values.put("resourceUrl", gift.getResourceUrl());
            values.put("simgs", gift.getSimgs());
            values.put("sort", gift.getSort());
            values.put("tags", gift.getTags());
            values.put("type", gift.getType());
            values.put("version", gift.getVersion());
            sqLiteDatabase.replaceOrThrow(DBInfo.Table.Gift, null, values);
        }
        close();
    }

    public Gift getGiftByGid(int gid) {
        open();
        String sql = " select * from " + DBInfo.Table.Gift + " where gid = " + gid;
        Cursor cursor = sqLiteDatabase.rawQuery(sql, null);
        Gift gift = new Gift();
        while (cursor.moveToNext()) {
            gift.setBimgs(cursor.getInt(cursor.getColumnIndexOrThrow("bimgs")));
            gift.setCover(cursor.getString(cursor.getColumnIndexOrThrow("cover")));
            gift.setDescript(cursor.getString(cursor.getColumnIndexOrThrow("descript")));
            gift.setDuration(cursor.getFloat(cursor.getColumnIndexOrThrow("duration")));
            gift.setGid(cursor.getInt(cursor.getColumnIndexOrThrow("gid")));
            gift.setGname(cursor.getString(cursor.getColumnIndexOrThrow("gname")));
            gift.setGoldCoin(cursor.getInt(cursor.getColumnIndexOrThrow("goldCoin")));
            gift.setIsShow(cursor.getInt(cursor.getColumnIndexOrThrow("isShow")));
            gift.setPimgs(cursor.getInt(cursor.getColumnIndexOrThrow("pimgs")));
            gift.setPlayType(cursor.getInt(cursor.getColumnIndexOrThrow("playType")));
            gift.setResourceUrl(cursor.getString(cursor.getColumnIndexOrThrow("resourceUrl")));
            gift.setSimgs(cursor.getInt(cursor.getColumnIndexOrThrow("simgs")));
            gift.setSort(cursor.getInt(cursor.getColumnIndexOrThrow("sort")));
            gift.setTags(cursor.getString(cursor.getColumnIndexOrThrow("tags")));
            gift.setType(cursor.getInt(cursor.getColumnIndexOrThrow("type")));
            gift.setVersion(cursor.getInt(cursor.getColumnIndexOrThrow("version")));
            break;
        }
        cursor.close();
        close();
        return gift;
    }


    /**
     * 获取礼物数据
     *
     * @return 礼物列表
     */
    public List<Gift> getPackageGift() {
        open();
        String sql = " select * from " + DBInfo.Table.Gift + " where (isShow = 1 and type = 0) order by sort";
        Cursor cursor = sqLiteDatabase.rawQuery(sql, null);
        List<Gift> giftList = new ArrayList<>();
        while (cursor.moveToNext()) {
            Gift gift = new Gift();
            gift.setBimgs(cursor.getInt(cursor.getColumnIndexOrThrow("bimgs")));
            gift.setCover(cursor.getString(cursor.getColumnIndexOrThrow("cover")));
            gift.setDescript(cursor.getString(cursor.getColumnIndexOrThrow("descript")));
            gift.setDuration(cursor.getFloat(cursor.getColumnIndexOrThrow("duration")));
            gift.setGid(cursor.getInt(cursor.getColumnIndexOrThrow("gid")));
            gift.setGname(cursor.getString(cursor.getColumnIndexOrThrow("gname")));
            gift.setGoldCoin(cursor.getInt(cursor.getColumnIndexOrThrow("goldCoin")));
            gift.setIsShow(cursor.getInt(cursor.getColumnIndexOrThrow("isShow")));
            gift.setPimgs(cursor.getInt(cursor.getColumnIndexOrThrow("pimgs")));
            gift.setPlayType(cursor.getInt(cursor.getColumnIndexOrThrow("playType")));
            gift.setResourceUrl(cursor.getString(cursor.getColumnIndexOrThrow("resourceUrl")));
            gift.setSimgs(cursor.getInt(cursor.getColumnIndexOrThrow("simgs")));
            gift.setSort(cursor.getInt(cursor.getColumnIndexOrThrow("sort")));
            gift.setTags(cursor.getString(cursor.getColumnIndexOrThrow("tags")));
            gift.setType(cursor.getInt(cursor.getColumnIndexOrThrow("type")));
            gift.setVersion(cursor.getInt(cursor.getColumnIndexOrThrow("version")));
            giftList.add(gift);
        }
        cursor.close();
        close();
        return giftList;
    }


    public void deleteAlLGift() {
        open();
        sqLiteDatabase.delete(DBInfo.Table.Gift, null, null);
        close();
    }

    public void deleteGift(long gid) {
        open();
        String whereClause = "gid=?";
        String[] whereArgs = new String[]{String.valueOf(gid)};
        sqLiteDatabase.delete(DBInfo.Table.Gift, whereClause, whereArgs);
        close();
    }

    public int getGiftCount() {
        open();
        String sql = " select * from " + DBInfo.Table.Gift;
        Cursor cursor = sqLiteDatabase.rawQuery(sql, null);
        cursor.moveToFirst();
        int count = 0;
        while (cursor.moveToNext()) {
            count++;
        }
        cursor.close();
        close();
        return count;
    }


    public void insertBadgeList(List<Badge> badgeList) {
        open();
        for (int i = 0; i < badgeList.size(); i++) {
            Badge badge = badgeList.get(i);
            ContentValues values = new ContentValues();
            values.put("bid", badge.getBid());
            values.put("descript", badge.getDescript());
            values.put("logoUrl", badge.getLogoUrl());
            values.put("name", badge.getName());

            sqLiteDatabase.replaceOrThrow(DBInfo.Table.Badge, null, values);
        }
        close();
    }

    public void deleteAlLBadge() {
        open();
        sqLiteDatabase.delete(DBInfo.Table.Badge, null, null);
        close();
    }


    public int getBadgeCount() {
        open();
        String sql = " select * from " + DBInfo.Table.Badge;
        Cursor cursor = sqLiteDatabase.rawQuery(sql, null);
        cursor.moveToFirst();
        int count = 0;
        while (cursor.moveToNext()) {
            count++;
        }
        cursor.close();
        close();
        return count;
    }

    /**
     * 删除入场数据
     */
    public void deleteAllAdmission() {
        open();
        sqLiteDatabase.delete(DBInfo.Table.ADMISSION_ANIMATION, null, null);
        close();
    }

    /**
     * 判断该当前这张表是否存在
     *
     * @param tableName 表的名字
     * @return 返回是否存在
     */
    public boolean isTableExist(String tableName) {
        open();
        String sql = " select * from " + tableName;
        int count;
        try (Cursor cursor = sqLiteDatabase.rawQuery(sql, null)) {
            cursor.moveToFirst();
            count = 0;
            while (cursor.moveToNext()) {
                count++;
            }
        }
        close();
        return count > 0;
    }

    /**
     * 插入入场动画
     *
     * @param admissionList 动画列表
     */
    public void insertAdmissionList(List<AdmissionEntity> admissionList) {
        open();
        for (int i = 0; i < admissionList.size(); i++) {
            AdmissionEntity admission = admissionList.get(i);
            LogUtils.e("Admission ====" + admission);
            ContentValues values = new ContentValues();
            values.put("cover", admission.getCover());
            values.put("duration", admission.getDuration());
            values.put("gid", admission.getGid());
            values.put("gname", admission.getGname());
            values.put("level", admission.getLevel());
            values.put("playType", admission.getPlayType());
            values.put("resourceUrl", admission.getResourceUrl());
            values.put("type", admission.getType());
            sqLiteDatabase.replaceOrThrow(DBInfo.Table.ADMISSION_ANIMATION, null, values);
        }
        close();
    }

    /**
     * 通过 Gid 查询 Admission
     *
     * @param level 等级
     * @return Admission
     */
    public AdmissionEntity getAdmissionByLevel(int level) {
        open();
        String sql = " select * from " + DBInfo.Table.ADMISSION_ANIMATION + " where level = " + level;
        Cursor cursor = sqLiteDatabase.rawQuery(sql, null);
        AdmissionEntity admission = new AdmissionEntity();
        while (cursor.moveToNext()) {
            admission.setGid(cursor.getInt(cursor.getColumnIndexOrThrow("gid")));
            admission.setCover(cursor.getString(cursor.getColumnIndexOrThrow("cover")));
            admission.setDuration(cursor.getInt(cursor.getColumnIndexOrThrow("duration")));
            admission.setGname(cursor.getString(cursor.getColumnIndexOrThrow("gname")));
            admission.setPlayType(cursor.getInt(cursor.getColumnIndexOrThrow("playType")));
            admission.setResourceUrl(cursor.getString(cursor.getColumnIndexOrThrow("resourceUrl")));
            admission.setType(cursor.getInt(cursor.getColumnIndexOrThrow("type")));
            admission.setLevel(cursor.getInt(cursor.getColumnIndexOrThrow("level")));
            break;
        }
        cursor.close();
        close();
        return admission;
    }


    /**
     * otherUser为另外个人的信息 有可能是发送人 有可能是接收人
     */
    public void insertLetterList(User otherUser, long loginUid, long letterId, String contnet, long timestamp, boolean isRead) {
        open();
        int count = 0;
        if (!isRead) {
            String sql = " select unreadcount from " + DBInfo.Table.CHAT_LIST + " where otherUid = " + otherUser.getUid() + " and loginUid = " + loginUid;
            Cursor cursor = sqLiteDatabase.rawQuery(sql, null);
            cursor.moveToFirst();
            if (!cursor.isAfterLast()) {
                count = cursor.getInt(0);
            }
            cursor.close();


            count += 1;
        }

        ContentValues values = new ContentValues();
        values.put("otherUid", otherUser.getUid());
        values.put("loginUid", loginUid);
        values.put("content", contnet);
        values.put("timestamp", timestamp);
        values.put("nickname", TextUtils.isEmpty(otherUser.getNickname()) ? "" : otherUser.getNickname());
        values.put("avatar", otherUser.getAvatar());
        values.put("sex", otherUser.getSex());
        values.put("letterId", letterId);
        values.put("userLevel", otherUser.getUserLevel());
        values.put("unReadCount", count);
        sqLiteDatabase.replaceOrThrow(DBInfo.Table.CHAT_LIST, null, values);

        close();
    }


    public List<LetterList> getLetterList(long loginUid) {//获取所有最后消息
        open();
        String sql = " select * from " + DBInfo.Table.CHAT_LIST + " where loginUid = " + loginUid + " order by timestamp desc";
        Cursor cursor = sqLiteDatabase.rawQuery(sql, null);
        List<LetterList> list = new ArrayList<>();
        while (cursor.moveToNext()) {
            LetterList letterList = new LetterList();
            letterList.setOtherUid(cursor.getLong(cursor.getColumnIndexOrThrow("otherUid")));
            letterList.setLoginUid(cursor.getLong(cursor.getColumnIndexOrThrow("loginUid")));
            letterList.setContent(cursor.getString(cursor.getColumnIndexOrThrow("content")));
            letterList.setTimestamp(cursor.getLong(cursor.getColumnIndexOrThrow("timestamp")));
            letterList.setNickname(cursor.getString(cursor.getColumnIndexOrThrow("nickname")));
            letterList.setAvatar(cursor.getString(cursor.getColumnIndexOrThrow("avatar")));
            letterList.setSex(cursor.getInt(cursor.getColumnIndexOrThrow("sex")));
            letterList.setLetterId(cursor.getLong(cursor.getColumnIndexOrThrow("letterId")));
            letterList.setUserLevel(cursor.getInt(cursor.getColumnIndexOrThrow("userLevel")));
            letterList.setUnReadCount(cursor.getInt(cursor.getColumnIndexOrThrow("unReadCount")));

            list.add(letterList);
        }
        cursor.close();
        close();
        return list;
    }

    /**
     * 已读某人的消息
     */
    public void userIsRead(long otherUid, long loginUid) {
        open();
        ContentValues values = new ContentValues();
        values.put("unReadCount", 0);
        String[] args = {String.valueOf(otherUid), String.valueOf(loginUid)};
        sqLiteDatabase.update(DBInfo.Table.CHAT_LIST, values, "otherUid=? and loginUid=?", args);
        close();
    }

    /**
     * 获取未读个数
     */
    public int getUnReadCount(long otherUid, long loginUid) {
        open();
        int count = 0;
        String sql = " select unreadcount from " + DBInfo.Table.CHAT_LIST + " where otherUid = " + otherUid + " and loginUid = " + loginUid;
        Cursor cursor = sqLiteDatabase.rawQuery(sql, null);
        cursor.moveToFirst();
        if (!cursor.isAfterLast()) {
            count = cursor.getInt(0);
        }
        cursor.close();
        close();
        return count;

    }


    public void insertLetter(Letter letter) {
        open();
        ContentValues values = new ContentValues();
        values.put("sendUid", letter.getSendUid());
        values.put("otherUid", letter.getOtherUid());
        values.put("content", letter.getContent());
        values.put("timestamp", System.currentTimeMillis());
        values.put("nickname", letter.getNickname());
        values.put("avatar", letter.getAvatar());
        values.put("letterId", letter.getLetterId());
        values.put("sex", letter.getSex());
        values.put("userLevel", letter.getUserLevel());
        values.put("msgtype", letter.getType());

        sqLiteDatabase.replaceOrThrow(DBInfo.Table.CHAT_CONTENT, null, values);
        close();
    }


    public Letter getLastLetter(long loginUid) {
        open();
        String sql = " select * from " + DBInfo.Table.CHAT_CONTENT + " where (sendUid = '" + loginUid + "') or (otherUid = '" + loginUid + "') order by timestamp desc limit 0,1";
        Cursor cursor = sqLiteDatabase.rawQuery(sql, null);
        Letter content = null;
//        for (cursor.moveToFirst(); !(cursor.isAfterLast()); cursor.moveToNext())
        if (cursor.moveToLast()) {
            content = new Letter();
            content.setContent(cursor.getString(cursor.getColumnIndexOrThrow("content")));
            content.setSendUid(cursor.getLong(cursor.getColumnIndexOrThrow("sendUid")));
            content.setNickname(cursor.getString(cursor.getColumnIndexOrThrow("nickname")));
            content.setAvatar(cursor.getString(cursor.getColumnIndexOrThrow("avatar")));
            content.setTimestamp(cursor.getLong(cursor.getColumnIndexOrThrow("timestamp")));
            content.setOtherUid(cursor.getLong(cursor.getColumnIndexOrThrow("otherUid")));
            content.setLetterId(cursor.getInt(cursor.getColumnIndexOrThrow("letterId")));
        }
        cursor.close();
        close();
        return content;
    }

    public List<Letter> getLetterListByUid(long otherUid, long loginUid) {
        List<Letter> chatList = new ArrayList<>();
        open();
//        String sql = " select * from " + DBInfo.Table.CHAT_CONTENT + " where (otherUid = '" + otherUid + "') order by timestamp asc";
        String sql = " select * from " + DBInfo.Table.CHAT_CONTENT + " where (sendUid = '" + loginUid + "' and otherUid = '" + otherUid + "') or (otherUid = '" + loginUid + "' and sendUid = '" + otherUid + "') order by timestamp asc";
        Cursor cursor = sqLiteDatabase.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            Letter content = new Letter();
            content.setContent(cursor.getString(cursor.getColumnIndexOrThrow("content")));
            content.setSendUid(cursor.getLong(cursor.getColumnIndexOrThrow("sendUid")));
            content.setNickname(cursor.getString(cursor.getColumnIndexOrThrow("nickname")));
            content.setAvatar(cursor.getString(cursor.getColumnIndexOrThrow("avatar")));
            content.setTimestamp(cursor.getLong(cursor.getColumnIndexOrThrow("timestamp")));
            content.setOtherUid(cursor.getLong(cursor.getColumnIndexOrThrow("otherUid")));
            content.setType(cursor.getInt(cursor.getColumnIndexOrThrow("msgtype")));
            content.setLetterId(cursor.getInt(cursor.getColumnIndexOrThrow("letterId")));
            chatList.add(content);
        }
        cursor.close();
        close();
        return chatList;
    }


    //    /**
//     * 私聊侧滑删除
//     * @param otherIdx 对方的Idx
//     */
    public void deleteChatMessage(long otherUid, long sendUid) {
        open();
        String whereClause1 = "otherUid=?";
        String[] whereArgs1 = new String[]{String.valueOf(otherUid)};
        sqLiteDatabase.delete(DBInfo.Table.CHAT_LIST, whereClause1, whereArgs1);
//        String sql = " select * from " + DBInfo.Table.CHAT_CONTENT + " where (sendUid = '" + loginUid + "' and otherUid = '" + otherUid + "') or (otherUid = '" + loginUid + "' and sendUid = '" + otherUid + "') order by timestamp asc";

        String whereClause2 = "(sendUid=? and otherUid=?) or (otherUid=? and sendUid=?)";
        String[] whereArgs2 = new String[]{String.valueOf(otherUid), String.valueOf(sendUid), String.valueOf(otherUid), String.valueOf(sendUid)};
        sqLiteDatabase.delete(DBInfo.Table.CHAT_CONTENT, whereClause2, whereArgs2);
        close();
    }

    /**
     * 单一私聊删除
     */
    public void deleteSingleChatMessage(long otherUid, long sendUid, long letterId, boolean isLast, boolean isMyself) {
//        String[] args = {String.valueOf(otherUid), String.valueOf(loginUid)};
//        sqLiteDatabase.update(DBInfo.Table.CHAT_LIST, values, "otherUid=? and loginUid=?", args);
        open();
        String whereClause1 = "letterId=? and otherUid=? and sendUid=?";
        String[] whereArgs1 = new String[]{String.valueOf(letterId), String.valueOf(otherUid), String.valueOf(sendUid)};
        sqLiteDatabase.delete(DBInfo.Table.CHAT_CONTENT, whereClause1, whereArgs1);

        if (isLast) {
            String whereClause2 = "otherUid=?";
            String[] whereArgs2;
            if (isMyself) {
                whereArgs2 = new String[]{String.valueOf(otherUid)};
            } else {
                whereArgs2 = new String[]{String.valueOf(sendUid)};
            }
            ContentValues values = new ContentValues();
            values.put("content", "Bạn đã thu hồi một tin nhắn");
            sqLiteDatabase.update(DBInfo.Table.CHAT_LIST, values, whereClause2, whereArgs2);
        }
//        else {
//            sqLiteDatabase.delete(DBInfo.Table.CHAT_LIST, whereClause1, whereArgs1);
//        }
        close();
    }


    /**
     * 忽略全部聊天消息
     */
    public void deleteChatMessageAll() {
        open();
        sqLiteDatabase.delete(DBInfo.Table.CHAT_LIST, null, null);

        sqLiteDatabase.delete(DBInfo.Table.CHAT_CONTENT, null, null);
        close();
    }

}
