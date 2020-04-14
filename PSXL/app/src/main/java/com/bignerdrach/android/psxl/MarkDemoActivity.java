package com.bignerdrach.android.psxl;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import android.app.Activity;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.maps.AMap;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.Projection;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.navi.AMapNavi;
import com.amap.api.navi.AMapNaviListener;
import com.amap.api.navi.AMapNaviViewOptions;
import com.amap.api.navi.model.AMapCalcRouteResult;
import com.amap.api.navi.model.AMapLaneInfo;
import com.amap.api.navi.model.AMapModelCross;
import com.amap.api.navi.model.AMapNaviCameraInfo;
import com.amap.api.navi.model.AMapNaviCross;
import com.amap.api.navi.model.AMapNaviInfo;
import com.amap.api.navi.model.AMapNaviLocation;

import com.amap.api.navi.model.AMapNaviRouteNotifyData;
import com.amap.api.navi.model.AMapNaviTrafficFacilityInfo;
import com.amap.api.navi.model.AMapServiceAreaInfo;
import com.amap.api.navi.model.AimLessModeCongestionInfo;
import com.amap.api.navi.model.AimLessModeStat;
import com.amap.api.navi.model.NaviInfo;
import com.amap.api.navi.model.NaviLatLng;
import com.amap.api.navi.view.RouteOverLay;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.autonavi.tbt.TrafficFacilityInfo;
import com.bignerdrach.android.psxl.fragment.TabFirstFragment;
import com.bignerdrach.android.psxl.util.AMapUtil;
import com.bignerdrach.android.psxl.util.ToastUtil;

import java.util.ArrayList;

public class MarkDemoActivity extends BaseActivity implements AMapNaviListener {

    // 标定目的地的点
    private MarkerOptions markerOption;
    private LatLng latlng = new LatLng(30.247286, 120.049675);
    Marker marker;

//    private String city = "杭州";
//    PoiSearch.Query query = null;
//    PoiSearch poiSearch;

    SearchView sw_poi;
    Button btn_poi;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mark_demo);
//        mMapView = findViewById(R.id.map);
//        mMapView.onCreate(savedInstanceState);
        sw_poi = findViewById(R.id.sv_poi);
        btn_poi = findViewById(R.id.btn_poi);
        btn_poi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //todo 搜索赋值
            }
        });

        mWayPointList = new ArrayList<com.amap.api.navi.model.NaviLatLng>();
        mAMapNaviView = findViewById(R.id.map);
        mAMapNaviView .onCreate(savedInstanceState);

        AMapNaviViewOptions options = mAMapNaviView.getViewOptions();
        options.setAutoDrawRoute(false);
        mAMapNaviView.setViewOptions(options);
        addMarkersToMap();// 往地图上添加marker


//        // 查询POI
//        // 构造query对象
//        query  = new PoiSearch.Query(sw_poi.getQuery().toString(),"",city);
//        query.setPageSize(10);
//        query.setPageNum(20);
//        // 设置监听
//        poiSearch = new PoiSearch(this,query);
//        poiSearch.setOnPoiSearchListener(this);
//        poiSearch.searchPOIAsyn();
//        poiSearch.setBound(new PoiSearch.SearchBound(new LatLonPoint(30.219416,120.024765), 1000));//设置周边搜索的中心点以及半径




//        mWayPointList.add(wayPoint);
//        mWayPointList.add(wayPoint1);
//        mWayPointList.add(wayPoint2);
//        mWayPointList.add(wayPoint3);



    }

    private void addMarkersToMap() {
        markerOption = new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.b2))
                .position(latlng)
                .draggable(true);
        marker = mAMapNaviView.getMap().addMarker(markerOption);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    @Override
    public void onInitNaviFailure() {

    }

    @Override
    public void onInitNaviSuccess() {
        super.onInitNaviSuccess();
        /**
         * 方法: int strategy=mAMapNavi.strategyConvert(congestion, avoidhightspeed, cost, hightspeed, multipleroute); 参数:
         *
         * @congestion 躲避拥堵
         * @avoidhightspeed 不走高速
         * @cost 避免收费
         * @hightspeed 高速优先
         * @multipleroute 多路径
         *
         *  说明: 以上参数都是boolean类型，其中multipleroute参数表示是否多条路线，如果为true则此策略会算出多条路线。
         *  注意: 不走高速与高速优先不能同时为true 高速优先与避免收费不能同时为true
         */
        int strategy = 0;
        try {
            //再次强调，最后一个参数为true时代表多路径，否则代表单路径
            strategy = mAMapNavi.strategyConvert(true, false, false, false, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //mAMapNavi.calculateDriveRoute(sList, eList, mWayPointList, strategy);
        mAMapNavi.calculateRideRoute(new NaviLatLng(30.219416,120.024765), new NaviLatLng(30.239878,120.050977));

    }


    @Override
    public void onCalculateRouteSuccess(int[] ints) {
        // 如果根据获取的导航路线来自定义绘制
        RouteOverLay routeOverlay = new RouteOverLay(mAMapNaviView.getMap(), mAMapNavi.getNaviPath(), this);
        routeOverlay.setStartPointBitmap(BitmapFactory.decodeResource(this.getResources(), R.drawable.r1));
        routeOverlay.setEndPointBitmap(BitmapFactory.decodeResource(this.getResources(), R.drawable.b1));
        //routeOverlay.setWayPointBitmap(BitmapFactory.decodeResource(this.getResources(), R.drawable.b2));
        routeOverlay.setTrafficLine(false);
        try {
            routeOverlay.setWidth(30);
        } catch (Throwable e) {
            //宽度须>0
            e.printStackTrace();
        }
        int[] color = new int[10];
        color[0] = Color.BLACK;
        color[1] = Color.RED;
        color[2] = Color.BLUE;
        color[3] = Color.YELLOW;
        color[4] = Color.GRAY;
        routeOverlay.addToMap(color, mAMapNavi.getNaviPath().getWayPointIndex());
        routeOverlay.addToMap();

        mAMapNavi.startNavi(1);
        //mAMapNavi.startNavi(AMapNavi.EmulatorNaviMode);//演示骑行效果
    }


}
