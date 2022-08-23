package com.live.fox.svga;

import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.util.LruCache;

import com.live.fox.Constant;
import com.live.fox.common.JsonCallback;
import com.live.fox.db.DataBase;
import com.live.fox.entity.AdmissionEntity;
import com.live.fox.entity.Audience;
import com.live.fox.okhttp.OkHttpUtil;
import com.live.fox.okhttp.callback.DownloadCallback;
import com.live.fox.server.Api_Config;
import com.live.fox.utils.FileUtils;
import com.live.fox.utils.LogUtils;
import com.live.fox.utils.SPUtils;
import com.live.fox.utils.StringUtils;

import java.io.File;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 入场动画管理
 */
public class AdmissionManager {

    public static int failCount = 0;
    private LruCache<String, AnimationDrawable> mLruCache;
    private static ThreadPoolExecutor threadPool;

    private static AdmissionManager admission = null;

    private AdmissionManager() {
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

        final int CPU_COUNT = Runtime.getRuntime().availableProcessors();
        //最大线程数在2-8之间
        final int maximumPoolSize = Math.max(2, Math.min(CPU_COUNT, 8));
        threadPool = new ThreadPoolExecutor(maximumPoolSize, maximumPoolSize,
                30, TimeUnit.SECONDS, new LinkedBlockingQueue<>());
    }

    public static AdmissionManager getInstance() {
        if (admission == null) {
            admission = new AdmissionManager();
        }
        return admission;
    }

    /**
     * 下载入场动画
     */
    public void downloadAdmissionGiftList() {

        Api_Config.ins().getAdmissionList(new JsonCallback<List<AdmissionEntity>>() {
            @Override
            public void onSuccess(int code, String msg, List<AdmissionEntity> result) {
                if (result != null) LogUtils.e("Admission : " + result);
                if (code == 0) {
                    if (result != null && result.size() > 0) {
                        upDataAdmissionDB(result);
                        for (AdmissionEntity admission : result) {
                            download(admission);
                        }
                    }
                }
            }
        });
    }

    /**
     * 更新入场动画数据库
     *
     * @param result 后台请求的列表
     */
    private void upDataAdmissionDB(List<AdmissionEntity> result) {
        new Thread() {
            @Override
            public void run() {
                if (result.size() > 0) {
                    DataBase db = DataBase.getDbInstance();
                    db.deleteAllAdmission();
                    db.insertAdmissionList(result);
                }
            }
        }.start();
    }

    public void download(AdmissionEntity admission) {
        String startFilePath = Constant.DEFAULT_DOWNLOAD_DIR + ".admission/" + admission.getLevel() + "/";
        String downPath = startFilePath + admission.getLevel() + (admission.getResourceUrl().endsWith(".svga") ? "" : ".zip");
        if (admission.getResourceUrl().startsWith("http://") || admission.getResourceUrl().startsWith("https://")) {
            threadPool.execute(new DownFileRunnable(admission.getResourceUrl(), startFilePath, downPath));
        }
    }

    //替换所有资源的域名
    public String replaceDomainTwo(String path) {
        if (path == null)
            return "";

        String url = "";
        try {
            url = path.toString();
        } catch (Exception e) {
            return "";
        }

        if (StringUtils.isEmpty(url))
            return "";

        String domain = SPUtils.getInstance("domain").getString("domainTwo", "");
        if (StringUtils.isEmpty(domain)) {
            domain = "www.baudu.com";
        }

        Uri mUri = Uri.parse(url);

        String startStr = url.startsWith("http://") ? "http://" : "https://";
        String endStr = url.split("/")[url.split("/").length - 1];


        url = url.replace(mUri.getScheme() + "://" + mUri.getAuthority(), startStr + domain);
//        LogUtils.e("替换url:"+url);
        return url;
    }

    /**
     * 获取SVGA动画的地址
     *
     * @param audience 当前进程的用户
     * @return 返回图片名字
     */
    public File getSvgAFile(Audience audience) {
        AdmissionEntity admission = DataBase.getDbInstance().getAdmissionByLevel(audience.getLevel());
        String path = Constant.DEFAULT_DOWNLOAD_DIR + ".admission/" + admission.getLevel() + "/";
        path = path + (admission.getLevel());
        return new File(path);
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

}
