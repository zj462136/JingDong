package com.bwie.zhangjunjingdong.view.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;

import com.bwie.zhangjunjingdong.R;
import com.bwie.zhangjunjingdong.model.bean.FenLeiBean;
import com.bwie.zhangjunjingdong.model.bean.HomeBean;
import com.bwie.zhangjunjingdong.presenter.FragmentHomeP;
import com.bwie.zhangjunjingdong.util.ApiUtil;
import com.bwie.zhangjunjingdong.util.ChenJinUtil;
import com.bwie.zhangjunjingdong.view.Iview.InterFragmentHome;
import com.bwie.zhangjunjingdong.view.adapter.FenLeiAdapter;

public class FragmentFenLei extends Fragment implements InterFragmentHome {

    private ListView fen_lei_list_view;
    private FrameLayout fen_lei_frame;
    private FragmentHomeP fragmentHomeP;
    private FenLeiAdapter fenLeiAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_fen_lei_layout,container,false);

        fen_lei_list_view = view.findViewById(R.id.fen_lei_list_view);
        fen_lei_frame = view.findViewById(R.id.fen_lei_frame);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initChenJin();
        fragmentHomeP = new FragmentHomeP(this);

        fragmentHomeP.getFenLeiData(ApiUtil.FEN_LEI_URL);

    }

    @Override
    public void onSuccess(HomeBean homeBean) {

    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);

        if (! hidden) {
            initChenJin();
        }
    }

    private void initChenJin() {
        ChenJinUtil.setStatusBarColor(getActivity(), getResources().getColor(R.color.colorPrimaryDark));
    }

    @Override
    public void onFenLeiDataSuccess(final FenLeiBean fenLeiBean) {

        fenLeiAdapter = new FenLeiAdapter(getActivity(), fenLeiBean);
        fen_lei_list_view.setAdapter(fenLeiAdapter);
        FragmentFenLeiRight fragmentFenLeiRight = FragmentFenLeiRight.getInstance(fenLeiBean.getData().get(0).getCid());

        getChildFragmentManager().beginTransaction().replace(R.id.fen_lei_frame,fragmentFenLeiRight).commit();

        fen_lei_list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                fenLeiAdapter.setCurPositon(i);
                fenLeiAdapter.notifyDataSetChanged();
                fen_lei_list_view.smoothScrollToPositionFromTop(i,(adapterView.getHeight()-view.getHeight())/2);

                FragmentFenLeiRight fragmentFenLeiRight = FragmentFenLeiRight.getInstance(fenLeiBean.getData().get(i).getCid());


                getChildFragmentManager().beginTransaction().replace(R.id.fen_lei_frame,fragmentFenLeiRight).commit();


            }
        });

    }
}
