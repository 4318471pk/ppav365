package com.live.fox.ui.mine;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
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
import com.live.fox.dialog.BuyPronFragment;
import com.live.fox.dialog.SvgaPreview;
import com.live.fox.entity.Gift;
import com.live.fox.server.Api_Config;
import com.live.fox.server.Api_Order;
import com.live.fox.utils.BarUtils;
import com.live.fox.utils.GlideUtils;
import com.live.fox.utils.LogUtils;
import com.live.fox.utils.StatusBarUtil;
import com.live.fox.utils.ToastUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


/**
 * 购买座驾
 */
public class ShopActivity extends BaseHeadActivity {

    private RecyclerView rv;
    private SmartRefreshLayout refreshLayout;

    BaseQuickAdapter adapter;


    public static void startActivity(@NonNull Context context) {
        Constant.isAppInsideClick = true;
        Intent intent = new Intent(context, ShopActivity.class);
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
        setHead(getString(R.string.shoppingmall), true, true);

        rv = findViewById(R.id.rv_);
        refreshLayout = findViewById(R.id.refreshLayout);

        setRecycleView();
        doGetShopApi();
    }

    public void setRecycleView() {
        refreshLayout.setEnablePureScrollMode(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rv.setLayoutManager(layoutManager);
        rv.setAdapter(adapter = new BaseQuickAdapter(R.layout.item_shoping, new ArrayList<Gift>()) {
            @Override
            protected void convert(BaseViewHolder helper, Object item) {
                Gift gift = (Gift) item;
                GlideUtils.loadImage(context, gift.getCover(), helper.getView(R.id.car_buy_avatar));
                helper.setText(R.id.car_buy_name, gift.getGname());
                LogUtils.e(gift.getDescript());
                if (!TextUtils.isEmpty(gift.getDescript())) {
                    TextView textView = helper.getView(R.id.car_buy_details);
                    textView.setText(gift.getDescript());
                    textView.setVisibility(View.VISIBLE);
                }
                helper.getView(R.id.car_buy).setVisibility(View.VISIBLE);
                helper.addOnClickListener(R.id.car_buy);
                helper.addOnClickListener(R.id.car_buy_avatar);
            }
        });

        adapter.setOnItemChildClickListener((adapter, view, position) -> {
            Gift gift = (Gift) adapter.getItem(position);
            if (view.getId() == R.id.car_buy) {
                BuyPronFragment buyPronFragment = BuyPronFragment.newInstance(gift);
                buyPronFragment.show(getSupportFragmentManager(), "dialogFragment");
            } else {
                SvgaPreview svgaPreviewDialog = new SvgaPreview();
                Bundle args = new Bundle();
                args.putSerializable(SvgaPreview.SVGA_GIFT_KEY, gift);
                svgaPreviewDialog.setArguments(args);
                svgaPreviewDialog.show(getSupportFragmentManager(), "show svga preview dialog");
            }
        });
    }

    /**
     * 道具商城
     */
    public void doGetShopApi() {

        Api_Config.ins().getProp(new JsonCallback<List<Gift>>() {
            @Override
            public void onSuccess(int code, String msg, List<Gift> data) {
                if (data != null) LogUtils.e(code + "," + msg + "," + new Gson().toJson(data));
                if (code == 0) {
                    if (data != null) {
                        List<Gift> newData = new ArrayList<>();
                        for (Gift gift : data) {
                            if (gift.getType() == 1 && gift.getIsShow() == 1) {
                                newData.add(gift);
                            }
                        }
                        Collections.sort(newData, new Comparator<Gift>() {
                            @Override
                            public int compare(Gift t0, Gift t1) {
                                return (int) t0.getSort() - (int) t1.getSort();
                            }
                        });
                        adapter.setNewData(newData);
                    }
                } else {
                    showEmptyView(msg);
                }
            }
        });
    }
}
