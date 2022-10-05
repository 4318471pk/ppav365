package com.live.fox.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewStub;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.google.android.material.tabs.TabLayout;
import com.live.fox.Constant;
import com.live.fox.R;
import com.live.fox.common.JsonCallback;
import com.live.fox.entity.User;
import com.live.fox.helper.SimpleTabSelectedListener;
import com.live.fox.server.Api_Live;
import com.live.fox.server.Api_User;
import com.live.fox.ui.mine.editprofile.UserDetailActivity;
import com.live.fox.utils.ChatSpanUtils;
import com.live.fox.utils.GlideUtils;
import com.live.fox.utils.device.DeviceUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.ArrayList;
import java.util.List;


/**
 * 直播间的观众列表
 */
public class AudienceListDialog extends DialogFragment {

    Dialog dialog;

    SmartRefreshLayout refreshLayout;
    RecyclerView rv;

    BaseQuickAdapter adapter;
    TabLayout tabLayout;

    long liveId;
    private int selectTab;

    public static AudienceListDialog newInstance(long liveId) {
        AudienceListDialog fragment = new AudienceListDialog();
        Bundle bundle = new Bundle();
        bundle.putLong("liveId", liveId);
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            liveId = bundle.getLong("liveId");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Dialog dialog = new Dialog(requireContext(), R.style.BottomDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_peo);
        dialog.setCanceledOnTouchOutside(true); // 外部點擊取消

        initView(dialog);

        // 設置寬度爲屏寬, 靠近屏幕底部。
        Window window = dialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        // 寬度持平
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = DeviceUtils.dp2px(requireContext(), 300);
        window.setGravity(Gravity.BOTTOM);
        window.setBackgroundDrawableResource(R.color.dialog_transparent_bg);
        window.setWindowAnimations(R.style.ActionSheetDialogAnimation);
        window.setAttributes(lp);
        return dialog;
    }

    private void initView(Dialog dialog) {
        this.dialog = dialog;
        refreshLayout = dialog.findViewById(R.id.refreshLayout);
        rv = dialog.findViewById(R.id.rv_);
        tabLayout = dialog.findViewById(R.id.tabLayout_pee);
        TabLayout.Tab mTab = tabLayout.newTab();
        mTab.setText(getString(R.string.audience));
        tabLayout.addTab(mTab);

        TabLayout.Tab mTab2 = tabLayout.newTab();
        mTab2.setText(getString(R.string.noble));
        tabLayout.addTab(mTab2);
        tabLayout.addOnTabSelectedListener(new SimpleTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                selectTab = tab.getPosition();
                loadingDataForRemote();
            }
        });

        showLoadingView();
        setRecycleView();
        doGetAudienceListApi();
    }

    private void loadingDataForRemote() {
        if (selectTab == 0) {
            doGetAudienceListApi();
        } else {
            doVipList();
        }
    }

    public void setRecycleView() {
        int dp18 = DeviceUtils.dp2px(requireContext(), 14);
        refreshLayout.setPadding(dp18, 0, dp18, 0);
        refreshLayout.setOnRefreshListener(refreshLayout -> loadingDataForRemote());

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rv.setLayoutManager(layoutManager);
        rv.setAdapter(adapter = new BaseQuickAdapter(R.layout.item_liveroom_audiencelist, new ArrayList<User>()) {
            @Override
            protected void convert(BaseViewHolder helper, Object item) {
                User user = (User) item;
                GlideUtils.loadDefaultCircleImage(getActivity(), user.getAvatar(), helper.getView(R.id.iv_));
                helper.setText(R.id.tv_name, ChatSpanUtils.ins().getNickNameSpan(user,requireContext()));
                if (user.vipUid != null) {
                    helper.setText(R.id.tv_uid, "ID:" + user.vipUid);

                } else {
                    helper.setText(R.id.tv_uid, "ID:" + user.getUid());
                }
            }
        });

        adapter.setOnItemClickListener((adapter, view, position) -> {
            User user = (User) adapter.getData().get(position);
            UserDetailActivity.startActivity(getActivity(), user.getUid());
        });
    }

    private void doVipList() {
        Api_User.ins().doVipList(liveId, Constant.OS, new JsonCallback<List<User>>() {
            @Override
            public void onSuccess(int code, String msg, List<User> data) {
                refreshLayout.finishRefresh();
                if (code == 0) {
                    adapter.setNewData(data);
                    if (data.size() == 0) {
                        showEmptyView(getString(R.string.liveNoAudience));
                    } else {
                        if (emptyView != null) emptyView.setVisibility(View.GONE);
                    }
                } else {
                    showEmptyView(msg);
                }
            }
        });
    }

    /**
     * 观众列表
     */
    public void doGetAudienceListApi() {
        Api_Live.ins().getRoomuserList(liveId, new JsonCallback<List<User>>() {
            @Override
            public void onSuccess(int code, String msg, List<User> data) {
                hideLoadingView();
                refreshLayout.finishRefresh();
                if (code == 0) {
                    adapter.setNewData(data);
                    if (data.size() == 0) {
                        showEmptyView(getString(R.string.liveNoAudience));
                    } else {
                        if (emptyView != null) emptyView.setVisibility(View.GONE);
                    }
                } else {
                    showEmptyView(msg);
                }
            }
        });
    }

    View emptyView;

    /**
     * 当Fragment中没有任何内容的时候，通过此方法显示提示界面给用户。
     *
     * @param tip 界面中的提示信息
     */
    public void showEmptyView(String tip) {
        if (emptyView != null) {
            emptyView.setVisibility(View.VISIBLE);
            return;
        }
        ViewStub viewStub = dialog.findViewById(R.id.emptyView);
        if (viewStub != null) {
            emptyView = viewStub.inflate();
            TextView noContentText = emptyView.findViewById(R.id.tv_empty);
            noContentText.setText(tip);
        }
    }

    View loadingView;

    /**
     * 当Fragment中没有任何内容的时候，通过此方法显示提示界面给用户。
     */
    public void showLoadingView() {
        if (loadingView != null) {
            loadingView.setVisibility(View.VISIBLE);
            return;
        }
        ViewStub viewStub = dialog.findViewById(R.id.loadingView);
        if (viewStub != null) {
            loadingView = viewStub.inflate();
            loadingView.findViewById(R.id.view_progress).setBackgroundResource(R.color.black_90);
        }
    }

    public void hideLoadingView() {
        if (loadingView != null)
            loadingView.setVisibility(View.GONE);
    }


    OnBtnClick btnClick;

    public void setBtnClick(OnBtnClick btnClick) {
        this.btnClick = btnClick;
    }


    public interface OnBtnClick {
        void onClick(int id);
    }
}
