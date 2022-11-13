package com.live.fox.manager;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.live.fox.entity.User;

public class UserInfo {

    User user;
    String token;
    public UserInfo() {
        user=getUser();
    }

    public String getToken() {
        if(TextUtils.isEmpty(token))
        {
            token=SPManager.getToken();
        }
        return token;
    }

    public void setToken(String token) {
        this.token = token;
        SPManager.saveToken(token);
    }

    public synchronized void setUser(User user)
    {
        if(user!=null)
        {
            this.user=user;
            SPManager.saveUserInfo(user);
        }

    }

    public synchronized void setUser(String userStr)
    {
        if(!TextUtils.isEmpty(userStr))
        {
            this.user=new Gson().fromJson(userStr,User.class);
            SPManager.saveUserInfo(userStr);
        }
    }

    public synchronized User getUser()
    {
        if(user==null || user.getUid()==null)
        {
            user=SPManager.getUserInfo();
            if(user==null || user.getUid()==null)
            {
                user=new User();
            }
        }

        return user;
    }

    public boolean isLogin()
    {
        User user= getUser();
        return user!=null && user.getUid()!=null;
    }

    public synchronized void updateUser(User mUser)
    {
        if(mUser==null)return;

        if(mUser.getAnchorCoin()!=null)
        {
            user.setAnchorCoin(mUser.getAnchorCoin());
        }

        if(mUser.getGold()!=null)
        {
            user.setGold(mUser.getGold());
        }

        if(mUser.getDiamond()!=null)
        {
            user.setDiamond(mUser.getDiamond());
        }

        if(mUser.getAuth()!=null)
        {
            user.setAuth(mUser.getAuth());
        }

        if(mUser.getSignature()!=null)
        {
            user.setSignature(mUser.getSignature());
        }

        if(mUser.getSex()!=null)
        {
            user.setSex(mUser.getSex());
        }

        if(mUser.getNickname()!=null)
        {
            user.setNickname(mUser.getNickname());
        }

        if(mUser.getAutoUpdownBalance()!=null)
        {
            user.setAutoUpdownBalance(mUser.getAutoUpdownBalance());
        }

        if(mUser.getHasPayPwd()!=null)
        {
            user.setHasPayPwd(mUser.getHasPayPwd());
        }

        setUser(user);
    }

    public void loginOut()
    {
        user=new User();
        token="";
    }
}
