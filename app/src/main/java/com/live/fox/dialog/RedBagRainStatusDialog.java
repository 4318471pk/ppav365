package com.live.fox.dialog;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.DialogFragment;

import com.live.fox.R;
import com.live.fox.utils.ConvertUtils;

import java.text.DecimalFormat;

/**
 * 仿IOS的 SheetDialog
 * https://github.com/skyline1631/ActionDialog
 * <p>
 * ActionDialog dialog = new ActionDialog(this);
 * dialog.setTitle("Title");
 * dialog.setMessage("Message");
 * dialog.addAction("Default");
 * dialog.addAction("Destructive", true);
 * dialog.setEventListener(this);
 * dialog.show();
 */
public class RedBagRainStatusDialog extends DialogFragment {

    private ConstraintLayout clRedBagRainStatus;
    private TextView tvRedBagRainStatus;
    private ImageView ivRedBagRainStatus;
    private TextView tvRedBagRainGetSuc;
    private TextView tvRedBagRainGetMoney;

    public static RedBagRainStatusDialog newInstanceGetMoney(double money) {
        RedBagRainStatusDialog fragment = new RedBagRainStatusDialog();
        Bundle bundle = new Bundle();
        bundle.putInt("activityStatus", 3);
        bundle.putDouble("money", money);
        fragment.setArguments(bundle);
        return fragment;
    }

    public static RedBagRainStatusDialog newInstance(Integer activityStatus) {
        RedBagRainStatusDialog fragment = new RedBagRainStatusDialog();
        Bundle bundle = new Bundle();
        bundle.putInt("activityStatus", activityStatus);
        fragment.setArguments(bundle);
        return fragment;
    }

    public static RedBagRainStatusDialog newInstance(String activityStatus) {
        RedBagRainStatusDialog fragment = new RedBagRainStatusDialog();
        Bundle bundle = new Bundle();
        bundle.putString("activityStatusStr", activityStatus);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        return inflater.inflate(R.layout.dialog_red_bag_rain_status, container, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置样式
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.SplashDialog);
    }

    //如果需要修改大小 修改显示动画 重写此方法
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle bundle = getArguments();
        int activityStatus = bundle.getInt("activityStatus");
        if (activityStatus == 3) {
            getDialog().getWindow().setLayout(ConvertUtils.dp2px(271), ConvertUtils.dp2px(353));
        } else {
            getDialog().getWindow().setLayout(ConvertUtils.dp2px(206), ConvertUtils.dp2px(278));
        }
        getDialog().setCanceledOnTouchOutside(true);
        getDialog().setCancelable(true);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        initView(view);
    }

    public void initView(View view) {
        Bundle bundle = getArguments();
        int activityStatus = bundle.getInt("activityStatus");
        double money = bundle.getDouble("money", 0);
        String activityStatusStr = bundle.getString("activityStatusStr");
        clRedBagRainStatus = view.findViewById(R.id.clRedBagRainStatus);
        tvRedBagRainStatus = view.findViewById(R.id.tvRedBagRainStatus);
        ivRedBagRainStatus = view.findViewById(R.id.ivRedBagRainStatus);
        tvRedBagRainGetSuc = view.findViewById(R.id.tvRedBagRainGetSuc);
        tvRedBagRainGetMoney = view.findViewById(R.id.tvRedBagRainGetMoney);
        if (activityStatus == 3) {
            ivRedBagRainStatus.setImageResource(R.drawable.red_bag_status_get);
            tvRedBagRainGetSuc.setText(money > 0 ? R.string.red_bag_rain_you_get_suc : R.string.red_bag_rain_thanks_join);
            if (money > 0) {
                DecimalFormat df = new DecimalFormat("#,###");
                tvRedBagRainGetMoney.setText(df.format(money));
            }
        } else {
            ivRedBagRainStatus.setImageResource(R.drawable.red_bag_status);
            if (TextUtils.isEmpty(activityStatusStr)) {
                //后台传来的 1 没开始，2活动进行中 红包已领完，3 活动正常进行中
                //本地自定义 4 活动已结束
                switch (activityStatus) {
                    case 0:
                        break;
                    case 1:
                        tvRedBagRainStatus.setText(R.string.red_bag_rain_activity_un_start);
                        break;
                    case 2:
                        tvRedBagRainStatus.setText(R.string.red_bag_rain_activity_get_finish);
                        break;
                    case 4:
                        tvRedBagRainStatus.setText(R.string.red_bag_rain_activity_end);
                        break;
                }
            } else {
                tvRedBagRainStatus.setText(activityStatusStr);
            }
        }
        clRedBagRainStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }
}
