package com.live.fox.view.danmu2;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.TextView;

import com.live.fox.R;
import com.live.fox.utils.ConvertUtils;
import com.live.fox.utils.IntentUtils;
import com.live.fox.utils.LogUtils;
import com.live.fox.utils.Utils;
import com.live.fox.utils.device.ScreenUtils;
import com.live.fox.view.danmukux.DanmuEntity;


/**
 * Created  cxf on 2017/8/25.
 * 弹幕
 */

public class DanmuViewHolder extends AbsViewHolder {

    private static final float SPEED = 0.2f;//弹幕的速度，这个值越小，弹幕走的越慢
    private static final int MARGIN_TOP = ConvertUtils.dp2px(0);
    private static final int SPACE = ConvertUtils.dp2px(0);
    private static final int DP_15 = ConvertUtils.dp2px(15);
    //    private ImageView mAvatar;
//    private TextView mName;
    private TextView mContent;
    private int mScreenWidth;//屏幕宽度
    private int mWidth;//控件的宽度
    private ValueAnimator mAnimator;
    private ValueAnimator.AnimatorUpdateListener mUpdateListener;
    private Animator.AnimatorListener mAnimatorListener;
    private boolean mCanNext;//是否可以有下一个
    private boolean mIdle;//是否空闲
    private ActionListener mActionListener;
    private int mLineNum;

    public DanmuViewHolder(Context context, ViewGroup parentView) {
        super(context, parentView);
        LogUtils.e("DanmuViewHolder");
    }

    @Override
    protected int getLayoutId() {
        LogUtils.e("12312321");
        return R.layout.item_piaopingad_danmu;
    }

    @Override
    public void init() {
        LogUtils.e("init");
        mContent = (TextView) findViewById(R.id.content);
        mScreenWidth = ScreenUtils.getScreenWidth(Utils.getApp());
        mUpdateListener = animation -> {
            float v = (float) animation.getAnimatedValue();
            mContentView.setX(v);
            if (!mCanNext && v <= mScreenWidth - mWidth - DP_15) {
                mCanNext = true;
                if (mActionListener != null) {
                    mActionListener.onCanNext(mLineNum);
                }
            }
        };
        mAnimatorListener = new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                removeFromParent();
                mIdle = true;
                if (mActionListener != null) {
                    mActionListener.onAnimEnd(DanmuViewHolder.this);
                }
            }
        };
    }

    public void show(DanmuEntity bean, int lineNum) {
        mLineNum = lineNum;

        mContent.setText(bean.getContent());
        mContent.setOnClickListener(view -> IntentUtils.toBrowser(mContext, bean.getNickname()));
        mCanNext = false;
        mContentView.measure(0, 0);
        mWidth = mContentView.getMeasuredWidth();
        LogUtils.e("mWidth:" + mWidth);
        mContentView.setX(mScreenWidth);
        mContentView.setY(MARGIN_TOP + lineNum * SPACE);
        addToParent();
        mAnimator = ValueAnimator.ofFloat(mScreenWidth, -mWidth);
        mAnimator.addUpdateListener(mUpdateListener);
        mAnimator.setInterpolator(new LinearInterpolator());
        mAnimator.setDuration((int) ((mScreenWidth + mWidth) / SPEED));
        mAnimator.addListener(mAnimatorListener);
        mAnimator.start();
        LogUtils.e("开始飘屏");
    }

    public boolean isIdle() {
        return mIdle;
    }

    public void setIdle(boolean idle) {
        mIdle = idle;
    }

    public void setActionListener(ActionListener actionListener) {
        mActionListener = actionListener;
    }

    public void release() {
        if (mAnimator != null) {
            mAnimator.cancel();
        }
        removeFromParent();
        mActionListener = null;
    }

    public interface ActionListener {
        void onCanNext(int lineNum);

        void onAnimEnd(DanmuViewHolder vh);
    }
}
