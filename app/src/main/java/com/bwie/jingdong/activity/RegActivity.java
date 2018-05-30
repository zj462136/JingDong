package com.bwie.jingdong.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bwie.jingdong.R;
import com.bwie.jingdong.bean.RegBean;
import com.bwie.jingdong.util.OkHttp3Util;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class RegActivity extends AppCompatActivity {

    private EditText et_phone;
    private EditText et_password;
    private ImageView iv;
    private String mobile;
    private String password;
    private RegBean regBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        setContentView(R.layout.activity_reg);
        et_phone = findViewById(R.id.et_phone);
        et_password = findViewById(R.id.et_password);
        iv = findViewById(R.id.image_fanhui);
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public void reg(View view) {
        mobile = et_phone.getText().toString();
        password = et_password.getText().toString();
        OkHttp3Util.doGet("https://www.zhaoapi.cn/user/reg?mobile=" + mobile + "&password=" + password, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()){
                    String string = response.body().string();
                    regBean = new Gson().fromJson(string, RegBean.class);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if ("0".equals(regBean.getCode())){
                                Toast.makeText(RegActivity.this,regBean.getMsg(),Toast.LENGTH_SHORT).show();
                                Intent intent=new Intent(RegActivity.this, LoginActivity.class);
//                                intent.putExtra("name","jd_"+mobile);
//                                setResult(1,intent);
                                startActivity(intent);
                                finish();
                            }else {
                                Toast.makeText(RegActivity.this,regBean.getMsg(),Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }
}
