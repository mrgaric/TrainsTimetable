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
import com.igordubrovin.trainstimetable.utils.ConstProject;

/**
 * Created by Игорь on 21.02.2017.
 */

public class FragmentSelectionTrain extends Fragment {
    String stationFrom = "";
    String stationTo = "";

    TextView tvInformFragment;
    ProgressBar pbLoad;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null){
            stationFrom = getArguments().getString("stationFrom");
            stationTo = getArguments().getString("stationTo");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_selection_train, null);
        tvInformFragment = (TextView)view.findViewById(R.id.tvInformFragment);
        pbLoad = (ProgressBar)view.findViewById(R.id.pbLoad);

        TextView tv = (TextView)getActivity().findViewById(R.id.etSearchToStation);
        if (tv.getText().toString().equals("")) tvInformFragment.setText(ConstProject.INF_ENTER_TO);

       /* if ((stationFrom.equals("") || stationTo.equals(""))){
            pbLoad.setVisibility(View.GONE);
            tvInformFragment.setVisibility(View.VISIBLE);
            if (stationFrom.equals("")){
                tvInformFragment.setText(ConstProject.INF_ENTER_FROM);
            }
            else {
                tvInformFragment.setText(ConstProject.INF_ENTER_TO);
            }
        }
        else {
            tvInformFragment.setVisibility(View.GONE);
            pbLoad.setVisibility(View.VISIBLE);
        }*/

        return view;
    }

    public void changeMainView(String stationFrom, String stationTo){
        this.stationFrom = stationFrom;
        this.stationTo = stationTo;
    }
}
