package com.live.fox.view.bulletMessage;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.live.fox.R;
import com.live.fox.db.LocalMountResourceDao;
import com.live.fox.entity.LivingEnterLivingRoomBean;
import com.live.fox.entity.MountResourceBean;
import com.live.fox.utils.GlideUtils;
import com.live.fox.utils.ResourceUtils;
import com.live.fox.utils.SpanUtils;
import com.live.fox.utils.Strings;

public class VehicleFloatingView extends RelativeLayout {

    ImageView ivProfile,ivEdge,ivBG;
    TextView tvWelcome,tvVehicleTitle;
    int[] decorationResource = null;
    float scaleAndMargins[][]={{0.87f,0.14f},{0.84f,0.05f},{0.87f,0.12f},{0.85f,0.07f},{0.87f,0.1f},{0.85f,0.1f},{0.85f,0.08f}};

    public VehicleFloatingView(Context context, LivingEnterLivingRoomBean bean) {
        super(context);
        initView(bean);
    }

    public VehicleFloatingView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public VehicleFloatingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void initView(LivingEnterLivingRoomBean bean)
    {

        decorationResource=new ResourceUtils().getResourcesID(R.array.rankEdgePics);

        LayoutInflater.from(getContext()).inflate(R.layout.layout_vehicle_floating,this,true);
        ivBG=findViewById(R.id.ivBG);
        ivProfile=findViewById(R.id.ivProfile);
        ivEdge=findViewById(R.id.ivEdge);

        tvVehicleTitle=findViewById(R.id.tvVehicleTitle);
        tvWelcome=findViewById(R.id.tvWelcome);

        String welcome=getContext().getString(R.string.welcome);
        String vehicle=getContext().getString(R.string.tip12);

        if(bean!=null)
        {
            SpanUtils spanUtils=new SpanUtils();
            spanUtils.append(welcome).setBold().setFontSize(14,true);
            spanUtils.append(bean.getNickname()).setBold().setForegroundColor(0xffffea00).setFontSize(14,true);
            tvWelcome.setText(spanUtils.create());

            if(Strings.isDigitOnly(bean.getCarId()))
            {
                MountResourceBean mountResourceBean=LocalMountResourceDao.getInstance().getVehicleById(Long.valueOf(bean.getCarId()));
                if(mountResourceBean!=null)
                {
                    tvVehicleTitle.setText(String.format(vehicle,mountResourceBean.getName()));
                }
            }

            if(bean.getVipLevel()>0)
            {
                ivEdge.setImageResource(decorationResource[bean.getVipLevel()-1]);
                ivProfile.setScaleX(scaleAndMargins[bean.getVipLevel()-1][0]);
                ivProfile.setScaleY(scaleAndMargins[bean.getVipLevel()-1][0]);
            }

            GlideUtils.loadCircleImage(getContext(),bean.getAvatar(),
                    R.mipmap.user_head_error,R.mipmap.user_head_error,ivProfile);
        }
    }
}
