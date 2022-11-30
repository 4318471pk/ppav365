package com.live.fox.dialog.bottomDialog;

import android.animation.ValueAnimator;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;

import com.google.gson.Gson;
import com.live.fox.R;
import com.live.fox.base.BaseBindingDialogFragment;
import com.live.fox.common.JsonCallback;
import com.live.fox.databinding.DialogArealistSelectorBinding;
import com.live.fox.entity.AddressBean;
import com.live.fox.entity.User;
import com.live.fox.server.Api_User;
import com.live.fox.utils.device.ScreenUtils;
import com.live.fox.view.wheel.OnWheelChangedListener;
import com.live.fox.view.wheel.WheelView;
import com.live.fox.view.wheel.adapters.ListWheelAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Handler;

public class AreaListSelectorDialog extends BaseBindingDialogFragment {

    DialogArealistSelectorBinding mBind;
    List<AddressBean> provinceList = new ArrayList<>();
    List<String> province = new ArrayList<>();
    List<String> cityFist = new ArrayList<>();
    HashMap<String, List<String>> city = new HashMap<>();

    @Override
    public void onClickView(View view) {
        switch (view.getId())
        {
            case R.id.rlMain:
            case R.id.tvCancel:
                dismissAllowingStateLoss();
                break;
            case R.id.tvConfirm:
                String p = province.get(mBind.wheel1.getCurrentItem());
                String c = city.get(p).get(mBind.wheel2.getCurrentItem());
                onCityConfirm.onSelect(p,c,this);

                break;
        }
    }

    @Override
    public int onCreateLayoutId() {
        return R.layout.dialog_arealist_selector;
    }

    @Override
    public void initView(View view) {
        mBind=getViewDataBinding();
        mBind.setClick(this);
        startAnimate();

//        HashMap<String,List<String>> lists=new HashMap<>();
//        List<String> strings=new ArrayList<>();
//        strings.add("1123");
//        strings.add("32432");
//        strings.add("sdsad");
//        strings.add("sadsa");
//        strings.add("32xcxzc432");
//        strings.add("sdsadsasad");
//        List<String> strings2=new ArrayList<>();
//        strings2.add("sad");
//        strings2.add("231");
//        strings2.add("sdssadsadad");
//        strings2.add("sa213213dsa");
//        strings2.add("31232xcxzc432");
//        strings2.add("sad");
//        lists.put("1111",strings);
//        lists.put("2222",strings2);
//
//        List<String> list1=new ArrayList<>();
//        list1.add("1111");
//        list1.add("2222");

        initData();


        int dip45= ScreenUtils.getDip2px(getContext(),45);
        ListWheelAdapter listWheelAdapter1=new ListWheelAdapter(getContext(),province);
        ListWheelAdapter listWheelAdapter2=new ListWheelAdapter(getContext(),cityFist);
        listWheelAdapter1.setTextSize(16);
        listWheelAdapter1.setItemHeight(dip45);
        listWheelAdapter2.setTextSize(16);
        listWheelAdapter2.setItemHeight(dip45);

        mBind.wheel1.setViewAdapter(listWheelAdapter1);
        mBind.wheel2.setViewAdapter(listWheelAdapter2);
        mBind.wheel1.setCyclic(true);
        mBind.wheel2.setCyclic(true);
        mBind.wheel1.setVisibleItems(3);
        mBind.wheel2.setVisibleItems(3);

        mBind.wheel1.addChangingListener(new OnWheelChangedListener() {
            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                ListWheelAdapter listWheelAdapter2=new ListWheelAdapter(getContext(),city.get(province.get(newValue)));
                listWheelAdapter2.setTextSize(16);
                listWheelAdapter2.setItemHeight(dip45);
                mBind.wheel2.setViewAdapter(listWheelAdapter2);
            }
        });

    }


    /**
     * 初始化数据
     */
    private void initData() {
        try {
            //InputStreamReader 将字节输入流转换为字符流
            //注意：address.json 是因人而异的
            InputStreamReader isr = new InputStreamReader(this.getResources().getAssets().open("province.json"), "UTF-8");
            //包装字符流,将字符流放入缓存里
            BufferedReader br = new BufferedReader(isr);
            String line;
            //StringBuilder和StringBuffer功能类似,存储字符串
            StringBuilder builder = new StringBuilder();
            while ((line = br.readLine()) != null) {
                //append 被选元素的结尾(仍然在内部)插入指定内容,缓存的内容依次存放到builder中
                builder.append(line);
            }
            br.close();
            isr.close();

            //builder.toString() 返回表示此序列中数据的字符串 (就是json串，后面自行解析就行)
            //这里我用的是fastJson,具体解析方式自行决定就好,数据格式也自行决定就好

            JSONArray arr = new JSONArray(builder.toString());
            for (int i = 0; i < arr.length(); i++) {
                AddressBean bean = new Gson().fromJson(arr.getJSONObject(i).toString(), AddressBean.class);
                province.add(bean.getName());
                List<String> list = new ArrayList<>();
                if (bean.getName().equals("北京市") || bean.getName().equals("天津市")  ||
                        bean.getName().equals("上海市") || bean.getName().equals("重庆市")) {
                    list.addAll(bean.getCity().get(0).getArea());
                } else {
                    for (int j=0; j < bean.getCity().size(); j++) {
                        list.add(bean.getCity().get(j).getName());
                    }
                }
                if (cityFist.size() == 0) {
                    cityFist = list;
                }
                city.put(bean.getName(), list);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    public interface OnCityConfirm {
        void onSelect(String province, String city,AreaListSelectorDialog dialog);
    }

    private OnCityConfirm onCityConfirm;

    public void setOnCityConfirm(OnCityConfirm onCityConfirm){
        this.onCityConfirm = onCityConfirm;
    }


    public void startAnimate(){

        Animation animation= new TranslateAnimation(Animation.ABSOLUTE,0,
                Animation.ABSOLUTE,0
                ,Animation.RELATIVE_TO_PARENT,1f
                ,Animation.RELATIVE_TO_PARENT,0f);
        animation.setDuration(300);

        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        mBind.rllMain.startAnimation(animation);

        ValueAnimator valueAnimator = ValueAnimator.ofInt(0, 0x88);
        //开始延时时长
        valueAnimator.setStartDelay(0);
        //动画时长
        valueAnimator.setDuration(300);
        //重复次数
        valueAnimator.setRepeatCount(0);
        //设置重复模式 ValueAnimator.RESTART正序重新开始  ValueAnimator.REVERSE逆序重新开始
//        valueAnimator.setRepeatMode(ValueAnimator.RESTART);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                //获取当前值
                int number = (int) animation.getAnimatedValue();
                mBind.rlMain.setBackgroundColor(number<<24);
            }
        });
        //开启动画
        valueAnimator.start();
    }
}
