package com.igordubrovin.trainstimetable.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.igordubrovin.trainstimetable.R;
import com.igordubrovin.trainstimetable.adapters.AdapterSelectionTrain;
import com.igordubrovin.trainstimetable.utils.LoaderHtml;
import com.igordubrovin.trainstimetable.utils.Train;
import com.igordubrovin.trainstimetable.utils.UrlDirector;

import java.util.List;

/**
 * Created by Игорь on 16.03.2017.
 */

public abstract class FragmentTrains <LoaderTrains extends LoaderHtml> extends Fragment{

    private TextView tvInformFragment;
    private RecyclerView rvSelection;
    private AdapterSelectionTrain adapterRvSelection;
    private boolean infoFlag;

    protected LoaderTrains loaderTrains;
    protected UrlDirector urlDirector = new UrlDirector();
    protected String stationFrom;
    protected String stationTo;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapterRvSelection = new AdapterSelectionTrain();
        infoFlag = false;
        this.setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_selection_train, container, false);

        tvInformFragment = (TextView)view.findViewById(R.id.tvInformFragment);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rvSelection = (RecyclerView) view.findViewById(R.id.rvSelection);
        rvSelection.setLayoutManager(linearLayoutManager);
        rvSelection.setAdapter(adapterRvSelection);

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (loaderTrains != null)
            loaderTrains.cancel(false);
    }

    public void setStation(String stationFrom, String stationTo){
        this.stationFrom = stationFrom;
        this.stationTo = stationTo;
    }

    protected void choiceView(){
        if (!infoFlag){
            tvInformFragment.setVisibility(View.VISIBLE);
            rvSelection.setVisibility(View.GONE);
        } else {
            tvInformFragment.setVisibility(View.GONE);
            rvSelection.setVisibility(View.VISIBLE);
        }
    }

    protected void trainsLoaded(List<Train> trains){
        adapterRvSelection.swapData(trains);
    }

    protected void setInfoFlag(boolean state){
        infoFlag = state;
    }

    abstract public void startLoaderTrains();

}
