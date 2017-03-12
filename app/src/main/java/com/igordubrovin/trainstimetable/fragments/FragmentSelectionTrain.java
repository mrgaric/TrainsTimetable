package com.igordubrovin.trainstimetable.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.igordubrovin.trainstimetable.R;
import com.igordubrovin.trainstimetable.adapters.AdapterSelectionTrain;
import com.igordubrovin.trainstimetable.utils.HtmlHelper;
import com.igordubrovin.trainstimetable.utils.SingletonHtmlHelper;

import java.util.List;
import java.util.Map;

/**
 * Created by Игорь on 21.02.2017.
 */

public class FragmentSelectionTrain extends Fragment implements HtmlHelper.LoadEndListener {

    ProgressBar pbLoad;
    TextView tvInformFragment;
    RecyclerView rvSelection;
    AdapterSelectionTrain adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new AdapterSelectionTrain();
        this.setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_selection_train, container, false);

        tvInformFragment = (TextView)view.findViewById(R.id.tvInformFragment);
        pbLoad = (ProgressBar)view.findViewById(R.id.pbLoad);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rvSelection = (RecyclerView) view.findViewById(R.id.rvSelection);
        rvSelection.setLayoutManager(linearLayoutManager);
        rvSelection.setAdapter(adapter);

        /*pbLoad.setVisibility(View.GONE);
        tvInformFragment.setVisibility(View.VISIBLE);*/
        pbLoad.setVisibility(View.GONE);
        tvInformFragment.setVisibility(View.GONE);
        rvSelection.setVisibility(View.VISIBLE);
        return view;
    }

    public void immediate(){
        SingletonHtmlHelper.getInstance(this).startLoad("Москва", "Зеленоград");
    }

    public void forDay(){
        SingletonHtmlHelper.getInstance(this).startLoad("Москва", "Одинцово");
    }

    public void choiceDate(){
        SingletonHtmlHelper.getInstance(this).startLoad("Москва", "Зеленоград");
    }

    @Override
    public void loadEnd(List<Map<String, String>> trainsList, List<Map<String, String>> trainsGoneList) {
        adapter.swapData(trainsList);
    }

    /*@Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (getActivity() instanceof MainActivity){
            boolean fromIsClear = ((MainActivity)getActivity()).getCetFromIsClear();
            boolean toIsClear = ((MainActivity)getActivity()).getCetToIsClear();
            if (fromIsClear || toIsClear){
                if (fromIsClear){
                    tvInformFragment.setText(ConstProject.INF_ENTER_FROM);
                }
                else {
                    tvInformFragment.setText(ConstProject.INF_ENTER_TO);
                }
            } else {
                svTvInfo.setVisibility(View.GONE);
                svPbInfo.setVisibility(View.VISIBLE);
            }
        }
    }*/
}
