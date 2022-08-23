package com.live.fox.adapter;

import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

import androidx.annotation.ColorRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.RadioButton;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.live.fox.R;
import com.live.fox.entity.response.MinuteTabItem;

import java.util.List;

/**
 * 一份快三
 * PK 10
 */
public class OneMinuteAdapter extends BaseQuickAdapter<MinuteTabItem, BaseViewHolder> {
    public OneMinuteAdapter() {
        super(R.layout.adapter_minute_item);
    }

    @Override
    protected void convert(BaseViewHolder helper, MinuteTabItem item) {
        helper.setText(R.id.tvGameFont, item.getTitle());
        View tvGameFont = helper.getView(R.id.tvGameFont);
        tvGameFont.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                tvGameFont.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) tvGameFont.getLayoutParams();
                if (tvGameFont.getWidth() > tvGameFont.getHeight()) {
                    lp.height = tvGameFont.getWidth();
                } else {
                    lp.width = tvGameFont.getHeight();
                }
                tvGameFont.setLayoutParams(lp);
            }
        });
        helper.setText(R.id.tvGameRatio, String.valueOf(item.getOdds()));
        RadioButton box = helper.getView(R.id.tvGameFont);
        box.setClickable(false);
        box.setChecked(item.check);
        helper.addOnClickListener(R.id.llItem);
    }


    public static class RecyclerSpace extends RecyclerView.ItemDecoration {

        public int space;
        private int color = -1;
        private Drawable mDivider;
        private Paint mPaint;
        private int type;

        public int getColor() {
            return color;
        }

        public void setColor(@ColorRes int color) {
            this.color = color;
        }

        public RecyclerSpace(int space) {
            this.space = space;
        }

        public RecyclerSpace(int space, int color) {
            this.space = space;
            this.color = color;
            mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            mPaint.setColor(color);
            mPaint.setStyle(Paint.Style.FILL);
            mPaint.setStrokeWidth(space * 2);
        }

        public RecyclerSpace(int space, int color, int type) {
            this.space = space;
            this.color = color;
            mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            mPaint.setColor(color);
            mPaint.setStyle(Paint.Style.FILL);
            mPaint.setStrokeWidth(space * 2);
            this.type = type;
        }

        public RecyclerSpace(int space, Drawable mDivider) {
            this.space = space;
            this.mDivider = mDivider;
        }

        @Override
        public void getItemOffsets(@NonNull Rect outRect, @NonNull View view,
                                   RecyclerView parent, @NonNull RecyclerView.State state) {
            if (parent.getLayoutManager() != null) {
                if (parent.getLayoutManager() instanceof GridLayoutManager) {
                    int spanCount = ((GridLayoutManager) parent.getLayoutManager()).getSpanCount();
                    outRect.set(space, 0, space, space / spanCount);

                }
            }
        }
    }
}
