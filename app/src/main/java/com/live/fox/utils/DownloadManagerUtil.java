package com.live.fox.utils;

import android.app.DownloadManager;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;


import java.io.File;


/**
 * 作者：cheng
   (不建议使用)
 * 功能：后台下载, 并在通知栏显示下载进度，下载完安装新APK
 * eg:
 *  DownloadManagerUtil DownloadManagerUtil = new DownloadManagerUtil();
 *  DownloadManagerUtil.startDownload(this, url, "人鱼部落", "girls.apk");
 *  ToastUtils.show("已进入后台下载，可在通知栏查看下载进度");
 */

public class DownloadManagerUtil {

    long downloadId;
    private File apkFile;

    /**
     * 接受下载完成广播
     */
    private class DownLoadBroadcast extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(intent.getAction())) {
                AppUtils.installApk(context, apkFile);
                clearCurrentTask(context, downloadId);
                context.unregisterReceiver(this);
            }
        }
    }

    //开始下载
    public void startDownload(Context context, String url, String appName, String fileName){
        if (!checkDownloadManagerEnable(context)) {
            Toast.makeText(context, "请开启下载管理器", Toast.LENGTH_SHORT).show();
            return ;
        }

        if(downloadId>0){
            clearCurrentTask(context, downloadId);
        }


        //先移除老的APK
        removeOldApk(context, fileName);

        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        //设置允许使用的网络类型，这里是移动网络和wifi都可以
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI);
        //设置允许使用漫游
        request.setAllowedOverRoaming(true);

        //下载中和下载完后都显示通知栏
        request.setTitle(appName);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

        //设置下载文件的路径
        apkFile = new File(FileUtils.getExternalCacheDir(context, "update"), fileName);
        LogUtils.e(apkFile.getAbsolutePath());
        request.setDestinationUri(Uri.fromFile(apkFile));

        // 设置为可见和可管理
        request.setVisibleInDownloadsUi(true);

        //开始下载
        DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        downloadManager.enqueue(request);

        // 获取id 方便做在通知栏移除操作
        try {
            downloadId = downloadManager.enqueue(request);
        } catch (Exception e) {
            Log.e("download1", "e:"+e.getMessage());
            Toast.makeText(context, "找不到下载文件", Toast.LENGTH_SHORT).show();
            downloadId =  -1;
        }

        /**注册广播 监听任务完成*/
        IntentFilter intentFilter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        context.registerReceiver(new DownLoadBroadcast(), intentFilter);
    }




    public void removeOldApk(Context context, String fileName){
        File cacheDir = FileUtils.getExternalCacheDir(context, "update");
        cacheDir.mkdirs();
        File apk = new File(cacheDir, fileName);
        LogUtils.e(apk.getAbsolutePath());
        if (apk.exists() && apk.isFile()) {
            LogUtils.e("移除老的");
            apk.delete();
        }
    }


    /**
     * 可能会出错Cannot update URI: content://downloads/my_downloads/-1
     * 检查下载管理器是否被禁用
     */
    public boolean checkDownloadManagerEnable(Context context){
        try {
            int state = context.getPackageManager().getApplicationEnabledSetting("com.android.providers.downloads");
            if (state == PackageManager.COMPONENT_ENABLED_STATE_DISABLED
                    || state == PackageManager.COMPONENT_ENABLED_STATE_DISABLED_USER
                    || state == PackageManager.COMPONENT_ENABLED_STATE_DISABLED_UNTIL_USED) {
                String packageName = "com.android.providers.downloads";
                try {
                    Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    intent.setData(Uri.parse("package:"+packageName));
                    context.startActivity(intent);
                } catch (ActivityNotFoundException e) {
                    e.printStackTrace();
                    Intent intent = new Intent(android.provider.Settings.ACTION_MANAGE_APPLICATIONS_SETTINGS);
                    context.startActivity(intent);
                }
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }


    /**
     * 下载前先移除前一个任务，防止重复下载
     */
    public void clearCurrentTask(Context context, long downloadId) {
        DownloadManager dm = (DownloadManager)context.getSystemService(Context.DOWNLOAD_SERVICE);
        try {
            dm.remove(downloadId);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

//
//    public Handler downLoadHandler = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//
//            int[] bytesAndStatus = getBytesAndStatus(downloadId);
//            Log.e("download1", "已下载："+(int)((bytesAndStatus[0]/(float)bytesAndStatus[1])*100)+"，下载大小："+bytesAndStatus[0]+"，文件总大小:"+bytesAndStatus[1]);
//            fileTotleSize = bytesAndStatus[0];
//            if (onProgressListener != null) {
//                onProgressListener.onProgress(bytesAndStatus[0], bytesAndStatus[1]);
//            }
//
//            if(downLoadHandler !=null){
//                downLoadHandler.sendEmptyMessageDelayed(0, 600);
//            }
//
//        }
//    };
//
//
//    /**
//     * 通过query查询下载状态，包括已下载数据大小，总大小，下载状态
//     *
//     * @param downloadId
//     * @return
//     */
//    private int[] getBytesAndStatus(long downloadId) {
//        int[] bytesAndStatus = new int[]{
//                -1, -1, 0
//        };
//        DownloadManager.Query query = new DownloadManager.Query().setFilterById(downloadId);
//        Cursor cursor = null;
//        try {
//            cursor = downloadManager.query(query);
//            if (cursor != null && cursor.moveToFirst()) {
//                //已经下载文件大小
//                bytesAndStatus[0] = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
//                //下载文件的总大小
//                bytesAndStatus[1] = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));
//                //下载状态
//                bytesAndStatus[2] = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS));
//            }
//
//        } finally {
//            if (cursor != null) {
//                cursor.close();
//            }
//        }
//        return bytesAndStatus;
//    };




//    public String getFilePath(String url){
//        String downLoadPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Download/";
//        File dir = new File(downLoadPath);
//        if (!dir.exists()) {
//            dir.mkdir();
//        }
//        String fileName = url.substring(url.lastIndexOf("/") + 1, url.length());
//        if (fileName == null && TextUtils.isEmpty(fileName) && !fileName.contains(".apk")) {
//            fileName = mContext.getPackageName() + ".apk";
//        }
//        File file = new File(downLoadPath + fileName);
//        Log.e("download1", "文件是否存在 =" +file.exists());
//        return file.getAbsolutePath();
//    }

//    /**
//     * 安装apk
//     */
//    private void installApk() {
//        File file = new File(filePath);
//        Intent intent =new Intent(Intent.ACTION_VIEW);
//        //判断是否是AndroidN以及更高的版本
//        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.N) {
//            Log.e("download1",mContext.getPackageName()+".fileProvider");
//            Uri contentUri = FileProvider.getUriForFile(mContext,mContext.getPackageName()+".fileProvider",file);
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//            intent.setDataAndType(contentUri,"application/vnd.android.package-archive");
//        }else{
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            intent.setDataAndType(Uri.fromFile(file),"application/vnd.android.package-archive");
//        }
//        mContext.startActivity(intent);
//    }




//    /**
//     * 关闭定时器，线程 下载器
//     */
//    private void close(long downloadId) {
//        //清除下载任务
////        clearCurrentTask(downloadId);
//
//        if (downLoadHandler != null) {
//            downLoadHandler.removeMessages(0);
//            downLoadHandler = null;
//        }
//    }
//

//
//
//
//    public interface OnProgressListener {
//        /**
//         * 下载进度
//         * @param fileSoFar 已下载大小
//         * @param fileTotleSize 总大小
//         */
//        void onProgress(int fileSoFar, int fileTotleSize);
//
//    }
//
//    /**
//     * 对外开发的方法
//     *
//     * @param onProgressListener
//     */
//    public void setOnProgressListener(OnProgressListener onProgressListener) {
//        this.onProgressListener = onProgressListener;
//    }


}
