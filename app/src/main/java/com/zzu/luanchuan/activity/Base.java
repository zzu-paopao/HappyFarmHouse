package com.zzu.luanchuan.activity;

import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

@SuppressWarnings("all")
public class Base extends AppCompatActivity {
    @SuppressWarnings("unchecked")
    protected <T extends View> T $(int resId) {
        return (T) super.findViewById(resId);
    }

    protected void showToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }
}
