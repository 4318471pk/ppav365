package com.live.fox.okhttp.callback;

import com.live.fox.utils.IOUtils;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public abstract class ResourceDownloadCallback<T> implements Callback {

    T t;
    String localPath;

    public ResourceDownloadCallback(T t,String localPath) {
        this.t = t;
        this.localPath=localPath;
    }

    @Override
    public void onFailure(@NotNull Call call, @NotNull IOException e) {

    }

    @Override
    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
            boolean isDownloadSuccess=false;
            if(response.isSuccessful() && response.body()!=null)
            {
                isDownloadSuccess= IOUtils.writeFileFromIS(localPath, response.body().byteStream());
            }

        onResponse(isDownloadSuccess,localPath,t);

    }

    public abstract void onResponse(boolean success, String filePath,T t);

}
