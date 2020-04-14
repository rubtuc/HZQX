package com.bignerdrach.android.psxl;

//import com.example.senior.R;

import java.util.ArrayList;

public class BannerList {
//    public int pic; // 大图的资源编号
//    public int rg;
//
//    public BannerList() {
//        pic = 0;
//        rg = 0;
//    }

//    // 声明一个手机商品的大图数组
//    private static int[] mPicArray = {
//            R.drawable.guide_bg1, R.drawable.guide_bg2, R.drawable.guide_bg3,
//            R.drawable.guide_bg4
//    };
//
//    private static int[] mRgArray = {
//           0,1,2,3
//    };
    // 获取默认的手机信息列表
    public static ArrayList<Integer> getDefaultList() {
        ArrayList<Integer> bannerList = new ArrayList<Integer>();
        bannerList.add(R.drawable.guide_bg1);
        bannerList.add(R.drawable.guide_bg2);
        bannerList.add(R.drawable.guide_bg3);
        bannerList.add(R.drawable.guide_bg4);
        return bannerList;
    }
}
