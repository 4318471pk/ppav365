package com.live.fox.ui.mine.noble;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.flyco.roundview.RoundTextView;
import com.live.fox.Constant;
import com.live.fox.R;
import com.live.fox.adapter.OneMinuteAdapter;
import com.live.fox.base.MvpBaseActivity;
import com.live.fox.base.MyNoblePrestener;
import com.live.fox.contract.MyNobleContract;
import com.live.fox.dialog.DialogFactory;
import com.live.fox.entity.FunctionItem;
import com.live.fox.entity.Noble;
import com.live.fox.utils.ClipboardUtils;
import com.live.fox.utils.device.DeviceUtils;

import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * 我的座驾
 */
public class MyNobleActivity extends MvpBaseActivity<MyNobleContract.Presenter>
        implements MyNobleContract.View, View.OnClickListener {
    private ImageView ivRobble;
    private TextView tvDate;
    private TextView tvQQ;
    private TextView tvQQdes;
    private TextView tvLiang;
    private TextView tvLiangdes;
    private TextView tv_head_title;
    private ImageView iv_head_left;
    private ImageView ivRobble2;

    private RelativeLayout rlHead;
    private RoundTextView rtvJf;
    private RoundTextView rtvLt;
    private RoundTextView rtvBd;
    private RecyclerView rvRoble;
    private Noble mNoble;
    private int levelId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mynoble);

        initView();

        tv_head_title.setText(getString(R.string.myNovel));
        iv_head_left.setVisibility(View.VISIBLE);

        getPresenter().getVipInnfo();
    }

    private void initView() {

        ivRobble = findViewById(R.id.ivRobble);
        tvDate = findViewById(R.id.tvDate);
        tvQQ = findViewById(R.id.tvQQ);
        tvQQdes = findViewById(R.id.tvQQdes);
        tvLiang = findViewById(R.id.tvLiang);
        tvLiangdes = findViewById(R.id.tvLiangdes);
        tv_head_title = findViewById(R.id.tv_head_title);
        iv_head_left = findViewById(R.id.iv_head_left);
        ivRobble2 = findViewById(R.id.ivRobble2);
        rlHead = findViewById(R.id.rlHead);
        rtvJf = findViewById(R.id.rtvJf);
        rtvLt = findViewById(R.id.rtvLt);
        rtvBd = findViewById(R.id.rtvBd);
        rvRoble = findViewById(R.id.rvRoble);

        findViewById(R.id.rlShit).setOnClickListener(this);
        findViewById(R.id.tvAdd).setOnClickListener(this);
        findViewById(R.id.tvFee).setOnClickListener(this);
        findViewById(R.id.tvImprove).setOnClickListener(this);
        findViewById(R.id.iv_head_left).setOnClickListener(this);
        findViewById(R.id.rtvBd).setOnClickListener(this);
        findViewById(R.id.rtvJf).setOnClickListener(this);
        findViewById(R.id.rtvLt).setOnClickListener(this);
    }

    public static void startActivity(Activity activity) {
        Constant.isAppInsideClick = true;
        Intent intent = new Intent(activity, MyNobleActivity.class);
        activity.startActivity(intent);
    }


    @Override
    public void onVipInfo(Noble noble) {
        this.mNoble = noble;
        if (mNoble == null) return;
        levelId = noble.getLevelId();
        FunctionItem mNobleRes = NobleFragment.getNobleRes(this, levelId);
        ivRobble.setBackgroundResource(mNobleRes.getResSmall());
        ivRobble2.setBackgroundResource(0);

        ivRobble2.setScaleType(ImageView.ScaleType.CENTER_CROP);
        ivRobble2.setImageResource(mNobleRes.getResId());

        rvRoble.setLayoutManager(new GridLayoutManager(this, 3));
        rvRoble.setAdapter(new BaseQuickAdapter(R.layout.item_robble_adapter, NobleFragment.createFunctionItem(this, mNoble.getLevelId(), 0)) {
            @Override
            protected void convert(BaseViewHolder helper, Object item) {
                FunctionItem obj = (FunctionItem) item;
                TextView tvFunDes = helper.getView(R.id.tvFunDes);
                tvFunDes.setVisibility(View.GONE);
                helper.setText(R.id.tvFunTitle, obj.getTitle());
                helper.setImageResource(R.id.ivFunction, obj.getResId());

            }
        });

        OneMinuteAdapter.RecyclerSpace recyclerSpace = new OneMinuteAdapter.RecyclerSpace(DeviceUtils.dp2px(this, 6));
        rvRoble.addItemDecoration(recyclerSpace);
        yinshenStatus();

        tvLiang.setText(getString(R.string.goodName) + (mNoble.getVipUid() == 0 ? getString(R.string.noString) : mNoble.getVipUid()));

        SimpleDateFormat dtf = new SimpleDateFormat("dd-MM-yyyy");
        String mFormat = dtf.format(new Date(mNoble.getEndTime()));

        tvDate.setText(getString(R.string.endTime) + mFormat);
        tvQQ.setText(getString(R.string.groupNumber) + (TextUtils.isEmpty(mNoble.getGroup()) ? getString(R.string.noString) : mNoble.getGroup()));
        String res = null;
        if (levelId == Constant.LEVEL5) {
            res = getString(R.string.threeCool);
        } else if (levelId == Constant.LEVEL4) {
            res = getString(R.string.fourCool);

        } else if (levelId == Constant.LEVEL3) {
            res = getString(R.string.sixCool);

        } else if (levelId == Constant.LEVEL2) {
            res = getString(R.string.sevenCool);

        } else if (levelId == Constant.LEVEL1) {
            res = getString(R.string.noString);

        }
        tvLiangdes.setText(res);

    }

    private void yinshenStatus() {
        if (mNoble.getRankHide() == 1) {
            rtvBd.setTextColor(Color.parseColor("#eb5c34"));
            rtvBd.getDelegate().setStrokeColor(Color.parseColor("#eb5c34"));

            rtvBd.getDelegate().setStrokeWidth(1);
            rtvBd.setTag(mNoble.getRankHide());
        } else {
            rtvBd.getDelegate().setStrokeWidth(0);
            rtvBd.getDelegate().setStrokeColor(Color.WHITE);
            rtvBd.setTextColor(Color.WHITE);
        }

        if (mNoble.getChatHide() == 1) {
            rtvLt.setTextColor(Color.parseColor("#eb5c34"));
            rtvLt.getDelegate().setStrokeWidth(1);
            rtvLt.getDelegate().setStrokeColor(Color.parseColor("#eb5c34"));

            rtvLt.setTag(mNoble.getChatHide());
        } else {
            rtvLt.getDelegate().setStrokeWidth(0);
            rtvLt.setTextColor(Color.WHITE);
            rtvLt.getDelegate().setStrokeColor(Color.WHITE);

        }
        if (mNoble.getRoomHide() == 1) {
            rtvJf.setTextColor(Color.parseColor("#eb5c34"));
            rtvJf.getDelegate().setStrokeWidth(1);
            rtvJf.getDelegate().setStrokeColor(Color.parseColor("#eb5c34"));

            rtvJf.setTag(mNoble.getRoomHide());
        } else {
            rtvJf.getDelegate().setStrokeWidth(0);
            rtvJf.setTextColor(Color.WHITE);
            rtvJf.getDelegate().setStrokeColor(Color.WHITE);

        }
    }

    @Override
    public void onVipHide(String data) {
        yinshenStatus();
    }

    @Override
    protected MyNobleContract.Presenter bindPresenter() {
        return new MyNoblePrestener(this);
    }

    private void setNull(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {//16
            view.setBackground(null);
        } else {
            view.setBackgroundDrawable(null);
        }
    }

    @Override
    public void onVipUp(String mNoble) {
        getPresenter().getVipInnfo();
    }

    @Override
    public void onVipPay(String mNoble) {
        getPresenter().getVipInnfo();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvAdd:
                if (mNoble == null) return;
                ClipboardUtils.copyText(String.valueOf(mNoble.getGroup()));
                showToastTip(true, getString(R.string.copiedClipboard));
                break;
            case R.id.tvFee:
                DialogFactory.showTwoBtnDialog(this, getString(R.string.getReward), (button, dialog) -> dialog.dismiss(), (button, dialog) -> {
                    dialog.dismiss();
                    getPresenter().doVipPay(levelId);
                });

                break;
            case R.id.tvImprove:
                DialogFactory.showTwoBtnDialog(this, getString(R.string.upNovel), (button, dialog) -> dialog.dismiss(), (button, dialog) -> {
                    dialog.dismiss();
                    levelId++;
                    getPresenter().doVipUp(levelId);

                });

                break;
            case R.id.iv_head_left:
                Constant.isAppInsideClick = true;
                finish();
                break;
            case R.id.rtvBd:

                if (levelId == 5) {
                    int mRankHide = mNoble.getRankHide();
                    mRankHide = (mRankHide == 0) ? 1 : 0;
                    rtvBd.getDelegate().setCornerRadius(10);
                    setNull(rtvBd);

                    mNoble.setRankHide(mRankHide);
                    getPresenter().doVipHide(mNoble);
                } else {
                    showToastTip(false, getString(R.string.noNobelPer));
                }


                break;
            case R.id.rtvJf:
                if (levelId == 4 || levelId == 5) {
                    int roomHide = mNoble.getRoomHide();
                    roomHide = (roomHide == 0) ? 1 : 0;
                    mNoble.setRoomHide(roomHide);
                    rtvJf.getDelegate().setCornerRadius(10);
                    setNull(rtvJf);

                    getPresenter().doVipHide(mNoble);
                } else {
                    showToastTip(false, getString(R.string.noNobelPer));
                }

                break;
            case R.id.rtvLt:
                if (levelId == 5) {
                    int chatHide = mNoble.getChatHide();
                    chatHide = (chatHide == 0) ? 1 : 0;
                    rtvLt.getDelegate().setCornerRadius(10);
                    mNoble.setChatHide(chatHide);
                    setNull(rtvLt);
                    getPresenter().doVipHide(mNoble);
                } else {
                    showToastTip(false, getString(R.string.noNobelPer));
                }
                break;
        }
    }
}
