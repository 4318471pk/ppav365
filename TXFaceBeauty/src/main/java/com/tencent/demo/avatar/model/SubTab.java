package com.tencent.demo.avatar.model;


import com.tencent.xmagic.avatar.AvatarData;

import java.util.List;

public class SubTab {
    public String label;
    public String category;
    public String relatedCategory;
    public int type = AvatarData.TYPE_SELECTOR;
    public List<AvatarItem> items;

}
