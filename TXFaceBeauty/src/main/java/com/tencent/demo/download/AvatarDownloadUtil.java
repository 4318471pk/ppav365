package com.tencent.demo.download;

import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import com.tencent.demo.XMagicImpl;
import com.tencent.demo.utils.OnDownloadListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.CopyOnWriteArrayList;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 用于下载avatar素材
 */
public class AvatarDownloadUtil {
    static final String TAG = AvatarDownloadUtil.class.getName();
    static List<String> downloadingUrls = new CopyOnWriteArrayList<>();

    public static boolean ENABLE_RESUME_FROM_BREAKPOINT = true;//下载服务器是否支持断点续传


    public static void download(String zipUrl, String fileName, OnDownloadListener downloadListener) {

        if (downloadingUrls.contains(zipUrl)) {
            if (downloadListener != null) {
                downloadListener.onDownloadFailed(DownloadErrorCode.DOWNLOADING);
            }
            return;
        }

        if (TextUtils.isEmpty(fileName)) {
            return;
        }
        if (!fileName.toLowerCase(Locale.getDefault()).endsWith(".zip")) {
            fileName = fileName + ".zip";
        }
        String downloadDir = XMagicImpl.applicationContext.getFilesDir()+File.separator+"avatar_download" + File.separator;
        File dirFile = new File(downloadDir);
        if (!dirFile.exists()) {
            dirFile.mkdirs();
        }
        downloadingUrls.add(zipUrl);
        Handler handler = new Handler(Looper.getMainLooper());
        OnDownloadListener avatarOnDownloadListener = new OnDownloadListener() {
            @Override
            public void onDownloadSuccess(String directory) {
                downloadingUrls.remove(zipUrl);
                if (downloadListener != null) {
                    handler.post(() -> downloadListener.onDownloadSuccess(directory));
                }
            }

            @Override
            public void onDownloading(int progress) {
                if (downloadListener != null) {
                    handler.post(() -> downloadListener.onDownloading(progress));
                }
            }

            @Override
            public void onDownloadFailed(int errorCode) {
                downloadingUrls.remove(zipUrl);
                if (downloadListener != null) {
                    handler.post(() -> downloadListener.onDownloadFailed(errorCode));
                }
            }
        };
        if (ENABLE_RESUME_FROM_BREAKPOINT) {
            try {
                downloadWithResumeBreakPoint(zipUrl, downloadDir, fileName, avatarOnDownloadListener);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                avatarOnDownloadListener.onDownloadFailed(DownloadErrorCode.FILE_IO_ERROR);
            }
        } else {
            downloadWithoutResumeBreakPoint(zipUrl, downloadDir, fileName, avatarOnDownloadListener);
        }
    }

    private static void downloadWithoutResumeBreakPoint(final String url, final String directory, final String fileName, final OnDownloadListener listener) {
        Request request = new Request.Builder().url(url).build();
        new OkHttpClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "enqueue onFailure: e=" + e.toString());
                listener.onDownloadFailed(DownloadErrorCode.NETWORK_ERROR);
            }

            @Override
            public void onResponse(Call call, Response response) {
                if (response == null || response.body() == null || response.body().byteStream() == null) {
                    Log.e(TAG, "onResponse: null or body null");
                    listener.onDownloadFailed(DownloadErrorCode.NETWORK_ERROR);
                    return;
                }
                InputStream is = null;
                FileOutputStream fos = null;
                try {
                    readData(response, is, fos);
                } catch (Exception e) {
                    Log.e(TAG, "onResponse: e=" + e.toString());
                    listener.onDownloadFailed(DownloadErrorCode.NETWORK_FILE_ERROR);
                } finally {
                    try {
                        if (is != null) {
                            is.close();
                        }
                    } catch (IOException e) {
                        Log.e(TAG, "onResponse: finally close is,e=" + e.toString());
                    }
                    try {
                        if (fos != null) {
                            fos.close();
                        }
                    } catch (IOException e) {
                        Log.e(TAG, "onResponse: finally close fos,e=" + e.toString());
                    }
                }
            }

            private void readData(Response response, InputStream is, FileOutputStream fos) throws IOException {
                byte[] buf = new byte[2048];
                is = response.body().byteStream();
                long total = response.body().contentLength();
                Log.d(TAG, "onResponse: response.body().contentLength() = " + total);
                if (total <= 0) {
                    listener.onDownloadFailed(DownloadErrorCode.NETWORK_ERROR);
                    return;
                }
                fos = new FileOutputStream(directory + File.separator + fileName);
                long sum = 0;
                int len;
                while ((len = is.read(buf)) != -1) {
                    fos.write(buf, 0, len);
                    sum += len;
                    int progress = (int) (sum * 1.0f / total * 100);
                    if (progress < 0) {
                        progress = 0;
                    }
                    if (progress > 100) {
                        progress = 100;
                    }
                    listener.onDownloading(progress);
                }
                fos.flush();
                Log.d(TAG, "onResponse: onDownloadSuccess");
                listener.onDownloadSuccess(directory);
            }
        });
    }

    private static void downloadWithResumeBreakPoint(final String url, final String directory, final String fileName, final OnDownloadListener listener) throws FileNotFoundException {
        File file = new File(directory, fileName);
        RandomAccessFile accessFile = new RandomAccessFile(file, "rw");
        // 断点续传：重新开始下载的位置：file.length()
        final long existFileLength = file.exists() ? file.length() : 0;
        Log.d(TAG, "download: file.length=" + existFileLength);
        String range = String.format(Locale.getDefault(), "bytes=%d-", existFileLength);

        Request request = new Request.Builder()
                .url(url)
                .header("range", range)
                .build();
        new OkHttpClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "enqueue onFailure: e=" + e.toString());
                listener.onDownloadFailed(DownloadErrorCode.NETWORK_ERROR);
            }

            @Override
            public void onResponse(Call call, Response response) {
                if (response == null || response.body() == null || response.body().byteStream() == null) {
                    Log.e(TAG, "onResponse: null or body null");
                    listener.onDownloadFailed(DownloadErrorCode.NETWORK_ERROR);
                    return;
                }
                InputStream is = null;
                try {
                    readData(accessFile, existFileLength, response, is);
                } catch (Exception e) {
                    Log.e(TAG, "onResponse: e=" + e.toString());
                    listener.onDownloadFailed(DownloadErrorCode.NETWORK_FILE_ERROR);
                } finally {
                    try {
                        if (is != null) {
                            is.close();
                        }
                    } catch (IOException e) {
                        Log.e(TAG, "onResponse: finally close is,e=" + e.toString());
                    }
                }
            }

            private void readData(RandomAccessFile accessFile, long existFileLength, Response response, InputStream is) throws IOException {
                byte[] buf = new byte[2048];
                is = response.body().byteStream();
                long total = response.body().contentLength();
                Log.d(TAG, "onResponse: response.body().contentLength() = " + total);
                if (total <= 0) {
                    listener.onDownloadFailed(DownloadErrorCode.NETWORK_ERROR);
                    return;
                }
                accessFile.seek(existFileLength);
                long sum = existFileLength;
                int len;
                total += existFileLength;
                while ((len = is.read(buf)) != -1) {
                    accessFile.write(buf, 0, len);
                    sum += len;
                    int progress = (int) (sum * 1.0f / total * 100);
                    if (progress < 0) {
                        progress = 0;
                    }
                    if (progress > 100) {
                        progress = 100;
                    }
                    listener.onDownloading(progress);
                }
                Log.d(TAG, "onResponse: onDownloadSuccess");
                listener.onDownloadSuccess(directory);
            }
        });
    }

}
