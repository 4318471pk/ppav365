package com.live.fox.adapter;

import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.live.fox.R;
import com.live.fox.entity.ChatEntity;

import java.util.List;


public class LevelChatAdapter extends BaseQuickAdapter<ChatEntity, BaseViewHolder> {


    public LevelChatAdapter(List data) {
        super(R.layout.item_level_chat, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ChatEntity chat) {
        TextView tvMessage = helper.getView(R.id.tv_message);
//        tvMessage.setMovementMethod(LinkMovementMethod.getInstance());

        //给文字设置阴影
//        float radius = mContext.getResources().getDisplayMetrics().density * 1;
//        tvMessage.setShadowLayer(radius, radius / 3, radius / 3, R.color.black);
        tvMessage.setText(chat.getContent());
        helper.addOnClickListener(R.id.tv_message);
    }

}
