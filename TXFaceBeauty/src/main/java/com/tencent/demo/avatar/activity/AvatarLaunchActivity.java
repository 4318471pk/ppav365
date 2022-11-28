package com.tencent.demo.avatar.activity;

import static com.tencent.demo.activity.CameraXActivity.EXTRA_IS_BACK_CAMERA;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import android.view.View.OnClickListener;
import android.widget.Button;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.tencent.demo.R;
import java.io.IOException;

public class AvatarLaunchActivity extends AppCompatActivity implements View.OnClickListener {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.avatar_launch_activity_layout);



        findViewById(R.id.custom_btn).setOnClickListener(this);
        findViewById(R.id.full_body_btn).setOnClickListener(this);
        findViewById(R.id.capture_btn).setOnClickListener(this);
        findViewById(R.id.btn_audio2exp).setOnClickListener(this);
        findViewById(R.id.btn_audio2exp_body).setOnClickListener(this);
        findViewById(R.id.btn_body_driven).setOnClickListener(this);
        findViewById(R.id.btn_qq_show).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        int id = v.getId();
        if (id == R.id.btn_audio2exp) {
            intent.setClass(this, Audio2ExpActivity.class);
        } else if (id == R.id.btn_audio2exp_body) {
            intent.setClass(this, Audio2ExpActivity.class);
            intent.putExtra("bodyMod", true);
        } else if (id == R.id.custom_btn) {
            intent.setClass(this, AvatarActivity.class);
        } else if (id == R.id.full_body_btn) {
            intent.setClass(this, AvatarActivity.class);
            intent.putExtra("bodyMod", true);
        } else if (id == R.id.capture_btn) {
            intent.setClass(this, GenderActivity.class);
            intent.putExtra(EXTRA_IS_BACK_CAMERA, getIntent().getBooleanExtra(EXTRA_IS_BACK_CAMERA, false));
        } else if (id == R.id.btn_body_driven) {
            intent.setClass(this, BodyTrackingActivity.class);
            intent.putExtra("avatar_res_name", "ch_0715_body_driven");
        } else if (id == R.id.btn_qq_show) {
            intent.setClass(this, BodyTrackingActivity.class);
            intent.putExtra("avatar_res_name", "video_sunnyboy_eye");
        } else {
            return;
        }
        startActivity(intent);

    }
}
