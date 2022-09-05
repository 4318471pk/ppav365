package com.live.fox.network;

import io.reactivex.Observer;

/**
 * 网络请求基础
 * 观察者
 *
 * @param <T> 后台返回的实体
 */
public abstract class NetworkObserver<T> implements Observer<NetworkBaseEntity<T>> {
}
