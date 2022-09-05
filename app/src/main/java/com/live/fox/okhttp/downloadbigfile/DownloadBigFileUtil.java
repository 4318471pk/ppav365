package com.live.fox.okhttp.downloadbigfile;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;

import androidx.core.content.FileProvider;

import com.live.fox.utils.LogUtils;
import com.live.fox.utils.StringUtils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.security.GeneralSecurityException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.X509TrustManager;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


/**
 * 功能：实现断点续传下载的功能 可用于下载大文件 下载超过40M的APK
 * <p>
 * <p>
 * 同时还需要 DownloadListener.java和DownloadResponseBody.java
 * 使用示例
 * public void download(View v){
 * //        downloadAPK("http://dev.static.goddess021.com/release.apk");
 * DownloadBigFileUtil downloadApkUtil = new DownloadBigFileUtil(this);
 * downloadApkUtil.downloadAPK("http://dev.static.goddess021.com/release.apk");
 * downloadApkUtil.setOnProgressListener(new DownloadBigFileUtil.OnProgressListener() {
 *
 * @Override public void onProgress(long fileSoFar1, long fileTotleSize1) {
 * //                progress = (int) ((fileSoFar/(float)fileTotleSize)*100);
 * // 更新进度
 * //                mHandler.sendEmptyMessage(DOWN_UPDATE);
 * fileSoFar = fileSoFar1;
 * fileTotleSize = fileTotleSize1;
 * <p>
 * }
 * });
 * mHandler.sendEmptyMessage(100);
 * }
 * <p>
 * private Handler mHandler = new Handler() {
 * public void handleMessage(Message msg) {
 * if(fileTotleSize==0){
 * tv.setText("0/0 0.00%");
 * }else {
 * tv.setText("progress:    "+format1(((float)fileSoFar/(float)fileTotleSize)*100)+"%("
 * +format2((float)fileSoFar/1024/1024)+"M" +
 * "/"+format2((float)fileTotleSize/1024/1024)+"M)");
 * }
 * <p>
 * if(fileTotleSize>fileSoFar || fileSoFar+fileTotleSize==0){
 * mHandler.sendEmptyMessageDelayed(100, 200);
 * }
 * }
 * };
 */
public class DownloadBigFileUtil {

    Context context;
    String downloadUrl;
    String apkName;
    long downloadLength;
    Call mCall;
    DownloadListener downloadListener;
    long fileMaxLength;
    private OnProgressListener onProgressListener;


    public DownloadBigFileUtil(Context context) {
        this.context = context;
    }


    public void downloadAPK(String url) {
        this.downloadUrl = url;
        apkName = url.substring(url.lastIndexOf("/") + 1);
        LogUtils.e(downloadUrl + " Apk名字：" + apkName);

        //先移除老的APK
        removeOldApk(apkName);

        //下载监听
        downloadListener = new DownloadListener() {
            @Override
            public void start(long max) {
                LogUtils.e("开始下载监听 :" + max);
            }

            @Override
            public void loading(int progress) {
                onProgressListener.onProgress(progress, fileMaxLength);
            }

            // 说明文件已经下载完，直接跳转安装就好
            @Override
            public void complete(String path) {
                LogUtils.e("下载完成 path:" + path);
                installApk(path);
                if (!mCall.isCanceled()) {
                    mCall.cancel();
                }
                context = null;
            }

            @Override
            public void fail(int code, String message) {
                LogUtils.e("失败 message:" + message);
            }

            @Override
            public void loadFail(String message) {
                LogUtils.e("下载失败 message:" + message);
            }
        };

        //开始下载APK
        download();
    }


    public void download() {
        LogUtils.e("下载开始");
        final long startsPoint = getFileStart() > 0 ? getFileStart() - 1 : getFileStart();
        LogUtils.e("开始下载的位置->" + startsPoint);
        downloadLength = startsPoint;

        if (StringUtils.isValidUrl(downloadUrl)) {
            mCall = download(downloadUrl, downloadListener, startsPoint, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    LogUtils.e("onFailure->" + e.getMessage());
                    downloadListener.loadFail(e.getMessage());
                    mCall.cancel();
                    download();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    long length = response.body().contentLength();
                    LogUtils.e("本次下载文件长度：" + length);
                    if (fileMaxLength == 0) {
                        fileMaxLength = length;
                    }
                    LogUtils.e("文件总长度->" + fileMaxLength);
                    if (length == 0) {
                        // 说明文件已经下载完，直接跳转安装就好
                        downloadListener.complete(String.valueOf(getApkFile(apkName).getAbsoluteFile()));
                        return;
                    }
                    downloadListener.start(length + startsPoint);
                    // 保存文件到本地
                    InputStream is = null;
                    RandomAccessFile randomAccessFile = null;
                    BufferedInputStream bis = null;

                    byte[] buff = new byte[2048];
                    int len = 0;
                    try {
                        is = response.body().byteStream();
                        bis = new BufferedInputStream(is);

                        File file = getApkFile(apkName);
                        // 随机访问文件，可以指定断点续传的起始位置
                        randomAccessFile = new RandomAccessFile(file, "rwd");
                        randomAccessFile.seek(startsPoint);
                        while ((len = bis.read(buff)) != -1) {
                            randomAccessFile.write(buff, 0, len);
                            downloadLength += len;
                        }
                        // 下载完成
                        downloadListener.complete(String.valueOf(file.getAbsoluteFile()));
                    } catch (Exception e) {
                        LogUtils.e("下载异常: " + e.getMessage());
                        downloadListener.loadFail(e.getMessage());
                        mCall.cancel();

                        download();
                    } finally {
                        try {
                            if (is != null) {
                                is.close();
                            }
                            if (bis != null) {
                                bis.close();
                            }
                            if (randomAccessFile != null) {
                                randomAccessFile.close();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        }
    }

    public Call download(String url, final DownloadListener downloadListener, final long startsPoint, Callback callback) {

        Request request = new Request.Builder()
                .url(url)
                .header("RANGE", "bytes=" + startsPoint + "-")//断点续传
                .build();

        // 重写ResponseBody监听请求
        Interceptor interceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Response originalResponse = chain.proceed(chain.request());
                return originalResponse.newBuilder()
                        .body(new DownloadResponseBody(originalResponse, startsPoint, downloadListener))
                        .build();
            }
        };

        OkHttpClient.Builder dlOkhttp = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)//设置连接超时时间
                .readTimeout(10, TimeUnit.SECONDS)//设置读取超时时间
                .addNetworkInterceptor(interceptor);
        // 绕开证书
        try {
            setSSL(dlOkhttp);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 发起请求
        Call call = dlOkhttp.build().newCall(request);
        call.enqueue(callback);
        return call;
    }

    public void setSSL(OkHttpClient.Builder dlOkhttp) {
        X509TrustManager trustManager = new X509TrustManager() {
            @Override
            public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {

            }

            @Override
            public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {

            }

            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[0];
            }
        };

        SSLSocketFactory sslSocketFactory;
        try {
            SSLContext sslContext;
            sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, new X509TrustManager[]{trustManager}, null);
            sslSocketFactory = sslContext.getSocketFactory();
        } catch (GeneralSecurityException e) {
            throw new RuntimeException(e);
        }


        HostnameVerifier DO_NOT_VERIFY = new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        };

        dlOkhttp.sslSocketFactory(sslSocketFactory, trustManager);

    }


    private File getApkFile(String apkName) {
        String root = Environment.getExternalStorageDirectory().getPath();
        File file = new File(root, apkName);
        return file;
    }


    private long getFileStart() {
        String root = Environment.getExternalStorageDirectory().getPath();
        File file = new File(root, apkName);
        if (file == null) {
            return 0;
        }
        return file.length();
    }


    public void removeOldApk(String apkName) {
        File file = getApkFile(apkName);
        if (file != null) {
            file.delete();
        }
    }

    private void installApk(String path) {
        File file = new File(path);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        //判断是否是AndroidN以及更高的版本
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Uri contentUri = FileProvider.getUriForFile(context, context.getPackageName() + ".fileprovider", file);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
        } else {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        }
        context.startActivity(intent);

    }


    public interface OnProgressListener {
        /**
         * 下载进度
         *
         * @param fileSoFar     已下载大小
         * @param fileTotleSize 总大小
         */
        void onProgress(long fileSoFar, long fileTotleSize);

    }

    /**
     * 对外开发的方法
     *
     * @param onProgressListener
     */
    public void setOnProgressListener(OnProgressListener onProgressListener) {
        this.onProgressListener = onProgressListener;
    }

}
