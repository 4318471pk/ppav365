package com.live.fox.entity.response;

/*****************************************************************
 * Created  on 2020\1\18 0018 下午
 * desciption:
 *****************************************************************/
public class CpGameResultInfoVO {

    /**
     * code : 08,07,09,05,10,04,02,01,03,06
     * expect : 20200116178
     * create_time : 2020-01-17 03:54:02
     */

    private String code;
    private String expect;
    private Integer multiple;
    private String create_time;
    private boolean isLHC=false;

    public boolean isLHC() {
        return isLHC;
    }

    public void setLHC(boolean LHC) {
        isLHC = LHC;
    }

    public Integer getMultiple() {
        return multiple;
    }

    public void setMultiple(Integer multiple) {
        this.multiple = multiple;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getExpect() {
        return expect;
    }

    public void setExpect(String expect) {
        this.expect = expect;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }
}
