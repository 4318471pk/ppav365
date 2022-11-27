package app.resource.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import com.live.fox.entity.RoomWatchedHistoryBean;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "ROOM_WATCHED_HISTORY_BEAN".
*/
public class RoomWatchedHistoryBeanDao extends AbstractDao<RoomWatchedHistoryBean, Long> {

    public static final String TABLENAME = "ROOM_WATCHED_HISTORY_BEAN";

    /**
     * Properties of entity RoomWatchedHistoryBean.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property LiveId = new Property(1, String.class, "liveId", false, "LIVE_ID");
        public final static Property Title = new Property(2, String.class, "title", false, "TITLE");
        public final static Property Aid = new Property(3, String.class, "aid", false, "AID");
        public final static Property RoomIcon = new Property(4, String.class, "roomIcon", false, "ROOM_ICON");
        public final static Property LiveChannel = new Property(5, int.class, "liveChannel", false, "LIVE_CHANNEL");
        public final static Property RoomCategory = new Property(6, int.class, "roomCategory", false, "ROOM_CATEGORY");
        public final static Property CategoryId = new Property(7, int.class, "categoryId", false, "CATEGORY_ID");
        public final static Property CategoryType = new Property(8, int.class, "categoryType", false, "CATEGORY_TYPE");
        public final static Property RoomType = new Property(9, int.class, "roomType", false, "ROOM_TYPE");
        public final static Property Status = new Property(10, int.class, "status", false, "STATUS");
        public final static Property LiveSum = new Property(11, int.class, "liveSum", false, "LIVE_SUM");
        public final static Property Option = new Property(12, int.class, "option", false, "OPTION");
        public final static Property VideoUrl = new Property(13, String.class, "videoUrl", false, "VIDEO_URL");
        public final static Property Hot = new Property(14, int.class, "hot", false, "HOT");
        public final static Property Recommend = new Property(15, int.class, "recommend", false, "RECOMMEND");
        public final static Property IsPayOver = new Property(16, String.class, "isPayOver", false, "IS_PAY_OVER");
        public final static Property RoomPrice = new Property(17, String.class, "roomPrice", false, "ROOM_PRICE");
    }


    public RoomWatchedHistoryBeanDao(DaoConfig config) {
        super(config);
    }
    
    public RoomWatchedHistoryBeanDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"ROOM_WATCHED_HISTORY_BEAN\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "\"LIVE_ID\" TEXT," + // 1: liveId
                "\"TITLE\" TEXT," + // 2: title
                "\"AID\" TEXT," + // 3: aid
                "\"ROOM_ICON\" TEXT," + // 4: roomIcon
                "\"LIVE_CHANNEL\" INTEGER NOT NULL ," + // 5: liveChannel
                "\"ROOM_CATEGORY\" INTEGER NOT NULL ," + // 6: roomCategory
                "\"CATEGORY_ID\" INTEGER NOT NULL ," + // 7: categoryId
                "\"CATEGORY_TYPE\" INTEGER NOT NULL ," + // 8: categoryType
                "\"ROOM_TYPE\" INTEGER NOT NULL ," + // 9: roomType
                "\"STATUS\" INTEGER NOT NULL ," + // 10: status
                "\"LIVE_SUM\" INTEGER NOT NULL ," + // 11: liveSum
                "\"OPTION\" INTEGER NOT NULL ," + // 12: option
                "\"VIDEO_URL\" TEXT," + // 13: videoUrl
                "\"HOT\" INTEGER NOT NULL ," + // 14: hot
                "\"RECOMMEND\" INTEGER NOT NULL ," + // 15: recommend
                "\"IS_PAY_OVER\" TEXT," + // 16: isPayOver
                "\"ROOM_PRICE\" TEXT);"); // 17: roomPrice
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"ROOM_WATCHED_HISTORY_BEAN\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, RoomWatchedHistoryBean entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String liveId = entity.getLiveId();
        if (liveId != null) {
            stmt.bindString(2, liveId);
        }
 
        String title = entity.getTitle();
        if (title != null) {
            stmt.bindString(3, title);
        }
 
        String aid = entity.getAid();
        if (aid != null) {
            stmt.bindString(4, aid);
        }
 
        String roomIcon = entity.getRoomIcon();
        if (roomIcon != null) {
            stmt.bindString(5, roomIcon);
        }
        stmt.bindLong(6, entity.getLiveChannel());
        stmt.bindLong(7, entity.getRoomCategory());
        stmt.bindLong(8, entity.getCategoryId());
        stmt.bindLong(9, entity.getCategoryType());
        stmt.bindLong(10, entity.getRoomType());
        stmt.bindLong(11, entity.getStatus());
        stmt.bindLong(12, entity.getLiveSum());
        stmt.bindLong(13, entity.getOption());
 
        String videoUrl = entity.getVideoUrl();
        if (videoUrl != null) {
            stmt.bindString(14, videoUrl);
        }
        stmt.bindLong(15, entity.getHot());
        stmt.bindLong(16, entity.getRecommend());
 
        String isPayOver = entity.getIsPayOver();
        if (isPayOver != null) {
            stmt.bindString(17, isPayOver);
        }
 
        String roomPrice = entity.getRoomPrice();
        if (roomPrice != null) {
            stmt.bindString(18, roomPrice);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, RoomWatchedHistoryBean entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String liveId = entity.getLiveId();
        if (liveId != null) {
            stmt.bindString(2, liveId);
        }
 
        String title = entity.getTitle();
        if (title != null) {
            stmt.bindString(3, title);
        }
 
        String aid = entity.getAid();
        if (aid != null) {
            stmt.bindString(4, aid);
        }
 
        String roomIcon = entity.getRoomIcon();
        if (roomIcon != null) {
            stmt.bindString(5, roomIcon);
        }
        stmt.bindLong(6, entity.getLiveChannel());
        stmt.bindLong(7, entity.getRoomCategory());
        stmt.bindLong(8, entity.getCategoryId());
        stmt.bindLong(9, entity.getCategoryType());
        stmt.bindLong(10, entity.getRoomType());
        stmt.bindLong(11, entity.getStatus());
        stmt.bindLong(12, entity.getLiveSum());
        stmt.bindLong(13, entity.getOption());
 
        String videoUrl = entity.getVideoUrl();
        if (videoUrl != null) {
            stmt.bindString(14, videoUrl);
        }
        stmt.bindLong(15, entity.getHot());
        stmt.bindLong(16, entity.getRecommend());
 
        String isPayOver = entity.getIsPayOver();
        if (isPayOver != null) {
            stmt.bindString(17, isPayOver);
        }
 
        String roomPrice = entity.getRoomPrice();
        if (roomPrice != null) {
            stmt.bindString(18, roomPrice);
        }
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public RoomWatchedHistoryBean readEntity(Cursor cursor, int offset) {
        RoomWatchedHistoryBean entity = new RoomWatchedHistoryBean( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // liveId
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // title
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // aid
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // roomIcon
            cursor.getInt(offset + 5), // liveChannel
            cursor.getInt(offset + 6), // roomCategory
            cursor.getInt(offset + 7), // categoryId
            cursor.getInt(offset + 8), // categoryType
            cursor.getInt(offset + 9), // roomType
            cursor.getInt(offset + 10), // status
            cursor.getInt(offset + 11), // liveSum
            cursor.getInt(offset + 12), // option
            cursor.isNull(offset + 13) ? null : cursor.getString(offset + 13), // videoUrl
            cursor.getInt(offset + 14), // hot
            cursor.getInt(offset + 15), // recommend
            cursor.isNull(offset + 16) ? null : cursor.getString(offset + 16), // isPayOver
            cursor.isNull(offset + 17) ? null : cursor.getString(offset + 17) // roomPrice
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, RoomWatchedHistoryBean entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setLiveId(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setTitle(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setAid(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setRoomIcon(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setLiveChannel(cursor.getInt(offset + 5));
        entity.setRoomCategory(cursor.getInt(offset + 6));
        entity.setCategoryId(cursor.getInt(offset + 7));
        entity.setCategoryType(cursor.getInt(offset + 8));
        entity.setRoomType(cursor.getInt(offset + 9));
        entity.setStatus(cursor.getInt(offset + 10));
        entity.setLiveSum(cursor.getInt(offset + 11));
        entity.setOption(cursor.getInt(offset + 12));
        entity.setVideoUrl(cursor.isNull(offset + 13) ? null : cursor.getString(offset + 13));
        entity.setHot(cursor.getInt(offset + 14));
        entity.setRecommend(cursor.getInt(offset + 15));
        entity.setIsPayOver(cursor.isNull(offset + 16) ? null : cursor.getString(offset + 16));
        entity.setRoomPrice(cursor.isNull(offset + 17) ? null : cursor.getString(offset + 17));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(RoomWatchedHistoryBean entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(RoomWatchedHistoryBean entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(RoomWatchedHistoryBean entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
