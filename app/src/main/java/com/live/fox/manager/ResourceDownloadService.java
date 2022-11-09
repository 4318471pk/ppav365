package com.live.fox.manager;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import com.live.fox.Constant;
import com.live.fox.R;
import com.live.fox.db.LocalGiftDao;
import com.live.fox.db.LocalMountResourceDao;
import com.live.fox.db.LocalSendGiftDao;
import com.live.fox.db.LocalUserGuardDao;
import com.live.fox.db.LocalUserLevelDao;
import com.live.fox.db.LocalUserTagResourceDao;
import com.live.fox.db.ResourceDataListener;
import com.live.fox.entity.UserLevelResourceBean;
import com.live.fox.network.RetryInterceptor;
import com.live.fox.network.ssl.SSLSocketFactoryCompat;
import com.live.fox.okhttp.OkHttpUtil;
import com.live.fox.okhttp.callback.DownloadCallback;
import com.live.fox.svga.GiftManager;
import com.live.fox.utils.FileUtils;

import java.io.File;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;

public class ResourceDownloadService extends IntentService {

    Long timeOut = 3000l;//项目实际所需最大时间
    Long netWorkTimeOut = 3000l;//okhttp 的超时时间
    private  final String TAG = "ResourceDownloadService";
    public  final String CHANNEL_ID_LOCATION = "com.live.fox.manager.ResourceDownloadService";
    static int corePoolSize = Math.max(2, Math.min(Runtime.getRuntime().availableProcessors() - 1, 4));
    private  ExecutorService executors = Executors.newFixedThreadPool(corePoolSize);
    private  OkHttpClient mOkHttpClient;


    public ResourceDownloadService(String name) {
        super(name);
    }

    public static void startService(Context context) {
        Intent intent = new Intent(context, ResourceDownloadService.class);
        if (Build.VERSION.SDK_INT >= 26) {
            context.startForegroundService(intent);
        } else {
            context.startService(intent);
        }
    }

    public void initChannel() {
        if (Build.VERSION.SDK_INT >= 26) {
            // service的onCreate
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID_LOCATION, "System", android.app.NotificationManager.IMPORTANCE_LOW);
            android.app.NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            manager.createNotificationChannel(channel);
            Notification notification = new Notification.Builder(this, CHANNEL_ID_LOCATION)
                    .setSmallIcon(R.mipmap.ic_launcher)  // the status icon
                    .setWhen(System.currentTimeMillis())  // the time stamp
                    .setContentText("资源下载服务正在运行")  // the contents of the entry
                    .build();
            startForeground(2, notification);
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        initChannel();
        Log.e(TAG, "-----------------资源下载服务开启--------------");
        if (intent == null) {
            return;
        }

        if(mOkHttpClient==null)
        {
            //RetryInterceptor 3次失败重连
            mOkHttpClient = new OkHttpClient.Builder()
                    .connectTimeout(netWorkTimeOut, TimeUnit.MILLISECONDS)
                    .readTimeout(netWorkTimeOut, TimeUnit.MILLISECONDS)
                    .writeTimeout(netWorkTimeOut, TimeUnit.MILLISECONDS)
                    .retryOnConnectionFailure(false)
                    .followSslRedirects(false)
                    .followRedirects(false)
                    .addInterceptor(new RetryInterceptor(3))
//                    .addInterceptor(new TokenInterceptor(BoxApplication.getInstance().getClientConfig()))
//                    .sslSocketFactory(SSLUtil.createSSLSocketFactory(), new SSLUtil.TrustAllManager())
//                    .hostnameVerifier(new SSLUtil.TrustAllHostnameVerifier())
                    .sslSocketFactory(new SSLSocketFactoryCompat(SSLSocketFactoryCompat.trustAllCert), SSLSocketFactoryCompat.trustAllCert)
                    .build();
        }

        //下载等级图标
        LocalUserLevelDao.getInstance().setResourceDataListener(new ResourceDataListener() {
            @Override
            public void onDataInsertDone(boolean isAvailable) {
                if(isAvailable)
                {
                    List<UserLevelResourceBean> beans=LocalUserLevelDao.getInstance().queryList();
                    downloadUserLevelResource(beans);
                }
            }
        });

        LocalUserTagResourceDao.getInstance().setResourceDataListener(new ResourceDataListener() {
            @Override
            public void onDataInsertDone(boolean isAvailable) {

            }
        });

        LocalMountResourceDao.getInstance().setResourceDataListener(new ResourceDataListener() {
            @Override
            public void onDataInsertDone(boolean isAvailable) {

            }
        });

        LocalGiftDao.getInstance().setResourceDataListener(new ResourceDataListener() {
            @Override
            public void onDataInsertDone(boolean isAvailable) {

            }
        });

        LocalSendGiftDao.getInstance().setResourceDataListener(new ResourceDataListener() {
            @Override
            public void onDataInsertDone(boolean isAvailable) {

            }
        });

        LocalUserGuardDao.getInstance().setResourceDataListener(new ResourceDataListener() {
            @Override
            public void onDataInsertDone(boolean isAvailable) {

            }
        });

    }

    private void downloadUserLevelResource(List<UserLevelResourceBean> beans)
    {
        for (UserLevelResourceBean bean: beans) {
            executors.execute(new DownloadRunnable<UserLevelResourceBean>(bean){
                @Override
                public void run(UserLevelResourceBean uBean) {
                    super.run(uBean);

                    StringBuilder sb=new StringBuilder();
                    sb.append(Constant.DEFAULT_DOWNLOAD_DIR);
                    sb.append("LevelIcon").append(File.separator).append(uBean.getLevel()).append(".level");

                    Request request = new Request.Builder().get().url(sb.toString()).build();

                    OkHttpUtil.doGetByExecute(uBean.getLevelImg(), new DownloadCallback(sb.toString(), false) {
                        @Override
                        public void onResponse(boolean success, String filePath) {

                        }
                    });
                }
            });
        }


    }
}
