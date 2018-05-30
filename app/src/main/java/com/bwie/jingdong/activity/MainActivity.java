package com.bwie.jingdong.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;
import android.widget.RadioGroup;

import com.bwie.jingdong.R;
import com.bwie.jingdong.fragment.FragmenFaXian;
import com.bwie.jingdong.fragment.FragmenFenLei;
import com.bwie.jingdong.fragment.FragmenMy;
import com.bwie.jingdong.fragment.FragmenShoueYe;
import com.bwie.jingdong.fragment.FragmentBuy;

public class MainActivity extends AppCompatActivity {

    private FrameLayout frameLayout;
    private RadioGroup rg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        frameLayout = findViewById(R.id.frame);
        rg = findViewById(R.id.rg);
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i){
                    case R.id.rb1:
                        getSupportFragmentManager().beginTransaction().replace(R.id.frame, new FragmenShoueYe()).commit();
                        break;
                    case R.id.rb2:
                        getSupportFragmentManager().beginTransaction().replace(R.id.frame, new FragmenFenLei()).commit();
                        break;
                    case R.id.rb3:
                        getSupportFragmentManager().beginTransaction().replace(R.id.frame, new FragmenFaXian()).commit();
                        break;
                    case R.id.rb4:
                        getSupportFragmentManager().beginTransaction().replace(R.id.frame, new FragmentBuy()).commit();
                        break;
                    case R.id.rb5:
                        getSupportFragmentManager().beginTransaction().replace(R.id.frame, new FragmenMy()).commit();
                        break;
                    default:

                        break;
                }
            }
        });
        getSupportFragmentManager().beginTransaction().replace(R.id.frame, new FragmenShoueYe()).commit();
    }
}
