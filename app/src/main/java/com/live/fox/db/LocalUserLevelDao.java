package com.live.fox.db;

import android.util.Log;

import com.live.fox.Constant;
import com.live.fox.common.CommonApp;
import com.live.fox.entity.UserLevelResourceBean;
import com.live.fox.utils.LogUtils;

import org.greenrobot.greendao.query.QueryBuilder;

import java.io.File;
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
                                    list.get(i).setLocalImgPath(oldBean.getLocalImgPath());
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
                    CommonApp.getInstance().getDaoSession().getUserLevelResourceBeanDao().insertOrReplaceInTx(list);
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
    public void updateData(UserLevelResourceBean bean)
    {
        if(bean==null || bean.getId()==null)
        {
            return;
        }
        UserLevelResourceBeanDao dao= CommonApp.getInstance().getDaoSession().getUserLevelResourceBeanDao();
        dao.update(bean);
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

    public String getLevelIcon(int level)
    {
      QueryBuilder<UserLevelResourceBean> queryBuilder= CommonApp.getInstance().getDaoSession().getUserLevelResourceBeanDao().queryBuilder();
      UserLevelResourceBean userLevelResourceBeans= queryBuilder.where(UserLevelResourceBeanDao.Properties.Level.eq(level)).unique();
      if(userLevelResourceBeans!=null)
      {
          return userLevelResourceBeans.getLocalImgPath();
      }
      return "";
    }
}
