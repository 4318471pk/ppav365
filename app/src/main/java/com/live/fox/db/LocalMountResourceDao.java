package com.live.fox.db;

import android.text.TextUtils;

import com.live.fox.common.CommonApp;
import com.live.fox.entity.MountResourceBean;
import com.live.fox.entity.UserTagResourceBean;
import com.live.fox.utils.LogUtils;

import org.greenrobot.greendao.query.QueryBuilder;

import java.io.File;
import java.util.List;

import app.resource.db.MountResourceBeanDao;
import app.resource.db.UserTagResourceBeanDao;

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
                                    //检查本地文件 有存在 设置为原来的状态 原来需要更新就更新
                                    boolean isLocalPathAvailable = true;
                                    if (TextUtils.isEmpty(oldBean.getLocalImgPath()) || TextUtils.isEmpty(oldBean.getLocalSvgPath())) {
                                        isLocalPathAvailable = false;
                                    } else {
                                        File file1 = new File(oldBean.getLocalSvgPath());
                                        File file2 = new File(oldBean.getLocalImgPath());
                                        if (file1 != null && file1.exists() && file2 != null && file2.exists()) {
                                            isLocalPathAvailable = true;
                                        } else {
                                            isLocalPathAvailable = false;
                                        }
                                    }

                                    if (isLocalPathAvailable) {
                                        list.get(i).setLocalShouldUpdate(oldBean.getLocalShouldUpdate());
                                        list.get(i).setLocalSvgPath(oldBean.getLocalSvgPath());
                                        list.get(i).setLocalImgPath(oldBean.getLocalImgPath());
                                    }
                                    else {
                                        list.get(i).setLocalShouldUpdate(1);
                                    }
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
        if(mountResourceBean==null || mountResourceBean.getId()==null)
        {
            return;
        }
       MountResourceBeanDao dao= CommonApp.getInstance().getDaoSession().getMountResourceBeanDao();
       dao.update(mountResourceBean);
    }

    public MountResourceBean getVehicleById(long carId)
    {
        QueryBuilder<MountResourceBean> queryBuilder= CommonApp.getInstance().getDaoSession().getMountResourceBeanDao().queryBuilder();
        List<MountResourceBean> mountResourceBeans= queryBuilder.where(MountResourceBeanDao.Properties.Id.eq(carId)).list();
        if(mountResourceBeans!=null && mountResourceBeans.size()==1)
        {
            return mountResourceBeans.get(0);
        }
        return null;
    }
}
