package com.zzu.luanchuan.fragment;

import android.support.v4.app.Fragment;
import android.view.View;

public class Base extends Fragment {
    protected <T extends View> T $(int resId,View v) {
        return (T) v.findViewById(resId);
    }
}
