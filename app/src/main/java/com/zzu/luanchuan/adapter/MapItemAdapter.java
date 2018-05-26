package com.zzu.luanchuan.adapter;

import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zzu.luanchuan.R;
import com.zzu.luanchuan.beans.AddressItem;


public class MapItemAdapter extends BaseQuickAdapter<AddressItem, BaseViewHolder> {
    public MapItemAdapter() {
        super(R.layout.address_item);
    }
    @Override
    protected void convert(BaseViewHolder helper, AddressItem item) {
        helper.setText(R.id.map_item_title, item.getTitle())
                .setText(R.id.map_item_content, item.getContent());
        ((View) helper.getView(R.id.is_selected)).setVisibility(item.isIs_selected()?View.VISIBLE:View.GONE);

    }
}
