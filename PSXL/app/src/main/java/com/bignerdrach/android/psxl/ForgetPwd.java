package com.bignerdrach.android.psxl;

import androidx.appcompat.app.AppCompatActivity;
import face.IForget;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class ForgetPwd extends AppCompatActivity {
    EditText newPwd;
    EditText confirmPwd;
    EditText account;
    Button submitBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_pwd);
        newPwd = findViewById(R.id.new_pwd);
        confirmPwd = findViewById(R.id.confirm_pwd);
        submitBtn = findViewById(R.id.submit_pwd);
        account = findViewById(R.id.account);
//        account = getIntent().getStringExtra("account");
//        Toast.makeText(ForgetPwd.this,account,Toast.LENGTH_SHORT).show();

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!confirmPwd.getText().toString().equals(newPwd.getText().toString())){
                    Toast.makeText(ForgetPwd.this,"密码输入不一致",Toast.LENGTH_SHORT).show();
                }
                else{

                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl("http://192.168.43.6:3042")
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();

                    IForget request = retrofit.create(IForget.class);

                    Call<User> call = request.resetUserInfo(account.getText().toString(),confirmPwd.getText().toString());
                     call.enqueue(new Callback<User>() {
                         @Override
                         public void onResponse(Call<User> call, Response<User> response) {
                             User r= response.body();
                             if(r.retcode == 1) {
                                 Toast.makeText(ForgetPwd.this,"密码修改成功",Toast.LENGTH_SHORT).show();
                                 Intent data = new Intent();
                                 data.putExtra("pwd",newPwd.getText().toString());
                                 data.putExtra("account",account.getText().toString());
                                 setResult(2,data);
                                 finish();//关闭页面
                             }
                             else {
                                 Toast.makeText(ForgetPwd.this,"该账号不存在",Toast.LENGTH_SHORT).show();
                             }
                         }

                         @Override
                         public void onFailure(Call<User> call, Throwable t) {
                             Log.e("1","S");
                             System.out.println("连接失败:"+t.getMessage());
                         }
                     });


//                    Intent data = new Intent();
//                    data.putExtra("pwd",newPwd.getText().toString());
//                    data.putExtra("account",account.getText().toString());
//                    setResult(2,data);
//                    finish();//关闭页面
                }
            }
        });
    }
}
