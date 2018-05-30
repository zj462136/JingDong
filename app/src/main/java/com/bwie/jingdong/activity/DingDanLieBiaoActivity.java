package com.bwie.jingdong.activity;

import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.bwie.jingdong.R;
import com.bwie.jingdong.fragment.FragmentDaiZhifu;
import com.bwie.jingdong.fragment.FragmentYiQuXiao;
import com.bwie.jingdong.fragment.FragmentYiZhiFu;

import java.util.ArrayList;
import java.util.List;

public class DingDanLieBiaoActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView image_liebiao;
    private TabLayout tab;
    private ViewPager vp;
    private View contentView;
    private PopupWindow popupWindow;
    private View parent;
    private TextView tv1;
    private TextView tv2;
    private TextView tv3;
    private List<String> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dingdanliebiao);
        image_liebiao = findViewById(R.id.iv);
        tab = findViewById(R.id.tab);
        vp = findViewById(R.id.vp);
        contentView = View.inflate(DingDanLieBiaoActivity.this, R.layout.pop_layout, null);
        //父窗体
        parent = View.inflate(DingDanLieBiaoActivity.this, R.layout.activity_dingdanliebiao, null);
        //通过构造方法创建一个popupWindown
        popupWindow = new PopupWindow(contentView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        /**
         * 出现的问题,,,点击周围不消失,,点击返回键直接退出这个activity...里面的editText控件不能输入
         */
        popupWindow.setTouchable(true);//设置窗体可以触摸,,,默认就是true
        popupWindow.setFocusable(true);//让窗体获取到焦点...一般情况下窗体里面的控件都能获取到焦点,但是editText特殊

        popupWindow.setOutsideTouchable(true);//设置窗体外部可以触摸
        popupWindow.setBackgroundDrawable(new BitmapDrawable());//设置背景

        tv1 = contentView.findViewById(R.id.tv1);
        tv2 = contentView.findViewById(R.id.tv2);
        tv3 = contentView.findViewById(R.id.tv3);

        image_liebiao.setOnClickListener(this);
        tv1.setOnClickListener(this);
        tv2.setOnClickListener(this);
        tv3.setOnClickListener(this);

        list = new ArrayList<>();
        list.add("待支付");
        list.add("已支付");
        list.add("已取消");
        vp.setOffscreenPageLimit(list.size());
        vp.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public CharSequence getPageTitle(int position) {
                return list.get(position);
            }

            @Override
            public Fragment getItem(int position) {
                Fragment fragment=null;
                if (list.get(position)=="待支付"){
                    fragment=new FragmentDaiZhifu();
                }else if (list.get(position)=="已支付"){
                    fragment=new FragmentYiZhiFu();
                }else if (list.get(position)=="已取消"){
                    fragment=new FragmentYiQuXiao();
                }
                return fragment;
            }

            @Override
            public int getCount() {
                return list.size();
            }
        });
        tab.setupWithViewPager(vp);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv:
                popupWindow.showAsDropDown(image_liebiao);
                break;
            case R.id.tv1:
                vp.setCurrentItem(0);
                Toast.makeText(DingDanLieBiaoActivity.this, "点击了待支付!", Toast.LENGTH_SHORT).show();
                popupWindow.dismiss();
                break;
            case R.id.tv2:
                vp.setCurrentItem(1);
                Toast.makeText(DingDanLieBiaoActivity.this, "点击了已支付!", Toast.LENGTH_SHORT).show();
                popupWindow.dismiss();
                break;
            case R.id.tv3:
                vp.setCurrentItem(2);
                Toast.makeText(DingDanLieBiaoActivity.this, "点击了已取消!", Toast.LENGTH_SHORT).show();
                popupWindow.dismiss();
                break;
        }
    }
}
