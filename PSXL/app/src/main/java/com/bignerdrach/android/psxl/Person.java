package com.bignerdrach.android.psxl;

import java.util.List;

public class Person {
    public List<personinfo> obj;
    public int retcode;
    public static class personinfo{
        public int person_id;
        public String person_name;
        public String person_intro;
        public double person_km;
        public int person_times;
        public int person_fans;
        public int person_follows;
        public String person_time;
        public String person_portrait;
        public String account;

        public personinfo(int person_id, String person_name, String person_intro,
                          double person_km, int person_times, int person_fans,int person_follows,String person_time, String person_portrait) {

            this.person_id = person_id;
            this.person_name = person_name;
            this.person_intro = person_intro;
            this.person_km = person_km;
            this.person_times = person_times;
            this.person_fans = person_fans;
            this.person_follows = person_follows;
            this.person_time = person_time;
            this.person_portrait = person_portrait;
        }
    }
}
