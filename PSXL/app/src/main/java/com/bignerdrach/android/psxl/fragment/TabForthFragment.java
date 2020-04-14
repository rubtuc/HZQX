package com.bignerdrach.android.psxl.fragment;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;


import face.IPersonRequest;
import com.bignerdrach.android.psxl.Person;
import com.bignerdrach.android.psxl.R;

import java.util.List;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import cn.smssdk.ui.companent.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TabForthFragment extends Fragment {
    private static final String TAG = "TabForthFragment";
    protected View mView;
    protected FragmentActivity mContent;
    TextView tv_username;
    CircleImageView portrait;
    TextView tv_userintro;
    TextView tv_times;
    TextView tv_time;
    TextView tv_km;
    TextView tv_fans;
    TextView tv_follows;
    List<Person.personinfo> PersonList;

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContent = getActivity();
        mView = inflater.inflate(R.layout.fragment_tab_forth,container,false);
        tv_username = mView.findViewById(R.id.tv_username);
        tv_times = mView.findViewById(R.id.tv_times);
        tv_time = mView.findViewById(R.id.tv_time);
        tv_fans = mView.findViewById(R.id.tv_fans);
        tv_follows = mView.findViewById(R.id.tv_follows);
        tv_km = mView.findViewById(R.id.tv_km);


        mView.findViewById(R.id.iv_setintro).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater factory = LayoutInflater.from(mContent);
                View textEntryView =factory.inflate(R.layout.edit_intro,null);
                final EditText reset_name = textEntryView.findViewById(R.id.reset_name);
                final EditText reset_intro = textEntryView.findViewById(R.id.reset_intro);
                new AlertDialog.Builder(getActivity())
                .setView(textEntryView)
                .setNegativeButton("取消",
                new DialogInterface.OnClickListener() {
                @Override
                 public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .setPositiveButton("提交", new DialogInterface.OnClickListener() {
                     @Override
                    public void onClick(DialogInterface dialog, int which) {
                     if (!reset_name.getText().toString().equals("")&&!reset_intro.getText().toString().equals("")) {
                         tv_username.setText(reset_name.getText().toString());
                         tv_userintro.setText(reset_intro.getText().toString());
                     }
                    }
                }).show();
            }
        });
        tv_userintro = mView.findViewById(R.id.tv_userintro);
        portrait =mView.findViewById(R.id.portrait);

        SharedPreferences pf = getActivity().getSharedPreferences("PSXL", 0);
        //根据数据类型，调用对应的get方法，通过键取得对应的值。
        String account = pf.getString("account", "null");

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.43.6:3042")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        IPersonRequest iPersonRequest = retrofit.create(IPersonRequest.class);

        Call<Person> call = iPersonRequest.getPersonInfo(account);
        call.enqueue(new Callback<Person>() {
            @Override
            public void onResponse(Call<Person> call, Response<Person> response) {
                Person r = response.body();
                if(r.retcode ==1){
                    tv_username.setText(r.obj.get(0).person_name);
                    tv_times.setText(String.valueOf(r.obj.get(0).person_times));
                    tv_fans.setText(String.valueOf(r.obj.get(0).person_fans));
                    tv_follows.setText(String.valueOf(r.obj.get(0).person_follows));
                    tv_time.setText(r.obj.get(0).person_time);
                    tv_km.setText(String.valueOf(r.obj.get(0).person_km));
                    tv_userintro.setText(r.obj.get(0).person_intro);
                    portrait.setImageResource(R.drawable.portrait);
                }
            }

            @Override
            public void onFailure(Call<Person> call, Throwable t) {

            }
        });
        //tv_forth.setText(desc);
        return mView;
    }
}

