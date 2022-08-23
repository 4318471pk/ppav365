package com.live.fox.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.live.fox.utils.LogUtils;


/**
 * User: hqs
 * Date: 2016/4/5
 * Time: 13:57
 */
public class DBHelper extends SQLiteOpenHelper {

    public DBHelper(Context context) {
        super(context, DBInfo.DB_NAME, null, DBInfo.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DBInfo.Table.GIFT_CREATE);
        db.execSQL(DBInfo.Table.BADGE_CREATE);
        db.execSQL(DBInfo.Table.CHAT_LIST_CREATE);
        db.execSQL(DBInfo.Table.CHAT_CONTENT_CREATE);
        db.execSQL(DBInfo.Table.ADMISSION_ANIMATION_CREATE);
        db.execSQL(DBInfo.Table.USER_INFO);
        LogUtils.e("dataBaseHelp: onCreate");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        LogUtils.e("dataBaseHelp: onUpgrade" + "oldVersion ->" + oldVersion + "newVersion:" + newVersion);
        if (oldVersion == 1 && newVersion == 2) {
            db.execSQL(DBInfo.Table.ADMISSION_ANIMATION_CREATE);
            db.execSQL(DBInfo.Table.USER_INFO);
        }
    }
}
