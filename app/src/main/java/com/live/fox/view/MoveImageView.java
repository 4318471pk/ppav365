package com.live.fox.view;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.live.fox.utils.device.ScreenUtils;


/**
 * 跟随手指位置移动而移动ImageView
 *
 * 1.xml里
 * <com.xglive.girlstribe.ui.view.MoveImageView
 android:id="@+id/iv_tree"
 android:layout_width="80dp"
 android:layout_height="80dp"
 android:src="@drawable/ic_tree"/>
   2.代码里
 ivTree = findViewById(R.id.iv_tree);
 ivTree.initView(this, x, y, viewW, viewH);
 */
public class MoveImageView extends ImageView {

    /**
     * View的宽高
     */
    private float width;
    private float height;

    /**
     * 触摸点相对于View的坐标
     */
    private float touchX;
    private float touchY;
    private float touchXPos;
    private float touchYPos;
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


    public MoveImageView(Context context) {
        super(context);
        this.context = context;
    }

    public MoveImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        //获取状态栏高度 如果隐藏 则加此值 如果未隐藏 则设置为0
//        barHeight = BarUtils.getStatusBarHeight();
        barHeight = 0;
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:

                clearAnimation();
                touchX = event.getX();
                touchY = event.getY();
                touchXPos = getX();
                touchYPos = getY();
                return true;
            case MotionEvent.ACTION_MOVE:
                /*
                    这里Y轴要减掉barHeight
                    因为计算时，View的默认坐标原点在状态栏下面
                    而屏幕的坐标是包含着状态栏的
                 */

                float nowX = event.getRawX() - touchX;
                float nowY = event.getRawY() - touchY - barHeight;
                nowX = nowX < 0 ? 0 : (nowX + width > screenWidth) ? (screenWidth - width) : nowX;
                nowY = nowY < 0 ? 0 : (nowY + height > screenHeight) ? (screenHeight - height) : nowY;
                this.setY(nowY);
                this.setX(nowX);
                invalidate();
                return true;
            case MotionEvent.ACTION_UP:

                //当从点击到弹起小于半秒的时候,则判断为点击,如果超过则不响应点击事件
                if(Math.abs(touchXPos-getX())<10&&Math.abs(touchYPos-getY())<10) {
                    onClick.onClick();
                }

                //这里做动画贴边效果
                toEdge();
                return true;
            default:
                return super.onTouchEvent(event);
        }
    }



    public void initView(Context context, int x, int y, int viewWidth, int viewHeight){
        this.setX(x);
        this.setY(y);
        invalidate();

        this.context = context;
        screenWidth = ScreenUtils.getScreenWidth(context);
        screenHeight = ScreenUtils.getScreenHeight(context);
        width = viewWidth;
        height = viewHeight;

        toEdge();
    }


    //做动画贴边效果
    public void toEdge(){
        float centerX = getX() + width / 2;
        if (centerX > ScreenUtils.getScreenWidth(context) / 2) {
            ObjectAnimator.ofFloat(this, "translationX",
                    getX(), screenWidth - width)
                    .setDuration(500)
                    .start();
        } else {
            ObjectAnimator.ofFloat(this, "translationX",
                    getX(), 0)
                    .setDuration(500)
                    .start();
        }
        touchX = 0;
        touchY = 0;
    }



    OnClick onClick;
    public interface OnClick{
        void onClick();
    }
    public void setOnClick(OnClick onClick){
        this.onClick = onClick;
    }
}
