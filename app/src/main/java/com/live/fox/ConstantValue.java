package com.live.fox;

public interface ConstantValue {

    String SwitchStatus="SwitchStatus";
    String GesturePassword="GesturePassword";


    /**
     * activity Request
     */
    public static final int REQUEST_CODE1 = 10001;
    public static final int REQUEST_CODE2 = 10002;
    public static int REQUEST_CODE3 = 10003;
    public static int REQUEST_CODE4 = 10004;
    public static int REQUEST_CROP_PIC=10005;

    /**
     * activity 结果
     */
    int RESULT_CODE1 = 20001>>1;
    int RESULT_CODE2 = 20002>>2;
    int RESULT_CODE3 = 20003>>3;
    int RESULT_CODE4 = 20004>>4;
    int GUEST_BINDPHONE = 20005;
    int RESULT_CROP_PIC=20006;

    public static String hasGuestLogin = "hasGuestLogin";
}
