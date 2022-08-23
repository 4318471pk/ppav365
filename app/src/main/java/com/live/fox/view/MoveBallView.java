package com.live.fox.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.live.fox.R;
import com.live.fox.utils.DensityUtils;
import com.live.fox.utils.LogUtils;
import com.live.fox.utils.device.ScreenUtils;


/**
 * 游戏里的精灵球
 */
public class MoveBallView extends LinearLayout {

    private ImageView iv_moveball;
    private FrameLayout layout_leftextend;
    private ImageView iv_left1;
    private ImageView iv_left2;
    private ImageView iv_left3;
    private LinearLayout layout_rightextend;
    private ImageView iv_right1;
    private ImageView iv_right2;
    private ImageView iv_right3;

    /**
     * View的宽高
     */
    private float width;
    private float height;

    /**
     * 状态栏的高度
     */
    int barHeight = 0;
    /**
     * 屏幕的宽高
     */
    private int screenWidth;
    private int screenHeight;

    Context context;

    boolean isExtend = false;

    int extendWidth = 0;
    boolean isRightEx = false;

    boolean isToEdgeAniming = false; //是否在靠边动画中

    public MoveBallView(Context context) {
        this(context, null);
    }

    public MoveBallView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
        setOrientation(LinearLayout.HORIZONTAL);
//        int padding = DeviceUtils.dp2px(context, 2);
//        setPadding(padding, 0, padding, 0);
//        setGravity(Gravity.CENTER_VERTICAL);
    }

    public void setView(Context context, int x, int y, int viewWidth, int viewHeight){
        this.setX(x);
        this.setY(y);
        invalidate();

        this.context = context;
        screenWidth = ScreenUtils.getScreenWidth(context);
        screenHeight = ScreenUtils.getScreenHeight(context);

        extendWidth  = DensityUtils.dp2px(context, 148);
        width = viewWidth;
        height = viewHeight;

        animator = ValueAnimator.ofFloat(.85f, 1f);
        animator.setDuration(120);

        toEdge();
    }

    ValueAnimator animator;

    private void initView(Context context) {
        LayoutInflater.from(context).inflate(R.layout.view_moveball, this, true);
        iv_moveball = (ImageView) findViewById(R.id.iv_moveball);
        layout_rightextend = (LinearLayout)findViewById(R.id.layout_rightextend);
        iv_right1 = (ImageView)findViewById(R.id.iv_right1);
        iv_right2 = (ImageView)findViewById(R.id.iv_right2);
        iv_right3 = (ImageView)findViewById(R.id.iv_right3);
        layout_leftextend = (FrameLayout)findViewById(R.id.layout_leftextend);
        iv_left1 = (ImageView)findViewById(R.id.iv_left1);
        iv_left2 = (ImageView)findViewById(R.id.iv_left2);
        iv_left3 = (ImageView)findViewById(R.id.iv_left3);

        layout_rightextend.setVisibility(View.GONE);
        layout_leftextend.setVisibility(View.GONE);

        iv_right1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(view!=null){
                    onClick.onClick(view);
                }
            }
        });

        iv_right2.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(view!=null){
                    onClick.onClick(view);
                }
            }
        });

        iv_right3.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(view!=null){
                    onClick.onClick(view);
                }
            }
        });

        iv_left1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(view!=null){
                    onClick.onClick(view);
                }
            }
        });

        iv_left2.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(view!=null){
                    onClick.onClick(view);
                }
            }
        });

        iv_left3.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(view!=null){
                    onClick.onClick(view);
                }
            }
        });




    }


    /**
     * 触摸点相对于View的坐标
     */
    private float touchX;
    private float touchY;
    private float touchXPos;
    private float touchYPos;

    boolean isFFF = false;
    private long currentMS;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                LogUtils.e( "ACTION_DOWN");
                clearAnimation();
                touchX = event.getX();
                touchY = event.getY();
                touchXPos = getX();
                touchYPos = getY();
                currentMS = System.currentTimeMillis();//long currentMS     获取系统时间
                return true;
            case MotionEvent.ACTION_MOVE:
                LogUtils.e( "ACTION_MOVE");
                return true;
            case MotionEvent.ACTION_UP:
                LogUtils.e( "ACTION_UP");
                //当从点击到弹起小于半秒的时候,则判断为点击,如果超过则不响应点击事件
                long moveTime = System.currentTimeMillis() - currentMS;//移动时间
                if(moveTime<200) {//  &&Math.abs(touchXPos-getX())<10 && Math.abs(touchYPos-getY())<10
                    if(!isToEdgeAniming){
                        //在执行贴边动画的过程中不执行点击事件
                        onClick();
                    }
                }else{
                    if(isRightEx){
                        setX(screenWidth-width);
                    }
                    isRightEx = false;
                    layout_leftextend.setVisibility(View.GONE);
                    layout_rightextend.setVisibility(View.GONE);
                    iv_moveball.setVisibility(View.VISIBLE);
                    float nowX = 0f;
                    isExtend = false;
                    if(isFFF){
                        isFFF = false;
                        nowX = screenWidth - width - touchX;
                    }else {
                        nowX = event.getRawX() - touchX;
                    }
                    float nowY = event.getRawY() - touchY - barHeight;
                    nowX = nowX < 0 ? 0 : (nowX + width > screenWidth) ? (screenWidth - width) : nowX;
                    nowY = nowY < 0 ? 0 : (nowY + height > screenHeight) ? (screenHeight - height) : nowY;
                    this.setY(nowY);
                    this.setX(nowX);
                    invalidate();
                    toEdge();
                }

                return true;
            default:
                return super.onTouchEvent(event);
        }
    }

    //做动画贴边效果
    public void toEdge(){
        float centerX = getX() + width / 2;
        ObjectAnimator objectAnimator;
        if (centerX > ScreenUtils.getScreenWidth(context) / 2) {
            objectAnimator = ObjectAnimator.ofFloat(this, "translationX",
                    getX(), screenWidth - width)
                    .setDuration(500);

        } else {
            objectAnimator = ObjectAnimator.ofFloat(this, "translationX",
                    getX(), 0)
                    .setDuration(500);

        }

        objectAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                LogUtils.e("动画开始");
                isToEdgeAniming = true;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                LogUtils.e("动画结束");
                isToEdgeAniming = false;
            }
        });
        if(!isToEdgeAniming) {
            objectAnimator.start();
        }

        touchX = 0;
        touchY = 0;
    }


    public void onClick(){

        if(isExtend){
            //展开状态
            isExtend = false;
            if(getX()==0){
                iv_moveball.setVisibility(View.VISIBLE);
                layout_leftextend.setVisibility(View.GONE);
            }else {
                setX(screenWidth - width);
                iv_moveball.setVisibility(View.VISIBLE);
                layout_rightextend.setVisibility(View.GONE);
            }
        }else {
            //收缩状态,现在展开
            isExtend = true;

            if(getX()==0){
                LogUtils.e( "左边展开");
                iv_moveball.setVisibility(View.INVISIBLE);
                layout_leftextend.setVisibility(View.VISIBLE);
            }else {
                LogUtils.e( "右边展开");
                isRightEx = true;
                setX(screenWidth-extendWidth);
                isFFF = true;
                iv_moveball.setVisibility(View.INVISIBLE);
                layout_rightextend.setVisibility(View.VISIBLE);
            }

//            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//                @Override
//                public void onAnimationUpdate(ValueAnimator animation) {
//                    float v = (float) animation.getAnimatedValue();
//                    layout_leftextend.setScaleX(v);
//                    layout_leftextend.setScaleY(v);
//                    layout_rightextend.setScaleX(v);
//                    layout_rightextend.setScaleY(v);
//                }
//
//            });
//            animator.addListener(new AnimatorListenerAdapter() {
//                @Override
//                public void onAnimationEnd(Animator animation, boolean isReverse) {
//
//                }
//            });
//            animator.start();

        }


    }


    OnClick onClick;
    public interface OnClick{
        void onClick(View view);
    }
    public void setOnClick(OnClick onClick){
        this.onClick = onClick;
    }

}
