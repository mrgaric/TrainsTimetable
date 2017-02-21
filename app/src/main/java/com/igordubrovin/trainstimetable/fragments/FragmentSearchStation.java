package com.igordubrovin.trainstimetable.fragments;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.igordubrovin.trainstimetable.R;
import com.igordubrovin.trainstimetable.adapters.AdapterSearchStation;

/**
 * Created by Игорь on 21.02.2017.
 */

public class FragmentSearchStation extends Fragment {
    RecyclerView rvSearchStation;
    AdapterSearchStation adapterSearchStation;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_station, null);

        adapterSearchStation = new AdapterSearchStation();

        LinearLayoutManager linearLayoutManager;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            linearLayoutManager = new LinearLayoutManager(getContext());
        }
        else {
            linearLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        }

        rvSearchStation = (RecyclerView)view.findViewById(R.id.rvSearchStation);
        rvSearchStation.setLayoutManager(linearLayoutManager);
        rvSearchStation.setAdapter(adapterSearchStation);

        return view;
    }
}
