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
import com.igordubrovin.trainstimetable.utils.CursorLoaderForDB;

/**
 * Created by Игорь on 21.02.2017.
 */

public class FragmentSearchStation extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{
    RecyclerView rvSearchStation;
    AdapterSearchStation adapterSearchStation;
    TextView tvHintSearch;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapterSearchStation = new AdapterSearchStation();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_station, null);

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

        tvHintSearch = (TextView)view.findViewById(R.id.tvHintSearch);

        return view;
    }

    public void searchStation(Bundle bundle){
        rvSearchStation.setVisibility(View.VISIBLE);
        tvHintSearch.setVisibility(View.GONE);
        getActivity().getSupportLoaderManager().restartLoader(0, bundle, FragmentSearchStation.this);
    }

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
