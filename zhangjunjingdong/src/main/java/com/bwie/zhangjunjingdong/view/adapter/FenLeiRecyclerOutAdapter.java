package com.bwie.zhangjunjingdong.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.bwie.zhangjunjingdong.R;
import com.bwie.zhangjunjingdong.model.bean.ChildFenLeiBean;
import com.bwie.zhangjunjingdong.view.Iview.OnItemListner;
import com.bwie.zhangjunjingdong.view.activity.ProductListActivity;
import com.bwie.zhangjunjingdong.view.hodler.FenLeiRecyclerOutHolder;

public class FenLeiRecyclerOutAdapter extends RecyclerView.Adapter<FenLeiRecyclerOutHolder>{
    private ChildFenLeiBean childFenLeiBean;
    private Context context;

    public FenLeiRecyclerOutAdapter(Context context, ChildFenLeiBean childFenLeiBean) {
        this.context = context;
        this.childFenLeiBean = childFenLeiBean;
    }

    @Override
    public FenLeiRecyclerOutHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = View.inflate(context, R.layout.reycler_out_item_layout,null);
        FenLeiRecyclerOutHolder fenLeiRecyclerOutHolder = new FenLeiRecyclerOutHolder(view);

        return fenLeiRecyclerOutHolder;
    }

    @Override
    public void onBindViewHolder(FenLeiRecyclerOutHolder holder, int position) {

        final ChildFenLeiBean.DataBean dataBean = childFenLeiBean.getData().get(position);
        holder.recycler_out_text.setText(dataBean.getName());

        holder.recycler_innner.setLayoutManager(new GridLayoutManager(context,3));
        FenRecyclerInnerAdapter fenRecyclerInnerAdapter = new FenRecyclerInnerAdapter(context, dataBean);
        holder.recycler_innner.setAdapter(fenRecyclerInnerAdapter);

        fenRecyclerInnerAdapter.setOnItemListner(new OnItemListner() {
            @Override
            public void onItemClick(int i) {
                 Intent intent = new Intent(context, ProductListActivity.class);


                intent.putExtra("keywords",dataBean.getList().get(i).getName());

                context.startActivity(intent);

            }

            @Override
            public void onItemLongClick(int i) {

            }
        });

    }

    @Override
    public int getItemCount() {
        return childFenLeiBean.getData().size();
    }
}
