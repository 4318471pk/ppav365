package com.live.fox.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.live.fox.R;
import com.live.fox.utils.device.DeviceUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 仿IOS的 SheetDialog
 * https://github.com/skyline1631/ActionDialog
 * <p>
 * ActionDialog dialog = new ActionDialog(this);
 * dialog.setTitle("Title");
 * dialog.setMessage("Message");
 * dialog.addAction("Default");
 * dialog.addAction("Destructive", true);
 * dialog.setEventListener(this);
 * dialog.show();
 */
public class ActionDialog extends Dialog
        implements View.OnClickListener,
        AdapterView.OnItemClickListener {

    // INSTANCES
    private TextView mTextTitle = null;
    private TextView mTextMessage = null;
    private TextView mTextCancel = null;
    private LinearLayout mLayoutHeader = null;
    private RelativeLayout mLayoutCancel = null;
    private ListView mListViewContent = null;
    private ListItemAdapter mListItemAdapter = null;
    private OnEventListener mEventListener = null;
    private List<ActionItem> mListActionItems = new ArrayList<>();

    // CLASSES
    public static class ActionItem {
        public boolean destructive = false;
        public String title = null;
        public String imgUrl = null;
        public Object key = 0;

        public ActionItem() {
        }
    }

    private class ListItemAdapter extends ArrayAdapter<ActionItem> {
        private LayoutInflater mLayoutInflater = null;

        public ListItemAdapter(Context context, List<ActionItem> items) {
            super(context, 0, items);

            mLayoutInflater = LayoutInflater.from(getContext());
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = mLayoutInflater.inflate(R.layout.sheetdialog_list_item, parent, false);
            }

            final ActionItem item = getItem(position);
            final TextView textTitle = convertView.findViewById(R.id.sky_action_item_text_title);
            final ImageView logo = convertView.findViewById(R.id.sky_action_item_image);
            textTitle.setText(item.title);
            textTitle.setGravity(Gravity.CENTER);
            if (item.imgUrl != null) {
                logo.setVisibility(View.VISIBLE);
                textTitle.setGravity(Gravity.LEFT);
                Glide.with(getContext()).load(item.imgUrl).into(logo);
            }

            if (item.destructive) {
                textTitle.setTextColor(Color.parseColor("#ffff3b30")); //红色
            } else {
                textTitle.setTextColor(Color.parseColor("#ff007aff")); //蓝色
            }

            return convertView;
        }

        private int getColor(int colorId) {
            return getContext().getResources().getColor(colorId);
        }
    }

    // INTERFACES
    public interface OnEventListener {
        void onActionItemClick(ActionDialog dialog, ActionItem item, int position);

        void onCancelItemClick(ActionDialog dialog);
    }

    // IMPLEMENTS
    public ActionDialog(Context context, int page) {
        super(context, R.style.SkyActionDialog);

        setupViews();
        setupWindow(page);
    }

    @Override
    public void onClick(View v) {
        if (v == mLayoutCancel) {
            dismiss();

            if (mEventListener != null) {
                mEventListener.onCancelItemClick(this);
            }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (mEventListener != null) {
            mEventListener.onActionItemClick(this, mListActionItems.get(position), position);
        }

        dismiss();
    }

    /**
     * Set the title text for this dialog's window.
     *
     * @param text The new text to display in the title.
     */
    @Override
    public void setTitle(CharSequence text) {
        super.setTitle(text);

        setTitle(text.toString());
    }

    /**
     * Set the title text for this dialog's window. The text is retrieved
     * from the resources with the supplied identifier.
     *
     * @param textId The title's text resource identifier
     */
    @Override
    public void setTitle(int textId) {
        super.setTitle(textId);

        setTitle(getContext().getString(textId));
    }

    /**
     * Set the title text for this dialog's window. The text is retrieved
     * from the resources with the supplied identifier.
     *
     * @param text The new text to display in the title.
     */
    public void setTitle(String text) {
        if (!TextUtils.isEmpty(text)) {
            mTextTitle.setText(text);
            mTextTitle.setVisibility(View.VISIBLE);
        } else {
            mTextTitle.setVisibility(View.GONE);
        }

        updateViews();
    }

    /**
     * Set the message text for this dialog's window. The text is retrieved
     * from the resources with the supplied identifier.
     *
     * @param textId The message's text resource identifier
     */
    public void setMessage(int textId) {
        setMessage(getContext().getString(textId));
    }

    /**
     * Set the message text for this dialog's window.
     *
     * @param text The new text to display in the message.
     */
    public void setMessage(CharSequence text) {
        setMessage(text.toString());
    }

    /**
     * Set the message text for this dialog's window.
     *
     * @param text The new text to display in the message.
     */
    public void setMessage(String text) {
        if (!TextUtils.isEmpty(text)) {
            mTextMessage.setText(text);
            mTextMessage.setVisibility(View.VISIBLE);
        } else {
            mTextMessage.setVisibility(View.GONE);
        }

        updateViews();
    }

    /**
     * Set the cancel text for this dialog's window. The text is retrieved
     * from the resources with the supplied identifier.
     *
     * @param textId The cancel's text resource identifier
     */
    public void setCancelText(int textId) {
        setCancelText(getContext().getString(textId));
    }

    /**
     * Set the cancel text for this dialog's window.
     *
     * @param text The new text to display in the cancel action.
     */
    public void setCancelText(CharSequence text) {
        setCancelText(text.toString());
    }

    /**
     * Set the cancel text for this dialog's window.
     *
     * @param text The new text to display in the cancel action.
     */
    public void setCancelText(String text) {
        mTextCancel.setText(text);
    }

    /**
     * Sets whether the cancel action item should be visible.
     *
     * @param visible Whether the cancel action item should be visible
     */
    public void setCancelVisible(boolean visible) {
        ((View) mLayoutCancel.getParent()).setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    /**
     * Set the callback that will be called if an action item is clicked.
     *
     * @param listener The listener that will be called.
     */
    public void setEventListener(OnEventListener listener) {
        mEventListener = listener;
    }

    /**
     * Append the specified item to the end of the dialog.
     *
     * @param item The action item object to be appended
     */
    public void addAction(ActionItem item) {
        mListActionItems.add(item);
    }

    /**
     * Appends the specified item to the end of the dialog.
     *
     * @param textId The text resource identifier of the action item's title
     */
    public void addAction(int textId) {
        addAction(getContext().getString(textId));
    }

    /**
     * Append the specified item to the end of the dialog.
     *
     * @param title The title of the action item
     */
    public void addAction(String title) {
        addAction(title, null);
    }

    /**
     * Append the specified item to the end of the dialog.
     *
     * @param title The title of the action item
     * @param key   The key of the action item
     */
    public void addAction(String title, Object key) {
        ActionItem item = new ActionItem();
        item.title = title;
        item.key = key;
        mListActionItems.add(item);
        mListItemAdapter.notifyDataSetChanged();
    }

    /**
     * Append the specified item by string to the end of the list.
     *
     * @param title       The title of the action item
     * @param destructive Whether the action should be showed as destructive style
     */
    public void addAction(String title, boolean destructive) {
        ActionItem item = new ActionItem();
        item.title = title;
        item.destructive = destructive;
        mListActionItems.add(item);
        mListItemAdapter.notifyDataSetChanged();
    }

    /**
     * Set a list of action items to be displayed in the dialog.
     *
     * @param arrayId The text array resource identifier for the action titles
     */
    public void setActions(int arrayId) {
        setActions(getContext().getResources().getStringArray(arrayId));
    }

    /**
     * Set a list of action items to be displayed in the dialog.
     *
     * @param titles The text array for the action titles
     */
    public void setActions(List<ActionItem> titles) {
        mListActionItems.clear();
        mListActionItems = titles;
        mListItemAdapter.addAll(mListActionItems);
        mListItemAdapter.notifyDataSetChanged();
    }

    public void setActions(String[] titles) {
        mListActionItems.clear();

        for (String title : titles) {
            addAction(title);
        }
    }
//    public void setActions(List<BankInfoList> data) {
//        mListActionItems.clear();
//
//        for (BankInfoList bean : data) {
//            addAction(bean.getBankName());
//        }
//    }

    /**
     * Remove all of the action items from the dialog.
     */
    public void clearActions() {
        mListActionItems.clear();
        mListItemAdapter.notifyDataSetChanged();
    }

    private void setupViews() {
        final LayoutInflater inflater = LayoutInflater.from(getContext());
        final View viewContent = inflater.inflate(R.layout.sheetdialog_layout, null);
        setContentView(viewContent);

        // Header
        mLayoutHeader = viewContent.findViewById(R.id.sky_action_dialog_layout_header);
        mLayoutHeader.setVisibility(View.GONE);

        // Title
        mTextTitle = viewContent.findViewById(R.id.sky_action_dialog_text_title);
        mTextTitle.setVisibility(View.GONE);

        // Message
        mTextMessage = viewContent.findViewById(R.id.sky_action_dialog_text_message);
        mTextMessage.setVisibility(View.GONE);

        // Adapter
        mListItemAdapter = new ListItemAdapter(getContext(), mListActionItems);

        // List
        mListViewContent = viewContent.findViewById(R.id.sky_action_dialog_list_content);
        mListViewContent.setAdapter(mListItemAdapter);
        mListViewContent.setOnItemClickListener(this);

        // Cancel
        mLayoutCancel = viewContent.findViewById(R.id.sky_action_dialog_layout_cancel);
        mLayoutCancel.setOnClickListener(this);

        mTextCancel = viewContent.findViewById(R.id.sky_action_dialog_text_cancel);
    }

    private void updateViews() {
        if ((mTextTitle.getVisibility() == View.VISIBLE) ||
                (mTextMessage.getVisibility() == View.VISIBLE)) {
            mLayoutHeader.setVisibility(View.VISIBLE);
        } else {
            mLayoutHeader.setVisibility(View.GONE);
        }
    }

    private void setupWindow(int page) {
        final Window window = getWindow();
        final WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.width = getContext().getResources().getDisplayMetrics().widthPixels;
        if (1 == page) {
            layoutParams.height =DeviceUtils.dp2px(getContext(),190);
        } else if (2 == page) {
            layoutParams.height = DeviceUtils.dp2px(getContext(),400);
        }
        window.setAttributes(layoutParams);
        window.setGravity(Gravity.BOTTOM);
    }
}
