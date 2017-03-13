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
import android.widget.ProgressBar;
import android.widget.TextView;

import com.igordubrovin.trainstimetable.R;
import com.igordubrovin.trainstimetable.adapters.AdapterSelectionTrain;
import com.igordubrovin.trainstimetable.utils.HtmlHelper;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Игорь on 21.02.2017.
 */

public class FragmentSelectionTrain extends Fragment {

    private static final String DAY_DEPARTURE = "dayDeparture";
    private static final String MONTH_DEPARTURE = "monthDeparture";

    ProgressBar pbLoad;
    TextView tvInformFragment;
    RecyclerView rvSelection;
    AdapterSelectionTrain adapter;
    LoadHtml loadHtmlImmediate;
    LoadHtml loadHtmlDay;
    LoadHtml loadHtmlDate;
    List<Map<String, String>> listTrainForImmediate;
    List<Map<String, String>> listTrainForDay;
    List<Map<String, String>> listTrainForDate;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new AdapterSelectionTrain();
        listTrainForImmediate = new ArrayList<>();
        listTrainForDay = new ArrayList<>();
        listTrainForDate = new ArrayList<>();
        this.setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_selection_train, container, false);

        tvInformFragment = (TextView)view.findViewById(R.id.tvInformFragment);
        pbLoad = (ProgressBar)view.findViewById(R.id.pbLoad);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rvSelection = (RecyclerView) view.findViewById(R.id.rvSelection);
        rvSelection.setLayoutManager(linearLayoutManager);
        rvSelection.setAdapter(adapter);

        /*pbLoad.setVisibility(View.GONE);
        tvInformFragment.setVisibility(View.VISIBLE);*/
        pbLoad.setVisibility(View.GONE);
        tvInformFragment.setVisibility(View.GONE);
        rvSelection.setVisibility(View.VISIBLE);
        return view;
    }

    @Override
    public void onDestroy() {
        if (loadHtmlImmediate != null)
            loadHtmlImmediate.cancel(false);
        if (loadHtmlDate != null)
            loadHtmlDate.cancel(false);
        if (loadHtmlDay != null)
            loadHtmlDay.cancel(false);
        super.onDestroy();
    }

    public void immediate(String stationFrom, String stationTo){
        String url = "https://t.rasp.yandex.ru/search/suburban/?toName=" + stationFrom + "&fromName=" + stationTo;
        loadHtmlImmediate = new LoadHtml();
        loadHtmlImmediate.setFlagLoad(LoadHtml.LOAD_FOR_IMMEDIATE);
        loadHtmlImmediate.execute(url);
    }

    public void forDay(String stationFrom, String stationTo){
        if (listTrainForDay.isEmpty()) {
            String url = "https://t.rasp.yandex.ru/search/suburban/?toName=" + stationFrom + "&fromName=" + stationTo;
            loadHtmlDay = new LoadHtml();
            loadHtmlDay.setFlagLoad(LoadHtml.LOAD_FOR_DAY);
            loadHtmlDay.execute(url);
        }
        else adapter.swapData(listTrainForDay);
    }

    public void choiceDate(String stationFrom, String stationTo, String dayDeparture, String monthDeparture){
        int sizeListTrainForDate = listTrainForDate.size() - 1;
        if (!listTrainForDate.isEmpty()){
            if (listTrainForDate.get(sizeListTrainForDate).get(DAY_DEPARTURE).equals(dayDeparture)
                    && listTrainForDate.get(sizeListTrainForDate).get(MONTH_DEPARTURE).equals(monthDeparture)){
                adapter.swapData(listTrainForDate);
                return;
            }
        }
        String url = "https://t.rasp.yandex.ru/search/suburban/?fromName=" + stationFrom + "&toName="
                + stationTo + "&when=" + dayDeparture + "+" + monthDeparture;
        loadHtmlDay = new LoadHtml();
        loadHtmlDay.setFlagLoad(LoadHtml.LOAD_FOR_DATE);
        loadHtmlDay.execute(url);
    }

    /*@Override
    public void loadEnd(List<Map<String, String>> trainsList, List<Map<String, String>> trainsGoneList) {
        adapter.swapData(trainsList);
    }*/

    private class LoadHtml extends AsyncTask<String, Void, List<Map<String, String>>>{
        static final int LOAD_FOR_IMMEDIATE = 0;
        static final int LOAD_FOR_DAY = 1;
        static final int LOAD_FOR_DATE = 2;

        private int flagLoad;

        private String dayDeparture;
        private String monthDeparture;

        void setFlagLoad(int flagLoad){
            this.flagLoad = flagLoad;
        }

        void setDate(String dayDeparture, String monthDeparture){
            this.dayDeparture = dayDeparture;
            this.monthDeparture = monthDeparture;
        }

        @Override
        protected List<Map<String, String>> doInBackground(String... params) {
            String url;
            Document doc;
            List<Map<String, String>> trainsList = new ArrayList<>();
            if (params != null) {
                url = params[0];
            } else {
                return null;
            }
            try {
                doc = Jsoup.connect(url)
                        .userAgent("Mozilla/5.0 (Windows NT 6.2; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/32.0.1667.0 Safari/537.36")
                        .referrer("http://www.google.com")
                        .timeout(5000)
                        .get();

                HtmlHelper htmlHelper = new HtmlHelper();
                trainsList = htmlHelper.htmlParse(doc);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return trainsList;
        }

        @Override
        protected void onPostExecute(List<Map<String, String>> maps) {
            super.onPostExecute(maps);
            switch (flagLoad) {
                case LOAD_FOR_IMMEDIATE:
                    listTrainForDay.addAll(maps);
                    for (int i = 0; i < maps.size() - 1; i++) {
                        if (!maps.get(i).get("timeBeforeDeparture").equals("")) {
                            maps.subList(0, i).clear();
                            listTrainForImmediate.addAll(maps);
                            break;
                        }
                    }
                    break;
                case LOAD_FOR_DAY:
                    listTrainForDay.addAll(maps);
                    break;
                case LOAD_FOR_DATE:
                    listTrainForDate.addAll(maps);
                    Map<String, String> madDate = new HashMap<>();
                    madDate.put(DAY_DEPARTURE, dayDeparture);
                    madDate.put(MONTH_DEPARTURE, monthDeparture);
                    listTrainForDate.add(madDate);
                    break;
                default: return;
            }
            adapter.swapData(maps);
        }
    }

    /*@Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (getActivity() instanceof MainActivity){
            boolean fromIsClear = ((MainActivity)getActivity()).getCetFromIsClear();
            boolean toIsClear = ((MainActivity)getActivity()).getCetToIsClear();
            if (fromIsClear || toIsClear){
                if (fromIsClear){
                    tvInformFragment.setText(ConstProject.INF_ENTER_FROM);
                }
                else {
                    tvInformFragment.setText(ConstProject.INF_ENTER_TO);
                }
            } else {
                svTvInfo.setVisibility(View.GONE);
                svPbInfo.setVisibility(View.VISIBLE);
            }
        }
    }*/
}
