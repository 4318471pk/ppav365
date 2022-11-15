package com.live.fox.view.BulletMessage;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.live.fox.R;
import com.live.fox.db.LocalUserGuardDao;
import com.live.fox.db.LocalUserTagResourceDao;
import com.live.fox.entity.PersonalLivingMessageBean;
import com.live.fox.entity.UserGuardResourceBean;
import com.live.fox.entity.UserTagResourceBean;
import com.live.fox.utils.GlideUtils;
import com.live.fox.utils.ResourceUtils;
import com.live.fox.utils.Strings;
import com.live.fox.view.RankProfileView;

public class BulletMessageView extends LinearLayout {

    TextView tvName;
    TextView tvBulletMessage;
    ImageView ivUserImg;
    ImageView ivEdge;
    ImageView ivTag1;
    ImageView ivTag2;
    PersonalLivingMessageBean bean;
    int[] decorationResource = null;

    float scaleAndMargins[][]={{0.87f,0.14f},{0.84f,0.05f},{0.87f,0.12f},{0.85f,0.07f},{0.87f,0.1f},{0.85f,0.1f},{0.85f,0.08f}};

    public BulletMessageView(Context context,PersonalLivingMessageBean bean) {
        super(context);
        initView(bean);
    }

    private BulletMessageView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    private BulletMessageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void initView(PersonalLivingMessageBean bean)
    {
        LayoutInflater.from(getContext()).inflate(R.layout.bullet_message_layout,this,true);
        tvName=findViewById(R.id.tvName);
        tvBulletMessage=findViewById(R.id.tvBulletMessage);
        ivUserImg=findViewById(R.id.ivUserImg);
        ivEdge=findViewById(R.id.ivEdge);
        ivTag1=findViewById(R.id.ivTag1);
        ivTag2=findViewById(R.id.ivTag2);
        decorationResource=new ResourceUtils().getResourcesID(R.array.rankEdgePics);

        if(bean!=null)
        {
            if(Strings.isDigitOnly(bean.getGuardLevel()))
            {
                int guardLevel=Integer.valueOf(bean.getGuardLevel());
                UserGuardResourceBean userGuardResourceBean=LocalUserGuardDao.getInstance().getLevel(guardLevel);
                if(userGuardResourceBean!=null && !TextUtils.isEmpty(userGuardResourceBean.getLocalImgSmallPath()))
                {
                    Bitmap bitmap=BitmapFactory.decodeFile(userGuardResourceBean.getLocalImgSmallPath());
                    if(bitmap!=null)
                    {
                        ivTag1.setImageBitmap(bitmap);
                    }
                }
            }

            if(bean.isIsRoomManage())
            {

            }

            if(bean.getVipLevel()>0 && bean.getVipLevel()<decorationResource.length)
            {
                ivEdge.setImageResource(decorationResource[bean.getVipLevel()-1]);
                ivUserImg.setScaleX(scaleAndMargins[bean.getVipLevel()-1][0]);
                ivUserImg.setScaleY(scaleAndMargins[bean.getVipLevel()-1][0]);

                UserTagResourceBean userTagResourceBean=LocalUserTagResourceDao.getInstance().getLevelTag(bean.getVipLevel());
                if(userTagResourceBean!=null && !TextUtils.isEmpty(userTagResourceBean.getLocalMedalUrlPath()))
                {
                    Bitmap bitmap=BitmapFactory.decodeFile(userTagResourceBean.getLocalMedalUrlPath());
                    if(bitmap!=null)
                    {
                        ivTag2.setImageBitmap(bitmap);
                    }
                }
            }

            tvName.setText(bean.getNickname());
            tvBulletMessage.setText(bean.getMsg());
            GlideUtils.loadCircleImage(getContext(),bean.getAvatar(),
                    R.mipmap.user_head_error,R.mipmap.user_head_error,ivUserImg);

        }
    }





}
