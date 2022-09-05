package com.live.fox.svga;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.util.Log;
import android.util.LruCache;

import com.live.fox.Constant;
import com.live.fox.common.JsonCallback;
import com.live.fox.db.DataBase;
import com.live.fox.entity.Badge;
import com.live.fox.server.Api_Config;
import com.live.fox.utils.HTTPSTrustManager;
import com.live.fox.utils.IOUtils;
import com.live.fox.utils.LogUtils;
import com.live.fox.utils.SPUtils;
import com.live.fox.utils.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

/**
 * 微章管理
 */
public class BadgesManager {

    private BadgesManager() {
        long l = Runtime.getRuntime().maxMemory();
        int cache = (int) (l / 8);
        LruCache<String, AnimationDrawable> mLruCache = new LruCache<String, AnimationDrawable>(cache) {
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
    }

    private static class InstanceHolder {
        private static final BadgesManager instance = new BadgesManager();
    }

    public static BadgesManager ins() {
        return InstanceHolder.instance;
    }

    /**
     * 下载徽章
     */
    public void downloadAllBadges() {
        Api_Config.ins().getBadgeList(new JsonCallback<List<Badge>>() {
            @Override
            public void onSuccess(int code, String msg, List<Badge> result) {
                if (code == 0) {
                    if (result != null && result.size() != 0) {
                        new Thread() {
                            @Override
                            public void run() {
                                upDataBadgeDB(result);
                                for (Badge badge : result) {
                                    String downloadPath = Constant.DEFAULT_DOWNLOAD_DIR + ".badge/" + badge.getBid() + ".png";
                                    LogUtils.i("badge.getLogoUrl()" + badge.getLogoUrl());
                                    boolean isSuccess = downFile(badge.getLogoUrl(), downloadPath);
                                    LogUtils.i("badge.getLogoUrl()" + replaceDomainTwo(badge.getLogoUrl()));
                                    LogUtils.i(badge.getName() + "徽章下载成功");
                                }
                            }
                        }.start();
                    }
                }
            }
        });
    }


    /**
     * 更新数据库
     */
    public void upDataBadgeDB(List<Badge> badgeList) {
        if (badgeList.size() > 0) {
            DataBase db = DataBase.getDbInstance();
            db.deleteAlLBadge();
            LogUtils.e("getBadgeList count1 " + db.getBadgeCount());
            db.insertBadgeList(badgeList);
            LogUtils.e("getBadgeList count2 " + db.getBadgeCount());
        }
    }


    /**
     * ChatSpanUtils專用
     *
     * @param bid
     * @return
     */
    public Bitmap getCoverByGid(final String bid) {
        final String filePath = Constant.DEFAULT_DOWNLOAD_DIR + ".badge/" + bid + ".png";
        File tempFile = new File(filePath);
        if (tempFile.exists()) {
            try {
                FileInputStream fis = new FileInputStream(filePath);
                return BitmapFactory.decodeStream(fis);
            } catch (Exception e) {
                Log.e("FrameAnimationUtil", "文件讀取錯誤");
                return null;
            }
        } else {
            return null;
        }
    }


    public boolean checkDownload(Badge badge) {
        final String filePath = Constant.DEFAULT_DOWNLOAD_DIR + ".badge/" + badge.getBid() + ".png";
        File file = new File(filePath);
        if (!file.exists()) {
            LogUtils.e("badge 下载文件不存在 : " + file.toString());
            return false;
        } else {
            LogUtils.e("badge 下载已存在 : " + file.toString());
            return true;
        }
    }

    /**
     * 替换所有资源的域名
     *
     * @param path
     * @return
     */
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
        return url;
    }

    /**
     * 下载文件 支持https
     *
     * @param httpUrl 网络链接
     * @param path    下载目的地
     */
    private boolean downFile(String httpUrl, String path) {
        LogUtils.e("badge 下载地址:" + httpUrl + ", 下载路径: " + path);
        boolean isSuccess = false;
        InputStream stream = null;
        try {
            if (httpUrl.startsWith("https")) {
                //如果是Https请求 则忽略素有证书
                HTTPSTrustManager.allowAllSSL();
            }
            URL url = new URL(httpUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(20000);
            conn.setRequestMethod("GET");

            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                stream = conn.getInputStream();
                isSuccess = IOUtils.writeFileFromIS(path, stream);
            }
            conn.disconnect();
        } catch (MalformedURLException e) {
            LogUtils.e("badge 下载错误:" + e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            LogUtils.e("badge 下载错误:" + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            LogUtils.e("badge 下载错误:" + e.getMessage());
            e.printStackTrace();
        } finally {
            IOUtils.closeIO(stream);
        }
        return isSuccess;
    }

    public String getBadgeGiftPath(Badge badge) {
        final String filePath = Constant.DEFAULT_DOWNLOAD_DIR + ".badge/";
        LogUtils.e("badge filePath : " + filePath);
        File file = new File(filePath);
        if (!file.exists()) {
            LogUtils.e("!file.exists() 1");
            file.mkdirs();
        }
        final String saveName = filePath + badge.getBid() + ".png";
        File nameFile = new File(saveName);
        if (nameFile.exists()) {
            nameFile.delete();
        }
        return file.getAbsolutePath();
    }


    public interface OnImgDownCompleteListener {
        void onComplete(int adapterPos);

    }

    OnImgDownCompleteListener onImgDownCompleteListener;

    public void setOnImgDownCompleteListener(OnImgDownCompleteListener onImgDownCompleteListener) {
        this.onImgDownCompleteListener = onImgDownCompleteListener;
    }
}
