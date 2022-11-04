package com.live.fox.adapter;

import android.text.Html;
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
import com.live.fox.App;
import com.live.fox.R;
import com.live.fox.base.BaseActivity;
import com.live.fox.base.DialogFramentManager;
import com.live.fox.common.JsonCallback;
import com.live.fox.dialog.bottomDialog.PurchaseCarDialog;
import com.live.fox.entity.MessageEvent;
import com.live.fox.entity.MyBagStoreListItemBean;
import com.live.fox.server.Api_Order;
import com.live.fox.server.BaseApi;
import com.live.fox.ui.mine.MyBagActivity;
import com.live.fox.ui.mine.MyBagAndStoreFragment;
import com.live.fox.utils.GlideUtils;
import com.live.fox.utils.OnClickFrequentlyListener;
import com.live.fox.utils.ToastUtils;
import com.live.fox.view.GradientTextView;

import org.greenrobot.eventbus.EventBus;
import org.jetbrains.annotations.NotNull;

import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.List;

public class MyBagAndStoreAdapter extends RecyclerView.Adapter<MyBagAndStoreAdapter.ViewHolder> {

    BaseActivity activity;
    LayoutInflater layoutInflater;
    int colors[] = null;
    List<MyBagStoreListItemBean> beans;
    SpannableString disable, renew;
    String purchaseStr,openStr;

    boolean isStore= true;

    public MyBagAndStoreAdapter(BaseActivity activity, List<MyBagStoreListItemBean> beans,boolean isStore) {
        this.activity = activity;
        layoutInflater = activity.getLayoutInflater();
        this.beans = beans;
        this.isStore = isStore;

        purchaseStr=activity.getString(R.string.buy);
        openStr=activity.getString(R.string.openStr);
        colors = activity.getResources().getIntArray(R.array.startingUseColor);
        disable = new SpannableString(activity.getString(R.string.forbiddenUse));
        disable.setSpan(new UnderlineSpan(), 0, disable.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

       // renew = new SpannableString(activity.getString(R.string.renew));
       // renew.setSpan(new UnderlineSpan(), 0, renew.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
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
        if (isStore) {
            holder.tvName.setText(bean.getName());
            holder.tvName.setTextColor(activity.getResources().getColor(R.color.color404040));
            holder.tvDes.setText(bean.getDescript());
            GlideUtils.loadDefaultImage(activity, bean.getLogUrl(), holder.ivCar1);
            if (bean.isHave()) {
                if (bean.isEnable()) {
                    // holder.gtvCommit.setVisibility(View.GONE);
                    holder.gtvCommit.setVisibility(View.VISIBLE);
                    holder.tvDisable.setVisibility(View.VISIBLE);
                    holder.tvDisable.setText(disable);
                    holder.gtvCommit.setText(activity.getString(R.string.renew));
                    holder.gtvCommit.setOnClickListener(new OnClickFrequentlyListener() {
                        @Override
                        public void onClickView(View view) {
                            DialogFramentManager.getInstance().showDialogAllowingStateLoss(
                                    activity.getSupportFragmentManager(),
                                    PurchaseCarDialog.getInstance(1, bean));
                        }
                    });

                    holder.tvDisable.setOnClickListener(new OnClickFrequentlyListener() {
                        @Override
                        public void onClickView(View view) {
                            myBagStoreClick.openCar(position,bean.getId(), false);
                        }
                    });
                } else {
                    holder.gtvCommit.setVisibility(View.VISIBLE);
                    holder.tvDisable.setVisibility(View.GONE);
                    holder.gtvCommit.setColors(colors);
                    holder.gtvCommit.setText(openStr);

                    holder.gtvCommit.setOnClickListener(new OnClickFrequentlyListener() { //启用座驾
                        @Override
                        public void onClickView(View view) {
                            myBagStoreClick.openCar(position,bean.getId(), true);
                        }
                    });

                }

            } else {
                holder.gtvCommit.setVisibility(View.VISIBLE);
                holder.tvDisable.setVisibility(View.GONE);
                holder.gtvCommit.setText(purchaseStr);
                holder.gtvCommit.setOnClickListener(new OnClickFrequentlyListener() {
                    @Override
                    public void onClickView(View view) {
                        if(!bean.isHave())
                        {
                            DialogFramentManager.getInstance().showDialogAllowingStateLoss(
                                    activity.getSupportFragmentManager(),
                                    PurchaseCarDialog.getInstance(1, bean));
                        }

                    }
                });
            }
            holder.layoutAni.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (myBagStoreClick != null) {
                        myBagStoreClick.startAni(position, bean.getAnimationUrl());
                    }
                }
            });
        } else {
            if (bean.getPropType() <= 2) {
                if (bean.getAmount() >1) {
                    String string = "<font color='#404040'>" +bean.getPropName()+ "</font>" +
                            "<font color='#F42C2C'>(" +bean.getAmount()+ activity.getResources().getString(R.string.ge)+ ")</font>";
                    holder.tvName.setText(Html.fromHtml(string));
                } else {
                    holder.tvName.setText(bean.getPropName());
                }
            } else {
                if (bean.getAmount() >1) {
                    String string = "<font color='#404040'>" +bean.getPropName()+ "</font>" +
                            "<font color='##F42C2C'>(" +bean.getAmount()+ activity.getResources().getString(R.string.day)+ ")</font>";
                    holder.tvName.setText(Html.fromHtml(string));
                } else {
                    holder.tvName.setText(bean.getPropName());
                }
            }
            holder.tvDes.setText(bean.getRemark());
            holder.ivCar2.setVisibility(View.GONE);
            holder.ivBo.setVisibility(View.GONE);
            GlideUtils.loadDefaultImage(activity, bean.getLogUrl(), holder.ivCar2);
            holder.gtvCommit.setVisibility(View.VISIBLE);
            holder.gtvCommit.setText(activity.getString(R.string.use));

        }




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
       // TextView tvRenew;

       //LinearLayout llUsing;
        GradientTextView gtvCommit;

        RoundRelativeLayout layoutAni;
        ImageView ivCar1;
        ImageView ivCar2;
        ImageView ivBo;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvDes = itemView.findViewById(R.id.tvDes);
            tvDisable = itemView.findViewById(R.id.tvDisable);
           // tvRenew = itemView.findViewById(R.id.tvRenew);
            gtvCommit = itemView.findViewById(R.id.gtvCommit);
           // llUsing = itemView.findViewById(R.id.llUsing);
            layoutAni = itemView.findViewById(R.id.rrlPlay);
            ivCar1 = itemView.findViewById(R.id.ivCar1);
            ivCar2 = itemView.findViewById(R.id.ivCar2);
            ivBo = itemView.findViewById(R.id.ivBo);
        }
    }


    public interface MyBagStoreClick{
        public void startAni(int pos, String url);
        public void openCar(int pos, int id, boolean isOpen);
    }

    private MyBagStoreClick myBagStoreClick;

    public void setMyBagStoreClick(MyBagStoreClick myBagStoreClick) {
        this.myBagStoreClick = myBagStoreClick;
    }
}
