package com.live.fox.ui.mine.activity.gamerecord;

import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
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
import com.live.fox.entity.LsResult;
import com.live.fox.server.Api_Cp;
import com.live.fox.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;


public class SXDialogFragment extends DialogFragment implements View.OnClickListener {

    private RecyclerView rv;
    private Button btCz;
    private Button btSure;
    private BaseQuickAdapter supportAdapter;
    private int suportSelPos = 0;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Dialog dialog = new Dialog(getActivity(), R.style.DialogDefault);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.fragment_xs);
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
        return inflater.inflate(R.layout.fragment_xs, container, false);
    }

    private void getAllLotteryLatestResult() {
        Api_Cp.ins().getLatestResult(new JsonCallback<List<LsResult>>() {
            @Override
            public void onSuccess(int code, String msg, List<LsResult> data) {
                if (code == Constant.Code.SUCCESS && data != null && data.size() > 0) {
                } else {
                    ToastUtils.showShort(msg);
                }
            }
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
//        getAllLotteryLatestResult();
    }

    public static SXDialogFragment newInstance() {
        SXDialogFragment fragment = new SXDialogFragment();
        return fragment;
    }

    private void initView(View view) {
        rv = (RecyclerView) view.findViewById(R.id.rv);
        btCz = (Button) view.findViewById(R.id.bt_cz);
        btSure = (Button) view.findViewById(R.id.bt_sure);
        btCz.setOnClickListener(this);
        btSure.setOnClickListener(this);
        rv.setNestedScrollingEnabled(false);
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 3, GridLayoutManager.VERTICAL, false);
        rv.setLayoutManager(layoutManager);
        rv.addItemDecoration(new RecyclerSpace(5));
        rv.setAdapter(supportAdapter = new BaseQuickAdapter(R.layout.item_offcial_recharge, new ArrayList<String>()) {

            @Override
            protected void convert(BaseViewHolder helper, Object item) {
                TextView cbOffcial = (TextView) helper.getView(R.id.cbOffcial);

                if (suportSelPos == helper.getLayoutPosition()) {
                    cbOffcial.setTextColor(Color.WHITE);
                    cbOffcial.setBackgroundResource(R.drawable.shape_red_corners_5);
                } else {
                    cbOffcial.setTextColor(Color.parseColor("#999999"));
                    cbOffcial.setBackgroundResource(R.drawable.shape_white_storke_1_corners_5);
                }
                cbOffcial.setText((CharSequence) item);
            }
        });

        supportAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                suportSelPos = position;
                supportAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_cz:

                break;

        }
    }
}
