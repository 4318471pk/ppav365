package com.live.fox.view.bulletMessage;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.live.fox.entity.LivingEnterLivingRoomBean;
import com.live.fox.entity.PersonalLivingMessageBean;
import com.live.fox.utils.BulletViewUtils;

import java.util.LinkedList;

public class EnterRoomMessageParentView extends RelativeLayout {

    LinkedList<LivingEnterLivingRoomBean> list=new LinkedList<>();

    public EnterRoomMessageParentView(Context context) {
        super(context);
        initView();
    }

    public EnterRoomMessageParentView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public EnterRoomMessageParentView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView()
    {

    }

    private void send(LivingEnterLivingRoomBean bean, Activity activity)
    {
        EnterRoomMessageView enterRoomMessageView =new EnterRoomMessageView(activity,bean);
        enterRoomMessageView.setTag(bean);
        enterRoomMessageView.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));
        addView(enterRoomMessageView);
        BulletViewUtils.goRightToLeftStopThenDisappear(enterRoomMessageView, activity, new BulletViewUtils.OnFinishAniListener() {
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
        if(!bean.isGuard() && bean.getVipLevel()<1)
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
}
