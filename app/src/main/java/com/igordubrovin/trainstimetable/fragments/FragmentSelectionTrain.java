package com.igordubrovin.trainstimetable.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.igordubrovin.trainstimetable.R;
import com.igordubrovin.trainstimetable.customView.CustomEditText;
import com.igordubrovin.trainstimetable.utils.ConstProject;

/**
 * Created by Игорь on 21.02.2017.
 */

public class FragmentSelectionTrain extends Fragment {
    TextView tvInformFragment;
    ProgressBar pbLoad;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_selection_train, null);
        tvInformFragment = (TextView)view.findViewById(R.id.tvInformFragment);
        pbLoad = (ProgressBar)view.findViewById(R.id.pbLoad);

        TextView tv = (TextView)getActivity().findViewById(R.id.etSearchToStation);
        if (tv.getText().toString().equals("")) tvInformFragment.setText(ConstProject.INF_ENTER_TO);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        EditText etFrom = (EditText) getActivity().findViewById(R.id.etSearchFromStation);
        EditText etTo = (EditText) getActivity().findViewById(R.id.etSearchToStation);
        String stationFrom = etFrom.getText().toString();
        String stationTo = etTo.getText().toString();
        if (stationFrom.equals("") || stationTo.equals("")){
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
            pbLoad.setVisibility(View.VISIBLE);
            tvInformFragment.setVisibility(View.GONE);
        }
    }
}
