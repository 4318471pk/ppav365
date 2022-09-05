package com.live.fox.common;

import android.content.Context;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bigkoo.convenientbanner.holder.Holder;
import com.live.fox.R;
import com.live.fox.entity.response.RedBagRainBean;
import com.live.fox.utils.GlideUtils;
import com.live.fox.utils.TimeUtils;

public class RedBagBannerHolder implements Holder<RedBagRainBean> {

    interface OnClickListener {
        void onClick();
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.mOnClickListener = onClickListener;
    }

    private ImageView ivRedBagRainIcon;
    private TextView tvRedBagRainTime;
    private OnClickListener mOnClickListener;
    private CountDownTimer mCountdownTimer;
    private long millisInFuture;

    @Override
    public View createView(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_red_bag_rain_banner, null);
        ivRedBagRainIcon = view.findViewById(R.id.ivRedBagRainIcon);
        tvRedBagRainTime = view.findViewById(R.id.tvRedBagRainTime);
        return view;
    }

    private long nh = 1000 * 60 * 60;
    private long nm = 1000 * 60;
    private long ns = 1000;

    @Override
    public void UpdateUI(Context context, int position, RedBagRainBean banner) {
        GlideUtils.loadImage(context, banner.getIconUrl(), ivRedBagRainIcon);
        ivRedBagRainIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RedBagBannerHolder.this.mOnClickListener.onClick();
            }
        });
        tvRedBagRainTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RedBagBannerHolder.this.mOnClickListener.onClick();
            }
        });
        //        millisInFuture = 1637155800000L - 1637154000000L;
        millisInFuture = banner.getLatelyStartTime() - TimeUtils.getCurrentTime();
        if (mCountdownTimer != null) {
            mCountdownTimer.cancel();
        }
        if (millisInFuture >= 0) {
            // 计算差多少小时
            long hour = millisInFuture / nh;
            // 计算差多少分钟
            long min = millisInFuture % nh / nm;
            // 计算差多少秒//输出结果
            long s = millisInFuture % nh % nm / ns;
            String time = hour + ":" + min + ":" + s;
            tvRedBagRainTime.setText(time);
            mCountdownTimer = new CountDownTimer(millisInFuture, 1000) {

                @Override
                public void onTick(long millisUntilFinished) {
                    // 计算差多少小时
                    long hour = millisUntilFinished / nh;
                    // 计算差多少分钟
                    long min = millisUntilFinished % nh / nm;
                    // 计算差多少秒//输出结果
                    long s = millisUntilFinished % nh % nm / ns;
                    // long sec = diff % nd % nh % nm / ns;
                    String hourStr = String.valueOf(hour);
                    if (hour < 10) {
                        hourStr = "0" + hourStr;
                    }
                    String minStr = String.valueOf(min);
                    if (min < 10) {
                        minStr = "0" + minStr;
                    }
                    String sStr = String.valueOf(s);
                    if (s < 10) {
                        sStr = "0" + sStr;
                    }
                    String time = hourStr + ":" + minStr + ":" + sStr;
                    tvRedBagRainTime.setText(time);
                }

                @Override
                public void onFinish() {
                    tvRedBagRainTime.setText("");
                }

            };
        } else {
            tvRedBagRainTime.setText("");
        }
    }


}
