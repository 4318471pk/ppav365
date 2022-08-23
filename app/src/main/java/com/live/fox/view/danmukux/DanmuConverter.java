package com.live.fox.view.danmukux;

import android.view.View;


public abstract class DanmuConverter<M> {
    public DanmuConverter() {
    }

    public abstract int getSingleLineHeight();

    public abstract View convert(M var1);
}
