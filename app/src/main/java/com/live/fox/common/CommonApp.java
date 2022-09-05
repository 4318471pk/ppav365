package com.live.fox.common;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.telephony.PhoneStateListener;
import android.text.TextUtils;
import android.view.Gravity;

import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.google.firebase.FirebaseApp;
import com.live.fox.AppConfig;
import com.live.fox.BuildConfig;
import com.live.fox.Constant;
import com.live.fox.R;
import com.live.fox.language.MultiLanguageUtils;
import com.live.fox.manager.NotificationManager;
import com.live.fox.utils.GlideUtils;
import com.live.fox.utils.LogUtils;
import com.live.fox.utils.ToastViewUtils;
import com.live.fox.utils.Utils;
import com.live.fox.utils.device.DeviceUtils;
import com.live.fox.utils.okgo.OkGoHttpUtil;
import com.live.fox.windowmanager.FFloatView;
import com.tencent.bugly.crashreport.CrashReport;
import com.wanjian.cockroach.Cockroach;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.List;

import cn.net.shoot.sharetracesdk.ShareTrace;

/**
 * 项目下的app
 * 主播端口和用户端还有差异
 * 可以把相同的放着这里
 */
public class CommonApp extends MultiDexApplication {
    public static WeakReference<Activity> topActivity;  //记录点击通知的时候活跃的 Activity
    public static boolean isNotificationClicked;   //点击了通知栏

    private static CommonApp sInstance;
    public static boolean isFloatWindowClick = false;

    public static CommonApp getInstance() {
        return sInstance;
    }

    public static long S_COUNTDOWNa = 0;
    public static String LOTERNAME = "";
    public FFloatView mFloatView;
    public static PhoneStateListener mPhoneListener = null;

    @Override
    public void onCreate() {
        super.onCreate();
        Utils.init(this); //工具类初始化
        sInstance = this;

        AppConfig.setAppLanguage(this);

        GlideUtils.defaultPlaceImg = R.drawable.img_default;
        GlideUtils.defaultErrorImg = R.drawable.img_default;

        NotificationManager.getInstance().init(this);

        //初始化OkGoHttp
        OkGoHttpUtil.getInstance().init(this, Constant.isShowLog);
        FirebaseApp.initializeApp(this);
        initToast();  //初始化日志
        if (Constant.isCarsh) initCrash();   //异常捕获初始化
        LogUtils.getConfig().setLogSwitch(Constant.isShowLog); //LogUtils日志是否打印到控制台

        String shareTraceKey;
        String shareDomain;
        switch (BuildConfig.AppFlavor) {
            case "MMLive":  //MM_live
            default:
                shareTraceKey = "784b6957e877d45a";
                shareDomain = "https://www.ggc5tc66.com";
                Constant.aboutAppUrl = "http://mmlive.com";
                break;

            case "QQLive": //QQ_live
                shareTraceKey = "9a7b79a5969d5c2b";
                shareDomain = "https://www.ggc5tc66.com";
                Constant.aboutAppUrl = "http://QQLive.com";
                break;

            case "AiAi":  //AiAi_live
                Constant.aboutAppUrl = "https://aiai-live.com";
                shareDomain = "https://www.aiaisharetrace.com";
                shareTraceKey = "8a9d89095f7c043f";
                break;

            case "Live24":  //24_live
                Constant.aboutAppUrl = "http://www.24live8.com";
                shareDomain = "https://www.aiaisharetrace.com";
                shareTraceKey = "7850587a6e67f48a";
                break;

            case "ThiLive":  //Thi_live
                Constant.aboutAppUrl = "https://www.thlive-stream1.com";
                shareDomain = "https://www.ggc5tc66.com";
                shareTraceKey = "67ca51e2455d695f";
                break;
        }

//        ShareTrace.init(this, shareTraceKey);
//        ShareTrace.setServerDomain(shareDomain);
        GlideUtils.defaultErrorImg = R.drawable.img_default;
        GlideUtils.defaultPlaceImg = R.drawable.img_default;

        Fresco.initialize(this);
        initBugly();
    }

    public FFloatView getFloatView() {
        if (mFloatView == null)
            mFloatView = new FFloatView(sInstance);
        return mFloatView;
    }

    /**
     * 应用程序是否运行在前台
     */
    public boolean isAppRunningForeground() {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);

        String packageName = getApplicationContext().getPackageName();

        //系统中当前正在运行的程序
        List<ActivityManager.RunningAppProcessInfo> appProcesses = manager.getRunningAppProcesses();
        if (appProcesses == null) {
            return false;
        }

        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.processName.equals(packageName)
                    && appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                return true;
            }
        }
        return false;
    }

    /**
     * 初始化Toast样式
     */
    public void initToast() {
        ToastViewUtils.setBgResource(R.drawable.shape_corners_20_tran);
        ToastViewUtils.setGravity(Gravity.BOTTOM, 0, DeviceUtils.dp2px(this, 80));
        ToastViewUtils.setMsgColor(Color.WHITE);
    }

    public void initCrash() {
        // handlerException内部建议手动try{  你的异常处理逻辑  }catch(Throwable e){ } ，以防handlerException内部再次抛出异常，导致循环调用handlerException
        Cockroach.install((thread, throwable) -> {
            //开发时使用Cockroach可能不容易发现bug，所以建议开发阶段在handlerException中用Toast谈个提示框，
            //由于handlerException可能运行在非ui线程中，Toast又需要在主线程，所以new了一个new Handler(Looper.getMainLooper())，
            //所以千万不要在下面的run方法中执行耗时操作，因为run已经运行在了ui线程中。
            //new Handler(Looper.getMainLooper())只是为了能弹出个toast，并无其他用途
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    try {
                        //建议使用下面方式在控制台打印异常，这样就可以在Error级别看到红色log
                        LogUtils.e("AndroidRuntime", "--->CockroachException:" + thread + "<---", throwable);
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                }
            });
        });
    }


    /**
     * Bugly
     * 腾讯应用崩溃日志记录
     */
    private void initBugly() {
        Context context = getApplicationContext();
        // 获取当前包名
        String packageName = context.getPackageName();
        // 获取当前进程名
        String processName = getProcessName(android.os.Process.myPid());
        // 设置是否为上报进程
        CrashReport.UserStrategy strategy = new CrashReport.UserStrategy(context);
        // 腾讯建议：只在主进程下上报数据 是为了节省流量、内存等资源
        strategy.setUploadProcess(processName == null || processName.equals(packageName));

        //参数  appID： 注册时申请的APPID  参数三 调试模式开关 建议在测试阶段建议设置成true，发布时设置为false
        String buglyKey;
        if (BuildConfig.DEBUG) {
            buglyKey = "6987979";
        } else {
            switch (BuildConfig.AppFlavor) {
                default:
                case "MMLive":
                    buglyKey = "973789beb0";
                    break;
                case "QQLive":
                    buglyKey = "6918099ca8";
                    break;

                case "AiAi":
                    buglyKey = "953bdef8c8";
                    break;
            }
        }
        CrashReport.initCrashReport(context, buglyKey, Constant.isShowLog, strategy);
    }

    /**
     * 获取进程号对应的进程名
     *
     * @param pid 进程号
     * @return 进程名
     */
    private static String getProcessName(int pid) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader("/proc/" + pid + "/cmdline"));
            String processName = reader.readLine();
            if (!TextUtils.isEmpty(processName)) {
                processName = processName.trim();
            }
            return processName;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
        return null;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(MultiLanguageUtils.attachBaseContext(base));
        //解决方法数超过64K的问题 minSdkVersion21以下需要加以下方法
        MultiDex.install(this);
    }

    public boolean isAppOnForeground(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        boolean isOnForground = false;
        List<ActivityManager.RunningAppProcessInfo> runnings = am.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo running : runnings) {
            if (running.processName.equals(getPackageName())) {
                isOnForground = running.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND
                        || running.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_VISIBLE;
                break;
            }
        }
        return isOnForground;
    }
}
