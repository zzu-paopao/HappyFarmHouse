package com.zzu.luanchuan.utils;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

@SuppressWarnings("all")
public class MyToast {
    public static void showToast(Context c, String text) {
        Toast.makeText(c, text, Toast.LENGTH_SHORT).show();
    }
    public static void showToastOnOtherThread(final Activity a, final String text) {
        a.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                showToast(a,text);
            }
        });

    }
}
