package com.live.fox.dialog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.google.gson.Gson;
import com.live.fox.R;
import com.live.fox.common.JsonCallback;
import com.live.fox.entity.User;
import com.live.fox.server.Api_LiveRecreation;
import com.live.fox.AnchorLiveActivity;
import com.live.fox.utils.LogUtils;
import com.live.fox.utils.ToastUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.ArrayList;


/**
 * 可邀请PK 用户列表
 */
public class PKInvertListDialog extends DialogFragment {

    RecyclerView rv;
    SmartRefreshLayout refreshLayout;

    ArrayList<User> userList;
    BaseQuickAdapter rvAdapter;
    public boolean isShow = false;
    //是否正在邀请PK
    boolean isInverPking = false;

    public static PKInvertListDialog newInstance(ArrayList<User> data) {
        PKInvertListDialog fragment = new PKInvertListDialog();
        Bundle bundle = new Bundle();
        bundle.putSerializable("userList", data);
        fragment.setArguments(bundle);
        return fragment;
    }



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置样式
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.DialogsWithoutBlack);
        Bundle bundle = getArguments();
        if (bundle != null) {
            userList = (ArrayList<User>)bundle.getSerializable("userList");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = inflater.inflate(R.layout.dialog_pk_invertlist, container, false);
        rv = view.findViewById(R.id.rv_);
        refreshLayout = view.findViewById(R.id.refreshLayout);
        return view;
    }

    //如果需要修改大小 修改显示动画 重写此方法
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getDialog().setCanceledOnTouchOutside(false);
        getDialog().setCancelable(false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        isShow = true;

        initView(view);
        setRecycleView();
    }


    public void initView(View view) {
        rv = view.findViewById(R.id.rv_);
        refreshLayout = view.findViewById(R.id.refreshLayout);
        view.findViewById(R.id.iv_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }


    public void setRecycleView() {
        refreshLayout.setEnablePureScrollMode(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        this.rv.setLayoutManager(layoutManager);
        this.rv.setAdapter(rvAdapter = new BaseQuickAdapter(R.layout.item_pklist, userList) {
            @Override
            protected void convert(BaseViewHolder helper, Object item) {
                User user = (User) item;
                ImageView imageView = helper.getView(R.id.iv_);
                Glide.with(requireActivity()).load(user.getAvatar()).into(imageView);
                helper.setText(R.id.tv_name, user.getNickname());

                helper.addOnClickListener(R.id.tv_invert);
            }
        });

        rvAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            User user = (User) rvAdapter.getItem(position);
            LogUtils.e(new Gson().toJson(user));
            if(isInverPking) return;
            isInverPking = true;
            ((AnchorLiveActivity)getActivity()).showLoadingDialog(getString(R.string.initiatingPK), false, false);
            Api_LiveRecreation.ins().sendOpenPk(user.getUid(), new JsonCallback<String>() {
                @Override
                public void onSuccess(int code, String msg, String data) {
                    isInverPking = false;
                    ((AnchorLiveActivity)getActivity()).hideLoadingDialog();
                    if (code == 0) {
                        dismiss();
                        ((AnchorLiveActivity)getActivity()).showPKInvertLoadingDialog(user.getUid(), user.getNickname());
                    } else {
                        ToastUtils.showShort(msg);
                    }
                }
            });

        });

    }


    @Override
    public void dismiss() {
        isShow = false;
        super.dismiss();
    }

    OnBtnSureClick btnSureClick;

    public void setBtnSureClick(OnBtnSureClick btnClick) {
        btnSureClick = btnClick;
    }

    public interface OnBtnSureClick {
        void onClick();
    }

}
