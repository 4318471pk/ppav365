package com.live.fox.entity.response;

/*****************************************************************
 * Created  on 2020\1\16 0016 下午
 * desciption:
 *****************************************************************/
public class GamePeriodInfoVO {


    /**
     * startIssue : 1440
     * startTime : 00:00
     * endTime : 24:00
     * timelong : 1
     * name : txssc
     * delay : 10
     * down_time : 52
     * expect : 202001190001
     * sort_expect : 0001
     * lastIssue : 202001181440
     */

    private String startIssue;
    private String startTime;
    private String endTime;
    private long timelong;
    private String name;
    private String delay;
    private int down_time;
    private String expect;
    private String sort_expect;
    private String nickname;
    private long lastIssue;

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getStartIssue() {
        return startIssue;
    }

    public void setStartIssue(String startIssue) {
        this.startIssue = startIssue;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public long getTimelong() {
        return timelong;
    }

    public void setTimelong(long timelong) {
        this.timelong = timelong;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDelay() {
        return delay;
    }

    public void setDelay(String delay) {
        this.delay = delay;
    }

    public int getDown_time() {
        return down_time;
    }

    public void setDown_time(int down_time) {
        this.down_time = down_time;
    }

    public String getExpect() {
        return expect;
    }

    public void setExpect(String expect) {
        this.expect = expect;
    }

    public String getSort_expect() {
        return sort_expect;
    }

    public void setSort_expect(String sort_expect) {
        this.sort_expect = sort_expect;
    }

    public long getLastIssue() {
        return lastIssue;
    }

    public void setLastIssue(long lastIssue) {
        this.lastIssue = lastIssue;
    }
}

