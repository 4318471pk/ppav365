package com.live.fox.view.bulletMessage;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.live.fox.MessageProtocol;
import com.live.fox.entity.LivingEnterLivingRoomBean;
import com.live.fox.entity.NewBornNobleOrGuardMessageBean;
import com.live.fox.utils.BulletViewUtils;

import java.util.LinkedList;

public class NewBornMessageParentView extends RelativeLayout {

    LinkedList<NewBornNobleOrGuardMessageBean> list=new LinkedList<>();

    public NewBornMessageParentView(Context context) {
        super(context);
    }

    public NewBornMessageParentView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NewBornMessageParentView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void postNewMessage(NewBornNobleOrGuardMessageBean bean,Activity activity)
    {
        if(bean==null || TextUtils.isEmpty(bean.getProtocol()) || !bean.getProtocol().equals(MessageProtocol.LIVE_BUY_VIP))
        {
            return;
        }

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

    private void send(NewBornNobleOrGuardMessageBean bean, Activity activity)
    {
        NewBornMessageView newBornMessageView =new NewBornMessageView(activity,bean);
        newBornMessageView.setTag(bean);
        newBornMessageView.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));
        addView(newBornMessageView);
        BulletViewUtils.goRightToLeftStopThenDisappear(newBornMessageView,1000,4000, activity, new BulletViewUtils.OnFinishAniListener() {
            @Override
            public void onFinish(Object obj) {
                if(obj instanceof NewBornNobleOrGuardMessageBean)
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
}
