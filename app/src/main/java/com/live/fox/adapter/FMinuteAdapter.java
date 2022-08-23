package com.live.fox.adapter;

import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import androidx.annotation.ColorRes;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.live.fox.R;
import com.live.fox.entity.response.MinuteTabItem;

import java.util.List;

/*****************************************************************
 * Created   on 2020\1\7 0007 下午
 * desciption:
 *****************************************************************/
public class FMinuteAdapter extends BaseQuickAdapter<MinuteTabItem, BaseViewHolder> {
    private int isVisiable=0;
    public FMinuteAdapter(@Nullable List<MinuteTabItem> data) {
        super(R.layout.adapter_f_item, data);

    }

    @Override
    protected void convert(BaseViewHolder helper, MinuteTabItem item) {
        LinearLayout tvGameFont=helper.getView(R.id.tvGameFont);
        ImageView iv1=helper.getView(R.id.iv1);
        ImageView iv2=helper.getView(R.id.iv2);
        helper.setText(R.id.tvGameRatio, String.valueOf(item.getOdds()));
        if (0==isVisiable){
            iv2.setVisibility(View.GONE);
            switch (helper.getAdapterPosition()){
                case 0:
                    iv1.setImageResource(R.drawable.fllu);
                    break;
                case 1:
                    iv1.setImageResource(R.drawable.flxie);
                    break;
                case 2:
                    iv1.setImageResource(R.drawable.frji);
                    break;
                case 3:
                    iv1.setImageResource(R.drawable.frfish);
                    break;
                case 4:
                    iv1.setImageResource(R.drawable.flpangxie);
                    break;
                case 5:
                    iv1.setImageResource(R.drawable.flxia);
                    break;
            }
        }else if(1==isVisiable){
            iv2.setVisibility(View.VISIBLE);
            switch (helper.getAdapterPosition()){
                case 0:
                    iv1.setImageResource(R.drawable.fllu);
                    iv2.setImageResource(R.drawable.fllu);
                    break;
                case 1:
                    iv1.setImageResource(R.drawable.flxie);
                    iv2.setImageResource(R.drawable.flxie);
                    break;
                case 2:
                    iv1.setImageResource(R.drawable.frji);
                    iv2.setImageResource(R.drawable.frji);
                    break;
                case 3:
                    iv1.setImageResource(R.drawable.frfish);
                    iv2.setImageResource(R.drawable.frfish);
                    break;
                case 4:
                    iv1.setImageResource(R.drawable.flpangxie);
                    iv2.setImageResource(R.drawable.flpangxie);
                    break;
                case 5:
                    iv1.setImageResource(R.drawable.flxia);
                    iv2.setImageResource(R.drawable.flxia);
                    break;
            }
        }else if(2==isVisiable){
            iv2.setVisibility(View.VISIBLE);
            switch (helper.getAdapterPosition()){
                case 0:
                    iv1.setImageResource(R.drawable.fllu);
                    iv2.setImageResource(R.drawable.flxie);
                    break;
                case 1:
                    iv1.setImageResource(R.drawable.fllu);
                    iv2.setImageResource(R.drawable.frji);
                    break;
                case 2:
                    iv1.setImageResource(R.drawable.fllu);
                    iv2.setImageResource(R.drawable.frfish);
                    break;
                case 3:
                    iv1.setImageResource(R.drawable.fllu);
                    iv2.setImageResource(R.drawable.flpangxie);
                    break;
                case 4:
                    iv1.setImageResource(R.drawable.fllu);
                    iv2.setImageResource(R.drawable.flxia);
                    break;

                case 5:
                    iv1.setImageResource(R.drawable.flxie);
                    iv2.setImageResource(R.drawable.frji);
                    break;
                case 6:
                    iv1.setImageResource(R.drawable.flxie);
                    iv2.setImageResource(R.drawable.frfish);
                    break;
                case 7:
                    iv1.setImageResource(R.drawable.flxie);
                    iv2.setImageResource(R.drawable.flpangxie);
                    break;
                case 8:
                    iv1.setImageResource(R.drawable.flxie);
                    iv2.setImageResource(R.drawable.flxia);
                    break;

                case 9:
                    iv1.setImageResource(R.drawable.frji);
                    iv2.setImageResource(R.drawable.frfish);
                    break;
                case 10:
                    iv1.setImageResource(R.drawable.frji);
                    iv2.setImageResource(R.drawable.flpangxie);
                    break;
                case 11:
                    iv1.setImageResource(R.drawable.frji);
                    iv2.setImageResource(R.drawable.flxia);
                    break;

                case 12:
                    iv1.setImageResource(R.drawable.frfish);
                    iv2.setImageResource(R.drawable.flpangxie);
                    break;
                case 13:
                    iv1.setImageResource(R.drawable.frfish);
                    iv2.setImageResource(R.drawable.flxia);
                    break;

                case 14:
                    iv1.setImageResource(R.drawable.flpangxie);
                    iv2.setImageResource(R.drawable.flxia);
                    break;
            }
        }

        if (item.check){
            tvGameFont.setBackgroundResource(R.drawable.game_ratio_check_bg);
        }else {
            tvGameFont.setBackgroundResource(R.drawable.game_ratio_funcheck_bg);
        }
        helper.addOnClickListener(R.id.llItem);
    }

    public void setDataNotifacation(int isVisiable){
        this.isVisiable=isVisiable;
        notifyDataSetChanged();
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
        public void getItemOffsets(Rect outRect, View view,
                                   RecyclerView parent, RecyclerView.State state) {
            if (parent.getLayoutManager() != null) {
                if (parent.getLayoutManager() instanceof GridLayoutManager) {
                    int spanCount = ((GridLayoutManager) parent.getLayoutManager()).getSpanCount();
                    outRect.set(space, 0, space, space / spanCount);

                }
            }

        }
    }
}
