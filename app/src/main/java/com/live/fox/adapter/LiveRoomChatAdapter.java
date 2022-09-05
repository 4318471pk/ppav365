package com.live.fox.adapter;

import android.text.method.LinkMovementMethod;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.live.fox.R;
import com.live.fox.entity.ChatEntity;
import com.live.fox.entity.User;
import com.live.fox.utils.GlideUtils;

import java.util.List;

/**
 * 直播聊天适配器
 */
public class LiveRoomChatAdapter extends BaseQuickAdapter<ChatEntity, BaseViewHolder> {

    public LiveRoomChatAdapter(List<ChatEntity> data) {
        super(R.layout.item_live_chat, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ChatEntity chat) {
        TextView tvMessage = helper.getView(R.id.tv_message);
        tvMessage.setMovementMethod(LinkMovementMethod.getInstance());

        //给文字设置阴影
        float radius = mContext.getResources().getDisplayMetrics().density * 1;
        tvMessage.setShadowLayer(radius, radius / 3, radius / 3, R.color.black);
        User user = chat.getUser();
        boolean isBackChange = false;
        if (user.getBadgeList() != null && user.come) {
            if (user.getBadgeList().contains(6)) {
                isBackChange = true;
            } else if (user.getBadgeList().contains(7)) {
                isBackChange = true;
            } else if (user.getBadgeList().contains(8)) {
                isBackChange = true;
            } else if (user.getBadgeList().contains(9)) {
                isBackChange = true;
            } else if (user.getBadgeList().contains(10)) {
                isBackChange = true;
            }
        }

        if (isBackChange) {
            helper.itemView.setBackgroundResource(R.drawable.shape_live_p);
        } else {
            helper.itemView.setBackgroundResource(R.drawable.shape_liveroom_chat_pubmsg_bg);
        }

        tvMessage.setText(chat.getContent());
        GlideUtils.releaseTextViewResouce(tvMessage);

        helper.addOnClickListener(R.id.tv_message);
    }

}
