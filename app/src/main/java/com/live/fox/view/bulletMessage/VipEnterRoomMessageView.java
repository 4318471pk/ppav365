package com.live.fox.view.bulletMessage;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.live.fox.R;
import com.live.fox.db.LocalUserGuardDao;
import com.live.fox.db.LocalUserLevelDao;
import com.live.fox.db.LocalUserTagResourceDao;
import com.live.fox.entity.EnterRoomBean;
import com.live.fox.entity.LivingEnterLivingRoomBean;
import com.live.fox.entity.PersonalLivingMessageBean;
import com.live.fox.entity.UserGuardResourceBean;
import com.live.fox.entity.UserTagResourceBean;
import com.live.fox.utils.ScreenUtils;
import com.live.fox.utils.Strings;

public class VipEnterRoomMessageView extends RelativeLayout {

    Drawable bgDrawable;
    ImageView ivBg;
    TextView tvEnterRoom;
    LinearLayout llContent;

    public VipEnterRoomMessageView(Context context, LivingEnterLivingRoomBean bean) {
        super(context);
        initView(bean);
    }

    public VipEnterRoomMessageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public VipEnterRoomMessageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void initView(LivingEnterLivingRoomBean bean)
    {
        bgDrawable=getResources().getDrawable(R.mipmap.bg_vip_floating_enterroom);
        int viewWidth=bgDrawable.getIntrinsicWidth();
        int viewHeight=bgDrawable.getIntrinsicHeight();

        LayoutInflater.from(getContext()).inflate(R.layout.layout_vip_enterroom_message,this,true);
        ivBg=findViewById(R.id.ivBg);
        tvEnterRoom=findViewById(R.id.tvEnterRoom);
        llContent=findViewById(R.id.llContent);
        RelativeLayout.LayoutParams rl=(RelativeLayout.LayoutParams)ivBg.getLayoutParams();
        rl.height= ScreenUtils.dp2px(getContext(),21);
        rl.width=rl.height*viewWidth/viewHeight;
        ivBg.setLayoutParams(rl);
        ivBg.setImageDrawable(bgDrawable);

        RelativeLayout.LayoutParams rlContent=(RelativeLayout.LayoutParams) llContent.getLayoutParams();
        rlContent.leftMargin=(int)(rl.width*0.157f);
        llContent.setLayoutParams(rlContent);

        if(bean!=null)
        {
            if(bean.getUserLevel()>0)
            {
                String imgPath= LocalUserLevelDao.getInstance().getLevelIcon(bean.getUserLevel());
                if(!TextUtils.isEmpty(imgPath))
                {
                    Bitmap bitmap=BitmapFactory.decodeFile(imgPath);
                    if(bitmap!=null)
                    {
                        addIcon(bitmap,12f,30f);
                    }
                }
            }

            long count=LocalUserTagResourceDao.getInstance().getCount();
            if(bean.getVipLevel()>0 && bean.getVipLevel()<count)
            {
                UserTagResourceBean userTagResourceBean= LocalUserTagResourceDao.getInstance().getLevelTag(bean.getVipLevel());
                if(userTagResourceBean!=null && !TextUtils.isEmpty(userTagResourceBean.getLocalVipImgPath()))
                {
                    Bitmap bitmap=BitmapFactory.decodeFile(userTagResourceBean.getLocalVipImgPath());
                    if(bitmap!=null)
                    {
                        addIcon(bitmap,12f,38.5f);
                    }
                }
            }

            if(Strings.isDigitOnly(bean.getGuardLevel()))
            {
                int guardLevel=Integer.valueOf(bean.getGuardLevel());
                UserGuardResourceBean userGuardResourceBean= LocalUserGuardDao.getInstance().getLevel(guardLevel);
                if(userGuardResourceBean!=null && !TextUtils.isEmpty(userGuardResourceBean.getLocalImgSmallPath()))
                {
                    Bitmap bitmap= BitmapFactory.decodeFile(userGuardResourceBean.getLocalImgSmallPath());
                    if(bitmap!=null)
                    {
                        addIcon(bitmap,14f,11.5f);
                    }
                }
            }

            if(bean.isRoomManage())
            {
                addIcon(getResources().getDrawable(R.mipmap.icon_admin_medium_tag),16f,16f);
            }

            if(!TextUtils.isEmpty( bean.getNickname()))
            {
                TextView textView=new TextView(getContext());
                LinearLayout.LayoutParams ll= new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                ll.gravity= Gravity.CENTER_VERTICAL;
                textView.setLayoutParams(ll);
                textView.setTextSize(TypedValue.COMPLEX_UNIT_SP,13);
                textView.setTextColor(0xff85EFFF);
                textView.setSingleLine();
                textView.setEllipsize(TextUtils.TruncateAt.valueOf("END"));
                textView.setText(bean.getNickname());
                llContent.addView(textView);
            }

        }

    }

    //12 30 12 38.5
    private void addIcon(Bitmap bitmap,float width,float height)
    {
        int dipWidth=ScreenUtils.dp2px(getContext(),width);
        int dipHeight=ScreenUtils.dp2px(getContext(),height);
        ImageView imageView=new ImageView(getContext());
        LinearLayout.LayoutParams ll= new LinearLayout.LayoutParams(dipWidth,dipHeight);
        ll.gravity= Gravity.CENTER_VERTICAL;
        imageView.setLayoutParams(ll);
        imageView.setImageBitmap(bitmap);
        llContent.addView(imageView);
    }

    private void addIcon(Drawable drawable,float width,float height)
    {
        int dipWidth=ScreenUtils.dp2px(getContext(),width);
        int dipHeight=ScreenUtils.dp2px(getContext(),height);
        ImageView imageView=new ImageView(getContext());
        LinearLayout.LayoutParams ll= new LinearLayout.LayoutParams(dipWidth, dipHeight);
        ll.gravity= Gravity.CENTER_VERTICAL;
        imageView.setLayoutParams(ll);
        imageView.setImageDrawable(drawable);
        llContent.addView(imageView);
    }
}
