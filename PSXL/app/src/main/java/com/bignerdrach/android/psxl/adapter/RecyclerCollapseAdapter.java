package com.bignerdrach.android.psxl.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.bignerdrach.android.psxl.Action;
import com.bignerdrach.android.psxl.R;
import com.bignerdrach.android.psxl.Route;
import com.bignerdrach.android.psxl.RouteDetailActivity;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class RecyclerCollapseAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final static String TAG = "RecyclerCollapseAdapter";
    private Context mContext; // 声明一个上下文对象
    private List<Route.routeinfo> mRouteArray; // 标题文字数组



    public RecyclerCollapseAdapter(Context context, List<Route.routeinfo> routeArray) {
        mContext = context;
        mRouteArray = routeArray;
    }

    // 获取列表项的个数
    public int getItemCount() {
        return mRouteArray.size();
    }

    // 创建列表项的视图持有者
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup vg, int viewType) {
        // 根据布局文件item_collapse.xml生成视图对象
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_collapse, vg, false);

        return new TitleHolder(v);
    }

    // 绑定列表项的视图持有者
    public void onBindViewHolder(RecyclerView.ViewHolder vh, final int position) {
        final TitleHolder holder = (TitleHolder) vh;
        //holder.tv_seq.setText("" + (position + 1));
        final Route.routeinfo routeinfo = mRouteArray.get(position);
        holder.tv_route_id.setText(String.valueOf(routeinfo.route_id));
        holder.tv_route_name.setText(routeinfo.route_name);
//        mRouteArray.get(position).route_pic
        holder.iv_route_pic.setImageResource(R.drawable.ride);//图片格式要转化
        holder.tv_route_date.setText(routeinfo.route_date);
        holder.tv_route_time.setText(routeinfo.route_time);
        holder.tv_route_distance.setText(String.valueOf(routeinfo.route_distance));//地址格式有问题
        holder.tv_route_description.setText(routeinfo.route_description);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext,"您选择了"+mRouteArray.get(position).route_name,Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(mContext,RouteDetailActivity.class);
                intent.putExtra("route_name",routeinfo.route_description);
                mContext.startActivity(intent);

            }
        });


        final Double start_longitude;
        final Double start_latitude;
        final Double end_longitude;
        final Double end_latitude;
        start_latitude =routeinfo.start_latitude;
        start_longitude = routeinfo.start_latitude;
        end_latitude = routeinfo.end_latitude;
        end_longitude = routeinfo.end_longitude;


    }

    // 获取列表项的类型
    public int getItemViewType(int position) {
        return 0;
    }

    // 获取列表项的编号
    public long getItemId(int position) {
        return position;
    }

    // 定义列表项的视图持有者
    public class TitleHolder extends RecyclerView.ViewHolder {
        public LinearLayout ll_item; // 声明列表项的线性布局
        public TextView tv_seq; // 声明列表项序号的文本视图
//        public TextView tv_title; // 声明列表项标题的文本视图

        public TextView tv_route_id;
        public TextView tv_route_name;
        public ImageView iv_route_pic;
        public TextView tv_route_date;
        public TextView tv_route_time;
        public TextView tv_route_distance;
        public TextView tv_route_description;
//        public String start_longitude;
//        public String start_latitude;
//        public String end_longitude;
//        public String end_latitude;

        public TitleHolder(View v) {
            super(v);
            ll_item = v.findViewById(R.id.ll_item);
            //tv_seq = v.findViewById(R.id.tv_seq);

            tv_route_id = v.findViewById(R.id.tv_route_id);
            tv_route_name = v.findViewById(R.id.tv_route_name);
            iv_route_pic = v.findViewById(R.id.iv_route_pic);
            tv_route_date = v.findViewById(R.id.tv_route_date);
            tv_route_time = v.findViewById(R.id.tv_route_time);
            tv_route_distance = v.findViewById(R.id.tv_route_distance);
            tv_route_description = v.findViewById(R.id.tv_route_description);
//            start_longitude = null;
//            start_latitude = null;
//            end_longitude = null;
//            end_latitude = null;
        }
    }



}
