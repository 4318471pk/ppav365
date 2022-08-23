package com.live.fox.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.flyco.roundview.RoundLinearLayout;
import com.flyco.roundview.RoundTextView;
import com.live.fox.R;
import com.live.fox.adapter.OneMinuteAdapter;
import com.live.fox.entity.response.ChipsVO;
import com.live.fox.svga.BetCartDataManager;
import com.live.fox.mvp.MvpDialogFragment;
import com.live.fox.utils.ToastUtils;

import java.util.List;


/**
 * 筹码弹窗
 */
public class ChipsDialogFragment extends MvpDialogFragment implements View.OnClickListener {

    private RecyclerView rvChips;
    private RoundTextView rtvOk;
    private BaseQuickAdapter<ChipsVO, BaseViewHolder> mChipsAdapter;
    private int flag;
    private EditTextDialog editTextDialog;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Dialog dialog = new Dialog(getActivity(), R.style.DialogDefault);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.fragment_chips);
        dialog.setCanceledOnTouchOutside(true); // 外部點擊取消
        // 設置寬度爲屏寬, 靠近屏幕底部。
        Window window = dialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        // 寬度持平
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setGravity(Gravity.BOTTOM);
        window.setDimAmount(0.05f);
        window.setBackgroundDrawableResource(R.color.transparent);
        window.setWindowAnimations(R.style.ActionSheetDialogAnimation);
        window.setAttributes(lp);
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_chips, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            flag = bundle.getInt("flag");
        }
        rvChips = view.findViewById(R.id.rvChips);
        rtvOk = view.findViewById(R.id.rtvOk);
        rtvOk.setOnClickListener(this);
        GridLayoutManager grid = new GridLayoutManager(getActivity(), 4);
        rvChips.setLayoutManager(grid);

        handleDialog();

        rvChips.setAdapter(mChipsAdapter = new BaseQuickAdapter<ChipsVO,
                BaseViewHolder>(R.layout.adapter_chips_item,
                ChipsVO.chipsVOS()) {
            @Override
            protected void convert(BaseViewHolder helper, ChipsVO it) {
                helper.getView(R.id.tvChip).setBackgroundResource(it.resId);
                RoundLinearLayout rll = (RoundLinearLayout) helper.getView(R.id.rll);
                helper.addOnClickListener(R.id.rll);
                if (it.check) {
                    rll.getDelegate().setStrokeWidth(1);
                } else {
                    rll.getDelegate().setStrokeWidth(0);
                }

                TextView chip = helper.getView(R.id.tvChip);
                if (helper.getLayoutPosition() == ChipsVO.chipsVOS().size() - 1) {
                    chip.setTextSize(8);
                } else {
                    chip.setTextSize(16);
                }
                helper.setText(R.id.tvChip, it.value);
            }
        });

        mChipsAdapter.setOnItemChildClickListener((adapter, view1, position) -> {
            List<ChipsVO> data = adapter.getData();
            for (int i = 0; i < data.size(); i++) {
                if (position == i) {
                    ChipsVO vo = data.get(i);
                    vo.check = true;
                } else {
                    data.get(i).check = false;
                }
            }

            if (position == ChipsVO.chipsVOS().size() - 1) {
                editTextDialog.show(getChildFragmentManager(), EditTextDialog.PARCELABLE_KEY);
            } else {
                BetCartDataManager.getInstance().setChipsIndex(position);
            }

            mChipsAdapter.notifyDataSetChanged();
        });
        OneMinuteAdapter.RecyclerSpace recyclerSpace = new OneMinuteAdapter.RecyclerSpace(8);
        rvChips.addItemDecoration(recyclerSpace);
        mChipsAdapter.getData().get(BetCartDataManager.getInstance().getChipsIndex()).check = true;
        mChipsAdapter.notifyDataSetChanged();
    }

    /**
     * 处理编辑弹窗
     */
    private void handleDialog() {
        editTextDialog = new EditTextDialog();
        Bundle args = new Bundle();
        EditTextDialog.EditTextEntity entity = new EditTextDialog.EditTextEntity();
        entity.setTitleStr(getString(R.string.modifyBet));
        entity.setSubmitStr(getString(R.string.picture_confirm));
        entity.setEditTextHint(getString(R.string.edit_text_dialog_hint));
        args.putParcelable(EditTextDialog.PARCELABLE_KEY, entity);
        editTextDialog.setArguments(args);
        editTextDialog.setSubmitClick(editContent -> {
            if (TextUtils.isEmpty(editContent)) {
                ToastUtils.showShort(getString(R.string.chips_multiple_5));
            }
            int anInt = 0;
            if (!TextUtils.isEmpty(editContent)) {
                anInt = Integer.parseInt(editContent);
            }

            if (anInt % 5 != 0) {
                ToastUtils.showShort(getString(R.string.chips_multiple_5));
                return;
            }

            if (anInt < 5) {
                ToastUtils.showShort(getString(R.string.chips_small_toast));
                return;
            }

            if (anInt > 20000) {
                ToastUtils.showShort(getString(R.string.chips_big_toast));
                return;
            }
            int index = ChipsVO.chipsVOS().size() - 1;
            ChipsVO chips = mChipsAdapter.getData().get(index);
            chips.value = String.valueOf(anInt);
            BetCartDataManager.getInstance().setChipsIndex(index);
            mChipsAdapter.notifyDataSetChanged();
            editTextDialog.dismiss();
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rtvOk:
                dismiss();
                break;
        }
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        if (isAdded()) {
            Fragment fragment;
            if (1 == flag) {
                fragment = requireActivity().getSupportFragmentManager().findFragmentByTag(MinuteGameDialogFragment.class.getSimpleName());
                if (fragment != null) {
                    ((MinuteGameDialogFragment) fragment).setItemInputValue();
                }
            } else if (3 == flag) {
                fragment = requireActivity().getSupportFragmentManager().findFragmentByTag(FGameDialogFragment.class.getSimpleName());
                if (fragment != null) {
                    ((FGameDialogFragment) fragment).setItemInputValue();
                }
            } else if (2 == flag) {
                fragment = requireActivity().getSupportFragmentManager().findFragmentByTag(HNDialogFragment.class.getSimpleName());
                if (fragment != null) {
                    ((HNDialogFragment) fragment).setItemInputValue();
                }
            }
        }
    }

    public static ChipsDialogFragment newInstance(int flag) {
        ChipsDialogFragment fragment = new ChipsDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("flag", flag);
        fragment.setArguments(bundle);
        return fragment;
    }
}
