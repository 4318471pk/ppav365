package com.live.fox.ui.live;

import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.live.fox.Constant;
import com.live.fox.R;
import com.live.fox.entity.Anchor;
import com.live.fox.utils.ClickUtil;
import com.live.fox.utils.GlideUtils;
import com.live.fox.utils.KeyboardUtils;
import com.live.fox.utils.StringUtils;
import com.live.fox.utils.ToastUtils;


/**
 * 直播间支付界面
 */
public class RoomPayFragment extends Fragment implements View.OnClickListener {

    protected View rootView;
    private ImageView iv_bg;
    private LinearLayout ll_pwdpay;
    private LinearLayout ll_pricepay;
    private EditText et_;
    private TextView tv_tip;

    private static final String DEFAULT_COLOR = "#929292";//系统消息颜色  红色
    private static final String PRICE_COLOR = "#EB4A81";//昵称颜色   灰色 #b9b9b9

    Anchor anchor;

    boolean isInit = false;


    public static RoomPayFragment newInstance(Anchor anchor) {
        RoomPayFragment fragment = new RoomPayFragment();
        Bundle args = new Bundle();
        args.putSerializable("anchor", anchor);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();
        if (bundle != null) {
            anchor = (Anchor) bundle.getSerializable("anchor");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.roompay_fragment, container, false);
        setView(rootView);
        return rootView;
    }

    public void setView(View bindSource) {
        iv_bg = bindSource.findViewById(R.id.iv_bg);
        ll_pwdpay = bindSource.findViewById(R.id.ll_pwdpay);
        ll_pricepay = bindSource.findViewById(R.id.ll_pricepay);
        et_ = bindSource.findViewById(R.id.et_);
        tv_tip = bindSource.findViewById(R.id.tv_tip);
        bindSource.findViewById(R.id.tv_cancel).setOnClickListener(this);
        bindSource.findViewById(R.id.live_room_pay_sure).setOnClickListener(this);
        bindSource.findViewById(R.id.tv_cancel_pwd).setOnClickListener(this);
        bindSource.findViewById(R.id.tv_sure_pwd).setOnClickListener(this);

        isInit = true;
        refreshPage(anchor);
    }

    public void showError(String msg) {
        if (getActivity() instanceof PlayLiveActivity) {
            ((PlayLiveActivity) getActivity()).showToastTip(false, msg);
        } else {
            ToastUtils.showShort(msg);
        }
    }


    public void refreshPage(Anchor anchor) {

        this.anchor = anchor;
        if (!isInit) return;
        if (anchor == null) {
            return;
        }

        GlideUtils.loadImage(getActivity(), anchor.getAvatar(), -1, -1, iv_bg);

        StringBuilder html = new StringBuilder();
        switch (anchor.getType()) {
            case 1:
                Constant.isShowWindow = false;
                ll_pricepay.setVisibility(View.VISIBLE);
                ll_pwdpay.setVisibility(View.INVISIBLE);
                html.append(getFontTag(getString(R.string.everyMin), false));
                html.append(getFontTag(anchor.getPrice() + "", true));
                html.append(getFontTag(getString(R.string.goldWatchLive), false));
                tv_tip.setText(Html.fromHtml(html.toString(), null, null));
                break;
            case 2:
                Constant.isShowWindow = false;
                ll_pricepay.setVisibility(View.VISIBLE);
                ll_pwdpay.setVisibility(View.INVISIBLE);
                html.append(getFontTag(getString(R.string.money_pay), false));
                html.append(getFontTag(anchor.getPrice() + "", true));
                html.append(getFontTag(getString(R.string.goldWatchLive), false));
                tv_tip.setText(Html.fromHtml(html.toString(), null, null));
                break;
            case 3:
                Constant.isShowWindow = false;
                ll_pricepay.setVisibility(View.INVISIBLE);
                ll_pwdpay.setVisibility(View.VISIBLE);
                et_.setText("");
                break;
        }
    }


    private String getFontTag(String text, boolean isPrice) {
        return getFontTag(text, isPrice ? PRICE_COLOR : DEFAULT_COLOR, false, true);
    }


    /**
     * 获取H5的font标签
     *
     * @param text  文本
     * @param color 文本颜色
     * @param colon 文本后面是否需要带上冒号
     */
    private String getFontTag(String text, String color, boolean colon, boolean space) {
        StringBuilder builder = new StringBuilder();
        builder.append("<font color =\"").append(color)
                .append("\">").append(text);
        if (colon) {
            builder.append(":");
        }
        if (space) {
            builder.append("&nbsp;&nbsp;");
        }
        builder.append("</font>");
        return builder.toString();
    }

    @Override
    public void onClick(View view) {
        if (ClickUtil.isFastDoubleClick()) {
            return;
        }
        switch (view.getId()) {
            case R.id.tv_cancel:
                ((PlayLiveActivity) requireActivity()).nextRoom();
                break;
            case R.id.tv_cancel_pwd:
                ((PlayLiveActivity) requireActivity()).outRoomByUserClose();
                break;
            case R.id.live_room_pay_sure:
                ((PlayLiveActivity) requireActivity()).reallyEnterRoom(anchor, "", true, 0);
                break;
            case R.id.tv_sure_pwd:
                String pwdStr = et_.getText().toString().trim();
                if (StringUtils.isEmpty(pwdStr)) {
                    return;
                }
                if (KeyboardUtils.isSoftInputVisible(getActivity())) {
                    KeyboardUtils.toggleSoftInput();
                }
                ((PlayLiveActivity) requireActivity()).checkLiveRoomCanBePreview(anchor, pwdStr, false);
                break;
        }
    }
}

