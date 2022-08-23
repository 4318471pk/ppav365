package com.live.fox.ui.mine.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.google.gson.Gson;
import com.live.fox.Constant;
import com.live.fox.R;
import com.live.fox.base.BaseHeadActivity;
import com.live.fox.common.JsonCallback;
import com.live.fox.db.DataBase;
import com.live.fox.dialog.SvgaPreview;
import com.live.fox.entity.Car;
import com.live.fox.entity.CarList;
import com.live.fox.entity.Gift;
import com.live.fox.language.MultiLanguageUtils;
import com.live.fox.server.Api_User;
import com.live.fox.utils.BarUtils;
import com.live.fox.utils.GlideUtils;
import com.live.fox.utils.LogUtils;
import com.live.fox.utils.StatusBarUtil;
import com.live.fox.utils.TimeUtils;
import com.live.fox.utils.okgo.HttpConsts;
import com.live.fox.utils.okgo.OkGoHttpUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;


/**
 * 我的座驾
 */
public class MyPronActivity extends BaseHeadActivity {

    private RecyclerView rv;
    private SmartRefreshLayout refreshLayout;

    BaseQuickAdapter adapter;

    Car car;

    public static void startActivity(@NonNull Context context) {
        Constant.isAppInsideClick = true;
        Intent intent = new Intent(context, MyPronActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_recycle_view_with_refresh);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_SECURE);
        initView();
    }

    private void initView() {
        StatusBarUtil.setStatusBarFulAlpha(this);
        BarUtils.setStatusBarLightMode(this, false);
        setHead(getString(R.string.myProps), true, true);

        rv = findViewById(R.id.rv_);
        refreshLayout = findViewById(R.id.refreshLayout);

        setRecycleView();
        doGetUserCarApi();
    }

    public void setRecycleView() {
        refreshLayout.setEnablePureScrollMode(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        this.rv.setLayoutManager(layoutManager);
        this.rv.setAdapter(adapter = new BaseQuickAdapter(R.layout.item_shoping, new ArrayList<Gift>()) {
            @Override
            protected void convert(BaseViewHolder helper, Object item) {
                Gift gift = (Gift) item;
                GlideUtils.loadImage(context, gift.getCover(), helper.getView(R.id.car_buy_avatar));
                helper.setText(R.id.car_buy_name, gift.getGname());
                TextView carData = helper.getView(R.id.car_buy_details);
                String data = getString(R.string.maturityDate) + TimeUtils.millis2String(gift.getEndTIme(),
                        new SimpleDateFormat("dd-MM-yyyy", MultiLanguageUtils.appLocal));
                carData.setText(data);
                carData.setVisibility(View.VISIBLE);
                CheckBox checkBox = helper.getView(R.id.car_select);
                checkBox.setVisibility(View.VISIBLE);
                checkBox.setChecked(gift.getGid() == car.getShowGid());
                helper.addOnClickListener(R.id.car_buy_avatar);
            }
        });

        adapter.setOnItemClickListener((adapter, view, position) -> {
            Gift gift = (Gift) adapter.getItem(position);
            if (gift != null && gift.getGid() != car.getShowGid()) {
                setShowCarApi(gift.getGid());
            }
        });

        adapter.setOnItemChildClickListener((adapter, view, position) -> {
            Gift gift = (Gift) adapter.getItem(position);
            if (view.getId() == R.id.car_buy_avatar) {
                SvgaPreview svgaPreviewDialog = new SvgaPreview();
                Bundle args = new Bundle();
                args.putSerializable(SvgaPreview.SVGA_GIFT_KEY, gift);
                svgaPreviewDialog.setArguments(args);
                svgaPreviewDialog.show(getSupportFragmentManager(), "show svga preview dialog");
            }
        });
    }


    /**
     * 我的道具列表
     */
    public void doGetUserCarApi() {
        Api_User.ins().getUserCar(new JsonCallback<Car>() {
            @Override
            public void onSuccess(int code, String msg, Car data) {
                if (data != null) LogUtils.e(code + "," + msg + "," + new Gson().toJson(data));
                if (code == 0) {
                    car = data;
                    if (car.getCarList() != null) {
                        DataBase db = DataBase.getDbInstance();
                        List<Gift> giftList = new ArrayList<>();
                        for (int i = 0; i < data.getCarList().size(); i++) {
                            CarList carList = data.getCarList().get(i);
                            Gift gift = db.getGiftByGid(carList.getGid());
                            LogUtils.e("礼物数据" + gift.getGname());
                            if (gift.getGid() != 0) {
                                gift.setEndTIme(carList.getEndTime());
                                gift.setNum(carList.getNum());
                                giftList.add(gift);
                            }
                        }
                        if (giftList.size() == 0) {
                            showEmptyView(getString(R.string.noProps));
                        }
                        adapter.setNewData(giftList);
                    }
                } else {
                    showEmptyView(msg);
                }
            }
        });
    }


    /**
     * 设置直播间座驾
     */
    public void setShowCarApi(int gid) {
        Api_User.ins().setShowCar(gid, new JsonCallback<String>() {
            @Override

            public void onSuccess(int code, String msg, String data) {
                if (data != null) LogUtils.e(code + "," + msg + "," + new Gson().toJson(data));
                if (code == 0) {
                    car.setShowGid(gid);
                    adapter.notifyDataSetChanged();
                    showToastTip(true, getString(R.string.liveRoomSet));
                } else {
                    showToastTip(false, msg);
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        OkGoHttpUtil.getInstance().cancel(HttpConsts.ADD_CASH_ACCOUNT);
        super.onDestroy();
    }

}
