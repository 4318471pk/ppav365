package com.live.fox.db;

import android.text.TextUtils;

import com.live.fox.common.CommonApp;
import com.live.fox.entity.GiftResourceBean;
import com.live.fox.entity.UserLevelResourceBean;
import com.live.fox.utils.LogUtils;

import org.greenrobot.greendao.query.QueryBuilder;

import java.io.File;
import java.util.List;

import app.resource.db.GiftResourceBeanDao;
import app.resource.db.UserLevelResourceBeanDao;

public class LocalGiftDao implements ResourceDaoImpl<GiftResourceBean>{

    private static LocalGiftDao localGiftDao;
    private boolean isAvailable=true;
    ResourceDataListener resourceDataListener;

    public static LocalGiftDao getInstance()
    {
        if(localGiftDao==null)
        {
            localGiftDao=new LocalGiftDao();
        }
        return localGiftDao;
    }

    public void setResourceDataListener(ResourceDataListener resourceDataListener) {
        this.resourceDataListener = resourceDataListener;
    }

    @Override
    public void insertOrReplaceList(List<GiftResourceBean> list) {
        if(null==list || !isAvailable){
            return;
        }

        try{
            isAvailable=false;
            CommonApp.getInstance().getDaoSession().runInTx(new Runnable() {
                @Override
                public void run() {
                    Long count=CommonApp.getInstance().getDaoSession().getGiftResourceBeanDao().count();
                    if(count>0)
                    {
                        for (int i = 0; i < list.size(); i++) {
                            GiftResourceBean newBean=list.get(i);
                            List<GiftResourceBean> beans= CommonApp.getInstance().getDaoSession().getGiftResourceBeanDao()
                                    .queryBuilder().where(GiftResourceBeanDao.Properties.Id.eq(newBean.getId())).list();

                            if(beans!=null && beans.size()>0)
                            {
                                GiftResourceBean oldBean=beans.get(0);
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
                    CommonApp.getInstance().getDaoSession().getGiftResourceBeanDao().insertOrReplaceInTx(list);
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
        CommonApp.getInstance().getDaoSession().getGiftResourceBeanDao().deleteAll();
    }

    @Override
    public List<GiftResourceBean> queryList() {
        List<GiftResourceBean> giftResourceBeans= CommonApp.getInstance().getDaoSession().getGiftResourceBeanDao().queryBuilder().list();
        return giftResourceBeans;
    }

    @Override
    public void updateData(GiftResourceBean giftResourceBean) {
        if(giftResourceBean==null || giftResourceBean.getId()==null)
        {
            return;
        }
       GiftResourceBeanDao dao= CommonApp.getInstance().getDaoSession().getGiftResourceBeanDao();
       dao.update(giftResourceBean);
    }

    public GiftResourceBean getGift(long gid)
    {
        QueryBuilder<GiftResourceBean> queryBuilder= CommonApp.getInstance().getDaoSession().getGiftResourceBeanDao().queryBuilder();
        List<GiftResourceBean> giftResourceBeans= queryBuilder.where(GiftResourceBeanDao.Properties.Id.eq(gid)).list();
        if(giftResourceBeans!=null && giftResourceBeans.size()==1)
        {
            return giftResourceBeans.get(0);
        }
        return null;
    }
}
