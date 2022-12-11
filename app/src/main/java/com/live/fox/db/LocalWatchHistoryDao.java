package com.live.fox.db;

import com.live.fox.common.CommonApp;
import com.live.fox.entity.RoomWatchedHistoryBean;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

import app.resource.db.RoomWatchedHistoryBeanDao;

public class LocalWatchHistoryDao implements ResourceDaoImpl<RoomWatchedHistoryBean> {

    private static LocalWatchHistoryDao localWatchHistoryDao;

    public static LocalWatchHistoryDao getInstance() {
        if (localWatchHistoryDao == null) {
            localWatchHistoryDao = new LocalWatchHistoryDao();
        }
        return localWatchHistoryDao;
    }

    @Override
    public void insertOrReplaceList(List<RoomWatchedHistoryBean> list) {
        if (list == null || list.size() == 0) return;

        CommonApp.getInstance().getDaoSession().runInTx(new Runnable() {
            @Override
            public void run() {

            }
        });
    }

    @Override
    public void deleteAll() {
        CommonApp.getInstance().getDaoSession().getRoomWatchedHistoryBeanDao().deleteAll();
    }

    @Override
    public List<RoomWatchedHistoryBean> queryList() {
        return CommonApp.getInstance().getDaoSession().getRoomWatchedHistoryBeanDao().loadAll();
    }

    @Override
    public void updateData(RoomWatchedHistoryBean roomListBean) {

    }

    public long getCount() {
        return CommonApp.getInstance().getDaoSession().getRoomWatchedHistoryBeanDao().count();
    }

    public void insert(RoomWatchedHistoryBean roomListBean) {
        CommonApp.getInstance().getDaoSession().runInTx(new Runnable() {
            @Override
            public void run() {
                //到了有100条就删掉40条
                if (getCount() > 99) {
                    try {
                        String temple = "delete from %s where _id in(select _id from %s order by _id limit 40)";
                        String tableName = CommonApp.getInstance().getDaoSession().getRoomWatchedHistoryBeanDao().getTablename();
                        CommonApp.getInstance().getDaoSession().getDatabase().rawQuery(String.format(temple, tableName, tableName), null);
                    } catch (Exception exception) {
                        exception.printStackTrace();
                    }
                }


                if (roomListBean != null) {
                    QueryBuilder queryBuilder = CommonApp.getInstance().getDaoSession().getRoomWatchedHistoryBeanDao().queryBuilder();
                    List<RoomWatchedHistoryBean> list = queryBuilder.where(RoomWatchedHistoryBeanDao.Properties.LiveId.eq(roomListBean.getLiveId())).list();
                    if (list == null || list.size() == 0) {
                        CommonApp.getInstance().getDaoSession().getRoomWatchedHistoryBeanDao().insert(roomListBean);
                    }
                    else
                    {
                        //更新数据
                        roomListBean.setId(list.get(0).getId());
                        CommonApp.getInstance().getDaoSession().getRoomWatchedHistoryBeanDao().update(roomListBean);
                    }
                }

            }
        });


    }
}
