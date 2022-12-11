package com.live.fox.adapter;

import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.UnderlineSpan;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.flyco.roundview.RoundRelativeLayout;
import com.live.fox.R;
import com.live.fox.base.BaseActivity;
import com.live.fox.base.DialogFramentManager;
import com.live.fox.dialog.bottomDialog.PurchaseCarDialog;
import com.live.fox.entity.MyBagStoreListItemBean;
import com.live.fox.utils.GlideUtils;
import com.live.fox.utils.OnClickFrequentlyListener;
import com.live.fox.utils.ScreenUtils;
import com.live.fox.utils.SpanUtils;
import com.live.fox.view.GradientTextView;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class MyBagAndStoreAdapter extends RecyclerView.Adapter<MyBagAndStoreAdapter.ViewHolder> {

    BaseActivity activity;
    LayoutInflater layoutInflater;
    int blueColors[] = null;
    int orangeColors[]=null;
    List<MyBagStoreListItemBean> beans;
    SpannableString disable, renew;
    String purchaseStr,openStr;
    int dip1;

    boolean isStore= true;

    public MyBagAndStoreAdapter(BaseActivity activity, List<MyBagStoreListItemBean> beans,boolean isStore) {
        this.activity = activity;
        layoutInflater = activity.getLayoutInflater();
        this.beans = beans;
        this.isStore = isStore;

        purchaseStr=activity.getString(R.string.buy);
        openStr=activity.getString(R.string.openStr);
        blueColors = activity.getResources().getIntArray(R.array.startingUseColor);
        orangeColors=activity.getResources().getIntArray(R.array.myBagBuyColor);
        disable = new SpannableString(activity.getString(R.string.forbiddenUse));
        disable.setSpan(new UnderlineSpan(), 0, disable.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        dip1= ScreenUtils.dp2px(activity,1);

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
            GlideUtils.loadDefaultImage(activity, bean.getLogUrl(),0,R.mipmap.img_error, holder.ivCar1);

            holder.gtvCommit.setVisibility(View.VISIBLE);
            holder.tvDisable.setVisibility(View.GONE);
            holder.gtvCommit.setText(purchaseStr);
            holder.gtvCommit.setTextSize(TypedValue.COMPLEX_UNIT_SP,14);
            holder.gtvCommit.setTextColor(0xffffffff);
            holder.gtvCommit.setGradientBackground(orangeColors,dip1*12);
            holder.gtvCommit.setOnClickListener(new OnClickFrequentlyListener() {
                @Override
                public void onClickView(View view) {
                    PurchaseCarDialog purchaseCarDialog= PurchaseCarDialog.getInstance(1, bean);
                    purchaseCarDialog.setOnBuyVehicleSuccessListener(new PurchaseCarDialog.OnBuyVehicleSuccessListener() {
                        @Override
                        public void onBuySuccess() {
                            if(myBagStoreClick!=null)
                            {
                                myBagStoreClick.buySuccess();
                            }
                        }
                    });
                    DialogFramentManager.getInstance().showDialogAllowingStateLoss(
                            activity.getSupportFragmentManager(),
                            purchaseCarDialog);

                }
            });

            holder.layoutAni.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (myBagStoreClick != null) {
                        myBagStoreClick.startAni(position, bean.getAnimationUrl());
                    }
                }
            });
        } else {
            //道具类型:1礼物2坐骑3改名卡4贵族体验卡5靓号
            if (bean.getPropType()==1) {
                if (bean.getAmount() >1) {
                    SpanUtils spanUtils=new SpanUtils();
                    spanUtils.append(bean.getPropName()).setForegroundColor(0xff404040);
                    StringBuilder sb=new StringBuilder();
                    sb.append("(").append(bean.getAmount()).append(")").append(activity.getResources().getString(R.string.ge));
                    spanUtils.append(sb.toString()).setForegroundColor(0xffF42C2C);
                    holder.tvName.setText(spanUtils.create());
                } else {
                    holder.tvName.setText(bean.getPropName());
                }
            } else {
                if (bean.getAmount() >1) {
                    SpanUtils spanUtils=new SpanUtils();
                    spanUtils.append(bean.getPropName()).setForegroundColor(0xff404040);
                    StringBuilder sb=new StringBuilder();
                    sb.append("(").append(bean.getAmount()).append(")").append(activity.getResources().getString(R.string.day));
                    spanUtils.append(sb.toString()).setForegroundColor(0xffF42C2C);
                    holder.tvName.setText(spanUtils.create());
                } else {
                    holder.tvName.setText(bean.getPropName());
                }
            }
            holder.tvDes.setText(bean.getRemark());
            if(bean.getPropType()==2)
            {
                holder.ivBo.setVisibility(View.VISIBLE);
            }
            else
            {
                holder.ivBo.setVisibility(View.GONE);
            }

            holder.layoutAni.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (myBagStoreClick != null && bean.getPropType()==2) {
                        myBagStoreClick.startAni(position, bean.getAnimationUrl());
                    }
                }
            });
//            holder.gtvCommit.setOnClickListener(new OnClickFrequentlyListener() { //启用座驾
//                @Override
//                public void onClickView(View view) {
//                    myBagStoreClick.openCar(position,bean.getPropId(), true);
//                }
//            });

//            holder.gtvCommit.setVisibility(View.VISIBLE);
//            if(bean.getStatus()==1)
//            {
//                holder.gtvCommit.setText(activity.getString(R.string.using));
//                holder.gtvCommit.setEnabled(false);
//            }
//            else
//            {
//                holder.gtvCommit.setText(activity.getString(R.string.use));
//                holder.gtvCommit.setEnabled(true);
//            }

            holder.tvDes.setText(bean.getDescript());
//            holder.gtvCommit.setDirection(GradientTextView.DIRECTION.LEFT);
//            holder.gtvCommit.setGradientBackground(activity.getResources().getIntArray(R.array.identificationColor),12*dip1);
            GlideUtils.loadDefaultImage(activity, bean.getLogUrl(),0,R.mipmap.img_error, holder.ivCar1);

            if (bean.getStatus()==1) {
                // holder.gtvCommit.setVisibility(View.GONE);
                holder.gtvCommit.setVisibility(View.VISIBLE);
                holder.tvDisable.setVisibility(View.VISIBLE);
                holder.tvDisable.setText(disable);
                holder.gtvCommit.setText(activity.getString(R.string.renew));
                holder.gtvCommit.setTextColor(0xffffffff);
                holder.gtvCommit.setTextSize(TypedValue.COMPLEX_UNIT_SP,13);
                holder.gtvCommit.setGradientBackground(blueColors,12*dip1);

                holder.gtvCommit.setOnClickListener(new OnClickFrequentlyListener() {
                    @Override
                    public void onClickView(View view) {
                        PurchaseCarDialog purchaseCarDialog= PurchaseCarDialog.getInstance(1, bean);
                        purchaseCarDialog.setOnBuyVehicleSuccessListener(new PurchaseCarDialog.OnBuyVehicleSuccessListener() {
                            @Override
                            public void onBuySuccess() {
                                if(myBagStoreClick!=null)
                                {
                                    myBagStoreClick.buySuccess();
                                }
                            }
                        });
                        DialogFramentManager.getInstance().showDialogAllowingStateLoss(
                                activity.getSupportFragmentManager(),
                                purchaseCarDialog);
                    }
                });

                holder.tvDisable.setOnClickListener(new OnClickFrequentlyListener() {
                    @Override
                    public void onClickView(View view) {
                        myBagStoreClick.openCar(position,bean.getPropId(), false);
                    }
                });
            } else {
                holder.gtvCommit.setVisibility(View.VISIBLE);
                holder.tvDisable.setVisibility(View.GONE);
                holder.gtvCommit.setText(openStr);
                holder.gtvCommit.setTextSize(TypedValue.COMPLEX_UNIT_SP,14);
                holder.gtvCommit.setTextColor(0xffA800FF);
                holder.gtvCommit.setStokeBackground(0xffA800FF,12*dip1,dip1);

                holder.gtvCommit.setOnClickListener(new OnClickFrequentlyListener() { //启用座驾
                    @Override
                    public void onClickView(View view) {
                        myBagStoreClick.openCar(position,bean.getPropId(), true);
                    }
                });
            }
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
            ivBo = itemView.findViewById(R.id.ivBo);
        }
    }


    public interface MyBagStoreClick{
        public void startAni(int pos, String url);
        public void openCar(int pos, int id, boolean isOpen);
        public void buySuccess();
    }

    private MyBagStoreClick myBagStoreClick;

    public void setMyBagStoreClick(MyBagStoreClick myBagStoreClick) {
        this.myBagStoreClick = myBagStoreClick;
    }
}
