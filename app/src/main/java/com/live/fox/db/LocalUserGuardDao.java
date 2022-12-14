package com.live.fox.db;

import android.text.TextUtils;

import com.live.fox.common.CommonApp;
import com.live.fox.entity.UserGuardResourceBean;
import com.live.fox.entity.UserTagResourceBean;
import com.live.fox.utils.LogUtils;

import org.greenrobot.greendao.query.QueryBuilder;

import java.io.File;
import java.util.List;

import app.resource.db.UserGuardResourceBeanDao;
import app.resource.db.UserTagResourceBeanDao;

public class LocalUserGuardDao implements ResourceDaoImpl<UserGuardResourceBean> {

    private static LocalUserGuardDao localUserGuardDao;
    private boolean isAvailable = true;
    ResourceDataListener resourceDataListener;

    public static LocalUserGuardDao getInstance() {
        if (localUserGuardDao == null) {
            localUserGuardDao = new LocalUserGuardDao();
        }
        return localUserGuardDao;
    }

    public void setResourceDataListener(ResourceDataListener resourceDataListener) {
        this.resourceDataListener = resourceDataListener;
    }

    @Override
    public void insertOrReplaceList(List<UserGuardResourceBean> list) {
        if (null == list || !isAvailable) {
            return;
        }

        try {
            isAvailable = false;
            CommonApp.getInstance().getDaoSession().runInTx(new Runnable() {
                @Override
                public void run() {
                    Long count = CommonApp.getInstance().getDaoSession().getUserGuardResourceBeanDao().count();
                    if (count > 0) {
                        for (int i = 0; i < list.size(); i++) {
                            UserGuardResourceBean newBean = list.get(i);
                            List<UserGuardResourceBean> beans = CommonApp.getInstance().getDaoSession().getUserGuardResourceBeanDao()
                                    .queryBuilder().where(UserGuardResourceBeanDao.Properties.Id.eq(newBean.getId())).list();

                            if (beans != null && beans.size() > 0) {
                                UserGuardResourceBean oldBean = beans.get(0);
                                if (newBean.getUpdateTime() != null && oldBean.getUpdateTime() != null && oldBean.getUpdateTime() < newBean.getUpdateTime()) {
                                    //????????????
                                    list.get(i).setLocalShouldUpdate(1);
                                } else {
                                    //?????????????????? ????????? ???????????????????????? ???????????????????????????
                                    boolean isLocalPathAvailable = true;
                                    if (TextUtils.isEmpty(oldBean.getLocalImgSmallPath()) || TextUtils.isEmpty(oldBean.getLocalImgMediumPath())) {
                                        isLocalPathAvailable = false;
                                    } else {
                                        File file1 = new File(oldBean.getLocalImgSmallPath());
                                        File file2 = new File(oldBean.getLocalImgMediumPath());
                                        if (file1 != null && file1.exists() && file2 != null && file2.exists()) {
                                            isLocalPathAvailable = true;
                                        } else {
                                            isLocalPathAvailable = false;
                                        }
                                    }

                                    if (isLocalPathAvailable) {
                                        list.get(i).setLocalShouldUpdate(oldBean.getLocalShouldUpdate());
                                        list.get(i).setLocalImgSmallPath(oldBean.getLocalImgSmallPath());
                                        list.get(i).setLocalImgMediumPath(oldBean.getLocalImgMediumPath());
                                    }
                                    else {
                                        list.get(i).setLocalShouldUpdate(1);
                                    }
                                }
                            } else {
                                //??????????????????????????? ??????????????????
                                list.get(i).setLocalShouldUpdate(1);
                            }
                        }
                    } else {
                        //?????????????????? ????????????
                        for (int i = 0; i < list.size(); i++) {
                            list.get(i).setLocalShouldUpdate(1);
                        }
                    }

                    deleteAll();
                    CommonApp.getInstance().getDaoSession().getUserGuardResourceBeanDao().insertOrReplaceInTx(list);
                    isAvailable = true;
                    if (resourceDataListener != null) {
                        resourceDataListener.onDataInsertDone(true);
                    }
                }
            });
        } catch (Exception exception) {
            LogUtils.e(exception.toString());
            isAvailable = true;
            if (resourceDataListener != null) {
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
        List<UserGuardResourceBean> userGuardResourceBeans = CommonApp.getInstance().getDaoSession().getUserGuardResourceBeanDao().queryBuilder().list();
        return userGuardResourceBeans;
    }

    @Override
    public void updateData(UserGuardResourceBean userGuardResourceBean) {
        if (userGuardResourceBean == null || userGuardResourceBean.getId() == null) {
            return;
        }
        UserGuardResourceBeanDao dao = CommonApp.getInstance().getDaoSession().getUserGuardResourceBeanDao();
        dao.update(userGuardResourceBean);
    }

    public UserGuardResourceBean getLevel(int level) {
        QueryBuilder<UserGuardResourceBean> queryBuilder = CommonApp.getInstance().getDaoSession().getUserGuardResourceBeanDao().queryBuilder();
        List<UserGuardResourceBean> userGuardResourceBeans = queryBuilder.where(UserGuardResourceBeanDao.Properties.GuardLevel.eq(level)).list();
        if(userGuardResourceBeans!=null && userGuardResourceBeans.size()==1)
        {
            return userGuardResourceBeans.get(0);
        }
        return null;
    }

    public long getCount() {
        long count = CommonApp.getInstance().getDaoSession().getUserGuardResourceBeanDao().count();
        return count;
    }

}
