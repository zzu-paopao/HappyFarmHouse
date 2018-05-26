package com.zzu.luanchuan.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

@SuppressWarnings("all")
public class MainPagerAdapter extends FragmentStatePagerAdapter {
ArrayList<Fragment> frag_list;
String[] titleContainer;
    public MainPagerAdapter(FragmentManager fm,ArrayList<Fragment> frag_list,String[] titleContainer) {
        super(fm);
        this.frag_list = frag_list;
        this.titleContainer  = titleContainer;

    }

    @Override
    public Fragment getItem(int position) {
        return frag_list.get(position);
    }

    @Override
    public int getCount() {
        return frag_list.size();
    }
    @Override
    public CharSequence getPageTitle(int position) {
        return titleContainer[position];
    }
}
