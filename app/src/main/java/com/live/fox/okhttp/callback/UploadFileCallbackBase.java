package com.live.fox.okhttp.callback;

/**
 * 文件上传回调类
 * User: ljx
 * Date: 2017/12/2
 * Time: 18:52
 */
public abstract class UploadFileCallbackBase<T> extends BaseJsonCallback<T> implements ProgressCallback {

    @Override
    public void onProgress(int progress, long currentSize, long totalSize) {

    }
}
