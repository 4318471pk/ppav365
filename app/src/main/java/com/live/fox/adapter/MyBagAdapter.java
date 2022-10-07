package com.live.fox.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.live.fox.R;
import com.live.fox.entity.MyBagListItemBean;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class MyBagAdapter  extends BaseQuickAdapter<MyBagListItemBean,BaseViewHolder> {

    public MyBagAdapter() {
        super(R.layout.item_my_bag);
    }

    @Override
    protected void convert(BaseViewHolder helper, MyBagListItemBean item) {
//        helper.setText(R.id.tvDes,item.getDes());
//        helper.setText(R.id.tvName,item.getName());
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull BaseViewHolder holder, int position) {
        MyBagListItemBean item=(MyBagListItemBean)getData().get(position);
        holder.setText(R.id.tvDes,item.getDes());
        holder.setText(R.id.tvName,item.getName());
    }


}
