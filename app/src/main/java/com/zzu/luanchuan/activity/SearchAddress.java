package com.zzu.luanchuan.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.amap.api.maps.model.LatLng;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.github.mmin18.widget.RealtimeBlurView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.zzu.luanchuan.R;
import com.zzu.luanchuan.adapter.MapSearchItemAdapter;
import com.zzu.luanchuan.beans.AddressItem;
import com.zzu.luanchuan.utils.KeyBoardUtils;
import com.zzu.luanchuan.utils.MyToast;
import com.zzu.luanchuan.utils.StatusBarUtil;

import java.util.ArrayList;
import java.util.List;

public class SearchAddress extends Base implements PoiSearch.OnPoiSearchListener {
    private RecyclerView address_suggestion;
    private LinearLayout search_head;
    private ImageView search_back;
    private ImageView search_go;
    private EditText et_search;
    private RealtimeBlurView blur_view;
    private ArrayList<AddressItem> mDatas = new ArrayList<>();
    private MapSearchItemAdapter mapSearchItemAdapter;
    private int currentPage;
    private PoiSearch.Query query;// Poi查询条件类
    private PoiSearch poiSearch;
    private LatLonPoint lp;//
    private PoiResult poiResult; // poi返回的结果
    private List<PoiItem> poiItems;// poi数据
    private boolean is_checking_alive = false;
    private boolean is_loading_more = false;
    private RefreshLayout refreshLayout;
    private Runnable search_background = new Runnable() {
        @Override
        public void run() {
            String previous = "";
            int i = 0;

            while (is_checking_alive) {
                String keywords = et_search.getText().toString().trim();

                if(!"".equals(keywords)){
                    if(keywords.equals(previous)){
                        i++;
                        if(i>10){
                            is_checking_alive = false;
                            break;

                        }
                    }else {
                        if(!keywords.contains("\'")){
                            doSearchQueryWithKeyWord(keywords);
                            previous = keywords;
                        }

                    }

                }else {
                    i++;
                    if(i>10){
                        is_checking_alive = false;
                        break;

                    }
                }
                try {
                    Thread.sleep(800);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    };


    Runnable load_more = new Runnable() {
        @Override
        public void run() {
            int i = 0;
            while (is_loading_more) {
             if(!mDatas.isEmpty()){

                 is_loading_more = false;
                 SearchAddress.this.runOnUiThread(new Runnable() {
                     @Override
                     public void run() {
                         mapSearchItemAdapter.addData(mDatas);
                         refreshLayout.finishLoadMore();

                     }
                 });
                 break;

             }else {
                 i++;
                 if(i>5){
                     is_loading_more = false;
                     currentPage--;
                     SearchAddress.this.runOnUiThread(new Runnable() {
                         @Override
                         public void run() {
                           MyToast.showToast(SearchAddress.this,"网络缓慢！！");
                         }
                     });
                     break;
                 }



             }
                try {
                    Thread.sleep(800);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }
    };



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_address);
        bind_view();
        init_view();
    }

    @Override
    protected void onPause() {
        super.onPause();
        is_checking_alive = false;//关闭轮询线程
    }

    private void bind_view() {
        refreshLayout = $(R.id.search_refreshLayout);
        address_suggestion = $(R.id.address_suggestion);
        search_head = $(R.id.search_head);
        search_back = $(R.id.search_back);
//        search_go = $(R.id.search_go);
        et_search = $(R.id.et_search);
        blur_view = $(R.id.blurView);

    }


    private void init_view() {
        refreshLayout.setEnableRefresh(false);

        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                String s = et_search.getText().toString().trim();
                if(!"".equals(s)){
                    mDatas.clear();
                    is_loading_more = true;
                    currentPage++;
                    doLoadMoreWithKeyWord(s);
                    new Thread(load_more).start();
                }

            }
        });
        LatLng point = (LatLng) getIntent().getParcelableExtra("point");
        lp = new LatLonPoint(point.latitude, point.longitude);


        et_search.setTextSize(17.0f);
        et_search.setTextColor(getResources().getColor(android.R.color.black));
        et_search.setHint("请输入位置关键字");


        FrameLayout.LayoutParams params1 = (FrameLayout.LayoutParams) blur_view.getLayoutParams();
        params1.height = 150;
        blur_view.setLayoutParams(params1);
        StatusBarUtil.immersive(this);
        StatusBarUtil.setPaddingSmart(this, blur_view);


        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) search_head.getLayoutParams();
        params.height = 150;
//        search_head.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        search_head.setLayoutParams(params);
        StatusBarUtil.setPaddingSmart(this, search_head);


        search_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        if (Build.VERSION.SDK_INT >= 19) {
            SmartRefreshLayout.LayoutParams llp = (SmartRefreshLayout.LayoutParams) address_suggestion.getLayoutParams();
            if (llp != null && llp.height > 0) {
                llp.height += StatusBarUtil.getStatusBarHeight(this);//增高
            }
            address_suggestion.setPadding(address_suggestion.getPaddingLeft(), address_suggestion.getPaddingTop() + StatusBarUtil.getStatusBarHeight(this) + 150,
                    address_suggestion.getPaddingRight(), address_suggestion.getPaddingBottom());
        }


        LinearLayoutManager layoutManager = new LinearLayoutManager(SearchAddress.this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        address_suggestion.setLayoutManager(layoutManager);

        mapSearchItemAdapter = new MapSearchItemAdapter();
        address_suggestion.setAdapter(mapSearchItemAdapter);
        mapSearchItemAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent();

                AddressItem addressItem = mapSearchItemAdapter.getData().get(position);
                intent.putExtra("title", addressItem.getTitle());
                intent.putExtra("content", addressItem.getContent());
                intent.putExtra("point", addressItem.getPoint());
                setResult(RESULT_OK, intent);
                finish();

            }

        });
//        search_go.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//               String keywords =  et_search.getText().toString().trim();
//
//               if(keywords!=null&&!"".equals(keywords)){
//                   KeyBoardUtils.closeKeybord(et_search,SearchAddress.this);
//                   doSearchQueryWithKeyWord(keywords);
//               }else {
//                   MyToast.showToast(SearchAddress.this,"请输入关键词");
//               }
//
//            }
//        });


        et_search.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                    String keywords = et_search.getText().toString().trim();

                    if (keywords != null && !"".equals(keywords)) {
                        KeyBoardUtils.closeKeybord(et_search, SearchAddress.this);
                        doSearchQueryWithKeyWord(keywords);
                    } else {
                        MyToast.showToast(SearchAddress.this, "请输入关键词");
                    }
                }
                return false;
            }
        });

        et_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                String keywords = s.toString().trim();
                if (keywords != null && !"".equals(keywords)) {
//                    KeyBoardUtils.closeKeybord(et_search,SearchAddress.this);
                    //开启线程轮询keywords  如果不是空 就调用doSearchQueryWithKeyWord  异步查询
                    // 开启线程之前先判断原来的线程是不是死了，，如果没有就不开启新的线程
                    if(!is_checking_alive){
                        is_loading_more = false;
                        is_checking_alive = true;
                        new Thread(search_background).start();
                    }

//                    doSearchQueryWithKeyWord(keywords);
                } else {
                    mDatas.clear();
                    mapSearchItemAdapter.replaceData(mDatas);
                }


            }
        });


    }

    private void doSearchQueryWithKeyWord(String keywords) {

        currentPage = 0;
        query = new PoiSearch.Query(keywords, "", "郑州市");// 第一个参数表示搜索字符串，第二个参数表示poi搜索类型，第三个参数表示poi搜索区域（空字符串代表全国）
        query.setPageSize(20);// 设置每页最多返回多少条poiitem
        query.setPageNum(currentPage);// 设置查第一页
        query.setCityLimit(true); //限定城市

        if (lp != null) {
            poiSearch = new PoiSearch(this, query);
            poiSearch.setOnPoiSearchListener(this);   // 实现  onPoiSearched  和  onPoiItemSearched
            poiSearch.setBound(new PoiSearch.SearchBound(lp, 10000, true));//
            // 设置搜索区域为以lp点为圆心，其周围5000米范围
            poiSearch.searchPOIAsyn();// 异步搜索
        }


    }
    private void doLoadMoreWithKeyWord(String keywords) {


        query = new PoiSearch.Query(keywords, "", "郑州市");// 第一个参数表示搜索字符串，第二个参数表示poi搜索类型，第三个参数表示poi搜索区域（空字符串代表全国）
        query.setPageSize(20);// 设置每页最多返回多少条poiitem
        query.setPageNum(currentPage);// 设置查第一页
        query.setCityLimit(true); //限定城市

        if (lp != null) {
            poiSearch = new PoiSearch(this, query);
            poiSearch.setOnPoiSearchListener(this);   // 实现  onPoiSearched  和  onPoiItemSearched
            poiSearch.setBound(new PoiSearch.SearchBound(lp, 10000, true));//
            // 设置搜索区域为以lp点为圆心，其周围5000米范围
            poiSearch.searchPOIAsyn();// 异步搜索
        }


    }

    @Override
    public void onPoiSearched(PoiResult result, int rcode) {
        if (rcode == 1000) {
            if (result != null && result.getQuery() != null) {  // 搜索poi的结果
                if (result.getQuery().equals(query)) {// 是否是同一条


                    poiResult = result;
                    poiItems = poiResult.getPois();// 取得第一页的poiitem数据，页数从数字0开始

//                    List<SuggestionCity> suggestionCities = poiResult
//                            .getSearchSuggestionCitys();// 当搜索不到poiitem数据时，会返回含有搜索关键字的城市信息

                    if (poiItems.size() > 0) {
                        mDatas.clear();
                        //mDatas.add(mAddressTextFirst);// 第一个元素
                        AddressItem addressEntity = null;
                        for (int i = 0; i < poiItems.size(); i++) {
                            PoiItem poiItem = poiItems.get(i);
                            if (i == 0) {
                                addressEntity = new AddressItem(poiItem.getTitle(), poiItem.getSnippet(), poiItem.getLatLonPoint(), true);
                            } else {
                                addressEntity = new AddressItem(poiItem.getTitle(), poiItem.getSnippet(), poiItem.getLatLonPoint(), false);
                            }
//                        L.d("得到的数据 poiItem "
//                                + "\npoiItem.getSnippet()"+poiItem.getSnippet()
//                                + "\npoiItem.getAdCode()"+poiItem.getAdCode()
//                                + "\npoiItem.getAdName()"+poiItem.getAdName()
//                                + "\npoiItem.getDirection()"+poiItem.getDirection()
//                                + "\npoiItem.getBusinessArea()"+poiItem.getBusinessArea()
//                                + "\npoiItem.getCityCode()"+poiItem.getCityCode()
//                                + "\npoiItem.getEmail()"+poiItem.getEmail()
//                                + "\npoiItem.getParkingType()"+poiItem.getParkingType()
//                                + "\npoiItem.getCityName()"+poiItem.getCityName()
//                                + "\npoiItem.getProvinceName()"+poiItem.getProvinceName()
//                                + "\npoiItem.getSnippet()"+poiItem.getSnippet()
//                                + "\npoiItem.getTitle()"+poiItem.getTitle()
//                                + "\npoiItem.getTypeDes()"+poiItem.getTypeDes()
//                                + "\npoiItem.getDistance()"+poiItem.getDistance()
//                                + "\npoiItem.getWebsite()"+poiItem.getWebsite()
//                        );

                            mDatas.add(addressEntity);
                        }
                        String s = et_search.getText().toString().trim();
                        if ("".equals(s)) {
                            mDatas.clear();
                        }
                        if(!is_loading_more){
                            MyToast.showToast(SearchAddress.this,"is_loading_moreis_loading_more");
                            mapSearchItemAdapter.replaceData(mDatas);
                        }

                    } else {
                        MyToast.showToast(SearchAddress.this, "包含此关键词的信息较少，换个关键词搜索吧！");
                        mDatas.clear();
                        mapSearchItemAdapter.replaceData(mDatas);
                    }

                }
            } else {
                MyToast.showToast(SearchAddress.this, "没有找到相关数据！！");
            }
        }
    }

    @Override
    public void onPoiItemSearched(PoiItem poiItem, int i) {
    }
}
