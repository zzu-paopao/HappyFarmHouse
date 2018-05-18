package com.zzu.luanchuan.activity;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;

import com.luseen.luseenbottomnavigation.BottomNavigation.BottomNavigationItem;
import com.luseen.luseenbottomnavigation.BottomNavigation.BottomNavigationView;
import com.luseen.luseenbottomnavigation.BottomNavigation.OnBottomNavigationItemClickListener;
import com.zzu.luanchuan.R;
import com.zzu.luanchuan.adapter.MainPagerAdapter;
import com.zzu.luanchuan.constant.Constants;
import com.zzu.luanchuan.fragment.IndexPage;
import com.zzu.luanchuan.fragment.Test;

import java.util.ArrayList;

public class Main extends Base {


    private BottomNavigationView bottomNavigationView;


    private ViewPager main_pager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);


        bottomNavigationView = findViewById(R.id.bottomNavigation);
        main_pager = findViewById(R.id.main_pager);
        int[] image = {R.drawable.index, R.drawable.bargen,
                R.drawable.me};
        int[] color = {ContextCompat.getColor(this, R.color.firstColor), ContextCompat.getColor(this, R.color.secondColor),
                ContextCompat.getColor(this, R.color.thirdColor)};



        if (bottomNavigationView != null) {
            bottomNavigationView.isWithText(true);//点击之前有字   没有字
            // bottomNavigationView.activateTabletMode();
            bottomNavigationView.isColoredBackground(false);//是否启用背景色
            bottomNavigationView.setTextActiveSize(getResources().getDimension(R.dimen.text_active));
            bottomNavigationView.setTextInactiveSize(getResources().getDimension(R.dimen.text_inactive));
            bottomNavigationView.setItemActiveColorWithoutColoredBackground(ContextCompat.getColor(this, R.color.secondColor));
            bottomNavigationView.setFont(Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/Noh_normal.ttf"));
        }
        init();
        bottomNavigationView.setUpWithViewPager(main_pager,color,image);

//        BottomNavigationItem bottomNavigationItem = new BottomNavigationItem
//                (Constants.NAMES_OF_BOTTOM_TABS[0], color[0], image[0]);
//        BottomNavigationItem bottomNavigationItem1 = new BottomNavigationItem
//                (Constants.NAMES_OF_BOTTOM_TABS[1], color[1], image[1]);
//        BottomNavigationItem bottomNavigationItem2 = new BottomNavigationItem
//                (Constants.NAMES_OF_BOTTOM_TABS[2], color[2], image[2]);
//        BottomNavigationItem bottomNavigationItem3 = new BottomNavigationItem
//                ("GitHub", color[3], image[3]);


//        bottomNavigationView.addTab(bottomNavigationItem);
//        bottomNavigationView.addTab(bottomNavigationItem1);
//        bottomNavigationView.addTab(bottomNavigationItem2);



    }

    void init() {
        ArrayList<Fragment> list=new ArrayList<>();

        Bundle bundle1=new Bundle();


        Fragment fg1= new IndexPage();

        Bundle bundle2=new Bundle();

        bundle2.putInt("pager_num",2);
        Fragment fg2=Test.newInstance(bundle2);

        Bundle bundle3=new Bundle();

        bundle3.putInt("pager_num",3);
        Fragment fg3=Test.newInstance(bundle3);



        list.add(fg1);
        list.add(fg2);
        list.add(fg3);


        main_pager.setAdapter(new MainPagerAdapter(getSupportFragmentManager(),list,Constants.NAMES_OF_BOTTOM_TABS));
        main_pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                bottomNavigationView.selectTab(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }
}
