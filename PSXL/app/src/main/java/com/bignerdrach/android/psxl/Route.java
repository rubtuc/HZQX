package com.bignerdrach.android.psxl;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.security.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
//
public class Route {
    public int retcode;
    public List<routeinfo> obj;

    public static class routeinfo {

        public int route_id;
        public String route_name;
        public double route_distance;
        public String route_date;
        public String route_description;
        public String route_pic;
        public String route_time;
        public Double start_longitude;
        public Double start_latitude;
        public Double end_longitude;
        public Double end_latitude;

        public routeinfo(int route_id, String route_name, double route_distance, String route_date, String route_description,
                         String route_pic, String route_time, Double start_longitude, Double start_latitude, Double end_longitude, Double end_latitude) {
            this.route_id = route_id;
            this.route_name = route_name;
            this.route_distance = route_distance;
            this.route_date = route_date;
            this.route_description = route_description;
            this.route_pic = route_pic;
            this.route_time = route_time;
            this.start_longitude = start_longitude;
            this.start_latitude = start_latitude;
            this.end_longitude = start_longitude;
            this.end_latitude = start_latitude;
        }
    }
}

    //public Date date = new Date();//获得系统时间. 
//        String nowTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
//        public Timestamp goodsC_date = Timestamp.valueOf(nowTime);//把时间转换 
