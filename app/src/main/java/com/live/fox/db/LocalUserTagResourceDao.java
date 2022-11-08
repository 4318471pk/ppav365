package com.live.fox.db;

import com.live.fox.common.CommonApp;
import com.live.fox.entity.UserTagResourceBean;
import com.live.fox.utils.LogUtils;

import java.util.List;

import app.resource.db.UserTagResourceBeanDao;

public class LocalUserTagResourceDao implements ResourceDaoImpl<UserTagResourceBean> {

    private static LocalUserTagResourceDao localUserTagDao;
    public static LocalUserTagResourceDao getInstance()
    {
        if(localUserTagDao==null)
        {
            localUserTagDao=new LocalUserTagResourceDao();
        }
        return localUserTagDao;
    }

    @Override
    public void insertOrReplaceList(List<UserTagResourceBean> list) {
        if(null==list){
            return;
        }

        try{
            CommonApp.getInstance().getDaoSession().runInTx(new Runnable() {
                @Override
                public void run() {
                    Long count=CommonApp.getInstance().getDaoSession().getUserTagResourceBeanDao().count();
                    if(count>0)
                    {
                        for (int i = 0; i < list.size(); i++) {
                            UserTagResourceBean newBean=list.get(i);
                            List<UserTagResourceBean> beans= CommonApp.getInstance().getDaoSession().getUserTagResourceBeanDao()
                                    .queryBuilder().where(UserTagResourceBeanDao.Properties.Id.eq(newBean.getId())).list();

                            if(beans!=null && beans.size()>0)
                            {
                                UserTagResourceBean oldBean=beans.get(0);
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
                    CommonApp.getInstance().getDaoSession().getUserTagResourceBeanDao().insertOrReplaceInTx(list);
                }
            });
        }
        catch (Exception exception){
            LogUtils.e(exception.toString());
        }
    }

    @Override
    public void deleteAll() {
        CommonApp.getInstance().getDaoSession().getUserTagResourceBeanDao().deleteAll();
    }

    @Override
    public List<UserTagResourceBean> queryList() {
        List<UserTagResourceBean> userTagResourceBeans= CommonApp.getInstance().getDaoSession().getUserTagResourceBeanDao().queryBuilder().list();
        return userTagResourceBeans;
    }
}
