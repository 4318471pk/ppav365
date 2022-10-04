package com.live.fox.ui.mine.noble;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.live.fox.R;
import com.live.fox.base.BaseActivity;
import com.live.fox.utils.BarUtils;

import static com.live.fox.R.layout.activity_noble_detail;

public class NobleDetailActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BarUtils.setStatusBarAlpha(this);
        setContentView(activity_noble_detail);
        TextView tvHead = findViewById(R.id.tv_head);
        tvHead.setText(getString(R.string.noble_2) + "Q&A");
        findViewById(R.id.iv_head_left).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
