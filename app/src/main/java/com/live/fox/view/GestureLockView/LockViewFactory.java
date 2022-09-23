package com.live.fox.view.GestureLockView;

public interface LockViewFactory {
    /**
     * 创建 LockView,必须是 newInstance 不能复用一个对象
     * @return
     */
    ILockView newLockView();
}
