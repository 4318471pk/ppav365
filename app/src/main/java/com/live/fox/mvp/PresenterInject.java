package com.live.fox.mvp;

import com.live.fox.base.AbsBasePresenter;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface PresenterInject {
    Class<? extends AbsBasePresenter> value();
}
