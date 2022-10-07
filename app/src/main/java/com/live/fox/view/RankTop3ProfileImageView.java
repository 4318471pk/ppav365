package com.live.fox.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.live.fox.R;
import com.live.fox.utils.device.ScreenUtils;

import org.jetbrains.annotations.NotNull;

import java.lang.ref.WeakReference;

public class RankTop3ProfileImageView extends RelativeLayout {

    int crownIndex = -1;
    int crownsResource[] = new int[]{R.mipmap.icon_top1, R.mipmap.icon_top2, R.mipmap.icon_top3};
    int decorationResource[] = new int[]{R.mipmap.icon_zi_rank};
    ImageView ivDecoration, ivCrown, ivProfile;
    boolean isInit=false;


    public RankTop3ProfileImageView(@NonNull @NotNull Context context, int crownIndex) {
        super(context);
        initView(crownIndex);
    }

    public RankTop3ProfileImageView(@NonNull @NotNull Context context) {
        super(context);
        initView(-1);
    }

    public RankTop3ProfileImageView(@NonNull @NotNull Context context, @Nullable @org.jetbrains.annotations.Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(-1);
    }

    public RankTop3ProfileImageView(@NonNull @NotNull Context context, @Nullable @org.jetbrains.annotations.Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(-1);
    }

    private void initView(int crownIndex) {
        this.crownIndex = crownIndex;
        View view = View.inflate(getContext(), R.layout.view_top3rank, null);
        ivProfile = view.findViewById(R.id.ivProfile);
        ivCrown = view.findViewById(R.id.ivCrown);
        ivDecoration = view.findViewById(R.id.ivDecoration);
        addView(view);
        view.post(new Runnable() {
            @Override
            public void run() {
                if (getWidth() > 0 && !isInit) {
                    isInit=true;

                    Drawable drawable = crownIndex > -1 && crownIndex < 3 ? getResources().getDrawable(crownsResource[crownIndex]) : null;
                    if (drawable == null) {
                        ivCrown.setVisibility(GONE);
                        RelativeLayout.LayoutParams rl = (RelativeLayout.LayoutParams) ivDecoration.getLayoutParams();
                        rl.topMargin = 0;
                        ivDecoration.setLayoutParams(rl);
                    } else {
                        ivCrown.setImageDrawable(drawable);
                        int crownDrawableWidth=drawable.getIntrinsicWidth();
                        int crownDrawableHeight=drawable.getIntrinsicHeight();

                        RelativeLayout.LayoutParams rl=(RelativeLayout.LayoutParams) ivCrown.getLayoutParams();
                        rl.width=getWidth()/2;
                        int crownHeight=crownDrawableHeight*rl.width/crownDrawableWidth;
                        rl.height=crownHeight;
                        ivCrown.setLayoutParams(rl);

                        Drawable drawableDecoration=ivDecoration.getDrawable();
                        int deHeight=drawableDecoration.getIntrinsicHeight();
                        int deWidth=drawableDecoration.getIntrinsicWidth();
                        RelativeLayout.LayoutParams rlDe = (RelativeLayout.LayoutParams) ivDecoration.getLayoutParams();
                        rlDe.width=getWidth();
                        rlDe.topMargin=crownHeight/2;
                        ivDecoration.setLayoutParams(rlDe);

                        RelativeLayout.LayoutParams rlProfile = (RelativeLayout.LayoutParams) ivProfile.getLayoutParams();
                        rlProfile.topMargin=crownHeight/2;
                        rlProfile.width=(int)(getWidth()*0.9f);
                        rlProfile.height=(int)(getWidth()*0.9f);
                        ivProfile.setLayoutParams(rlProfile);
//                ivProfile.setPadding(0,crownHeight/2,0,0);

                    }

//                    int padding=(int)( getWidth()*0.08f);
//                    ivProfile.setPadding(padding,padding,padding,padding);

                }
            }
        });
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

    }
}
