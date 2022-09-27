package com.live.fox.manager;

//缓存数据中心
public class DataCenter {

    private static DataCenter dataCenter;
    private UserInfo userInfo;

    public static synchronized DataCenter getInstance()
    {
        if(dataCenter==null)
        {
            dataCenter=new DataCenter();
        }
        return dataCenter;
    }

    public synchronized UserInfo getUserInfo() {
        if(userInfo==null)
        {
            userInfo=new UserInfo();
        }
        return userInfo;
    }


}
