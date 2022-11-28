package com.tencent.demo.avatar.activity;

import static com.tencent.demo.activity.CameraXActivity.EXTRA_IS_BACK_CAMERA;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.tencent.demo.R;

public class GenderActivity extends AppCompatActivity implements View.OnClickListener {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gender_activity_layout);
        findViewById(R.id.back_btn).setOnClickListener(this);
        findViewById(R.id.male_icon).setOnClickListener(this);
        findViewById(R.id.female_icon).setOnClickListener(this);


    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.back_btn) {
            finish();
        } else if (id == R.id.male_icon) {
            action(true);
        } else if (id == R.id.female_icon) {
            action(false);
        }
    }


    private void action(boolean isMale) {
        Intent intent = new Intent(this, CaptureActivity.class);
        intent.putExtra(CaptureActivity.IS_MALE_KEY, isMale);
        intent.putExtra(EXTRA_IS_BACK_CAMERA,getIntent().getBooleanExtra(EXTRA_IS_BACK_CAMERA,false));
        startActivity(intent);
        finish();
    }
}
