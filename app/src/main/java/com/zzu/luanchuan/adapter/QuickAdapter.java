package com.zzu.luanchuan.adapter;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zzu.luanchuan.R;
import com.zzu.luanchuan.beans.Movie;

public class QuickAdapter extends BaseQuickAdapter<Movie, BaseViewHolder> {
    public QuickAdapter() {
        super(R.layout.listitem_movie_item);
    }
    @Override
    protected void convert(BaseViewHolder helper, Movie item) {
        helper.setText(R.id.lmi_title, item.filmName)
                .setText(R.id.lmi_actor, item.actors)
                .setText(R.id.lmi_grade, item.grade)
                .setText(R.id.lmi_describe, item.shortinfo);
        Glide.with(mContext).load(item.picaddr).into((ImageView) helper.getView(R.id.lmi_avatar));
    }
}
