package com.bignerdrach.android.psxl;

import androidx.appcompat.app.AppCompatActivity;
import face.IActionRequest;
import face.IRouteRequest;
import face.ISearchRoute;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
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
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.RidePath;
import com.amap.api.services.route.RideRouteResult;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.services.route.WalkRouteResult;
import com.autonavi.tbt.TrafficFacilityInfo;
import com.bignerdrach.android.psxl.fragment.TabFirstFragment;
import com.bignerdrach.android.psxl.util.AMapUtil;
import com.bignerdrach.android.psxl.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;
//AMapNaviListener, AMapNaviViewListener
public class RouteDetailActivity extends BaseActivity implements AMapNaviListener {

    private double start_longitude;
    private double start_latitude;
    private double end_longitude;
    private double end_latitude;

    TextView tv_time;
    TextView tv_km;
    TextView tv_name;
    TextView tv_description;
    Retrofit retrofit;
    String route_name;

    // 标点
    private MarkerOptions markerOption;
    Marker marker;

    private LatLng latlng = new LatLng(30.254313,120.088456);//终点
    private NaviLatLng startLocation = new NaviLatLng();
    private NaviLatLng endLocation =  new NaviLatLng();


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_detail);
        tv_time = findViewById(R.id.tv_time);
        tv_km = findViewById(R.id.tv_km);
        tv_name = findViewById(R.id.tv_name);
        tv_description = findViewById(R.id.tv_description);


        retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.43.6:3042")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        search();

        mWayPointList = new ArrayList<NaviLatLng>();
        mAMapNaviView = findViewById(R.id.map);
        mAMapNaviView.onCreate(savedInstanceState);
        AMapNaviViewOptions options = mAMapNaviView.getViewOptions();
        options.setAutoDrawRoute(true);
        mAMapNaviView.setViewOptions(options);



        addMarkersToMap();
        //aMap = mAMapNaviView.getMap();
        //setupMap();




    }


    private void addMarkersToMap() {
        markerOption = new MarkerOptions()
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.b2))
                .position(latlng)
                .draggable(true);
        marker = mAMapNaviView.getMap().addMarker(markerOption);
    }

    //接口
    private void search() {
        Intent intent = getIntent();
        route_name = intent.getStringExtra("route_name");
        Toast.makeText(RouteDetailActivity.this, route_name, Toast.LENGTH_SHORT).show();

        ISearchRoute searchRoute = retrofit.create(ISearchRoute.class);
        Call<Route> call = searchRoute.getSearchRoute(route_name);

        call.enqueue(new Callback<Route>() {
            @Override
            public void onResponse(Call<Route> call, Response<Route> response) {
                Route r = response.body();
                if (r.retcode == 1) {
                    Toast.makeText(getApplication(), r.obj.get(0).route_name, Toast.LENGTH_LONG).show();
//                    for (int i=0;i<r.obj.size();i++) {
//                        RouteList.add(new Route.routeinfo(r.obj.get(i).route_id,r.obj.get(i).route_name,r.obj.get(i).
//                                route_distance,r.obj.get(i).route_date,r.obj.get(i).route_description,r.obj.get(i).route_pic,r.obj.get(i).route_time));
//                    }
                    tv_description.setText(r.obj.get(0).route_description);
                    tv_time.setText(String.valueOf(r.obj.get(0).route_time));
                    tv_name.setText(r.obj.get(0).route_name);
                    tv_km.setText(String.valueOf(r.obj.get(0).route_distance));
                    start_latitude = r.obj.get(0).start_latitude;
                    start_longitude = r.obj.get(0).start_longitude;
                    end_latitude = r.obj.get(0).end_latitude;
                    end_longitude = r.obj.get(0).end_longitude;

                    startLocation =  new NaviLatLng(start_latitude,start_longitude);
                    endLocation =  new NaviLatLng(end_latitude,end_longitude);

                } else {
                    Toast.makeText(getApplication(), "正在加载", Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<Route> call, Throwable t) {
                Log.e("1", "S");
                System.out.println("连接失败:" + t.getMessage());
            }
        });
    }

//    private void navi() {
//        //获取AMapNavi实例
//        mAMapNavi = AMapNavi.getInstance(getApplicationContext());
//        //添加监听
//        mAMapNavi.addAMapNaviListener(this);
//
//        mAMapNaviView = (AMapNaviView) findViewById(R.id.navi_view);
//        mAMapNaviView.setAMapNaviViewListener(this);
//
//        //设置模拟导航的行车速度
//        mAMapNavi.setEmulatorNaviSpeed(75);
//
//        sList.add(mStartLatlng);
//        eList.add(mEndLatlng);
//    }


//    private void init() {
//        if(aMap == null){
//            aMap = mAMapNaviView.getMap();
//        }
//        registerListener();
//        mRouteSearch = new RouteSearch(this);
//        mBottomLayout = (RelativeLayout) findViewById(R.id.bottom_layout);
//        mHeadLayout = (RelativeLayout) findViewById(R.id.routemap_header);
//        mHeadLayout.setVisibility(View.GONE);
//        mRotueTimeDes = (TextView) findViewById(R.id.firstline);
//        mRouteDetailDes = (TextView) findViewById(R.id.secondline);
//
//    }
//
//    private void searchRouteResult(int routeType, int mode) {
//        if(mStartPoint == null) {
//            ToastUtil.show(mContext,"定位中，稍后再试");
//            return;
//        }
//        if(mEndPoint ==null) {
//            ToastUtil.show(mContext,"终点未设置");
//        }
//        //showProgressDialog();
//        final RouteSearch.FromAndTo fromAndTo = new RouteSearch.FromAndTo(
//                mStartPoint,mEndPoint);
//        if(routeType == ROUTE_TYPE_RIDE) {
//            RouteSearch.RideRouteQuery query= new RouteSearch.RideRouteQuery(fromAndTo,mode);
//            mRouteSearch.calculateRideRouteAsyn(query);
//        }
//    }

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
        //Toast.makeText(getApplication(), startLocation.toString(), Toast.LENGTH_LONG).show();
        mAMapNavi.calculateRideRoute(startLocation,endLocation);
        //mAMapNavi.calculateRideRoute(new NaviLatLng(30.219416,120.024765), new NaviLatLng(30.238878,120.051977));
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mAMapNaviView.onDestroy()，销毁地图
        mAMapNaviView.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mAMapNaviView.onResume ()，重新绘制加载地图
        mAMapNaviView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mAMapNaviView.onPause ()，暂停地图的绘制
        mAMapNaviView.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mAMapNaviView.onSaveInstanceState (outState)，保存地图当前的状态
        mAMapNaviView.onSaveInstanceState(outState);
    }

    @Override
    public void onStartNavi(int i) {

    }

    @Override
    public void onTrafficStatusUpdate() {

    }
}

