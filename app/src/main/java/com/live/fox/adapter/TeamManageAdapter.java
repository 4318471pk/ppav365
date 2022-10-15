package com.live.fox.adapter;

import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.live.fox.R;

import java.util.List;

public class TeamManageAdapter extends BaseQuickAdapter<String, BaseViewHolder> {


    public TeamManageAdapter(List data) {
        super(R.layout.item_team_manage, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, String data) {

        int pos = helper.getLayoutPosition();
        if (pos % 2 == 0) {
            helper.getView(R.id.mView).setBackgroundColor(mContext.getResources().getColor(R.color.colorFAFAFA));
        } else {
            helper.getView(R.id.mView).setBackgroundColor(mContext.getResources().getColor(R.color.white));
        }

        helper.getView(R.id.ivCz).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (teamManageOperate != null) {
                    teamManageOperate.oparate(pos);
                }
            }
        });


    }

    public interface TeamManageOperate{
        public void oparate(int pos);
    }

    private TeamManageOperate teamManageOperate;

    public void setTeamManageOperate(TeamManageOperate teamManageOperate) {
        this.teamManageOperate = teamManageOperate;
    }

}
