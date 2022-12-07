package com.live.fox.utils;

import android.content.Context;
import android.content.res.TypedArray;


/**
 * @author cheng
 * @date 2019/1/07

 getResourcesIdByIndex : 根据index批量获取资源id
 */
public class ResourceUtils {

    /**
     * 根据index批量获取资源id
     * 1.在arrays.xml中增加对应图片
     * <integer-array name="luckNum">
        <item>@drawable/luck_0</item>
        <item>@drawable/luck_1</item>
        <item>@drawable/luck_2</item>
        <item>@drawable/luck_3</item>
        <item>@drawable/luck_4</item>
        <item>@drawable/luck_5</item>
        <item>@drawable/luck_6</item>
        <item>@drawable/luck_7</item>
        <item>@drawable/luck_8</item>
        <item>@drawable/luck_9</item>
        </integer-array>

       2.调用时传入array
         new ResourceUtils().getResourcesIdByIndex(R.array.luckNum, new int[1,0,2,4,0]);
     */
    public int[] getResourcesIdByIndex(int arrayId, int[] index) {
        int[] resources = new int[index.length];
        TypedArray typedArray = Utils.getApp().getResources().obtainTypedArray(arrayId);
        for (int i = 0; i < index.length; i++) {
            if (i >= typedArray.length()) {
                resources[i] = typedArray.getResourceId(typedArray.length() - 1, 1);
            }else {
                resources[i] = typedArray.getResourceId(index[i], 1);
            }
        }
        return resources;
    }

    public int[] getResourcesID(int arrayId) {

        TypedArray typedArray = Utils.getApp().getResources().obtainTypedArray(arrayId);
        int[] resources = new int[typedArray.length()];
        for (int i = 0; i < resources.length; i++) {
            resources[i] = typedArray.getResourceId(i, 1);
        }
        return resources;
    }

    private static ResourceUtils resourceUtils = new ResourceUtils();

    public static ResourceUtils getInstance() {
        return resourceUtils;
    }

    public String getString(int resourceId) {
        Context context = Utils.getApp().getBaseContext();
        String message = "";
        if(context != null) {
            message = context.getResources().getString(resourceId);
        }
        return message;
    }

}
