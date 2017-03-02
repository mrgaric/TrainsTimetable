package com.igordubrovin.trainstimetable.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.igordubrovin.trainstimetable.R;

/**
 * Created by Игорь on 21.02.2017.
 */

public class FragmentSelectionTrain extends Fragment {

    ProgressBar pbLoad;
    TextView tvInformFragment;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_selection_train, container, false);

        tvInformFragment = (TextView)view.findViewById(R.id.tvInformFragment);
        pbLoad = (ProgressBar)view.findViewById(R.id.pbLoad);

        pbLoad.setVisibility(View.GONE);
        tvInformFragment.setVisibility(View.VISIBLE);

        return view;
    }

    public void immediate(){

    }

    public void forDay(){

    }

    public void choiceDate(){

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
