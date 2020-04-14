package com.bignerdrach.android.psxl;

import android.os.Bundle;

import com.amap.api.navi.AMapNaviView;
import com.amap.api.navi.enums.AimLessMode;


public class IntelligentActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_mark_demo);
        mAMapNaviView = (AMapNaviView) findViewById(R.id.map);
        mAMapNaviView.onCreate(savedInstanceState);
        mAMapNaviView.setAMapNaviViewListener(this);
    }

    @Override
    public void onInitNaviSuccess() {
        super.onInitNaviSuccess();
        //智能巡航
        mAMapNavi.startAimlessMode(AimLessMode.CAMERA_AND_SPECIALROAD_DETECTED);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mAMapNavi.stopAimlessMode();
        mAMapNaviView.onDestroy();
        mAMapNavi.destroy();
        mTtsManager.destroy();
    }
}
