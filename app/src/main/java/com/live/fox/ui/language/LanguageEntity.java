package com.live.fox.ui.language;

import android.graphics.drawable.Drawable;

/**
 * 语言实体
 */
public class LanguageEntity {

    private String language;
    private Drawable type;
    private boolean isSelected;

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public Drawable getType() {
        return type;
    }

    public void setType(Drawable type) {
        this.type = type;
    }
}
