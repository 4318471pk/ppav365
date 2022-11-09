package com.live.fox.db;

import com.live.fox.common.CommonApp;
import com.live.fox.entity.UserLevelResourceBean;
import com.live.fox.utils.LogUtils;

import java.util.List;

import app.resource.db.UserLevelResourceBeanDao;

public class LocalUserLevelDao implements ResourceDaoImpl<UserLevelResourceBean>{

    private static LocalUserLevelDao localUserLevelDao;
    private boolean isAvailable=true;
    ResourceDataListener resourceDataListener;

    public static LocalUserLevelDao getInstance()
    {
        if(localUserLevelDao==null)
        {
            localUserLevelDao=new LocalUserLevelDao();
        }
        return localUserLevelDao;
    }

    public void setResourceDataListener(ResourceDataListener resourceDataListener) {
        this.resourceDataListener = resourceDataListener;
        if(isAvailable)
        {
            resourceDataListener.onDataInsertDone(true);
        }
    }

    @Override
    public void insertOrReplaceList(List<UserLevelResourceBean> list) {
        if(null==list || !isAvailable){
            return;
        }

        try{
            isAvailable=false;
            CommonApp.getInstance().getDaoSession().runInTx(new Runnable() {
                @Override
                public void run() {
                    Long count=CommonApp.getInstance().getDaoSession().getUserLevelResourceBeanDao().count();
                    if(count>0)
                    {
                        for (int i = 0; i < list.size(); i++) {
                            UserLevelResourceBean newBean=list.get(i);
                            List<UserLevelResourceBean> beans= CommonApp.getInstance().getDaoSession().getUserLevelResourceBeanDao()
                                    .queryBuilder().where(UserLevelResourceBeanDao.Properties.Id.eq(newBean.getId())).list();

                            if(beans!=null && beans.size()>0)
                            {
                                UserLevelResourceBean oldBean=beans.get(0);
                                if(newBean.getUpdateTime()!=null && oldBean.getUpdateTime()!=null && oldBean.getUpdateTime()<newBean.getUpdateTime())
                                {
                                    //需要更新
                                    list.get(i).setLocalShouldUpdate(1);
                                }
                                else
                                {
                                    //设置为原来的状态 原来需要更新就更新
                                    list.get(i).setLocalShouldUpdate(oldBean.getLocalShouldUpdate());
                                }
                            }
                            else
                            {
                                //如果原本的数据没有 等于是新增的
                                list.get(i).setLocalShouldUpdate(1);
                            }
                        }
                    }
                    else
                    {
                        //没有数据不用更新
                        for (int i = 0; i < list.size(); i++) {
                            list.get(i).setLocalShouldUpdate(0);
                        }
                    }

                    deleteAll();
                    CommonApp.getInstance().getDaoSession().getUserLevelResourceBeanDao().insertOrReplaceInTx(list);
                    isAvailable=true;
                    resourceDataListener.onDataInsertDone(true);
                }
            });
        }
        catch (Exception exception){
            LogUtils.e(exception.toString());
            isAvailable=true;
            resourceDataListener.onDataInsertDone(true);
        }
    }

    @Override
    public void deleteAll() {
        CommonApp.getInstance().getDaoSession().getUserLevelResourceBeanDao().deleteAll();
    }

    @Override
    public List<UserLevelResourceBean> queryList() {
        List<UserLevelResourceBean> userLevelResourceBeans= CommonApp.getInstance().getDaoSession().getUserLevelResourceBeanDao().queryBuilder().list();
        return userLevelResourceBeans;
    }
}
