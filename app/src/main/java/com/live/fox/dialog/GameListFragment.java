package com.live.fox.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.google.gson.Gson;
import com.live.fox.R;
import com.live.fox.adapter.devider.RecyclerSpace;
import com.live.fox.common.JsonCallback;
import com.live.fox.entity.Game;
import com.live.fox.entity.MessageEvent;
import com.live.fox.server.Api_Config;
import com.live.fox.utils.GlideUtils;
import com.live.fox.utils.LogUtils;
import com.live.fox.utils.ToastUtils;
import com.live.fox.utils.device.DeviceUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;


/**
 * 直播间的游戏列表
 */
public class GameListFragment extends DialogFragment {

    SmartRefreshLayout refreshLayout;
    RecyclerView rv;

    BaseQuickAdapter adapter;

    boolean isShow = true;

    public static GameListFragment newInstance() {
        GameListFragment fragment = new GameListFragment();
        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Dialog dialog = new Dialog(getActivity(), R.style.BottomDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_recycle_view_with_refresh);
        dialog.setCanceledOnTouchOutside(true); // 外部點擊取消

        initView(dialog);

        // 設置寬度爲屏寬, 靠近屏幕底部。
        Window window = dialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        // 寬度持平
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = DeviceUtils.dp2px(requireContext(), 214);
        window.setGravity(Gravity.BOTTOM);
        window.setBackgroundDrawableResource(R.color.transparent);
        window.setWindowAnimations(R.style.ActionSheetDialogAnimation);
        window.setAttributes(lp);

        return dialog;
    }

    private void initView(Dialog dialog) {
        isShow = true;

        refreshLayout = dialog.findViewById(R.id.refreshLayout);
        rv = dialog.findViewById(R.id.rv_);

        setRecycleView();
        doGetGameListApi();
    }


    public void setRecycleView() {
        refreshLayout.setEnablePureScrollMode(true);
        refreshLayout.setBackgroundResource(R.color.black_90);
        int dp18 = DeviceUtils.dp2px(requireContext(), 26);
        refreshLayout.setPadding(dp18, 0, dp18, 0);

        GridLayoutManager layoutManager = new GridLayoutManager(requireContext(), 4);
        rv.setLayoutManager(layoutManager);
        rv.addItemDecoration(new RecyclerSpace(5));
        rv.setAdapter(adapter = new BaseQuickAdapter(R.layout.item_game, new ArrayList<Game>()) {
            @Override
            protected void convert(BaseViewHolder helper, Object item) {
                Game game = (Game) item;
                GlideUtils.loadImage(getActivity(), game.getImage(), helper.getView(R.id.iv_));
            }
        });

        adapter.setOnItemClickListener((adapter, view, position) -> {
            Game game = (Game) adapter.getItem(position);
            EventBus.getDefault().post(new MessageEvent(66, new Gson().toJson(game)));
            dismiss();
        });
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        isShow = false;
    }

    /**
     * 游戏列表
     */
    public void doGetGameListApi() {
        Api_Config.ins().getGameList(new JsonCallback<List<Game>>() {
            @Override
            public void onSuccess(int code, String msg, List<Game> data) {
                if (data != null) LogUtils.e(code + "," + msg + "," + new Gson().toJson(data));
                if (code == 0) {
                    adapter.setNewData(data);
                } else {
                    ToastUtils.showShort(msg);
                }
            }
        });
    }

    OnBtnClick btnClick;

    public void setBtnClick(OnBtnClick btnClick) {
        this.btnClick = btnClick;
    }

    public interface OnBtnClick {
        void onClick(int id);
    }
}
