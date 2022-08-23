package com.live.fox.utils;//package com.lc.base.util;
//
//import android.util.Log;
//
//import com.qiniu.android.http.ResponseInfo;
//import com.qiniu.android.storage.UpCancellationSignal;
//import com.qiniu.android.storage.UpCompletionHandler;
//import com.qiniu.android.storage.UpProgressHandler;
//import com.qiniu.android.storage.UploadManager;
//import com.qiniu.android.storage.UploadOptions;
//
//import org.json.JSONObject;
//
///**
// * 用于进行七牛云上传操作的管理类。
// *
// * @author guolin
// * @since 17/3/5
// */
//public final class QiniuManager {
//    private UploadManager uploadManager = new UploadManager();
//    private boolean isCanceled = false;
//
//    /**
//     * @param filePath  文件路径
//     * @param key       指定七牛服务上的文件名，或 null
//     * @param uptoken   从服务端SDK获取
//     */
//    public final void upload(String filePath, String key, String uptoken, final QiniuManager.UploadListener listener) {
//        uploadManager.put(filePath, key, uptoken, new UpCompletionHandler() {
//                    @Override
//                    public void complete(String key, ResponseInfo info, JSONObject response) {
//                        if (listener != null && info != null) {
//                            if (info.isOK()) {
//                                listener.onSuccess(key);
//                            } else {
//                                listener.onFailure(info);
//                            }
//                        }
//                    }
//                }, new UploadOptions(null, null, false, new UpProgressHandler() {
//                    public void progress(String key, double percent) {
//                        Log.i("qiniu", key + ": " + percent);
//                        listener.onProgress(percent);
//                    }
//                }, new UpCancellationSignal() {
//                    @Override
//                    public boolean isCancelled() {
//                        return isCanceled;
//                    }
//                })
//        );
//    }
//
//    public void cancel() {
//        isCanceled = true;
//    }
//
//
//    public interface UploadListener {
//        void onSuccess(String key);
//
//        void onFailure(ResponseInfo info);
//
//        void onProgress(double percent);
//    }
//}