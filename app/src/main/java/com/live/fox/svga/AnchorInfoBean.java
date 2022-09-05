package com.live.fox.svga;

import com.chad.library.adapter.base.entity.SectionEntity;
import com.live.fox.entity.Anchor;


public class AnchorInfoBean extends SectionEntity<Anchor> {

    private String title;
    private boolean isMore;
    private boolean isShowBanner;

    public AnchorInfoBean(boolean isHeader) {
        super(isHeader, "");
    }

    public AnchorInfoBean(Anchor t) {
        super(t);
    }

//
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isMore() {
        return isMore;
    }

    public void setMore(boolean more) {
        isMore = more;
    }

    public boolean isShowBanner() {
        return isShowBanner;
    }

    public void setShowBanner(boolean showBanner) {
        isShowBanner = showBanner;
    }


}
