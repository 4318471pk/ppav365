package com.live.fox.dialog.bottomDialog;

import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.live.fox.R;
import com.live.fox.base.BaseBindingDialogFragment;
import com.live.fox.base.DialogFramentManager;
import com.live.fox.common.JsonCallback;
import com.live.fox.databinding.DialogAudienceAdminlistBinding;
import com.live.fox.entity.LivingRoomAdminListBean;
import com.live.fox.server.Api_Live;
import com.live.fox.utils.ChatSpanUtils;
import com.live.fox.utils.GlideUtils;
import com.live.fox.utils.ScreenUtils;
import com.live.fox.utils.SpanUtils;
import com.live.fox.utils.ToastUtils;
import com.live.fox.view.RankProfileView;

import java.util.ArrayList;
import java.util.List;

public class AudienceAdminListDialog extends BaseBindingDialogFragment {

    DialogAudienceAdminlistBinding mBind;
    int viewHeight;
    String liveId,anchorId;
    List<LivingRoomAdminListBean> livingRoomAdminListBeans=new ArrayList<>();

    public static AudienceAdminListDialog getInstance(String liveId,String anchorId) {
        AudienceAdminListDialog audienceAdminListDialog=new AudienceAdminListDialog();
        audienceAdminListDialog.liveId=liveId;
        audienceAdminListDialog.anchorId=anchorId;
        return audienceAdminListDialog;
    }

    @Override
    public void onClickView(View view) {
        switch (view.getId()) {
            case R.id.ivClose:
                if (mBind.ivIntroduction.getVisibility() == View.GONE) {
                    mBind.tvTips.setVisibility(View.GONE);
                    mBind.tvTitle.setText(getStringWithoutContext(R.string.setAudienceAdmin));
                    mBind.ivIntroduction.setVisibility(View.VISIBLE);
                    mBind.rlListContent.setVisibility(View.VISIBLE);

                    if(mBind.llList.getChildCount()>0)
                    {
                        mBind.llList.setVisibility(View.VISIBLE);
                        mBind.tvAmountOfAdmin.setVisibility(View.VISIBLE);
                        mBind.rlEmptyDataView.setVisibility(View.GONE);
                    }
                    else
                    {
                        mBind.llList.setVisibility(View.GONE);
                        mBind.tvAmountOfAdmin.setVisibility(View.GONE);
                        mBind.rlEmptyDataView.setVisibility(View.VISIBLE);
                    }
                } else {
                    showAnotherDialog(R.id.ivClose);
                }
                break;
            case R.id.ivIntroduction:
                mBind.tvTitle.setText(getStringWithoutContext(R.string.dialogTitleAudienceList));
                mBind.ivIntroduction.setVisibility(View.GONE);
                mBind.rlListContent.setVisibility(View.GONE);
                mBind.tvTips.setVisibility(View.VISIBLE);
                break;
            case R.id.rlMain:
                mBind.rlMain.setEnabled(false);
                startAnimate(mBind.llContent, false);
                break;
        }
    }

    @Override
    public boolean onBackPress() {
        startAnimate(mBind.llContent,false);
        return true;
    }

    private void showAnotherDialog(int id) {
        startAnimate(mBind.llContent, false, new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                switch (id) {
                    case R.id.ivClose:
                        AudienceManagerDialog audienceManagerDialog = AudienceManagerDialog.getInstance(liveId,anchorId);
                        DialogFramentManager.getInstance().showDialogAllowingStateLoss(getFragmentManager(), audienceManagerDialog);
                        break;
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    @Override
    public int onCreateLayoutId() {
        return R.layout.dialog_audience_adminlist;
    }

    @Override
    public void initView(View view) {
        mBind = getViewDataBinding();
        mBind.setClick(this);

        view.setVisibility(View.GONE);
        int screenHeight = ScreenUtils.getScreenHeight(getActivity());
        viewHeight = (int) (screenHeight * 0.56f);
        mBind.llContent.getLayoutParams().height = viewHeight;

        view.setVisibility(View.VISIBLE);
        getAdminList();
        startAnimate(mBind.llContent, true);
    }

    private void addItem(List<LivingRoomAdminListBean> list) {
        String temple=getStringWithoutContext(R.string.hadSetSomeAdmin);
        if (list == null || list.size() == 0) {
            mBind.llList.setVisibility(View.GONE);
            mBind.rlEmptyDataView.setVisibility(View.VISIBLE);
            mBind.tvAmountOfAdmin.setText(String.format(temple,"0"));
            return;
        } else {
            mBind.llList.removeAllViews();
            mBind.llList.setVisibility(View.VISIBLE);
            mBind.rlEmptyDataView.setVisibility(View.GONE);
            mBind.tvAmountOfAdmin.setText(String.format(temple,list.size()+""));
        }

        int itemHeight=(int)(viewHeight*0.64f/4);
        for (int i = 0; i < list.size(); i++) {
            View view = View.inflate(getActivity(), R.layout.item_audience_admin_list, null);
            mBind.llList.addView(view,ViewGroup.LayoutParams.MATCH_PARENT,itemHeight);

            LivingRoomAdminListBean bean=list.get(i);
            SpanUtils spanUtils=new SpanUtils();
            if(ChatSpanUtils.appendSexIcon(spanUtils,bean.getUserLevel(), getActivity(), SpanUtils.ALIGN_CENTER))
            {
                spanUtils.append(" ");
            }

            if(ChatSpanUtils.appendLevelIcon(spanUtils,bean.getUserLevel(), getActivity()))
            {
                spanUtils.append(" ");
            }

            if(ChatSpanUtils.appendVipLevelRectangleIcon(spanUtils,bean.getVipLevel(), getActivity()))
            {
                spanUtils.append(" ");
            }

            if(ChatSpanUtils.appendRoomManageIcon(spanUtils,bean.isRoomManage(), getActivity()))
            {
                spanUtils.append(" ");
            }

            if(ChatSpanUtils.appendGuardIcon(spanUtils,bean.getGuardLevel(), getActivity()))
            {
                spanUtils.append(" ");
            }

            TextView tvRemove = view.findViewById(R.id.tvRemove);
            RankProfileView rpv = view.findViewById(R.id.rpv);
            TextView tvNickName = view.findViewById(R.id.tvNickName);
            TextView tvIcons = view.findViewById(R.id.tvIcons);

            tvNickName.setText(list.get(i).getNickname());
            tvIcons.setText(spanUtils.create());
            rpv.setIndex(RankProfileView.NONE, bean.getVipLevel(),false);
            GlideUtils.loadCircleImage(getActivity(),bean.getAvatar(),R.mipmap.user_head_error,R.mipmap.user_head_error,rpv.getProfileImage());
            tvRemove.setTag(list.get(i));
            tvRemove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LivingRoomAdminListBean bean=(LivingRoomAdminListBean)v.getTag();
                    roomManagerOperate(bean);
                }
            });

        }

        if (list.size() < 5) {
            TextView textView = new TextView(getActivity());
            LinearLayout.LayoutParams ll = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            ll.topMargin = ScreenUtils.dp2px(getActivity(), 10);
            textView.setLayoutParams(ll);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 11);
            textView.setTextColor(0xffA3A3A3);
            textView.setGravity(Gravity.CENTER);
            textView.setText(getStringWithoutContext(R.string.noMoreData));

            mBind.llList.addView(textView);
        }

    }

    private void getAdminList()
    {
        Api_Live.ins().getLivingRoomManagerList(liveId, new JsonCallback<List<LivingRoomAdminListBean>>() {
            @Override
            public void onSuccess(int code, String msg, List<LivingRoomAdminListBean> data) {
                if(!isConditionOk())
                {
                    return;
                }
                if(code==0)
                {
                    if(data!=null && data.size()>0)
                    {
                        livingRoomAdminListBeans.clear();
                        livingRoomAdminListBeans.addAll(data);
                        addItem(livingRoomAdminListBeans);
                    }
                }
                else
                {
                    ToastUtils.showShort(msg);
                }
            }
        });
    }

    private void roomManagerOperate(LivingRoomAdminListBean bean)
    {
        if(bean==null || TextUtils.isEmpty(bean.getUid()))
        {
            Log.e("roomManagerOperate","uid can not be null");
            return;
        }
        Api_Live.ins().roomManagerOperate(bean.getUid(),anchorId,false,new JsonCallback<String>(){
            @Override
            public void onSuccess(int code, String msg, String data) {
                    if(!isConditionOk())
                    {
                        return;
                    }
                    if(code==0)
                    {
                        livingRoomAdminListBeans.remove(bean);
                        addItem(livingRoomAdminListBeans);
                    }
                    else
                    {
                        ToastUtils.showShort(msg);
                    }

            }
        });
    }
}
