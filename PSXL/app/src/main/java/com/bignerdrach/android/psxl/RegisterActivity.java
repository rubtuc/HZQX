package com.bignerdrach.android.psxl;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import face.IRegister;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RegisterActivity extends AppCompatActivity {
    EditText r_pwd;
    EditText r_account;
    Button register;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        r_pwd = findViewById(R.id.r_pwd);
        r_account = findViewById(R.id.r_account);
        register = findViewById(R.id.register);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("http://http://192.168.43.6:3042") // 设置网络请求的Url地址
                        .addConverterFactory(GsonConverterFactory.create()) // 设置数据解析器
                        .build();
                // 创建网络请求接口的实例
//                IRequest request = retrofit.create(IRequest.class);
                IRegister request = retrofit.create(IRegister.class);
                //对发送请求进行封装
//                Map<String, Object> map = new HashMap<>();
//                map.put("account", mAccount.getText().toString());
//                map.put("pwd",  mPwd.getText().toString());
//                Call<UserInfo> call = request.getUserInfo(map);
                Call<User> call = request.setUserInfo(r_account.getText().toString(),r_pwd.getText().toString());
                //发送网络请求(异步)
                call.enqueue(new Callback<User>() {
                    //请求成功时回调
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        User r=response.body();
                        System.out.println("---------1:"+r.obj.size());
                        System.out.println("---------2:"+r.retcode);
//                        User user =  r.obj;
                        //  System.out.println("11_____________"+r.retcode+"   "+r.obj.get(0).account);
                        if (r.retcode==-1){
                            //todo 检查账号密码是否存在
                            Toast.makeText(getApplicationContext(),"注册失败",Toast.LENGTH_LONG).show();
                        }else{
                            Toast.makeText(getApplicationContext(),"注册成功",Toast.LENGTH_LONG).show();
                            Intent loginIntent = new Intent(RegisterActivity.this, Login.class);
                            startActivity(loginIntent);
                        }
                        System.out.println(response.body().toString());
                    }


                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        Log.e("1","S");
                        System.out.println("连接失败:"+t.getMessage());
                    }
                });
            }
        });
    }
}
