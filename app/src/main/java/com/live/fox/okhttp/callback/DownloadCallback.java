package com.live.fox.okhttp.callback;


import com.live.fox.utils.IOUtils;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.ResponseBody;

/**
 * 文件下载回调类
 * User: ljx
 * Date: 2017/12/1
 * Time: 20:04
 */
public abstract class DownloadCallback extends FailureCallback implements ProgressCallback {

    private final boolean callbackInUIThread;//回调是否在主线程

    private final String localFilePath;

    boolean isCancel = false;

    public DownloadCallback(String localFilePath) {
        this(localFilePath, true);
    }

    public DownloadCallback(String localFilePath, boolean callbackInUIThread) {
        this.localFilePath = localFilePath;
        this.callbackInUIThread = callbackInUIThread;
    }

    /**
     * 此取消不是正常的取消 只是最后返回时，不再会回调而已
     */
    public void cancel(){
        this.isCancel = true;
    }

    @Override
    public final void onResponse(Call call, okhttp3.Response response) {
        try {
            if (!response.isSuccessful())
                throw new IOException(String.valueOf(response.code()));
            ResponseBody body = response.body();
            if (body == null)
                throw new IOException("download empty");
            boolean isSuccess = IOUtils.writeFileFromIS(localFilePath, body.byteStream());
            if(isCancel) return;
            if (!isSuccess)
                throw new IOException("download failure");
            if (!callbackInUIThread) {
                onResponse(true, localFilePath);
                return;
            }
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    onResponse(true, localFilePath);
                }
            });
        } catch (IOException e) {
            onFailure(call, e);
        }
    }

    @Override
    public void onFailure(String error) {
        onResponse(false, localFilePath);
    }

    public abstract void onResponse(boolean success, String filePath);


    @Override
    public void onProgress(int progress, long currentSize, long totalSize) {
    }
}
