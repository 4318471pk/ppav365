package com.live.fox.manager;

public class DownloadRunnable<T> implements Runnable {

    T t;

    public DownloadRunnable(T t) {
        this.t = t;
    }

    @Override
    public void run() {
        run(t);
    }

    public void run(T t)
    {

    }
}
