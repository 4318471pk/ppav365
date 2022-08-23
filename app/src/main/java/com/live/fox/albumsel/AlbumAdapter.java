package com.live.fox.albumsel;

import android.app.Activity;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.live.fox.R;
import com.live.fox.utils.ImageUtil;
import com.live.fox.utils.ToastUtils;

import java.io.File;
import java.util.ArrayList;


public class AlbumAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    //    List<String> mImageList = new ArrayList<>();
    int mImageSize = 0;
    int cropW = 0;
    int cropH = 0;

//    public AlbumAdapter(List<String> data) {
//        super(R.layout.album_image_item, data);
//    }

    public AlbumAdapter(ArrayList<String> data, int imageSize, int cropW, int cropH) {
        super(R.layout.album_image_item, data);
        this.mImageSize = imageSize;
        this.cropW = cropW;
        this.cropH = cropH;
    }

    @Override
    protected void convert(final BaseViewHolder holder, final String imagePath) {
        ImageView iv = holder.getView(R.id.albumImage);
        iv.getLayoutParams().width = mImageSize;
        iv.getLayoutParams().height = mImageSize;
        if (ImageUtil.isGif(imagePath)) {
            RequestOptions options = new RequestOptions().placeholder(R.drawable.album_loading_bg).error(R.drawable.album_loading_bg)
                    .override(mImageSize, mImageSize);
            Glide.with(mContext).load(imagePath).apply(options).into(iv);
        } else {
//            Glide.with(mContext)
//                    .load(imagePath)
//                    .placeholder(R.drawable.album_loading_bg)
//                    .override(mImageSize, mImageSize)
//                    .into(holder.image);
//            GlideUtils.loadImage(mContext, imagePath, R.drawable.album_loading_bg, mImageSize, mImageSize, iv);
        }

        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mContext instanceof Activity) {
                    Activity activity = (Activity) mContext;
//                        int position = holder.getAdapterPosition();
                    Uri fileUri = Uri.fromFile(new File(imagePath));
//                        CropImage.activity(fileUri).setGuidelines(CropImageView.Guidelines.ON)
//                                .setFixAspectRatio(true)
//                                .setAspectRatio(cropW, cropH)
//                                .setActivityTitle("剪裁")
//                                .setRequestedSize(cropW, cropH)
//                                .setCropMenuCropButtonIcon(R.drawable.ic_crop)
//                                .start(activity);
                } else {
                    ToastUtils.showShort(mContext.getString(R.string.baseLoading));
                }
            }
        });
//        holder.setText(R.id.tv_roomtitle, liveInfo.getTitle())
//              .setText(R.id.tv_username, liveInfo.getNick())
//              .setText(R.id.tv_popularity, liveInfo.getView());
//
////        Glide.with(mContext).load(liveInfo.getThumb())
////                .placeholder(R.mipmap.default_ivbg).error(R.mipmap.default_ivbg).crossFade().centerCrop().diskCacheStrategy(DiskCacheStrategy.SOURCE)
////                .into((ImageView) holder.getView(R.id.iv_live));
//        GlideUtils.loadImage(mContext, liveInfo.getThumb(),R.mipmap.default_ivbg, R.mipmap.default_ivbg, true, (ImageView) holder.getView(R.id.iv_live));
    }




}
