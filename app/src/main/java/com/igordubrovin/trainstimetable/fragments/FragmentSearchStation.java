package com.igordubrovin.trainstimetable.fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.igordubrovin.trainstimetable.R;
import com.igordubrovin.trainstimetable.adapters.AdapterSearchStation;
import com.igordubrovin.trainstimetable.utils.ConstProject;
import com.igordubrovin.trainstimetable.utils.CursorLoaderForDB;

public class FragmentSearchStation extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{
    RecyclerView rvSearchStation;
    AdapterSearchStation adapterSearchStation;
    View view = null;
    OnSelectStationListener listener;

    public static FragmentSearchStation newInstance(String partStationName){
        FragmentSearchStation fragmentSearchStation = new FragmentSearchStation();
        Bundle args = new Bundle();
        args.putString(ConstProject.PART_STATION_NAME, partStationName);
        fragmentSearchStation.setArguments(args);
        return fragmentSearchStation;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        adapterSearchStation = new AdapterSearchStation();
        adapterSearchStation.setOnItemRecyclerViewClickListener(new AdapterSearchStation.OnItemRecyclerViewClickListener() {
            @Override
            public void onItemRecyclerViewClickListener(View view, int code) {
                listener.onSelectStation(view, ((TextView)view).getText().toString(), code);
            }
        });

        searchStation(getArguments());

        this.setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_search_station, container, false);
        }

        LinearLayoutManager linearLayoutManager;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            linearLayoutManager = new LinearLayoutManager(getContext());
        } else {
            linearLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        }

        rvSearchStation = (RecyclerView) view.findViewById(R.id.rvSearchStation);
        rvSearchStation.setLayoutManager(linearLayoutManager);
        rvSearchStation.setAdapter(adapterSearchStation);

        return view;
    }

    /*вспомогательные методы*/
    public void searchStation(Bundle bundle){
        getActivity().getSupportLoaderManager().restartLoader(0, bundle, FragmentSearchStation.this);
    }

    /*interface callback*/
    public void setOnSelectStation(OnSelectStationListener l){
        listener = l;
    }

    public interface OnSelectStationListener{
        void onSelectStation(View v, String station, int code);
    }

    /*implements methods*/
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoaderForDB(getContext(), args);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        adapterSearchStation.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
