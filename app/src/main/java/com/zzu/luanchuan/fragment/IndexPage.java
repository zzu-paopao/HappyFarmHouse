package com.zzu.luanchuan.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.youth.banner.Banner;
import com.youth.banner.loader.ImageLoader;
import com.zzu.luanchuan.R;
import com.zzu.luanchuan.activity.Search;
import com.zzu.luanchuan.adapter.QuickAdapter;
import com.zzu.luanchuan.beans.BannerItem;
import com.zzu.luanchuan.beans.Movie;
import com.zzu.luanchuan.constant.Constants;
import com.zzu.luanchuan.utils.StatusBarUtil;

import java.util.ArrayList;
import java.util.List;

import static android.support.v7.widget.DividerItemDecoration.VERTICAL;

public class IndexPage extends Base {
    private QuickAdapter mAdapter;
    private RecyclerView recyclerView;
    private FloatingActionButton fab;
    private Toolbar toolbar;
    private RefreshLayout refreshLayout;
    private ImageView goto_search;
    public static List<BannerItem> BANNER_ITEMS = new ArrayList<BannerItem>() {{
        add(new BannerItem("最后的骑士", R.mipmap.image_movie_header_48621499931969370));
        add(new BannerItem("三生三世十里桃花", R.mipmap.image_movie_header_12981501221820220));
        add(new BannerItem("豆福传", R.mipmap.image_movie_header_12231501221682438));
    }};


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.index_page, container, false);

        initialize_view(view);
        return view;
    }

    private void initialize_view(View v) {
        final List<Movie> movies = new Gson().fromJson(Constants.JSON_MOVIES, new TypeToken<ArrayList<Movie>>() {
        }.getType());
        mAdapter = new QuickAdapter();
        mAdapter.replaceData(movies);


        recyclerView = $(R.id.recyclerView, v);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), VERTICAL));
        LinearLayoutManager lm = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(lm);
        recyclerView.setAdapter(mAdapter);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                //获得recyclerView的线性布局管理器
                LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
                //获取到第一个item的显示的下标  不等于0表示第一个item处于不可见状态 说明列表没有滑动到顶部 显示回到顶部按钮
                int firstVisibleItemPosition = manager.findFirstVisibleItemPosition();
                // 当不滚动时
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    // 判断是否滚动超过一屏
                    if (firstVisibleItemPosition <= 5) {
                        fab.hide();
                    } else {
                        fab.show();
                    }
                    //获取RecyclerView滑动时候的状态
                } else if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {//拖动中

                }
            }
        });


        refreshLayout = $(R.id.refreshLayout, v);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull final RefreshLayout refreshLayout) {
                refreshLayout.getLayout().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (mAdapter.getItemCount() < 2) {
                            List<Movie> movies = new Gson().fromJson(Constants.JSON_MOVIES, new TypeToken<ArrayList<Movie>>() {
                            }.getType());
                            mAdapter.replaceData(movies);
                        }
                        refreshLayout.finishRefresh();
                    }
                }, 2000);
            }
        });
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                mAdapter.addData(movies);
                refreshLayout.finishLoadMoreWithNoMoreData();
            }
        });


        Banner banner = (Banner) (LayoutInflater.from(getActivity()).inflate(R.layout.listitem_movie_header, recyclerView, false));

        banner.setImageLoader(new ImageLoader() {

            @Override
            public void displayImage(Context context, Object path, ImageView imageView) {
                imageView.setImageResource(((BannerItem) path).pic);
            }

        });
        banner.setImages(BANNER_ITEMS);
        banner.start();
        mAdapter.addHeaderView(banner);
        mAdapter.openLoadAnimation();


        toolbar = $(R.id.toolbar, v);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        //状态栏透明和间距处理
        StatusBarUtil.immersive(getActivity());
        StatusBarUtil.setPaddingSmart(getActivity(), toolbar);
        StatusBarUtil.setPaddingSmart(getActivity(), recyclerView);
        StatusBarUtil.setMargin(getActivity(), $(R.id.header, v));
        StatusBarUtil.setPaddingSmart(getActivity(), $(R.id.blurView, v));


        fab = $(R.id.fab, v);
        fab.hide();
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerView.smoothScrollToPosition(0);
            }
        });

        goto_search = $(R.id.goto_search,v);
        goto_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), Search.class));
            }
        });
    }


}
