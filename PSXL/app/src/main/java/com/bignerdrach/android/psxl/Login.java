package com.bignerdrach.android.psxl;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;

import androidx.appcompat.app.AppCompatActivity;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import cn.smssdk.gui.RegisterPage;
import face.IRequest;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class Login extends AppCompatActivity {
    private EditText mAccount;
    private EditText mPwd;

    //private String content = pwd.getText().toString();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAccount = findViewById(R.id.account);
        mPwd = findViewById(R.id.pwd);
        findViewById(R.id.tv_register).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this,RegisterActivity.class);
                //startActivity(intent);
                startActivity(intent);
            }
        });
        findViewById(R.id.tv_forget).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this,ForgetPwd.class);
                intent.putExtra("account",mAccount.getText().toString());
                //startActivity(intent);
                startActivityForResult(intent,1);
            }
        });
        findViewById(R.id.tv_code).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendCode(getApplicationContext());
            }
        });


        System.out.println("666");
        findViewById(R.id.login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(getApplicationContext(),"登录前",Toast.LENGTH_LONG).show();
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("http://192.168.43.6:3042") // 设置网络请求的Url地址
                        .addConverterFactory(GsonConverterFactory.create()) // 设置数据解析器
                        .build();
                // 创建网络请求接口的实例
                IRequest request = retrofit.create(IRequest.class);
                //对发送请求进行封装
                Call<User> call = request.getUserInfo(mAccount.getText().toString(),mPwd.getText().toString());
//                Map<String, Object> map = new HashMap<>();
//                map.put("account", mAccount.getText().toString());
//                map.put("pwd",  mPwd.getText().toString());
//                Call<UserInfo> call = request.getUserInfo(map);

//                Toast.makeText(getApplicationContext(),map.toString(),Toast.LENGTH_SHORT).show();
                //Call<UserInfo> call = request.getUserInfo(mAccount.getText().toString());
                //发送网络请求(异步)
                call.enqueue(new Callback<User>() {
                    //请求成功时回调
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        User r=response.body();
                        if (r.retcode==-1){
                            Toast.makeText(getApplicationContext(),"登录失败",Toast.LENGTH_LONG).show();
                        }else{
                            Toast.makeText(getApplicationContext(),"登录成功",Toast.LENGTH_LONG).show();

                            SharedPreferences PSXL = getSharedPreferences("PSXL", 0);
                            //获取Editor对象
                            SharedPreferences.Editor edit = PSXL.edit();
                            //根据要保存的数据的类型，调用对应的put方法,
                            //以键值对的形式添加新值。
                            edit.putString("account",mAccount.getText().toString());
                            //提交新值。必须执行，否则前面的操作都无效。
                            edit.commit();
                            Intent loginIntent = new Intent(Login.this, TabFragmentActivity.class);
                            startActivity(loginIntent);
//                            }
//                            else {
//                                Toast.makeText(getApplicationContext(),"登录成功",Toast.LENGTH_LONG).show();
//
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
    @Override
    protected  void onActivityResult(int requestCode,int resultCode ,Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if(requestCode==1&&resultCode==2){//当请求码是1&&返回码是2进行下面操作
//            String temp = "";
//            Bundle bundle=data.getExtras();
//            temp=bundle.getString("name");
//            pwd.setText(temp);
            String pwd = data.getStringExtra("pwd");
            String account = data.getStringExtra("account");
            mPwd.setText(pwd);
            mAccount.setText(account);
//            pwd.setText(data.getStringExtra("newPwd"));
        }
    }


    public void sendCode(Context context) {
        RegisterPage page = new RegisterPage();
        //如果使用我们的ui，没有申请模板编号的情况下需传null
        page.setTempCode(null);
        page.setRegisterCallback(new EventHandler() {
            public void afterEvent(int event, int result, Object data) {
                if (result == SMSSDK.RESULT_COMPLETE) {
                    // 处理成功的结果
                    HashMap<String,Object> phoneMap = (HashMap<String, Object>) data;
                    // 国家代码，如“86”
                    String country = (String) phoneMap.get("country");
                    // 手机号码，如“13800138000”
                    String phone = (String) phoneMap.get("phone");
                } else{
                }
            }
        });
        page.show(context);
//        Intent LoginIntent = new Intent(Login.this, HomeActivity.class);
//        startActivity(LoginIntent);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id ==R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
