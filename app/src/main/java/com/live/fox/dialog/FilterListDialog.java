package com.live.fox.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.live.fox.R;
import com.live.fox.adapter.FilterDialogAdapter;
import com.live.fox.entity.FilterDialogEntity;
import com.live.fox.entity.FilterItemEntity;
import com.live.fox.ui.mine.TransactionActivity;


/**
 * 过滤弹窗
 * 根据列表中的条件选择过滤数据
 */
public class FilterListDialog extends DialogFragment {

    public static final String FILTER_DIALOG_POSITION = "filter dialog position";

    private FilterDialogEntity dialogEntity;
    private FilterDialogAdapter adapter;
    private int selectPosition;
    private Dialog dialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.dialog_list_filter, container, false);
        initView(root);
        return root;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        if (getArguments() != null) {
            dialogEntity = getArguments().getParcelable(FILTER_DIALOG_POSITION);
            if (dialogEntity != null) {
                initData();
            }
        }

        //背景透明
        dialog = super.onCreateDialog(savedInstanceState);
        Window window = dialog.getWindow();
        window.setGravity(Gravity.TOP | Gravity.START);
        WindowManager.LayoutParams params = window.getAttributes();
        params.y = dialogEntity.getPosition();
        window.setAttributes(params);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        window.setDimAmount(0f);
        return dialog;
    }

    private void initView(View root) {
        RecyclerView recyclerView = root.findViewById(R.id.dialog_filter_list);
        GridLayoutManager layoutManager = new GridLayoutManager(requireContext(), 3);
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return setSpanSize(position);
            }
        });
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);
    }

    private int setSpanSize(int position) {
        int count;
        int length = dialogEntity.getFilterItems().get(position).getName().length();
        if (length > 15) {
            count = 2;
        } else {
            count = 1;
        }
        return count;
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        if (requireActivity() instanceof TransactionActivity) {
            TransactionActivity activity = (TransactionActivity) requireActivity();
            activity.setTextStyle(false);
        }
    }

    /**
     * 过滤数据
     */
    public void initData() {
        adapter = new FilterDialogAdapter();
        dialogEntity.getFilterItems().get(selectPosition).setSelect(true);
        adapter.setOnItemClickListener((adapter, view, position) -> {
            FilterItemEntity itemEntity = dialogEntity.getFilterItems().get(position);
            TransactionActivity activity = (TransactionActivity) requireActivity();
            if (itemEntity != null) {
                activity.onFilterClick(itemEntity);
                if (selectPosition != position) {
                    //取消上一次选中的
                    FilterItemEntity entity = dialogEntity.getFilterItems().get(selectPosition);
                    entity.setSelect(false);
                    itemEntity.setSelect(true);
                    adapter.notifyItemChanged(selectPosition);
                    selectPosition = position;
                    adapter.notifyItemChanged(selectPosition);
                }
            }
        });

        adapter.addData(dialogEntity.getFilterItems());
    }

    @Override
    public void onResume() {
        super.onResume();
        WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setAttributes(params);
    }

}
