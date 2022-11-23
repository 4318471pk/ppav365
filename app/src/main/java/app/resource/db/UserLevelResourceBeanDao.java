package app.resource.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import com.live.fox.entity.UserLevelResourceBean;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "USER_LEVEL_RESOURCE_BEAN".
*/
public class UserLevelResourceBeanDao extends AbstractDao<UserLevelResourceBean, Long> {

    public static final String TABLENAME = "USER_LEVEL_RESOURCE_BEAN";

    /**
     * Properties of entity UserLevelResourceBean.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property LevelName = new Property(1, String.class, "levelName", false, "LEVEL_NAME");
        public final static Property Level = new Property(2, int.class, "level", false, "LEVEL");
        public final static Property LevelImg = new Property(3, String.class, "levelImg", false, "LEVEL_IMG");
        public final static Property Rang = new Property(4, int.class, "rang", false, "RANG");
        public final static Property RangMark = new Property(5, String.class, "rangMark", false, "RANG_MARK");
        public final static Property Type = new Property(6, int.class, "type", false, "TYPE");
        public final static Property Remark = new Property(7, String.class, "remark", false, "REMARK");
        public final static Property CreateTime = new Property(8, Long.class, "createTime", false, "CREATE_TIME");
        public final static Property UpdateTime = new Property(9, Long.class, "updateTime", false, "UPDATE_TIME");
        public final static Property LocalShouldUpdate = new Property(10, int.class, "localShouldUpdate", false, "LOCAL_SHOULD_UPDATE");
        public final static Property LocalImgPath = new Property(11, String.class, "localImgPath", false, "LOCAL_IMG_PATH");
    }


    public UserLevelResourceBeanDao(DaoConfig config) {
        super(config);
    }
    
    public UserLevelResourceBeanDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"USER_LEVEL_RESOURCE_BEAN\" (" + //
                "\"_id\" INTEGER PRIMARY KEY ," + // 0: id
                "\"LEVEL_NAME\" TEXT," + // 1: levelName
                "\"LEVEL\" INTEGER NOT NULL ," + // 2: level
                "\"LEVEL_IMG\" TEXT," + // 3: levelImg
                "\"RANG\" INTEGER NOT NULL ," + // 4: rang
                "\"RANG_MARK\" TEXT," + // 5: rangMark
                "\"TYPE\" INTEGER NOT NULL ," + // 6: type
                "\"REMARK\" TEXT," + // 7: remark
                "\"CREATE_TIME\" INTEGER," + // 8: createTime
                "\"UPDATE_TIME\" INTEGER," + // 9: updateTime
                "\"LOCAL_SHOULD_UPDATE\" INTEGER NOT NULL ," + // 10: localShouldUpdate
                "\"LOCAL_IMG_PATH\" TEXT);"); // 11: localImgPath
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"USER_LEVEL_RESOURCE_BEAN\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, UserLevelResourceBean entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String levelName = entity.getLevelName();
        if (levelName != null) {
            stmt.bindString(2, levelName);
        }
        stmt.bindLong(3, entity.getLevel());
 
        String levelImg = entity.getLevelImg();
        if (levelImg != null) {
            stmt.bindString(4, levelImg);
        }
        stmt.bindLong(5, entity.getRang());
 
        String rangMark = entity.getRangMark();
        if (rangMark != null) {
            stmt.bindString(6, rangMark);
        }
        stmt.bindLong(7, entity.getType());
 
        String remark = entity.getRemark();
        if (remark != null) {
            stmt.bindString(8, remark);
        }
 
        Long createTime = entity.getCreateTime();
        if (createTime != null) {
            stmt.bindLong(9, createTime);
        }
 
        Long updateTime = entity.getUpdateTime();
        if (updateTime != null) {
            stmt.bindLong(10, updateTime);
        }
        stmt.bindLong(11, entity.getLocalShouldUpdate());
 
        String localImgPath = entity.getLocalImgPath();
        if (localImgPath != null) {
            stmt.bindString(12, localImgPath);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, UserLevelResourceBean entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String levelName = entity.getLevelName();
        if (levelName != null) {
            stmt.bindString(2, levelName);
        }
        stmt.bindLong(3, entity.getLevel());
 
        String levelImg = entity.getLevelImg();
        if (levelImg != null) {
            stmt.bindString(4, levelImg);
        }
        stmt.bindLong(5, entity.getRang());
 
        String rangMark = entity.getRangMark();
        if (rangMark != null) {
            stmt.bindString(6, rangMark);
        }
        stmt.bindLong(7, entity.getType());
 
        String remark = entity.getRemark();
        if (remark != null) {
            stmt.bindString(8, remark);
        }
 
        Long createTime = entity.getCreateTime();
        if (createTime != null) {
            stmt.bindLong(9, createTime);
        }
 
        Long updateTime = entity.getUpdateTime();
        if (updateTime != null) {
            stmt.bindLong(10, updateTime);
        }
        stmt.bindLong(11, entity.getLocalShouldUpdate());
 
        String localImgPath = entity.getLocalImgPath();
        if (localImgPath != null) {
            stmt.bindString(12, localImgPath);
        }
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public UserLevelResourceBean readEntity(Cursor cursor, int offset) {
        UserLevelResourceBean entity = new UserLevelResourceBean( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // levelName
            cursor.getInt(offset + 2), // level
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // levelImg
            cursor.getInt(offset + 4), // rang
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // rangMark
            cursor.getInt(offset + 6), // type
            cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7), // remark
            cursor.isNull(offset + 8) ? null : cursor.getLong(offset + 8), // createTime
            cursor.isNull(offset + 9) ? null : cursor.getLong(offset + 9), // updateTime
            cursor.getInt(offset + 10), // localShouldUpdate
            cursor.isNull(offset + 11) ? null : cursor.getString(offset + 11) // localImgPath
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, UserLevelResourceBean entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setLevelName(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setLevel(cursor.getInt(offset + 2));
        entity.setLevelImg(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setRang(cursor.getInt(offset + 4));
        entity.setRangMark(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setType(cursor.getInt(offset + 6));
        entity.setRemark(cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7));
        entity.setCreateTime(cursor.isNull(offset + 8) ? null : cursor.getLong(offset + 8));
        entity.setUpdateTime(cursor.isNull(offset + 9) ? null : cursor.getLong(offset + 9));
        entity.setLocalShouldUpdate(cursor.getInt(offset + 10));
        entity.setLocalImgPath(cursor.isNull(offset + 11) ? null : cursor.getString(offset + 11));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(UserLevelResourceBean entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(UserLevelResourceBean entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(UserLevelResourceBean entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
