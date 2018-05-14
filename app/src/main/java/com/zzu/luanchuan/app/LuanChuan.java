package com.zzu.luanchuan.app;

import android.app.Application;
import android.content.Context;
import android.net.Uri;
import android.widget.ImageView;

import com.goyourfly.multi_picture.ImageLoader;
import com.goyourfly.multi_picture.MultiPictureView;
import com.goyourfly.vincent.Vincent;
import com.zzu.luanchuan.R;
import com.zzu.luanchuan.utils.StorageUtils;

public class LuanChuan extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        MultiPictureView.setImageLoader(new ImageLoader() {
            @Override
            public void loadImage(ImageView imageView, Uri uri) {
                Vincent.with(imageView.getContext())
                        .load(uri)
                        .placeholder(R.drawable.loading)
                        .error(R.drawable.loading)
                        .into(imageView);
            }
        });

        StorageUtils.initial_storage();
    }


}
