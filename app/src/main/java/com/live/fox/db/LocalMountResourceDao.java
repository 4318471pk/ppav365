package com.live.fox.db;

import com.live.fox.common.CommonApp;
import com.live.fox.entity.MountResourceBean;
import com.live.fox.utils.LogUtils;

import java.util.List;

public class LocalMountResourceDao implements ResourceDaoImpl<MountResourceBean> {

    private static LocalMountResourceDao localMountDao;
    public static LocalMountResourceDao getInstance()
    {
        if(localMountDao==null)
        {
            localMountDao=new LocalMountResourceDao();
        }
        return localMountDao;
    }

    @Override
    public void insertOrReplaceList(final List<MountResourceBean> list){
        if(null==list){
            return;
        }
        try{
            CommonApp.getInstance().getDaoSession().runInTx(new Runnable() {
                @Override
                public void run() {
                    deleteAll();
                    CommonApp.getInstance().getDaoSession().getMountResourceBeanDao().insertOrReplaceInTx(list);
                }
            });
        }
        catch (Exception exception){
            LogUtils.e(exception.toString());
        }
    }

    @Override
    public void deleteAll(){
        CommonApp.getInstance().getDaoSession().getMountResourceBeanDao().deleteAll();
    }

    @Override
    public List<MountResourceBean> queryList() {
        List<MountResourceBean> gameItemBeans= CommonApp.getInstance().getDaoSession().getMountResourceBeanDao().queryBuilder().list();
        return gameItemBeans;
    }
}
