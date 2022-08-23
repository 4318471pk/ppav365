package com.live.fox.albumsel;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.live.fox.R;
import com.live.fox.base.BaseHeadFragment;
import com.live.fox.utils.BarUtils;
import com.live.fox.utils.LogUtils;
import com.live.fox.utils.StatusBarUtil;
import com.live.fox.utils.ToastUtils;
import com.live.fox.utils.device.DeviceUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


/**
 * 选择照片
 */
public class AlbumFragment extends BaseHeadFragment {

    RecyclerView recyclerView;
    ProgressBar progressBar;
    View emptyView;

    AlbumAdapter adapter;
    List<String> imageList = new ArrayList<>();
    private int cropWidth;
    private int cropHeight;

    //每行展示几张图片。
    int columnCount = 3;

//    RxPermissions rxPermissions;

    //imageType 1:头像  2:背景 剪裁用
    public static AlbumFragment newInstance(int imageType) {
        AlbumFragment fragment = new AlbumFragment();
        Bundle args = new Bundle();
        args.putInt("imageType", imageType);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.album_fragment, null, false);
//        rxPermissions =  new RxPermissions(this);
        setView(rootView);
        return rootView;
    }

    public void setView(View view) {

        initToolHead(view, "选择相册", true, true);

        StatusBarUtil.setStatusBarFulAlpha(getActivity());
        //设置状态栏文字为灰色
        BarUtils.setStatusBarLightMode(getActivity(), false);

        Bundle args = getArguments();
        if(args!=null){
            int imageType = args.getInt("imageType");

            //imageType 1:头像  2:背景 剪裁用
            cropWidth = DeviceUtils.getScreenWidth(getActivity());
            cropHeight = cropWidth;
            if (imageType == 2) {
                cropHeight = cropWidth * 5 / 7;
            }
        }

        recyclerView = view.findViewById(R.id.rv_);
        progressBar = view.findViewById(R.id.progressBar);

        LogUtils.e(cropWidth+","+cropHeight);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), columnCount));
        recyclerView.setAdapter(adapter = new AlbumAdapter(new ArrayList<String>(), getImageSize(), cropWidth, cropHeight));

        emptyView = getLayoutInflater().inflate(R.layout.no_content_view, (ViewGroup) recyclerView.getParent(), false);
        emptyView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.setEmptyView(R.layout.view_loading, (ViewGroup) recyclerView.getParent());
                loadImages();
            }
        });
        adapter.setEmptyView(emptyView);

        loadImages();
    }

    public int getImageSize(){
        WindowManager windowManager = (WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(metrics);
        return metrics.widthPixels / columnCount;
    }

    /**
     * 开启线程，开始加载相册中的图片。
     */
    private void loadImages() {
        new Thread(new ImageLoaderRunnable()).start();
    }

    private class ImageLoaderRunnable implements Runnable {
        public void run() {
            Cursor cursor = null;
            try {
                cursor = getActivity().getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new String[]{MediaStore.Images.Media.DATA}, null, null, MediaStore.Images.Media.DATE_ADDED);
                if (cursor != null) {
                    imageList.clear();
                    if (cursor.moveToLast()) {
                        do {
                            String path = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA));
                            File file = new File(path);
                            if (file.length() > 5 * 1024) {
                                imageList.add(path);
                            }
                        } while (cursor.moveToPrevious());
                    }
                }
            } catch (Exception e) {
                Log.e("AlbumFragment", e.getMessage());
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
            loadComplete();
        }
    }

    /**
     * 所有相册中的图片加载完成，通知刷新界面。
     */
    public void loadComplete() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (imageList.isEmpty()) {
                    ToastUtils.showShort("您的相册空空如也");
                }else {
                    adapter.setNewData(imageList);
                    progressBar.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case 1:
                break;
            case 203:
                if (resultCode == 0) {
                    return;
                }

                Intent intent = new Intent();
//                CropImage.ActivityResult result = CropImage.getActivityResult(data);
//                if(resultCode == RESULT_OK) {
//                    intent.putExtra("image_path", result.getUri().getPath());
//                } else if(resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
//                    Log.e("AlbumActivity", "Cropping failed: " + result.getError().getMessage());
//                }

                getActivity().setResult(resultCode, intent);
                getActivity().finish();
                break;
        }
    }
}

