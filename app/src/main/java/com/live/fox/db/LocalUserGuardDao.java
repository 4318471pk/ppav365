package com.live.fox.db;

import com.live.fox.common.CommonApp;
import com.live.fox.entity.UserGuardResourceBean;
import com.live.fox.entity.UserTagResourceBean;
import com.live.fox.utils.LogUtils;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

import app.resource.db.UserGuardResourceBeanDao;
import app.resource.db.UserTagResourceBeanDao;

public class LocalUserGuardDao implements ResourceDaoImpl<UserGuardResourceBean>{

    private static LocalUserGuardDao localUserGuardDao;
    private boolean isAvailable=true;
    ResourceDataListener resourceDataListener;

    public static LocalUserGuardDao getInstance()
    {
        if(localUserGuardDao==null)
        {
            localUserGuardDao=new LocalUserGuardDao();
        }
        return localUserGuardDao;
    }

    public void setResourceDataListener(ResourceDataListener resourceDataListener) {
        this.resourceDataListener = resourceDataListener;
    }

    @Override
    public void insertOrReplaceList(List<UserGuardResourceBean> list) {
        if(null==list || !isAvailable){
            return;
        }

        try{
            isAvailable=false;
            CommonApp.getInstance().getDaoSession().runInTx(new Runnable() {
                @Override
                public void run() {
                    Long count=CommonApp.getInstance().getDaoSession().getUserGuardResourceBeanDao().count();
                    if(count>0)
                    {
                        for (int i = 0; i < list.size(); i++) {
                            UserGuardResourceBean newBean=list.get(i);
                            List<UserGuardResourceBean> beans= CommonApp.getInstance().getDaoSession().getUserGuardResourceBeanDao()
                                    .queryBuilder().where(UserGuardResourceBeanDao.Properties.Id.eq(newBean.getId())).list();

                            if(beans!=null && beans.size()>0)
                            {
                                UserGuardResourceBean oldBean=beans.get(0);
                                if(newBean.getUpdateTime()!=null && oldBean.getUpdateTime()!=null && oldBean.getUpdateTime()<newBean.getUpdateTime())
                                {
                                    //需要更新
                                    list.get(i).setLocalShouldUpdate(1);
                                }
                                else
                                {
                                    //设置为原来的状态 原来需要更新就更新
                                    list.get(i).setLocalShouldUpdate(oldBean.getLocalShouldUpdate());
                                    list.get(i).setLocalImgSmallPath(oldBean.getLocalImgSmallPath());
                                    list.get(i).setLocalImgMediumPath(oldBean.getLocalImgMediumPath());
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
                        //本地没有数据 必须更新
                        for (int i = 0; i < list.size(); i++) {
                            list.get(i).setLocalShouldUpdate(1);
                        }
                    }

                    deleteAll();
                    CommonApp.getInstance().getDaoSession().getUserGuardResourceBeanDao().insertOrReplaceInTx(list);
                    isAvailable=true;
                    if(resourceDataListener!=null)
                    {
                        resourceDataListener.onDataInsertDone(true);
                    }
                }
            });
        }
        catch (Exception exception){
            LogUtils.e(exception.toString());
            isAvailable=true;
            if(resourceDataListener!=null)
            {
                resourceDataListener.onDataInsertDone(false);
            }
        }
    }

    @Override
    public void deleteAll() {
        CommonApp.getInstance().getDaoSession().getUserGuardResourceBeanDao().deleteAll();
    }

    @Override
    public List<UserGuardResourceBean> queryList() {
        List<UserGuardResourceBean> userGuardResourceBeans= CommonApp.getInstance().getDaoSession().getUserGuardResourceBeanDao().queryBuilder().list();
        return userGuardResourceBeans;
    }

    @Override
    public void updateData(UserGuardResourceBean userGuardResourceBean) {
      UserGuardResourceBeanDao dao=  CommonApp.getInstance().getDaoSession().getUserGuardResourceBeanDao();
      dao.update(userGuardResourceBean);
    }

    public UserGuardResourceBean getLevel(int level)
    {
        QueryBuilder<UserGuardResourceBean> queryBuilder= CommonApp.getInstance().getDaoSession().getUserGuardResourceBeanDao().queryBuilder();
        UserGuardResourceBean userGuardResourceBean= queryBuilder.where(UserGuardResourceBeanDao.Properties.GuardLevel.eq(level)).unique();
        return userGuardResourceBean;
    }

}
