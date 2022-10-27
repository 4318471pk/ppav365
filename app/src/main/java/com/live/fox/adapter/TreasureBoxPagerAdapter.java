package com.live.fox.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.live.fox.entity.TreasureItemBean;

import java.util.List;

public class TreasureBoxPagerAdapter extends RecyclerView.Adapter<TreasureBoxPagerAdapter.TreasureBoxHolder> {

    Context context;
    List<List<TreasureItemBean>> lists;
    int pageIndex=0;

    public TreasureBoxPagerAdapter(Context context, List<List<TreasureItemBean>> lists) {
        this.lists=lists;
        this.context=context;
    }

    public void setPageIndex(int pageIndex)
    {
        this.pageIndex=pageIndex;
    }


    @NonNull
    @Override
    public TreasureBoxHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull TreasureBoxHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        if(lists.get(pageIndex)==null || lists.get(pageIndex).size()==0)
        {
            return 0;
        }

        int totalSize=lists.get(pageIndex).size();
        int pageSize=totalSize%8==0?(totalSize/8):(totalSize/8+1);
        return pageSize;
    }

    public static final class TreasureBoxHolder extends RecyclerView.ViewHolder {
        public TreasureBoxHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
