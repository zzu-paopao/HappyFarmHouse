package com.zzu.luanchuan.customed_view.search_with_history;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

public class SearchHistoryListView extends ListView {
    public SearchHistoryListView(Context context) {
        super(context);
    }

    public SearchHistoryListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SearchHistoryListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    //通过复写其onMeasure方法、达到对ScrollView适配的效果
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }

}
