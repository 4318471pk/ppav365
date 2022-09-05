package com.live.fox.ui.live;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.live.fox.Constant;
import com.live.fox.R;
import com.live.fox.base.BaseFragment;
import com.live.fox.common.JsonCallback;
import com.live.fox.entity.Anchor;
import com.live.fox.server.Api_User;
import com.live.fox.utils.ClickUtil;
import com.live.fox.utils.GlideUtils;
import com.live.fox.utils.LogUtils;
import com.live.fox.utils.ToastUtils;
import com.makeramen.roundedimageview.RoundedImageView;

/**
 * 直播结束的界面
 */
public class LiveFinishFragment extends BaseFragment implements View.OnClickListener {

    protected View rootView;
    private RelativeLayout ll_finish_bg;
    ImageView iv_bg;
    RoundedImageView iv_finish_avatar;
    TextView tv_finish_tip;
    TextView tv_finish_id;
    TextView tv_follow;
    TextView tv_finish_name;

    Anchor anchor;
    String reason;

    public static LiveFinishFragment newInstance(Anchor anchorInfo, String reason) {
        if (reason == null) reason = "";
        LiveFinishFragment fragment = new LiveFinishFragment();
        Bundle args = new Bundle();
        args.putSerializable("anchor", anchorInfo);
        args.putString("reason", reason);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();
        anchor = (Anchor) bundle.getSerializable("anchor");
        reason = bundle.getString("reason");
        Constant.isShowWindow=false;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.userlivefinish_activity, null, false);
        setView(rootView);
        return rootView;
    }


    public void setView(View view) {
        ll_finish_bg = view.findViewById(R.id.ll_finish_bg);
        iv_bg = view.findViewById(R.id.iv_bg);
        iv_finish_avatar = view.findViewById(R.id.iv_finish_avatar);
        tv_finish_tip = view.findViewById(R.id.tv_finish_tip);
        tv_finish_id = view.findViewById(R.id.tv_finish_id);
        tv_follow = view.findViewById(R.id.tv_follow);
        tv_finish_name = view.findViewById(R.id.tv_finish_name);

        view.findViewById(R.id.tv_finish_back).setOnClickListener(this);
        view.findViewById(R.id.tv_follow).setOnClickListener(this);

        updateData(anchor, reason);
    }


    public void updateData(Anchor anchor, String reason) {
        if (reason == null) reason = "";
        GlideUtils.loadDefaultImage(getActivity(), anchor.getAvatar(), iv_bg);
        GlideUtils.loadDefaultCircleImage(getActivity(), anchor.getAvatar(), iv_finish_avatar);
        tv_finish_name.setText(anchor.getNickname());
        tv_finish_tip.setText(reason);
        tv_follow.setVisibility(anchor.isFollow() ? View.INVISIBLE : View.VISIBLE);
    }


    @Override
    public void onClick(View v) {
        if (ClickUtil.isFastDoubleClick()) return;
        switch (v.getId()) {
            case R.id.tv_follow:
                Api_User.ins().follow(anchor.getAnchorId(), !anchor.isFollow(), new JsonCallback<String>() {
                    @Override
                    public void onSuccess(int code, String msg, String result) {
                        if (result != null) LogUtils.e("follow result : " + result);
                        if (code == 0 && result != null) {
                            anchor.setFollow(!anchor.isFollow());
                            updateData(anchor, reason);
                            ToastUtils.showShort(getString(R.string.successFocus));
                        } else {
                            ToastUtils.showShort(msg);
                        }

                    }
                });
                break;
            case R.id.tv_finish_back:
                if (getActivity() instanceof PlayLiveActivity) {
                    ((PlayLiveActivity) requireActivity()).closeRoomAndStopPlay(false, true, true);
                } else {
                    requireActivity().finish();
                }
                break;
        }
    }


}

