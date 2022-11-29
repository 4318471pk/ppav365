package com.live.fox.view.bulletMessage;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.live.fox.MessageProtocol;
import com.live.fox.R;
import com.live.fox.db.LocalUserTagResourceDao;
import com.live.fox.entity.NewBornNobleOrGuardMessageBean;
import com.live.fox.entity.UserTagResourceBean;
import com.live.fox.ui.mine.noble.NobleActivity;
import com.live.fox.utils.ScreenUtils;

//新的守护或者贵族的产生的漂房子View
public class NewBornMessageView extends RelativeLayout {

    View maskView;
    TextView tvContent;
    ImageView ivBG;
    NewBornNobleOrGuardMessageBean bean;

    public NewBornMessageView(Context context, NewBornNobleOrGuardMessageBean bean) {
        super(context);
        this.bean=bean;
        initView();

    }

    public NewBornMessageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public NewBornMessageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView()
    {
        LayoutInflater.from(getContext()).inflate(R.layout.layout_message_view_new_born,this,true);
        ivBG=findViewById(R.id.ivBG);
        tvContent=findViewById(R.id.tvContent);
        maskView=findViewById(R.id.maskView);

        if(bean.getVipLevel()>0)
        {
          UserTagResourceBean userTagResourceBean= LocalUserTagResourceDao.getInstance().getLevelTag(bean.getVipLevel());
            if(userTagResourceBean==null)return;
            Drawable bg=ivBG.getDrawable();
            int bgWidth=bg.getIntrinsicWidth();
            int bgHeight=bg.getIntrinsicHeight();
            int padding10= ScreenUtils.dp2px(getContext(),10);

            String temple=getContext().getString(R.string.newBornNobleTips);
            String text=String.format(temple,bean.getNickname(),userTagResourceBean.getVipName());
            tvContent.setText(text);
            RelativeLayout.LayoutParams rl=(RelativeLayout.LayoutParams) maskView.getLayoutParams();
            rl.width=bgWidth/5;
            rl.height=bgHeight;
            maskView.setLayoutParams(rl);

            RelativeLayout.LayoutParams rlContent=(RelativeLayout.LayoutParams) tvContent.getLayoutParams();
            rlContent.width=bgWidth*4/5;
            rlContent.height=bgHeight;
            tvContent.setLayoutParams(rlContent);
            tvContent.setPadding(padding10,0,padding10,0);

            maskView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(bean.getProtocol().equals(MessageProtocol.LIVE_BUY_VIP))
                    {
                        NobleActivity.startActivity(getContext());
                    }
                }
            });
        }

    }


}
