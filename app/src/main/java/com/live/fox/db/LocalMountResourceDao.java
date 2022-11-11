package com.live.fox.db;

import com.live.fox.common.CommonApp;
import com.live.fox.entity.MountResourceBean;
import com.live.fox.utils.LogUtils;

import java.util.List;

import app.resource.db.MountResourceBeanDao;

public class LocalMountResourceDao implements ResourceDaoImpl<MountResourceBean> {

    private static LocalMountResourceDao localMountDao;
    private boolean isAvailable=true;
    ResourceDataListener resourceDataListener;

    public static LocalMountResourceDao getInstance()
    {
        if(localMountDao==null)
        {
            localMountDao=new LocalMountResourceDao();
        }
        return localMountDao;
    }

    public void setResourceDataListener(ResourceDataListener resourceDataListener) {
        this.resourceDataListener = resourceDataListener;
        if(isAvailable)
        {
            resourceDataListener.onDataInsertDone(true);
        }
    }

    @Override
    public void insertOrReplaceList(final List<MountResourceBean> list){
        if(null==list || !isAvailable){
            return;
        }

        try{
            isAvailable=false;
            CommonApp.getInstance().getDaoSession().runInTx(new Runnable() {
                @Override
                public void run() {
                    Long count=CommonApp.getInstance().getDaoSession().getMountResourceBeanDao().count();
                    if(count>0)
                    {
                        for (int i = 0; i < list.size(); i++) {
                            MountResourceBean newBean=list.get(i);
                            List<MountResourceBean> beans= CommonApp.getInstance().getDaoSession().getMountResourceBeanDao()
                                    .queryBuilder().where(MountResourceBeanDao.Properties.Id.eq(newBean.getId())).list();

                            if(beans!=null && beans.size()>0)
                            {
                                MountResourceBean oldBean=beans.get(0);
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
                                    list.get(i).setLocalSvgPath(oldBean.getLocalSvgPath());
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
                    CommonApp.getInstance().getDaoSession().getMountResourceBeanDao().insertOrReplaceInTx(list);
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
    public void deleteAll(){
        CommonApp.getInstance().getDaoSession().getMountResourceBeanDao().deleteAll();
    }

    @Override
    public List<MountResourceBean> queryList() {
        List<MountResourceBean> mountResourceBeans= CommonApp.getInstance().getDaoSession().getMountResourceBeanDao().queryBuilder().list();
        return mountResourceBeans;
    }

    @Override
    public void updateData(MountResourceBean mountResourceBean) {
       MountResourceBeanDao dao= CommonApp.getInstance().getDaoSession().getMountResourceBeanDao();
       dao.update(mountResourceBean);
    }
}