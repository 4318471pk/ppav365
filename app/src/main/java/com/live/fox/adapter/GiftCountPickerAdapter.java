package com.live.fox.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.live.fox.R;

import java.util.List;


public class GiftCountPickerAdapter extends BaseQuickAdapter<String, BaseViewHolder> {


    public GiftCountPickerAdapter(List data) {
        super(R.layout.item_popup_count_picker, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, String data) {
        if (data == null) {
            return;
        }
        helper.setText(R.id.tv_item_count_picker, data);
    }
//    private Context mContext;
//    private OnItemClickListener listener;
//
//    public GiftCountPickerAdapter(Context context) {
//        super(new ArrayList<String>());
//        this.mContext = context;
//    }
//
//
//
//    @Override
//    public GiftCountHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        return new GiftCountHolder(LayoutInflater.from(mContext).inflate(R.layout.item_popup_count_picker, parent, false));
//    }
//
//    @Override
//    public void onBindViewHolder(GiftCountHolder holder, final int position, final String data) {
//        if (data == null) {
//            return;
//        }
//        holder.tv_item_count_picker.setText(data);
//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (listener != null) {
//                    listener.onItemClick(GiftCountPickerAdapter.this, data, v, position);
//                }
//            }
//        });
//    }


}
