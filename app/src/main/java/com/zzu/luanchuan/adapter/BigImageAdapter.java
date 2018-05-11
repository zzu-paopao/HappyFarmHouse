package com.zzu.luanchuan.adapter;

import android.app.Activity;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.goyourfly.vincent.Vincent;
import com.zzu.luanchuan.R;

import com.zzu.luanchuan.activity.BigImage;
import com.zzu.luanchuan.customed_view.SmoothImageView;

import java.util.ArrayList;

public class BigImageAdapter extends PagerAdapter {
    private ArrayList<SmoothImageView> mlist;
    private ArrayList<Uri> mUris;
    private Activity activity;

    public BigImageAdapter(ArrayList<SmoothImageView> mlist, ArrayList<Uri> mUris, Activity activity) {
        this.mlist = mlist;
        this.mUris = mUris;
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return mlist.size();

    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position,
                            @NonNull Object object) {
        container.removeView(mlist.get(position));

    }

    @Override
    public int getItemPosition(Object object) {

        return super.getItemPosition(object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(mlist.get(position));
        final SmoothImageView imageView = mlist.get(position);

        Vincent.with(imageView.getContext())
                .load(mUris.get(position))
                .placeholder(R.drawable.loading)
                .error(R.drawable.loading)
                .into(imageView);


        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                imageView.transformOut();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(200);
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    activity.finish();
                                    activity.overridePendingTransition(0, 0);
                                }
                            });
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();

            }
        });


        return imageView;
    }

}
