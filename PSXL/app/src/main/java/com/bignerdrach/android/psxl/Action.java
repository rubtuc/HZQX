package com.bignerdrach.android.psxl;

import android.widget.ImageView;
import android.widget.ListView;

import java.util.List;

public class Action {
    public int retcode;
    public List<actioninfo> obj;
    public static class actioninfo {
        public int publish_id;
        public String publish_follow;
        public String publish_username;
        public String publish_time;
        public String publish_comment;
        public String publish_data;
        public int publish_loved;
        public int publish_shared;
        public String publish_place;
        public int publish_discuss;
        public String publish_userphoto;

        public actioninfo(int publish_id,String publish_follow, String publish_username, String publish_time, String publish_comment, String publish_data, int publish_loved,
                          int publish_shared, String publish_place, int publish_discuss, String publish_userphoto) {
            this.publish_id = publish_id;
            this.publish_follow= publish_follow;
            this.publish_username= publish_username;
            this.publish_time= publish_time;
            this.publish_comment= publish_comment;
            this.publish_data= publish_data;
            this.publish_loved= publish_loved;
            this.publish_shared= publish_shared;
            this.publish_place= publish_place;
            this.publish_discuss= publish_discuss;
            this.publish_userphoto= publish_userphoto;
        }
    }
}
