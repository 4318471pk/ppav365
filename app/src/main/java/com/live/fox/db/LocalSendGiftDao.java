package com.live.fox.db;

import com.live.fox.common.CommonApp;
import com.live.fox.entity.SendGiftResourceBean;
import com.live.fox.utils.LogUtils;

import java.util.List;

import app.resource.db.SendGiftResourceBeanDao;

public class LocalSendGiftDao implements ResourceDaoImpl<SendGiftResourceBean> {

    private static LocalSendGiftDao localSendGiftDao;
    private boolean isAvailable=true;
    ResourceDataListener resourceDataListener;

    public static LocalSendGiftDao getInstance()
    {
        if(localSendGiftDao==null)
        {
            localSendGiftDao=new LocalSendGiftDao();
        }
        return localSendGiftDao;
    }

    public void setResourceDataListener(ResourceDataListener resourceDataListener) {
        this.resourceDataListener = resourceDataListener;
    }

    @Override
    public void insertOrReplaceList(List<SendGiftResourceBean> list) {
        if(null==list || !isAvailable){
            return;
        }

        try{
            isAvailable=false;
            CommonApp.getInstance().getDaoSession().runInTx(new Runnable() {
                @Override
                public void run() {
                    Long count=CommonApp.getInstance().getDaoSession().getSendGiftResourceBeanDao().count();
                    if(count>0)
                    {
                        for (int i = 0; i < list.size(); i++) {
                            SendGiftResourceBean newBean=list.get(i);
                            List<SendGiftResourceBean> beans= CommonApp.getInstance().getDaoSession().getSendGiftResourceBeanDao()
                                    .queryBuilder().where(SendGiftResourceBeanDao.Properties.Id.eq(newBean.getId())).list();

                            if(beans!=null && beans.size()>0)
                            {
                                SendGiftResourceBean oldBean=beans.get(0);
                                if(newBean.getUpdateTime()!=null && oldBean.getUpdateTime()!=null && oldBean.getUpdateTime()<newBean.getUpdateTime())
                                {
                                    //需要更新
                                    list.get(i).setLocalShouldUpdate(1);
                                }
                                else
                                {
                                    //设置为原来的状态 原来需要更新就更新
                                    list.get(i).setLocalShouldUpdate(oldBean.getLocalShouldUpdate());
                                    list.get(i).setLocalSvgPath(oldBean.getLocalSvgPath());
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
                    CommonApp.getInstance().getDaoSession().getSendGiftResourceBeanDao().insertOrReplaceInTx(list);
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
        CommonApp.getInstance().getDaoSession().getSendGiftResourceBeanDao().deleteAll();
    }

    @Override
    public List<SendGiftResourceBean> queryList() {
        List<SendGiftResourceBean> sendGiftResourceBeans= CommonApp.getInstance().getDaoSession().getSendGiftResourceBeanDao().queryBuilder().list();
        return sendGiftResourceBeans;
    }

    @Override
    public void updateData(SendGiftResourceBean sendGiftResourceBean) {
       SendGiftResourceBeanDao dao= CommonApp.getInstance().getDaoSession().getSendGiftResourceBeanDao();
       dao.update(sendGiftResourceBean);
    }
}
