package com.tencent.demo.avatar.model;

import com.tencent.xmagic.avatar.AvatarData;

import java.util.List;
import java.util.Map;

public class AvatarItem {
    public String id;
    public String icon;
    //TYPE_SELECTOR 或者 TYPE_SLIDER
    public int type;
    //如果是selector类型，则它表示当前有无被选中
    public boolean selected = false;
    public String downloadUrl;
    public String category;
    public Map<String, String> labels = null;
    public List<BindData> bindData;
    public AvatarData avatarData;

}
