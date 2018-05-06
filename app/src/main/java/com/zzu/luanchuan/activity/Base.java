package com.zzu.luanchuan.activity;

import android.support.v7.app.AppCompatActivity;
import android.view.View;

@SuppressWarnings("all")
public class Base extends AppCompatActivity {
    @SuppressWarnings("unchecked")
    protected  <T extends View> T $(int resId) {
        return (T) super.findViewById(resId);
    }

}
