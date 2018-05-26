package com.zzu.luanchuan.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zzu.luanchuan.R;

public class Test extends Fragment {
    private Bundle arg;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        arg=getArguments();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.test,null);
        TextView t = view.findViewById(R.id.test_text);
        int page=arg.getInt("pager_num");

        if (page==1){
            t.setText("haha");
        }else if(page==2){
            t.setText("hehe");
        }else {
            t.setText("heihei");
        }


        return view;
    }


    public static Test newInstance(Bundle args) {
        Test fragment = new Test();
        fragment.setArguments(args);
        return fragment;
    }
}
