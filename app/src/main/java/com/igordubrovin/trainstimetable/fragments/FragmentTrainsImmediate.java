package com.igordubrovin.trainstimetable.fragments;

import com.igordubrovin.trainstimetable.utils.Train;
import com.igordubrovin.trainstimetable.utils.UrlDirector;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Игорь on 16.03.2017.
 */

public class FragmentTrainsImmediate extends FragmentTrains<FragmentTrainsImmediate.LoaderTrains> {
    @Override
    protected String getUrl() {
        UrlDirector urlDirector = new UrlDirector();
        return urlDirector
                .createUrlAddressOnlyStation(stationFrom, stationTo)
                .getUrlAddress()
                .getUrl();
    }

    @Override
    protected LoaderTrains createLoader() {
        return new LoaderTrains();
    }


    class LoaderTrains extends FragmentTrains.LoaderHtml {
        @Override
        protected void onPostParse(List<Train> trains) {
            for (int i = 0; i < trains.size() - 1; i++) {
                if (!trains.get(i).getTimeBeforeDeparture().equals("")) {
                    trains.subList(0, i).clear();
                    trainList = new ArrayList<>(trains);
                    break;
                }
            }
            updateAdapter(trainList);
        }
    }
}
