package com.live.fox.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.live.fox.R;
import com.live.fox.utils.ResourceUtils;

//带有装饰边框和小图标是不是守护 房管
public class AnchorProtectorProfileView extends RelativeLayout {

    int decorationIndex=-1;
    int[] decorationResource = null;
    boolean isInit=false;
    ImageView ivDecoration, ivWings, ivProfile;
    float scaleAndMargins[][]={{0.87f,0.14f},{0.84f,0.05f},{0.87f,0.12f},{0.85f,0.07f},{0.87f,0.1f},{0.85f,0.1f},{0.85f,0.08f}};
    public AnchorProtectorProfileView(Context context) {
        this(context,null);
    }

    public AnchorProtectorProfileView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public AnchorProtectorProfileView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        if(attrs!=null)
        {
            final TypedArray array =
                    context.obtainStyledAttributes(attrs, R.styleable.AnchorProtectorProfileView);
            decorationIndex = array.getDimensionPixelSize(R.styleable.AnchorProtectorProfileView_ap_decorationIndex, -1);
            array.recycle();
        }

        initView();
    }

    public void setDecorationIndex(int decorationIndex) {
        if(decorationIndex<1 || decorationIndex>7)
        {
            decorationIndex=1;
        }
        this.decorationIndex = decorationIndex-1;
        adjustLayout();
    }

    private void initView() {
        decorationResource=new ResourceUtils().getResourcesID(R.array.rankEdgePics);
        View view = View.inflate(getContext(), R.layout.view_anchor_protector, null);
        ivProfile = view.findViewById(R.id.ivProfile);
        ivWings = view.findViewById(R.id.ivWings);
        ivDecoration = view.findViewById(R.id.ivDecoration);
        addView(view);
        view.post(new Runnable() {
            @Override
            public void run() {
                adjustLayout();
            }
        });

    }

    public ImageView getIvProfile() {
        return ivProfile;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if(!isInit)
        {
            adjustLayout();
        }
    }

    private void adjustLayout()
    {
        if(getWidth()>0 )
        {
            isInit=true;
            if(decorationIndex>-1)
            {
                Drawable decorationDrawable=getResources().getDrawable(decorationResource[decorationIndex]);
                int deDrawableWidth=decorationDrawable.getIntrinsicWidth();
                int deDrawableHeight=decorationDrawable.getIntrinsicHeight();

                ivDecoration.setImageDrawable(decorationDrawable);

                int viewWidth=ivWings.getDrawable().getIntrinsicWidth();
                int viewHeight=ivWings.getDrawable().getIntrinsicHeight();

                RelativeLayout.LayoutParams rlWings = (RelativeLayout.LayoutParams) ivWings.getLayoutParams();
                rlWings.width=getWidth();
                rlWings.height=rlWings.width*viewHeight/viewWidth;
                ivWings.setLayoutParams(rlWings);

                RelativeLayout.LayoutParams rlDe = (RelativeLayout.LayoutParams) ivDecoration.getLayoutParams();
                rlDe.width=(int)(getWidth()*0.5f);
                rlDe.height=rlDe.width*deDrawableHeight/deDrawableWidth;
                rlDe.topMargin=(int)(rlWings.height*0.3f);
                rlDe.addRule(RelativeLayout.CENTER_HORIZONTAL,RelativeLayout.TRUE);
                ivDecoration.setLayoutParams(rlDe);

                RelativeLayout.LayoutParams rlProfile = (RelativeLayout.LayoutParams) ivProfile.getLayoutParams();
                rlProfile.width=(int)(rlDe.width*scaleAndMargins[decorationIndex][0]);
                rlProfile.height=(int)(rlDe.width*scaleAndMargins[decorationIndex][0]);
                rlProfile.topMargin=(int)(rlDe.height*scaleAndMargins[decorationIndex][1])+rlDe.topMargin;
                ivProfile.setLayoutParams(rlProfile);

                getLayoutParams().height=rlDe.topMargin+rlDe.height;
            }
            else
            {
                Drawable decorationDrawable=getResources().getDrawable(R.mipmap.bg_guard_circle);
                int deDrawableWidth=decorationDrawable.getIntrinsicWidth();
                int deDrawableHeight=decorationDrawable.getIntrinsicHeight();

                ivDecoration.setImageDrawable(decorationDrawable);

                int viewWidth=ivWings.getDrawable().getIntrinsicWidth();
                int viewHeight=ivWings.getDrawable().getIntrinsicHeight();

                RelativeLayout.LayoutParams rlWings = (RelativeLayout.LayoutParams) ivWings.getLayoutParams();
                rlWings.width=getWidth();
                rlWings.height=rlWings.width*viewHeight/viewWidth;
                ivWings.setLayoutParams(rlWings);

                RelativeLayout.LayoutParams rlDe = (RelativeLayout.LayoutParams) ivDecoration.getLayoutParams();
                rlDe.width=(int)(getWidth()*0.5f);
                rlDe.height=rlDe.width*deDrawableHeight/deDrawableWidth;
                rlDe.topMargin=(int)(rlWings.height*0.3f);
                rlDe.addRule(RelativeLayout.CENTER_HORIZONTAL,RelativeLayout.TRUE);
                ivDecoration.setLayoutParams(rlDe);

                RelativeLayout.LayoutParams rlProfile = (RelativeLayout.LayoutParams) ivProfile.getLayoutParams();
                rlProfile.width=(int)(rlDe.width*0.95);
                rlProfile.height=(int)(rlDe.width*0.95);
                rlProfile.topMargin=(int)(rlDe.width*0.05)+rlDe.topMargin;
                ivProfile.setLayoutParams(rlProfile);

                getLayoutParams().height=rlDe.topMargin+rlDe.height;
            }
        }

    }

}
