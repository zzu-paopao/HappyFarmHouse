package com.zzu.luanchuan.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;

import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.core.SuggestionCity;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.github.mmin18.widget.RealtimeBlurView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.zzu.luanchuan.R;
import com.zzu.luanchuan.adapter.MapItemAdapter;
import com.zzu.luanchuan.beans.AddressItem;
import com.zzu.luanchuan.beans.Movie;
import com.zzu.luanchuan.constant.Constants;
import com.zzu.luanchuan.utils.BitMapUtils;
import com.zzu.luanchuan.utils.MyToast;
import com.zzu.luanchuan.utils.StatusBarUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@SuppressWarnings("all")
public class MapPicker extends Base implements AMap.OnMapClickListener, AMap.OnCameraChangeListener, GeocodeSearch.OnGeocodeSearchListener, PoiSearch.OnPoiSearchListener {
    MapView mMapView;
    MyLocationStyle myLocationStyle;
    private View center_marker;
    private RealtimeBlurView blurView;
    private int searchBlockHeight = 150;
    private LinearLayout search_map; // 搜索框布局
    private ImageView ic_back;
    private ImageView sure;
    private TextView goto_search_map;
    private Animation show_loc;
    private RefreshLayout refreshLayout;
    private Marker dynamic_marker = null;

    private MapView mapview;
    private AMap mAMap;
    private PoiResult poiResult; // poi返回的结果
    private int currentPage = 0;// 当前页面，从0开始计数
    private PoiSearch.Query query;// Poi查询条件类
    private LatLonPoint lp;//
    private Marker locationMarker; // 选择的点
    private PoiSearch poiSearch;
    private List<PoiItem> poiItems;// poi数据
    private RelativeLayout mPoiDetail;
    private TextView mPoiName, mPoiAddress;
    private String keyWord = "";
    private String city;
    private TextView mTvHint;
    private RelativeLayout search_bar_layout;

    private ImageView mIvCenter;
    private Animation animationMarker;
    private LatLng mFinalChoosePosition; //最终选择的点
    private LatLng temp;
    private GeocodeSearch geocoderSearch;

    private String addressName;
    private RecyclerView recycler_map;
    private MapItemAdapter mMapItemAdapter;
    private ArrayList<AddressItem> mDatas = new ArrayList<>();
    private ArrayList<AddressItem> mtempDatas = new ArrayList<>();
    private AddressItem first_address_item = null;
    private TextView mTvSearch;
    private boolean isHandDrag = true;
    private boolean isFirstLoadList = true;
    private boolean is_zooming = true;
    private boolean isBackFromSearchChoose = false;
    private boolean is_loading_more = false;
    private int zoom_lavel = 3;
    private final double lat_scale = Constants.lat_scale;
    private final double lon_scale = Constants.lon_scale;

    private Handler uiHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    mAMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mFinalChoosePosition.latitude, mFinalChoosePosition.longitude), 16));
                    break;
            }
        }
    };


    private Runnable updateUI = new Runnable() {
        @Override
        public void run() {
            mAMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lp.getLatitude(), lp.getLongitude()), zoom_lavel));
        }
    };
    private Runnable zooming = new Runnable() {
        @Override
        public void run() {
            int i = 0;
            while (true) {
                zoom_lavel++;

                MapPicker.this.runOnUiThread(updateUI);
                if (zoom_lavel > 15) {
                    is_zooming = false;
                    MapPicker.this.runOnUiThread(updateUI);
                    break;
                }

                try {
                    Thread.sleep(85);
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
            while (true) {
                if (!mtempDatas.isEmpty() && is_loading_more) {
                    is_loading_more = false;
                    if (isHandDrag) {

                    }
                    MapPicker.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mMapItemAdapter.addData(mtempDatas);
                            refreshLayout.finishLoadMore();

                        }
                    });
                    break;

                } else {

                    i++;
                    if (i > 5 || !is_loading_more) {
                        MapPicker.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                is_loading_more = false;
                                isHandDrag = true;
                                refreshLayout.finishLoadMore();
                                MyToast.showToast(MapPicker.this, "加载缓慢");
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
        setContentView(R.layout.map_picker);
        find_view();
        initializing(savedInstanceState);


    }


    private void find_view() {
        mMapView = $(R.id.map);
        blurView = $(R.id.blurView);
        search_map = $(R.id.search_map);
        ic_back = $(R.id.search_back);
        goto_search_map = $(R.id.goto_search_map);
        recycler_map = $(R.id.recycler_map);
        refreshLayout = $(R.id.map_refreshLayout);
        center_marker = $(R.id.center_marker);
        sure = $(R.id.sure);

    }

    private void initializing(Bundle savedInstanceState) {
        show_loc = AnimationUtils.loadAnimation(this, R.anim.show_loc);
        mMapView.onCreate(savedInstanceState);
        Intent intent = getIntent();
        double lon = intent.getDoubleExtra("lon", 0);
        double lat = intent.getDoubleExtra("lat", 0);
        city = intent.getStringExtra("cityCode");

        lp = new LatLonPoint(lat + lat_scale, lon + lon_scale);


//        myLocationStyle = new MyLocationStyle();//初始化定位蓝点样式类
        //   myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
//        myLocationStyle.interval(2000); //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
//        myLocationStyle.showMyLocation(true);
//        aMap.setMyLocationStyle(myLocationStyle);//设置定位蓝点的Style
        //aMap.getUiSettings().setMyLocationButtonEnabled(true);//设置默认定位按钮是否显示，非必需设置。
//        mAMap.setMyLocationEnabled(true);// 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。


        FrameLayout.LayoutParams bfparams = (FrameLayout.LayoutParams) blurView.getLayoutParams();
        bfparams.height = searchBlockHeight;
        blurView.setLayoutParams(bfparams);
        StatusBarUtil.immersive(this);
        StatusBarUtil.setPaddingSmart(this, blurView);
        bfparams = (FrameLayout.LayoutParams) search_map.getLayoutParams();
        bfparams.height = searchBlockHeight;
        search_map.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        search_map.setLayoutParams(bfparams);
        StatusBarUtil.setPaddingSmart(this, search_map);

        ic_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        goto_search_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mFinalChoosePosition == null) {
                    MyToast.showToast(MapPicker.this, "获取当前位置失败，请重试！！");
                    return;
                }
                Intent intent = new Intent(MapPicker.this, SearchAddress.class);
                intent.putExtra("point", mFinalChoosePosition);
                isBackFromSearchChoose = false;
                startActivityForResult(intent, Constants.OPEN_SEARCH_ADDRESS);

            }
        });


        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recycler_map.setLayoutManager(layoutManager);
        mMapItemAdapter = new MapItemAdapter();
        recycler_map.setAdapter(mMapItemAdapter);


        mAMap = mMapView.getMap();
        mAMap.setOnMapClickListener(this);
        mAMap.setOnCameraChangeListener(this);
        new Thread(zooming).start();
        mAMap.setOnMarkerClickListener(new AMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                marker.showInfoWindow();
                return false;
            }
        });


        recycler_map.setClickable(true);


        mMapItemAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                List<AddressItem> list = mMapItemAdapter.getData();
                temp = mFinalChoosePosition;
                mFinalChoosePosition = convertToLatLng(list.get(position).getPoint());

                for (int i = 0; i < list.size(); i++) {
                    list.get(i).setIs_selected(false);
                }
                list.get(position).setIs_selected(true);
                //  L.d("点击后的最终经纬度：  纬度" + mFinalChoosePosition.latitude + " 经度 " + mFinalChoosePosition.longitude);
                isHandDrag = false;
                mMapItemAdapter.replaceData(list);
                // 点击之后，我利用代码指定的方式改变了地图中心位置，所以也会调用 onCameraChangeFinish
                // 只要地图发生改变，就会调用 onCameraChangeFinish ，不是说非要手动拖动屏幕才会调用该方法

                if (dynamic_marker != null) {
                    dynamic_marker.remove();
                }
                dynamic_marker = mAMap.addMarker(new MarkerOptions()
                        .anchor(0.5f, 0.5f)
                        .icon(BitmapDescriptorFactory
                                .fromBitmap(BitMapUtils.getBitmap(MapPicker.this, R.drawable.ic_dot2)))
                        .position(new LatLng(mFinalChoosePosition.latitude, mFinalChoosePosition.longitude)));

                Message msg = uiHandler.obtainMessage();
                msg.what = 1;
                uiHandler.sendMessageDelayed(msg, 500);

            }
        });


//        LatLng latLng = mAMap.getCameraPosition().target;
//        Point screenPosition = mAMap.getProjection().toScreenLocation(new LatLng(lp.getLatitude(), lp.getLongitude()));
//       MyToast.showToast(this,latLng.toString()+"gfdhhfhgfhgfhgffg");
//       Bitmap bmap= BitMapUtils.getBitmap(this, R.drawable.ic_loc_center);
//        center_marker = mAMap.addMarker(new MarkerOptions()
//                .anchor(0.5f, 1.0f)
//                .icon(BitmapDescriptorFactory.fromBitmap(bmap)));
//
//        //设置Marker在屏幕上,不跟随地图移动
//        center_marker.setPositionByPixels(screenPosition.x, screenPosition.y);


        locationMarker = mAMap.addMarker(new MarkerOptions()
                .anchor(0.5f, 0.5f)
                .icon(BitmapDescriptorFactory
                        .fromBitmap(BitMapUtils.getBitmap(this, R.drawable.ic_dot)))
                .position(new LatLng(lp.getLatitude(), lp.getLongitude())));
        mFinalChoosePosition = locationMarker.getPosition();
        geocoderSearch = new GeocodeSearch(this);
        geocoderSearch.setOnGeocodeSearchListener(this);
        refreshLayout.autoRefresh();


        refreshLayout.getLayout().postDelayed(new Runnable() {
            @Override
            public void run() {
                refreshLayout.finishRefresh();
                refreshLayout.setEnableRefresh(false);
            }
        }, 2000);
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                is_loading_more = true;

                currentPage++;//每次上拉加载更多的时候，page+1
                mtempDatas.clear();
                isHandDrag = false;
//                MyToast.showToast(MapPicker.this,is_loading_more+""+isHandDrag+""+currentPage+"");
                doSearchMore();


                new Thread(load_more).start();


            }
        });

        sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int index = 0;
             List<AddressItem> list=   mMapItemAdapter.getData();
             for(int i = 0;i<list.size();i++){
                 if(list.get(i).isIs_selected()){
                     index = i;
                     break;
                 }
             }


            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图
        mMapView.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView.onResume ()，重新绘制加载地图
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView.onPause ()，暂停地图的绘制
        mMapView.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
        mMapView.onSaveInstanceState(outState);
    }

    @Override
    public void onMapClick(LatLng latLng) {
//        MyToast.showToast(this, "经度：" + latLng.longitude + "   纬度： " + latLng.latitude);
    }


    @Override
    public void onCameraChange(CameraPosition cameraPosition) {


    }

    @Override
    public void onCameraChangeFinish(CameraPosition cameraPosition) {

        if (is_zooming) {

        } else {

            mFinalChoosePosition = cameraPosition.target;
            mFinalChoosePosition = new LatLng(mFinalChoosePosition.latitude + lat_scale, mFinalChoosePosition.longitude + lon_scale);


            if (!isFirstLoadList) {
                if (isHandDrag) {

                    isBackFromSearchChoose = false;
                    center_marker.startAnimation(show_loc);
                    getAddress(cameraPosition.target);
                    doSearchQuery();

                } else  {

                    if (isBackFromSearchChoose) {

                        isHandDrag = false;
                        doSearchQuery();
                    }else {
                        isHandDrag = true;
                    }





                }


                is_loading_more = false;
            } else {
                isFirstLoadList = false;
                getAddress(cameraPosition.target);
                doSearchQuery();

            }


        }


    }


    private void getAddress(LatLng target) {
        RegeocodeQuery query = new RegeocodeQuery(convertToLatLonPoint(target), 200, GeocodeSearch.AMAP);
        geocoderSearch.getFromLocationAsyn(query);// 设置同步逆地理编码请求
    }

    private void doSearchQuery() {

        currentPage = 0;
        query = new PoiSearch.Query("", "", city);// 第一个参数表示搜索字符串，第二个参数表示poi搜索类型，第三个参数表示poi搜索区域（空字符串代表全国）
        query.setPageSize(10);// 设置每页最多返回多少条poiitem
        query.setPageNum(currentPage);// 设置查第一页

        LatLonPoint lpTemp = convertToLatLonPoint(mFinalChoosePosition);

        if (lpTemp != null) {
            poiSearch = new PoiSearch(this, query);
            poiSearch.setOnPoiSearchListener(this);  // 实现  onPoiSearched  和  onPoiItemSearched
            poiSearch.setBound(new PoiSearch.SearchBound(lpTemp, 800, true));//
            // 设置搜索区域为以lp点为圆心，其周围5000米范围
            poiSearch.searchPOIAsyn();// 异步搜索
        }
    }

    private void doSearchMore() {


        query = new PoiSearch.Query("", "", city);// 第一个参数表示搜索字符串，第二个参数表示poi搜索类型，第三个参数表示poi搜索区域（空字符串代表全国）
        query.setPageSize(10);// 设置每页最多返回多少条poiitem
        query.setPageNum(currentPage);// 设置查第一页

        LatLonPoint lpTemp = convertToLatLonPoint(mFinalChoosePosition);

        if (lpTemp != null) {
            poiSearch = new PoiSearch(this, query);
            poiSearch.setOnPoiSearchListener(this);  // 实现  onPoiSearched  和  onPoiItemSearched
            poiSearch.setBound(new PoiSearch.SearchBound(lpTemp, 800, true));//
            // 设置搜索区域为以lp点为圆心，其周围5000米范围
            poiSearch.searchPOIAsyn();// 异步搜索
        }
    }


    @Override
    public void onRegeocodeSearched(RegeocodeResult result, int rCode) {

        if (rCode == 1000) {
            if (result != null && result.getRegeocodeAddress() != null
                    && result.getRegeocodeAddress().getFormatAddress() != null) {
                addressName = result.getRegeocodeAddress().getFormatAddress(); // 逆转地里编码不是每次都可以得到对应地图上的opi

                first_address_item = new AddressItem(addressName, addressName, convertToLatLonPoint(mFinalChoosePosition), true);
            } else {
                MyToast.showToast(this, "没有结果，可能网络断开");
            }
        } else if (rCode == 27) {
            MyToast.showToast(this, "没有结果，网络错误");
        } else if (rCode == 32) {
            MyToast.showToast(this, "没有结果，key错误");
        } else {
            MyToast.showToast(this, "错误码：" + rCode);
        }

    }

    @Override
    public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {
    }

    /**
     * 把LatLng对象转化为LatLonPoint对象
     */
    public static LatLonPoint convertToLatLonPoint(LatLng latlon) {
        return new LatLonPoint(latlon.latitude, latlon.longitude);
    }

    public LatLng convertToLatLng(LatLonPoint latLonPoint) {
        return new LatLng(latLonPoint.getLatitude(), latLonPoint.getLongitude());
    }

    @Override
    public void onPoiSearched(PoiResult result, int rcode) {
        if (rcode == 1000) {

            if (result != null && result.getQuery() != null) {// 搜索poi的结果
                if (result.getQuery().equals(query)) {// 是否是同一条
                    poiResult = result;

                    poiItems = poiResult.getPois();// 取得第一页的poiitem数据，页数从数字0开始

                    List<SuggestionCity> suggestionCities = poiResult
                            .getSearchSuggestionCitys();// 当搜索不到poiitem数据时，会返回含有搜索关键字的城市信息
                    mDatas.clear();
                    //if(isFirstLoadList || isBackFromSearchChoose){
                    if (isHandDrag) {
                        mDatas.add(first_address_item);
                    } else if (isBackFromSearchChoose) {
                        mDatas.add(first_address_item);
                    }
                    // 第一个元素

                    AddressItem address_item = null;
                    for (PoiItem poiItem : poiItems) {
                        //  L.d("得到的数据 poiItem " + poiItem.getSnippet());
                        address_item = new AddressItem(poiItem.getTitle(), poiItem.getSnippet(), poiItem.getLatLonPoint(), false);
                        mDatas.add(address_item);
                    }
                    if (isHandDrag) {
                        mDatas.get(0).setIs_selected(true);
                        mMapItemAdapter.replaceData(mDatas);
                    } else if (isBackFromSearchChoose) {
                        isHandDrag = true;
                        isBackFromSearchChoose = false;
                        mDatas.get(0).setIs_selected(true);
                        mMapItemAdapter.replaceData(mDatas);

                    }
                    if (is_loading_more) {
                        mtempDatas = mDatas;
                    }


                }
            } else {
                MyToast.showToast(this, "对不起，没有搜索到相关数据！");
            }
        }


    }

    @Override
    public void onPoiItemSearched(com.amap.api.services.core.PoiItem poiItem, int i) {
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.OPEN_SEARCH_ADDRESS && resultCode == RESULT_OK) {
            first_address_item.setTitle(data.getStringExtra("title"));
            first_address_item.setContent(data.getStringExtra("content"));
            first_address_item.setPoint((LatLonPoint) data.getParcelableExtra("point"));
            first_address_item.setIs_selected(true);
            isBackFromSearchChoose = true;
            isHandDrag = false;
            if (dynamic_marker != null) {
                dynamic_marker.remove();
            }
            dynamic_marker = mAMap.addMarker(new MarkerOptions()
                    .anchor(0.5f, 0.5f)
                    .icon(BitmapDescriptorFactory
                            .fromBitmap(BitMapUtils.getBitmap(MapPicker.this, R.drawable.ic_dot2)))
                    .position(new LatLng(first_address_item.getPoint().getLatitude(), first_address_item.getPoint().getLongitude())));
            mAMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(first_address_item.getPoint().getLatitude(), first_address_item.getPoint().getLongitude()), 17));
        }


    }
}
