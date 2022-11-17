package app.resource.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import com.live.fox.entity.GiftResourceBean;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "GIFT_RESOURCE_BEAN".
*/
public class GiftResourceBeanDao extends AbstractDao<GiftResourceBean, Long> {

    public static final String TABLENAME = "GIFT_RESOURCE_BEAN";

    /**
     * Properties of entity GiftResourceBean.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property Name = new Property(1, String.class, "name", false, "NAME");
        public final static Property Ename = new Property(2, String.class, "ename", false, "ENAME");
        public final static Property Gshort = new Property(3, String.class, "gshort", false, "GSHORT");
        public final static Property Type = new Property(4, int.class, "type", false, "TYPE");
        public final static Property Mark = new Property(5, int.class, "mark", false, "MARK");
        public final static Property Needdiamond = new Property(6, int.class, "needdiamond", false, "NEEDDIAMOND");
        public final static Property Gitficon = new Property(7, String.class, "gitficon", false, "GITFICON");
        public final static Property Swf = new Property(8, String.class, "swf", false, "SWF");
        public final static Property Swftime = new Property(9, int.class, "swftime", false, "SWFTIME");
        public final static Property CompressUrl = new Property(10, String.class, "compressUrl", false, "COMPRESS_URL");
        public final static Property Compress = new Property(11, int.class, "compress", false, "COMPRESS");
        public final static Property Status = new Property(12, int.class, "status", false, "STATUS");
        public final static Property Sort = new Property(13, int.class, "sort", false, "SORT");
        public final static Property ShowFront = new Property(14, int.class, "showFront", false, "SHOW_FRONT");
        public final static Property Remark = new Property(15, String.class, "remark", false, "REMARK");
        public final static Property CreateTime = new Property(16, Long.class, "createTime", false, "CREATE_TIME");
        public final static Property UpdateTime = new Property(17, Long.class, "updateTime", false, "UPDATE_TIME");
        public final static Property LocalShouldUpdate = new Property(18, int.class, "localShouldUpdate", false, "LOCAL_SHOULD_UPDATE");
        public final static Property LocalImgPath = new Property(19, String.class, "localImgPath", false, "LOCAL_IMG_PATH");
        public final static Property LocalSvgPath = new Property(20, String.class, "localSvgPath", false, "LOCAL_SVG_PATH");
    }


    public GiftResourceBeanDao(DaoConfig config) {
        super(config);
    }
    
    public GiftResourceBeanDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"GIFT_RESOURCE_BEAN\" (" + //
                "\"_id\" INTEGER PRIMARY KEY ," + // 0: id
                "\"NAME\" TEXT," + // 1: name
                "\"ENAME\" TEXT," + // 2: ename
                "\"GSHORT\" TEXT," + // 3: gshort
                "\"TYPE\" INTEGER NOT NULL ," + // 4: type
                "\"MARK\" INTEGER NOT NULL ," + // 5: mark
                "\"NEEDDIAMOND\" INTEGER NOT NULL ," + // 6: needdiamond
                "\"GITFICON\" TEXT," + // 7: gitficon
                "\"SWF\" TEXT," + // 8: swf
                "\"SWFTIME\" INTEGER NOT NULL ," + // 9: swftime
                "\"COMPRESS_URL\" TEXT," + // 10: compressUrl
                "\"COMPRESS\" INTEGER NOT NULL ," + // 11: compress
                "\"STATUS\" INTEGER NOT NULL ," + // 12: status
                "\"SORT\" INTEGER NOT NULL ," + // 13: sort
                "\"SHOW_FRONT\" INTEGER NOT NULL ," + // 14: showFront
                "\"REMARK\" TEXT," + // 15: remark
                "\"CREATE_TIME\" INTEGER," + // 16: createTime
                "\"UPDATE_TIME\" INTEGER," + // 17: updateTime
                "\"LOCAL_SHOULD_UPDATE\" INTEGER NOT NULL ," + // 18: localShouldUpdate
                "\"LOCAL_IMG_PATH\" TEXT," + // 19: localImgPath
                "\"LOCAL_SVG_PATH\" TEXT);"); // 20: localSvgPath
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"GIFT_RESOURCE_BEAN\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, GiftResourceBean entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String name = entity.getName();
        if (name != null) {
            stmt.bindString(2, name);
        }
 
        String ename = entity.getEname();
        if (ename != null) {
            stmt.bindString(3, ename);
        }
 
        String gshort = entity.getGshort();
        if (gshort != null) {
            stmt.bindString(4, gshort);
        }
        stmt.bindLong(5, entity.getType());
        stmt.bindLong(6, entity.getMark());
        stmt.bindLong(7, entity.getNeeddiamond());
 
        String gitficon = entity.getGitficon();
        if (gitficon != null) {
            stmt.bindString(8, gitficon);
        }
 
        String swf = entity.getSwf();
        if (swf != null) {
            stmt.bindString(9, swf);
        }
        stmt.bindLong(10, entity.getSwftime());
 
        String compressUrl = entity.getCompressUrl();
        if (compressUrl != null) {
            stmt.bindString(11, compressUrl);
        }
        stmt.bindLong(12, entity.getCompress());
        stmt.bindLong(13, entity.getStatus());
        stmt.bindLong(14, entity.getSort());
        stmt.bindLong(15, entity.getShowFront());
 
        String remark = entity.getRemark();
        if (remark != null) {
            stmt.bindString(16, remark);
        }
 
        Long createTime = entity.getCreateTime();
        if (createTime != null) {
            stmt.bindLong(17, createTime);
        }
 
        Long updateTime = entity.getUpdateTime();
        if (updateTime != null) {
            stmt.bindLong(18, updateTime);
        }
        stmt.bindLong(19, entity.getLocalShouldUpdate());
 
        String localImgPath = entity.getLocalImgPath();
        if (localImgPath != null) {
            stmt.bindString(20, localImgPath);
        }
 
        String localSvgPath = entity.getLocalSvgPath();
        if (localSvgPath != null) {
            stmt.bindString(21, localSvgPath);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, GiftResourceBean entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String name = entity.getName();
        if (name != null) {
            stmt.bindString(2, name);
        }
 
        String ename = entity.getEname();
        if (ename != null) {
            stmt.bindString(3, ename);
        }
 
        String gshort = entity.getGshort();
        if (gshort != null) {
            stmt.bindString(4, gshort);
        }
        stmt.bindLong(5, entity.getType());
        stmt.bindLong(6, entity.getMark());
        stmt.bindLong(7, entity.getNeeddiamond());
 
        String gitficon = entity.getGitficon();
        if (gitficon != null) {
            stmt.bindString(8, gitficon);
        }
 
        String swf = entity.getSwf();
        if (swf != null) {
            stmt.bindString(9, swf);
        }
        stmt.bindLong(10, entity.getSwftime());
 
        String compressUrl = entity.getCompressUrl();
        if (compressUrl != null) {
            stmt.bindString(11, compressUrl);
        }
        stmt.bindLong(12, entity.getCompress());
        stmt.bindLong(13, entity.getStatus());
        stmt.bindLong(14, entity.getSort());
        stmt.bindLong(15, entity.getShowFront());
 
        String remark = entity.getRemark();
        if (remark != null) {
            stmt.bindString(16, remark);
        }
 
        Long createTime = entity.getCreateTime();
        if (createTime != null) {
            stmt.bindLong(17, createTime);
        }
 
        Long updateTime = entity.getUpdateTime();
        if (updateTime != null) {
            stmt.bindLong(18, updateTime);
        }
        stmt.bindLong(19, entity.getLocalShouldUpdate());
 
        String localImgPath = entity.getLocalImgPath();
        if (localImgPath != null) {
            stmt.bindString(20, localImgPath);
        }
 
        String localSvgPath = entity.getLocalSvgPath();
        if (localSvgPath != null) {
            stmt.bindString(21, localSvgPath);
        }
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public GiftResourceBean readEntity(Cursor cursor, int offset) {
        GiftResourceBean entity = new GiftResourceBean( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // name
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // ename
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // gshort
            cursor.getInt(offset + 4), // type
            cursor.getInt(offset + 5), // mark
            cursor.getInt(offset + 6), // needdiamond
            cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7), // gitficon
            cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8), // swf
            cursor.getInt(offset + 9), // swftime
            cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10), // compressUrl
            cursor.getInt(offset + 11), // compress
            cursor.getInt(offset + 12), // status
            cursor.getInt(offset + 13), // sort
            cursor.getInt(offset + 14), // showFront
            cursor.isNull(offset + 15) ? null : cursor.getString(offset + 15), // remark
            cursor.isNull(offset + 16) ? null : cursor.getLong(offset + 16), // createTime
            cursor.isNull(offset + 17) ? null : cursor.getLong(offset + 17), // updateTime
            cursor.getInt(offset + 18), // localShouldUpdate
            cursor.isNull(offset + 19) ? null : cursor.getString(offset + 19), // localImgPath
            cursor.isNull(offset + 20) ? null : cursor.getString(offset + 20) // localSvgPath
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, GiftResourceBean entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setName(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setEname(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setGshort(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setType(cursor.getInt(offset + 4));
        entity.setMark(cursor.getInt(offset + 5));
        entity.setNeeddiamond(cursor.getInt(offset + 6));
        entity.setGitficon(cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7));
        entity.setSwf(cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8));
        entity.setSwftime(cursor.getInt(offset + 9));
        entity.setCompressUrl(cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10));
        entity.setCompress(cursor.getInt(offset + 11));
        entity.setStatus(cursor.getInt(offset + 12));
        entity.setSort(cursor.getInt(offset + 13));
        entity.setShowFront(cursor.getInt(offset + 14));
        entity.setRemark(cursor.isNull(offset + 15) ? null : cursor.getString(offset + 15));
        entity.setCreateTime(cursor.isNull(offset + 16) ? null : cursor.getLong(offset + 16));
        entity.setUpdateTime(cursor.isNull(offset + 17) ? null : cursor.getLong(offset + 17));
        entity.setLocalShouldUpdate(cursor.getInt(offset + 18));
        entity.setLocalImgPath(cursor.isNull(offset + 19) ? null : cursor.getString(offset + 19));
        entity.setLocalSvgPath(cursor.isNull(offset + 20) ? null : cursor.getString(offset + 20));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(GiftResourceBean entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(GiftResourceBean entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(GiftResourceBean entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}