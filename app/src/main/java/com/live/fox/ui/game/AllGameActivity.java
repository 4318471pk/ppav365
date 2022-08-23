package com.live.fox.ui.game;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.live.fox.Constant;
import com.live.fox.R;
import com.live.fox.utils.BarUtils;

/**
 * 所有有游戏的界面
 */
public class AllGameActivity extends AppCompatActivity {

    public static void launch(Context context) {
        Constant.isAppInsideClick=true;
        Intent intent = new Intent(context, AllGameActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BarUtils.setStatusBarAlpha(this);
        setContentView(R.layout.activity_all_game);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        GameFragment fragment = new GameFragment();
        Bundle args = new Bundle();
        args.putBoolean(GameFragment.SINGLE_ACTIVITY_ALL_GAME, true);
        fragment.setArguments(args);
        transaction.add(R.id.all_game_frame_layout, fragment, "left_tag");
        transaction.commitAllowingStateLoss();
    }
}