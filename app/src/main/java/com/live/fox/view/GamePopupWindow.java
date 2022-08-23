package com.live.fox.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.live.fox.R;
import com.live.fox.adapter.devider.RecyclerSpace;

import java.util.ArrayList;
import java.util.List;


public class GamePopupWindow extends PopupWindow {
    private RecyclerView rv_window;
    private BaseQuickAdapter adapter;
    private int currentPosition = 0;
    private Context mContext;

    public GamePopupWindow(Context context) {
        super(context);
        mContext = context;
        View inflate = LayoutInflater.from(context).inflate(R.layout.game_popup_window, null);
        this.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
        this.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        this.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        this.setFocusable(true);
        this.setOutsideTouchable(true);
        this.update();
        this.setContentView(inflate);
        rv_window = inflate.findViewById(R.id.rv_window);
        GridLayoutManager layoutManager = new GridLayoutManager(context, 3, GridLayoutManager.VERTICAL, false);
        rv_window.setLayoutManager(layoutManager);
        rv_window.addItemDecoration(new RecyclerSpace(5));
        rv_window.setAdapter(adapter = new BaseQuickAdapter(R.layout.item_offcial_recharge, new ArrayList<String>()) {

            @Override
            protected void convert(BaseViewHolder helper, Object item) {
                TextView cbOffcial = (TextView) helper.getView(R.id.cbOffcial);
                if (currentPosition == helper.getLayoutPosition()) {
                    cbOffcial.setTextColor(Color.WHITE);
                    cbOffcial.setBackgroundResource(R.drawable.shape_red_corners_5);
                } else {
                    cbOffcial.setTextColor(Color.parseColor("#808080"));
                    cbOffcial.setBackgroundResource(R.drawable.shape_white_storke_1_corners_5);
                }
                cbOffcial.setText((CharSequence) item);
            }
        });

        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (onItemClickListener != null) {
                    currentPosition = position;
                    adapter.notifyDataSetChanged();
                    onItemClickListener.onItemClick(position);
                }
            }
        });


    }

    public void setDatas(List<String> datas) {
        adapter.setNewData(datas);
    }

    public void setAlphBackground(float bgAlpha) {
        WindowManager.LayoutParams lp = ((Activity) mContext).getWindow()
                .getAttributes();
        lp.alpha = bgAlpha;
        ((Activity) mContext).getWindow().setAttributes(lp);
    }

    @Override
    public void dismiss() {
        super.dismiss();
//setAlphBackground(1.0f);
        if (onDismissClickListener != null) {
            onDismissClickListener.onDismissClick();
        }
    }

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(int positon);
    }

    private OnDismissClickListener onDismissClickListener;

    public void setOnDismissClickListener(OnDismissClickListener onDismissClickListener) {
        this.onDismissClickListener = onDismissClickListener;
    }

    public interface OnDismissClickListener {
        void onDismissClick();
    }
}
