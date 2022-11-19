package com.live.fox.db;

import com.live.fox.common.CommonApp;
import com.live.fox.entity.UserLevelResourceBean;
import com.live.fox.entity.UserTagResourceBean;
import com.live.fox.utils.LogUtils;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

import app.resource.db.UserLevelResourceBeanDao;
import app.resource.db.UserTagResourceBeanDao;

public class LocalUserTagResourceDao implements ResourceDaoImpl<UserTagResourceBean> {

    private static LocalUserTagResourceDao localUserTagDao;
    private boolean isAvailable=true;
    ResourceDataListener resourceDataListener;

    public static LocalUserTagResourceDao getInstance()
    {
        if(localUserTagDao==null)
        {
            localUserTagDao=new LocalUserTagResourceDao();
        }
        return localUserTagDao;
    }

    public void setResourceDataListener(ResourceDataListener resourceDataListener) {
        this.resourceDataListener = resourceDataListener;
    }

    @Override
    public void insertOrReplaceList(List<UserTagResourceBean> list) {
        if(null==list || !isAvailable){
            return;
        }

        try{
            isAvailable=false;
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
                                    list.get(i).setLocalMedalUrlPath(oldBean.getLocalMedalUrlPath());
                                    list.get(i).setLocalVipImgPath(oldBean.getLocalVipImgPath());
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
                    CommonApp.getInstance().getDaoSession().getUserTagResourceBeanDao().insertOrReplaceInTx(list);
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

    public long getCount()
    {
        return CommonApp.getInstance().getDaoSession().getUserTagResourceBeanDao().count();
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

    @Override
    public void updateData(UserTagResourceBean userTagResourceBean) {
        if(userTagResourceBean==null || userTagResourceBean.getId()==null)
        {
            return;
        }
      UserTagResourceBeanDao dao=  CommonApp.getInstance().getDaoSession().getUserTagResourceBeanDao();
      dao.update(userTagResourceBean);
    }

    public UserTagResourceBean getLevelTag(int level)
    {
        QueryBuilder<UserTagResourceBean> queryBuilder= CommonApp.getInstance().getDaoSession().getUserTagResourceBeanDao().queryBuilder();
        UserTagResourceBean userTagResourceBean= queryBuilder.where(UserTagResourceBeanDao.Properties.VipLevel.eq(level)).unique();
        return userTagResourceBean;
    }
}
