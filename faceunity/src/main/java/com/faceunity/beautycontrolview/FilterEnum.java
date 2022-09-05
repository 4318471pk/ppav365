package com.faceunity.beautycontrolview;


import com.faceunity.beautycontrolview.entity.Filter;

import java.util.ArrayList;

/**
 * Created by tujh on 2018/1/30.
 */

public enum FilterEnum {

    nature("origin", R.drawable.nature, "Origin", Filter.FILTER_TYPE_FILTER),
    delta("delta", R.drawable.delta, "Delta", Filter.FILTER_TYPE_FILTER),
    electric("electric", R.drawable.electric, "Electric", Filter.FILTER_TYPE_FILTER),
    slowlived("slowlived", R.drawable.slowlived, "Slowlived", Filter.FILTER_TYPE_FILTER),
    tokyo("tokyo", R.drawable.tokyo, "Tokyo", Filter.FILTER_TYPE_FILTER),
    warm("warm", R.drawable.warm, "Warm", Filter.FILTER_TYPE_FILTER),

    ziran("ziran", R.drawable.origin, "Natural", Filter.FILTER_TYPE_BEAUTY_FILTER),
    danya("danya", R.drawable.qingxin, "Elegant", Filter.FILTER_TYPE_BEAUTY_FILTER),
    fennen("fennen", R.drawable.shaonv, "Matte", Filter.FILTER_TYPE_BEAUTY_FILTER),
    qingxin("qingxin", R.drawable.ziran, "Refreshing", Filter.FILTER_TYPE_BEAUTY_FILTER),
    hongrun("hongrun", R.drawable.hongrun, "Rosy", Filter.FILTER_TYPE_BEAUTY_FILTER);

    private String filterName;
    private int resId;
    private String description;
    private int filterType;

    FilterEnum(String name, int resId, String description, int filterType) {
        this.filterName = name;
        this.resId = resId;
        this.description = description;
        this.filterType = filterType;
    }

    public String filterName() {
        return filterName;
    }

    public int resId() {
        return resId;
    }

    public String description() {
        return description;
    }

    public Filter filter() {
        return new Filter(filterName, resId, description, filterType);
    }

    public static ArrayList<Filter> getFiltersByFilterType(int filterType) {
        ArrayList<Filter> filters = new ArrayList<>();
        for (FilterEnum f : FilterEnum.values()) {
            if (f.filterType == filterType) {
                filters.add(f.filter());
            }
        }
        return filters;
    }
}
