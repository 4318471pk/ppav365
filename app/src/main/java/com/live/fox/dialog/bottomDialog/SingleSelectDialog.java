package com.lc.base.dialog.bottomdialog;

import android.app.Dialog;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
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
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.live.fox.R;
import com.live.fox.utils.GsonUtil;
import com.live.fox.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;

//单选Dialog 支持单层级和多层级层(最多2层)
public class SingleSelectDialog extends DialogFragment{

    private TextView tv_head_left, tv_head_right;
    private ImageView iv_head_left;
    private RecyclerView rv;
    private LinearLayout llCancel;

    String pageTitle;

    BaseQuickAdapter adapter;

    int level = 1;//层级

    List<String> optionsItems1 = new ArrayList<>();
    List<List<String>> optionsItems2 = new ArrayList<>();

    String selText;

    //单选 单层级 ["广东","湖南","广西"]
    public static SingleSelectDialog newInstance(String pageTitle, List<String> optionsItems) {
        SingleSelectDialog fragment = new SingleSelectDialog();
        Bundle bundle = new Bundle();
        bundle.putString("pageTitle", pageTitle);
        bundle.putInt("levelCount", 1);
        bundle.putString("optionsItems1", new Gson().toJson(optionsItems));
        fragment.setArguments(bundle);
        return fragment;
    }

    //单选 多层级 ["广东","湖南","广西"]  [["广州","东莞","珠海"], ["长沙","株洲"], ["玉林"]]
    public static SingleSelectDialog newInstance(String pageTitle, List<String> optionsItems1, List<List<String>> optionsItems2) {
        SingleSelectDialog fragment = new SingleSelectDialog();
        Bundle bundle = new Bundle();
        bundle.putString("pageTitle", pageTitle);
        bundle.putInt("levelCount", 2);
        bundle.putString("optionsItems1", new Gson().toJson(optionsItems1));
        bundle.putString("optionsItems2", new Gson().toJson(optionsItems2));
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();
        if (bundle != null) {
            pageTitle = bundle.getString("pageTitle");
            level = bundle.getInt("levelCount");
            String data1 = bundle.getString("optionsItems1");
            optionsItems1 = GsonUtil.getObjects(data1, String[].class);
            LogUtils.e("level:"+level);
            if(level==2){
                String data2 = bundle.getString("optionsItems2");
                optionsItems2 = new Gson().fromJson(data2, new TypeToken<ArrayList<ArrayList<String>>>(){}.getType());
            }
        }
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Dialog dialog = new Dialog(getActivity(), R.style.BottomDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_single_sel);
        dialog.setCanceledOnTouchOutside(true); // 外部點擊取消

        tv_head_left = dialog.findViewById(R.id.tv_head_left);
        iv_head_left = dialog.findViewById(R.id.iv_head_left);
        tv_head_right = dialog.findViewById(R.id.tv_head_right);
        llCancel = dialog.findViewById(R.id.ll_cancel);
        rv = dialog.findViewById(R.id.rv_);
        tv_head_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(level==2){
                    changeLevel(1, -1);
                }else {
                    dismiss();
                }
            }
        });
        tv_head_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        ((TextView)dialog.findViewById(R.id.tv_head_title)).setText(pageTitle);
        if(optionsItems2.size()==0){
            //只有一层
            tv_head_left.setVisibility(View.GONE);
            iv_head_left.setVisibility(View.GONE);
            tv_head_right.setVisibility(View.GONE);
            llCancel.setVisibility(View.VISIBLE);
            llCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dismiss();
                }
            });
        }else {
            //有二层
            llCancel.setVisibility(View.GONE);
        }

        initData();

        // 設置寬度爲屏寬, 靠近屏幕底部。
        Window window = dialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        // 寬度持平
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setGravity(Gravity.BOTTOM);
        window.setBackgroundDrawableResource(R.color.transparent);
        window.setWindowAnimations(R.style.ActionSheetDialogAnimation);
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


        changeLevel(1, -1);

        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                LogUtils.e(level+",");
                LogUtils.e((optionsItems2!=null)+",");
                if(level==1 && optionsItems2!=null && optionsItems2.size()>0){
                    if(position > optionsItems2.size() || optionsItems2.get(position)==null || optionsItems2.get(position).size()==0){
                        //只有一个层级
                        selText((String) adapter.getData().get(position));
                    }else {
                        changeLevel(2, position);
                    }
                }else {
//                    ToastUtils.showShort((String) adapter.getData().get(position));
                    selText((String) adapter.getData().get(position));
                }
            }
        });
    }

    public void changeLevel(int level, int selPos){
        if(level==1){
            this.level = 1;
            tv_head_left.setVisibility(View.GONE);
            iv_head_left.setVisibility(View.GONE);
            selText = "";

            adapter.setNewData(optionsItems1);
            adapter.notifyDataSetChanged();
        }else {
            this.level = 2;
            tv_head_left.setVisibility(View.VISIBLE);
            iv_head_left.setVisibility(View.VISIBLE);
            tv_head_left.setText(optionsItems1.get(selPos));

            selText = optionsItems1.get(selPos)+",";

//            for (int i = 0; i < cityList.get(selPos).getCity().size(); i++) {
//                cityLevel2.add(cityList.get(selPos).getCity().get(i).getName());
//            }
//
//            adapter.removeHeaderView(headView);

            adapter.setNewData(optionsItems2.get(selPos));
            adapter.notifyDataSetChanged();
        }
    }


    public void selText(String text){
//        ToastUtils.showShort(selText + text);
        if(level==2){
            text = selText + text;
        }
        listener.onSelTextChange(text);
        dismiss();
    }



    @Override
    public void dismiss() {
//        locationHandler.removeMessages(0);
        super.dismiss();
    }

    //    public void addText(String text){
//        if(selCityList.contains(text))
//            return;
//
//        if(selCityList.size()>=4){
//            ToastUtils.showShort("最多可以选择4个,点击已选项可取消选择！");
//            return;
//        }
//
//
//        TextView textView = new TextView(getActivity());
//        textView.setGravity(Gravity.CENTER_VERTICAL);
//        textView.setText(" "+text);
//        textView.setTextColor(Color.BLACK);
//        textView.setTextSize(DeviceUtils.sp2px(getActivity(), 5));
//        textView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                selCityList.remove(((TextView)view).getText().toString().trim());
//                ll_selcity.removeView(view);
////                int pos = -1;
////                for (int i = 0; i < selCity.size(); i++) {
////                    if(((TextView)view).getText().toString().trim().equals());
////                }
//            }
//        });
//        ll_selcity.addView(textView);
//        selCityList.add(text);
//    }


//    int maxlocationTime = 10;
//
//    private Handler locationHandler = new Handler(new Handler.Callback() {
//        @Override
//        public boolean handleMessage(Message msg) {
//                boolean isSuccess = refreshLocation();
//                if(!isSuccess){
//                    maxlocationTime--;
//                    if(maxlocationTime>0) {
//                        locationHandler.sendEmptyMessageDelayed(0, 300);
//                    }else {
//                        locationHandler.removeMessages(0);
//                        tv_location.setText("定位失败，未开启GPS或未允许定位权限");
//                    }
//                }
//            return false;
//        }
//    });

    //评论个数变化监听
    public interface OnSureBtnListener {
        void onSelTextChange(String data);
    }

    SingleSelectDialog.OnSureBtnListener listener;
    public void setSureBtnListener(SingleSelectDialog.OnSureBtnListener listener) {
        //设置关闭弹框的回调
        this.listener = listener;
    }

}
