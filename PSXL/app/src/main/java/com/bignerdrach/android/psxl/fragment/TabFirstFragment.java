package com.bignerdrach.android.psxl.fragment;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.location.Location;
import android.os.Bundle;

import android.os.Handler;
import android.os.SystemClock;
import android.provider.SyncStateContract;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.BounceInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.Projection;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.maps.model.Text;
import com.amap.api.maps.model.TextOptions;
import com.amap.api.maps.model.animation.Animation;
import com.amap.api.maps.model.animation.ScaleAnimation;
import com.bignerdrach.android.psxl.R;
import com.bignerdrach.android.psxl.util.Constants;
import com.bignerdrach.android.psxl.util.ToastUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import androidx.fragment.app.Fragment;
//LocationSource,
//        AMapLocationListener,AMap.OnMapTouchListener,View.OnClickListener
public class TabFirstFragment extends Fragment implements View.OnClickListener,AMapLocationListener,LocationSource{
    private static final String TAG = "TabFirstFragment";
    //声明地图
    protected View mView; // 声明一个视图对象
    protected Context mContext; // 声明一个上下文对象
    private MapView mMapView = null;
    private AMap aMap = null;
    //标点
    private MarkerOptions markerOption;
    private LatLng latlng = new LatLng(120.024765,30.219416);//仅用于添加的marker
    Marker locationMarker;  //自定义定位小蓝点的Marker
    Marker marker;//添加的小蓝点
    //定位
    public AMapLocationClient mLocationClient = null;  //声明AMapLocationClient类对象
    public AMapLocationClientOption mLocationOption = null; //声明mLocationOption对象
    private LocationSource.OnLocationChangedListener mLocationListener;   //声明定位回调监听器
    boolean useMoveToLocationWithMapMode = true;
    Projection projection;//坐标和经纬度转换工具
    MyCancelCallback myCancelCallback = new MyCancelCallback();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (null != mView) {//fragment重复加载的解决办法
            ViewGroup parent = (ViewGroup) mView.getParent();
            if (null != parent) {
                parent.removeView(mView);
            }
        } else {
            mContext = getActivity(); // 获取活动页面的上下文
            mView = inflater.inflate(R.layout.fragment_tab_first, container, false);
            mMapView = mView.findViewById(R.id.map);
            mMapView.onCreate(savedInstanceState);

            init();
        }
        return mView;
    }

    private void init() {
        Button clearMap = (Button) mView.findViewById(R.id.clearMap);
        clearMap.setOnClickListener(this);
        Button resetMap = (Button) mView.findViewById(R.id.resetMap);
        resetMap.setOnClickListener(this);
        if (aMap == null) {
            aMap = mMapView.getMap();
            setUpMap();
            addMarkersToMap();// 往地图上添加marker
        }
    }

    private void setUpMap() {
        //aMap.setLocationSource(this);// 设置定位监听 包含两个回调，activate(OnLocationChangedListener)和deactivate()
        //aMap.setOnMapTouchListener(this);

        // 启动定位
        MyLocationStyle myLocationStyle;
        myLocationStyle = new MyLocationStyle();//初始化定位蓝点样式类myLocationStyle.myLocationType
        myLocationStyle.interval(2000); //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
        aMap.setMyLocationStyle(myLocationStyle);//设置定位蓝点的Style
        aMap.getUiSettings().setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示，非必需设置。
        aMap.setMyLocationEnabled(true);// 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。
    }

//    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (mLocationListener != null && aMapLocation != null) {
            if (aMapLocation != null
                    && aMapLocation.getErrorCode() == 0) {
                LatLng latLng = new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude());
                //展示自定义定位小蓝点
                if(locationMarker == null) {
                    //首次定位
                    locationMarker = aMap.addMarker(new MarkerOptions().position(latLng)
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.location_marker))
                            .anchor(0.2f, 0.2f));

                    //首次定位,选择移动到地图中心点并修改级别到15级
                    aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
                } else {
                    if(useMoveToLocationWithMapMode) {
                        //二次以后定位，使用sdk中没有的模式，让地图和小蓝点一起移动到中心点（类似导航锁车时的效果）
                        startMoveLocationAndMap(latLng);
                    } else {
                        startChangeLocation(latLng);
                    }

                }


            } else {
                String errText = "定位失败," + aMapLocation.getErrorCode()+ ": " + aMapLocation.getErrorInfo();
                Log.e("AmapErr",errText);
            }
        }
    }

    @Override
    public void activate(LocationSource.OnLocationChangedListener onLocationChangedListener) {
        mLocationListener = onLocationChangedListener;
        if (mLocationClient == null) {
            mLocationClient = new AMapLocationClient(mContext);
            mLocationOption = new AMapLocationClientOption();
            mLocationClient.setLocationListener(this);//设置定位监听
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);//设置为高精度定位模式
            mLocationOption.setInterval(2000); //设置定位间隔
            mLocationClient.setLocationOption(mLocationOption); //设置定位参数
            // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
            // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
            // 在定位结束后，在合适的生命周期调用onDestroy()方法
            // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
            mLocationClient.startLocation();
        }
    }

    @Override
    public void deactivate() {
        mLocationListener = null;
        if (mLocationClient != null) {
            mLocationClient.stopLocation();
            mLocationClient.onDestroy();
        }
        mLocationClient = null;
    }
    /**
     * 修改自定义定位小蓝点的位置
     * @param latLng
     */
    private void startChangeLocation(LatLng latLng) {

        if(locationMarker != null) {
            LatLng curLatlng = locationMarker.getPosition();
            if(curLatlng == null || !curLatlng.equals(latLng)) {
                locationMarker.setPosition(latLng);
            }
        }
    }

    /**
     * 同时修改自定义定位小蓝点和地图的位置
     * @param //latLng
     */
    private void startMoveLocationAndMap(LatLng latLng) {

        //将小蓝点提取到屏幕上
        if(projection == null) {
            projection = aMap.getProjection();
        }
        if(locationMarker != null && projection != null) {
            LatLng markerLocation = locationMarker.getPosition();
            Point screenPosition = aMap.getProjection().toScreenLocation(markerLocation);
            locationMarker.setPositionByPixels(screenPosition.x, screenPosition.y);
        }
        //移动地图，移动结束后，将小蓝点放到放到地图上
        myCancelCallback.setTargetLatlng(latLng);
        //动画移动的时间，最好不要比定位间隔长，如果定位间隔2000ms 动画移动时间最好小于2000ms，可以使用1000ms
        //如果超过了，需要在myCancelCallback中进行处理被打断的情况
        aMap.animateCamera(CameraUpdateFactory.changeLatLng(latLng),1000,myCancelCallback);

    }


    /**
     * 监控地图动画移动情况，如果结束或者被打断，都需要执行响应的操作
     */
    class MyCancelCallback implements AMap.CancelableCallback {

        LatLng targetLatlng;
        public void setTargetLatlng(LatLng latlng) {
            this.targetLatlng = latlng;
        }

        @Override
        public void onFinish() {
            if(locationMarker != null && targetLatlng != null) {
                locationMarker.setPosition(targetLatlng);
            }
        }

        @Override
        public void onCancel() {
            if(locationMarker != null && targetLatlng != null) {
                locationMarker.setPosition(targetLatlng);
            }
        }
    };


    /**
     * 在地图上添加marker
     */
    private void addMarkersToMap() {

        markerOption = new MarkerOptions()
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.location_marker))
                .position(latlng)
                .draggable(true);
        marker = aMap.addMarker(markerOption);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            /**
             * 清空地图上所有已经标注的marker
             */
            case R.id.clearMap:
                if (aMap != null) {
                    marker.destroy();
                }
                break;
            /**
             * 重新标注所有的marker
             */
            case R.id.resetMap:
                if (aMap != null) {
                    aMap.clear();
                    addMarkersToMap();
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
//        if(null != mLocationClient){
//            mLocationClient.onDestroy();
//        }
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
        //deactivate();
        //useMoveToLocationWithMapMode = false;
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
        //useMoveToLocationWithMapMode = true;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mMapView.onSaveInstanceState(outState);// 用户重载这个方法时必须调用父类的这个方法 用于MapView保存地图状态。
    }

}


//    /**
//     * 在地图上添加marker  多种样式
//     */
//    private void addMarkersToMap() {
//        // 文字显示标注，可以设置显示内容，位置，字体大小颜色，背景色旋转角度
//        TextOptions textOptions = new TextOptions()
//                .position(Constants.BEIJING)
//                .text("Text")
//                .fontColor(Color.BLACK)
//                .backgroundColor(Color.BLUE)
//                .fontSize(30)
//                .rotate(20)
//                .align(Text.ALIGN_CENTER_HORIZONTAL, Text.ALIGN_CENTER_VERTICAL)
//                .zIndex(1.f).typeface(Typeface.DEFAULT_BOLD);
//        aMap.addText(textOptions);
//
//        Marker marker = aMap.addMarker(new MarkerOptions()
//
//                .title("好好学习")
//                .icon(BitmapDescriptorFactory
//                        .defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
//                .draggable(true));
//        marker.setRotateAngle(90);// 设置marker旋转90度
//        marker.setPositionByPixels(400, 400);
//        marker.showInfoWindow();// 设置默认显示一个infowinfow
//
//        markerOption = new MarkerOptions();
//        markerOption.position(Constants.XIAN);
//        markerOption.title("西安市").snippet("西安市：34.341568, 108.940174");
//
//        markerOption.draggable(true);
//        markerOption.icon(
//                BitmapDescriptorFactory
//                        .fromResource(R.drawable.location_marker));
////                BitmapDescriptorFactory.fromBitmap(BitmapFactory
////                        .decodeResource(getResources(),
////                                R.drawable.location_marker)));
//        // 将Marker设置为贴地显示，可以双指下拉看效果
//        markerOption.setFlat(true);
//
//        ArrayList<BitmapDescriptor> giflist = new ArrayList<BitmapDescriptor>();
//        giflist.add(BitmapDescriptorFactory
//                .defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
//        giflist.add(BitmapDescriptorFactory
//                .defaultMarker(BitmapDescriptorFactory.HUE_RED));
//        giflist.add(BitmapDescriptorFactory
//                .defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));
//
//        MarkerOptions markerOption1 = new MarkerOptions().anchor(0.5f, 0.5f)
//                .position(Constants.CHENGDU).title("成都市")
//                .snippet("成都市:30.679879, 104.064855").icons(giflist)
//                .draggable(true).period(10);
//        ArrayList<MarkerOptions> markerOptionlst = new ArrayList<MarkerOptions>();
//        markerOptionlst.add(markerOption);
//        markerOptionlst.add(markerOption1);
//        List<Marker> markerlst = aMap.addMarkers(markerOptionlst, true);
//        marker2 = markerlst.get(0);
//
//        marker3 = aMap.addMarker(new MarkerOptions().position(
//                Constants.ZHENGZHOU).icon(
//                BitmapDescriptorFactory.fromResource(R.drawable.location_marker)));
//    }
//
//    /**
//     * 对marker标注点点击响应事件
//     */
//    @Override
//    public boolean onMarkerClick(final Marker marker) {
//        if (aMap != null) {
//            if (marker.equals(marker2)) {
//                jumpPoint(marker);
//            } else if (marker.equals(marker3)) {
//                growInto(marker);
//            }
//
//        }
//        markerText.setText("你点击的是" + marker.getTitle());
//
//        return false;
//    }
//
//    /**
//     * marker点击时跳动一下
//     */
//    public void jumpPoint(final Marker marker) {
//        final Handler handler = new Handler();
//        final long start = SystemClock.uptimeMillis();
//        Projection proj = aMap.getProjection();
//        Point startPoint = proj.toScreenLocation(Constants.XIAN);
//        startPoint.offset(0, -100);
//        final LatLng startLatLng = proj.fromScreenLocation(startPoint);
//        final long duration = 1500;
//
//        final Interpolator interpolator = new BounceInterpolator();
//        handler.post(new Runnable() {
//            @Override
//            public void run() {
//                long elapsed = SystemClock.uptimeMillis() - start;
//                float t = interpolator.getInterpolation((float) elapsed
//                        / duration);
//                double lng = t * Constants.XIAN.longitude + (1 - t)
//                        * startLatLng.longitude;
//                double lat = t * Constants.XIAN.latitude + (1 - t)
//                        * startLatLng.latitude;
//                marker.setPosition(new LatLng(lat, lng));
//                if (t < 1.0) {
//                    handler.postDelayed(this, 16);
//                }
//            }
//        });
//    }

//    /**
//     * marker 必须有设置图标，否则无效果
//     *
//     * @param marker
//     */
//    private void dropInto(final Marker marker) {
//
//        final Handler handler = new Handler();
//        final long start = SystemClock.uptimeMillis();
//        final LatLng markerLatlng = marker.getPosition();
//        Projection proj = aMap.getProjection();
//        Point markerPoint = proj.toScreenLocation(markerLatlng);
//        Point startPoint = new Point(markerPoint.x, 0);// 从marker的屏幕上方下落
//        final LatLng startLatLng = proj.fromScreenLocation(startPoint);
//        final long duration = 800;// 动画总时长
//
//        final Interpolator interpolator = new AccelerateInterpolator();
//        handler.post(new Runnable() {
//            @Override
//            public void run() {
//                long elapsed = SystemClock.uptimeMillis() - start;
//                float t = interpolator.getInterpolation((float) elapsed
//                        / duration);
//                double lng = t * markerLatlng.longitude + (1 - t)
//                        * startLatLng.longitude;
//                double lat = t * markerLatlng.latitude + (1 - t)
//                        * startLatLng.latitude;
//                marker.setPosition(new LatLng(lat, lng));
//                if (t < 1.0) {
//                    handler.postDelayed(this, 16);
//                }
//            }
//        });
//    }

//    /**
//     * 从地上生长效果，
//     * @param marker
//     */
//    private void growInto(final Marker marker) {
//        Animation animation = new ScaleAnimation(0,1,0,1);
//        animation.setInterpolator(new LinearInterpolator());
//        //整个移动所需要的时间
//        animation.setDuration(1000);
//        //设置动画
//        marker.setAnimation(animation);
//        //开始动画
//        marker.startAnimation();
//    }
//
//    /**
//     * 监听点击infowindow窗口事件回调
//     */
//    @Override
//    public void onInfoWindowClick(Marker marker) {
//        ToastUtil.show(mContext, "你点击了infoWindow窗口" + marker.getTitle());
//        ToastUtil.show(mContext, "当前地图可视区域内Marker数量:"
//                + aMap.getMapScreenMarkers().size());
//    }
//
//    /**
//     * 监听拖动marker时事件回调
//     */
//    @Override
//    public void onMarkerDrag(Marker marker) {
//        String curDes = marker.getTitle() + "拖动时当前位置:(lat,lng)\n("
//                + marker.getPosition().latitude + ","
//                + marker.getPosition().longitude + ")";
//        markerText.setText(curDes);
//    }
//    /**
//     * 监听拖动marker结束事件回调
//     */
//    @Override
//    public void onMarkerDragEnd(Marker marker) {
//        markerText.setText(marker.getTitle() + "停止拖动");
//    }
//
//    /**
//     * 监听开始拖动marker事件回调
//     */
//    @Override
//    public void onMarkerDragStart(Marker marker) {
//        markerText.setText(marker.getTitle() + "开始拖动");
//    }
//
//    /**
//     * 监听amap地图加载成功事件回调
//     */
//    @Override
//    public void onMapLoaded() {
//        // 设置所有maker显示在当前可视区域地图中
//        LatLngBounds bounds = new LatLngBounds.Builder()
//                .include(Constants.XIAN).include(Constants.CHENGDU)
//                .include(Constants.BEIJING).include(latlng).build();
//        aMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 150));
//    }
//
//    /**
//     * 监听自定义infowindow窗口的infocontents事件回调
//     */
//    @Override
//    public View getInfoContents(Marker marker) {
//        if (radioOption.getCheckedRadioButtonId() != R.id.custom_info_contents) {
//            return null;
//        }
//        View infoContent = getLayoutInflater().inflate(
//                R.layout.custom_info_contents, null);
//        render(marker, infoContent);
//        return infoContent;
//    }
//
//    /**
//     * 监听自定义infowindow窗口的infowindow事件回调
//     */
//    @Override
//    public View getInfoWindow(Marker marker) {
//        if (radioOption.getCheckedRadioButtonId() != R.id.custom_info_window) {
//            return null;
//        }
//        View infoWindow = getLayoutInflater().inflate(
//                R.layout.custom_info_window, null);
//
//        render(marker, infoWindow);
//        return infoWindow;
//    }
//
//    /**
//     * 自定义infowinfow窗口
//     */
//    public void render(Marker marker, View view) {
//        if (radioOption.getCheckedRadioButtonId() == R.id.custom_info_contents) {
//            ((ImageView) view.findViewById(R.id.badge))
//                    .setImageResource(R.drawable.icon_point_c);
//        } else if (radioOption.getCheckedRadioButtonId() == R.id.custom_info_window) {
//            ImageView imageView = (ImageView) view.findViewById(R.id.badge);
//            imageView.setImageResource(R.drawable.icon_point_n);
//        }
//        String title = marker.getTitle();
//        TextView titleUi = ((TextView) view.findViewById(R.id.title));
//        if (title != null) {
//            SpannableString titleText = new SpannableString(title);
//            titleText.setSpan(new ForegroundColorSpan(Color.RED), 0,
//                    titleText.length(), 0);
//            titleUi.setTextSize(15);
//            titleUi.setText(titleText);
//
//        } else {
//            titleUi.setText("");
//        }
//        String snippet = marker.getSnippet();
//        TextView snippetUi = ((TextView) view.findViewById(R.id.snippet));
//        if (snippet != null) {
//            SpannableString snippetText = new SpannableString(snippet);
//            snippetText.setSpan(new ForegroundColorSpan(Color.GREEN), 0,
//                    snippetText.length(), 0);
//            snippetUi.setTextSize(20);
//            snippetUi.setText(snippetText);
//        } else {
//            snippetUi.setText("");
//        }
//    }
//
//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
//            /**
//             * 清空地图上所有已经标注的marker
//             */
//            case R.id.clearMap:
//                if (aMap != null) {
//                    aMap.clear();
//                }
//                break;
//            /**
//             * 重新标注所有的marker
//             */
//            case R.id.resetMap:
//                if (aMap != null) {
//                    aMap.clear();
//                    addMarkersToMap();
//                }
//                break;
//            default:
//                break;
//        }
//    }
//
//    @Override
//    public void onCheckedChanged(RadioGroup group, int checkedId) {
//        if(checkedId == R.id.default_info_window) {
//            //取消自定义InfoWindow，则使用默认InfoWindow样式
//            aMap.setInfoWindowAdapter(null);
//        } else if (checkedId == R.id.custom_info_contents) {
//            aMap.setInfoWindowAdapter(this);
//        } else if (checkedId == R.id.custom_info_window) {
//            aMap.setInfoWindowAdapter(this);
//        }
//    }



//    RadioGroup radioOption;
//    TextView markerText;
//    private Marker marker2;// 有跳动效果的marker对象
//    private Marker marker3;// 从地上生长的marker对象