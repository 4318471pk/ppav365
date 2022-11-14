package com.live.fox.ui.agency;

import android.app.Activity;
import android.content.Intent;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.PopupWindow;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.live.fox.Constant;
import com.live.fox.R;
import com.live.fox.adapter.TeamManageAdapter;
import com.live.fox.base.BaseBindingViewActivity;
import com.live.fox.databinding.ActivityTeamManageBinding;

import java.util.ArrayList;
import java.util.List;

public class TeamManageActivity extends BaseBindingViewActivity {

    ActivityTeamManageBinding mBind;

    TeamManageAdapter teamManageAdapter;
    List<String> mData = new ArrayList<>();
    PopupWindow popupWindowOperate;

    EditText etLiving;
    EditText etGame;

    private int clickPos = 0;

    public static void startActivity(Activity activity) {
        Constant.isAppInsideClick = true;
        Intent intent = new Intent(activity, TeamManageActivity.class);
        activity.startActivity(intent);
    }

    @Override
    public void onClickView(View view) {

    }

    @Override
    public int onCreateLayoutId() {
        return R.layout.activity_team_manage;
    }

    @Override
    public void initView() {
        mBind = getViewDataBinding();
        setHeadGone();
        mBind.ivHeadLeft.setOnClickListener(view -> {finish();});
        setPopOperate();
        mData.add("1"); mData.add(""); mData.add(""); mData.add(""); mData.add("1");
        teamManageAdapter = new TeamManageAdapter(mData);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mBind.rc.setLayoutManager(layoutManager);
        mBind.rc.setAdapter(teamManageAdapter);

        teamManageAdapter.setTeamManageOperate(new TeamManageAdapter.TeamManageOperate() {
            @Override
            public void oparate(int pos) {
                clickPos = pos;
                //etGame.setText();
                //etLiving.setText();
                popupWindowOperate.showAtLocation(mBind.getRoot(), Gravity.CENTER | Gravity.CENTER_HORIZONTAL, 0, 0);
                setRootAlpha(0.35f);

            }
        });
    }


    private void setPopOperate(){
        View popupView = this.getLayoutInflater().inflate(R.layout.pop_operate_team_manage,null);

        popupWindowOperate = new PopupWindow(popupView, ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT,true);
        popupWindowOperate.setFocusable(true);
        popupWindowOperate.setOutsideTouchable(false);// 设置同意在外点击消失

        popupWindowOperate.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                //在dismiss中恢复透明度
                setRootAlpha(1f);
            }
        });
        popupWindowOperate.setAnimationStyle(R.style.ActionSheetDialogAnimation);

        etLiving = popupView.findViewById(R.id.etLiving);
        etGame = popupView.findViewById(R.id.etGame);

        popupView.findViewById(R.id.imgClose).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindowOperate.dismiss();
            }
        });

        popupView.findViewById(R.id.iv_close_living).setOnClickListener(view -> etLiving.setText(""));
        popupView.findViewById(R.id.iv_close_game).setOnClickListener(view -> etGame.setText(""));

        popupView.findViewById(R.id.tvSave).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popupWindowOperate.dismiss();
                    }
                });


    }



    private void setRootAlpha(float al){
        WindowManager.LayoutParams lp = this.getWindow().getAttributes();
        lp.alpha= al;
        this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        this.getWindow().setAttributes(lp);
    }

}
