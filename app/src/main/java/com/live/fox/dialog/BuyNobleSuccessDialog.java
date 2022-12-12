package com.live.fox.dialog;

import android.text.Html;
import android.view.View;
import android.widget.TextView;

import com.live.fox.R;
import com.live.fox.base.BaseBindingDialogFragment;
import com.live.fox.databinding.DialogBuyNobleSuccessBinding;
import com.live.fox.utils.SpanUtils;
import com.live.fox.utils.TimeCounter;
import com.live.fox.view.RankProfileView;
import com.opensource.svgaplayer.SVGADrawable;
import com.opensource.svgaplayer.SVGAParser;
import com.opensource.svgaplayer.SVGAVideoEntity;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.List;

public class BuyNobleSuccessDialog extends BaseBindingDialogFragment {

    DialogBuyNobleSuccessBinding mBind;
    int level;
    boolean isRenew;
    int second=5;

    String svgaList[]=new String[]{"vip1.svga","vip2.svga","vip3.svga","vip4.svga","vip5.svga","vip6.svga","vip7.svga"};
    TimeCounter.TimeListener timeListener=new TimeCounter.TimeListener() {
        @Override
        public void onSecondTick(TimeCounter.TimeListener listener) {
            super.onSecondTick(listener);
            if(isConditionOk())
            {
                second--;
                if(second==0)
                {
                    TimeCounter.getInstance().remove(timeListener);
                    dismissAllowingStateLoss();
                }
                else
                {
                    mBind.tvTime.setText(second + "s");
                }
            }

        }
    };

    public static BuyNobleSuccessDialog getInstance(int level,boolean isRenew)
    {
        BuyNobleSuccessDialog dialog=new BuyNobleSuccessDialog();
        dialog.level=level;
        dialog.isRenew=isRenew;
        return dialog;
    }

    @Override
    public void onClickView(View view) {

    }

    @Override
    public int onCreateLayoutId() {
        return R.layout.dialog_buy_noble_success;
    }

    @Override
    public void initView(View view) {
        mBind=getViewDataBinding();
        mBind.setClick(this);


        String buy = getResources().getString(isRenew?R.string.con_you_xufei:R.string.con_you_buy);
        SpanUtils spanUtils=new SpanUtils();
        spanUtils.append(buy).setForegroundColor(0xffffffff);
        spanUtils.append(getStringWithoutContext(R.string.noble_2)).setForegroundColor(0xffE2B361);
        spanUtils.append(getStringWithoutContext(R.string.tab_change_success)).setForegroundColor(0xffffffff);
        mBind.tvTitle.setText(spanUtils.create());

        if(level>0 && level<8)
        {
            SVGAParser parser = SVGAParser.Companion.shareParser();
            String path="buyVIPSuccessSVGA/"+svgaList[level-1];
            parser.decodeFromAssets(path, new SVGAParser.ParseCompletion() {
                @Override
                public void onComplete(SVGAVideoEntity svgaVideoEntity) {
                    if(svgaVideoEntity!=null)
                    {
                        mBind.ivSvg.setImageDrawable(new SVGADrawable(svgaVideoEntity));
                        mBind.ivSvg.startAnimation();

                        TimeCounter.getInstance().add(timeListener);
                    }
                }

                @Override
                public void onError() {
                }
            }, new SVGAParser.PlayCallback() {
                @Override
                public void onPlay(@NotNull List<? extends File> list) {

                }
            });
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mBind.ivSvg!=null)
        {
            mBind.ivSvg.clear();
        }
        TimeCounter.getInstance().remove(timeListener);
    }
}
