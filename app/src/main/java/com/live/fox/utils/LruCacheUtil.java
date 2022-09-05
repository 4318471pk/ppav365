package com.live.fox.utils;

import android.graphics.Bitmap;
import android.util.LruCache;

import com.live.fox.entity.Anchor;
import com.live.fox.manager.TaskManager;

import java.util.ArrayList;


/**
 * 此类仅用于房间公聊列表礼物图片的缓存，其它地方请慎用
 * User: ljx
 * Date: 2017/9/20
 * Time: 10:42
 */
public class LruCacheUtil {

    private static LruCacheUtil lruCacheUtil;
    private final ArrayList<Anchor> mMemoryCache = new ArrayList<>();

    public static LruCacheUtil getInstance() {
        if (lruCacheUtil == null) {
            synchronized (TaskManager.class) {
                if (lruCacheUtil == null) {
                    lruCacheUtil = new LruCacheUtil();
                }
            }
        }
        return lruCacheUtil;
    }



    //添加Bitmap到内存缓存
    public void put(Anchor anchor) {
            mMemoryCache.add(anchor);

    }

    //从内存缓存中获取一个Bitmap
    public ArrayList<Anchor> get() {
        return mMemoryCache;
    }
}
