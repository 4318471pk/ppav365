package com.live.fox.view.bulletMessage;

import android.app.Activity;
import android.content.Context;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.Transformation;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.google.gson.Gson;
import com.live.fox.entity.PersonalLivingMessageBean;
import com.live.fox.utils.BulletViewUtils;
import com.live.fox.utils.ScreenUtils;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class BulletMessageParentView extends LinearLayout {

    private int location[] = new int[2];
    int screenWidth;
    LinkedList<PersonalLivingMessageBean> list=new LinkedList<>();

    public BulletMessageParentView(Context context) {
        super(context);
        initView();
    }

    public BulletMessageParentView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public BulletMessageParentView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView()
    {
        screenWidth= ScreenUtils.getScreenWidth(getContext());
    }

    public void postBulletMessage(PersonalLivingMessageBean messageBean, Activity activity)
    {
        messageBean.setMoving(false);
        list.add(messageBean);
        send(messageBean,activity);
    }

    private void send(PersonalLivingMessageBean messageBean, Activity activity)
    {
        List<Integer> available=new ArrayList<>();
        for (int i = 0; i <getChildCount() ; i++) {
            RelativeLayout relativeLayout=(RelativeLayout) getChildAt(i);
            if(isAvailablePost(relativeLayout))
            {
                available.add(i);
            }
        }

        if(available.size()==0)
        {
            return;
        }
        if(available.size()==1)
        {
            RelativeLayout relativeLayout=(RelativeLayout) getChildAt(available.get(0));
            newMessageView(relativeLayout,messageBean,activity);

        }
        else
        {
            int index=new Random().nextInt(available.size());
            RelativeLayout relativeLayout=(RelativeLayout) getChildAt(available.get(index));
            newMessageView(relativeLayout,messageBean,activity);
        }
    }


    private boolean isAvailablePost(RelativeLayout relativeLayout)
    {
        if(relativeLayout.getChildCount()==0)
        {
            return true;
        }

        BulletMessageView bulletMessageView=(BulletMessageView)relativeLayout.getChildAt(relativeLayout.getChildCount()-1);

        Transformation transformation = new Transformation();
        if(transformation==null)
        {
            return false;
        }
        bulletMessageView.getAnimation().getTransformation(AnimationUtils.currentAnimationTimeMillis(),transformation);
        Matrix matrix = transformation.getMatrix();
        float[] matrixVals = new float[9];
        matrix.getValues(matrixVals);
        float left=matrixVals[2];
        if(left==0)
        {
            left=screenWidth;
        }
        boolean isAvailable=screenWidth-left-bulletMessageView.getWidth()>100;
        return isAvailable;
    }

    private void newMessageView(RelativeLayout relativeLayout,PersonalLivingMessageBean messageBean,Activity activity)
    {
        messageBean.setMoving(true);
        BulletMessageView bulletMessageView=new BulletMessageView(getContext(),messageBean);
        LinearLayout.LayoutParams ll=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        bulletMessageView.setLayoutParams(ll);
        bulletMessageView.setVisibility(View.GONE);
        bulletMessageView.setTag(messageBean);
        relativeLayout.addView(bulletMessageView);
        BulletViewUtils.goRightToLeftDisappear(bulletMessageView, activity, new BulletViewUtils.OnFinishAniListener() {
            @Override
            public void onFinish(Object obj) {
                if(obj instanceof PersonalLivingMessageBean)
                {
                    list.remove(obj);
                    for (int i = 0; i <list.size() ; i++) {
                        if(!list.get(i).isMoving())
                        {
                            send(list.get(i),activity);
                        }
                    }

                }
            }
        });

    }
}
