package com.live.fox.manager;

//缓存数据中心  用户详情专用
public class DataCenter2 {

    private static DataCenter2 dataCenter;
    private UserInfo userInfo;

    public static synchronized DataCenter2 getInstance()
    {
        if(dataCenter==null)
        {
            dataCenter=new DataCenter2();
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
