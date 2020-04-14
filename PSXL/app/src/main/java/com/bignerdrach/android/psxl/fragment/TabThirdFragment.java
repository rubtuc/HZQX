package com.bignerdrach.android.psxl.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.bignerdrach.android.psxl.Action;
import face.IActionRequest;
import face.IActionUpdate;
import com.bignerdrach.android.psxl.R;
import com.bignerdrach.android.psxl.adapter.ActionAdapter;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.Fragment;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TabThirdFragment extends Fragment{
    private static final String TAG = "TabThirdFragment";
    protected View mView; // 声明一个视图对象
    protected Context mContext; // 声明一个上下文对象
    private List<Action.actioninfo> ActionList = new ArrayList<Action.actioninfo>();;
    ListView lv_action;
    Retrofit retrofit;

//    //重写listview itenclicklistener
//    private AdapterView.OnItemClickListener mListener;
//    public void onListItemClick(ListView l, View v, int position, long id) {
//        updateAction();
//    }
//
//    @Override
//    public void onClick(View v) {
//        // 获取翻页视图当前页面项的序号
//        int position = lv_;
//        // 触发点击监听器的onActionClick方法
//        mListener.onItemClick(position);
//    }
//
//    // 设置广告图的点击监听器
//    public void setOnActionListener(AdapterView.OnItemClickListener listener) {
//        mListener = listener;
//    }
//
//    // 声明一个广告图点击的监听器对象
//    //private ActionAdapter mListener;
//
//    // 定义一个广告图片的点击监听器接口
//    public interface ActionClickListener {
//        void onActionClick(int position);
//    }




    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (null != mView) {//fragment重复加载的解决办法
            ViewGroup parent = (ViewGroup) mView.getParent();
            if (null != parent) {
                parent.removeView(mView);
            }
        } else {
            mContext = getActivity(); // 获取活动页面的上下文
            // 根据布局文件fragment_tab_third.xml生成视图对象
            mView = inflater.inflate(R.layout.fragment_tab_third, container, false);
            initActionListView();


            retrofit = new Retrofit.Builder()
                    .baseUrl("http://192.168.43.6:3042")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            //显示全部数据
            showAllAction();
            //initActionListView();
        }
        return mView;
    }


    private void initActionListView() {
        lv_action  = mView.findViewById(R.id.lv_action);
        ActionAdapter adapter = new ActionAdapter(mContext,ActionList);
        lv_action.setAdapter(adapter);
        lv_action.setOnItemClickListener(new myListener());
    }

    private void showAllAction() {
        IActionRequest iActionRequest = retrofit.create(IActionRequest.class);
        Call<Action> call = iActionRequest.getActionInfo();
        call.enqueue(new Callback<Action>() {
            @Override
            public void onResponse(Call<Action> call, Response<Action> response) {
                Action r = response.body();
                System.out.println("--------1" + r.obj.get(0).publish_username);
                if (r.retcode == 1) {
                    for (int i = 0; i < r.obj.size(); i++) {
                        ActionList.add(new Action.actioninfo(
                                r.obj.get(i).publish_id,
                                r.obj.get(i).publish_follow,
                                r.obj.get(i).publish_username,
                                r.obj.get(i).publish_time,
                                r.obj.get(i).publish_comment,
                                r.obj.get(i).publish_data,
                                r.obj.get(i).publish_loved,
                                r.obj.get(i).publish_shared,
                                r.obj.get(i).publish_place,
                                r.obj.get(i).publish_discuss,
                                r.obj.get(i).publish_userphoto));
                    }
                    initActionListView();
                }
            }

            @Override
            public void onFailure(Call<Action> call, Throwable t) {
                Log.e("1", "S");
                System.out.println("连接失败:" + t.getMessage());
            }
        });
    }

    private void updateAction() {
        SharedPreferences pf = getActivity().getSharedPreferences("PSXL", 0);
        //根据数据类型，调用对应的get方法，通过键取得对应的值。
        String publish_id = pf.getString("publish_id", "null");
        String publish_loved = pf.getString("publish_loved", "null");
        String publish_shared = pf.getString("publish_shared", "null");
        System.out.println("111111"+publish_id);
        System.out.println("111111"+publish_loved);
        System.out.println("111111"+publish_shared);

        IActionUpdate iActionUpdate = retrofit.create(IActionUpdate.class);
        Call<Action> call = iActionUpdate.getUpdateAction(publish_id,publish_shared,publish_loved,null);
        call.enqueue(new Callback<Action>() {
            @Override
            public void onResponse(Call<Action> call, Response<Action> response) {
                Action r = response.body();
                System.out.println("--------1" + r.obj.get(0).publish_username);
                if (r.retcode == 1) {
//                    for (int i = 0; i < r.obj.size(); i++) {
//                        ActionList.remove(i);
//                    }
//                    for (int i = 0; i < ActionList.size(); i++) {
//                        ActionList.add(new Action.actioninfo(
//                                r.obj.get(i).publish_id,
//                                r.obj.get(i).publish_follow,
//                                r.obj.get(i).publish_username,
//                                r.obj.get(i).publish_time,
//                                r.obj.get(i).publish_comment,
//                                r.obj.get(i).publish_data,
//                                r.obj.get(i).publish_loved,
//                                r.obj.get(i).publish_shared,
//                                r.obj.get(i).publish_place,
//                                r.obj.get(i).publish_discuss,
//                                r.obj.get(i).publish_userphoto));
//                    }
//                   initActionListView();
                }
            }

            @Override
            public void onFailure(Call<Action> call, Throwable t) {
                Log.e("1", "S");
                System.out.println("连接失败:" + t.getMessage());
            }
        });

    }


    private class myListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Toast.makeText(getContext(),"haaa",Toast.LENGTH_SHORT).show();
            updateAction();
        }
    }
}
