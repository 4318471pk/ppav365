package com.live.fox.utils;

import com.live.fox.base.BaseActivity;

import java.util.LinkedList;

public class ActivityManager {

    LinkedList<BaseActivity> baseActivities=new LinkedList<>();
    static ActivityManager activityManager;

    public static ActivityManager getInstance()
    {
        if(activityManager==null)
        {
            activityManager=new ActivityManager();
        }
        return activityManager;
    }

    public void finishAllActivity()
    {
        for (int i = baseActivities.size()-1; i > -1; i--) {
            if(baseActivities.get(i)!=null && !baseActivities.get(i).isFinishing() && !baseActivities.get(i).isDestroyed())
            {
                baseActivities.get(i).finish();
            }
        }
        baseActivities.clear();
    }

    public boolean finishToActivity(Class cls)
    {
        boolean isContains=false;
        LinkedList<BaseActivity> temp=new LinkedList<>();
        for (int i = baseActivities.size()-1; i > -1; i--) {
            if(baseActivities.get(i).getClass().getName().equals(cls.getName()))
            {
                isContains=true;
                break;
            }
            else
            {
                temp.add(baseActivities.get(i));
            }
        }

        if(isContains)
        {
            for (int i = 0; i < temp.size(); i++) {
                temp.get(i).finish();
            }
        }
        return isContains;
    }

    public void onStart(BaseActivity baseActivity)
    {
        baseActivities.add(baseActivity);
    }

    public void onDestroy(BaseActivity baseActivity)
    {
        baseActivities.remove(baseActivity);
    }
}
