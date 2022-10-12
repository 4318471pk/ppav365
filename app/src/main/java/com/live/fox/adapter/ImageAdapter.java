package com.live.fox.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.live.fox.R;
import com.live.fox.utils.pickphoto.MultiplexImage;
import com.live.fox.utils.GlideUtils;

import java.util.List;


/**
 * 图片列表适配器
 */
public class ImageAdapter extends BaseQuickAdapter<MultiplexImage, BaseViewHolder> {

    public ImageAdapter(List<MultiplexImage> data) {
        super(R.layout.image_recycle_item, data);
    }

    @Override
    protected void convert(BaseViewHolder holder, MultiplexImage multiplexImage) {

        GlideUtils.loadDefaultImage(mContext, multiplexImage.getImagePath(), holder.getView(R.id.iv_));
    }

}
