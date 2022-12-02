package com.live.fox.ui.openLiving;

import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.live.fox.ConstantValue;
import com.live.fox.R;
import com.live.fox.base.BaseBindingFragment;
import com.live.fox.base.DialogFramentManager;
import com.live.fox.common.JsonCallback;
import com.live.fox.databinding.FragmentPreparingLivingBinding;
import com.live.fox.dialog.bottomDialog.ContactCardObtainDialog;
import com.live.fox.dialog.bottomDialog.EditLivingGameTypeDialog;
import com.live.fox.dialog.bottomDialog.EditProfileImageDialog;
import com.live.fox.dialog.bottomDialog.SetLocationDialog;
import com.live.fox.dialog.bottomDialog.SetRoomTypeDialog;
import com.live.fox.entity.LotteryCategoryOfBeforeLiving;
import com.live.fox.entity.User;
import com.live.fox.manager.DataCenter;
import com.live.fox.server.Api_Config;
import com.live.fox.server.Api_Live;
import com.live.fox.server.Api_Living_Lottery;
import com.live.fox.ui.mine.CenterOfAnchorActivity;
import com.live.fox.utils.GlideUtils;
import com.live.fox.utils.SPUtils;
import com.live.fox.utils.ToastUtils;
import com.live.fox.utils.device.ScreenUtils;

import org.json.JSONException;
import org.json.JSONObject;

public class PreparingLivingFragment extends BaseBindingFragment {

    FragmentPreparingLivingBinding mBind;
    int iconArray[] = {R.mipmap.icon_cameral_change, R.mipmap.icon_beaty_effect,
            R.mipmap.icon_gametype, R.mipmap.icon_advantage, R.mipmap.icon_roomtype};
    String liveId;

    @Override
    public void onClickView(View view) {
        switch (view.getId()) {
            case R.id.rlMain:
                hideKeyBoard(getView());
                break;
            case R.id.ivClose:
                getActivity().finish();
                break;
            case R.id.gtvStartLiving:
                if (!TextUtils.isEmpty(mBind.tvName.getText().toString())) {
                    getMainActivity().roomTitle = mBind.tvName.getText().toString();
                }
                getMainActivity().showStartLiving();
                break;
            case R.id.tvLocation:
                //使用自己的位置不让点击了
//                SetLocationDialog setLocationDialog= SetLocationDialog.getInstance();
//                DialogFramentManager.getInstance().showDialogAllowingStateLoss(getChildFragmentManager(),setLocationDialog);
                break;
            case R.id.ivRoomPic:
                EditProfileImageDialog dialog = EditProfileImageDialog.getInstance();
                DialogFramentManager.getInstance().showDialogAllowingStateLoss(getChildFragmentManager(), dialog);
                break;
        }
    }

    @Override
    public int onCreateLayoutId() {
        return R.layout.fragment_preparing_living;
    }

    public OpenLivingActivity getMainActivity() {
        if (isActivityOK()) {
            return (OpenLivingActivity) getActivity();
        }
        return null;
    }

    public void setImage(String imageUrl) {
        GlideUtils.loadDefaultImage(getActivity(), imageUrl, R.mipmap.user_head_error,
                R.mipmap.user_head_error, mBind.ivRoomPic);
    }

    @Override
    public void initView(View view) {
        mBind = getViewDataBinding();
        mBind.setClick(this);

        int screenWidth = ScreenUtils.getScreenWidth(getContext());
        int screenHeight = ScreenUtils.getScreenHeight(getContext());
        liveId = getMainActivity().liveId;
        String roomBG = getMainActivity().imageURL;
        if (!TextUtils.isEmpty(roomBG)) {
            GlideUtils.loadDefaultImage(getActivity(), roomBG, R.mipmap.user_head_error,
                    R.mipmap.user_head_error, mBind.ivRoomPic);
        }
        mBind.tvName.setText(getMainActivity().roomTitle);

        view.setVisibility(View.GONE);
        RelativeLayout.LayoutParams rl = (RelativeLayout.LayoutParams) mBind.rlContent.getLayoutParams();
        rl.width = (int) (screenWidth * 0.8f);
        rl.leftMargin = (int) (screenWidth * 0.1f);
        rl.rightMargin = (int) (screenWidth * 0.1f);
        mBind.rlContent.setLayoutParams(rl);

        RelativeLayout.LayoutParams rlBot = (RelativeLayout.LayoutParams) mBind.llBotView.getLayoutParams();
        rlBot.width = (int) (screenWidth * 0.8f);
        rlBot.leftMargin = (int) (screenWidth * 0.1f);
        rlBot.rightMargin = (int) (screenWidth * 0.1f);
        mBind.llBotView.setLayoutParams(rlBot);

        RelativeLayout.LayoutParams rlButtons = (RelativeLayout.LayoutParams) mBind.llButtons.getLayoutParams();
        int botMargin = (int) (screenHeight * 0.141f);
        rlButtons.bottomMargin = botMargin + ScreenUtils.getDip2px(getContext(), 70);
        mBind.llButtons.setLayoutParams(rlButtons);

        User user = DataCenter.getInstance().getUserInfo().getUser();
        if (!TextUtils.isEmpty(getMainActivity().fixRoomType)) {
            mBind.gtvRoomType.setText(getMainActivity().fixRoomType);
        }

        if (TextUtils.isEmpty(user.getProvince()) && TextUtils.isEmpty(user.getCity())) {
            mBind.tvLocation.setText(getStringWithoutContext(R.string.mars));
        } else {
            StringBuilder sb = new StringBuilder();
            if (!TextUtils.isEmpty(user.getProvince())) {
                sb.append(user.getProvince());
            }
            if (!TextUtils.isEmpty(user.getCity())) {
                sb.append("-").append(user.getCity());
            }
            mBind.tvLocation.setText(sb.toString());
        }

        String buttonTitles[] = getResources().getStringArray(R.array.startLivingTitles);

        int padding = ScreenUtils.getDip2px(getContext(), 5);
        for (int i = 0; i < buttonTitles.length; i++) {
            TextView textView = new TextView(getActivity());
            LinearLayout.LayoutParams ll = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            ll.weight = 1;
            textView.setLayoutParams(ll);
            textView.setTextColor(0xffffffff);
            textView.setText(buttonTitles[i]);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 11);
            textView.setGravity(Gravity.CENTER_HORIZONTAL);
            textView.setCompoundDrawablePadding(padding * 2);
            Drawable drawable = getResources().getDrawable(iconArray[i]);
            textView.setCompoundDrawablesWithIntrinsicBounds(null, drawable, null, null);
            mBind.llButtons.addView(textView);
            textView.setTag(i);
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    OpenLivingActivity activity = (OpenLivingActivity) getActivity();
                    int index = (int) view.getTag();
                    switch (index) {
                        case 0:
                            activity.switchCamera();
                            break;
                        case 1:
                            activity.showBeautyLayout();
                            break;
                        case 2:
                            EditLivingGameTypeDialog editLivingGameTypeDialog = EditLivingGameTypeDialog.getInstance();
                            editLivingGameTypeDialog.setOnSelectGameListener(new EditLivingGameTypeDialog.OnSelectGameListener() {
                                @Override
                                public void onSelected(LotteryCategoryOfBeforeLiving bean) {
                                    getMainActivity().lotteryCategoryOfBeforeLiving = bean;
                                }
                            });
                            DialogFramentManager.getInstance().showDialogAllowingStateLoss(getChildFragmentManager(), editLivingGameTypeDialog);
                            break;
                        case 3:
                            ContactCardObtainDialog contactCardObtainDialog = ContactCardObtainDialog.getInstance();
                            contactCardObtainDialog.setOnContactCardListener(new ContactCardObtainDialog.OnContactCardListener() {
                                @Override
                                public void onContactCard(boolean isAvailable, String account, int diamondAmount, int type) {
                                    if (isAvailable) {
                                        getMainActivity().contactAccount = account;
                                        getMainActivity().contactCostDiamond = diamondAmount;
                                        getMainActivity().contactType = type;
                                    }
                                }
                            });
                            DialogFramentManager.getInstance().showDialogAllowingStateLoss(getChildFragmentManager(), contactCardObtainDialog);
                            break;
                        case 4:
                            String liveId = getMainActivity().liveId;
                            SetRoomTypeDialog setRoomTypeDialog = SetRoomTypeDialog.getInstance(false, liveId);
                            setRoomTypeDialog.setOnSelectRoomTypeListener(new SetRoomTypeDialog.OnSelectRoomTypeListener() {
                                @Override
                                public void onSelect(String liveId, int type, int price) {
                                    getMainActivity().roomPrice = price;
                                    getMainActivity().roomType = type;
                                }
                            });
                            DialogFramentManager.getInstance().showDialogAllowingStateLoss(getChildFragmentManager(), setRoomTypeDialog);
                            break;
                    }
                }
            });
        }

        view.setVisibility(View.VISIBLE);
        CacheOpenLivingGameList();
    }

    private void CacheOpenLivingGameList() {
        Api_Living_Lottery.ins().getLiveBeforeGames(new JsonCallback<String>() {
            @Override
            public void onSuccess(int code, String msg, String data) {
                if (code == 0) {
                    SPUtils.getInstance().put(ConstantValue.liveBeforeGames, data);
                }
            }
        });
    }

}
