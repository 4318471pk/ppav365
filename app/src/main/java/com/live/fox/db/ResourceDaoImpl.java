package com.live.fox.db;

import com.live.fox.entity.MountResourceBean;

import java.util.List;

public interface ResourceDaoImpl<T> {

    public void insertOrReplaceList(final List<T> list);
    void deleteAll();
    List<T> queryList();
}
