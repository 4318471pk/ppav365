package com.tencent.demo.avatar.view;

import android.content.Context;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.tencent.demo.R;
import com.tencent.demo.avatar.model.AvatarConfigUI;
import com.tencent.demo.avatar.widget.AvatarSeekBarLayout;
import com.tencent.xmagic.AvatarAction;
import com.tencent.xmagic.avatar.AvatarData;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class AvatarApiExamplePanel extends LinearLayout {

    private AvatarUpdateCallback avatarUpdateCallback;
    private Context mContext;
    private Gson gson = new Gson();

    public void setAvatarUpdateCallback(AvatarUpdateCallback avatarUpdateCallback) {
        this.avatarUpdateCallback = avatarUpdateCallback;
    }

    public AvatarApiExamplePanel(Context context) {
        this(context, null);
    }

    public AvatarApiExamplePanel(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AvatarApiExamplePanel(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOrientation(VERTICAL);
        mContext = context;

    }


    private List<AvatarConfigUI> initData() {
        List<AvatarConfigUI> avatarConfigUIList = new ArrayList<>();

        avatarConfigUIList.add(new AvatarConfigUI(0, "脸型", "脸型0", createAvatarData("face_main", AvatarAction.SHAPE_VALUE, "{\"chin_length\":0,\"cheek_width\":0,\"chin_width\":0}")));
        avatarConfigUIList.add(new AvatarConfigUI(0, "", "脸型1", createAvatarData("face_main", AvatarAction.SHAPE_VALUE, "{\"chin_length\":0,\"cheek_width\":1.0,\"chin_width\":0}")));
        avatarConfigUIList.add(new AvatarConfigUI(0, "", "脸型2", createAvatarData("face_main", AvatarAction.SHAPE_VALUE, "{\"chin_length\":1.0,\"cheek_width\":0,\"chin_width\":0}")));
        avatarConfigUIList.add(new AvatarConfigUI(0, "", "脸型3", createAvatarData("face_main", AvatarAction.SHAPE_VALUE, "{\"chin_length\":0,\"cheek_width\":0,\"chin_width\":1.0}")));

        AvatarConfigUI skinUi1=new AvatarConfigUI(0, "肤色", "肤色1", createAvatarData("face_main", AvatarAction.CHANGE_TEXTURE, "{\"key\":\"baseColorMap\",\"switchKey\":\"baseColorEnableTexture\",\"switchValue\":true,\"value\":\"custom_configs/resources/skin_color/8d1d89911e2049380fecfaa465fbdb47/face_tex_base_3.png\"}"));
        List<AvatarData> bindAvatarList = new ArrayList<>();
        bindAvatarList.add(createAvatarData("neck", AvatarAction.CHANGE_TEXTURE,"{\"key\":\"baseColorMap\",\"switchKey\":\"baseColorEnableTexture\",\"switchValue\":true,\"value\":\"custom_configs/resources/neck_color/5998beb7d567f4c962645ff86cdf00dd/neck_tex_base_3.png\"}"));
        skinUi1.bindAvatarDataList = bindAvatarList;
        avatarConfigUIList.add(skinUi1);

        AvatarConfigUI skinUi2 = new AvatarConfigUI(0, "", "肤色2", createAvatarData("face_main", AvatarAction.CHANGE_TEXTURE, "{\"key\":\"baseColorMap\",\"switchKey\":\"baseColorEnableTexture\",\"switchValue\":true,\"value\":\"custom_configs/resources/skin_color/08ddde91e5e5bec67843e9dbdcb1cbc1/face_tex_base_5.png\"}"));
        List<AvatarData> bindAvatarList2 = new ArrayList<>();
        bindAvatarList2.add(createAvatarData("neck", AvatarAction.CHANGE_TEXTURE,"{\"key\":\"baseColorMap\",\"switchKey\":\"baseColorEnableTexture\",\"switchValue\":true,\"value\":\"custom_configs/resources/neck_color/57e0ad25c6ed16157e42a8e9cb2d901c/neck_tex_base_5.png\"}"));
        skinUi2.bindAvatarDataList = bindAvatarList2;
        avatarConfigUIList.add(skinUi2);


        AvatarConfigUI skinUi3 = new AvatarConfigUI(0, "", "肤色3", createAvatarData("face_main", AvatarAction.CHANGE_TEXTURE, "{\"key\":\"baseColorMap\",\"switchKey\":\"baseColorEnableTexture\",\"switchValue\":true,\"value\":\"custom_configs/resources/skin_color/51a3f2f01f5fef13573639d72e6c8c39/face_tex_base_0.png\"}"));
        List<AvatarData> bindAvatarList3 = new ArrayList<>();
        bindAvatarList3.add(createAvatarData("neck", AvatarAction.CHANGE_TEXTURE,"{\"key\":\"baseColorMap\",\"switchKey\":\"baseColorEnableTexture\",\"switchValue\":true,\"value\":\"custom_configs/resources/neck_color/437c371ef09d808309e6d03fbdb33a1d/neck_tex_base_0.png\"}"));
        skinUi3.bindAvatarDataList = bindAvatarList3;
        avatarConfigUIList.add(skinUi3);

        avatarConfigUIList.add(new AvatarConfigUI(0, "妆容", "无", createAvatarData("face_main", AvatarAction.CHANGE_TEXTURE, "{\"switchKey\":\"faceMakeupEnableTexture\",\"switchValue\":false}")));
        avatarConfigUIList.add(new AvatarConfigUI(0, "", "妆容1", createAvatarData("face_main", AvatarAction.CHANGE_TEXTURE, "{\"key\":\"faceMakeupMap\",\"switchKey\":\"faceMakeupEnableTexture\",\"switchValue\":true,\"value\":\"custom_configs/resources/face_makeup/7b93efa37c93b7284ff7110d3b63f17f/blush_tex_4.png\"}")));
        avatarConfigUIList.add(new AvatarConfigUI(0, "", "妆容2", createAvatarData("face_main", AvatarAction.CHANGE_TEXTURE, "{\"key\":\"faceMakeupMap\",\"switchKey\":\"faceMakeupEnableTexture\",\"switchValue\":true,\"value\":\"custom_configs/resources/face_makeup/66bd9fa5dba2814dfa2ec741f938567d/blush_tex_1.png\"}")));
        avatarConfigUIList.add(new AvatarConfigUI(0, "", "妆容3", createAvatarData("face_main", AvatarAction.CHANGE_TEXTURE, "{\"key\":\"faceMakeupMap\",\"switchKey\":\"faceMakeupEnableTexture\",\"switchValue\":true,\"value\":\"custom_configs/resources/face_makeup/836e07cea2e29a4b24c661e225f97876/blush_tex_2.png\"}")));

        Map<String, Float> value1 = new ArrayMap<>();
        value1.put("chin_width", 0.0f);
        avatarConfigUIList.add(new AvatarConfigUI(1, "微调", "下颌宽度", createAvatarData("face_main", AvatarAction.SHAPE_VALUE, gson.toJson(value1))));

        Map<String, Float> value2 = new ArrayMap<>();
        value2.put("chin_length", 0.0f);
        avatarConfigUIList.add(new AvatarConfigUI(1, "", "下巴高度", createAvatarData("face_main", AvatarAction.SHAPE_VALUE, gson.toJson(value2))));

        Map<String, Float> value3 = new ArrayMap<>();
        value3.put("cheek_width", 0.0f);
        avatarConfigUIList.add(new AvatarConfigUI(1, "", "脸颊宽度", createAvatarData("face_main", AvatarAction.SHAPE_VALUE, gson.toJson(value3))));

        avatarConfigUIList.add(new AvatarConfigUI(0, "发型", "无", createAvatarData("hair", AvatarAction.REPLACE, null)));
        avatarConfigUIList.add(new AvatarConfigUI(0, "", "发型1", createAvatarData("hair", AvatarAction.REPLACE, "{\"meshResourceKey\":\"custom_configs/resources/hair_style/557c3c39be5e1dade6ee3d66ea85e337/hair_05.fmesh\",\"position\":{\"x\":0,\"y\":0,\"z\":0},\"rotation\":{\"x\":0,\"y\":0,\"z\":0},\"scale\":{\"x\":1,\"y\":1,\"z\":1},\"subMeshConfigs\":[{\"materialResourceKeys\":[\"custom_configs/resources/hair_style/557c3c39be5e1dade6ee3d66ea85e337/hair_1.fmaterial\"]}]}")));
        avatarConfigUIList.add(new AvatarConfigUI(0, "", "发型2", createAvatarData("hair", AvatarAction.REPLACE, "{\"meshResourceKey\":\"custom_configs/resources/hair_style/888a5a15b18583b519d6d41a13a90182/hair_06.fmesh\",\"position\":{\"x\":0,\"y\":0,\"z\":0},\"rotation\":{\"x\":0,\"y\":0,\"z\":0},\"scale\":{\"x\":1,\"y\":1,\"z\":1},\"subMeshConfigs\":[{\"materialResourceKeys\":[\"custom_configs/resources/hair_style/888a5a15b18583b519d6d41a13a90182/hair_1.fmaterial\"]}]}")));

        avatarConfigUIList.add(new AvatarConfigUI(0, "发色", "发色1", createAvatarData("hair", AvatarAction.CHANGE_COLOR, "{\"key\":\"baseColorFactor\",\"value\":[18,20,24,255]}")));
        avatarConfigUIList.add(new AvatarConfigUI(0, "", "发色2", createAvatarData("hair", AvatarAction.CHANGE_COLOR, "{\"key\":\"baseColorFactor\",\"value\":[127,94,73,255]}")));
        avatarConfigUIList.add(new AvatarConfigUI(0, "", "发色3", createAvatarData("hair", AvatarAction.CHANGE_COLOR, "{\"key\":\"baseColorFactor\",\"value\":[43,26,23,255]}")));

        Map<String, Float> eyeValue = new ArrayMap<>();
        eyeValue.put("eye_spacing", 0.0f);
        avatarConfigUIList.add(new AvatarConfigUI(1, "微调", "眼距", createAvatarData("face_main,eyelashes", AvatarAction.SHAPE_VALUE, gson.toJson(eyeValue))));



        AvatarConfigUI glass1Ui = new AvatarConfigUI(0, "眼镜", "眼镜 无", createAvatarData("glass", AvatarAction.REPLACE, null));
        List<AvatarData> bindGlass1 = new ArrayList<>();
        bindGlass1.add(createAvatarData("lens", AvatarAction.REPLACE,null));
        glass1Ui.bindAvatarDataList = bindGlass1;
        avatarConfigUIList.add(glass1Ui);


        AvatarConfigUI glass1Ui2 = new AvatarConfigUI(0, "", "眼镜1", createAvatarData("glass", AvatarAction.REPLACE, "{\"meshResourceKey\":\"custom_configs/resources/glass/9509ae098561bed67215ae0fd74dd7b9/glass_02.fmesh\",\"position\":{\"x\":0,\"y\":0,\"z\":0},\"rotation\":{\"x\":0,\"y\":0,\"z\":0},\"scale\":{\"x\":1,\"y\":1,\"z\":1},\"subMeshConfigs\":[{\"materialResourceKeys\":[\"custom_configs/resources/glass/9509ae098561bed67215ae0fd74dd7b9/glass_2.fmaterial\"]}]}"));
        List<AvatarData> bindGlass2 = new ArrayList<>();
        bindGlass2.add(createAvatarData("lens", AvatarAction.REPLACE,"{\"meshResourceKey\":\"custom_configs/resources/lens/121df8e0932409f3249d58424641088a/glass_2_lens.fmesh\",\"position\":{\"x\":0,\"y\":0,\"z\":0},\"rotation\":{\"x\":0,\"y\":0,\"z\":0},\"scale\":{\"x\":1,\"y\":1,\"z\":1},\"subMeshConfigs\":[{\"materialResourceKeys\":[\"custom_configs/resources/lens/121df8e0932409f3249d58424641088a/glass_lens_2.fmaterial\"]}]}"));
        glass1Ui2.bindAvatarDataList = bindGlass2;
        avatarConfigUIList.add(glass1Ui2);

        return avatarConfigUIList;

    }

    private AvatarData createAvatarData(String entityName, AvatarAction avatarAction, String jsonValue) {
        AvatarData avatarData = new AvatarData();
        avatarData.entityName = entityName;
        avatarData.action = avatarAction.description;
        avatarData.value = gson.fromJson(jsonValue, JsonObject.class);
        return avatarData;
    }

    public void initViews() {
        RecyclerView recyclerView = new RecyclerView(mContext);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        RecycleViewAdapter recycleViewAdapter = new RecycleViewAdapter(initData(), avatarUpdateCallback);
        recyclerView.setAdapter(recycleViewAdapter);
        this.addView(recyclerView, new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }


    static class RecycleViewAdapter extends RecyclerView.Adapter {

        private List<AvatarConfigUI> avatarConfigUIList;
        private AvatarUpdateCallback avatarUpdateCallback;

        public RecycleViewAdapter(List<AvatarConfigUI> avatarConfigUIList, AvatarUpdateCallback avatarUpdateCallback) {
            this.avatarConfigUIList = avatarConfigUIList;
            this.avatarUpdateCallback = avatarUpdateCallback;
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            if (viewType == 0) {  //icon
                return new IconHolder(layoutInflater.inflate(R.layout.avatar_api_example_panel_icon_layout, parent, false));
            } else if (viewType == 1) { //slider
                return new SliderHolder(layoutInflater.inflate(R.layout.avatar_api_example_panel_slider_layout, parent, false));
            }
            return null;
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            AvatarConfigUI avatarConfigUI = avatarConfigUIList.get(position);
            if (holder instanceof IconHolder) {
                holder.itemView.setOnClickListener(v -> {
                    if (avatarUpdateCallback != null) {
                        avatarUpdateCallback.onUpdate(avatarConfigUI);
                    }
                });
                ((IconHolder) holder).labelTxt.setText(avatarConfigUI.itemName);
                if (TextUtils.isEmpty(avatarConfigUI.groupLabel)) {
                    ((IconHolder) holder).groupLabel.setText("");
                    ((IconHolder) holder).lineView.setVisibility(View.GONE);
                } else {
                    ((IconHolder) holder).groupLabel.setText(avatarConfigUI.groupLabel);
                    ((IconHolder) holder).lineView.setVisibility(View.VISIBLE);
                }

            } else if (holder instanceof SliderHolder) {
                ((SliderHolder) holder).avatarSeekBarLayout.setName(avatarConfigUI.itemName);
                AvatarData avatarData = avatarConfigUI.avatarData;
                String key = getKeyFormJsonObject(avatarConfigUI.avatarData.value);
                if (TextUtils.isEmpty(key)) {
                    return;
                }
                Double currentValue = avatarData.value.get(key).getAsDouble();
                ((SliderHolder) holder).avatarSeekBarLayout.setProgress(-100, 100, (int) (100 * currentValue));
                ((SliderHolder) holder).avatarSeekBarLayout.setName(avatarConfigUI.itemName);
                ((SliderHolder) holder).avatarSeekBarLayout.setOnSeekBarChangeListener((seekBar, progress, fromUser) -> {
                    double value = ((double) progress) / 100;
                    avatarData.value.addProperty(key, value);
                    if (avatarUpdateCallback != null) {
                        avatarUpdateCallback.onUpdate(avatarConfigUI);
                    }
                });
                if (TextUtils.isEmpty(avatarConfigUI.groupLabel)) {
                    ((SliderHolder) holder).groupLabel.setText("");
                    ((SliderHolder) holder).lineView.setVisibility(GONE);
                } else {
                    ((SliderHolder) holder).groupLabel.setText(avatarConfigUI.groupLabel);
                    ((SliderHolder) holder).lineView.setVisibility(View.VISIBLE);
                }
            }
        }

        @Override
        public int getItemCount() {
            return avatarConfigUIList == null ? 0 : avatarConfigUIList.size();
        }

        @Override
        public int getItemViewType(int position) {
            return avatarConfigUIList.get(position).uiType;

        }


        private String getKeyFormJsonObject(JsonObject jsonObject) {
            if (jsonObject != null) {
                Set<String> keys = jsonObject.keySet();
                if (keys == null) {
                    return null;
                }
                for (String key : keys) {
                    return key;
                }
            }
            return null;
        }
    }


    static class IconHolder extends RecyclerView.ViewHolder {

        TextView labelTxt;
        TextView groupLabel;
        View lineView;

        public IconHolder(@NonNull View itemView) {
            super(itemView);
            labelTxt = itemView.findViewById(R.id.label_txt);
            groupLabel = itemView.findViewById(R.id.group_label);
            lineView = itemView.findViewById(R.id.item_line_view);
        }
    }


    static class SliderHolder extends RecyclerView.ViewHolder {
        AvatarSeekBarLayout avatarSeekBarLayout;
        TextView groupLabel;
        View lineView;

        public SliderHolder(@NonNull View itemView) {
            super(itemView);
            groupLabel = itemView.findViewById(R.id.group_label);
            avatarSeekBarLayout = itemView.findViewById(R.id.slider_layout);
            lineView = itemView.findViewById(R.id.item_line_view);
        }
    }


    public interface AvatarUpdateCallback {
        void onUpdate(AvatarConfigUI avatarConfigUI);
    }
}
