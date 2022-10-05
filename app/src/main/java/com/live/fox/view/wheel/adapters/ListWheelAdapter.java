package com.live.fox.view.wheel.adapters;

import android.content.Context;

import java.util.List;

public class ListWheelAdapter<T extends String> extends AbstractWheelTextAdapter{

    // items
    private List<T> items;

    /**
     * Constructor
     * @param context the current context
     * @param items the items
     */
    public ListWheelAdapter(Context context, List<T> items) {
        super(context);

        //setEmptyItemResource(TEXT_VIEW_ITEM_RESOURCE);
        this.items = items;
    }

    @Override
    public CharSequence getItemText(int index) {
        if (index >= 0 && index < items.size()) {
            if (items.get(index) instanceof String) {
                return (String) items.get(index);
            }
            return items.get(index).toString();
        }
        return null;
    }

    @Override
    public int getItemsCount() {
        return items.size();
    }
}
