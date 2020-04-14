package com.bignerdrach.android.psxl.fragment;

import android.content.Context;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.LinearLayout;
//import android.widget.SearchView;

import android.widget.TextView;
import android.widget.Toast;


import com.bignerdrach.android.psxl.BannerList;
import face.IRouteRequest;
import face.ISearchRoute;
import com.bignerdrach.android.psxl.R;
import com.bignerdrach.android.psxl.Route;
import com.bignerdrach.android.psxl.adapter.RecyclerCollapseAdapter;
import com.bignerdrach.android.psxl.util.Utils;
import com.bignerdrach.android.psxl.widget.BannerPager;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TabSecondFragment extends Fragment {
    private static final String TAG = "TabSecondFragment";
    protected View mView; // 声明一个视图对象
    protected Context mContext; // 声明一个上下文对象
    private List<Route.routeinfo> RouteList = new ArrayList<Route.routeinfo>();
    TextView tv_desc;
    BannerPager banner;
    RecyclerView rv_main;
    Retrofit retrofit;
    RecyclerCollapseAdapter adapter;
    Toolbar toolbar;




    private AlphaAnimation mShowAnim, mHiddenAmin;//控件的显示和隐藏动画

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (null != mView) {//fragment重复加载的解决办法
            ViewGroup parent = (ViewGroup) mView.getParent();
            if (null != parent) {
                parent.removeView(mView);
            }
        } else {
            mContext = getActivity(); // 获取活动页面的上下文
            // 根据布局文件fragment_tab_second.xml生成视图对象
            mView = inflater.inflate(R.layout.fragment_tab_second, container, false);
            //在fragment中设置menu
            setHasOptionsMenu(true);
            // 从布局文件中获取名叫banner_pager的横幅轮播条
            banner = mView.findViewById(R.id.banner_pager);
            // 从布局文件中获取和使用toolbar
            toolbar = mView.findViewById(R.id.tl_head);
            ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);//fragment 中使用toolbar 的用法和  activity里直接是setsupport（）
            tv_desc = mView.findViewById(R.id.tv_desc);
            //控件显示的动画
            mShowAnim = new AlphaAnimation(0.0f, 1.0f);
            mShowAnim.setDuration(1000);
            //控件隐藏的动画
            mHiddenAmin = new AlphaAnimation(1.0f, 0.0f);
            mHiddenAmin.setDuration(1000);

            retrofit = new Retrofit.Builder()
                    .baseUrl("http://192.168.43.6:3042")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            initBanner();
            //doSearch("");
            showAll();
//            initRouteListView();
        }

        return mView;
    }

    private void initBanner() {
        //根据移动端分辨率设置banner高度
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) banner.getLayoutParams();
        params.height = (int) (Utils.getScreenWidth(mContext) * 250f / 640f);
        // 设置横幅轮播条的布局参数
        banner.setLayoutParams(params);
        // 设置横幅轮播条的广告图片队列
        banner.setImage(BannerList.getDefaultList());
        // 设置横幅轮播条的广告点击监听器
        banner.setOnBannerListener(new BannerPager.BannerClickListener() {
            @Override
            public void onBannerClick(int position) {
                Toast.makeText(getContext(),"您点击了第" + position+1 +"张图片",Toast.LENGTH_SHORT).show();
            }
        });
        // 开始广告图片的轮播滚动
        banner.start();
    }

    private void initRouteListView() {
//        RouteList = Route.getDefaultList();
// 从布局文件中获取名叫tl_title的工具栏

        // 从布局文件中获取名叫rv_main的循环视图
        rv_main = mView.findViewById(R.id.rv_main);
        // 创建一个垂直方向的线性布局管理器
        LinearLayoutManager llm = new LinearLayoutManager(this.getActivity());
        llm.setReverseLayout(false);
        llm.setOrientation(RecyclerView.VERTICAL);
        // 设置循环视图的布局管理器
        rv_main.setLayoutManager(llm);

        // 构建一个十二生肖的线性适配器
       // String[] arr = RouteList.toArray(new String[RouteList.size()]);
        //System.out.println("---------4:"+arr);
        System.out.println("---------5:"+RouteList);

        adapter = new RecyclerCollapseAdapter(mContext,RouteList);
        rv_main.setAdapter(adapter);
        rv_main.addOnScrollListener(new MyRecyclerViewScrollListener());
    }
    // 根据菜单项初始化搜索框
    private void initSearchView(Menu menu) {
        MenuItem menuItem = menu.findItem(R.id.menu_search);
        // 从菜单项中获取搜索框对象
        SearchView searchView = (SearchView) menuItem.getActionView();
        // 设置搜索框默认自动缩小为图标
        searchView.setIconifiedByDefault(getActivity().getIntent().getBooleanExtra("collapse", true));
        // 设置是否显示搜索按钮。搜索按钮只显示一个箭头图标，Android暂不支持显示文本。
        // 查看Android源码，搜索按钮用的控件是ImageView，所以只能显示图标不能显示文字。
        searchView.setSubmitButtonEnabled(true);
        searchView.setQueryHint("请输入关键字");

        /**
         * 默认情况下, search widget是"iconified“的，只是用一个图标 来表示它(一个放大镜),
         * 当用户按下它的时候才显示search box . 你可以调用setIconifiedByDefault(false)让search
         * box默认都被显示。 你也可以调用setIconified()让它以iconified“的形式显示。
         */

        // 给搜索框设置文本变化监听器
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            // 搜索关键词完成输入
            public boolean onQueryTextSubmit(String query) {
                //todo 回收过去的循环视图
                for (int i=0;i<RouteList.size();i++) {
                    RouteList.remove(i);
                }
                boolean search =true;
                doSearch(query);
                adapter = new RecyclerCollapseAdapter(mContext,RouteList);

                // 给rv_main设置十二生肖线性适配器
                rv_main.setAdapter(adapter);
                return true;
            }

            // 搜索关键词发生变化
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    // 自动匹配相关的关键词列表
    private void doSearch(String text) {
        ISearchRoute searchRoute = retrofit.create(ISearchRoute.class);
        Call<Route> call = searchRoute.getSearchRoute(text);

        call.enqueue(new Callback<Route>() {
            @Override
            public void onResponse(Call<Route> call, Response<Route> response) {
                Route r = response.body();
                System.out.println("---------1:"+r.obj.size());
                System.out.println("---------2:"+r.obj.get(0).route_name);
                System.out.println("---------3:"+r.retcode);

                if(r.retcode ==1) {
                    Toast.makeText(mContext,r.obj.get(0).route_name,Toast.LENGTH_LONG).show();
                    for (int i=0;i<r.obj.size();i++) {
                        RouteList.add(new Route.routeinfo(r.obj.get(i).route_id,r.obj.get(i).route_name,r.obj.get(i).
                                route_distance,r.obj.get(i).route_date,r.obj.get(i).route_description,
                                r.obj.get(i).route_pic,r.obj.get(i).route_time,r.obj.get(i).start_longitude,
                                r.obj.get(i).start_latitude,r.obj.get(i).end_longitude,r.obj.get(i).end_latitude));
                    }

                }
                else{
                    Toast.makeText(mContext,"正在加载",Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<Route> call, Throwable t) {
                Log.e("1","S");
                System.out.println("连接失败:"+t.getMessage());
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu,MenuInflater inflater) {
        // 从menu_search.xml中构建菜单界面布局
        //getMenuInflater().inflate(R.menu.menu_search, menu);
        inflater.inflate(R.menu.menu_search,menu);
        // 初始化搜索框
        initSearchView(menu);
        //return true;
    }

    private class MyRecyclerViewScrollListener extends RecyclerView.OnScrollListener {

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
            int firstVisibleItem = manager.findFirstVisibleItemPosition();//获取第一个完全显示的ItemPosition
            // 当不滚动时
            if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                // 判断是否滚动到顶部
                if (firstVisibleItem == 0) {
                    banner.startAnimation(mShowAnim);
                    LinearLayout.LayoutParams linearParams =(LinearLayout.LayoutParams) banner.getLayoutParams(); //取控件textView当前的布局参数
                    linearParams.height = (int) (Utils.getScreenWidth(mContext) * 250f / 640f);// 控件的高强制设成20
                    Toast.makeText(mContext,String.valueOf(linearParams.height),Toast.LENGTH_SHORT).show();
                    banner.setLayoutParams(linearParams); //使设置好的布局参数应用到控件</pre>
                    banner.setVisibility(View.VISIBLE);

                }
            } else if (newState == RecyclerView.SCROLL_STATE_DRAGGING ) {//拖动中
                if (banner.getVisibility() == View.VISIBLE) {
                    banner.startAnimation(mHiddenAmin);
                    LinearLayout.LayoutParams linearParams =(LinearLayout.LayoutParams) banner.getLayoutParams(); //取控件textView当前的布局参数
                    linearParams.height = 0;// 控件的高强制设成20
                    banner.setLayoutParams(linearParams); //使设置好的布局参数应用到控件</pre>
                }
            }
        }
    }
    private void showAll() {
        IRouteRequest routeRequest = retrofit.create(IRouteRequest.class);
        Call<Route> call = routeRequest.getRouteData();

        call.enqueue(new Callback<Route>() {
            @Override
            public void onResponse(Call<Route> call, Response<Route> response) {
                Route r = response.body();

                //封装为arraylist
                //ArrayList<Route.routeinfo> routeList= new ArrayList<Route.routeinfo>();
                if(r.retcode ==1) {
//                    for(int i=0; i<r.obj.size();i++){
//                        //todo 赋值给RecycleView
//                    }
                    Toast.makeText(mContext,r.obj.get(0).route_name,Toast.LENGTH_LONG).show();
                    for (int i=0;i<r.obj.size();i++) {
                        RouteList.add(new Route.routeinfo(r.obj.get(i).route_id,r.obj.get(i).route_name,r.obj.get(i).
                                route_distance,r.obj.get(i).route_date,r.obj.get(i).route_description,
                                r.obj.get(i).route_pic,r.obj.get(i).route_time,r.obj.get(i).start_longitude,
                                r.obj.get(i).start_latitude,r.obj.get(i).end_longitude,r.obj.get(i).end_latitude));
                    }

                    initRouteListView();
                }
                else{
                    Toast.makeText(mContext,"正在加载",Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<Route> call, Throwable t) {
                Log.e("1","S");
                System.out.println("连接失败:"+t.getMessage());
            }
        });
    }
}
