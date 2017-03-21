package com.igordubrovin.trainstimetable.fragments;

import com.igordubrovin.trainstimetable.utils.Train;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Игорь on 17.03.2017.
 */

public class FragmentTrainsDay extends FragmentTrainsImmediate {

    @Override
    protected FragmentTrainsImmediate.LoaderTrains createLoader() {
        return new LoaderTrains();
    }

    @Override
    public void startLoaderTrains() {
        if (trainList == null)
            super.startLoaderTrains();
        else stopRefresh();
    }

    private class LoaderTrains extends FragmentTrainsImmediate.LoaderTrains {
        @Override
        protected void onPostParse(List<Train> trains) {
            trainList = new ArrayList<>(trains);
            updateDataAdapter(trainList);
        }
    }
}
