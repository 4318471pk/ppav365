package com.live.fox.adapter;

import android.annotation.SuppressLint;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.live.fox.App;
import com.live.fox.Constant;
import com.live.fox.R;
import com.live.fox.entity.Audience;
import com.live.fox.entity.FunctionItem;
import com.live.fox.ui.mine.noble.NobleFragment;
import com.live.fox.utils.DensityUtils;
import com.live.fox.utils.GlideUtils;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class AudienceAdapter extends BaseQuickAdapter<Audience, BaseViewHolder> {

    public AudienceAdapter(List<Audience> data) {
        super(R.layout.item_audience, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Audience audience) {
        boolean flag = false;
        FunctionItem mNobleRes = null;
        if (audience.getBadgeList() != null && audience.getBadgeList().size() > 0) {
            if (audience.getBadgeList().contains(6)) {
                mNobleRes = NobleFragment.getNobleRes(helper.itemView.getContext(), Constant.LEVEL1);
                flag = true;
            } else if (audience.getBadgeList().contains(7)) {
                mNobleRes = NobleFragment.getNobleRes(helper.itemView.getContext(), Constant.LEVEL2);
                flag = true;
            } else if (audience.getBadgeList().contains(8)) {
                mNobleRes = NobleFragment.getNobleRes(helper.itemView.getContext(), Constant.LEVEL3);
                flag = true;
            } else if (audience.getBadgeList().contains(9)) {
                mNobleRes = NobleFragment.getNobleRes(helper.itemView.getContext(), Constant.LEVEL4);
                flag = true;
            } else if (audience.getBadgeList().contains(10)) {
                mNobleRes = NobleFragment.getNobleRes(helper.itemView.getContext(), Constant.LEVEL5);
                flag = true;
            }
        }
        if (flag) {
            RelativeLayout rlNick = helper.getView(R.id.rlNick);
            ViewGroup.LayoutParams mLayoutParams = rlNick.getLayoutParams();
            mLayoutParams.width = DensityUtils.dp2px(App.getInstance(), 33);
            mLayoutParams.height = DensityUtils.dp2px(App.getInstance(), 31);
            rlNick.setBackgroundResource(mNobleRes.circleBg);
        } else {
            RelativeLayout rlNick = helper.getView(R.id.rlNick);
            ViewGroup.LayoutParams mLayoutParams = rlNick.getLayoutParams();
            mLayoutParams.width = ViewGroup.LayoutParams.WRAP_CONTENT;
            mLayoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            rlNick.setBackgroundResource(0);
        }
        ImageView imageView = helper.getView(R.id.iv_audienceavatar);
        GlideUtils.loadDefaultCircleImage(mContext, audience.getAvatar(), imageView);
    }


    @SuppressLint("CheckResult")
    public void addItem(Audience data) {
        if (data == null) return;
        List<Audience> dataList = getData();

        boolean isTong = false;
        //去重操作
        for (Audience tcSimpleUserInfo : dataList) {
            if (tcSimpleUserInfo.getUid() == data.getUid()) {
                isTong = true;
                break;
            }
        }
        if (isTong) return;

        dataList.add(data);
        Collections.sort(dataList, new Comparator<Audience>() {
            @Override
            public int compare(Audience t0, Audience t1) {
                return (int) t1.getUserExp() - (int) t0.getUserExp();
            }
        });
        int indexInsert = dataList.indexOf(data);
//        LogUtils.d("需要刷新的index:" + indexInsert);
//        LogUtils.d("indexInsert:" + indexInsert);
        LinearLayoutManager manager = (LinearLayoutManager) getRecyclerView().getLayoutManager();
        int itemCount = manager.findLastVisibleItemPosition() - manager.findFirstVisibleItemPosition();
        int firstVisibleItemPosition = manager.findFirstVisibleItemPosition();
        if (indexInsert >= firstVisibleItemPosition && indexInsert <= firstVisibleItemPosition + itemCount) {//如果要插入的数据在 可见区域内就刷新
            notifyItemRangeChanged(firstVisibleItemPosition, itemCount);
        }
    }

    public void removeItem(long userId) {
        List<Audience> dataList = getData();
        int index = -1;
        for (int i = 0; i < dataList.size(); i++) {
            if (dataList.get(i).getUid() == userId) {
                index = i;
                break;
            }
        }
        if (index >= 0) {
            remove(index);
        }

    }

}
