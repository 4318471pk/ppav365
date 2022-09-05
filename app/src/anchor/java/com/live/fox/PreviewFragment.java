package com.live.fox;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.live.fox.adapter.devider.RecyclerSpace;
import com.live.fox.base.BaseFragment;
import com.live.fox.common.CommonApp;
import com.live.fox.common.JsonCallback;
import com.live.fox.entity.ConfigPathsBean;
import com.live.fox.entity.response.ChipsVO;
import com.live.fox.server.Api_Config;
import com.live.fox.ui.live.PlayLiveActivity;
import com.live.fox.utils.AppUserManger;
import com.live.fox.utils.ClickUtil;
import com.live.fox.utils.GlideUtils;
import com.live.fox.utils.ToastUtils;
import com.live.fox.utils.device.DeviceUtils;
import com.live.fox.windowmanager.WindowUtils;
import com.lovense.sdklibrary.LovenseToy;
import com.tencent.rtmp.TXLiveBase;

import java.util.ArrayList;
import java.util.List;


public class PreviewFragment extends BaseFragment implements View.OnClickListener {

    TextView tvLocation;
    ImageView ivLocation;
    TextView tvRoomset;
    TextView tvToy;

    RecyclerView rvLines;
    RecyclerView rvchoice;
    BaseQuickAdapter<ChipsVO, BaseViewHolder> chAdapter;
    private final ArrayList<String> lotteryNames = new ArrayList<>();
    private final ArrayList<String> names = new ArrayList<>();
    List<ConfigPathsBean> conData = new ArrayList<>();
    BaseQuickAdapter<ConfigPathsBean, BaseViewHolder> lineAdapter;
    private int choicePosition = 0;

    public static PreviewFragment newInstance() {
        return new PreviewFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.preview_fragment, container, false);
        setView();
        return rootView;
    }

    public void setView() {
        tvLocation = rootView.findViewById(R.id.tv_location);
        ivLocation = rootView.findViewById(R.id.iv_location);
        tvRoomset = rootView.findViewById(R.id.tv_roomset);
        tvToy = rootView.findViewById(R.id.tv_toy);
        rvLines = rootView.findViewById(R.id.rvLines);
        rvchoice = rootView.findViewById(R.id.rvchoice);

        rootView.findViewById(R.id.layout_root).setOnClickListener(this);
        rootView.findViewById(R.id.iv_close).setOnClickListener(this);
        rootView.findViewById(R.id.layout_toy).setOnClickListener(this);
        rootView.findViewById(R.id.iv_change_camera).setOnClickListener(this);
        rootView.findViewById(R.id.btn_openlive).setOnClickListener(this);
        rootView.findViewById(R.id.layout_beauty).setOnClickListener(this);
        rootView.findViewById(R.id.layout_roomset).setOnClickListener(this);

        livePathConfig();
        GridLayoutManager grid = new GridLayoutManager(getActivity(), 3);
        rvchoice.setLayoutManager(grid);
        rvchoice.setAdapter(chAdapter = new BaseQuickAdapter<ChipsVO, BaseViewHolder>(R.layout.adapter_choicec_item) {
            @Override
            protected void convert(BaseViewHolder helper, ChipsVO it) {
                TextView tvchoice = helper.getView(R.id.tvchoice);
                tvchoice.setText(it.getChinese());
                GlideUtils.loadImage(getActivity(), it.getIcon(), helper.getView(R.id.ivchoice));
                if (it.isCheck()) {
                    tvchoice.setTextColor(Color.parseColor("#EB4A81"));
                } else {
                    tvchoice.setTextColor(Color.WHITE);
                }
                helper.addOnClickListener(R.id.llchoicec);
            }
        });
        RecyclerSpace recyclerSpace = new RecyclerSpace(DeviceUtils.dp2px(requireActivity(), 10));
        rvchoice.addItemDecoration(recyclerSpace);
        chAdapter.setOnItemChildClickListener((adapter, view12, position) -> {
            ChipsVO choiceCaiBean = (ChipsVO) adapter.getData().get(position);

            if (choiceCaiBean.getName().equals("allgame") || choiceCaiBean.getName().equals("EURO")) {
                //不能选择所有游戏
                return;
            }

            choiceCaiBean.setCheck(!choiceCaiBean.isCheck());
            if (choiceCaiBean.isCheck()) {
                lotteryNames.add(choiceCaiBean.getChinese());
                names.add(choiceCaiBean.getName());
            } else {
                lotteryNames.remove(choiceCaiBean.getChinese());
                names.remove(choiceCaiBean.getName());
            }

            if (lotteryNames.size() > 2) {
                ToastUtils.showShort(getString(R.string.noMoreThanTwo));
            }
            lotteryClick.onClick(lotteryNames, names);
            chAdapter.notifyDataSetChanged();
        });

        GridLayoutManager grid1 = new GridLayoutManager(getActivity(), 2);
        rvLines.setLayoutManager(grid1);
        rvLines.setAdapter(lineAdapter = new BaseQuickAdapter<ConfigPathsBean, BaseViewHolder>(R.layout.child) {
            @Override
            protected void convert(BaseViewHolder helper, ConfigPathsBean it) {
                CheckBox checkbox = helper.getView(R.id.live_line_check_box);
                checkbox.setText(it.getName());
                checkbox.setChecked(choicePosition == helper.getAdapterPosition());
                helper.addOnClickListener(R.id.live_line_check_box);
            }
        });

        RecyclerSpace recyclerSpace1 = new RecyclerSpace(DeviceUtils.dp2px(requireActivity(), 10));
        rvLines.addItemDecoration(recyclerSpace1);

        lineAdapter.setOnItemChildClickListener((adapter, view1, position) -> {
            ConfigPathsBean configPathsBean = (ConfigPathsBean) adapter.getData().get(position);
            choicePosition = position;
            lineAdapter.notifyDataSetChanged();
            onLineClick.onClick(configPathsBean.getId() + "");
        });
    }

    private void livePathConfig() {
        Api_Config.ins().getConfigPaths(AppUserManger.getUserInfo().getUid(), new JsonCallback<List<ConfigPathsBean>>() {
            @Override
            public void onSuccess(int code, String msg, List<ConfigPathsBean> data) {
                if (code == Constant.Code.SUCCESS) {
                    if (data != null && data.size() > 0) {
                        onLineClick.onClick(data.get(0).getId() + "");
                        conData = data;
                        lineAdapter.setNewData(data);
                        lineAdapter.notifyDataSetChanged();
                    }
                } else {
                    ToastUtils.showShort(msg);
                }
            }
        });

        Api_Config.ins().getCpList(new JsonCallback<List<ChipsVO>>() {
            @Override
            public void onSuccess(int code, String msg, List<ChipsVO> data) {
                if (code == Constant.Code.SUCCESS) {
                    if (data != null && data.size() > 0) {
                        lotteryNames.add(data.get(0).getChinese());
                        names.add(data.get(0).getName());
                        lotteryClick.onClick(lotteryNames, names);
                        data.get(0).setCheck(true);
                        for (int i = 0; i < data.size(); i++) {
                            if (1 == data.get(i).getGameType()) {
                                data.remove(i);
                            }
                        }
                        chAdapter.setNewData(data);
                        chAdapter.notifyDataSetChanged();
                    }
                } else {
                    ToastUtils.showShort(msg);
                }

            }
        });
    }

    public void refreshRoomType(String text) {
        tvRoomset.setText(text);
    }

    public void changeToyState(LovenseToy lovenseToy) {
        tvToy.setText(getString(R.string.connected));
        tvToy.setTextColor(Color.parseColor("#F5FFBF"));
    }

    OnCaiClick lotteryClick;

    public void setLotteryClick(OnCaiClick onClick) {
        lotteryClick = onClick;
    }

    @Override
    public void onClick(View view) {
        if (ClickUtil.isFastDoubleClick()) return;
        switch (view.getId()) {
            case R.id.layout_root:
                ((AnchorLiveActivity) requireActivity()).hideBeauty();
                break;
            case R.id.layout_toy:
                ((AnchorLiveActivity) requireActivity()).searchJoy();
                break;
            case R.id.iv_close:
                requireActivity().finish();
                break;
            case R.id.iv_change_camera:
                ((AnchorLiveActivity) requireActivity()).switchCamera();
                break;
            case R.id.layout_beauty:
                ((AnchorLiveActivity) requireActivity()).toggleBeautySetting();
                break;
            case R.id.layout_roomset:
                ((AnchorLiveActivity) requireActivity()).showLiveRoomTypeSetDialog(true);
                break;
            case R.id.btn_openlive: //开始直播
                if (conData != null && conData.size() > 0) {
                    TXLiveBase.getInstance().setLicence(CommonApp.getInstance(),
                            conData.get(choicePosition).getLicenceUrl(),
                            conData.get(choicePosition).getLicenceKey());
                    Constant.LiveAppId = conData.get(choicePosition).getAppId();
                    ((AnchorLiveActivity) requireActivity()).hideBeauty();
                    ((AnchorLiveActivity) requireActivity()).startLive();
                }
                break;
        }
    }

    public interface OnCaiClick {
        void onClick(ArrayList<String> lotteryNames, ArrayList<String> names);
    }

    OnLineClick onLineClick;

    public interface OnLineClick {
        void onClick(String line);
    }

    public void setOnLineClick(OnLineClick onClick) {
        onLineClick = onClick;
    }
}

