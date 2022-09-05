package com.live.fox.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.live.fox.AppConfig;
import com.live.fox.Constant;
import com.live.fox.R;
import com.live.fox.common.JsonCallback;
import com.live.fox.entity.LsResult;
import com.live.fox.server.Api_Cp;
import com.live.fox.utils.ToastUtils;

import java.util.List;


public class LSDialogFragment extends DialogFragment implements View.OnClickListener {

    private TextView tvNameks;
    private TextView tvQihaoks;
    private TextView dot01;
    private TextView dot02;
    private TextView dot03;
    private TextView tvNamessc;
    private TextView tvQihaossc;
    private TextView numa;
    private TextView numb;
    private TextView numc;
    private TextView numd;
    private TextView nume;
    private TextView tvNamepk10;
    private TextView tvQihaopk10;
    private TextView numpa;
    private TextView numpb;
    private TextView numpc;
    private TextView numpd;
    private TextView numpe;
    private TextView numpf;
    private TextView numpg;
    private TextView numph;
    private TextView numpi;
    private TextView numpj;
    private TextView tvNamelhc;
    private TextView tvQihaolhc;
    private TextView numla;
    private TextView numlb;
    private TextView numlc;
    private TextView numld;
    private TextView numle;
    private TextView numlf;
    private TextView numlg;
    private TextView tvNamehn;
    private TextView tvQihaohn;
    private TextView numhna;
    private TextView numhnb;
    private TextView numhnc;
    private TextView numhnd;
    private RelativeLayout reKS;
    private RelativeLayout reFast5d;
    private RelativeLayout rePK10;
    private RelativeLayout reLHC;
    private RelativeLayout reHN;
    private TextView tvNameyxx;
    private TextView tvQihaoyxx;
    private ImageView yxx01;
    private ImageView yxx02;
    private ImageView yxx03;
    private RelativeLayout reYXX;
    private List<LsResult> lsData;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Dialog dialog = new Dialog(getActivity(), R.style.DialogDefault);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.fragment_ls);
        dialog.setCanceledOnTouchOutside(true); // 外部點擊取消
        // 設置寬度爲屏寬, 靠近屏幕底部。
        Window window = dialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        // 寬度持平
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setGravity(Gravity.BOTTOM);
        window.setDimAmount(0.05f);
        window.setBackgroundDrawableResource(R.color.transparent);
        window.setWindowAnimations(R.style.ActionSheetDialogAnimation);
        window.setAttributes(lp);
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_ls, container, false);
    }

    void getAllLotteryLatestResult() {
        Api_Cp.ins().getLatestResult(new JsonCallback<List<LsResult>>() {
            @Override
            public void onSuccess(int code, String msg, List<LsResult> data) {
                if (code == Constant.Code.SUCCESS && data != null && data.size() > 0) {
                    lsData = data;
                    //fast5d
                    data.size();
                    tvNamessc.setText(data.get(0).getNickName());
                    tvQihaossc.setText(data.get(0).getExpect());
                    List<Integer> lotteryResult5d = data.get(0).getLotteryResult();
                    for (int i = 0; i < lotteryResult5d.size(); i++) {
                        switch (i) {
                            case 0:
                                numa.setText(lotteryResult5d.get(i) + "");
                                break;
                            case 1:
                                numb.setText(lotteryResult5d.get(i) + "");
                                break;
                            case 2:
                                numc.setText(lotteryResult5d.get(i) + "");
                                break;
                            case 3:
                                numd.setText(lotteryResult5d.get(i) + "");
                                break;
                            case 4:
                                nume.setText(lotteryResult5d.get(i) + "");
                                break;
                        }
                    }
                    //yfks
                    if (data.size() >= 2) {
                        tvNameks.setText(data.get(1).getNickName());
                        tvQihaoks.setText(data.get(1).getExpect());
                        List<Integer> lotteryResult = data.get(1).getLotteryResult();
                        for (int i = 0; i < lotteryResult.size(); i++) {
                            switch (i) {
                                case 0:
                                    choicePis(dot01, lotteryResult.get(i));
                                    break;
                                case 1:
                                    choicePis(dot02, lotteryResult.get(i));
                                    break;
                                case 2:
                                    choicePis(dot03, lotteryResult.get(i));
                                    break;
                            }
                        }
                    }
                    //pk10
                    if (data.size() >= 3) {
                        tvNamepk10.setText(data.get(2).getNickName());
                        tvQihaopk10.setText(data.get(2).getExpect());
                        List<Integer> lotteryResultpk10 = data.get(2).getLotteryResult();
                        for (int i = 0; i < lotteryResultpk10.size(); i++) {
                            switch (i) {
                                case 0:
                                    numpa.setText(lotteryResultpk10.get(i) + "");
                                    break;
                                case 1:
                                    numpb.setText(lotteryResultpk10.get(i) + "");
                                    break;
                                case 2:
                                    numpc.setText(lotteryResultpk10.get(i) + "");
                                    break;
                                case 3:
                                    numpd.setText(lotteryResultpk10.get(i) + "");
                                    break;
                                case 4:
                                    numpe.setText(lotteryResultpk10.get(i) + "");
                                    break;
                                case 5:
                                    numpf.setText(lotteryResultpk10.get(i) + "");
                                    break;
                                case 6:
                                    numpg.setText(lotteryResultpk10.get(i) + "");
                                    break;
                                case 7:
                                    numph.setText(lotteryResultpk10.get(i) + "");
                                    break;
                                case 8:
                                    numpi.setText(lotteryResultpk10.get(i) + "");
                                    break;
                                case 9:
                                    numpj.setText(lotteryResultpk10.get(i) + "");
                                    break;
                            }
                        }
                    }
                    //6
                    if (data.size() >= 4) {
                        tvNamelhc.setText(data.get(3).getNickName());
                        tvQihaolhc.setText(data.get(3).getExpect());
                        List<Integer> lotteryResultlhc = data.get(3).getLotteryResult();
                        for (int i = 0; i < lotteryResultlhc.size() - 1; i++) {
                            switch (i) {
                                case 0:
                                    numla.setText(lotteryResultlhc.get(i) + "");
                                    break;
                                case 1:
                                    numlb.setText(lotteryResultlhc.get(i) + "");
                                    break;
                                case 2:
                                    numlc.setText(lotteryResultlhc.get(i) + "");
                                    break;
                                case 3:
                                    numld.setText(lotteryResultlhc.get(i) + "");
                                    break;
                                case 4:
                                    numle.setText(lotteryResultlhc.get(i) + "");
                                    break;
                                case 5:
                                    numlf.setText(lotteryResultlhc.get(i) + "");
                                    break;
                                case 6:
                                    numlg.setText(lotteryResultlhc.get(i) + "");
                                    if (3 == lotteryResultlhc.get(7)) {
                                        numlg.setBackgroundResource(R.drawable.prizer_num_bg_blue);
                                    } else if (2 == lotteryResultlhc.get(7)) {
                                        numlg.setBackgroundResource(R.drawable.prizer_num_bg_green);
                                    } else {
                                        numlg.setBackgroundResource(R.drawable.prizer_num_bg_red);
                                    }
                                    break;
                            }
                        }
                    }
                    //yn_hncp
                    if (data.size() >= 5) {
                        tvNamehn.setText(data.get(4).getNickName());
                        tvQihaohn.setText(data.get(4).getExpect());
                        List<Integer> lotteryResulthn = data.get(4).getLotteryResult();
                        for (int i = 0; i < lotteryResulthn.size(); i++) {
                            switch (i) {
                                case 0:
                                    numhna.setText(lotteryResulthn.get(i) + "");
                                    break;
                                case 1:
                                    numhnb.setText(lotteryResulthn.get(i) + "");
                                    break;
                                case 2:
                                    numhnc.setText(lotteryResulthn.get(i) + "");
                                    break;
                                case 3:
                                    numhnd.setText(lotteryResulthn.get(i) + "");
                                    break;

                            }
                        }
                    }
                    //yxx
                    List<Integer> lotteryResultYxx;
                    if (AppConfig.isThLive()) {
                        reHN.setVisibility(View.GONE);
                        tvNameyxx.setText(data.get(4).getNickName());
                        tvQihaoyxx.setText(data.get(4).getExpect());
                        lotteryResultYxx = data.get(4).getLotteryResult();
                    } else {
                        tvNameyxx.setText(data.get(5).getNickName());
                        tvQihaoyxx.setText(data.get(5).getExpect());
                        lotteryResultYxx = data.get(5).getLotteryResult();
                    }

                    for (int i = 0; i < lotteryResultYxx.size(); i++) {
                        switch (i) {
                            case 0:
                                choicePisYxx(yxx01, lotteryResultYxx.get(i));
                                break;
                            case 1:
                                choicePisYxx(yxx02, lotteryResultYxx.get(i));
                                break;
                            case 2:
                                choicePisYxx(yxx03, lotteryResultYxx.get(i));
                                break;
                        }
                    }
                } else {
                    ToastUtils.showShort(msg);
                }
            }
        });
    }

    private void choicePis(TextView textView, int item) {
        if (1 == item) {
            textView.setBackgroundResource(R.drawable.dot01);
        } else if (2 == item) {
            textView.setBackgroundResource(R.drawable.dot02);
        } else if (3 == item) {
            textView.setBackgroundResource(R.drawable.dot03);
        } else if (4 == item) {
            textView.setBackgroundResource(R.drawable.dot04);
        } else if (5 == item) {
            textView.setBackgroundResource(R.drawable.dot05);
        } else if (6 == item) {
            textView.setBackgroundResource(R.drawable.dot06);
        }
    }

    private void choicePisYxx(ImageView imageView, int item) {
        imageView.setImageResource(R.drawable.level_list_lottery);
        imageView.setImageLevel(item);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        getAllLotteryLatestResult();
    }

    public static LSDialogFragment newInstance() {
        LSDialogFragment fragment = new LSDialogFragment();
        return fragment;
    }

    private void initView(View view) {
        tvNameks = (TextView) view.findViewById(R.id.tv_nameks);
        tvQihaoks = (TextView) view.findViewById(R.id.tv_qihaoks);
        dot01 = (TextView) view.findViewById(R.id.dot01);
        dot02 = (TextView) view.findViewById(R.id.dot02);
        dot03 = (TextView) view.findViewById(R.id.dot03);
        tvNamessc = (TextView) view.findViewById(R.id.tv_namessc);
        tvQihaossc = (TextView) view.findViewById(R.id.tv_qihaossc);
        numa = (TextView) view.findViewById(R.id.numa);
        numb = (TextView) view.findViewById(R.id.numb);
        numc = (TextView) view.findViewById(R.id.numc);
        numd = (TextView) view.findViewById(R.id.numd);
        nume = (TextView) view.findViewById(R.id.nume);
        tvNamepk10 = (TextView) view.findViewById(R.id.tv_namepk10);
        tvQihaopk10 = (TextView) view.findViewById(R.id.tv_qihaopk10);
        numpa = (TextView) view.findViewById(R.id.numpa);
        numpb = (TextView) view.findViewById(R.id.numpb);
        numpc = (TextView) view.findViewById(R.id.numpc);
        numpd = (TextView) view.findViewById(R.id.numpd);
        numpe = (TextView) view.findViewById(R.id.numpe);
        numpf = (TextView) view.findViewById(R.id.numpf);
        numpg = (TextView) view.findViewById(R.id.numpg);
        numph = (TextView) view.findViewById(R.id.numph);
        numpi = (TextView) view.findViewById(R.id.numpi);
        numpj = (TextView) view.findViewById(R.id.numpj);
        tvNamelhc = (TextView) view.findViewById(R.id.tv_namelhc);
        tvQihaolhc = (TextView) view.findViewById(R.id.tv_qihaolhc);
        numla = view.findViewById(R.id.numla);
        numlb = view.findViewById(R.id.numlb);
        numlc = view.findViewById(R.id.numlc);
        numld = view.findViewById(R.id.numld);
        numle = view.findViewById(R.id.numle);
        numlf = view.findViewById(R.id.numlf);
        numlg = view.findViewById(R.id.numlg);
        tvNamehn = view.findViewById(R.id.tv_namehn);
        tvQihaohn = view.findViewById(R.id.tv_qihaohn);
        numhna = view.findViewById(R.id.numhna);
        numhnb = view.findViewById(R.id.numhnb);
        numhnc = view.findViewById(R.id.numhnc);
        numhnd = view.findViewById(R.id.numhnd);
        reKS = view.findViewById(R.id.reKS);
        reFast5d = view.findViewById(R.id.reFast5d);
        rePK10 = view.findViewById(R.id.rePK10);
        reLHC = view.findViewById(R.id.reLHC);
        reHN = view.findViewById(R.id.reHN);
        reYXX = view.findViewById(R.id.reYXX);
        tvNameyxx = view.findViewById(R.id.tv_nameyxx);
        tvQihaoyxx = view.findViewById(R.id.tv_qihaoyxx);
        yxx01 = view.findViewById(R.id.yxx01);
        yxx02 = view.findViewById(R.id.yxx02);
        yxx03 = view.findViewById(R.id.yxx03);
        reKS.setOnClickListener(this);
        reFast5d.setOnClickListener(this);
        rePK10.setOnClickListener(this);
        reLHC.setOnClickListener(this);
        reHN.setOnClickListener(this);
        reYXX.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (lsData == null || lsData.size() == 0) {
            return;
        }

        switch (v.getId()) {
            case R.id.reFast5d:
                FastDialogFragment fastDialogFragment = FastDialogFragment.newInstance(lsData.get(0).getLotteryName(), lsData.get(0).getNickName());
                if (!fastDialogFragment.isAdded()) {
                    fastDialogFragment.show(requireActivity().getSupportFragmentManager(), FastDialogFragment.class.getSimpleName());
                }
                break;

            case R.id.reKS:
                KSDialogFragment ksDialogFragment =
                        KSDialogFragment.newInstance(lsData.get(1).getLotteryName(), lsData.get(1).getNickName());
                if (!ksDialogFragment.isAdded()) {
                    ksDialogFragment.show(requireActivity().getSupportFragmentManager(), KSDialogFragment.class.getSimpleName());
                }
                break;

            case R.id.rePK10:
                PKDialogFragment pkDialogFragment =
                        PKDialogFragment.newInstance(lsData.get(2).getLotteryName(), lsData.get(2).getNickName());
                if (!pkDialogFragment.isAdded()) {
                    pkDialogFragment.show(requireActivity().getSupportFragmentManager(), PKDialogFragment.class.getSimpleName());
                }
                break;

            case R.id.reLHC:
                LHCDialogFragment lhcDialogFragment =
                        LHCDialogFragment.newInstance(lsData.get(3).getLotteryName(), lsData.get(3).getNickName());
                if (!lhcDialogFragment.isAdded()) {
                    lhcDialogFragment.show(requireActivity()
                            .getSupportFragmentManager(), LHCDialogFragment.class.getSimpleName());
                }
                break;

            case R.id.reHN:
                HNHDialogFragment hnDialogFragment =
                        HNHDialogFragment.newInstance(lsData.get(4).getLotteryName(), lsData.get(4).getNickName());
                if (!hnDialogFragment.isAdded()) {
                    hnDialogFragment.show(requireActivity()
                            .getSupportFragmentManager(), HNHDialogFragment.class.getSimpleName());
                }
                break;

            case R.id.reYXX:
                String lotteryName;
                String niceName;
                if (AppConfig.isThLive()) {
                    lotteryName = lsData.get(4).getLotteryName();
                    niceName = lsData.get(4).getNickName();
                } else {
                    lotteryName = lsData.get(5).getLotteryName();
                    niceName = lsData.get(5).getNickName();
                }
                YXXDialogFragment yxxDialogFragment = YXXDialogFragment.newInstance(lotteryName, niceName);
                if (!yxxDialogFragment.isAdded()) {
                    yxxDialogFragment.show(requireActivity()
                            .getSupportFragmentManager(), YXXDialogFragment.class.getSimpleName());
                }

                break;
        }
    }
}
