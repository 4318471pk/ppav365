package com.live.fox.ui.chat;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.live.fox.R;
import com.live.fox.entity.Letter;
import com.live.fox.utils.GlideUtils;
import com.live.fox.utils.TimeUtils;

import java.util.List;

/**
 * 如果想要展示更丰富的信息(语音信息、图片信息、位置信息等等 可参考 https://github.com/baiyuliang/QRobot)
 */
public class ChatAdapter extends BaseMultiItemQuickAdapter<Letter, BaseViewHolder> {

    public ChatAdapter(List<Letter> data) {
        super(data);
        //有2中情况 2种布局
        addItemType(0, R.layout.item_chat_receive);
        addItemType(1, R.layout.item_chat_send);
    }

    @Override
    protected void convert(BaseViewHolder helper, Letter data) {

        helper.setText(R.id.chat_time, TimeUtils.convertShortTime(data.getTimestamp()));
        helper.setText(R.id.tv_text, data.getContent());

        GlideUtils.loadDefaultCircleImage(mContext, data.getAvatar(), helper.getView(R.id.head_view));
    }


}
