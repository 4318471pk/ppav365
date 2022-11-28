package com.tencent.demo.avatar.view;

import com.tencent.demo.avatar.model.AvatarItem;
import com.tencent.demo.avatar.model.MainTab;
import com.tencent.demo.avatar.model.SubTab;


public interface AvatarPanelCallBack {
    void onItemChecked(MainTab mainTab, AvatarItem avatarItem);

    void onItemValueChange(AvatarItem avatarItem);

    boolean onShowPage(AvatarPageInf avatarPageInf, SubTab subTab);
}
