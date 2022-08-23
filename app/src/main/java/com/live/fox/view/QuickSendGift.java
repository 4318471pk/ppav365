package com.live.fox.view;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.live.fox.R;
import com.live.fox.entity.Gift;


/**
 * User: ljx
 * Date: 2017/08/17
 * Time: 09:47
 */
public class QuickSendGift extends RelativeLayout {

    Context context;

    private Gift currentGift; //当前礼物

    private RoundProgressBar progressBar;  //快捷礼物倒计时
    private TextView sdGiftIcon; //连发按钮
//    private ImageView sdGiftIcon; //礼物图标
//
//    private TextView tvCoinCount; //剩余币值数量
//
//    private LinearLayout llNumContainer; //送礼物数量

    private Handler handler = new Handler();

    private AnimatorSet animatorSet;

    private GiftPanelView.OnGiftActionListener giftListener;

    private OnComboClickListener onComboClickListener;

    public QuickSendGift(Context context) {
        this(context, null);
    }

    public QuickSendGift(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public QuickSendGift(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onComboClickListener != null) {
                    onComboClickListener.onComboClick();
                }
                if (animatorSet == null) {
                    ObjectAnimator scaleX = ObjectAnimator.ofFloat(sdGiftIcon, "scaleX", 1.0f, 1.1f, 1.0f);
                    ObjectAnimator scaleY = ObjectAnimator.ofFloat(sdGiftIcon, "scaleY", 1.0f, 1.1f, 1.0f);
                    AnimatorSet animatorSet = new AnimatorSet();
                    animatorSet.playTogether(scaleX, scaleY);
                    animatorSet.setDuration(100);
                    animatorSet.setInterpolator(new AccelerateDecelerateInterpolator());
                    QuickSendGift.this.animatorSet = animatorSet;
                }
                if (animatorSet != null && animatorSet.isRunning()) {
                    animatorSet.cancel();
                }
                animatorSet.start();
            }
        });
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        progressBar = findViewById(R.id.roundProgressBar);
        progressBar.setMax(100);
        progressBar.setProgress(100);

        sdGiftIcon = findViewById(R.id.sd_gift_icon);
//        tvCoinCount = findViewById(R.id.tv_coin_count);
//        llNumContainer = findViewById(R.id.ll_num_container);
    }

    public void startCountDownTimer(Gift gift) {
        if (gift == null) return;
        currentGift = gift;
        progressBar.setProgress(100);
        handler.removeCallbacks(timerRunnable);
        handler.postDelayed(timerRunnable, 25);
    }

    public void showQuickGift(Gift gift) {
        if (currentGift == null) return;
        currentGift = gift;
        setVisibility(View.VISIBLE);
//        tvCoinCount.setText(""+gift.getGoldCoin());
//        updateGiftIcon(currentGift);
    }

//    public void refreshCoinCount(String coin) {
//        if (tvCoinCount != null && Util.isLegal(coin)) {
//            tvCoinCount.setText(coin);
//        }
//    }


//    private void updateGiftIcon(GiftBean gift) {
////        String hotIcon = gift.getHotIcon();
////        if (TextUtils.isEmpty(hotIcon)) {
////            String giftIcon = GiftManager.getGiftManager(getContext()).getGiftFile(gift.getGiftId());
////            Uri giftUrl = Uri.parse("file://" + giftIcon);
////            sdGiftIcon.setImageURI(giftUrl);
////        } else {
////            sdGiftIcon.setImageURI(hotIcon);
////        }
//        GlideUtils.loadImage(context, gift.getCover(), sdGiftIcon);
//
//        llNumContainer.removeAllViews();
//        TextView xImageView = new TextView(getContext());
//        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(DensityUtils.dp2px(getContext(), 11), DensityUtils.dp2px(getContext(), 14));
//        xImageView.setLayoutParams(params);
//        xImageView.setText("x");
//        llNumContainer.addView(xImageView);
//
//        TextView numImageView = new TextView(getContext());
//        LinearLayout.LayoutParams numParams = new LinearLayout.LayoutParams(DensityUtils.dp2px(getContext(), 11), DensityUtils.dp2px(getContext(), 14));
//        numImageView.setLayoutParams(params);
//        numImageView.setText("1");
//        llNumContainer.addView(numImageView);
//
////        String countStr = String.valueOf(currentGift.getCount());
////        for (int i = 0; i < countStr.length(); i++) {
////            String subStr = countStr.substring(i, i + 1);
////            ImageView numImageView = new ImageView(getContext());
////            LinearLayout.LayoutParams numParams = new LinearLayout.LayoutParams(DensityUtils.dp2px(getContext(), 9), DensityUtils.dp2px(getContext(), 14));
////            numImageView.setLayoutParams(numParams);
////            numImageView.setImageResource(R.drawable.gift_num0 + Integer.valueOf(subStr));
////            llNumContainer.addView(numImageView);
////        }
//    }


    public int getCurrentProgress() {
        return progressBar != null ? progressBar.getProgress() : 0;
    }

    public void setGiftListener(GiftPanelView.OnGiftActionListener giftListener) {
        this.giftListener = giftListener;
    }

    public void setOnComboClickListener(OnComboClickListener onComboClickListener) {
        this.onComboClickListener = onComboClickListener;
    }

    Runnable timerRunnable = new Runnable() {
        @Override
        public void run() {
            if (progressBar != null) {
                progressBar.setProgress(progressBar.getProgress() - 1);
                if (progressBar.getProgress() > 0) {
                    handler.postDelayed(this, 25);
                } else {
                    if (countDownListener != null) {
                        countDownListener.countDownFinish();
                    }
                    setVisibility(GONE);
                    currentGift = null;
                }
            }
        }
    };

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        cancelTimer();
    }

    public void reset() {
        cancelTimer();
        currentGift = null;
        progressBar.setProgress(0);
    }

    public void cancelTimer() {
        handler.removeCallbacks(timerRunnable);
        setVisibility(GONE);
    }

    CountDownListener countDownListener;

    public void setCountDownListener(CountDownListener countDownListener) {
        this.countDownListener = countDownListener;
    }

    public interface CountDownListener {
        void countDownFinish();
    }

    public interface OnComboClickListener {
        void onComboClick();
    }
}
