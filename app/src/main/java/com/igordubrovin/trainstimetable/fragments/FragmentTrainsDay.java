package com.igordubrovin.trainstimetable.fragments;

import com.igordubrovin.trainstimetable.utils.Train;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Игорь on 17.03.2017.
 */

public class FragmentTrainsDay extends FragmentTrainsImmediate {

    private String stationFrom;
    private String stationTo;

    @Override
    protected FragmentTrainsImmediate.LoaderTrains createLoader() {
        return new LoaderTrains();
    }

    @Override
    public void startLoaderTrains() {
        if (trainList == null || (!stationFrom.equals(newStationFrom)) || !stationTo.equals(newStationTo)) {
            super.startLoaderTrains();
            stationFrom = newStationFrom;
            stationTo = newStationTo;
        }
        else stopRefresh();
    }

    public void clearTrainsDay(){
        trainList = null;
    }

    private class LoaderTrains extends FragmentTrainsImmediate.LoaderTrains {
        @Override
        protected void onPostParse(List<Train> trains) {
            trainList = new ArrayList<>(trains);
            updateDataAdapter(trainList);
        }
    }
}
