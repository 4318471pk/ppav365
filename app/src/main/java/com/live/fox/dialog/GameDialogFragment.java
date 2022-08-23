package com.live.fox.dialog;

import static com.live.fox.common.CommonLiveControlFragment.TIME_INTERVAL;
import static com.live.fox.entity.cp.CpOutputFactory.TYPE_CP_FF;
import static com.live.fox.entity.cp.CpOutputFactory.TYPE_CP_HNCP;

import android.app.Dialog;
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
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.live.fox.Constant;
import com.live.fox.R;
import com.live.fox.adapter.devider.RecyclerSpace;
import com.live.fox.common.JsonCallback;
import com.live.fox.entity.response.ChipsVO;
import com.live.fox.server.Api_Config;
import com.live.fox.server.Api_TYGame;
import com.live.fox.ui.game.AllGameActivity;
import com.live.fox.ui.game.GameFullWebViewActivity;
import com.live.fox.utils.AppUserManger;
import com.live.fox.utils.GlideUtils;
import com.live.fox.utils.LogUtils;
import com.live.fox.utils.ToastUtils;
import com.live.fox.utils.device.DeviceUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * 所有游戏
 * 直播间游戏列表
 */
public class GameDialogFragment extends DialogFragment {

    private BaseQuickAdapter<ChipsVO, BaseViewHolder> mChipsAdapter;
    private long mLastClickTime;
    private long liveId;
    private View rootView;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Dialog dialog = new Dialog(requireActivity(), R.style.DialogDefault);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.fragment_cp);
        dialog.setCanceledOnTouchOutside(true); // 外部點擊取消
        // 設置寬度爲屏寬, 靠近屏幕底部。
        Window window = dialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = DeviceUtils.dp2px(requireActivity(), 200);
        window.setGravity(Gravity.BOTTOM);
        window.setDimAmount(0.05f);
        window.setBackgroundDrawableResource(R.color.transparent);
        window.setWindowAnimations(R.style.ActionSheetDialogAnimation);
        window.setAttributes(lp);
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_cp, container, false);
        }
        return rootView;
    }

    void doCpList() {
        Api_Config.ins().getCpList(new JsonCallback<List<ChipsVO>>() {
            @Override
            public void onSuccess(int code, String msg, List<ChipsVO> data) {
                if (code == Constant.Code.SUCCESS) {
                    if (data != null && data.size() > 0) {
                        mChipsAdapter.setNewData(data);
                    } else {
                        rootView.findViewById(R.id.live_play_empty).setVisibility(View.VISIBLE);
                    }
                } else {
                    ToastUtils.showShort(msg);
                }
            }
        });
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView rvCp = view.findViewById(R.id.live_play_recycler_view);

        if (getArguments() != null) {
            liveId = getArguments().getLong("liveId", 0);
        }

        GridLayoutManager grid = new GridLayoutManager(getActivity(), 4);
        rvCp.setLayoutManager(grid);

        rvCp.setAdapter(mChipsAdapter = new BaseQuickAdapter<ChipsVO, BaseViewHolder>(R.layout.adapter_cp_item) {
            @Override
            protected void convert(BaseViewHolder helper, ChipsVO it) {
                helper.getAdapterPosition();
                TextView tvCp = helper.getView(R.id.tvCp);
                tvCp.setText(it.getChinese());
                GlideUtils.loadImage(getActivity(), it.getIcon(), helper.getView(R.id.ivShit));
                helper.addOnClickListener(R.id.llCp);
            }
        });

        RecyclerSpace recyclerSpace = new RecyclerSpace(DeviceUtils.dp2px(requireActivity(), 8));
        rvCp.addItemDecoration(recyclerSpace);
        mChipsAdapter.setOnItemChildClickListener((adapter, view1, position) -> {
            long nowTime = System.currentTimeMillis();
            if (nowTime - mLastClickTime > TIME_INTERVAL) {
                mLastClickTime = nowTime;
                ChipsVO chipsVO = (ChipsVO) adapter.getData().get(position);
                handleGameClick(chipsVO);
            }
        });
        doCpList();
    }


    /**
     * 处理游戏点击事件
     * 游戏下注
     *
     * @param chipsVO 游戏实体
     */
    private void handleGameClick(ChipsVO chipsVO) {
        switch (chipsVO.getName()) {
            case TYPE_CP_FF:// 彩票1 鱼虾蟹
                FGameDialogFragment fragmentGame = FGameDialogFragment.newInstance(chipsVO, liveId);
                if (!fragmentGame.isAdded()) {
                    fragmentGame.showNow(requireActivity().getSupportFragmentManager(),
                            FGameDialogFragment.class.getSimpleName());
                }
                break;

            case "allgame"://点击所有游戏
                AllGameActivity.launch(requireActivity());
                dismiss();
                break;
            case "EURO":
                Api_TYGame.ins().forwardGame("14", AppUserManger.getUserInfo().getUid() + "",
                        new JsonCallback<String>() {
                            @Override
                            public void onSuccess(int code, String msg, String result) {
                                if (code == 0) {
                                    try {
                                        JSONObject jsonObject = new JSONObject(result);
                                        String url = jsonObject.optString("url");
                                        LogUtils.e("url: " + url);
                                        GameFullWebViewActivity.startActivity(requireActivity(), 7, url);
                                    } catch (JSONException e) {
                                        ToastUtils.showShort(e.getMessage());
                                    }
                                } else {
                                    ToastUtils.showShort(msg);
                                }
                            }
                        });
                break;

            case TYPE_CP_HNCP: // 河内彩票
                HNDialogFragment fragment = HNDialogFragment.newInstance(chipsVO, liveId);
                if (!fragment.isAdded()) {
                    fragment.showNow(requireActivity().getSupportFragmentManager(),
                            HNDialogFragment.class.getSimpleName());
                }
                break;

            default:  //一分快三 pk 10
                MinuteGameDialogFragment fragmentMinute = MinuteGameDialogFragment.newInstance(chipsVO, liveId);
                if (!fragmentMinute.isAdded()) {
                    fragmentMinute.showNow(requireActivity().getSupportFragmentManager(),
                            MinuteGameDialogFragment.class.getSimpleName());
                }
                break;
        }
    }

    public static GameDialogFragment newInstance(long liveId) {
        GameDialogFragment fragment = new GameDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putLong("liveId", liveId);
        fragment.setArguments(bundle);
        return fragment;
    }
}
