package com.tencent.demo.utils;

import android.graphics.Rect;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

public class PropertyItemDecoration extends RecyclerView.ItemDecoration {
    private int margin;

    public PropertyItemDecoration(int margin) {
        this.margin = margin;
    }
    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.right = margin;
        outRect.left = margin;
        if (parent.getChildAdapterPosition(view) == 0) {
            outRect.left = margin*2;
        }else if(parent.getChildAdapterPosition(view) == parent.getAdapter().getItemCount()-1){
            outRect.right = margin*2;
        }
    }
}
