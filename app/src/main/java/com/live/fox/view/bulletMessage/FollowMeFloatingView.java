package com.live.fox.view.bulletMessage;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.live.fox.R;
import com.live.fox.entity.LivingCurrentAnchorBean;
import com.live.fox.entity.RoomListBean;
import com.live.fox.utils.BulletViewUtils;
import com.live.fox.utils.GlideUtils;
import com.live.fox.utils.OnClickFrequentlyListener;
import com.live.fox.utils.ScreenUtils;
import com.live.fox.view.GradientTextView;

import java.lang.ref.WeakReference;

import de.hdodenhof.circleimageview.CircleImageView;

public class FollowMeFloatingView extends RelativeLayout {

    GradientTextView gtvFollow;
    TextView tvFollowMe,tvName;
    ImageView civProfileImage;

    public FollowMeFloatingView(Context context) {
        super(context);
        init();
    }

    public FollowMeFloatingView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public FollowMeFloatingView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init()
    {
        LayoutInflater.from(getContext()).inflate(R.layout.floating_view_follow_me,this,true);
        civProfileImage=findViewById(R.id.civProfileImage);
        gtvFollow=findViewById(R.id.gtvFollow);
        tvFollowMe=findViewById(R.id.tvFollowMe);
        tvName=findViewById(R.id.tvName);
    }

    public void postWidow(Activity activity,LivingCurrentAnchorBean bean, RelativeLayout relativeLayout, OnClickFollowListener onClickFollowListener)
    {
        if(bean!=null)
        {
            if(!TextUtils.isEmpty(bean.getAvatar()))
            {
                GlideUtils.loadCircleImage(getContext(),bean.getAvatar(),0,0,civProfileImage);
            }
            gtvFollow.setOnClickListener(new OnClickFrequentlyListener() {
                @Override
                public void onClickView(View view) {
                    WeakReference<Activity> weakReference=new WeakReference<>(activity);
                    BulletViewUtils.disappear(FollowMeFloatingView.this, weakReference, new BulletViewUtils.OnFinishAniListener() {
                        @Override
                        public void onFinish(Object obj) {

                        }
                    });

                    if(onClickFollowListener!=null)
                    {
                        onClickFollowListener.onClickFollow();
                    }
                }
            });
            tvName.setText(bean.getNickname());

            int screenHeight= ScreenUtils.getScreenHeight(getContext());
            int screenWidth=ScreenUtils.getScreenWidth(getContext());
            RelativeLayout.LayoutParams rl=new RelativeLayout.LayoutParams((int)(screenWidth*0.72f),ScreenUtils.dp2px(getContext(),60));
            rl.topMargin=(int)(screenHeight*0.736f);
            relativeLayout.addView(this,rl);

            BulletViewUtils.goLeftToRightDelayDisappear(this, 1000, 10000, activity, new BulletViewUtils.OnFinishAniListener() {
                @Override
                public void onFinish(Object obj) {

                }
            });
        }
    }

    public interface OnClickFollowListener
    {
        void onClickFollow();
    }
}
