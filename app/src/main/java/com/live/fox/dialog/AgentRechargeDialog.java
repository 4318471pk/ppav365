package com.live.fox.dialog;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.live.fox.R;
import com.live.fox.adapter.AgentAdapter;
import com.live.fox.base.AgentPresenter;
import com.live.fox.contract.AgentContract;
import com.live.fox.entity.User;
import com.live.fox.entity.response.AgentInfoVO;
import com.live.fox.manager.DataCenter;
import com.live.fox.mvp.MvpDialogFragment;
import com.live.fox.server.BaseApi;
import com.live.fox.manager.AppUserManger;
import com.live.fox.utils.ClipboardUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class AgentRechargeDialog extends MvpDialogFragment<AgentPresenter> implements View.OnClickListener, AgentContract.View {
    private RecyclerView rvAgent;
    private TextView tvId;
    private AgentAdapter mAgentAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        Window window = getDialog().getWindow();
        if (window != null) {
            window.getDecorView().setPadding(0, 0, 0, 0);
            window.setBackgroundDrawableResource(android.R.color.transparent);
            WindowManager.LayoutParams lp = window.getAttributes();
            lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            //设置dialog的位置在底部
            lp.gravity = Gravity.CENTER;
            window.setWindowAnimations(R.style.agentDialog);
        }
        return inflater.inflate(R.layout.fragment_agent_recharge, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rvAgent = (RecyclerView) view.findViewById(R.id.rvAgent);
        tvId = (TextView) view.findViewById(R.id.tvId);
        view.findViewById(R.id.rtvCopy).setOnClickListener(this);
        mAgentAdapter = new AgentAdapter(new ArrayList<AgentInfoVO>());
        mAgentAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                AgentInfoVO agentInfoVO = (AgentInfoVO) adapter.getItem(position);
                if (view.getId() == R.id.ivQQ) {
                    if (btnClick != null) {
                        btnClick.onClick(1, agentInfoVO);
                    }
//                    if (presenter.isQQClientAvailable()) {
//                        ClipboardUtils.copyText(agentInfoVO.getQq());
//                        presenter.openQQApp(agentInfoVO.getQq());
//                    } else {
//                        showToastTip(false, "请先安装QQ");
//                    }
                } else if (view.getId() == R.id.ivWechat) {
                    if (btnClick != null) {
                        btnClick.onClick(2, agentInfoVO);
                    }
//                    if (presenter.isWeixinAvilible()) {
//                        ClipboardUtils.copyText(agentInfoVO.getWechat());
//                        showToastTip(true, "已复制微信号，即将前往微信添加好友进行充值");
//                        tvId.postDelayed(mRunnable, 2500);
//                    } else {
//                        showToastTip(false, "请先安装微信");
//                    }

                }
            }
        });
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvAgent.setLayoutManager(layoutManager);
        rvAgent.addItemDecoration(new AgentAdapter.SpaceItemDecoration());
        rvAgent.setAdapter(mAgentAdapter);
        User userinfo = DataCenter.getInstance().getUserInfo().getUser();
        tvId.setText(getString(R.string.currentCharge) + userinfo.getUid());

        doGetAgentInfo();
    }

    public void doGetAgentInfo() {
        HashMap<String, Object> params = BaseApi.getCommonParams();
        params.put("goldCoin", getArguments().get("goldCoin"));
        presenter.doGetAgentInfo(params);
    }

    @Override
    public void onGetAgentInfo(List<AgentInfoVO> result) {
        if (result != null && result.size() > 0) mAgentAdapter.setNewData(result);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rtvCopy:
                User userinfo = DataCenter.getInstance().getUserInfo().getUser();
                ClipboardUtils.copyText(String.valueOf(userinfo.getUid()));
                showToastTip(true, getString(R.string.chargeCopy));
                break;
        }
    }


    OnBtnClick btnClick;

    public void setBtnClick(OnBtnClick btnClick) {
        this.btnClick = btnClick;
    }


    public interface OnBtnClick {
        //type 1QQ 2WX
        void onClick(int agentType, AgentInfoVO agentInfoVO);
    }

}
