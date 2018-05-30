package com.bwie.zhangjunjingdong.view.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bwie.zhangjunjingdong.R;
import com.bwie.zhangjunjingdong.model.bean.ChildFenLeiBean;
import com.bwie.zhangjunjingdong.presenter.FragmentFenLeiRightPresenter;
import com.bwie.zhangjunjingdong.util.ApiUtil;
import com.bwie.zhangjunjingdong.view.Iview.FenLeiRightInter;
import com.bwie.zhangjunjingdong.view.adapter.FenLeiRecyclerOutAdapter;

public class FragmentFenLeiRight extends Fragment implements FenLeiRightInter {

    private RecyclerView fen_lei_recycler_out;
    private FragmentFenLeiRightPresenter fragmentFenLeiRightPresenter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fen_lei_right_layout,container,false);

        fen_lei_recycler_out = view.findViewById(R.id.fen_lei_recycler_out);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        fragmentFenLeiRightPresenter = new FragmentFenLeiRightPresenter(this);

        int cid = getArguments().getInt("cid", -1);
        if (cid != -1) {
            fragmentFenLeiRightPresenter.getChildData(ApiUtil.CHILD_FEN_LEI_URL,cid);
        }

    }

    public static FragmentFenLeiRight getInstance(int cid) {
        FragmentFenLeiRight fragmentFenLeiRight = new FragmentFenLeiRight();

        Bundle bundle = new Bundle();
        bundle.putInt("cid",cid);
        fragmentFenLeiRight.setArguments(bundle);

        return fragmentFenLeiRight;
    }

    @Override
    public void getSuccessChildData(ChildFenLeiBean childFenLeiBean) {


        fen_lei_recycler_out.setLayoutManager(new LinearLayoutManager(getActivity()));
        FenLeiRecyclerOutAdapter fenLeiRecyclerOutAdapter = new FenLeiRecyclerOutAdapter(getActivity(), childFenLeiBean);
        fen_lei_recycler_out.setAdapter(fenLeiRecyclerOutAdapter);

    }
}
