package com.live.fox.ui.language;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.live.fox.R;


public class LanguageAdapter extends BaseQuickAdapter<LanguageEntity, BaseViewHolder> {

    public LanguageAdapter() {
        super(R.layout.item_language);
    }

    @Override
    protected void convert(BaseViewHolder helper, LanguageEntity item) {
        ImageView imageView = helper.getView(R.id.language_country_flag);
        Glide.with(mContext).load(item.getType()).into(imageView);
        helper.setText(R.id.language_country_title, item.getLanguage());
    }

}
