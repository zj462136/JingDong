package com.bwie.jingdong.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.bwie.jingdong.R;
import com.bwie.jingdong.bean.LoginBean;
import com.bwie.jingdong.util.OkHttp3Util;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity {

    private EditText et_phone;
    private EditText et_password;
    private LoginBean loginBean;
    private String mobile;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        et_phone = findViewById(R.id.et_phone);
        et_password = findViewById(R.id.et_password);
    }

    public void login(View view) {
        mobile = et_phone.getText().toString();
        password = et_password.getText().toString();
        OkHttp3Util.doGet("https://www.zhaoapi.cn/user/login?mobile=" + mobile + "&password=" + password, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()){
                    String string = response.body().string();
                    loginBean = new Gson().fromJson(string, LoginBean.class);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if ("0".equals(loginBean.getCode())){
//                                Toast.makeText(LoginActivity.this, loginBean.getMsg(),Toast.LENGTH_SHORT).show();
//                                Intent intent=new Intent(LoginActivity.this, FragmenMy.class);
//                                intent.putExtra("username","jd_"+mobile);
//                                setResult(2,intent);
//                                finish();
                                SharedPreferences sp = getSharedPreferences("login", MODE_PRIVATE);
                                SharedPreferences.Editor edit = sp.edit();
                                edit.putBoolean("islogin",true);
                                edit.putString("username",loginBean.getData().getUsername());
                                edit.commit();
                                finish();//关闭掉页面
                            }else {
                                Toast.makeText(LoginActivity.this, loginBean.getMsg(),Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }

    public void reg(View view) {
        Intent intent = new Intent(LoginActivity.this, RegActivity.class);
        startActivity(intent);
    }
}
