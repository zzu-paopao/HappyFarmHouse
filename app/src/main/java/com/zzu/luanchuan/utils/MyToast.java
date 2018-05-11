package com.zzu.luanchuan.utils;

import android.content.Context;
import android.widget.Toast;

@SuppressWarnings("all")
public class MyToast {
    public static void showToast(Context c, String text) {
        Toast.makeText(c, text, Toast.LENGTH_SHORT).show();
    }
}
