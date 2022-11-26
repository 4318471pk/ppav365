package com.live.fox.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.live.fox.R;
import com.live.fox.utils.ResourceUtils;
import com.live.fox.utils.ScreenUtils;
import com.opensource.svgaplayer.SVGADrawable;
import com.opensource.svgaplayer.SVGAImageView;
import com.opensource.svgaplayer.SVGAParser;
import com.opensource.svgaplayer.SVGAVideoEntity;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.List;

public class RankProfileView extends RelativeLayout {

    public static final int NONE=-1;
    int crownIndex = -1;
    int decorationIndex=-1;
    boolean isLiving=false;
    int crownsResource[] = new int[]{R.mipmap.icon_top1, R.mipmap.icon_top2, R.mipmap.icon_top3};
    int[] decorationResource = null;
    float scaleAndMargins[][]={{0.87f,0.14f},{0.84f,0.05f},{0.87f,0.12f},{0.85f,0.07f},{0.87f,0.1f},{0.85f,0.1f},{0.85f,0.08f}};
    ImageView ivDecoration, ivCrown, ivProfile;
    SVGAImageView ivLiving;
    OnConfirmWidthAndHeightListener onConfirmWidthAndHeightListener;
    boolean isInit=false;
    int viewHeight=0,viewWidth=0;
    boolean resumeAfterAttached=false;
    SVGAVideoEntity svgaVideoEntity;
    Handler handler=new Handler(Looper.myLooper()){
        @Override
        public void handleMessage(@NonNull @NotNull Message msg) {
            super.handleMessage(msg);
            Log.e("BBBBBN","decodeFromAssets "+isLiving);
            if(isLiving)
            {
                startLivingAnimation();
            }
        }
    };

    public RankProfileView(@NonNull @NotNull Context context, int crownIndex,boolean isLiving) {
        super(context);
        initView(crownIndex,-1,isLiving);
    }

    public RankProfileView(@NonNull @NotNull Context context, int crownIndex, int decorationIndex,boolean isLiving) {
        super(context);
        initView(crownIndex,decorationIndex,isLiving);
    }

    public RankProfileView(@NonNull @NotNull Context context) {
        this(context,null);
    }

    public RankProfileView(@NonNull @NotNull Context context,  AttributeSet attrs) {
        this(context, attrs,0);
    }

    public RankProfileView(@NonNull @NotNull Context context,  AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        if(attrs!=null)
        {
            final TypedArray array =
                    context.obtainStyledAttributes(attrs, R.styleable.RankProfileView);
            crownIndex = array.getDimensionPixelSize(R.styleable.RankProfileView_crownIndex, -1);
            decorationIndex = array.getDimensionPixelSize(R.styleable.RankProfileView_decorationIndex, -1);
            array.recycle();
        }

        initView(crownIndex,decorationIndex,isLiving);
    }

    public void setOnConfirmWidthAndHeightListener(OnConfirmWidthAndHeightListener onConfirmWidthAndHeightListener) {
        this.onConfirmWidthAndHeightListener = onConfirmWidthAndHeightListener;
        if(isInit && onConfirmWidthAndHeightListener!=null)
        {
            onConfirmWidthAndHeightListener.onValue(viewWidth,viewHeight);
        }
    }

    private void initView(int crownIndex, int decorationIndex,boolean isLiving) {
        decorationResource=new ResourceUtils().getResourcesID(R.array.rankEdgePics);
        this.crownIndex = crownIndex;
        this.decorationIndex=decorationIndex;
        this.isLiving=isLiving;
        LayoutInflater.from(getContext()).inflate(R.layout.view_rank_profile,this,true);
        ivProfile = findViewById(R.id.ivProfile);
        ivCrown = findViewById(R.id.ivCrown);
        ivDecoration = findViewById(R.id.ivDecoration);
        ivLiving=findViewById(R.id.ivLiving);

        Log.e("BBBBBN","initView  "+isLiving);

        SVGAParser parser = SVGAParser.Companion.shareParser();
        parser.decodeFromAssets("living_profile.svga", new SVGAParser.ParseCompletion() {
            @Override
            public void onComplete(SVGAVideoEntity svgaVideoEntity) {
                RankProfileView.this.svgaVideoEntity = svgaVideoEntity;
                handler.sendEmptyMessage(0);
            }

            @Override
            public void onError() {
            }
        }, new SVGAParser.PlayCallback() {
            @Override
            public void onPlay(@NotNull List<? extends File> list) {

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
        if(w>0 && !isInit)
        {
            isInit=true;
//            post(new Runnable() {
//                @Override
//                public void run() {
//                    adjustLayout();
//                }
//            });
//            adjustLayout();
        }
    }

    public void setIndex(int crownIndex,int decorationIndex,boolean isLiving)
    {
        this.crownIndex = crownIndex;
        if(decorationIndex>0 && decorationIndex<8)
        {
            this.decorationIndex = decorationIndex-1;
        }
        else
        {
            this.decorationIndex=-1;
        }

        this.isLiving=isLiving;
        if(isLiving)
        {
            startLivingAnimation();
        }
        adjustLayout();
    }

    public ImageView getProfileImage() {
        return ivProfile;
    }

    private void adjustLayout()
    {
        if(getLayoutParams()==null)
        {
            return;
        }
        viewWidth=getLayoutParams().width;
        if(viewWidth==0)
        {
            viewWidth=getWidth();
        }
        if (viewWidth > 0) {
            isInit=true;
            if(decorationIndex>-1 && decorationIndex<decorationResource.length)
            {
                ivDecoration.setImageDrawable(getResources().getDrawable(decorationResource[decorationIndex]));
            }

            Drawable drawable = crownIndex > -1 && crownIndex < 3 ? getResources().getDrawable(crownsResource[crownIndex]) : null;
            if (drawable == null) {
                ivCrown.setVisibility(GONE);

                if(decorationIndex>-1 && decorationIndex<decorationResource.length)
                {
                    RelativeLayout.LayoutParams rlDe = (RelativeLayout.LayoutParams) ivDecoration.getLayoutParams();
                    rlDe.width=viewWidth;
                    ivDecoration.setLayoutParams(rlDe);

                    int ivDecorationHeight=viewWidth*ivDecoration.getDrawable().getIntrinsicHeight()
                            /ivDecoration.getDrawable().getIntrinsicWidth();

                    RelativeLayout.LayoutParams rlProfile = (RelativeLayout.LayoutParams) ivProfile.getLayoutParams();
                    rlProfile.width=(int)(viewWidth*scaleAndMargins[decorationIndex][0]);
                    rlProfile.height=(int)(viewWidth*scaleAndMargins[decorationIndex][0]);
                    rlProfile.topMargin=(int)(ivDecorationHeight*scaleAndMargins[decorationIndex][1]);
                    ivProfile.setLayoutParams(rlProfile);
                    ivProfile.setPadding(0,0,0,0);

                    viewHeight=rlProfile.height+rlProfile.topMargin;
                    if(onConfirmWidthAndHeightListener!=null)
                    {
                        onConfirmWidthAndHeightListener.onValue(rlProfile.width,rlProfile.height+rlProfile.topMargin);
                    }
                }
                else
                {
                    ivDecoration.setVisibility(GONE);

                    int dip15=ScreenUtils.dp2px(getContext(),15);
                    RelativeLayout.LayoutParams rlProfile = (RelativeLayout.LayoutParams) ivProfile.getLayoutParams();
                    rlProfile.width=viewWidth;
                    rlProfile.height=viewWidth;
                    ivProfile.setLayoutParams(rlProfile);
//                    ivProfile.setPadding(dip15/2,dip15/2,dip15/2,dip15/2);

                    viewHeight=rlProfile.height;
                    if(onConfirmWidthAndHeightListener!=null)
                    {
                        onConfirmWidthAndHeightListener.onValue(rlProfile.width,rlProfile.width);
                    }
                }
            } else {
                ivCrown.setImageDrawable(drawable);
                int crownDrawableWidth=drawable.getIntrinsicWidth();
                int crownDrawableHeight=drawable.getIntrinsicHeight();

                RelativeLayout.LayoutParams rl=(RelativeLayout.LayoutParams) ivCrown.getLayoutParams();
                rl.width=viewWidth/2;
                int crownHeight=crownDrawableHeight*rl.width/crownDrawableWidth;
                rl.height=crownHeight;
                ivCrown.setLayoutParams(rl);

                RelativeLayout.LayoutParams rlDe = (RelativeLayout.LayoutParams) ivDecoration.getLayoutParams();
                rlDe.width=viewWidth;
                rlDe.topMargin=crownHeight/2;
                ivDecoration.setLayoutParams(rlDe);

                int ivDecorationHeight=viewWidth*ivDecoration.getDrawable().getIntrinsicHeight()
                        /ivDecoration.getDrawable().getIntrinsicWidth();

                RelativeLayout.LayoutParams rlProfile = (RelativeLayout.LayoutParams) ivProfile.getLayoutParams();
                rlProfile.topMargin=crownHeight/2+(int)(ivDecorationHeight*scaleAndMargins[decorationIndex][1]);
                rlProfile.width=(int)(viewWidth*scaleAndMargins[decorationIndex][0]);
                rlProfile.height=(int)(viewWidth*scaleAndMargins[decorationIndex][0]);
                ivProfile.setLayoutParams(rlProfile);
                ivProfile.setPadding(0,0,0,0);

                viewHeight=rlDe.height+rlDe.topMargin;
                if(onConfirmWidthAndHeightListener!=null)
                {
                    onConfirmWidthAndHeightListener.onValue(rlProfile.width,rlDe.height+rlDe.topMargin);
                }
//                ivProfile.setPadding(0,crownHeight/2,0,0);

            }

//                    int padding=(int)( viewWidth*0.08f);
//                    ivProfile.setPadding(padding,padding,padding,padding);


            if(isLiving)
            {
                ivLiving.setVisibility(VISIBLE);
                RelativeLayout.LayoutParams rlProfile = (RelativeLayout.LayoutParams) ivProfile.getLayoutParams();

                RelativeLayout.LayoutParams rlLiving=(RelativeLayout.LayoutParams)ivLiving.getLayoutParams();
                rlLiving.width=rlProfile.width;
                rlLiving.height=rlProfile.height;
                rlLiving.topMargin=rlProfile.topMargin;
                rlLiving.addRule(RelativeLayout.CENTER_HORIZONTAL,RelativeLayout.TRUE);
                ivLiving.setLayoutParams(rlLiving);
                ivLiving.setScaleX(1.9f);
                ivLiving.setScaleY(1.9f);
                ivLiving.setClearsAfterDetached(false);

                if(isLiving && !ivLiving.isAnimating())
                {
                    startLivingAnimation();
                }

            }
            else
            {
//                ivLiving.stopAnimation();
                ivLiving.setVisibility(GONE);
            }
        }
    }

    //viewPager出bug用的方法
    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if(resumeAfterAttached && isLiving && ivLiving!=null)
        {
            ivLiving.pauseAnimation();
        }
    }

    //viewPager出bug用的方法
    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if(!isInit)
        {
            adjustLayout();
        }

//        if(resumeAfterAttached && isLiving && ivLiving!=null)
//        {
//            if(svgaVideoEntity!=null)
//            {
//                ivLiving.setImageDrawable(new SVGADrawable(svgaVideoEntity));
//                ivLiving.startAnimation();
//            }
//        }
    }

    public void setResumeAniAfterAttached(boolean resumeAfterAttached)
    {
        this.resumeAfterAttached=resumeAfterAttached;
    }


    // recycleView出bug用的方法
    public void stopLivingAnimation()
    {
        if(isLiving)
        {
            ivLiving.pauseAnimation();
//            ivLiving.clear();
        }
    }

    // recycleView出bug用的方法
    public void startLivingAnimation()
    {
        if(isLiving)
        {
//            SVGAParser parser = SVGAParser.Companion.shareParser();
//            parser.decodeFromAssets("living_profile.svga", new SVGAParser.ParseCompletion() {
//                @Override
//                public void onComplete(SVGAVideoEntity svgaVideoEntity) {
//                    SVGADrawable drawable = new SVGADrawable(svgaVideoEntity);
////                    int width=(int)(svgaVideoEntity.getVideoSize().viewWidth);
////                    int height=(int)(svgaVideoEntity.getVideoSize().getHeight());
////
////                    int bot=(int)(-1*(height*1.6f/12));
//
////                    Log.e("SVGADrawable",svgaVideoEntity.getVideoSize().viewWidth+" "+svgaVideoEntity.getVideoSize().getHeight());
////                        drawable.setBounds(width/4,height/2,width/2,height/2-(height/12));
//                    ivLiving.setImageDrawable(drawable);
//                    ivLiving.startAnimation();
//                }
//
//                @Override
//                public void onError() {
//                }
//            }, new SVGAParser.PlayCallback() {
//                @Override
//                public void onPlay(@NotNull List<? extends File> list) {
//
//                }
//            });
            if(svgaVideoEntity!=null)
            {
                ivLiving.setImageDrawable(new SVGADrawable(svgaVideoEntity));
                ivLiving.startAnimation();
            }
        }
    }

    public interface OnConfirmWidthAndHeightListener
    {
        void onValue(int width,int height);
    }
}
