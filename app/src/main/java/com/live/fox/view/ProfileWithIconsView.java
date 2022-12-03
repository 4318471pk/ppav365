package com.live.fox.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.live.fox.R;
import com.live.fox.db.LocalUserGuardDao;
import com.live.fox.db.LocalUserTagResourceDao;
import com.live.fox.entity.Audience;
import com.live.fox.entity.UserGuardResourceBean;
import com.live.fox.entity.UserTagResourceBean;
import com.live.fox.utils.GlideUtils;
import com.live.fox.utils.ResourceUtils;

public class ProfileWithIconsView extends RelativeLayout {

    ImageView ivUserImg;
    ImageView ivEdge;
    ImageView ivTag1;
    ImageView ivTag2;
    int[] decorationResource = null;
    float scaleAndMargins[][]={{0.87f,0.14f},{0.84f,0.05f},{0.87f,0.12f},{0.85f,0.07f},{0.87f,0.1f},{0.85f,0.1f},{0.85f,0.08f}};

    public ProfileWithIconsView(Context context) {
        super(context);
        initView();
    }

    public ProfileWithIconsView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public ProfileWithIconsView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_profile_with_iconns,this,true);
        ivUserImg=findViewById(R.id.ivUserImg);
        ivEdge=findViewById(R.id.ivEdge);
        ivTag1=findViewById(R.id.ivTag1);
        ivTag2=findViewById(R.id.ivTag2);

        decorationResource=new ResourceUtils().getResourcesID(R.array.rankEdgePics);
    }

    public ImageView getIvUserImg() {
        return ivUserImg;
    }

    public void setData(Integer vipLevel, Integer guardLevel,String avatar)
    {
        if(!TextUtils.isEmpty(avatar))
        {
            GlideUtils.loadCircleImage(getContext(),avatar,0,0,ivUserImg);
        }
        else
        {
            ivUserImg.setImageDrawable(null);
        }

        if(vipLevel==null || vipLevel<1 || vipLevel>7)
        {
            ivEdge.setVisibility(GONE);
            ivTag2.setVisibility(GONE);
            ivUserImg.setScaleX(1.0f);
            ivUserImg.setScaleY(1.0f);
        }
        else
        {
            ivEdge.setVisibility(VISIBLE);
            ivEdge.setImageResource(decorationResource[vipLevel-1]);
            ivUserImg.setScaleX(scaleAndMargins[vipLevel-1][0]);
            ivUserImg.setScaleY(scaleAndMargins[vipLevel-1][0]);

            UserTagResourceBean userTagResourceBean= LocalUserTagResourceDao.getInstance().getLevelTag(vipLevel);
            if(userTagResourceBean!=null && !TextUtils.isEmpty(userTagResourceBean.getLocalMedalUrlPath()))
            {
                Bitmap bitmap=BitmapFactory.decodeFile(userTagResourceBean.getLocalMedalUrlPath());
                if(bitmap!=null)
                {
                    ivTag2.setImageBitmap(bitmap);
                }
            }
        }

        if(guardLevel!=null)
        {
            UserGuardResourceBean userGuardResourceBean= LocalUserGuardDao.getInstance().getLevel(guardLevel);
            if(userGuardResourceBean!=null && !TextUtils.isEmpty(userGuardResourceBean.getLocalImgSmallPath()))
            {
                Bitmap bitmap= BitmapFactory.decodeFile(userGuardResourceBean.getLocalImgSmallPath());
                if(bitmap!=null)
                {
                    ivTag1.setVisibility(VISIBLE);
                    ivTag1.setImageBitmap(bitmap);
                }
            }
        }
        else
        {
            ivTag1.setVisibility(GONE);
        }
    }


}
