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
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.live.fox.Constant;
import com.live.fox.R;
import com.live.fox.db.LocalGiftDao;
import com.live.fox.db.LocalMountResourceDao;
import com.live.fox.db.LocalSendGiftDao;
import com.live.fox.db.LocalUserGuardDao;
import com.live.fox.db.LocalUserLevelDao;
import com.live.fox.db.LocalUserTagResourceDao;
import com.live.fox.db.ResourceDataListener;
import com.live.fox.entity.GiftResourceBean;
import com.live.fox.entity.MountResourceBean;
import com.live.fox.entity.SendGiftResourceBean;
import com.live.fox.entity.UserGuardResourceBean;
import com.live.fox.entity.UserLevelResourceBean;
import com.live.fox.entity.UserTagResourceBean;
import com.live.fox.network.RetryInterceptor;
import com.live.fox.network.ssl.SSLSocketFactoryCompat;
import com.live.fox.okhttp.OkHttpUtil;
import com.live.fox.okhttp.callback.DownloadCallback;
import com.live.fox.okhttp.callback.ResourceDownloadCallback;
import com.live.fox.server.BaseApi;
import com.live.fox.svga.GiftManager;
import com.live.fox.utils.FileUtils;
import com.live.fox.utils.IOUtils;
import com.live.fox.utils.Strings;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ResourceDownloadService extends IntentService {

    private String DEFAULT_DOWNLOAD_DIR;
    Long timeOut = 3000l;//项目实际所需最大时间
    Long netWorkTimeOut = 3000l;//okhttp 的超时时间
    private final String TAG = "ResourceDownloadService";
    public final String CHANNEL_ID_LOCATION = "com.live.fox.manager.ResourceDownloadService";
    static int corePoolSize = Math.max(2, Math.min(Runtime.getRuntime().availableProcessors() - 1, 4));
    private ExecutorService executors = Executors.newFixedThreadPool(corePoolSize);
    private OkHttpClient mOkHttpClient;
    private String userLevelFolderPath;
    private String userTagFolderPath;
    private String mountFolderPath;
    private String giftFolderPath;
    private String guardFolderPath;


    public ResourceDownloadService() {
        super("ResourceDownloadService");
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

        if (mOkHttpClient == null) {
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

        requestResource();
        DEFAULT_DOWNLOAD_DIR=getExternalCacheDir().getAbsolutePath();
        Log.e("DEFAULT_DOWNLOAD_DIR",DEFAULT_DOWNLOAD_DIR);
        userLevelFolderPath=new StringBuilder().append(DEFAULT_DOWNLOAD_DIR).append(File.separator)
                .append("LevelIcon").append(File.separator).toString();
        userTagFolderPath=new StringBuilder().append(DEFAULT_DOWNLOAD_DIR).append(File.separator)
                .append("VIPLevelIcon").append(File.separator).toString();
        mountFolderPath=new StringBuilder().append(DEFAULT_DOWNLOAD_DIR).append(File.separator)
                .append("MountImg").append(File.separator).toString();
        giftFolderPath=new StringBuilder().append(DEFAULT_DOWNLOAD_DIR).append(File.separator)
                .append("GiftImg").append(File.separator).toString();
        guardFolderPath=new StringBuilder().append(DEFAULT_DOWNLOAD_DIR).append(File.separator)
                .append("GuardImg").append(File.separator).toString();

    }

    private void requestResource() {
        String url = BaseApi.getBaseServerDomain() + Constant.URL.BaseResource;
        Request request = new Request.Builder().get().url(url).build();
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.isSuccessful() && response.body() != null) {
                    String strData = response.body().string();
                    if (!TextUtils.isEmpty(strData)) {
                        try {
                            JSONObject dataJson = new JSONObject(strData);
                            int code = dataJson.optInt("code", 0);
                            if (code == 0) {
                                JSONObject jsonObject = dataJson.optJSONObject("data");
                                Gson gson = new Gson();
                                String levelUserList = jsonObject.optString("levelUserList", "");
                                String levelVipList = jsonObject.optString("levelVipList", "");
                                String levelGuardList = jsonObject.optString("levelGuardList", "");
                                String propList = jsonObject.optString("propList", "");
                                String mountList = jsonObject.optString("mountList", "");
                                String settingList = jsonObject.optString("settingList", "");

                                dataWatch();

                                Type userLevel = new TypeToken<List<UserLevelResourceBean>>() {
                                }.getType();
                                List<UserLevelResourceBean> userLevelResourceBeans = gson.fromJson(levelUserList, userLevel);
                                LocalUserLevelDao.getInstance().insertOrReplaceList(userLevelResourceBeans);

                                Type userTag = new TypeToken<List<UserTagResourceBean>>() {
                                }.getType();
                                List<UserTagResourceBean> userTagResourceBeans = gson.fromJson(levelVipList, userTag);
                                LocalUserTagResourceDao.getInstance().insertOrReplaceList(userTagResourceBeans);

                                Type userGuard = new TypeToken<List<UserGuardResourceBean>>() {
                                }.getType();
                                List<UserGuardResourceBean> userGuardResourceBeans = gson.fromJson(levelGuardList, userGuard);
                                LocalUserGuardDao.getInstance().insertOrReplaceList(userGuardResourceBeans);

                                Type gift = new TypeToken<List<GiftResourceBean>>() {
                                }.getType();
                                List<GiftResourceBean> giftResourceBeans = gson.fromJson(propList, gift);
                                LocalGiftDao.getInstance().insertOrReplaceList(giftResourceBeans);

                                Type mount = new TypeToken<List<MountResourceBean>>() {
                                }.getType();
                                List<MountResourceBean> mountResourceBeans = gson.fromJson(mountList, mount);
                                LocalMountResourceDao.getInstance().insertOrReplaceList(mountResourceBeans);

                                Type sendGift = new TypeToken<List<SendGiftResourceBean>>() {
                                }.getType();
                                List<SendGiftResourceBean> sendGiftResourceBeans = gson.fromJson(settingList, sendGift);
                                LocalSendGiftDao.getInstance().insertOrReplaceList(sendGiftResourceBeans);


                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }
            }
        });

    }

    private void dataWatch() {
        //下载等级图标
        LocalUserLevelDao.getInstance().setResourceDataListener(new ResourceDataListener() {
            @Override
            public void onDataInsertDone(boolean isAvailable) {
                if (isAvailable) {
                    List<UserLevelResourceBean> beans = LocalUserLevelDao.getInstance().queryList();
                    downloadUserLevelResource(beans);
                }
            }
        });

        //下载用户标签
        LocalUserTagResourceDao.getInstance().setResourceDataListener(new ResourceDataListener() {
            @Override
            public void onDataInsertDone(boolean isAvailable) {
                if (isAvailable) {
                    List<UserTagResourceBean> beans = LocalUserTagResourceDao.getInstance().queryList();
                    downloadUserTagResource(beans);
                }
            }
        });

        //下载座驾
        LocalMountResourceDao.getInstance().setResourceDataListener(new ResourceDataListener() {
            @Override
            public void onDataInsertDone(boolean isAvailable) {
                if (isAvailable) {
                    List<MountResourceBean> beans = LocalMountResourceDao.getInstance().queryList();
                    downloadMountResource(beans);
                }
            }
        });

        //下载礼物
        LocalGiftDao.getInstance().setResourceDataListener(new ResourceDataListener() {
            @Override
            public void onDataInsertDone(boolean isAvailable) {
                if (isAvailable) {
                    List<GiftResourceBean> beans = LocalGiftDao.getInstance().queryList();
                    downloadGift(beans);
                }
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
                if(isAvailable)
                {
                    List<UserGuardResourceBean> beans = LocalUserGuardDao.getInstance().queryList();
                    downloadGuardResource(beans);
                }
            }
        });
    }

    private Response getResponse(String downloadUrl) {

        String url=Strings.urlConnect(downloadUrl);
        Request request = new Request.Builder().get().url(url).build();
        Call call = mOkHttpClient.newCall(request);
        try {
            Response response = call.execute();
            return response;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getFileSuffix(String path) {
        int index = path.lastIndexOf(".");
        String imageType = path.substring(index, path.length());
        return imageType;
    }

    private void downloadUserLevelResource(List<UserLevelResourceBean> beans) {
        for (UserLevelResourceBean bean : beans) {
            if (bean.getLocalShouldUpdate() == 1) {
                executors.execute(new DownloadRunnable<UserLevelResourceBean>(bean) {
                    @Override
                    public void run(UserLevelResourceBean uBean) {
                        super.run(uBean);

                        StringBuilder sb = new StringBuilder();
                        sb.append(userLevelFolderPath);
                        sb.append("level").append(uBean.getId());
                        sb.append(getFileSuffix(uBean.getLevelImg()));

                        Response response = getResponse(uBean.getLevelImg());
                        if (response != null) {
                            if (response.isSuccessful() && response.body() != null) {
                                boolean isDownloadSuccess = IOUtils.writeFileFromIS(sb.toString(), response.body().byteStream());
                                if (isDownloadSuccess) {
                                    uBean.setLocalShouldUpdate(0);
                                    uBean.setLocalImgPath(sb.toString());
                                    LocalUserLevelDao.getInstance().updateData(uBean);
                                }
                            }
                            else
                            {
                                Log.e("UserLevelResource","下载失败"+uBean.getLevelImg());
                            }
                        }
                    }
                });
            }
        }
    }

    private void downloadUserTagResource(List<UserTagResourceBean> beans) {
        for (UserTagResourceBean bean : beans) {
            if (bean.getLocalShouldUpdate() == 1) {
                executors.execute(new DownloadRunnable<UserTagResourceBean>(bean) {
                    @Override
                    public void run(UserTagResourceBean uBean) {
                        super.run(uBean);

                        boolean isDownloadSuccess=false;

                        StringBuilder sb = new StringBuilder();
                        sb.append(userTagFolderPath);
                        sb.append("vipImg").append(uBean.getId());
                        sb.append(getFileSuffix(uBean.getVipImg()));

                        String vipImg=sb.toString();
                        Response response = getResponse(uBean.getVipImg());
                        if (response != null) {
                            if (response.isSuccessful() && response.body() != null) {
                                isDownloadSuccess = IOUtils.writeFileFromIS(sb.toString(), response.body().byteStream());
                            }
                        }

                        sb.delete(0,sb.length());
                        sb.append(userTagFolderPath);
                        sb.append("medalUrl").append(uBean.getId());
                        sb.append(getFileSuffix(uBean.getMedalUrl()));

                        String medalUrl=sb.toString();
                        Response responseMedal = getResponse(uBean.getMedalUrl());
                        if (responseMedal != null) {
                            if (responseMedal.isSuccessful() && responseMedal.body() != null) {
                                isDownloadSuccess = isDownloadSuccess && IOUtils.writeFileFromIS(sb.toString(), responseMedal.body().byteStream());
                            }
                        }


                        if (isDownloadSuccess) {
                            uBean.setLocalShouldUpdate(0);
                            uBean.setLocalVipImgPath(vipImg);
                            uBean.setLocalMedalUrlPath(medalUrl);
                            LocalUserTagResourceDao.getInstance().updateData(uBean);
                        }

                    }
                });
            }
        }
    }

    private void downloadMountResource(List<MountResourceBean> beans) {
        for (MountResourceBean bean : beans) {
            if (bean.getLocalShouldUpdate() == 1) {
                executors.execute(new DownloadRunnable<MountResourceBean>(bean) {
                    @Override
                    public void run(MountResourceBean uBean) {
                        super.run(uBean);

                        boolean isDownloadSuccess=false;

                        StringBuilder sb = new StringBuilder();
                        sb.append(mountFolderPath);
                        sb.append("logUrl").append(uBean.getId());
                        sb.append(getFileSuffix(uBean.getLogUrl()));

                        String logUrl=sb.toString();
                        Response response = getResponse(uBean.getLogUrl());
                        if (response != null) {
                            if (response.isSuccessful() && response.body() != null) {
                                isDownloadSuccess = IOUtils.writeFileFromIS(sb.toString(), response.body().byteStream());
                            }
                        }

                        sb.delete(0,sb.length());
                        sb.append(mountFolderPath);
                        sb.append("animationUrl").append(uBean.getId());
                        sb.append(getFileSuffix(uBean.getAnimationUrl()));

                        String animationUrl=sb.toString();
                        Response response2= getResponse(uBean.getAnimationUrl());
                        if (response2 != null) {
                            if (response2.isSuccessful() && response2.body() != null) {
                                isDownloadSuccess = isDownloadSuccess && IOUtils.writeFileFromIS(sb.toString(), response2.body().byteStream());
                            }
                        }

                        if (isDownloadSuccess) {
                            uBean.setLocalShouldUpdate(0);
                            uBean.setLocalImgPath(logUrl);
                            uBean.setLocalSvgPath(animationUrl);
                            LocalMountResourceDao.getInstance().updateData(uBean);
                        }

                    }
                });
            }
        }
    }

    private void downloadGift(List<GiftResourceBean> beans) {
        for (GiftResourceBean bean : beans) {
            if (bean.getLocalShouldUpdate() == 1) {
                executors.execute(new DownloadRunnable<GiftResourceBean>(bean) {
                    @Override
                    public void run(GiftResourceBean uBean) {
                        super.run(uBean);

                        boolean isDownloadSuccess=false;

                        StringBuilder sb = new StringBuilder();
                        sb.append(giftFolderPath);
                        sb.append("giftIcon").append(uBean.getId());
                        sb.append(getFileSuffix(uBean.getGitficon()));

                        String giftIcon=sb.toString();
                        Response response = getResponse(uBean.getGitficon());
                        if (response != null) {
                            if (response.isSuccessful() && response.body() != null) {
                                isDownloadSuccess = IOUtils.writeFileFromIS(sb.toString(), response.body().byteStream());
                            }
                        }

                        sb.delete(0,sb.length());
                        sb.append(giftFolderPath);
                        sb.append("SWF").append(uBean.getId());
                        sb.append(getFileSuffix(uBean.getSwf()));

                        String svg=sb.toString();
                        Response response2= getResponse(uBean.getSwf());
                        if (response2 != null) {
                            if (response2.isSuccessful() && response2.body() != null) {
                                isDownloadSuccess = isDownloadSuccess && IOUtils.writeFileFromIS(sb.toString(), response2.body().byteStream());
                            }
                        }

                        if (isDownloadSuccess) {
                            uBean.setLocalShouldUpdate(0);
                            uBean.setLocalImgPath(giftIcon);
                            uBean.setLocalSvgPath(svg);
                            LocalGiftDao.getInstance().updateData(uBean);
                        }

                    }
                });
            }
        }
    }

    private void downloadGuardResource(List<UserGuardResourceBean> beans) {
        for (UserGuardResourceBean bean : beans) {
            if (bean.getLocalShouldUpdate() == 1) {
                executors.execute(new DownloadRunnable<UserGuardResourceBean>(bean) {
                    @Override
                    public void run(UserGuardResourceBean uBean) {
                        super.run(uBean);

                        boolean isDownloadSuccess=false;

                        StringBuilder sb = new StringBuilder();
                        sb.append(guardFolderPath);
                        sb.append("small").append(uBean.getId());
                        sb.append(getFileSuffix(uBean.getImgUrl()));

                        String small=sb.toString();
                        Response response = getResponse(uBean.getMedalUrl());
                        if (response != null) {
                            if (response.isSuccessful() && response.body() != null) {
                                isDownloadSuccess = IOUtils.writeFileFromIS(sb.toString(), response.body().byteStream());
                            }
                        }

                        sb.delete(0,sb.length());
                        sb.append(guardFolderPath);
                        sb.append("medium").append(uBean.getId());
                        sb.append(getFileSuffix(uBean.getImgUrl()));

                        String medium=sb.toString();
                        Response response2= getResponse(uBean.getImgUrl());
                        if (response2 != null) {
                            if (response2.isSuccessful() && response2.body() != null) {
                                isDownloadSuccess = isDownloadSuccess && IOUtils.writeFileFromIS(sb.toString(), response2.body().byteStream());
                            }
                        }

                        if (isDownloadSuccess) {
                            uBean.setLocalShouldUpdate(0);
                            uBean.setLocalImgSmallPath(small);
                            uBean.setLocalImgMediumPath(medium);
                            LocalUserGuardDao.getInstance().updateData(uBean);
                        }

                    }
                });
            }
        }
    }

}
