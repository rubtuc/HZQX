package com.bignerdrach.android.psxl.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bignerdrach.android.psxl.Action;
import com.bignerdrach.android.psxl.R;

import java.util.List;

public class ActionAdapter extends BaseAdapter implements AdapterView.OnItemClickListener{
    Context mContext;
    List<Action.actioninfo> mActioninfoList;

    public ActionAdapter(Context context, List<Action.actioninfo> actionList) {
        mContext= context;
        mActioninfoList = actionList;
    }
    @Override
    public int getCount() {
        return mActioninfoList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if(convertView == null){
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.action_item_list,null);
            viewHolder.publish_id = convertView.findViewById(R.id.pubish_id);
            viewHolder.publish_follow = convertView.findViewById(R.id.btn_follow);
            viewHolder.publish_username = convertView.findViewById(R.id.publish_username);
            viewHolder.publish_time = convertView.findViewById(R.id.publish_time);
            viewHolder.publish_comment = convertView.findViewById(R.id.publish_comment);
            viewHolder.publish_data = convertView.findViewById(R.id.publish_data);
            viewHolder.publish_loved = convertView.findViewById(R.id.publish_loved);
            viewHolder.publish_shared = convertView.findViewById(R.id.publish_shared);
            viewHolder.publish_place = convertView.findViewById(R.id.publish_place);
            viewHolder.publish_discuss = convertView.findViewById(R.id.publish_discuss);
            viewHolder.publish_userphoto = convertView.findViewById(R.id.publish_userphoto);
            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final Action.actioninfo actioninfo = mActioninfoList.get(position);//final 的意义？

        viewHolder.publish_id.setText("");
        viewHolder.publish_username.setText(actioninfo.publish_username);
        viewHolder.publish_time.setText(actioninfo.publish_time);
        viewHolder.publish_comment.setText(actioninfo.publish_comment);
       // viewHolder.publish_username.setText(mActioninfoList.get(position).publish_username);
        //viewHolder.publish_data.setImageResource(R.drawable.guide_bg3);
        viewHolder.publish_loved.setText(String.valueOf(actioninfo.publish_loved));
        viewHolder.publish_shared.setText(String.valueOf(actioninfo.publish_shared));
        viewHolder.publish_place.setText(actioninfo.publish_place);
        viewHolder.publish_discuss.setText(String.valueOf(actioninfo.publish_discuss));
        if(actioninfo.publish_follow.equals(1))
        {
            viewHolder.publish_follow.setText("已关注");
        }else{
            viewHolder.publish_follow.setText("关注");
        }

        viewHolder.publish_follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(viewHolder.publish_follow.getText().toString().equals("已关注")){
                    viewHolder.publish_follow.setText("关注");
                }
                else{
                    viewHolder.publish_follow.setText("已关注");
                }


            }
        });

        viewHolder.publish_shared.setOnClickListener(new View.OnClickListener() {
            private boolean status=false;
            @Override
            public void onClick(View v) {
                if(!status){
                    actioninfo.publish_shared++;
                    status = true;
                }
                else {
                    actioninfo.publish_shared--;
                    status = false;
                }
                viewHolder.publish_shared.setText(String.valueOf(actioninfo.publish_shared));
                SharedPreferences PSXL = mContext.getSharedPreferences("PSXL", 0);
                //获取Editor对象
                SharedPreferences.Editor edit = PSXL.edit();
                //根据要保存的数据的类型，调用对应的put方法,
                //以键值对的形式添加新值。
                edit.putString("publish_id",String.valueOf(actioninfo.publish_id));
                edit.putString("publish_shared",String.valueOf(actioninfo.publish_shared));
                //提交新值。必须执行，否则前面的操作都无效。
                edit.commit();
            }
        });


        viewHolder.publish_loved.setOnClickListener(new View.OnClickListener() {
            private boolean status=false;
            @Override
            public void onClick(View v) {
                if(!status){
                    actioninfo.publish_loved++;
                    status = true;
                }
                else {
                    actioninfo.publish_loved--;
                    status = false;
                }
                viewHolder.publish_loved.setText(String.valueOf(actioninfo.publish_loved));

                SharedPreferences PSXL = mContext.getSharedPreferences("PSXL", 0);
                //获取Editor对象
                SharedPreferences.Editor edit = PSXL.edit();
                //根据要保存的数据的类型，调用对应的put方法,
                //以键值对的形式添加新值。
                edit.putString("publish_id",String.valueOf(actioninfo.publish_id));
                edit.putString("publish_loved",String.valueOf(actioninfo.publish_loved));
                //提交新值。必须执行，否则前面的操作都无效。
                edit.commit();

            }
        });

        viewHolder.publish_discuss.setOnClickListener(new View.OnClickListener() {
            private boolean status=false;
            @Override
            public void onClick(View v) {
                if(!status){
                    actioninfo.publish_discuss++;
                    status = true;
                }
                else {
                    actioninfo.publish_discuss--;
                    status = false;
                }
                viewHolder.publish_discuss.setText(String.valueOf(actioninfo.publish_discuss));

                SharedPreferences PSXL = mContext.getSharedPreferences("PSXL", 0);
                //获取Editor对象
                SharedPreferences.Editor edit = PSXL.edit();
                //根据要保存的数据的类型，调用对应的put方法,
                //以键值对的形式添加新值。
                edit.putString("publish_id",String.valueOf(actioninfo.publish_id));
                edit.putString("publish_discuss",String.valueOf(actioninfo.publish_discuss));
                //提交新值。必须执行，否则前面的操作都无效。
                edit.commit();

            }
        });


        return convertView;
    }



    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(mContext,"您选择了"+mActioninfoList.get(position).publish_username,Toast.LENGTH_SHORT).show();
    }

    public final class ViewHolder{
        public TextView publish_id;
        public Button publish_follow;
        public TextView publish_username;
        public TextView publish_time;
        public TextView publish_comment;
        public ImageView publish_data;
        public TextView publish_loved;
        public  TextView publish_shared;
        public TextView publish_place;
        public TextView publish_discuss;
        public ImageView publish_userphoto;
    }

}
