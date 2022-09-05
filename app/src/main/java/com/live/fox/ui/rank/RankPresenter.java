package com.live.fox.ui.rank;

import com.live.fox.R;
import com.live.fox.common.JsonCallback;
import com.live.fox.entity.RankListEntity;
import com.live.fox.server.Api_Rank;
import com.live.fox.utils.GsonUtil;
import com.live.fox.utils.LogUtils;
import com.live.fox.utils.StringUtils;


/**
 * 处理请求数据
 */
public class RankPresenter {

    private final IRankView rankView;

    public RankPresenter(IRankView view) {
        rankView = view;
    }

    public void doRequestData(int type) {
        Api_Rank.ins().getRankList(type, new JsonCallback<String>() {
            @Override
            public void onSuccess(int code, String msg, String data) {
                if (code == 0) {
                    try {
                        RankListEntity rankListEntity = GsonUtil.getObject(data, RankListEntity.class);
                        rankView.requestResult(rankListEntity);
                    } catch (Exception e) {
                        LogUtils.e(e.getMessage());
                        if (rankView instanceof RankListFragment) {
                            RankListFragment rankListFragment = (RankListFragment) rankView;
                            rankView.error(rankListFragment.getString(R.string.jiexiWrong));
                        }
                    }
                } else {
                    if (StringUtils.isEmpty(msg)) msg = " ";
                    rankView.error(msg);
                }
            }
        });
    }
}
