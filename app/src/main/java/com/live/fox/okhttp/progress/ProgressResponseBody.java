package com.live.fox.okhttp.progress;

import com.live.fox.okhttp.callback.BaseCallback;
import com.live.fox.okhttp.callback.ProgressCallback;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;
import okio.Source;

/**
 * 包装的响体，处理进度
 * Created by way on 15/9/16.
 */
public class ProgressResponseBody extends ResponseBody {
    //实际的待包装响应体
    private final ResponseBody responseBody;
    //进度回调接口
    private volatile ProgressCallback callback;
    //包装完成的BufferedSource
    private BufferedSource bufferedSource;

    /**
     * 构造函数，赋值
     *
     * @param responseBody 待包装的响应体
     * @param callback     回调接口
     */
    public ProgressResponseBody(ResponseBody responseBody, ProgressCallback callback) {
        this.responseBody = responseBody;
        this.callback = callback;
    }


    /**
     * 重写调用实际的响应体的contentType
     *
     * @return MediaType
     */
    @Override
    public MediaType contentType() {
        return responseBody.contentType();
    }

    /**
     * 重写调用实际的响应体的contentLength
     *
     * @return contentLength
     */
    @Override
    public long contentLength() {
        return responseBody.contentLength();
    }

    /**
     * 重写进行包装source
     *
     * @return BufferedSource
     */
    @Override
    public BufferedSource source() {
        if (bufferedSource == null) {
            //包装
            bufferedSource = Okio.buffer(source(responseBody.source()));
        }
        return bufferedSource;
    }

    /**
     * 读取，回调进度接口
     *
     * @param source Source
     * @return Source
     */
    private Source source(Source source) {

        return new ForwardingSource(source) {
            //当前读取字节数
            long totalBytesRead = 0L;
            int lastProgress; //上次进度

            @Override
            public long read(Buffer sink, long byteCount) throws IOException {
                long bytesRead = super.read(sink, byteCount);
                //增加当前读取的字节数，如果读取完成了bytesRead会返回-1
                totalBytesRead += bytesRead != -1 ? bytesRead : 0;
                final long fileSize = responseBody.contentLength();
                //回调，如果contentLength()不知道长度，会返回-1
//				LogUtils.e("fileSize=="+fileSize);
                if (0 != fileSize) {
                    final int progress = (int) ((totalBytesRead * 100) / fileSize);
                    if (progress > lastProgress) {
                        lastProgress = progress;
                        updateProgress(lastProgress, totalBytesRead, fileSize);
                    }
                }
                return bytesRead;
            }
        };
    }

    private void updateProgress(final int progress, final long currentSize, final long totalSize) {
        if (callback == null) return;
        BaseCallback.mHandler.post(new Runnable() {
            @Override
            public void run() {
                callback.onProgress(progress, currentSize, totalSize);
            }
        });
    }
}