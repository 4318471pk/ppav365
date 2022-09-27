package com.live.fox.ui.mine;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.live.fox.R;
import com.live.fox.base.BaseHeadActivity;
import com.live.fox.utils.AppUtils;
import com.live.fox.utils.BarUtils;
import com.live.fox.utils.ClipboardUtils;
import com.live.fox.utils.StatusBarUtil;
import com.live.fox.utils.StringUtils;


public class AboutUsActivity extends BaseHeadActivity {

    private TextView tvVersion;
    private TextView tvOfficialhttp;
    private TextView tvOfficialemail;

    public static void startActivity(@NonNull Context context) {
        context.startActivity(new Intent(context, AboutUsActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aboutus_activity);

        initView();
    }

    private void initView() {
        StatusBarUtil.setStatusBarFulAlpha(this);
        BarUtils.setStatusBarLightMode(this, false);
        setHead(getString(R.string.about), true, true);


        tvOfficialhttp.setText("-");
        tvOfficialemail.setText("-");
        tvVersion.setText(String.format(getString(R.string.currentBan), AppUtils.getAppVersionName()));
        tvVersion = findViewById(R.id.tv_version);
        tvOfficialhttp = findViewById(R.id.tv_officialhttp);
        tvOfficialemail = findViewById(R.id.tv_officialemail);
        findViewById(R.id.layout_officialhttp).setOnClickListener(this);
        findViewById(R.id.layout_officialemail).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_officialhttp:
                String Officialhttp = tvOfficialhttp.getText().toString().trim();
                if (!StringUtils.isEmpty(Officialhttp)) {
                    ClipboardUtils.copyText(Officialhttp);
                    showToastTip(true, getString(R.string.officialWebsiteCutting));
                }
                break;
            case R.id.layout_officialemail:
                String OfficialEmail = tvOfficialemail.getText().toString().trim();
                if (!StringUtils.isEmpty(OfficialEmail)) {
                    ClipboardUtils.copyText(OfficialEmail);
                    showToastTip(true, getString(R.string.officialyouxianCutting));
                }
                break;
        }
    }
}
