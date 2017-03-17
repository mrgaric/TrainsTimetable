package com.igordubrovin.trainstimetable.fragments;

import com.igordubrovin.trainstimetable.utils.Train;
import com.igordubrovin.trainstimetable.utils.UrlDirector;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Игорь on 17.03.2017.
 */

public class FragmentTrainsDate extends FragmentTrains<FragmentTrainsDate.LoaderTrains> {

    private String dayDeparture;
    private String monthDeparture;

    private String newDayDeparture;
    private String newMonthDeparture;

    @Override
    protected LoaderTrains createLoader() {
        return new LoaderTrains();
    }

    @Override
    public void startLoaderTrains() {
        if (trainList == null || (!dayDeparture.equals(newDayDeparture) && !monthDeparture.equals(newMonthDeparture))){
            super.startLoaderTrains();
            dayDeparture = newDayDeparture;
            monthDeparture = newMonthDeparture;
        }
    }

    @Override
    protected String getUrl() {
        UrlDirector urlDirector = new UrlDirector();
        return urlDirector
                .createUrlAddressDateDeparture(stationFrom, stationTo, dayDeparture, monthDeparture)
                .getUrlAddress()
                .getUrl();
    }

    public void setNewDayDeparture(String newDayDeparture) {
        this.newDayDeparture = newDayDeparture;
    }

    public void setNewMonthDeparture(String newMonthDeparture) {
        this.newMonthDeparture = newMonthDeparture;
    }

    class LoaderTrains extends FragmentTrains.LoaderHtml {
        @Override
        protected void onPostParse(List<Train> trains) {
            trainList = new ArrayList<>(trains);
            updateAdapter(trainList);
        }
    }
}
