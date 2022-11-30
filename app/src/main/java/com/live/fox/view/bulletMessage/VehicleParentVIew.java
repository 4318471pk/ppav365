package com.live.fox.view.bulletMessage;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.live.fox.entity.LivingEnterLivingRoomBean;
import com.live.fox.utils.BulletViewUtils;

import java.util.LinkedList;

//座驾飘房父View
public class VehicleParentVIew extends RelativeLayout {

    LinkedList<LivingEnterLivingRoomBean> list=new LinkedList<>();

    public VehicleParentVIew(Context context) {
        super(context);
    }

    public VehicleParentVIew(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public VehicleParentVIew(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void send(LivingEnterLivingRoomBean bean, Activity activity)
    {
        VehicleFloatingView vehicleFloatingView =new VehicleFloatingView(activity,bean);
        vehicleFloatingView.setTag(bean);
        vehicleFloatingView.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));
        addView(vehicleFloatingView);
        BulletViewUtils.goRightToLeftStopThenDisappear(vehicleFloatingView, activity, new BulletViewUtils.OnFinishAniListener() {
            @Override
            public void onFinish(Object obj) {
                if(obj instanceof LivingEnterLivingRoomBean)
                {
                    list.remove(obj);
                    if(list.size()>0)
                    {
                        send(list.get(0),activity);
                    }

                }
            }
        });
    }

    public void postEnterRoomMessage(LivingEnterLivingRoomBean bean, Activity activity)
    {
        if(activity==null || activity.isDestroyed() || activity.isFinishing())
        {
            return;
        }

        list.add(bean);
        if(getChildCount()>0)
        {
            return;
        }
        else
        {
            send(bean,activity);
        }


    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return false;
    }

}
