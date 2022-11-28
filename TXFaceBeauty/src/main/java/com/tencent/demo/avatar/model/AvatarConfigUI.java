package com.tencent.demo.avatar.model;

import com.tencent.xmagic.avatar.AvatarData;

import java.util.ArrayList;
import java.util.List;

public class AvatarConfigUI {
    public int uiType;
    public String groupLabel;
    public String itemName;
    public AvatarData avatarData;
    public List<AvatarData> bindAvatarDataList;

    public AvatarConfigUI(int uiType, String groupLabel, String itemName, AvatarData avatarData) {
        this.uiType = uiType;
        this.groupLabel = groupLabel;
        this.itemName = itemName;
        this.avatarData = avatarData;
    }


}
