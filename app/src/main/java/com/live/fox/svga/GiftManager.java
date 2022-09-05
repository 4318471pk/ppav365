package com.live.fox.svga;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.util.LruCache;

import com.live.fox.Constant;
import com.live.fox.R;
import com.live.fox.common.JsonCallback;
import com.live.fox.db.DataBase;
import com.live.fox.entity.Gift;
import com.live.fox.entity.MessageEvent;
import com.live.fox.okhttp.OkHttpUtil;
import com.live.fox.okhttp.callback.DownloadCallback;
import com.live.fox.server.Api_Config;
import com.live.fox.utils.FileUtils;
import com.live.fox.utils.LogUtils;
import com.live.fox.utils.SPUtils;
import com.live.fox.utils.Utils;
import com.live.fox.utils.ZipUtils;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


public class GiftManager {
    public static int failCount = 0;
    private LruCache<String, AnimationDrawable> mLruCache;
    private static ThreadPoolExecutor threadPool;
    private final DataBase db;


    private static class InstanceHolder {
        private static GiftManager instance = new GiftManager();
    }

    public static GiftManager ins() {
        return InstanceHolder.instance;
    }

    private GiftManager() {
        db = DataBase.getDbInstance();
        long l = Runtime.getRuntime().maxMemory();
        int cache = (int) (l / 8);
        mLruCache = new LruCache<String, AnimationDrawable>(cache) {
            @Override
            protected int sizeOf(String key, AnimationDrawable value) {
                int num = value.getNumberOfFrames();
                int size = 0;
                for (int i = 0; i < num; i++) {
                    BitmapDrawable drawable = (BitmapDrawable) value.getFrame(i);
                    Bitmap bitmap = drawable.getBitmap();
                    size += bitmap.getByteCount();
                }
                return size;
            }
        };
//        mLruCoverCache = new LruCache<String, Bitmap>(cache) {
//            @Override
//            protected int sizeOf(String key, Bitmap value) {
//                return value.getByteCount();
//            }
//        };
//        mLruCache.put("0", getDefaultAnim());

        final int CPU_COUNT = Runtime.getRuntime().availableProcessors();
        final int CORE_POOL_SIZE = CPU_COUNT + 1;
        final int MAXIMUM_POOL_SIZE = CPU_COUNT * 2 + 1;
        threadPool = new ThreadPoolExecutor(CORE_POOL_SIZE, MAXIMUM_POOL_SIZE,
                30, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
    }


    /**
     * 下载礼物
     */
    public void downloadAllGifts() {
        Api_Config.ins().getGiftList(new JsonCallback<List<Gift>>() {
            @Override
            public void onSuccess(int code, String msg, List<Gift> result) {
                if (code == 0 && result != null && result.size() > 0) {
                    upDataGiftDB(result);
                    for (Gift gift : result) {
                        if (!checkGiftIsNewstVersion(gift)) {
                            //叫gift太明显 就改名叫tx
                            String startFilePath = Constant.DEFAULT_DOWNLOAD_DIR + ".tx/" + gift.getGid() + "/";
                            FileUtils.deleteAllInDir(startFilePath);
                        }

                        if (gift.getPlayType() > 0) { // 大礼物才下载
                            if (!checkLocalGiftIsExist(gift)) {
                                LogUtils.e("Gift 下载不存在 " + gift.getGname());
                                download(gift);
                            } else {
                                LogUtils.e("Gift 下载已存在 " + gift.getGname());
                            }
                        }
                    }
                }
            }
        });
    }

    public void download(Gift gift) {
        //叫gift太明显 就改名叫tx  因为zip文件需要解压 所有后面要加上gift.getGid() + "/"
        String startFilePath = Constant.DEFAULT_DOWNLOAD_DIR + ".tx/" + gift.getGid() + "/";
        String downPath = startFilePath + gift.getGid() + (gift.getResourceUrl().endsWith(".svga") ? "" : ".zip");
        if (gift.getResourceUrl().startsWith("http://") || gift.getResourceUrl().startsWith("https://")) {
            threadPool.execute(new DownFileRunnable(gift.getResourceUrl(), startFilePath, downPath));
        }
    }

    /**
     * 更新礼物数据库
     */
    public void upDataGiftDB(List<Gift> giftList) {
        new Thread() {
            @Override
            public void run() {
                if (giftList.size() > 0) {
                    db.deleteAlLGift();
                    db.insertGiftList(giftList);
                    EventBus.getDefault().post(new MessageEvent(16, ""));
                }
            }
        }.start();
    }

    //检查礼物是否是最新版本
    public boolean checkGiftIsNewstVersion(Gift gift) {
        int version = SPUtils.getInstance("GiftVersion").getInt("gid," + gift.getGid(), -1111);
        SPUtils.getInstance("GiftVersion").put("gid," + gift.getGid(), gift.getVersion());
        if (version == -1111) {
            return false;
        } else {   //如果本地存的版本号与数据的版本号不一致 则删除本地文件
            return version == gift.getVersion();
        }
    }

    public boolean checkLocalGiftIsExist(Gift gift) {

        final String filePath = Constant.DEFAULT_DOWNLOAD_DIR + ".tx/" + gift.getGid() + "/";
        String fiePath = filePath + gift.getGid() + (gift.getResourceUrl().endsWith(".svga") ? "" : ".zip");
        File giftFile = new File(fiePath);

        if (!fiePath.endsWith(".zip")) {   //svga礼物
            return giftFile.exists();
        } else {  //gif礼物
            String samllPngPath = filePath + "s_" + gift.getBimgs() + ".png";
            String bigPngPath = filePath + "b_" + gift.getBimgs() + ".png";
            File samllPngFile = new File(samllPngPath);
            File bigPngFile = new File(bigPngPath);
            if (giftFile.exists()) {
                if (gift.getPlayType() == 0) {
                    if (!samllPngFile.exists()) {
                        try {
                            ZipUtils.unzipFile(fiePath, filePath);
                        } catch (IOException e) {
                            return false;
                        }
                    } else {
                        LogUtils.e("检测->下载完成 " + gift.getGname());
                    }
                    return true;
                } else {
                    if (!bigPngFile.exists()) {
                        try {
                            ZipUtils.unzipFile(filePath, filePath);
                        } catch (IOException e) {
                            return false;
                        }
                    } else {
                        LogUtils.e("检测->下载完成 " + gift.getGname());
                    }
                    return true;
                }
            } else {
                return false;
            }
        }
    }

//    public GiftBean getGiftById(String id) {
//      return GiftBean.findById(GiftBean.class, Long.valueOf(id));
//        return null;
//    }
//
//    public List<GiftBean> getShowGifts(int type) {
//     List<GiftBean> gifts = GiftBean.find(GiftBean.class, "is_show = 1 and type = 0 and subject = " + type);
//       Collections.sort(gifts, new SortComparator());
//        return null;
//    }
//
//    class SortComparator implements Comparator {
//        @Override
//        public int compare(Object lhs, Object rhs) {
//            GiftBean lui = (GiftBean) lhs;
//            GiftBean rui = (GiftBean) rhs;
//            return lui.getSort() - rui.getSort();
//        }
//    }

    /**
     * 使用LruCache 緩存AnimationDrawable
     */
    public AnimationDrawable getCacheAnimation(String gid) {

        AnimationDrawable cacheAnimation = mLruCache.get(gid);
        if (null == cacheAnimation) {
            cacheAnimation = getAnimation(gid);
            if (cacheAnimation != mLruCache.get("0")) {
                mLruCache.put(gid, cacheAnimation);
            }
        }
        return cacheAnimation;
    }

    private AnimationDrawable getAnimation(String gid) {
//        GiftBean giftBean = GiftBean.findById(GiftBean.class, Long.valueOf(gid));
//        if (giftBean == null) {
//            requestGifts();
//            return null;
//        }
//        int skin = giftBean.getGid();
//        AnimationDrawable anim = new AnimationDrawable();
//        anim.setOneShot(false);
//        String root = Constant.DEFAULT_DOWNLOAD_DIR + "gifts/" + skin + "/";
//        int count = giftBean.getSimgs();
//        int dur = (int) ((1000 * giftBean.getDuration()) / count);
//        LogUtils.e("getAnimation dur : " + dur);
//        if (dur == 0) {
//            dur = 90;
//        }
//        for (int i = 1; i <= count; i++) {
//            String name = root + "s_" + i + ".png";
//            File file = new File(name);
//            if (file.exists()) {
//                Drawable drawable = ImageUtils.bitmap2Drawable(ImageUtils.getBitmap(name));
//                anim.addFrame(drawable, dur);
//            } else {
//                LogUtils.e("GiftManager  !file.exists() :  " + name);
//                break;
//            }
//        }
//        if (anim.getNumberOfFrames() == 0) {
//            download(giftBean);
//            return mLruCache.get("0");
//        }
        return null;
    }

//    public void addCoverCache(String gid, Bitmap bitmap) {
//        Bitmap cover = mLruCoverCache.get(gid);
//        if (cover == null) {
//            mLruCoverCache.put(gid, bitmap);
//        }
//    }


    private AnimationDrawable getDefaultAnim() {
        AnimationDrawable drawable = (AnimationDrawable) Utils.getApp().getResources().getDrawable(R.drawable.gift_loading);
        drawable.setOneShot(false);
        return drawable;
    }

    private Bitmap getDefaultBitmap(int drawableId) {
        Bitmap mDefauleBitmap = null;
        Bitmap bitmap = BitmapFactory.decodeResource(Utils.getApp().getResources(), drawableId);
        if (bitmap != null) {
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();
            Matrix matrix = new Matrix();
            matrix.postScale(((float) 50) / width, ((float) 50) / height);
            mDefauleBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
        }
        return mDefauleBitmap;
    }


    private static class DownFileRunnable implements Runnable {

        private final String url;
        private final String startPath; //下载的目录
        private final String downPath;  //下载路径

        DownFileRunnable(String url, String startPath, String downPath) {
            this.url = url;
            this.startPath = startPath;
            this.downPath = downPath;
        }

        @Override
        public void run() {
            OkHttpUtil.doGetByExecute(url, new DownloadCallback(downPath, false) {
                @Override
                public void onResponse(boolean success, String filePath) {
                    if (success) {
                        if (filePath.endsWith(".zip")) {
                            FileUtils.unzipFile(filePath, startPath);
                        }
                    } else {
                        if ((failCount++) < 100) {
                            threadPool.execute(new DownFileRunnable(url, startPath, downPath));
                        }
                    }
                }
            });
        }
    }


    public interface OnImgDownCompleteListener {
        void onComplete(int adapterPos);
    }

    OnImgDownCompleteListener onImgDownCompleteListener;

    public void setOnImgDownCompleteListener(OnImgDownCompleteListener onImgDownCompleteListener) {
        this.onImgDownCompleteListener = onImgDownCompleteListener;
    }
}
