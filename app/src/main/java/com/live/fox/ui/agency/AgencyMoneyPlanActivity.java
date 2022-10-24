package com.live.fox.ui.agency;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.live.fox.Constant;
import com.live.fox.R;
import com.live.fox.adapter.AgencyLowerMoneyAdapter;
import com.live.fox.base.BaseBindingViewActivity;
import com.live.fox.databinding.ActivityAgencyMoneyPlanBinding;

import java.util.ArrayList;
import java.util.List;

public class AgencyMoneyPlanActivity extends BaseBindingViewActivity {

    ActivityAgencyMoneyPlanBinding mBind;

    boolean fromMain = true;

    AgencyLowerMoneyAdapter mAdapter;
    List<String> mData = new ArrayList<>();

    public static void startActivity(Activity activity, boolean fromMain, String living, String game, int code) {
        Constant.isAppInsideClick = true;
        Intent intent = new Intent(activity, AgencyMoneyPlanActivity.class);
        intent.putExtra("fromMain", fromMain);
        intent.putExtra("living", living);
        intent.putExtra("game", game);
        activity.startActivityForResult(intent, code);
    }

    @Override
    public void onClickView(View view) {

    }

    @Override
    public int onCreateLayoutId() {
        return R.layout.activity_agency_money_plan;
    }

    @Override
    public void initView() {
        mBind = getViewDataBinding();
        setHeadGone();
        mBind.ivHeadLeft.setOnClickListener(view -> {finish();});

        fromMain = this.getIntent().getBooleanExtra("fromMain", fromMain);
        if (!fromMain) {
            mBind.etLiving.setText(getIntent().getStringExtra("living"));
            mBind.etGame.setText(getIntent().getStringExtra("game"));
        }

        mData.add("1"); mData.add(""); mData.add(""); mData.add(""); mData.add("1");
        mAdapter = new AgencyLowerMoneyAdapter(mData);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mBind.rc.setLayoutManager(layoutManager);
        mBind.rc.setAdapter(mAdapter);


    }


    @Override
    public void onBackPressed() {
        if (!fromMain) {
            Intent intent = new Intent();
            intent.setClass(this, PromoMaterialActivity.class);
            intent.putExtra("living", mBind.etLiving.getText().toString().trim() );
            intent.putExtra("game", mBind.etGame.getText().toString().trim() );
            setResult(RESULT_OK, intent);
        }
        super.onBackPressed();

    }
}
