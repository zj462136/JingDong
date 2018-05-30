package com.bwie.jingdong.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bwie.jingdong.R;
import com.bwie.jingdong.adapter.GridAdapter2;
import com.bwie.jingdong.adapter.ListViewAdapter2;
import com.bwie.jingdong.bean.ZiZiFenLeiBean;
import com.bwie.jingdong.custom.LiuShiBuJu;
import com.bwie.jingdong.inter.ItemClickListener;
import com.bwie.jingdong.presenter.ZiZiFenLeiPresenter;
import com.bwie.jingdong.util.ApiUtil;
import com.bwie.jingdong.view.IZiZiFenLeiView;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LieBiaoActivity2 extends AppCompatActivity implements IZiZiFenLeiView{
    private String mNames[] = {
            "袜子", "汾酒",
            "电压力锅", "煲汤锅砂锅",
            "汾酒","汾酒"};
    private ImageView image_fanhui;
    private EditText et_name;
    private XRecyclerView xrv;
    private CheckBox cb;
    private LiuShiBuJu ls;
    private int page=1;
    private boolean flag=false;
    private GridAdapter2 gridAdapter;
    private ListViewAdapter2 listViewAdapter;
    private int pscid;
    private ZiZiFenLeiPresenter ziZiFenLeiPresenter;
    private ZiZiFenLeiBean ziZiFenLeiBean;
    private List<ZiZiFenLeiBean.DataBean> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lie_biao);
        image_fanhui = findViewById(R.id.image_fanhui);
        et_name = findViewById(R.id.et_name);
        xrv = findViewById(R.id.xrv);
        cb = findViewById(R.id.cb);
        ls = findViewById(R.id.ls);
        image_fanhui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        xrv.setLayoutManager(layoutManager);
//        xrv.setLoadingListener(this);
        pscid = getIntent().getIntExtra("pscid", 0);
        ziZiFenLeiPresenter = new ZiZiFenLeiPresenter(this);
        Map<String,String> params1=new HashMap<>();
        params1.put("page", String.valueOf(page));
        params1.put("pscid", String.valueOf(pscid));
        ziZiFenLeiPresenter.getData(ApiUtil.ZIZIFENLEIURL,params1);
        initChildViews();
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
                    Toast.makeText(LieBiaoActivity2.this,mNames[finalI], Toast.LENGTH_SHORT).show();
                }
            });
            view.setBackgroundDrawable(getResources().getDrawable(R.drawable.textview_bg));
            ls.addView(view, lp);
        }
    }
    @Override
    public void getSuccess(final ZiZiFenLeiBean ziZiFenLeiBean) {
        this.ziZiFenLeiBean=ziZiFenLeiBean;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                data = ziZiFenLeiBean.getData();
                Toast.makeText(LieBiaoActivity2.this, ziZiFenLeiBean.getMsg(), Toast.LENGTH_SHORT).show();
                    setLvAdapter();
                    cb.setChecked(flag);
                    if (cb.isChecked()) {
                        setLvAdapter();
                        gridAdapter.notifyDataSetChanged();
                    } else {
                        setLvAdapter();
                        listViewAdapter.notifyDataSetChanged();
                    }
                    cb.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (flag) {
                                setLvAdapter();
                                cb.setChecked(false);
                                flag = cb.isChecked();
                            } else {
                                setGvAdapter();
                                cb.setChecked(true);
                                flag = cb.isChecked();
                            }
                        }
                    });

                    xrv.setLoadingListener(new XRecyclerView.LoadingListener() {
                        @Override
                        public void onRefresh() {
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    data.clear();
                                    Map<String,String> params1=new HashMap<>();
                                    params1.put("page", String.valueOf(page));
                                    params1.put("pscid", String.valueOf(pscid));
                                    ziZiFenLeiPresenter.getData(ApiUtil.ZIZIFENLEIURL,params1);
                                    xrv.refreshComplete();
                                }
                            }, 2000);
                        }

                        @Override
                        public void onLoadMore() {
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    page++;
                                    Map<String,String> params1=new HashMap<>();
                                    params1.put("page", String.valueOf(page));
                                    params1.put("pscid", String.valueOf(pscid));
                                    ziZiFenLeiPresenter.getData(ApiUtil.ZIZIFENLEIURL,params1);
                                    xrv.loadMoreComplete();

                                }
                            }, 2000);
                        }
                    });
            }
        });
    }

    private void setGvAdapter() {
        gridAdapter = new GridAdapter2(LieBiaoActivity2.this, data);
        xrv.setAdapter(gridAdapter);
        xrv.setLayoutManager(new StaggeredGridLayoutManager(2, GridLayoutManager.VERTICAL));

        gridAdapter.setItemClickListener(new ItemClickListener() {
            @Override
            public void setItemClickListener(int position) {
                int pid = ziZiFenLeiBean.getData().get(position).getPid();
                Intent intent = new Intent(LieBiaoActivity2.this, GoodsActivity.class);
                intent.putExtra("pid",pid);
                startActivity(intent);
            }

            @Override
            public void setItemLongClickListener(int position) {
                int pid = ziZiFenLeiBean.getData().get(position).getPid();
                Intent intent = new Intent(LieBiaoActivity2.this, GoodsActivity.class);
                intent.putExtra("pid",pid);
                startActivity(intent);
            }
        });
    }

    private void setLvAdapter() {
        listViewAdapter = new ListViewAdapter2(LieBiaoActivity2.this,data);
        xrv.setAdapter(listViewAdapter);
        xrv.setLayoutManager(new LinearLayoutManager(LieBiaoActivity2.this,LinearLayoutManager.VERTICAL,false));
        listViewAdapter.setItemClickListener(new ItemClickListener() {
            @Override
            public void setItemClickListener(int position) {
                int pid = ziZiFenLeiBean.getData().get(position).getPid();
                Intent intent = new Intent(LieBiaoActivity2.this, GoodsActivity.class);
                intent.putExtra("pid",pid);
                startActivity(intent);
            }

            @Override
            public void setItemLongClickListener(int position) {
                int pid = ziZiFenLeiBean.getData().get(position).getPid();
                Intent intent = new Intent(LieBiaoActivity2.this, GoodsActivity.class);
                intent.putExtra("pid",pid);
                startActivity(intent);
            }
        });
    }

}
