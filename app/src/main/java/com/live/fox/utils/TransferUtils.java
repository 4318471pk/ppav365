package com.live.fox.utils;

import android.content.Context;

import com.live.fox.R;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class TransferUtils {

    public static String getTransferStr(Context context,long num) {
        if (num > 1000000000) {
            return new BigDecimal(num).divide(new BigDecimal(1000000000)).setScale(2, RoundingMode.DOWN).toPlainString() +"B";
        } else if (num > 1000000) {
            return new BigDecimal(num).divide(new BigDecimal(1000000)).setScale(2, RoundingMode.DOWN).toPlainString() + context.getString(R.string.baiwan);
        } else if (num > 1000) {
            return new BigDecimal(num).divide(new BigDecimal(1000)).setScale(2, RoundingMode.DOWN).toPlainString() + "K";
        } else {
            return new BigDecimal(num).toPlainString();
        }
    }


//    public static SpannableStringBuilder getCardNameStr(User user, boolean isAnchor) {
//        SpanUtils spanUtils = new SpanUtils();
//        spanUtils.append(user.getNickname() + " ").setForegroundColor(Utils.getApp().getResources().getColor(R.color.white));
//        spanUtils.appendImage(user.getSex() == 1 ? R.mipmap.ic_menu_man : R.mipmap.ic_menu_woman, SpanUtils.ALIGN_CENTER);
//        spanUtils.append(" ");
//        spanUtils.appendImage(ImageUtils.scale(ImageUtils.getBitmap(getUserLevel(user.getUserLevel())), 100, 50), SpanUtils.ALIGN_CENTER);
//        spanUtils.append(" ");
//
//        if (user.getBadgeList().size() > 0) {
//            for (int i = 0; i < user.getBadgeList().size(); i++) {
//                String badge_id = user.getBadgeList().get(i).toString();
//                LogUtils.e("getUserInfoData json : badge_id " + badge_id);
//                if(BadgesManager.ins().getCoverByGid(badge_id)!=null) {
//                    spanUtils.appendImage(ImageUtils.scale(BadgesManager.ins().getCoverByGid(badge_id), 50, 50), SpanUtils.ALIGN_CENTER);
//                }
//
//            }
//        }
//
//        return spanUtils.create();
//    }


    /**
     * 获取对应用户等级的资源id
     */
//    public static int getUserLevel(int level) {
//        TypedArray userLevel = null;
//        if (userLevel == null) {
//            userLevel = Utils.getApp().getResources().obtainTypedArray(R.array.level);
//        }
//        if (level >= userLevel.length()) {
//            return userLevel.getResourceId(userLevel.length() - 1, 1);
//        }
//        return userLevel.getResourceId(level, 1);
//    }

    public static String transferDynamicCount(int num) {
        if (num > 100000) {
            return "10W+";
        } else if (num > 10000) {
            return new BigDecimal(num).divide(new BigDecimal(10000)).setScale(0, RoundingMode.DOWN).toPlainString() + "W";
        } else {
            return String.valueOf(num);
        }
    }

    //0无标签1子爵2伯爵3公爵4国王5皇帝6豪气7跳蛋
    public static String getTag(Context context,String tag) {
        String result = "";
        switch (tag) {
            case "1":
                result =context.getString(R.string.grade_gold);
                break;
            case "2":
                result =context.getString(R.string.grade_platinum);
                break;
            case "3":
                result = context.getString(R.string.grade_diamond);
                break;
            case "4":
                result = context.getString(R.string.grade_master);
                break;
            case "5":
                result = context.getString(R.string.grade_king);
                break;
            case "6":
                result =context.getString(R.string.haoqi);
                break;
            case "7":
                result = context.getString(R.string.skipping);
                break;
            case "8":
                result = context.getString(R.string.renqi);
                break;
            default:
                result = "";
                break;

        }

        return result;
    }

    public static String getTimePoor(Context context,long time) {
        if (time < 3600 * 1000) {
            return "1"+context.getString(R.string.xiaoshi);
        } else if (time < 24 * 3600 * 1000) {
            if (time % (3600 * 1000) != 0) {
                return (time / (3600 * 1000) + 1) + context.getString(R.string.xiaoshi);
            } else {
                return time / (3600 * 1000) + context.getString(R.string.xiaoshi);
            }
        } else {
            if (time % (24 * 3600 * 1000) != 0) {
                return (time / (24 * 3600 * 1000) + 1) + context.getString(R.string.tian);
            } else {
                return time / (24 * 3600 * 1000) + context.getString(R.string.tian);
            }
        }
    }
}
