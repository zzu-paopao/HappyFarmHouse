package com.zzu.luanchuan.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.goyourfly.vincent.Vincent;
import com.zzu.luanchuan.R;
import com.zzu.luanchuan.adapter.BigImageAdapter;
import com.zzu.luanchuan.customed_view.SmoothImageView;

import java.util.ArrayList;

@SuppressWarnings("all")
public class BigImage extends Base {
    private ArrayList<Uri> mUris;
    private int mPosition;
    private int mLocationX;
    private int mLocationY;
    private int mWidth;
    private int mHeight;
    private SmoothImageView imageView;
    private ViewPager big_image_view_pager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.big_image);
        big_image_view_pager = $(R.id.big_image_view_pager);
        Intent intent = getIntent();
        mUris = intent.getParcelableArrayListExtra("uris");
        mPosition = intent.getIntExtra("position", 0);
        mLocationX = intent.getIntExtra("locationX", 0);
        mLocationY = intent.getIntExtra("locationY", 0);
        mWidth = intent.getIntExtra("width", 0);
        mHeight = intent.getIntExtra("height", 0);

        ArrayList<SmoothImageView> mlist = new ArrayList<SmoothImageView>();
        for (int i = 0; i < mUris.size(); i++) {
            if (i == mPosition) {
                imageView = new SmoothImageView(this);
                imageView.setOriginalInfo(mWidth, mHeight, mLocationX, mLocationY);
                imageView.transformIn();
                imageView.setLayoutParams(new ViewGroup.LayoutParams(-1, -1));
                imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                mlist.add(imageView);
            } else {
                imageView = new SmoothImageView(this);
                imageView.setOriginalInfo(mWidth, mHeight, mLocationX, mLocationY);

                imageView.setLayoutParams(new ViewGroup.LayoutParams(-1, -1));
                imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                mlist.add(imageView);
            }
        }


        BigImageAdapter bia = new BigImageAdapter(mlist, mUris,BigImage.this);
        big_image_view_pager.setAdapter(bia);
        big_image_view_pager.setCurrentItem(mPosition, true);

    }
}
