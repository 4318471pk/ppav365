package com.live.fox.utils;


import static com.live.fox.entity.MessageEvent.APPEND_BET_TYPE;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.text.Layout;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.style.ClickableSpan;
import android.view.View;

import com.google.gson.Gson;
import com.live.fox.Constant;
import com.live.fox.MessageProtocol;
import com.live.fox.R;
import com.live.fox.db.LocalGiftDao;
import com.live.fox.db.LocalUserGuardDao;
import com.live.fox.db.LocalUserLevelDao;
import com.live.fox.db.LocalUserTagResourceDao;
import com.live.fox.entity.ChatEntity;
import com.live.fox.entity.FunctionItem;
import com.live.fox.entity.Gift;
import com.live.fox.entity.GiftResourceBean;
import com.live.fox.entity.LivingFollowMessage;
import com.live.fox.entity.LivingEnterLivingRoomBean;
import com.live.fox.entity.LivingMessageGiftBean;
import com.live.fox.entity.MessageEvent;
import com.live.fox.entity.NewBornNobleOrGuardMessageBean;
import com.live.fox.entity.PersonalLivingMessageBean;
import com.live.fox.entity.ReceiveGiftBean;
import com.live.fox.entity.User;
import com.live.fox.entity.UserGuardResourceBean;
import com.live.fox.entity.UserTagResourceBean;
import com.live.fox.entity.response.LotteryItem;
import com.live.fox.entity.response.MinuteTabItem;
import com.live.fox.manager.DataCenter;
import com.live.fox.ui.mine.noble.NobleFragment;
import com.live.fox.utils.device.DeviceUtils;
import com.live.fox.utils.device.ScreenUtils;
import com.live.fox.view.LivingClickTextSpan;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ChatSpanUtils {

    private ChatSpanUtils() {
    }

    private static class InstanceHolder {
        private static final ChatSpanUtils instance = new ChatSpanUtils();
    }

    public static ChatSpanUtils ins() {
        return InstanceHolder.instance;
    }

    private OnUserClickListener onUserClickListener;

    private static final String SYSTEM_COLOR = "#FFD700";//??????????????????  ?????? #EF3272
    private static final String HINT_COLOR = "#ffffff";//???????????? @????????????????????????????????????????????????????????? ?????? #fdd443 #FAE86B
    private static final String CONTENT_COLOR = "#ffffff";//???????????? ?????????????????????  ??????
    private static final String NICKNAME_COLOR = "#99ffffff";//???????????? ?????? #b9b9b9

    public enum ContentType {
        System,
        Hint,
        Normal,
        NOBLE
    }

    private static final int placeholder = 0x110;

    //??????????????????????????????  ?????????????????????????????????null
    public ChatEntity getChatEntity(Context context, JSONObject jsonObject, long anchorId,
                                    int liveId, int adapterPos, long lastFollowUserId) {
        ChatEntity chatEntity = null;
        if (jsonObject == null) return null;
        User user = new Gson().fromJson(jsonObject.toString(), User.class);
        boolean isShow = false;
        int chatHide = jsonObject.optInt("chatHide");

        SpanUtils spanUtils = new SpanUtils();

        switch (jsonObject.optInt("protocol")) {

            case Constant.MessageProtocol.PROTOCOL_SYSTEM:
                isShow = true;
                appendText(spanUtils, jsonObject.optString("content"), ContentType.System,
                        false, null);
                break;

            case Constant.MessageProtocol.PROTOCOL_ANCHOR_SWITCH: //3 ???????????????????????????
                isShow = true;
                appendText(spanUtils, jsonObject.optBoolean("leave") ? context.getString(R.string.anchorleft) : context.getString(R.string.anchorCome),
                        ContentType.Hint, false, null);
                break;

            case Constant.MessageProtocol.PROTOCOL_FOCUS:  //4 ??????
                if (lastFollowUserId != user.getUid()) {
                    isShow = true;
                }
                appendLevel(spanUtils, user.getUserLevel(), context);
                appendBadges(context, spanUtils, jsonObject, chatHide);
                appendText(spanUtils, chatHide == 0 ? user.getNickname() : context.getString(R.string.mysteriousMan),
                        getNameColor(user.getUserLevel()), true);
                appendText(spanUtils, context.getString(R.string.focusAnchor), ContentType.Hint, false, null);
                break;

            case Constant.MessageProtocol.PROTOCOL_AUDIENCE: //5 ?????????????????????
                if (jsonObject.optBoolean("isInter")) {
                    isShow = true;
                }

                //?????????????????????????????????
                if (jsonObject.optInt("isRoomPreview") == 1) {
                    return null;
                }

                user.setCome(true);
                appendLevel(spanUtils, user.getUserLevel(), context);
                appendBadges(context, spanUtils, jsonObject, chatHide);
                if (user.isVip()) {
                    String welCome = String.format(context.getString(R.string.welcomeVIP), user.getNickname());
                    appendText(spanUtils, chatHide == 0 ? user.getNickname() : context.getString(R.string.mysteriousMan),
                            ContentType.Hint, true, null);
                    appendText(spanUtils, welCome, ContentType.Hint, false, null);
                } else {
                    appendText(spanUtils, chatHide == 0 ? user.getNickname() : context.getString(R.string.mysteriousMan),
                            getNameColor(user.getUserLevel()), true);
                }

                String welCome;
                if (user.getUserLevel() > 10) {
                    welCome = String.format(context.getString(R.string.welcomeVIP), user.getNickname());
                } else {
                    welCome = context.getString(R.string.comeWelcome);
                }
                appendText(spanUtils, welCome, ContentType.Hint, false, null);
                break;

            case Constant.MessageProtocol.PROTOCOL_LEVEL_UP: // 6 ??????????????? ??????
                int levelType = jsonObject.optInt("levelType");
                if (levelType == 1) {
                    isShow = true;
                }
                appendText(spanUtils, context.getString(R.string.congraAnchor) + jsonObject.optInt("level"),
                        ContentType.Hint, false, null);
                break;

            case Constant.MessageProtocol.PROTOCOL_RECEIVE_GIFT: // 7 ????????????
                ReceiveGiftBean giftBean = new Gson().fromJson(jsonObject.toString(), ReceiveGiftBean.class);
                int luck = jsonObject.optInt("luck");
                if (giftBean.getGift() == null) {
                    return null;
                }
                //??????????????????????????????500?????????????????????
                if (giftBean.getGift().getPlayType() == 0 && luck < 500) {
                    return null;
                }
                if (giftBean.getGift().getPlayType() != 0 && liveId != jsonObject.optLong("liveId")) {
                    return null;
                }
                isShow = true;

                Gift gift = giftBean.getGift();
                LogUtils.e(gift.getCover());
                appendMessageType(spanUtils, "", context);
                appendBadges(context, spanUtils, jsonObject, chatHide);
                appendText(spanUtils, giftBean.chatHide == 0 ? user.getNickname() : context.getString(R.string.mysteriousMan), ContentType.System, true, null);
                appendText(spanUtils, context.getString(R.string.sended) + gift.getGname(),
                        ContentType.System, true, null);
                if (giftBean.getCount() > 1) {
                    appendText(spanUtils, "x" + giftBean.getCount(), ContentType.Hint, false, null);
                }
                break;

            case Constant.MessageProtocol.PROTOCOL_SILENT: // 8 ???????????????????????????
                boolean isBlack = jsonObject.optBoolean("isBlack");
                String uid = jsonObject.optString("uid");
                if ((DataCenter.getInstance().getUserInfo().getUser().getUid() + "").equals(uid)) {
                    isShow = true;
                }
                appendText(spanUtils, isBlack ? context.getString(R.string.niAnchorSilence) : context.getString(R.string.voiceOpen),
                        ContentType.Hint, false, null);
                break;

            case Constant.MessageProtocol.PROTOCOL_CHAT: //9 ???????????????
                if (chatHide == 1) {
                    user.setNickname(context.getString(R.string.mysteriousMan));
                }
                int type = jsonObject.optInt("type");
                //????????????????????????????????????
                if (type == 3 && liveId != jsonObject.optLong("liveId")) {
                    return null;
                }
                isShow = true;


                //?????????
                appendLevel(spanUtils, user.getUserLevel(), context);
                FunctionItem mShit = appendBadges(context, spanUtils, jsonObject, chatHide);
                if (user.isVip()) {
                    appendText(spanUtils, chatHide == 0 ? user.getNickname() : context.getString(R.string.mysteriousMan),
                            mShit == null ? ContentType.Hint : ContentType.NOBLE, true, mShit);
                } else {
                    appendText(spanUtils, chatHide == 0 ? user.getNickname() : context.getString(R.string.mysteriousMan),
                            getNameColor(user.getUserLevel()), true);
                }

                appendText(spanUtils, jsonObject.optString("msg"),
                        mShit == null ? ContentType.Normal : ContentType.NOBLE, false, mShit);

                break;

            case Constant.MessageProtocol.PROTOCOL_LIVE_BROADCAST: //13 ?????????????????????
                isShow = true;
                appendText(spanUtils, context.getString(R.string.message) + jsonObject.optString("content"),
                        ContentType.System, false, null);
                break;

            case Constant.MessageProtocol.PROTOCOL_RoomPay_Change: //24 ????????????????????????
                isShow = true;
                int roomType = jsonObject.optInt("type", 0);
                int roomPrice = jsonObject.optInt("price", 0);
                if (roomType > 0) {
                    String roomMode = roomType == 1 ? context.getString(R.string.jssffj) + roomPrice + context.getString(R.string.goldMinute) : (roomType == 2 ? context.getString(R.string.dcsffj) + roomPrice + context.getString(R.string.goldOnce) : context.getString(R.string.passwordRoomt));
                    appendText(spanUtils, context.getString(R.string.abjjjfjbgw) + roomMode,
                            ContentType.Hint, false, null);
                } else {
                    appendText(spanUtils, context.getString(R.string.zbjjjfjbgwmffj),
                            ContentType.Hint, false, null);
                }
                break;

            case Constant.MessageProtocol.PROTOCOL_LIVE_KICK: //24 ????????????
                isShow = true;
                appendText(spanUtils, jsonObject.optString("nickname") +
                        context.getString(R.string.btcbzbj), ContentType.Hint, false, null);
                break;

            case Constant.MessageProtocol.PROTOCOL_MyCoust:  //99 ??????????????? ?????????????????????
                isShow = true;
                appendText(spanUtils, jsonObject.optString("content"),
                        ContentType.Hint, false, null);
                break;

            case Constant.MessageProtocol.PROTOCOL_CpBets: //26 ??????????????????
                isShow = true;
                //??????????????????????????????????????????
                if (jsonObject.optInt("liveId") != liveId) {
                    return null;
                }
                String nickName = jsonObject.optString("nickName");
                String name = jsonObject.optString("name");
                int totalCoin = jsonObject.optInt("totalCoin");
                String lottryName = jsonObject.optString("code");
                int times = jsonObject.optInt("times");
                List<LotteryItem> payList = GsonUtil.getObjects(String.valueOf(jsonObject.opt("payList")), LotteryItem[].class);
                appendMessageType(spanUtils, "", context);

                String strFormat = String.format(context.getString(R.string.already_bet_format),
                        nickName, name, RegexUtils.westMoney(totalCoin) + context.getString(R.string.gold));
                appendText(spanUtils, strFormat, ContentType.Hint, true, null);
                if (!"yn_hncp".equals(lottryName)) {
                    appendFollowBet(spanUtils, payList, lottryName, times, name, context);
                }
                break;

            case Constant.MessageProtocol.PROTOCOL_CpWin: //27 ??????????????????
                isShow = true;
                //??????????????????????????????????????????
                if (jsonObject.optInt("liveId") != liveId) {
                    return null;
                }
                String nickName27 = jsonObject.optString("nickName");
                String name27 = jsonObject.optString("name");
                double winMoney = jsonObject.optDouble("winMoney");
                appendMessageType(spanUtils, "", context);
                String strFormatLottery = String.format(context.getString(R.string.win_format),
                        nickName27, name27, RegexUtils.westMoney(winMoney) + context.getString(R.string.gold));
                appendText(spanUtils, strFormatLottery, ContentType.Hint, true, null);
                break;

            case Constant.MessageProtocol.PROTOCOL_RoomManager: //28 ????????????
                isShow = true;
                String nickName28 = jsonObject.optString("nickname");
                long uid28 = jsonObject.optLong("uid");
                int type28 = jsonObject.optInt("type");
                if (uid28 == DataCenter.getInstance().getUserInfo().getUser().getUid()) {
                    appendMessageType(spanUtils, "", context);
                    String msg = type28 == 1 ? context.getString(R.string.gxnnbrmwfg) : context.getString(R.string.hyhnbqxlfg);
                    appendText(spanUtils, msg, ContentType.System, true, null);
                } else {
                    appendMessageType(spanUtils, "", context);
                    String msg = type28 == 1 ? context.getString(R.string.congratulation) + nickName28 + context.getString(R.string.brmwfg) : nickName28 + context.getString(R.string.bqxlfg);
                    appendText(spanUtils, msg, ContentType.System, true, null);
                }
                break;
        }

        if (isShow) {
            chatEntity = new ChatEntity(user);
            chatEntity.setValue(jsonObject, anchorId, liveId, adapterPos);
            chatEntity.setVip(jsonObject.optBoolean("isVip", false));
            chatEntity.setContent(spanUtils.create());

        }
        return chatEntity;
    }

    public Spanned getNickNameSpan(User user, Context context) {
        SpanUtils spanUtils = new SpanUtils();
        spanUtils.append(user.getNickname() + " ");
        appendLevel(spanUtils, user.getUserLevel(), context);
        appendSex(spanUtils, user, context);
        appendBadges(spanUtils, user.getBadgeList());
        return spanUtils.create();
    }

    public Spanned getUserInfoSpan(User user, Context context) {
        SpanUtils spanUtils = new SpanUtils();
        appendSex(spanUtils, user, context);
        appendLevel(spanUtils, user.getUserLevel(), context);
        appendBadges(spanUtils, user.getBadgeList());
        return spanUtils.create();
    }

    public Spanned getAnchorNickNameSpan(User user, Context context) {
        SpanUtils spanUtils = new SpanUtils();
        spanUtils.append(user.getNickname() + " ");
        if (user.getBadgeList() != null && user.getBadgeList().contains(2)) {//??????
            appendLevel(spanUtils, user.getUserLevel(), context);
            appendLevel(spanUtils, user.getAnchorLevel(), context);
        } else {
            appendLevel(spanUtils, user.getUserLevel(), context);
        }
        appendSex(spanUtils, user, context);
        appendBadges(spanUtils, user.getBadgeList());
        return spanUtils.create();
    }

    public Spanned getNickNameAndLevel(String name, int level, Context context) {
        SpanUtils spanUtils = new SpanUtils();
        spanUtils.append(name + " ");
        appendLevel(spanUtils, level, context);
        return spanUtils.create();
    }

    public void appendText(SpanUtils spanUtils, String text, ContentType contentType, boolean space, FunctionItem shit) {
    }

    public void appendText(SpanUtils spanUtils, String text, String color, boolean space) {
    }


    public void appendLevel(SpanUtils spanUtils, Integer userLevel, Context context) {
    }

    public void appendSex(SpanUtils spanUtils, User user, Context context) {

    }

    public void appendMessageType(SpanUtils spanUtils, String protocol, Context context) {
    }


    /**
     * ??????????????????
     */
    public static SpanUtils appendSystemMessageType(String protocol, CharSequence message, Context context) {
        SpanUtils spanUtils = new SpanUtils();

        int resourceId = 1;
        switch (protocol) {
            case MessageProtocol.SYSTEM_NOTICE:// ??????
            case MessageProtocol.SYSTEM_ADVERTISE:// ??????
            case MessageProtocol.LIVE_ENTER_OUT_ROOM:
            case MessageProtocol.LIVE_BLACK_CHAT:
            case MessageProtocol.LIVE_ROOM_SET_MANAGER_MSG:
            case MessageProtocol.LIVE_BAN_USER:
            case MessageProtocol.LIVE_FOLLOW:
                resourceId = R.mipmap.icon_tag_sys;
                break;
            case MessageProtocol.GAME_CP_WIN:// ??????
                resourceId = R.mipmap.icon_tag_win;
                break;
        }

        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resourceId);
        if (bitmap == null) return spanUtils;
        int height = ScreenUtils.getDip2px(context, 16);
        int width = ScreenUtils.getDip2px(context, 40);
        spanUtils.appendImage(ImageUtils.scale(bitmap, width, height), SpanUtils.ALIGN_CENTER);//120/68
        spanUtils.append(" ");

        if(!TextUtils.isEmpty(message))
        {
            spanUtils.append(message).setForegroundColor(0xffffffff);
        }
        return spanUtils;
    }

    /**
     * ??????
     *
     * @param spanUtils   SpannableStringBuilder
     * @param payList     ????????????
     * @param lotteryName ?????????
     * @param times       ??????
     * @param name        ?????????
     * @param context     ??????bitmap
     */
    public void appendFollowBet(SpanUtils spanUtils, List<LotteryItem> payList, String lotteryName,
                                int times, String name, Context context) {
        int length1 = spanUtils.getLength();
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.gentou);//132/60
        if (bitmap == null) return;
        spanUtils.appendImage(ImageUtils.scale(bitmap, 120, 60), SpanUtils.ALIGN_CENTER);
        spanUtils.append(" ");
        int length2 = spanUtils.getLength();

        spanUtils.getBuilder().setSpan(new ClickableSpan() {
            @Override
            public void onClick(View view) {
                if (payList == null || payList.size() == 0) return;
                MinuteTabItem.appendBet(payList, lotteryName);
                //???cp  car
            }
        }, length1, length2, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);//- 1
        if (!bitmap.isRecycled()) {
            bitmap.recycle();
        }
    }

    /**
     * ????????????
     *
     * @param context    context
     * @param spanUtils  ??????
     * @param jsonObject json
     * @param chatHide   ????????????
     * @return ??????
     */
    public FunctionItem appendBadges(Context context, SpanUtils spanUtils,
                                     JSONObject jsonObject, int chatHide) {
        JSONArray badgeList1 = jsonObject.optJSONArray("badgeList");
        FunctionItem shit = null;

        if (badgeList1 != null && badgeList1.length() > 0) {
            for (int i = 0; i < badgeList1.length(); i++) {
                try {
                    String mS = badgeList1.get(i).toString();
                    if (("2".equals(mS) && chatHide == 1) || "5".equals(mS) && chatHide == 1)
                        continue;
                    if (shit == null) {
                        shit = getShit(context, Integer.parseInt(mS));
                    }
                    Bitmap bitmap = getBadgeByGid(mS);
                    if (bitmap == null) continue;
                    spanUtils.appendImage(ImageUtils.scale(bitmap,
                            DeviceUtils.dp2px(context, 24),
                            DeviceUtils.dp2px(context, 20)),
                            SpanUtils.ALIGN_CENTER);
                    spanUtils.append(" ");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        return shit;
    }

    public void appendBadges(SpanUtils spanUtils, ArrayList<Integer> badgeList) {
        if (badgeList != null && badgeList.size() > 0) {
            for (int i = 0; i < badgeList.size(); i++) {
                try {
                    Bitmap bitmap = getBadgeByGid(badgeList.get(i).toString());
                    if (bitmap == null) continue;
                    spanUtils.appendImage(ImageUtils.scale(bitmap, 84, 52), SpanUtils.ALIGN_CENTER);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * ????????????
     *
     * @param bid id
     * @return bitmap
     */
    public Bitmap getBadgeByGid(final String bid) {
        String basePath = Constant.DEFAULT_DOWNLOAD_DIR + ".badge/" + bid + ".png";
        LogUtils.e(basePath);
        File tempFile = new File(basePath);
        if (tempFile.exists()) {
            try {
                FileInputStream fis = new FileInputStream(basePath);
                return BitmapFactory.decodeStream(fis);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        } else {
            return null;
        }
    }


    private Bitmap getLevelBitmap(int drawableId, Context context) {
        Bitmap mDefauleBitmap = null;
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), drawableId);
        if (bitmap != null) {
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();
            Matrix matrix = new Matrix();
            matrix.postScale(((float) 30) / width, ((float) 10) / height);
            mDefauleBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
        }
        return mDefauleBitmap;
    }


    private Bitmap getDefaultBitmap(Context context, int drawableId) {
        Bitmap mDefauleBitmap = null;
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), drawableId);
        if (bitmap != null) {
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();
            Matrix matrix = new Matrix();
            matrix.postScale(((float) 50) / width, ((float) 50) / height);
            mDefauleBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
        }
        return mDefauleBitmap;
    }

    public String getNameColor(int level) {
        String color = "#E0AC7F";
        return color.trim();
    }

    public void setOnUserClickListener(OnUserClickListener onUserClickListener) {
        this.onUserClickListener = onUserClickListener;
    }

    public interface OnUserClickListener {
        void onUserClick(long data);
    }

    public class UserClickSpan extends ClickableSpan implements Serializable {
        private User userInfo;
        private int nameColor = Color.parseColor("#FFF08C");

        public UserClickSpan(User userInfo, int nameColor) {
            this.userInfo = userInfo;
            this.nameColor = nameColor;
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            ds.setColor(nameColor);
            ds.setUnderlineText(false);
        }


        @Override
        public void onClick(View widget) {
            LogUtils.e("onClick");
            if (onUserClickListener != null) {
                onUserClickListener.onUserClick(userInfo.getUid());
            }
        }
    }

    private FunctionItem getShit(Context context, int badge) {

        FunctionItem mNobleRes = null;
        if (badge == 6) {
            mNobleRes = NobleFragment.getNobleRes(context, Constant.LEVEL1);
        } else if (badge == 7) {
            mNobleRes = NobleFragment.getNobleRes(context, Constant.LEVEL2);

        } else if (badge == 8) {
            mNobleRes = NobleFragment.getNobleRes(context, Constant.LEVEL3);

        } else if (badge == 9) {
            mNobleRes = NobleFragment.getNobleRes(context, Constant.LEVEL4);

        } else if (badge == 10) {
            mNobleRes = NobleFragment.getNobleRes(context, Constant.LEVEL5);
        }

        return mNobleRes;
    }


    public static boolean appendSexIcon(SpanUtils spanUtils, Integer sex, Context context,int align) {
        if(sex==null)
        {
            return false;
        }
        int sexResId = sex == 1 ? R.mipmap.men : R.mipmap.women;
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), sexResId);
        if (bitmap == null) return false;
        spanUtils.appendImage(ImageUtils.scale(bitmap, 41, 39), align);
        spanUtils.append(" ");
        return true;
    }

    /**
     * ????????????
     */
    public static boolean appendLevelIcon(SpanUtils spanUtils, Integer level, Context context) {
        if (level==null || level < 1) {
            return false;
        }
        String levelIcon = LocalUserLevelDao.getInstance().getLevelIcon(level);
        Bitmap bitmap = BitmapFactory.decodeFile(levelIcon);
        if (bitmap == null) return false;
        int height = ScreenUtils.getDip2px(context, 12);
        int width = ScreenUtils.getDip2px(context, 30);
        spanUtils.appendImage(ImageUtils.scale(bitmap, width, height), SpanUtils.ALIGN_CENTER);//120/68
        return true;
    }

    /**
     * ???????????? ?????? 7??????
     */
    public static boolean appendVipLevelRectangleIcon(SpanUtils spanUtils, Integer level, Context context) {
        if (level==null || level < 1 || level > 7) {
            return false;
        }
        UserTagResourceBean levelTagBean = LocalUserTagResourceDao.getInstance().getLevelTag(level);
        if (levelTagBean != null && !TextUtils.isEmpty(levelTagBean.getLocalVipImgPath())) {
            Bitmap bitmap = BitmapFactory.decodeFile(levelTagBean.getLocalVipImgPath());
            if (bitmap == null) return false;
            int height = ScreenUtils.getDip2px(context, 12);
            int width = ScreenUtils.getDip2px(context, 38.5f);
            spanUtils.appendImage(ImageUtils.scale(bitmap, width, height), SpanUtils.ALIGN_CENTER);//120/68
        }
        return true;
    }

    /**
     * ???????????? ?????? 7??????
     */
    public static void appendVipLevelCircleIcon(SpanUtils spanUtils, int level, Context context) {
        if (level < 1 || level > 7) {
            return;
        }
        UserTagResourceBean levelTagBean = LocalUserTagResourceDao.getInstance().getLevelTag(level);
        if (levelTagBean != null && !TextUtils.isEmpty(levelTagBean.getLocalVipImgPath())) {
            Bitmap bitmap = BitmapFactory.decodeFile(levelTagBean.getLocalVipImgPath());
            if (bitmap == null) return;
            int height = ScreenUtils.getDip2px(context, 12);
            int width = ScreenUtils.getDip2px(context, 30);
            spanUtils.appendImage(ImageUtils.scale(bitmap, width, height), SpanUtils.ALIGN_CENTER);//120/68
        }
    }

    /**
     * ??????????????????????????????????????????
     */
    public static SpanUtils appendNewBornGuard( NewBornNobleOrGuardMessageBean bean, Context context,LivingClickTextSpan.OnClickTextItemListener listener) {
        SpanUtils spanUtils=new SpanUtils();
        if(bean==null || bean.getGuardLevel()<1 || bean.getGuardLevel()>3 || TextUtils.isEmpty(bean.getNickname()))
        {
            return spanUtils;
        }

        String hasOpened=context.getResources().getString(R.string.hasOpened);
        String newBornProtectorTips[]=context.getResources().getStringArray(R.array.newBornProtectorTips);

        Bitmap sysBitmap=BitmapFactory.decodeResource(context.getResources(),R.mipmap.icon_tag_sys);
        if (sysBitmap == null) return spanUtils;
        int height = ScreenUtils.getDip2px(context, 16);
        int width = ScreenUtils.getDip2px(context, 40);
        spanUtils.appendImage(ImageUtils.scale(sysBitmap, width, height), SpanUtils.ALIGN_CENTER);//120/68
        spanUtils.append(" ");

        LivingClickTextSpan livingClickTextSpan = new LivingClickTextSpan(bean, 0xff85EFFF);
        livingClickTextSpan.setOnClickTextItemListener(listener);

        spanUtils.append(bean.getNickname()).setFontSize(13,true);
        int length1 = spanUtils.getLength();
        spanUtils.append(hasOpened).setForegroundColor(0xffffffff).setFontSize(13,true);
        int length2 = spanUtils.getLength();
        UserGuardResourceBean userGuardResourceBean= LocalUserGuardDao.getInstance().getLevel(bean.getGuardLevel());
        if(userGuardResourceBean!=null)
        {
            StringBuilder stringBuilder=new StringBuilder();
            stringBuilder.append("[").append(userGuardResourceBean.getName()).append("]");
            spanUtils.append(stringBuilder.toString()).setFontSize(13,true).setForegroundColor(0xffFFF796);
        }

        spanUtils.append(newBornProtectorTips[bean.getGuardLevel()-1]).setForegroundColor(0xffffffff);

        spanUtils.getBuilder().setSpan(livingClickTextSpan, length1, length2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        return spanUtils;
    }


    /**
     * ??????????????????
     */
    public static boolean appendRoomManageIcon(SpanUtils spanUtils,Boolean isManage, Context context) {
        if(isManage==null || !isManage)
        {
            return false;
        }

        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.icon_admin);
        if (bitmap == null) return false;
        int height = ScreenUtils.getDip2px(context, 16);
        int width = ScreenUtils.getDip2px(context, 16);
        spanUtils.appendImage(ImageUtils.scale(bitmap, width, height), SpanUtils.ALIGN_CENTER);//120/68
        return true;
    }

    /**
     * ???????????? 7??????
     */
    public static boolean appendGuardIcon(SpanUtils spanUtils, Integer level, Context context) {
        if(level==null)
        {
            return false;
        }

        long count = LocalUserGuardDao.getInstance().getCount();
        if (level < 1 || level > count) {
            return false;
        }

        UserGuardResourceBean guardResourceBean = LocalUserGuardDao.getInstance().getLevel(level);
        if (guardResourceBean != null && !TextUtils.isEmpty(guardResourceBean.getLocalImgMediumPath())) {
            Bitmap bitmap = BitmapFactory.decodeFile(guardResourceBean.getLocalImgSmallPath());
            if (bitmap == null) return false;
            int height = ScreenUtils.getDip2px(context, 11.5f);
            int width = ScreenUtils.getDip2px(context, 14);
            spanUtils.appendImage(ImageUtils.scale(bitmap, width, height), SpanUtils.ALIGN_CENTER);//120/68
        }
        return true;
    }

    /**
     * ???????????????????????????
     */
    public static synchronized SpanUtils enterRoom(LivingEnterLivingRoomBean bean, Context context) {
        SpanUtils spanUtils = new SpanUtils();
        if (appendLevelIcon(spanUtils, bean.getUserLevel(), context)) {
            spanUtils.append(" ");
        }

        if (!TextUtils.isEmpty(bean.getNickname())) {
            spanUtils.append(bean.getNickname()).setFontSize(13, true)
                    .setForegroundColor(0xff85EFFF).setAlign(Layout.Alignment.ALIGN_CENTER);
        }

        if (!TextUtils.isEmpty(bean.getMessage())) {
            spanUtils.append(bean.getMessage()).setForegroundColor(0xffffffff);
        }
        return spanUtils;
    }


    /**
     * ??????????????????
     */
    public static void appendPersonalMessage(SpanUtils spanUtils, PersonalLivingMessageBean pBean, Context context, LivingClickTextSpan.OnClickTextItemListener listener) {
        if (pBean == null || TextUtils.isEmpty(pBean.getProtocol())) {
            return;
        }

        switch (pBean.getProtocol()) {
            case MessageProtocol.LIVE_ROOM_CHAT_FLOATING_MESSAGE:
            case MessageProtocol.LIVE_ROOM_CHAT:
                if (appendLevelIcon(spanUtils, pBean.getUserLevel(), context)) {
                    spanUtils.append(" ");
                }

                if (appendVipLevelRectangleIcon(spanUtils, pBean.getVipLevel(), context)) {
                    spanUtils.append(" ");
                }

                if (Strings.isDigitOnly(pBean.getGuardLevel()) && pBean.isIsGuard()) {
                    appendGuardIcon(spanUtils, Integer.valueOf(pBean.getGuardLevel()), context);
                    spanUtils.append(" ");
                }

                if (appendRoomManageIcon(spanUtils,pBean.isIsRoomManage(), context)) {
                    spanUtils.append(" ");
                }


                LivingClickTextSpan livingClickTextSpan = new LivingClickTextSpan(pBean, 0xff85EFFF);
                livingClickTextSpan.setOnClickTextItemListener(listener);
                String nickName = pBean.getNickname() + ": ";
                spanUtils.append(nickName);
                int length1 = spanUtils.getLength();

                spanUtils.append(pBean.getMsg()).setFontSize(13, true)
                        .setForegroundColor(0xffffffff).setAlign(Layout.Alignment.ALIGN_CENTER);
                int length2 = spanUtils.getLength();
                spanUtils.getBuilder().setSpan(livingClickTextSpan, length1, length2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                break;

        }
    }

    /**
     * ??????????????????
     * {"anchorId":1028924365,"avatar":"","combo":1,"count":1,"gid":5,"liveId":100029,"nickname":"lbMOLjbzsb","protocol":"2008","rq":39102,"timestamp":1668156260903,"tipType":0,"uid":1028924366,"userLevel":1,"zb":39102}
     */
    public static void appendPersonalSendGiftMessage(SpanUtils spanUtils, LivingMessageGiftBean gBean, Context context, LivingClickTextSpan.OnClickTextItemListener listener) {
        if (gBean == null || TextUtils.isEmpty(gBean.getProtocol())) {
            return;
        }

        switch (gBean.getProtocol()) {
            case MessageProtocol.LIVE_SEND_GIFT:

                GiftResourceBean giftResourceBean = LocalGiftDao.getInstance().getGift(gBean.getGid());
                if (giftResourceBean != null) {
                    LivingClickTextSpan livingClickTextSpan = new LivingClickTextSpan(gBean, 0xff85EFFF);
                    livingClickTextSpan.setOnClickTextItemListener(listener);

                    if (appendLevelIcon(spanUtils, gBean.getUserLevel(), context)) {
                        spanUtils.append(" ");
                    }

                    if (appendVipLevelRectangleIcon(spanUtils, gBean.getVipLevel(), context)) {
                        spanUtils.append(" ");
                    }

                    if (Strings.isDigitOnly(gBean.getGuardLevel()) && gBean.isIsGuard()) {
                        appendGuardIcon(spanUtils, Integer.valueOf(gBean.getGuardLevel()), context);
                        spanUtils.append(" ");
                    }

                    if (gBean.isIsRoomManage()) {
                        appendGuardIcon(spanUtils, Integer.valueOf(gBean.getGuardLevel()), context);
                        spanUtils.append(" ");
                    }

                    spanUtils.append(gBean.getNickname() + ": ").setFontSize(13, true)
                            .setForegroundColor(0xff85EFFF).setAlign(Layout.Alignment.ALIGN_CENTER);
                    int length1 = spanUtils.getLength();

                    StringBuilder sb = new StringBuilder();
                    sb.append(context.getResources().getString(R.string.hasSent));
                    sb.append("[");

                    spanUtils.append(sb.toString()).setFontSize(13, true)
                            .setForegroundColor(0xffffffff).setAlign(Layout.Alignment.ALIGN_CENTER);

                    int length2 = spanUtils.getLength();
                    spanUtils.getBuilder().setSpan(livingClickTextSpan, length1, length2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                    spanUtils.append(giftResourceBean.getName()).setFontSize(13, true)
                            .setForegroundColor(0xffFFF796).setAlign(Layout.Alignment.ALIGN_CENTER);

                    sb.delete(0, sb.length());
                    sb.append("]x").append(gBean.getCount());
                    spanUtils.append(sb.toString()).setFontSize(13, true)
                            .setForegroundColor(0xffffffff).setAlign(Layout.Alignment.ALIGN_CENTER);
                }

                break;
        }
    }

    /**
     * ??????????????????
     */
    public static SpanUtils appendFollowMessage(SpanUtils spanUtils, LivingFollowMessage fBean, Context context) {
        if (fBean == null || TextUtils.isEmpty(fBean.getProtocol())) {
            return spanUtils;
        }

        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.icon_tag_sys);
        if (bitmap == null) return spanUtils;
        int height = ScreenUtils.getDip2px(context, 16);
        int width = ScreenUtils.getDip2px(context, 40);
        spanUtils.appendImage(ImageUtils.scale(bitmap, width, height), SpanUtils.ALIGN_CENTER);//120/68
        spanUtils.append(" ");

        switch (fBean.getProtocol()) {
            case MessageProtocol.LIVE_FOLLOW:
                spanUtils.append(fBean.getNickname()).setForegroundColor(0xff85EFFF);
                spanUtils.append(context.getString(R.string.focusAnchor)).setForegroundColor(0xffffffff);
                spanUtils.append(fBean.getAnchorNickName()).setForegroundColor(0xff85EFFF);
                break;
        }
        return spanUtils;
    }

}
