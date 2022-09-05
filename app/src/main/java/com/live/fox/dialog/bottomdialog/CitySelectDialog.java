package com.lc.base.dialog.bottomdialog;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.live.fox.R;
import com.live.fox.entity.CityLevel1;
import com.live.fox.utils.FileUtils;
import com.live.fox.utils.GsonUtil;
import com.live.fox.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;

/**
 *  城市选择器 单选(最多2层) 如果需要定位 则需要补充LocationManager类
 */
public class CitySelectDialog extends DialogFragment {


    private TextView tv_head_left, tv_location;
    private LinearLayout locationLayout, allLayout;
    ImageView iv_head_left;

    private RecyclerView rv;

    BaseQuickAdapter adapter;

    int level = 1;//层级

    List<CityLevel1> cityList;
    List<String> cityLevel1 = new ArrayList<>();
    List<String> cityLevel2 = new ArrayList<>();


    String address = "unknow";

    View headView;

    public static CitySelectDialog newInstance() {
        CitySelectDialog fragment = new CitySelectDialog();
//        Bundle bundle = new Bundle();
//        bundle.putString("address", address);
//        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Dialog dialog = new Dialog(getActivity(), R.style.BottomDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_city_select);
        dialog.setCanceledOnTouchOutside(true); // 外部點擊取消

        tv_head_left = dialog.findViewById(R.id.tv_head_left);
        iv_head_left = dialog.findViewById(R.id.iv_head_left);
        dialog.findViewById(R.id.tv_head_left).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(level==2){
                    changeLevel(1, -1);
                }else {
                    dismiss();
                }
            }
        });
        dialog.findViewById(R.id.tv_head_right).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        rv = dialog.findViewById(R.id.rv_);

        headView = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_city_select_headview, (ViewGroup) rv.getParent(), false);
        locationLayout =  headView.findViewById(R.id.ll_location);
        allLayout = headView.findViewById(R.id.ll_all);
        tv_location = headView.findViewById(R.id.tv_location);
        locationLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(address.equals("unknow")){
                    maxlocationTime = 10;
                    tv_location.setText("重新定位中...");
//                    LocationManager.ins().refreshLocation();
                    locationHandler.sendEmptyMessageDelayed(0, 300);
                }else {
                    selText(address);
                }
            }
        });
        allLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selText("全部");
            }
        });


        initData();

        // 設置寬度爲屏寬, 靠近屏幕底部。
        Window window = dialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        // 寬度持平
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setGravity(Gravity.BOTTOM);
        window.setBackgroundDrawableResource(R.color.transparent);
        window.setAttributes(lp);

        return dialog;
    }


    public void initData(){
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rv.setLayoutManager(layoutManager);
        rv.setAdapter(adapter = new BaseQuickAdapter(R.layout.item_base_style1, new ArrayList<String>()) {
            @Override
            protected void convert(BaseViewHolder helper, Object item) {
                helper.setText(R.id.tv_, (String)item);
            }
        });


        if(!refreshLocation());{
            LogUtils.e(address);
            if(address.equals("unknow")){
                tv_location.setText("定位失败，未开启GPS或未允许定位权限");
            }else {
                tv_location.setText("当前城市："+address);
            }
        }

        changeLevel(1, -1);

        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if(level==1){
                    if(cityList.get(position).getCity().size()==1){
                        //只有一个层级
//                        ToastUtils.showShort(cityList.get(position).getName());
                        selText((String) adapter.getData().get(position)+","+(String) adapter.getData().get(position));
                    }else {
                        city = (String) adapter.getData().get(position);
                        changeLevel(2, position);
                    }
                }else {
//                    ToastUtils.showShort((String) adapter.getData().get(position));
                    selText(city+","+(String) adapter.getData().get(position));
                }
            }
        });
    }

    String city = "";

    public void changeLevel(int level, int selPos){
        if(level==1){
            this.level = 1;
//            tv_head_left.setText("取消");
//            tv_head_left.setCompoundDrawables(null, null, null, null);
            tv_head_left.setVisibility(View.GONE);
            iv_head_left.setVisibility(View.GONE);
            allLayout.setVisibility(View.VISIBLE);
            locationLayout.setVisibility(View.VISIBLE);

            if(cityList==null){
                String json = FileUtils.getAssetsContent(getActivity(), "province.json");
//                LogUtils.e(json);
                cityList = GsonUtil.getObjects(json, CityLevel1[].class);
                LogUtils.e(cityList.size()+",2222");
                cityLevel1.clear();
                for (int i = 0; i < cityList.size(); i++) {
                    cityLevel1.add(cityList.get(i).getName());
                }
            }
            LogUtils.e(cityLevel1.size()+",11111");

//            adapter.addHeaderView(headView);

            adapter.setNewData(cityLevel1);
            adapter.notifyDataSetChanged();
        }else {
            this.level = 2;
            allLayout.setVisibility(View.GONE);
            locationLayout.setVisibility(View.GONE);
            tv_head_left.setVisibility(View.VISIBLE);
            iv_head_left.setVisibility(View.VISIBLE);
            tv_head_left.setText(cityList.get(selPos).getName());

            cityLevel2.clear();
            for (int i = 0; i < cityList.get(selPos).getCity().size(); i++) {
                cityLevel2.add(cityList.get(selPos).getCity().get(i).getName());
            }

            adapter.removeHeaderView(headView);

            adapter.setNewData(cityLevel2);
            adapter.notifyDataSetChanged();
        }
    }


    public void selText(String text){
        listener.onSelTextChange(text);
        dismiss();
    }

    public boolean refreshLocation(){
//        address = LocationManager.ins().getCity();
        LogUtils.e("address:"+address);
        if(address.equals("unknow")){
            return false;
        }else {
            tv_location.setText("当前城市："+address);
            return true;
        }
    }

    @Override
    public void dismiss() {
        locationHandler.removeMessages(0);
        super.dismiss();
    }



    int maxlocationTime = 10;

    private Handler locationHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
                boolean isSuccess = refreshLocation();
                if(!isSuccess){
                    maxlocationTime--;
                    if(maxlocationTime>0) {
                        locationHandler.sendEmptyMessageDelayed(0, 300);
                    }else {
                        locationHandler.removeMessages(0);
                        tv_location.setText("定位失败，未开启GPS或未允许定位权限");
                    }
                }
            return false;
        }
    });

    //评论个数变化监听
    public interface OnSureBtnListener {
        void onSelTextChange(String data);
    }

    CitySelectDialog.OnSureBtnListener listener;
    public void setSureBtnListener(CitySelectDialog.OnSureBtnListener listener) {
        //设置关闭弹框的回调
        this.listener = listener;
    }

}
