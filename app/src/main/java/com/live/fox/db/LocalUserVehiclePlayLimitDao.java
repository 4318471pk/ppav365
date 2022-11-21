package com.live.fox.db;

import com.live.fox.common.CommonApp;
import com.live.fox.entity.UserTagResourceBean;
import com.live.fox.entity.UserVehiclePlayLimitBean;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

import app.resource.db.UserTagResourceBeanDao;
import app.resource.db.UserVehiclePlayLimitBeanDao;

public class LocalUserVehiclePlayLimitDao implements ResourceDaoImpl<UserVehiclePlayLimitBean>{

    public static final int Audience=0;
    public static final int Anchor=1;

    private static LocalUserVehiclePlayLimitDao localUserVehiclePlayLimitDao;

    public static LocalUserVehiclePlayLimitDao getInstance()
    {
        if(localUserVehiclePlayLimitDao==null)
        {
            localUserVehiclePlayLimitDao=new LocalUserVehiclePlayLimitDao();
        }
        return localUserVehiclePlayLimitDao;
    }

    @Override
    public void insertOrReplaceList(List<UserVehiclePlayLimitBean> list) {

    }

    @Override
    public void deleteAll() {
        CommonApp.getInstance().getDaoSession().getUserVehiclePlayLimitBeanDao().deleteAll();
    }

    @Override
    public List<UserVehiclePlayLimitBean> queryList() {
        List<UserVehiclePlayLimitBean> userVehiclePlayLimitBeans= CommonApp.getInstance().getDaoSession().getUserVehiclePlayLimitBeanDao().queryBuilder().list();
        return userVehiclePlayLimitBeans;
    }

    @Override
    public void updateData(UserVehiclePlayLimitBean userVehiclePlayLimitBean) {
        if(userVehiclePlayLimitBean==null || userVehiclePlayLimitBean.getId()==null)
        {
            return;
        }
        UserVehiclePlayLimitBeanDao dao=  CommonApp.getInstance().getDaoSession().getUserVehiclePlayLimitBeanDao();
        dao.update(userVehiclePlayLimitBean);
    }

    public UserVehiclePlayLimitBean selectByLiveIDAndUID(String liveId,String uid,int type)
    {
        QueryBuilder<UserVehiclePlayLimitBean> queryBuilder= CommonApp.getInstance().getDaoSession().getUserVehiclePlayLimitBeanDao().queryBuilder();
        List<UserVehiclePlayLimitBean> userTagResourceBeans= queryBuilder.where(UserVehiclePlayLimitBeanDao.Properties.LiveId.eq(liveId)
                ,UserVehiclePlayLimitBeanDao.Properties.Uid.eq(uid)).list();
        if(userTagResourceBeans!=null && userTagResourceBeans.size()==1)
        {
            return userTagResourceBeans.get(0);
        }
        return null;
    }

    public void insert(UserVehiclePlayLimitBean userVehiclePlayLimitBean)
    {
        if(userVehiclePlayLimitBean!=null)
        {
            CommonApp.getInstance().getDaoSession().getUserVehiclePlayLimitBeanDao().insert(userVehiclePlayLimitBean);
        }
    }
}
