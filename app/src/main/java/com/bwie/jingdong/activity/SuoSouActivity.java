package com.bwie.jingdong.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bwie.jingdong.R;
import com.bwie.jingdong.custom.LiuShiBuJu;
import com.bwie.jingdong.dao.Dao;

import java.util.ArrayList;
import java.util.List;

public class SuoSouActivity extends AppCompatActivity {
    private String mNames[] = {
            "袜子", "汾酒", "婴儿爬行垫",
            "电压力锅", "煲汤锅砂锅"};
    private ImageView image_fanhui;
    private EditText text_name;
    private LiuShiBuJu ls;
    private ListView lv;
    private Dao dao;
    private List<String> select;
    private Button btn;
    private ArrayAdapter<String> adapter;
    List<String> a=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suo_sou);
        image_fanhui = findViewById(R.id.image_fanhui);
        text_name = findViewById(R.id.text_name);
        ls = findViewById(R.id.ls);
        lv = findViewById(R.id.lv);
        btn = findViewById(R.id.btn);
        image_fanhui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        dao = new Dao(SuoSouActivity.this);
        select = dao.select();
        adapter = new ArrayAdapter<String>(SuoSouActivity.this,android.R.layout.simple_list_item_1, android.R.id.text1,select);
        lv.setAdapter(adapter);
        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int d, long l) {
                AlertDialog.Builder builder = new AlertDialog.Builder(SuoSouActivity.this);
                builder.setTitle("是否删除");
                builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        int delYi = dao.delYi(select.get(d).toString());
                        if (delYi==1){
                            zhanshi();
                        }
                    }
                });
                builder.setNegativeButton("取消",null);
                builder.show();
                return false;
            }
        });

        if (select.size()>0){
            btn.setVisibility(View.VISIBLE);
        }else if(select.size()==0){
            btn.setVisibility(View.INVISIBLE);
        }
        initChildViews();
    }

    private void zhanshi() {
        List<String> sel4 = dao.select();
        ArrayAdapter<String> ada = new ArrayAdapter<String>(SuoSouActivity.this, android.R.layout.simple_list_item_1, android.R.id.text1, sel4);
        lv.setAdapter(ada);
    }

    private void initChildViews() {
        ViewGroup.MarginLayoutParams lp = new ViewGroup.MarginLayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.leftMargin = 10;
        lp.rightMargin = 10;
        lp.topMargin = 5;
        lp.bottomMargin = 5;
        for (int i = 0; i < mNames.length; i++) {
            TextView view = new TextView(this);
            view.setText(mNames[i]);
            view.setTextColor(Color.WHITE);
            final int finalI = i;
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(SuoSouActivity.this,mNames[finalI], Toast.LENGTH_SHORT).show();
                }
            });
            view.setBackgroundDrawable(getResources().getDrawable(R.drawable.textview_bg));
            ls.addView(view, lp);
        }
    }

    public void delall(View view) {
        dao.del();
        List<String> select2 = dao.select();
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(SuoSouActivity.this, android.R.layout.simple_list_item_1, android.R.id.text1, select2);
        lv.setAdapter(adapter2);
        Toast.makeText(this, "清楚成功", Toast.LENGTH_SHORT).show();
        //没有记录隐藏
        btn.setVisibility(View.INVISIBLE);
    }

    public void add(View view) {
        String keywords = text_name.getText().toString();
        dao.insert(keywords);
        btn.setVisibility(View.VISIBLE);

        List<String> sel3 = dao.select();
        a.add(0,keywords);
        ArrayAdapter<String> adapter3 = new ArrayAdapter<>(SuoSouActivity.this, android.R.layout.simple_list_item_1, android.R.id.text1, a);
        lv.setAdapter(adapter3);

        Intent intent = new Intent(SuoSouActivity.this,LieBiaoActivity.class);
        intent.putExtra("keywords",keywords);
        startActivity(intent);
    }
}