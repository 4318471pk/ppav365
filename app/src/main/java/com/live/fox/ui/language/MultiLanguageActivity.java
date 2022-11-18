package com.live.fox.ui.language;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.live.fox.R;
import com.live.fox.SplashActivity;
import com.live.fox.adapter.devider.DividerItemDecoration;
import com.live.fox.base.BaseHeadActivity;
import com.live.fox.language.LocalManager;
import com.live.fox.language.MultiLanguageUtils;
import com.live.fox.utils.ActivityManager;
import com.live.fox.utils.BarUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


/**
 * 多语言切换
 */
public class MultiLanguageActivity extends BaseHeadActivity {

    public static void launch(Context context) {
        Intent intent = new Intent(context, MultiLanguageActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multi_language);
        BarUtils.setStatusBarLightMode(this, false);

        setHead(getString(R.string.language_choose), true, false);
        handleRecyclerView();
    }

    /**
     * 语言类别
     */
    private void handleRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.multi_language_recycler);
        recyclerView.addItemDecoration(new DividerItemDecoration(0xffF5F1F8).setDevideHeight(2));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        LanguageAdapter adapter = new LanguageAdapter();
        recyclerView.setAdapter(adapter);

//        RecyclerSpace recyclerSpace = new RecyclerSpace(DeviceUtils.dp2px(this, 10));
//        recyclerView.addItemDecoration(recyclerSpace);

        LanguageEntity ch = new LanguageEntity();
        ch.setType(ContextCompat.getDrawable(this, R.drawable.icon_flag_cn));
        ch.setLanguage("简体中文");
        ch.setSelected(MultiLanguageUtils.getRequestHeader().equals("zh-cn"));

        LanguageEntity vi = new LanguageEntity();
        vi.setType(ContextCompat.getDrawable(this, R.drawable.icon_flag_vi));
        vi.setLanguage("ENGLISH");
        vi.setSelected(MultiLanguageUtils.getRequestHeader().equals("en-us"));

        LanguageEntity th = new LanguageEntity();
        th.setType(ContextCompat.getDrawable(this, R.drawable.icon_flag_thi));
        th.setLanguage("台湾繁体");
        th.setSelected(MultiLanguageUtils.getRequestHeader().equals("zh-tw"));

        List<LanguageEntity> languageEntities = new ArrayList<>();
        languageEntities.add(ch);
        languageEntities.add(vi);
        languageEntities.add(th);

        adapter.addData(languageEntities);
        adapter.setOnItemClickListener((adapter1, view, position) -> {
            Locale locale = LocalManager.getLocalByPosition(position);
            MultiLanguageUtils.changeAppLanguage(this, locale, true);

            ActivityManager.getInstance().finishAllActivity();
            Intent intent = new Intent(MultiLanguageActivity.this, SplashActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });
    }
}