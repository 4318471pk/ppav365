package com.live.fox.ui.mine.zbgl;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.flyco.roundview.RoundTextView;
import com.live.fox.Constant;
import com.live.fox.R;
import com.live.fox.base.BaseFragment;
import com.live.fox.common.JsonCallback;
import com.live.fox.entity.ZbjlBean;
import com.live.fox.server.Api_User;
import com.live.fox.utils.RegexUtils;
import com.live.fox.utils.TimeUtils;
import com.live.fox.utils.ToastUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;



public class ZbjlFragment extends BaseFragment implements View.OnClickListener {
    BaseQuickAdapter adapter;
    private TextView tvTotalTime;
    private TextView tvMoney;
    private TextView tvGift;
    private TextView tvCp;
    private LinearLayout dilaogValueLayout;
    private TextView tvFrom;
    private TextView tvTo;
    private RoundTextView tvCx;
    private RecyclerView rv;
    private SmartRefreshLayout refreshLayout;
    private Long uid;
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
    int page = 0;
    List<String> openListPos = new ArrayList<>();

    public static ZbjlFragment newInstance(Long uid) {
        ZbjlFragment fragment = new ZbjlFragment();
        Bundle args = new Bundle();
        args.putLong("uid", uid);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.layout_zbjl, null, false);//layout_zbjl
        setView(rootView);
        initData(getArguments());
        return rootView;
    }


    public void initData(Bundle arguments) {
        if (arguments != null) {
            uid = arguments.getLong("uid");
            doSearch(true);
        }
    }

    public void setView(View bindSource) {
        tvTotalTime = bindSource.findViewById(R.id.tv_total_time);
        tvMoney = bindSource.findViewById(R.id.tv_money);
        tvGift = bindSource.findViewById(R.id.tv_gift);
        tvCp = bindSource.findViewById(R.id.tv_cp);
        dilaogValueLayout = bindSource.findViewById(R.id.dilaog_value_layout);
        tvFrom = bindSource.findViewById(R.id.tv_from);
        tvTo = bindSource.findViewById(R.id.tv_to);
        tvCx = bindSource.findViewById(R.id.tv_cx);
        rv = bindSource.findViewById(R.id.rv_);
        refreshLayout = bindSource.findViewById(R.id.refreshLayout);
         bindSource.findViewById(R.id.tv_cx).setOnClickListener(this);
        bindSource.findViewById(R.id.tv_from).setOnClickListener(this);
        bindSource.findViewById(R.id.tv_to).setOnClickListener(this);

        tvFrom.setText(TimeUtils.getNowString(simpleDateFormat));
        tvTo.setText(TimeUtils.getNowString(simpleDateFormat));//getTomorrowString
        setRecycleView();
    }

    public void setRecycleView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rv.setLayoutManager(layoutManager);
        rv.setAdapter(adapter = new BaseQuickAdapter(R.layout.item_zbjl, new ArrayList<ZbjlBean.JsonsBean>()) {
            @Override
            protected void convert(BaseViewHolder helper, Object item) {
                ZbjlBean.JsonsBean bean = (ZbjlBean.JsonsBean) item;
                helper.setText(R.id.tv_name, bean.getNickname());
                helper.setText(R.id.tv_ud, "ID:" + bean.getUid());
                helper.setText(R.id.tv_timelong, bean.getTotalstartTime());
                helper.setText(R.id.tv_money, RegexUtils.westMoney(bean.getFfml()));
                helper.setText(R.id.tv_gift, RegexUtils.westMoney(bean.getMl()));
                helper.setText(R.id.tv_cp, RegexUtils.westMoney(bean.getCp_statement()));
                helper.setText(R.id.tv_kbsj, "Bắt đầu live  " + TimeUtils.millis2String(bean.getStart_time()));
                helper.setText(R.id.tv_xbsj, "Kết thúc live  " + TimeUtils.millis2String(bean.getEnd_time()));
                ImageView imageView = helper.getView(R.id.iv_portrait);
                Glide.with(requireActivity()).load(bean.getAvatar()).into(imageView);
                if (openListPos.contains(helper.getLayoutPosition() + "")) {
                    helper.setImageResource(R.id.iv_isopen, R.drawable.zbjl_up);
                    helper.getView(R.id.layout_extend).setVisibility(View.VISIBLE);
                } else {
                    helper.setImageResource(R.id.iv_isopen, R.drawable.zbjl_down);
                    helper.getView(R.id.layout_extend).setVisibility(View.GONE);
                }
            }
        });
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (openListPos.contains(position + "")) {
                    openListPos.remove(position + "");
                } else {
                    openListPos.add(position + "");
                }
                adapter.notifyDataSetChanged();
            }
        });

        refreshLayout.setOnLoadMoreListener(refreshLayout -> {
            page = page + 1;
            doSearch(false);
        });
    }



    private void showDatePickerDialog(int themeResId, final TextView tv) {
        DatePickerDialog datePickerDialog;
        Calendar calendar = Calendar.getInstance(Locale.getDefault());
        // 绑定监听器(How the parent is notified that the date is set.)
        datePickerDialog = new DatePickerDialog(getCtx(), themeResId, (view, year, monthOfYear, dayOfMonth) -> {
            // 此处得到选择的时间，可以进行你想要的操作
            tv.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());// + 24 * 3600 * 1000
        datePickerDialog.show();
    }

    public void doSearch(boolean flag) {
        String fromTime = tvFrom.getText().toString().trim();
        String toTime = tvTo.getText().toString().trim();
        String dayNum = TimeUtils.getFitTimeSpan2(fromTime, toTime, simpleDateFormat, 1);
        if (Integer.parseInt(dayNum) < 30) {
            doSearchApi(flag, uid, TimeUtils.string2Millis(fromTime, simpleDateFormat), (TimeUtils.string2Millis(toTime, simpleDateFormat) + 24 * 3600 * 1000));
        } else {
            ToastUtils.showShort("Thời gian kiểm tra tối đa là 30 ngày");
        }
    }


    public void doSearchApi(boolean isRefresh, Long uid, Long startTime, Long endTime) {
        showLoadingView();
        Api_User.ins().searchAnchor2(uid, startTime, endTime, page, new JsonCallback<ZbjlBean>() {
            @Override
            public void onSuccess(int code, String msg, ZbjlBean data) {
                hideLoadingView();
                if (code == 0) {
                    if (data == null) {
                        showEmptyView(getString(R.string.noData));
                        tvTotalTime.setText("Tổng thời gian live:0");
                        tvMoney.setText("0");
                        tvGift.setText("0");
                        tvCp.setText("0");
                    } else {
                        tvTotalTime.setText("Tổng thời gian live:" + data.getHeartTime());
                        tvMoney.setText(RegexUtils.westMoney(data.getToTalffml()));
                        tvGift.setText(RegexUtils.westMoney(data.getToTalMl()));
                        tvCp.setText(RegexUtils.westMoney(data.getToTalcpStatement()));
                        if (data.getJsons() == null) {

                            showEmptyView(getString(R.string.noData));
                        } else {
                            if (isRefresh) {
                                if (data.getJsons().size() == 0) {

                                    showEmptyView(getString(R.string.noData));
                                } else {
                                    refreshLayout.finishRefresh();

                                    refreshLayout.setEnableLoadMore(true);
                                    hideEmptyView();
                                    adapter.setNewData(data.getJsons());
                                }
                            } else {
                                refreshLayout.finishLoadMore();
                                List<ZbjlBean> list = adapter.getData();
                                adapter.addData(data.getJsons());
                                adapter.notifyItemRangeInserted(list.size(), data.getJsons().size());
                            }
                            if (data.getJsons().size() < Constant.pageSizeLater) {
                                //如果没有更多的数据 则隐藏加载更多功能
                                refreshLayout.finishLoadMoreWithNoMoreData();
                            }
                        }
                    }
                } else {

                    showEmptyView(msg);
                }

            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_cx:
                doSearch(true);
                break;
            case R.id.tv_from:
                showDatePickerDialog(2, tvFrom);
                break;
            case R.id.tv_to:
                showDatePickerDialog(2, tvTo);
                break;
        }
    }
}

