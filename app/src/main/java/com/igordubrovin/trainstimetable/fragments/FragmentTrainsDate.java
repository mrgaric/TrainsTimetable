package com.igordubrovin.trainstimetable.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.igordubrovin.trainstimetable.utils.Train;
import com.igordubrovin.trainstimetable.utils.UrlDirector;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Игорь on 17.03.2017.
 */

public class FragmentTrainsDate extends FragmentTrains<FragmentTrainsDate.LoaderTrains> {

    private FloatingActionButton fabCalendar;

    private String dayDeparture;
    private String monthDeparture;

    private static String newDayDeparture;
    private static String newMonthDeparture;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);

        /*ConstraintLayout layout = (ConstraintLayout) view.findViewById(R.id.constrain_layout_train_fragment);
        ConstraintSet set = new ConstraintSet();
        fabCalendar = new FloatingActionButton(getContext());
        layout.addView(fabCalendar, 0);
        set.clone(layout);
        set.connect(fabCalendar.getId(), ConstraintSet.TOP, layout.getId(), ConstraintSet.TOP, 8);
        set.connect(fabCalendar.getId(), ConstraintSet.RIGHT, layout.getId(), ConstraintSet.RIGHT, 8);
        set.applyTo(layout);*/

        return view;
    }

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

    public static void setNewDayDeparture(String dayDeparture) {
        newDayDeparture = dayDeparture;
    }

    public static void setNewMonthDeparture(String monthDeparture) {
        newMonthDeparture = monthDeparture;
    }

    class LoaderTrains extends FragmentTrains.LoaderHtml {
        @Override
        protected void onPostParse(List<Train> trains) {
            trainList = new ArrayList<>(trains);
            updateAdapter(trainList);
        }
    }
}
