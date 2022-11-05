package com.live.fox.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.live.fox.R;
import com.live.fox.utils.ResourceUtils;

import org.jetbrains.annotations.NotNull;

public class RankProfileView extends RelativeLayout {

    public static final int NONE=-1;
    int crownIndex = -1;
    int decorationIndex=-1;
    int crownsResource[] = new int[]{R.mipmap.icon_top1, R.mipmap.icon_top2, R.mipmap.icon_top3};
    int[] decorationResource = null;
    float scaleAndMargins[][]={{0.87f,0.14f},{0.84f,0.05f},{0.87f,0.12f},{0.85f,0.07f},{0.87f,0.3f},{0.85f,0.1f},{0.85f,0.08f}};
    ImageView ivDecoration, ivCrown, ivProfile;
    OnConfirmWidthAndHeightListener onConfirmWidthAndHeightListener;


    public RankProfileView(@NonNull @NotNull Context context, int crownIndex) {
        super(context);
        initView(crownIndex,-1);
    }

    public RankProfileView(@NonNull @NotNull Context context, int crownIndex, int decorationIndex) {
        super(context);
        initView(crownIndex,decorationIndex);
    }

    public RankProfileView(@NonNull @NotNull Context context) {
        this(context,null);
    }

    public RankProfileView(@NonNull @NotNull Context context,  AttributeSet attrs) {
        this(context, attrs,0);
    }

    public RankProfileView(@NonNull @NotNull Context context,  AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        if(attrs!=null)
        {
            final TypedArray array =
                    context.obtainStyledAttributes(attrs, R.styleable.RankProfileView);
            crownIndex = array.getDimensionPixelSize(R.styleable.RankProfileView_crownIndex, -1);
            decorationIndex = array.getDimensionPixelSize(R.styleable.RankProfileView_decorationIndex, -1);
            array.recycle();
        }

        initView(crownIndex,decorationIndex);
    }

    public void setOnConfirmWidthAndHeightListener(OnConfirmWidthAndHeightListener onConfirmWidthAndHeightListener) {
        this.onConfirmWidthAndHeightListener = onConfirmWidthAndHeightListener;
    }

    private void initView(int crownIndex, int decorationIndex) {
        decorationResource=new ResourceUtils().getResourcesID(R.array.rankEdgePics);
        this.crownIndex = crownIndex;
        this.decorationIndex=decorationIndex;
        View view = View.inflate(getContext(), R.layout.view_rank_profile, null);
        ivProfile = view.findViewById(R.id.ivProfile);
        ivCrown = view.findViewById(R.id.ivCrown);
        ivDecoration = view.findViewById(R.id.ivDecoration);
        addView(view);
        view.post(new Runnable() {
            @Override
            public void run() {
                adjustLayout();
            }
        });

    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

    }

    public void setIndex(int crownIndex,int decorationIndex)
    {
        this.crownIndex = crownIndex;
        this.decorationIndex = decorationIndex;
        adjustLayout();
    }

    private void adjustLayout()
    {
        if (getWidth() > 0) {

            if(decorationIndex>-1)
            {
                ivDecoration.setImageDrawable(getResources().getDrawable(decorationResource[decorationIndex]));
            }
            else
            {
                return;
            }

            Drawable drawable = crownIndex > -1 && crownIndex < 3 ? getResources().getDrawable(crownsResource[crownIndex]) : null;
            if (drawable == null) {
                ivCrown.setVisibility(GONE);
                RelativeLayout.LayoutParams rlDe = (RelativeLayout.LayoutParams) ivDecoration.getLayoutParams();
                rlDe.width=getWidth();
                ivDecoration.setLayoutParams(rlDe);

                RelativeLayout.LayoutParams rlProfile = (RelativeLayout.LayoutParams) ivProfile.getLayoutParams();
                rlProfile.width=(int)(getWidth()*scaleAndMargins[decorationIndex][0]);
                rlProfile.height=(int)(getWidth()*scaleAndMargins[decorationIndex][0]);
                rlProfile.topMargin=(int)(getHeight()*scaleAndMargins[decorationIndex][1]);
                ivProfile.setLayoutParams(rlProfile);
                if(onConfirmWidthAndHeightListener!=null)
                {
                    onConfirmWidthAndHeightListener.onValue(rlProfile.width,rlProfile.height+rlProfile.topMargin);
                }

            } else {
                ivCrown.setImageDrawable(drawable);
                int crownDrawableWidth=drawable.getIntrinsicWidth();
                int crownDrawableHeight=drawable.getIntrinsicHeight();

                RelativeLayout.LayoutParams rl=(RelativeLayout.LayoutParams) ivCrown.getLayoutParams();
                rl.width=getWidth()/2;
                int crownHeight=crownDrawableHeight*rl.width/crownDrawableWidth;
                rl.height=crownHeight;
                ivCrown.setLayoutParams(rl);

                RelativeLayout.LayoutParams rlDe = (RelativeLayout.LayoutParams) ivDecoration.getLayoutParams();
                rlDe.width=getWidth();
                rlDe.topMargin=crownHeight/2;
                ivDecoration.setLayoutParams(rlDe);

                RelativeLayout.LayoutParams rlProfile = (RelativeLayout.LayoutParams) ivProfile.getLayoutParams();
                rlProfile.topMargin=crownHeight/2+(int)(getHeight()*scaleAndMargins[decorationIndex][1]);
                rlProfile.width=(int)(getWidth()*scaleAndMargins[decorationIndex][0]);
                rlProfile.height=(int)(getWidth()*scaleAndMargins[decorationIndex][0]);
                ivProfile.setLayoutParams(rlProfile);

                if(onConfirmWidthAndHeightListener!=null)
                {
                    onConfirmWidthAndHeightListener.onValue(rlProfile.width,rlProfile.height+rlProfile.topMargin);
                }
//                ivProfile.setPadding(0,crownHeight/2,0,0);

            }

//                    int padding=(int)( getWidth()*0.08f);
//                    ivProfile.setPadding(padding,padding,padding,padding);

        }
    }

    public interface OnConfirmWidthAndHeightListener
    {
        void onValue(int width,int height);
    }
}
