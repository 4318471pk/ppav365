package com.live.fox.dialog;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.google.gson.Gson;
import com.live.fox.R;
import com.live.fox.AnchorLiveActivity;
import com.live.fox.utils.LogUtils;
import com.live.fox.utils.StringUtils;
import com.live.fox.utils.ToastUtils;
import com.live.fox.utils.Utils;
import com.lovense.sdklibrary.Lovense;
import com.lovense.sdklibrary.LovenseToy;
import com.lovense.sdklibrary.callBack.LovenseError;
import com.lovense.sdklibrary.callBack.OnErrorListener;
import com.lovense.sdklibrary.callBack.OnSearchToyListener;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.ArrayList;
import java.util.List;


/**
 * 玩具列表
 */
public class ToyListDialog extends DialogFragment {

    RecyclerView rv;
    SmartRefreshLayout refreshLayout;

    BaseQuickAdapter rvAdapter;
    public boolean isShow = false;

    List<LovenseToy> lovenseToys = new ArrayList<>();

    public static ToyListDialog newInstance() {
        ToyListDialog fragment = new ToyListDialog();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置样式
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.DialogsWithoutBlack);
        Bundle bundle = getArguments();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        return inflater.inflate(R.layout.dialog_toylist, container, false);
    }

    //如果需要修改大小 修改显示动画 重写此方法
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getDialog().setCanceledOnTouchOutside(false);
        getDialog().setCancelable(false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        isShow = true;

        initView(view);
        setRecycleView();
        searchToysAnd();
    }


    public void initView(View view) {
        rv = view.findViewById(R.id.rv_);
        refreshLayout = view.findViewById(R.id.refreshLayout);
        view.findViewById(R.id.iv_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }


    public void setRecycleView() {
        refreshLayout.setEnablePureScrollMode(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        this.rv.setLayoutManager(layoutManager);
        this.rv.setAdapter(rvAdapter = new BaseQuickAdapter(R.layout.item_toylist, lovenseToys) {
            @Override
            protected void convert(BaseViewHolder helper, Object item) {
                LovenseToy lovenseToy = (LovenseToy) item;
                helper.setText(R.id.tv_name, lovenseToy.getDeviceName() + "");

                helper.addOnClickListener(R.id.tv_invert);
            }
        });

        rvAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                LovenseToy lovenseToy = (LovenseToy) rvAdapter.getItem(position);
                LogUtils.e(new Gson().toJson(lovenseToy));
                ((AnchorLiveActivity) getActivity()).connectToy(lovenseToy);
                dismiss();
            }
        });

    }


    public void searchToysAnd() {
        Lovense.getInstance(Utils.getApp()).searchToys(new OnSearchToyListener() {
            @Override
            public void onSearchToy(LovenseToy lovenseToy) {
//                ToastUtils.showShort("onSearchToy");
                addDevice(lovenseToy);
            }

            @Override
            public void finishSearch() {
//                LogUtils.e("finishSearch");
                ToastUtils.showShort(getString(R.string.toast_search_results));
                Lovense.getInstance(Utils.getApp()).saveToys(lovenseToys, new OnErrorListener() {
                    @Override
                    public void onError(LovenseError error) {
                        if (lovenseToys!=null && lovenseToys.size()>0) {
                            LogUtils.e(new Gson().toJson(lovenseToys));
                        }
                    }

                });
            }

            @Override
            public void onError(LovenseError msg) {
                if(!StringUtils.isEmpty(msg.getMessage())){
                    ToastUtils.showShort(msg.getMessage());
                }else {
                    ToastUtils.showShort(getString(R.string.bluetoothGPS));
                }
                requireActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ((AnchorLiveActivity) requireActivity()).dimissToyListDialog();
                    }
                });

            }

        });


    }

    private void addDevice(LovenseToy lovenseToy) {
        if (lovenseToy != null) {
            LogUtils.e(new Gson().toJson(lovenseToy));
            if (!isAdded(lovenseToy)) {
                lovenseToys.add(lovenseToy);
                rvAdapter.setNewData(lovenseToys);
            }
        }
    }

    protected boolean isAdded(LovenseToy lovenseToy) {
        for (LovenseToy t : lovenseToys) {
            String id = t.getToyId();
            String toyId = lovenseToy.getToyId();
            if (!TextUtils.isEmpty(id) && id.equals(toyId)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void dismiss() {
        isShow = false;
        Lovense.getInstance(Utils.getApp()).stopSearching();
        super.dismiss();
    }

    OnBtnSureClick btnSureClick;

    public void setBtnSureClick(OnBtnSureClick btnClick) {
        btnSureClick = btnClick;
    }

    public interface OnBtnSureClick {
        void onClick();
    }

}
