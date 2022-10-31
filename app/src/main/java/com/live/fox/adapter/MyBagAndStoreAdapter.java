package com.live.fox.adapter;

import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.flyco.roundview.RoundRelativeLayout;
import com.live.fox.R;
import com.live.fox.base.BaseActivity;
import com.live.fox.base.DialogFramentManager;
import com.live.fox.dialog.bottomDialog.PurchaseCarDialog;
import com.live.fox.entity.MyBagStoreListItemBean;
import com.live.fox.ui.mine.MyBagActivity;
import com.live.fox.utils.GlideUtils;
import com.live.fox.utils.OnClickFrequentlyListener;
import com.live.fox.view.GradientTextView;

import org.jetbrains.annotations.NotNull;

import java.net.MalformedURLException;
import java.util.List;

public class MyBagAndStoreAdapter extends RecyclerView.Adapter<MyBagAndStoreAdapter.ViewHolder> {

    BaseActivity activity;
    LayoutInflater layoutInflater;
    int colors[] = null;
    List<MyBagStoreListItemBean> beans;
    SpannableString disable, renew;
    String purchaseStr,openStr;

    public MyBagAndStoreAdapter(BaseActivity activity, List<MyBagStoreListItemBean> beans) {
        this.activity = activity;
        layoutInflater = activity.getLayoutInflater();
        this.beans = beans;

        purchaseStr=activity.getString(R.string.buy);
        openStr=activity.getString(R.string.openStr);
        colors = activity.getResources().getIntArray(R.array.startingUseColor);
        disable = new SpannableString(activity.getString(R.string.forbiddenUse));
        disable.setSpan(new UnderlineSpan(), 0, disable.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        renew = new SpannableString(activity.getString(R.string.renew));
        renew.setSpan(new UnderlineSpan(), 0, renew.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        return new ViewHolder(layoutInflater.inflate(R.layout.item_my_bagandstore, null, false));
//        if (viewType == 0) {
//            return new ViewHolder(layoutInflater.inflate(R.layout.item_my_bagandstore, null, false));
//        } else {
//            return new ViewHolder(layoutInflater.inflate(R.layout.item_my_bagandstore2, null, false));
//        }
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        MyBagStoreListItemBean bean = beans.get(position);

        //if (getItemViewType(position) == 0) {
            holder.tvName.setText(bean.getName());
            holder.tvDes.setText(bean.getDescript());
        GlideUtils.loadDefaultImage(activity, bean.getLogUrl(), holder.ivCar);

            if (bean.isPurchased()) {
                if (bean.isUsing()) {
                    holder.gtvCommit.setVisibility(View.GONE);
                    holder.llUsing.setVisibility(View.VISIBLE);
                    holder.tvDisable.setText(disable);
                    holder.tvRenew.setText(renew);
                    holder.tvRenew.setOnClickListener(new OnClickFrequentlyListener() {
                        @Override
                        public void onClickView(View view) {
                            DialogFramentManager.getInstance().showDialogAllowingStateLoss(
                                    activity.getSupportFragmentManager(),
                                    PurchaseCarDialog.getInstance(PurchaseCarDialog.renew, bean));
                        }
                    });
                } else {
                    holder.gtvCommit.setVisibility(View.VISIBLE);
                    holder.llUsing.setVisibility(View.GONE);
                    holder.gtvCommit.setColors(colors);
                    holder.gtvCommit.setText(openStr);

                }



            } else {
                holder.gtvCommit.setVisibility(View.VISIBLE);
                holder.llUsing.setVisibility(View.GONE);
                holder.gtvCommit.setText(purchaseStr);

                holder.gtvCommit.setOnClickListener(new OnClickFrequentlyListener() {
                    @Override
                    public void onClickView(View view) {
                        if(!bean.isPurchased())
                        {
                            DialogFramentManager.getInstance().showDialogAllowingStateLoss(
                                    activity.getSupportFragmentManager(),
                                    PurchaseCarDialog.getInstance(PurchaseCarDialog.purchase, bean));
                        }

                    }
                });
            }

//        holder.gtvCommit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (myBagStoreClick != null) {
//                    myBagStoreClick.buy(position);
//                }
//            }
//        });

        holder.layoutAni.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (myBagStoreClick != null) {
                    myBagStoreClick.startAni(position, bean.getAnimationUrl());
                }
            }
        });

    }

//    @Override
//    public int getItemViewType(int position) {
//        return position == 1 ? 1 : 0;
//    }

    @Override
    public int getItemCount() {
        return beans.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName;
        TextView tvDes;
        TextView tvDisable;
        TextView tvRenew;

        LinearLayout llUsing;
        GradientTextView gtvCommit;

        RoundRelativeLayout layoutAni;
        ImageView ivCar;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvDes = itemView.findViewById(R.id.tvDes);
            tvDisable = itemView.findViewById(R.id.tvDisable);
            tvRenew = itemView.findViewById(R.id.tvRenew);
            gtvCommit = itemView.findViewById(R.id.gtvCommit);
            llUsing = itemView.findViewById(R.id.llUsing);
            layoutAni = itemView.findViewById(R.id.rrlPlay);
            ivCar = itemView.findViewById(R.id.ivCar);
        }
    }


    public interface MyBagStoreClick{
        public void startAni(int pos, String url);
        public void buy(int pos);
    }

    private MyBagStoreClick myBagStoreClick;

    public void setMyBagStoreClick(MyBagStoreClick myBagStoreClick) {
        this.myBagStoreClick = myBagStoreClick;
    }
}
