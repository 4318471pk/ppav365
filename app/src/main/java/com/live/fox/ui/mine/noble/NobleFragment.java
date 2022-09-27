package com.live.fox.ui.mine.noble;

import android.content.Context;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.live.fox.Constant;
import com.live.fox.R;
import com.live.fox.adapter.OneMinuteAdapter;
import com.live.fox.base.NoblePresenter;
import com.live.fox.contract.NobleContract;
import com.live.fox.dialog.DialogFactory;
import com.live.fox.entity.FunctionItem;
import com.live.fox.entity.VipInfo;
import com.live.fox.mvp.MvpBaseFragment;
import com.live.fox.mvp.PresenterInject;
import com.live.fox.utils.RegexUtils;
import com.live.fox.utils.device.DeviceUtils;

import java.util.ArrayList;
import java.util.List;

@PresenterInject(NoblePresenter.class)
public class NobleFragment extends MvpBaseFragment<NoblePresenter> implements NobleContract.View {

    private TextView livePrice;
    private TextView livePriceRenew;
    private TextView livePriceTitle;
    private TextView livePriceSel;
    private String tips;
    private float pk;
    private RecyclerView levePrivilege;

    private NobleActivity mActivity;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_zijue, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getArguments() != null) {
            int level = getArguments().getInt("level");
            levePrivilege = view.findViewById(R.id.noble_level_features);
            ImageView levelBg = view.findViewById(R.id.noble_flag_bg);
            ImageView levelHead = view.findViewById(R.id.noble_flag_head);
            livePrice = view.findViewById(R.id.noble_level_price);

            livePriceTitle = view.findViewById(R.id.noble_leve_title);
            livePriceSel = view.findViewById(R.id.noble_leve_hand_sel);
            livePriceRenew = view.findViewById(R.id.noble_level_features_renew);

            view.findViewById(R.id.noble_leve_buy).setOnClickListener(v ->
                    DialogFactory.showTwoBtnDialog(getActivity(),
                            tips, (button, dialog) -> dialog.dismiss(), (button, dialog) -> {
                                dialog.dismiss();
                                presenter.doBuyVip(level);
                            }));

            levePrivilege.setLayoutManager(new GridLayoutManager(getActivity(), 3));

            OneMinuteAdapter.RecyclerSpace recyclerSpace =
                    new OneMinuteAdapter.RecyclerSpace(DeviceUtils.dp2px(requireContext(), 8));
            levePrivilege.addItemDecoration(recyclerSpace);

            mActivity = (NobleActivity) requireActivity();

            levelBg.setImageLevel(level);  //设置背景
            levelHead.setImageLevel(level);  //设置头像

            setLiveStyle(level);

            levePrivilege.setAdapter(new BaseQuickAdapter(R.layout.item_robble_adapter,
                    createFunctionItem(requireContext(), level, pk)) {
                @Override
                protected void convert(BaseViewHolder helper, Object item) {
                    FunctionItem obj = (FunctionItem) item;
                    TextView tvFunDes = helper.getView(R.id.tvFunDes);
                    tvFunDes.setText(obj.getDes());
                    helper.setText(R.id.tvFunTitle, obj.getTitle());
                    helper.setImageResource(R.id.ivFunction, obj.getResId());
                }
            });
        }
    }

    private void setLiveStyle(int level) {
        switch (level) {
            case 1:
                if (mActivity.vipInfoList != null && mActivity.vipInfoList.size() > 0) {
                    VipInfo mVipInfo = mActivity.vipInfoList.get(0);
                    pk = mVipInfo.getPkAddition();
                    levePrivilege.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.colorBrownGold));
                    String price = RegexUtils.formatNumber(mVipInfo.getPrice());
                    setNobleTitle(getString(R.string.grade_gold), price);

                    setBuyTitle(livePriceTitle, getString(R.string.grade_gold), price);
                    price = RegexUtils.formatNumber(mVipInfo.getRewardPrice()) + getString(R.string.gold);
                    setBuyGive(livePriceSel, getString(R.string.give), price);
                    setRenewal(mVipInfo);

                    tips = getString(R.string.jijiangPay) +
                            RegexUtils.formatNumber(mVipInfo.getPrice()) +
                            String.format(getString(R.string.purchase), getString(R.string.grade_gold));
                }

                break;
            case 2:
                if (mActivity.vipInfoList != null && mActivity.vipInfoList.size() > 1) {
                    VipInfo mVipInfo = mActivity.vipInfoList.get(1);
                    pk = mVipInfo.getPkAddition();
                    levePrivilege.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.colorBrownPlatinum));
                    setNobleTitle(getString(R.string.grade_platinum), String.valueOf(mVipInfo.getPrice()));

                    String price = RegexUtils.formatNumber(mVipInfo.getPrice());
                    setBuyTitle(livePriceTitle, getString(R.string.grade_platinum), price);

                    price = RegexUtils.formatNumber(mVipInfo.getRewardPrice()) + getString(R.string.gold);
                    setBuyGive(livePriceSel, getString(R.string.give), price);

                    setRenewal(mVipInfo);

                    tips = getString(R.string.jijiangPay) +
                            RegexUtils.formatNumber(mVipInfo.getPrice()) +
                            String.format(getString(R.string.purchase), getString(R.string.grade_platinum));

                }
                break;
            case 3:
                if (mActivity.vipInfoList != null && mActivity.vipInfoList.size() > 2) {
                    VipInfo mVipInfo = mActivity.vipInfoList.get(2);
                    pk = mVipInfo.getPkAddition();
                    levePrivilege.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.colorBrownDiamond));
                    setNobleTitle(getString(R.string.grade_diamond), String.valueOf(mVipInfo.getPrice()));

                    String price = RegexUtils.formatNumber(mVipInfo.getPrice());
                    setBuyTitle(livePriceTitle, getString(R.string.grade_diamond), price);

                    price = RegexUtils.formatNumber(mVipInfo.getRewardPrice()) + getString(R.string.gold);
                    setBuyGive(livePriceSel, getString(R.string.give), price);

                    setRenewal(mVipInfo);

                    tips = getString(R.string.jijiangPay) +
                            RegexUtils.formatNumber(mVipInfo.getPrice()) +
                            String.format(getString(R.string.purchase), getString(R.string.grade_diamond));
                }

                break;
            case 4:

                if (mActivity.vipInfoList != null && mActivity.vipInfoList.size() > 3) {
                    VipInfo mVipInfo = mActivity.vipInfoList.get(3);
                    pk = mVipInfo.getPkAddition();
                    setNobleTitle(getString(R.string.grade_master), String.valueOf(mVipInfo.getPrice()));
                    setRenewal(mVipInfo);
                    levePrivilege.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.colorBrownMaster));
                    String price = RegexUtils.formatNumber(mVipInfo.getPrice());
                    setBuyTitle(livePriceTitle, getString(R.string.grade_master), price);

                    price = RegexUtils.formatNumber(mVipInfo.getRewardPrice()) + getString(R.string.gold);
                    setBuyGive(livePriceSel, getString(R.string.give), price);

                    tips = getString(R.string.jijiangPay) +
                            RegexUtils.formatNumber(mVipInfo.getPrice()) +
                            String.format(getString(R.string.purchase), getString(R.string.grade_master));
                }
                break;
            case 5:
                if (mActivity.vipInfoList != null && mActivity.vipInfoList.size() > 4) {
                    VipInfo mVipInfo = mActivity.vipInfoList.get(4);
                    pk = mVipInfo.getPkAddition();
                    setNobleTitle(getString(R.string.grade_king), String.valueOf(mVipInfo.getPrice()));
                    setRenewal(mVipInfo);
                    levePrivilege.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.colorBrownKing));
                    String price = RegexUtils.formatNumber(mVipInfo.getPrice());
                    setBuyTitle(livePriceTitle, getString(R.string.grade_king), price);

                    price = RegexUtils.formatNumber(mVipInfo.getRewardPrice()) + getString(R.string.gold);
                    setBuyGive(livePriceSel, getString(R.string.give), price);

                    tips = getString(R.string.jijiangPay) +
                            RegexUtils.formatNumber(mVipInfo.getPrice()) +
                            String.format(getString(R.string.purchase), getString(R.string.grade_king));
                }
                break;
        }
    }


    /**
     * 设置续费
     *
     * @param vip VIP
     */
    private void setRenewal(VipInfo vip) {
        String renewalReturn = String.format(getString(R.string.renewal_return),
                RegexUtils.formatNumber(vip.getRenewPrice()),
                RegexUtils.formatNumber(vip.getReturnPrice()));
        livePriceRenew.setText(renewalReturn);
    }

    /**
     * 设置 标题
     *
     * @param title title
     * @param price 价格
     */
    private void setNobleTitle(String title, String price) {
        String format = String.format(getString(R.string.noble_title), title, price);
        livePrice.setText(format);
    }

    /**
     * 设置购买的标题颜色
     *
     * @param title title
     * @param price price
     */
    private void setBuyTitle(TextView textView, String title, String price) {
        String format = String.format(getString(R.string.noble_title), title, price);
        SpannableStringBuilder builder = new SpannableStringBuilder();
        builder.append(format);
        int start = 0;
        int end = title.length();
        builder.setSpan(new ForegroundColorSpan(ContextCompat.getColor(requireContext(),
                R.color.white)), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView.setText(builder);
    }

    /**
     * 设置购买的标题颜色
     *
     * @param title title
     * @param price price
     */
    private void setBuyGive(TextView textView, String title, String price) {
        SpannableStringBuilder builder = new SpannableStringBuilder();
        builder.append(title).append(price);
        int start = 0;
        int end = title.length();
        builder.setSpan(new ForegroundColorSpan(ContextCompat.getColor(requireContext(),
                R.color.white)), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView.setText(builder);
    }

    public static NobleFragment newInstance(int level) {
        NobleFragment fragment = new NobleFragment();
        Bundle args = new Bundle();
        args.putInt("level", level);
        fragment.setArguments(args);
        return fragment;
    }

    public static List<FunctionItem> createFunctionItem(Context context, int level, float pk) {
        List<FunctionItem> mList = new ArrayList<>();
        FunctionItem it1;
        switch (level) {
            case Constant.LEVEL1:
                String str = String.format(context.getString(R.string.grade_introduction), context.getString(R.string.grade_gold));
                it1 = new FunctionItem(R.drawable.ic_zhumu, str, context.getString(R.string.allEyesFixedOn));
                mList.add(it1);
                it1 = new FunctionItem(R.drawable.ic_pk_item, pk +
                        context.getString(R.string.doublePKAddition), context.getString(R.string.PKaddition));
                mList.add(it1);
                it1 = new FunctionItem(R.drawable.ic_zhuanshu, context.getString(R.string.giftFromViscount),
                        context.getString(R.string.exclusiveGift));
                mList.add(it1);
                it1 = new FunctionItem(R.drawable.ic_fuwu, context.getString(R.string.seniorVIPgroup), context.getString(R.string.exclusiveServices));
                mList.add(it1);

                break;
            case Constant.LEVEL2:
                it1 = new FunctionItem(R.drawable.ic_zhumu,
                        String.format(context.getString(R.string.grade_introduction), context.getString(R.string.grade_platinum)),
                        context.getString(R.string.allEyesFixedOn));
                mList.add(it1);
                it1 = new FunctionItem(R.drawable.ic_pk_item, pk + context.getString(R.string.doublePKAddition), context.getString(R.string.PKaddition));
                mList.add(it1);
                it1 = new FunctionItem(R.drawable.ic_zhuanshu, context.getString(R.string.giftsEarlsBelow), context.getString(R.string.exclusiveGift));
                mList.add(it1);
                it1 = new FunctionItem(R.drawable.ic_fuwu, context.getString(R.string.seniorVIPgroup), context.getString(R.string.exclusiveServices));
                mList.add(it1);
                it1 = new FunctionItem(R.drawable.ic_lianghao, context.getString(R.string.sevenCharacter), context.getString(R.string.nobleName));
                mList.add(it1);
                break;
            case Constant.LEVEL3:
                it1 = new FunctionItem(R.drawable.ic_zhumu,
                        String.format(context.getString(R.string.grade_introduction), context.getString(R.string.grade_diamond)),
                        context.getString(R.string.allEyesFixedOn));
                mList.add(it1);
                it1 = new FunctionItem(R.drawable.ic_pk_item, pk + context.getString(R.string.doublePKAddition), context.getString(R.string.PKaddition));
                mList.add(it1);
                it1 = new FunctionItem(R.drawable.ic_zhuanshu, context.getString(R.string.giftsDukeBelow), context.getString(R.string.exclusiveGift));
                mList.add(it1);
                it1 = new FunctionItem(R.drawable.ic_fuwu, context.getString(R.string.seniorVIPgroup), context.getString(R.string.exclusiveServices));
                mList.add(it1);
                it1 = new FunctionItem(R.drawable.ic_lianghao, context.getString(R.string.sixCharacter), context.getString(R.string.nobleName));
                mList.add(it1);
                it1 = new FunctionItem(R.drawable.ic_changliao, context.getString(R.string.unlimitedPrivateChatAnchor), context.getString(R.string.privateChat));
                mList.add(it1);
                it1 = new FunctionItem(R.drawable.ic_fangti, context.getString(R.string.managementKickForbidden), context.getString(R.string.superKickDefense));
                mList.add(it1);
                break;
            case Constant.LEVEL4:
                it1 = new FunctionItem(R.drawable.ic_zhumu,
                        String.format(context.getString(R.string.grade_introduction), context.getString(R.string.grade_master)),
                        context.getString(R.string.allEyesFixedOn));
                mList.add(it1);
                it1 = new FunctionItem(R.drawable.ic_pk_item, pk + context.getString(R.string.doublePKAddition), context.getString(R.string.PKaddition));
                mList.add(it1);
                it1 = new FunctionItem(R.drawable.ic_zhuanshu, context.getString(R.string.giftsKingBelow), context.getString(R.string.exclusiveGift));
                mList.add(it1);
                it1 = new FunctionItem(R.drawable.ic_fuwu, context.getString(R.string.exclusiveCustomerVIP), context.getString(R.string.exclusiveServices));
                mList.add(it1);
                it1 = new FunctionItem(R.drawable.ic_lianghao, context.getString(R.string.fourCharacter), context.getString(R.string.nobleName));
                mList.add(it1);
                it1 = new FunctionItem(R.drawable.ic_changliao, context.getString(R.string.unlimitedPrivateChatAnchor), context.getString(R.string.privateChat));
                mList.add(it1);
                it1 = new FunctionItem(R.drawable.ic_fangti, context.getString(R.string.managementKickForbidden), context.getString(R.string.superKickDefense));
                mList.add(it1);
                it1 = new FunctionItem(R.drawable.ic_yinshen, context.getString(R.string.enterRoomHide), context.getString(R.string.superHide));
                mList.add(it1);
                break;
            case Constant.LEVEL5:
                it1 = new FunctionItem(R.drawable.ic_zhumu,
                        String.format(context.getString(R.string.grade_introduction), context.getString(R.string.grade_king)),
                        context.getString(R.string.allEyesFixedOn));
                mList.add(it1);
                it1 = new FunctionItem(R.drawable.ic_pk_item, pk + context.getString(R.string.doublePKAddition), context.getString(R.string.PKaddition));
                mList.add(it1);
                it1 = new FunctionItem(R.drawable.ic_zhuanshu, context.getString(R.string.giftsEmperorBelow), context.getString(R.string.exclusiveGift));
                mList.add(it1);
                it1 = new FunctionItem(R.drawable.ic_fuwu, context.getString(R.string.exclusiveCustomerVIP), context.getString(R.string.exclusiveServices));
                mList.add(it1);
                it1 = new FunctionItem(R.drawable.ic_lianghao, context.getString(R.string.threeCharacter), context.getString(R.string.nobleName));
                mList.add(it1);
                it1 = new FunctionItem(R.drawable.ic_changliao, context.getString(R.string.unlimitedPrivateChatAnchor), context.getString(R.string.privateChat));
                mList.add(it1);
                it1 = new FunctionItem(R.drawable.ic_fangti, context.getString(R.string.allNoTickSilence), context.getString(R.string.superKickDefense));
                mList.add(it1);
                it1 = new FunctionItem(R.drawable.ic_yinshen, context.getString(R.string.enterChatBangdang), context.getString(R.string.superHide));
                mList.add(it1);
                break;
        }
        return mList;
    }

    @Override
    public void onBuyVip(String data) {
        requireActivity().setResult(555);
        requireActivity().finish();
        showToastTip(true, getString(R.string.successfulOpening));
    }

    public static FunctionItem getNobleRes(Context context, int levelId) {
        FunctionItem it = null;
        switch (levelId) {
            case Constant.LEVEL1:
                it = new FunctionItem(R.drawable.ic_my_zj, R.drawable.img_noble_flag_gold, null, context.getString(R.string.grade_gold), "#f0c49f");
                it.circleBg = R.drawable.ic_zijue_c;

                break;
            case Constant.LEVEL2:
                it = new FunctionItem(R.drawable.ic_my_bj, R.drawable.img_noble_flag_platinum, null, context.getString(R.string.grade_platinum), "#c0bdff");

                it.circleBg = R.drawable.ic_bj_c;
                break;
            case Constant.LEVEL3:
                it = new FunctionItem(R.drawable.ic_my_gj, R.drawable.img_noble_flag_diamond, null, context.getString(R.string.grade_diamond), "#ae58e3");

                it.circleBg = R.drawable.ic_gj2_c;

                break;
            case Constant.LEVEL4:
                it = new FunctionItem(R.drawable.ic_my_gw, R.drawable.img_noble_flag_master, null, context.getString(R.string.grade_master), "#f35180");
                it.circleBg = R.drawable.ic_gw_c;

                break;
            case Constant.LEVEL5:
                it = new FunctionItem(R.drawable.ic_my_huangdi, R.drawable.img_noble_flag_king, null, context.getString(R.string.grade_king), "#f0c377");
                it.circleBg = R.drawable.ic_hd_c;

                break;
        }
        return it;
    }
}
