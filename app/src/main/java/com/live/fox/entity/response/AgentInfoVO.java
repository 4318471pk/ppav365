package com.live.fox.entity.response;

/*****************************************************************
 * Created  on 2020\1\7 0007 下午
 * desciption:
 *****************************************************************/
public class AgentInfoVO {
    private String agentName;
    private String qq;
    private String wechat;
    private String balance;

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getAgentName() {
        return agentName;
    }

    public void setAgentName(String agentName) {
        this.agentName = agentName;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public String getWechat() {
        return wechat;
    }

    public void setWechat(String wechat) {
        this.wechat = wechat;
    }

}
