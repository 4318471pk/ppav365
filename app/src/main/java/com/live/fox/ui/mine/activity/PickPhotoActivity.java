package com.live.fox.ui.mine.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;
import androidx.viewpager.widget.ViewPager;

import com.live.fox.App;
import com.live.fox.Constant;
import com.live.fox.R;
import com.live.fox.pickphoto.MultiplexImage;
import com.live.fox.pickphoto.PickPhotoAdapter;
import com.live.fox.utils.BarUtils;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by cheng on 2016/9/3.
 */
public class PickPhotoActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener {

    private static final String TAG = PickPhotoActivity.class.getName();

    private ViewPager viewPager;
    private TextView hint;
    private PickPhotoAdapter adapter;

    private List<MultiplexImage> images;
    int position;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.pickview_activity);

        initView();
    }

    private void initView() {
        viewPager = (ViewPager) this.findViewById(R.id.viewPager);
        hint = (TextView) this.findViewById(R.id.hint);

//        getSupportActionBar().hide();
//        Action
        BarUtils.setStatusBarColor(this, Color.BLACK);

        images = getIntent().getParcelableArrayListExtra("images");
        position = getIntent().getIntExtra("position", 0);

        if (adapter == null && images != null && images.size() != 0) {
            adapter = new PickPhotoAdapter(this, images);
//            hiddenOriginalButton(position);
            viewPager.setAdapter(adapter);
            viewPager.setCurrentItem(position);
            viewPager.addOnPageChangeListener(this);
            if (images.size() == 1) {
                hint.setVisibility(View.GONE);
            }
            hint.setText(position + 1 + "/" + images.size());
        }

    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        hint.setText(position + 1 + "/" + images.size());

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }


    public static void startActivity(Context context, View view, List<MultiplexImage> imageList, int position) {
        Constant.isAppInsideClick = true;
        Intent intent = new Intent(context, PickPhotoActivity.class);
        intent.putParcelableArrayListExtra("images", (ArrayList<? extends Parcelable>) imageList);
        intent.putExtra("position", position);
        if (Build.VERSION.SDK_INT < 21) {
            context.startActivity(intent);
        } else {
            ActivityOptionsCompat options = ActivityOptionsCompat.
                    makeSceneTransitionAnimation((Activity) context, view, "photoshow");
            context.startActivity(intent, options.toBundle());
        }
    }

    public static void startActivity(Context context, View view, String imageUrl) {
        List<MultiplexImage> images = new ArrayList<MultiplexImage>();
        images.add(new MultiplexImage(imageUrl));
        startActivity(context, view, images, 0);
    }

    public static void startActivity(Context context, View view, String imageUrl, String thumImageUrl) {
        List<MultiplexImage> images = new ArrayList<MultiplexImage>();
        images.add(new MultiplexImage(imageUrl, thumImageUrl));
        startActivity(context, view, images, 0);
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(0, R.anim.trans_to_bottom);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Constant.isShowWindow) {
            Constant.isOpenWindow = true;
            App.getInstance().getFloatView().addToWindow(true, this);
        }
    }
}
