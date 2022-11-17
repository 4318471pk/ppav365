package com.live.fox.view.bulletMessage;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
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

public class EnterRoomMessageView extends RelativeLayout {

    Drawable bgDrawable;
    ImageView ivBg;
    LinearLayout llContent;
    int widthEnterRoom;

    public EnterRoomMessageView(Context context, LivingEnterLivingRoomBean bean) {
        super(context);
        initView(bean);
    }

    public EnterRoomMessageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public EnterRoomMessageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void initView(LivingEnterLivingRoomBean bean)
    {
        LayoutInflater.from(getContext()).inflate(R.layout.layout_vip_enterroom_message,this,true);
        ivBg=findViewById(R.id.ivBg);
        llContent=findViewById(R.id.llContent);

        if(bean.isGuard())
        {
            ivBg.setImageDrawable(getResources().getDrawable(R.mipmap.bg_guard_floating_enterroom));
        }
        else
        {
            if(bean.getVipLevel()>0)
            {
                ivBg.setImageDrawable(getResources().getDrawable(R.mipmap.bg_vip_floating_enterroom));
            }
        }

        bgDrawable=ivBg.getDrawable();
        int viewWidth=bgDrawable.getIntrinsicWidth();
        int viewHeight=bgDrawable.getIntrinsicHeight();
        Paint paint = new Paint();
        paint.setTextSize(ScreenUtils.sp2px(getContext(),13));
        widthEnterRoom = (int)(paint.measureText(getResources().getString(R.string.enterRoomTag)));

        RelativeLayout.LayoutParams rl=(RelativeLayout.LayoutParams)ivBg.getLayoutParams();
        rl.height= ScreenUtils.dp2px(getContext(),21);
        rl.width=rl.height*viewWidth/viewHeight;
        ivBg.setLayoutParams(rl);

        RelativeLayout.LayoutParams rlContent=(RelativeLayout.LayoutParams) llContent.getLayoutParams();
        rlContent.leftMargin=(int)(rl.width*0.14f);
        rlContent.width=(int)(rl.width*0.86f);
        llContent.setLayoutParams(rlContent);

        int totalWidth=rlContent.width;
        int iconWidth=0;
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
                        addIcon(bitmap,30f,12f);
                        iconWidth=iconWidth+ScreenUtils.dp2px(getContext(),32);
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
                        addIcon(bitmap,38.5f,12f);
                        iconWidth=iconWidth+ScreenUtils.dp2px(getContext(),40.5f);
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
                        iconWidth=iconWidth+ScreenUtils.dp2px(getContext(),16f);
                    }
                }
            }

            if(bean.isRoomManage())
            {
                addIcon(getResources().getDrawable(R.mipmap.icon_admin_medium_tag),16f,16f);
                iconWidth=iconWidth+ScreenUtils.dp2px(getContext(),18f);
            }

            if(!TextUtils.isEmpty( bean.getNickname()))
            {
                int dip2=ScreenUtils.dp2px(getContext(),2);
                int width=totalWidth-iconWidth-widthEnterRoom-(dip2*10);
                int nameWidth = (int)(paint.measureText(bean.getNickname()));
                if(nameWidth<width)
                {
                    width=nameWidth;
                }
                TextView textView=new TextView(getContext());
                LinearLayout.LayoutParams ll= new LinearLayout.LayoutParams(width, ViewGroup.LayoutParams.WRAP_CONTENT);
                ll.gravity= Gravity.CENTER_VERTICAL;
                ll.leftMargin=ScreenUtils.dp2px(getContext(),2);
                textView.setLayoutParams(ll);
                textView.setTextSize(TypedValue.COMPLEX_UNIT_SP,13);
                textView.setTextColor(0xff85EFFF);
                textView.setSingleLine();
                textView.setEllipsize(TextUtils.TruncateAt.valueOf("END"));
                textView.setText(bean.getNickname());
                textView.setBackgroundColor(Color.GRAY);
                llContent.addView(textView);

                TextView enterRoom=new TextView(getContext());
                LinearLayout.LayoutParams llEnterRoom= new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                llEnterRoom.gravity= Gravity.CENTER_VERTICAL;
                enterRoom.setLayoutParams(llEnterRoom);
                enterRoom.setTextSize(TypedValue.COMPLEX_UNIT_SP,13);
                enterRoom.setTextColor(0xffFFFFFF);
                enterRoom.setSingleLine();
                enterRoom.setText(getResources().getString(R.string.enterRoomTag));
                enterRoom.setBackgroundColor(Color.GREEN);
                llContent.addView(enterRoom);

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
        ll.leftMargin=ScreenUtils.dp2px(getContext(),2);
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
        ll.leftMargin=ScreenUtils.dp2px(getContext(),2);
        ll.gravity= Gravity.CENTER_VERTICAL;
        imageView.setLayoutParams(ll);
        imageView.setImageDrawable(drawable);
        llContent.addView(imageView);
    }
}
