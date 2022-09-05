package com.live.fox.ui.live;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.live.fox.R;
import com.live.fox.utils.ConvertUtils;
import com.live.fox.utils.GlideUtils;
import com.live.fox.utils.StringUtils;
import com.live.fox.utils.Utils;
import com.live.fox.utils.device.ScreenUtils;
import com.live.fox.view.danmu2.DanmuViewHolder;
import com.live.fox.view.danmukux.DanmuEntity;

import java.util.ArrayList;
import java.util.List;


public class PiaopingFragment extends Fragment {

    protected View rootView;

    private static final float SPEED = 0.3f;//弹幕的速度，这个值越小，弹幕走的越慢
    private static final int MARGIN_TOP = ConvertUtils.dp2px(0);
    private static final int SPACE = ConvertUtils.dp2px(0);
    private static final int DP_15 = ConvertUtils.dp2px(15);
    //    private ImageView mAvatar;
//    private TextView mName;
    private TextView mContentView;
    private int mScreenWidth;//屏幕宽度
    private int mWidth;//控件的宽度
    private ValueAnimator mAnimator;
    private ValueAnimator.AnimatorUpdateListener mUpdateListener;
    private Animator.AnimatorListener mAnimatorListener;
    private boolean mCanNext;//是否可以有下一个
    private boolean mIdle;//是否空闲
    private DanmuViewHolder.ActionListener mActionListener;
    private int mLineNum;
    private ImageView iv_ppdh;


    public static PiaopingFragment newInstance() {
        PiaopingFragment fragment = new PiaopingFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//		EventBus.getDefault().register(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.item_piaopingad_game, null, false);
//			initData(getArguments());
        setView(rootView);
        return rootView;
    }


    private void setView(View view) {

        mContentView = view.findViewById(R.id.content);
        iv_ppdh=view.findViewById(R.id.iv_ppdh);
        mContentView.setVisibility(View.GONE);
        iv_ppdh.setVisibility(View.GONE);
        mScreenWidth = ScreenUtils.getScreenWidth(Utils.getApp());
        mUpdateListener = animation -> {
            float v = (float) animation.getAnimatedValue();
            mContentView.setX(v);
            iv_ppdh.setX(v+ConvertUtils.dp2px(40));
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
                mContentView.setVisibility(View.GONE);
                mContentView.setX(0);
                iv_ppdh.setVisibility(View.GONE);
                iv_ppdh.setX(0);
                mIdle = true;
                mList.remove(0);
                if (mList.size() > 0) {
                    startPlay(mList.get(0), 0);
                }
                if (mActionListener != null) {
//                    mActionListener.onAnimEnd(DanmuViewHolder.this);
                }
            }
        };
    }


    List<DanmuEntity> mList = new ArrayList<>();

    public void show(DanmuEntity danmuEntity, int lineNum) {
        mLineNum = lineNum;

        if (mList.size() == 0) {
            mList.add(danmuEntity);
            startPlay(danmuEntity, 0);
        } else {
            mList.add(danmuEntity);
        }
    }

    public void startPlay(DanmuEntity bean, int lineNum) {
        if (mContentView == null || bean == null || StringUtils.isEmpty(bean.getContent())) {
            return;
        }
        if (bean.avatarRes != null) {
            mContentView.setBackgroundResource(bean.avatarRes);
            mContentView.setGravity(Gravity.CENTER);
        }
        mContentView.setText(bean.getContent());
//        float width = mContentView.getPaint().measureText(bean.getContent().toString());
//        width = width + ConvertUtils.dp2px(54);
//        LogUtils.e("wwww: " + mContentView.getWidth() + "," + width + "," + mScreenWidth);
//        LogUtils.e("wwww: " + mContentView.getWidth());

//        mContentView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                try {
//                    if (!StringUtils.isEmpty(bean.getNickname())) {
//                        IntentUtils.toBrowser(getActivity(), bean.getNickname());
//                    }
//                } catch (Exception e) {
//
//                }
//            }
//        });
        mCanNext = false;
        mContentView.measure(0, 0);
//        mContentView.startAnimation(inAnim);
        mWidth = mContentView.getMeasuredWidth();
//        LogUtils.e("mWidth:" + mWidth+","+mScreenWidth);
        if (mWidth < mScreenWidth) {
            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) mContentView.getLayoutParams();
            lp.width = mScreenWidth;
            mWidth=mScreenWidth;
            mContentView.setLayoutParams(lp);
        } else {
//            LogUtils.e("mWidth: 11111");
            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) mContentView.getLayoutParams();
            lp.width = mWidth;
            mContentView.setLayoutParams(lp);
        }
        mContentView.setX(mScreenWidth);
        mContentView.setY(MARGIN_TOP + lineNum * SPACE);
        mAnimator = ValueAnimator.ofFloat(mScreenWidth, -mWidth);
        mAnimator.addUpdateListener(mUpdateListener);
        mAnimator.setInterpolator(new LinearInterpolator());
        mAnimator.setDuration((int) ((mScreenWidth + mWidth) / SPEED));
        mAnimator.addListener(mAnimatorListener);
        mContentView.setVisibility(View.VISIBLE);
        iv_ppdh.setVisibility(View.VISIBLE);
        if (getActivity()!=null) {
            GlideUtils.loadImageByGif(getActivity(), R.drawable.pp_game_dh, iv_ppdh);
        }
        mAnimator.start();
//        LogUtils.e("开始飘屏");
    }

    public boolean isIdle() {
        return mIdle;
    }

    public void setIdle(boolean idle) {
        mIdle = idle;
    }

    public void setActionListener(DanmuViewHolder.ActionListener actionListener) {
        mActionListener = actionListener;
    }

//    public void release() {
//        if (mAnimator != null) {
//            mAnimator.cancel();
//        }
//        removeFromParent();
//        mActionListener = null;
//    }

    public interface ActionListener {
        void onCanNext(int lineNum);

        void onAnimEnd(DanmuViewHolder vh);
    }


}

