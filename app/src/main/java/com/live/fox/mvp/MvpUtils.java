package com.live.fox.mvp;

import com.live.fox.base.AbsBasePresenter;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Created   on 2017/9/5.
 */

public final class MvpUtils {

    public static <P extends AbsBasePresenter> P instantiatePresenter(Object obj) {
        PresenterInject pi = obj.getClass().getAnnotation(PresenterInject.class);
        if (pi == null) {
            return null;
        }
        try {
            return (P) pi.value().newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <M> M instantiateModel(Object obj, int i) {
        if(obj == null) {
            return null;
        }
        Type type = obj.getClass().getGenericSuperclass();
        if (type instanceof ParameterizedType) {
            Type[] types = ((ParameterizedType) type).getActualTypeArguments();
            if (types != null && types.length > i) {
                try {
                    return ((Class<M>) types[i]).newInstance();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                    return null;
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                    return null;
                }
            }
        }
        return null;
    }
}
