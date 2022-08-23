package com.live.fox.view;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.live.fox.R;
import com.live.fox.entity.ReceiveGiftBean;
import com.live.fox.utils.DensityUtils;
import com.live.fox.utils.GlideUtils;
import com.live.fox.utils.LogUtils;
import com.live.fox.utils.ResourceUtils;
import com.live.fox.utils.device.ScreenUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


/**
 * 礼物特效
 */
public class GiftEffectFragment extends Fragment {

    private final String TAG = getClass().getSimpleName();
    private View m_viewRooter = null;

    //Fragment 处于销毁过程中标志位
    private boolean m_isDestorying = false;
    //Fragment 处于OnStop标志位
    private boolean m_isStoping = false;

    private RelativeLayout rl_root;
    private LinearLayout ll_gift_group;

    private TranslateAnimation outAnim;
    private TranslateAnimation inAnim;
    private NumberAnim giftNumberAnim;
    private LuckSamllAnim luckSamllAnim;
    private LuckBigAnim luckBigAnim;
    private Timer clearAnimTimer;
    private AnimationDrawable luck_anima;
    private AnimationDrawable luck_good_anima;

    private List<ReceiveGiftBean> giftsBigCache;  //礼物列表
    private List<ReceiveGiftBean> currentBigGift; //当前播放列表
    //private List<ReceiveGiftBean> silderSmallGiftCache;  //侧边礼物列表
    //private List<ReceiveGiftBean> currentSilderGift; //当前播放侧边礼列表

    private List<String> tagList;

    private Context mContext;

    public static SoundPool mSound = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        giftsBigCache = new ArrayList<>();
        currentBigGift = new ArrayList<>();
//        silderSmallGiftCache = new ArrayList<>();
//        currentSilderGift = new ArrayList<>();
        // 开啓定时清理礼物列表
        clearTiming();
        // 初始化数字动画
        giftNumberAnim = new NumberAnim();

        // 初始化幸运礼物中奖倍数动画
        luckSamllAnim = new LuckSamllAnim();
        luckBigAnim = new LuckBigAnim();
        // 礼物进入时动画
        inAnim = (TranslateAnimation) AnimationUtils.loadAnimation(getActivity(), R.anim.gift_in);
        // 礼物退出时动画
        outAnim = (TranslateAnimation) AnimationUtils.loadAnimation(getActivity(), R.anim.gift_out);

        tagList = new ArrayList<>();

        //声音文件
//        mSound.load(getActivity(), R.raw.gold_in, 1);// 1
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        m_viewRooter = inflater.inflate(R.layout.gifteffect_fragment, container, false);
        rl_root = m_viewRooter.findViewById(R.id.rl_root);
        ll_gift_group = m_viewRooter.findViewById(R.id.ll_gift_group);
        this.mContext = getActivity();
        return m_viewRooter;
    }


    public void showOnSlide(final ReceiveGiftBean giftBean) {
        if (giftBean.getGift() == null) {
            return;
        }
        LogUtils.e("addGift2Show " + giftBean.getGift().toString());
        startPlaySilder(giftBean);
    }

    private void startPlaySilder(final ReceiveGiftBean giftBean) {
        final String tag = giftBean.getUid() + giftBean.getGid() + giftBean.getCount();
        View newGiftView = ll_gift_group.findViewWithTag(tag);
        // 是否有该tag类型的礼物
        if (newGiftView == null) {
            // 判断礼物列表是否已经有3个了，如果有那么删除掉一个没更新过的, 然后再添加新进来的礼物，始终保持只有3个
            if (ll_gift_group.getChildCount() >= 2) {
                // 获取前2个元素的最后更新时间
                View giftView01 = ll_gift_group.getChildAt(0);
//                ImageView iv_gift01 = (ImageView) giftView01.findViewById(R.id.iv_gift);
//                long lastTime1 = (long) iv_gift01.getTag();
                View giftView02 = ll_gift_group.getChildAt(1);
//                ImageView iv_gift02 = (ImageView) giftView02.findViewById(R.id.iv_gift);
//                long lastTime2 = (long) iv_gift02.getTag();
                LinearLayout luck_star01 = giftView01.findViewById(R.id.luck_star);
                long lastTime1 = (long) luck_star01.getTag();
                LinearLayout luck_star02 = giftView02.findViewById(R.id.luck_star);
                long lastTime2 = (long) luck_star02.getTag();
                // 如果第二个View显示的时间比较长
                if (lastTime1 > lastTime2) {
                    removeGiftView(1);
                } else {// 如果第一个View显示的时间长
                    removeGiftView(0);
                }
            }

            // 获取礼物
            newGiftView = getNewGiftView(giftBean);
            if (newGiftView == null) return;
            ll_gift_group.addView(newGiftView);
            tagList.add(tag);

            //幸运礼物中奖特效会用到
            final LinearLayout ll_left = newGiftView.findViewById(R.id.ll_left);

            //播放动画
            newGiftView.startAnimation(inAnim);
            final TextView mtv_giftNum = newGiftView.findViewById(R.id.mtv_giftNum);
            inAnim.setAnimationListener(new Animation.AnimationListener() {

                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    giftNumberAnim.showAnimator(mtv_giftNum);

                    //如果幸运礼物中将 则在礼物动画位移完成后展示幸运礼物中奖的动画效果
                    if (giftBean.getLuck() > 0) {
                        sendLuckGiftEffect(giftBean.getLuck(), tag, ll_left);
                    }
                }
            });
        } else {
            // 更新标识，记录最新修改的时间，用于回收判断

            LinearLayout luck_star = newGiftView.findViewById(R.id.luck_star);
            luck_star.setTag(System.currentTimeMillis());

            // 更新标识，更新记录礼物个数
            TextView mtv_giftNum = newGiftView.findViewById(R.id.mtv_giftNum);
            // 递增
            int giftCount = (int) mtv_giftNum.getTag() + giftBean.getCount();
            mtv_giftNum.setText("x" + giftCount);
            mtv_giftNum.setTag(giftCount);
            giftNumberAnim.showAnimator(mtv_giftNum);

            //如果幸运礼物中将 则展示幸运礼物中奖的动画效果
            if (giftBean.getLuck() > 0) {
                sendLuckGiftEffect(giftBean.getLuck(), tag, newGiftView.findViewById(R.id.ll_left));
            }
        }
    }


    //展示幸运礼物中奖的动画效果
    public void sendLuckGiftEffect(int luck, String tag, View objView) {
        View view = rl_root.findViewWithTag(tag + "luck");
        //如果有 则先删除上一次的动画
        if (view != null) {
            rl_root.removeView(view);
        }

        //低倍率中奖和高倍率中奖是2个view
        if (luck > 0 && luck < 500) {
            view = LayoutInflater.from(mContext).inflate(R.layout.liveroom_luckgift_small, null);
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    DensityUtils.dp2px(mContext, 24));
            lp.topMargin = DensityUtils.dp2px(mContext, 170);
            lp.addRule(RelativeLayout.CENTER_HORIZONTAL);
            view.setLayoutParams(lp);
        } else if (luck >= 500) {
            view = LayoutInflater.from(mContext).inflate(R.layout.liveroom_luckgift_big, null);
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(DensityUtils.dp2px(mContext, 140),
                    DensityUtils.dp2px(mContext, 120));
            lp.topMargin = DensityUtils.dp2px(mContext, 170);
            lp.addRule(RelativeLayout.CENTER_HORIZONTAL);
            view.setLayoutParams(lp);
        }
        view.setTag(tag + "luck");
        rl_root.addView(view);

        //View初始化操作
        LinearLayout llLuck = (LinearLayout) view.findViewById(R.id.ll_luck);
        ImageView ivLuck1 = (ImageView) view.findViewById(R.id.iv_luck1);
        ImageView ivLuck2 = (ImageView) view.findViewById(R.id.iv_luck2);
        ImageView ivLuck3 = (ImageView) view.findViewById(R.id.iv_luck3);
        ImageView ivLuck4 = (ImageView) view.findViewById(R.id.iv_luck4);

        ivLuck1.setVisibility(View.GONE);
        ivLuck2.setVisibility(View.GONE);
        ivLuck3.setVisibility(View.GONE);
        ivLuck4.setVisibility(View.GONE);

        //将中奖倍率替换成对应的数字图片
        int tmpLuck = luck;
        int[] luckAry = new int[(tmpLuck + "").length()];
        for (int i = luckAry.length - 1; i >= 0; i--) {
            luckAry[i] = tmpLuck % 10;
            tmpLuck /= 10;
        }
        ResourceUtils resourceUtils = new ResourceUtils();
        int[] luckNumArray = resourceUtils.getResourcesIdByIndex(R.array.luckNum, luckAry);
        for (int i = 0; i < luckNumArray.length; i++) {
            int resourceId = luckNumArray[i];
            if (i == 0) {
                ivLuck1.setVisibility(View.VISIBLE);
                ivLuck1.setImageResource(resourceId);
            } else if (i == 1) {
                ivLuck2.setVisibility(View.VISIBLE);
                ivLuck2.setImageResource(resourceId);
            } else if (i == 2) {
                ivLuck3.setVisibility(View.VISIBLE);
                ivLuck3.setImageResource(resourceId);
            } else {
                ivLuck4.setVisibility(View.VISIBLE);
                ivLuck4.setImageResource(resourceId);
            }
        }

        //如何<500 则展示低倍率效果 >=500 展示高倍率效果
        if (luck > 0 && luck < 500) {
            luckSamllAnim.showAnimator(view, objView, tag);
        } else if (luck >= 500) {
            luckBigAnim.showAnimator(view, objView, tag);
            //如果大于500倍 则自己这里播放声音 手机震动
//            if(giftBean.getUid().equals(UserManager.ins().getLoginUser().getUid())){
//            mSound.play(1, 1, 1, 0, 0, 1);
//            Vibrator vib = (Vibrator) mContext.getSystemService(Service.VIBRATOR_SERVICE);
//            vib.vibrate(200);
//            }
        }
    }


    /**
     * 获取礼物
     */
    private View getNewGiftView(ReceiveGiftBean giftBean) {
        LogUtils.e("giftbean anim " + giftBean.toString());
        if (giftBean.getGid() == 150) {
            return null;
        }
        // 添加标识, 该view若在layout中存在，就不在生成（用于findViewWithTag判断是否存在）
        String tag = giftBean.getUid() + giftBean.getGid() + giftBean.getCount();

        if (isValidContextForGlide(requireContext())) return null;
        View giftView = LayoutInflater.from(requireContext()).inflate(R.layout.liveroom_side_giftshow, null);
        giftView.setTag(tag);
        ImageView cv_send_gift_userIcon = giftView.findViewById(R.id.cv_send_gift_userIcon);

        TextView tv_send_gift_sender = giftView.findViewById(R.id.tv_send_gift_sender);
        if (giftBean.chatHide == 1) {
            cv_send_gift_userIcon.setImageResource(R.drawable.ic_shenmi);
            tv_send_gift_sender.setText(getString(R.string.mysteriousMan));
        } else {
            GlideUtils.loadDefaultCircleImage(getActivity(), giftBean.getAvatar(), cv_send_gift_userIcon);
            tv_send_gift_sender.setText(giftBean.getNickname());
        }

        TextView tv_send_gift_receiver = giftView.findViewById(R.id.tv_send_gift_receiver);
        tv_send_gift_receiver.setText(getString(R.string.sendLiao) + giftBean.getGift().getGname());

        // 添加标识, 记录生成时间，回收时用于判断是否是最新的，回收最老的
        ImageView iv_gift = giftView.findViewById(R.id.iv_gift);

        LinearLayout luck_star = giftView.findViewById(R.id.luck_star);
        luck_star.setTag(System.currentTimeMillis());

        // 此处使用Glide会报错 因爲ImageView设置了tag
        GlideUtils.loadImage(mContext, giftBean.getGift().getCover(), R.color.transparent, R.color.transparent, true, iv_gift);

        // 添加标识，记录礼物个数
        TextView mtv_giftNum = giftView.findViewById(R.id.mtv_giftNum);
        mtv_giftNum.setText("x" + giftBean.getCombo() * giftBean.getCount());
        mtv_giftNum.setTag(giftBean.getCombo() * giftBean.getCount());

        //礼物之间的上下间隔
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.topMargin = 10;
        giftView.setLayoutParams(lp);

        return giftView;
    }

    /**
     * 判断 context 是否能用
     */
    private boolean isValidContextForGlide(final Context context) {
        if (context == null) {
            return true;
        }
        if (context instanceof Activity) {
            final Activity activity = (Activity) context;
            return activity.isDestroyed() || activity.isFinishing();
        }
        return false;
    }

    /**
     * 定时清理礼物列表信息
     */
    private void clearTiming() {

        clearAnimTimer = new Timer();
        clearAnimTimer.schedule(new TimerTask() {

            @Override
            public void run() {

                if (ll_gift_group == null) {
                    return;
                }
                int childCount = ll_gift_group.getChildCount();
                long nowTime = System.currentTimeMillis();
                for (int i = 0; i < childCount; i++) {

                    View childView = ll_gift_group.getChildAt(i);
                    LinearLayout luck_star = childView.findViewById(R.id.luck_star);
                    long lastUpdateTime = (long) luck_star.getTag();

                    // 更新超过3秒就刷新
                    if (nowTime - lastUpdateTime >= 3000) {
                        removeGiftView(i);
                    }
                }
            }
        }, 0, 3000);
    }


    /**
     * 移除礼物列表里的giftView
     */
    private void removeGiftView(final int index) {
        // 移除列表，外加退出动画
        final View removeView = ll_gift_group.getChildAt(index);

        tagList.remove(removeView.getTag().toString());
        final View luckGiftView = rl_root.findViewWithTag(removeView.getTag().toString() + "luck");


        // 开啓动画，因爲定时原因，所以可能是在子线程
        getActivity().runOnUiThread(new Runnable() {

            @Override
            public void run() {
                removeView.startAnimation(outAnim);

                if (luckGiftView != null) {
                    luckGiftView.setVisibility(View.GONE);
                }
            }
        });

        //退出动画监听
        outAnim.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                ll_gift_group.removeView(removeView);
                if (removeView != null) {
                    removeView.destroyDrawingCache();
                }

                luckSamllAnim.cancelAnim();
                luckBigAnim.cancelAnim();

                if (removeView != null) {
                    rl_root.removeView(luckGiftView);
                }

            }
        });
    }


    /**
     * 送的礼物后面的数字动画
     */
    public class NumberAnim {

        private Animator lastAnimator;

        public void showAnimator(View v) {

            if (lastAnimator != null) {
                lastAnimator.removeAllListeners();
                lastAnimator.end();
                lastAnimator.cancel();
            }

            ObjectAnimator animScaleX = ObjectAnimator.ofFloat(v, "scaleX", 1.6f, 1.0f);
            ObjectAnimator animScaleY = ObjectAnimator.ofFloat(v, "scaleY", 1.6f, 1.0f);
            //设置中心点
            v.setPivotX(v.getWidth() / 2);
            v.setPivotY(v.getHeight() / 2);
            AnimatorSet animSet = new AnimatorSet();
            animSet.setDuration(800);
            animSet.setInterpolator(new OvershootInterpolator());
            animSet.playTogether(animScaleX, animScaleY);
            lastAnimator = animSet;
            animSet.start();
        }
    }


    /**
     * 幸运礼物中奖的小倍率动画效果(中奖<500倍时用这种动画)
     */
    public class LuckSamllAnim {

        private Animator luckAnimator;

        public void showAnimator(View v, View pointView, String tag) {

            //先取消动画
            cancelAnim();

            v.setVisibility(View.VISIBLE);

//            LogUtils.e("LuckAnim S View W" + v.getWidth() + ",H" + v.getHeight() + ",x" + v.getX() + ",y" + v.getY());
//            LogUtils.e("LuckAnim S pointView W" + pointView.getWidth() + ",H" + pointView.getHeight() + ",x" + pointView.getX() + ",y" + pointView.getY());

            int xOff = DensityUtils.dp2px(mContext, 8);
            int yOff = DensityUtils.dp2px(mContext, 12); //View控件高度的一半
            int childPos = -1;
            for (int i = 0; i < tagList.size(); i++) {
                if (tagList.get(i).equals(tag)) {
                    childPos = i;
                    break;
                }
            }
//            LogUtils.e("LuckAnim S childPos" + childPos);

            //不知道什么原因View的xy和宽高都是0 所以要加偏移补充
            float tranX = -(ScreenUtils.getScreenWidth(mContext) / 2 - pointView.getWidth() / 2) + xOff;
            float tranY = pointView.getY() + pointView.getHeight() - yOff + childPos * DensityUtils.dp2px(mContext, 60);


            ObjectAnimator animTranX = ObjectAnimator.ofFloat(v, "translationX", 0, tranX);
            ObjectAnimator animTranY = ObjectAnimator.ofFloat(v, "translationY", 0, tranY);
            ObjectAnimator animAppha = ObjectAnimator.ofFloat(v, "alpha", 0f, 1.0f);

            AnimatorSet animSet = new AnimatorSet();
            animSet.setDuration(100);
            animSet.playTogether(animTranX, animTranY, animAppha);
            luckAnimator = animSet;
            animSet.start();
        }

        public void cancelAnim() {
            if (luckAnimator != null) {
                luckAnimator.removeAllListeners();
                luckAnimator.end();
                luckAnimator.cancel();
            }
        }
    }


    /**
     * 幸运礼物中奖的大倍率效果(中奖>=500倍时用这种动画)
     */
    public class LuckBigAnim {

        private Animator luckTranAnimator;
        private Animator luckAnimator;
        private Animator luckBgRotationAnimator;

        public void showAnimator(final View v, final View pointView, final String tag) {

            //先取消动画
            cancelAnim();

            v.setVisibility(View.INVISIBLE);

            //(此段不要删除) 通过view.layout()改变位置改变其位置，但不知道爲啥没效果
//            float tX = pointView.getX() + pointView.getWidth() / 2 - v.getWidth() / 2 + DensityUtils.dp2px(mContext, 170);
//            float tY = pointView.getY() + pointView.getHeight() / 2 - v.getHeight() / 2 + DensityUtils.dp2px(mContext, 170);
//
//            LogUtils.e("LuckAnim rlBigLuck W" + tX + "，" + (int) tY);
//            v.layout(200, (int)tY, v.getWidth() + (int) tX, v.getHeight() + (int) tY);
//            v.setVisibility(View.VISIBLE);

//            LogUtils.e("LuckAnim View W" + v.getWidth() + ",H" + v.getHeight() + ",x" + v.getX() + ",y" + v.getY());
//            LogUtils.e("LuckAnim pointView W" + pointView.getWidth() + ",H" + pointView.getHeight() + ",x" + pointView.getX() + ",y" + pointView.getY());


            int xOff = DensityUtils.dp2px(mContext, 16);
            int yOff = DensityUtils.dp2px(mContext, 70); //View控件高度的一半
            int childPos = -1;
            for (int i = 0; i < tagList.size(); i++) {
                if (tagList.get(i).equals(tag)) {
                    childPos = i;
                    break;
                }
            }
//            LogUtils.e("LuckAnim childPos" + childPos);
            //通过位移动画改变位置 不知道什么原因View的xy和宽高都是0 所以要加偏移补充 （试过使用view.layout()改变位置改变其位置，但不知道爲啥没效果）
            float tranX = -(ScreenUtils.getScreenWidth(mContext) / 2 - pointView.getWidth() / 2) + xOff;
            float tranY = pointView.getY() + pointView.getHeight() - yOff + childPos * DensityUtils.dp2px(mContext, 60);
//            LogUtils.e("LuckAnim tranX" + tranX + ",tranY" + tranY);
            ObjectAnimator animTranX = ObjectAnimator.ofFloat(v, "translationX", 0, tranX);
            ObjectAnimator animTranY = ObjectAnimator.ofFloat(v, "translationY", 0, tranY);
            ObjectAnimator animAppha = ObjectAnimator.ofFloat(v, "alpha", 0f, 1.0f);

            AnimatorSet tranAnimSet = new AnimatorSet();
            tranAnimSet.setDuration(15);
            tranAnimSet.playTogether(animTranX, animTranY);
            luckTranAnimator = tranAnimSet;
            tranAnimSet.start();

            tranAnimSet.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {

                }

                @Override
                public void onAnimationEnd(Animator animator) {
                    //控件位移完成后 开始幸运礼物中奖的大倍率动画
                    startBigLuckGiftAnim(v, pointView, tag);
                }

                @Override
                public void onAnimationCancel(Animator animator) {

                }

                @Override
                public void onAnimationRepeat(Animator animator) {

                }
            });
        }

        //开始幸运礼物中奖的大倍率动画
        public void startBigLuckGiftAnim(View v, View pointView, String tag) {
            v.setVisibility(View.VISIBLE);
            final ImageView ivBigluckBg = v.findViewById(R.id.iv_bigluckbg);
            final LinearLayout llBigluck = v.findViewById(R.id.ll_luck);
            ivBigluckBg.setVisibility(View.INVISIBLE);
            llBigluck.setVisibility(View.INVISIBLE);

            ObjectAnimator animScaleX = ObjectAnimator.ofFloat(v, "scaleX", 6.0f, 1.0f);
            ObjectAnimator animScaleY = ObjectAnimator.ofFloat(v, "scaleY", 6.0f, 1.0f);
            ObjectAnimator animAppha = ObjectAnimator.ofFloat(v, "alpha", 0f, 1.0f);

            AnimatorSet animSet1 = new AnimatorSet();
            animSet1.playTogether(animScaleX, animScaleY);

            AnimatorSet animSet = new AnimatorSet();
            animSet.playTogether(animScaleX, animScaleY, animAppha);
            animSet.setDuration(100);
            luckAnimator = animSet;
            animSet.start();

            final ObjectAnimator animRotation = ObjectAnimator.ofFloat(ivBigluckBg, "rotation", 0f, 360f);
            animRotation.setRepeatCount(-1);

            luckAnimator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {
//                    LogUtils.e("LuckAnim onAnimationStart");
                }

                @Override
                public void onAnimationEnd(Animator animator) {
//                    LogUtils.e("LuckAnim onAnimationEnd");
                    //缩小动画完成后 显示背景和数字 并开始背景动画的旋转
                    llBigluck.setVisibility(View.VISIBLE);
                    ivBigluckBg.setVisibility(View.VISIBLE);
                    AnimatorSet animSet = new AnimatorSet();
                    animSet.setDuration(1600);
                    animSet.play(animRotation);
                    animSet.setInterpolator(new LinearInterpolator());
                    luckBgRotationAnimator = animator;
                    animSet.start();
                }

                @Override
                public void onAnimationCancel(Animator animator) {
//                    LogUtils.e("LuckAnim onAnimationCancel");
                }

                @Override
                public void onAnimationRepeat(Animator animator) {
//                    LogUtils.e("LuckAnim onAnimationRepeat");

                }
            });
        }


        public void cancelAnim() {
            if (luckAnimator != null) {
                luckAnimator.removeAllListeners();
                luckAnimator.end();
                luckAnimator.cancel();
            }
            if (luckTranAnimator != null) {
                luckTranAnimator.removeAllListeners();
                luckTranAnimator.end();
                luckTranAnimator.cancel();
            }
            if (luckBgRotationAnimator != null) {
                luckBgRotationAnimator.removeAllListeners();
                luckBgRotationAnimator.end();
                luckBgRotationAnimator.cancel();
            }
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        clearAnimTimer.cancel();
    }

}
