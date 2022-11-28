package app.resource.db;

import java.util.Map;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.AbstractDaoSession;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.identityscope.IdentityScopeType;
import org.greenrobot.greendao.internal.DaoConfig;

import com.live.fox.entity.GiftResourceBean;
import com.live.fox.entity.MountResourceBean;
import com.live.fox.entity.RoomWatchedHistoryBean;
import com.live.fox.entity.SendGiftResourceBean;
import com.live.fox.entity.UserGuardResourceBean;
import com.live.fox.entity.UserLevelResourceBean;
import com.live.fox.entity.UserTagResourceBean;
import com.live.fox.entity.UserVehiclePlayLimitBean;

import app.resource.db.GiftResourceBeanDao;
import app.resource.db.MountResourceBeanDao;
import app.resource.db.RoomWatchedHistoryBeanDao;
import app.resource.db.SendGiftResourceBeanDao;
import app.resource.db.UserGuardResourceBeanDao;
import app.resource.db.UserLevelResourceBeanDao;
import app.resource.db.UserTagResourceBeanDao;
import app.resource.db.UserVehiclePlayLimitBeanDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see org.greenrobot.greendao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig giftResourceBeanDaoConfig;
    private final DaoConfig mountResourceBeanDaoConfig;
    private final DaoConfig roomWatchedHistoryBeanDaoConfig;
    private final DaoConfig sendGiftResourceBeanDaoConfig;
    private final DaoConfig userGuardResourceBeanDaoConfig;
    private final DaoConfig userLevelResourceBeanDaoConfig;
    private final DaoConfig userTagResourceBeanDaoConfig;
    private final DaoConfig userVehiclePlayLimitBeanDaoConfig;

    private final GiftResourceBeanDao giftResourceBeanDao;
    private final MountResourceBeanDao mountResourceBeanDao;
    private final RoomWatchedHistoryBeanDao roomWatchedHistoryBeanDao;
    private final SendGiftResourceBeanDao sendGiftResourceBeanDao;
    private final UserGuardResourceBeanDao userGuardResourceBeanDao;
    private final UserLevelResourceBeanDao userLevelResourceBeanDao;
    private final UserTagResourceBeanDao userTagResourceBeanDao;
    private final UserVehiclePlayLimitBeanDao userVehiclePlayLimitBeanDao;

    public DaoSession(Database db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        giftResourceBeanDaoConfig = daoConfigMap.get(GiftResourceBeanDao.class).clone();
        giftResourceBeanDaoConfig.initIdentityScope(type);

        mountResourceBeanDaoConfig = daoConfigMap.get(MountResourceBeanDao.class).clone();
        mountResourceBeanDaoConfig.initIdentityScope(type);

        roomWatchedHistoryBeanDaoConfig = daoConfigMap.get(RoomWatchedHistoryBeanDao.class).clone();
        roomWatchedHistoryBeanDaoConfig.initIdentityScope(type);

        sendGiftResourceBeanDaoConfig = daoConfigMap.get(SendGiftResourceBeanDao.class).clone();
        sendGiftResourceBeanDaoConfig.initIdentityScope(type);

        userGuardResourceBeanDaoConfig = daoConfigMap.get(UserGuardResourceBeanDao.class).clone();
        userGuardResourceBeanDaoConfig.initIdentityScope(type);

        userLevelResourceBeanDaoConfig = daoConfigMap.get(UserLevelResourceBeanDao.class).clone();
        userLevelResourceBeanDaoConfig.initIdentityScope(type);

        userTagResourceBeanDaoConfig = daoConfigMap.get(UserTagResourceBeanDao.class).clone();
        userTagResourceBeanDaoConfig.initIdentityScope(type);

        userVehiclePlayLimitBeanDaoConfig = daoConfigMap.get(UserVehiclePlayLimitBeanDao.class).clone();
        userVehiclePlayLimitBeanDaoConfig.initIdentityScope(type);

        giftResourceBeanDao = new GiftResourceBeanDao(giftResourceBeanDaoConfig, this);
        mountResourceBeanDao = new MountResourceBeanDao(mountResourceBeanDaoConfig, this);
        roomWatchedHistoryBeanDao = new RoomWatchedHistoryBeanDao(roomWatchedHistoryBeanDaoConfig, this);
        sendGiftResourceBeanDao = new SendGiftResourceBeanDao(sendGiftResourceBeanDaoConfig, this);
        userGuardResourceBeanDao = new UserGuardResourceBeanDao(userGuardResourceBeanDaoConfig, this);
        userLevelResourceBeanDao = new UserLevelResourceBeanDao(userLevelResourceBeanDaoConfig, this);
        userTagResourceBeanDao = new UserTagResourceBeanDao(userTagResourceBeanDaoConfig, this);
        userVehiclePlayLimitBeanDao = new UserVehiclePlayLimitBeanDao(userVehiclePlayLimitBeanDaoConfig, this);

        registerDao(GiftResourceBean.class, giftResourceBeanDao);
        registerDao(MountResourceBean.class, mountResourceBeanDao);
        registerDao(RoomWatchedHistoryBean.class, roomWatchedHistoryBeanDao);
        registerDao(SendGiftResourceBean.class, sendGiftResourceBeanDao);
        registerDao(UserGuardResourceBean.class, userGuardResourceBeanDao);
        registerDao(UserLevelResourceBean.class, userLevelResourceBeanDao);
        registerDao(UserTagResourceBean.class, userTagResourceBeanDao);
        registerDao(UserVehiclePlayLimitBean.class, userVehiclePlayLimitBeanDao);
    }
    
    public void clear() {
        giftResourceBeanDaoConfig.clearIdentityScope();
        mountResourceBeanDaoConfig.clearIdentityScope();
        roomWatchedHistoryBeanDaoConfig.clearIdentityScope();
        sendGiftResourceBeanDaoConfig.clearIdentityScope();
        userGuardResourceBeanDaoConfig.clearIdentityScope();
        userLevelResourceBeanDaoConfig.clearIdentityScope();
        userTagResourceBeanDaoConfig.clearIdentityScope();
        userVehiclePlayLimitBeanDaoConfig.clearIdentityScope();
    }

    public GiftResourceBeanDao getGiftResourceBeanDao() {
        return giftResourceBeanDao;
    }

    public MountResourceBeanDao getMountResourceBeanDao() {
        return mountResourceBeanDao;
    }

    public RoomWatchedHistoryBeanDao getRoomWatchedHistoryBeanDao() {
        return roomWatchedHistoryBeanDao;
    }

    public SendGiftResourceBeanDao getSendGiftResourceBeanDao() {
        return sendGiftResourceBeanDao;
    }

    public UserGuardResourceBeanDao getUserGuardResourceBeanDao() {
        return userGuardResourceBeanDao;
    }

    public UserLevelResourceBeanDao getUserLevelResourceBeanDao() {
        return userLevelResourceBeanDao;
    }

    public UserTagResourceBeanDao getUserTagResourceBeanDao() {
        return userTagResourceBeanDao;
    }

    public UserVehiclePlayLimitBeanDao getUserVehiclePlayLimitBeanDao() {
        return userVehiclePlayLimitBeanDao;
    }

}
