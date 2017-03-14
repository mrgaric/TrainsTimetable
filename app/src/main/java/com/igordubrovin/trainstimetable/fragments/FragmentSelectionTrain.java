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
import com.igordubrovin.trainstimetable.utils.ConstProject;
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

    boolean nothing;
    boolean searching;
    boolean shows;

    private int choiceTimetable;

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
        nothing = true;
        shows = false;
        searching = false;
        /*listTrainForImmediate = new ArrayList<>();
        listTrainForDay = new ArrayList<>();
        listTrainForDate = new ArrayList<>();*/
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

        if (nothing)
            updViewVisible(tvInformFragment, pbLoad, rvSelection);
        else if (searching)
            updViewVisible(pbLoad, tvInformFragment, rvSelection);
        else if (shows)
            updViewVisible(rvSelection, pbLoad, tvInformFragment);

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

    public void loadTrainsImmediate(String stationFrom, String stationTo){
        updViewVisible(pbLoad, tvInformFragment, rvSelection);
        String url = "https://t.rasp.yandex.ru/search/suburban/?toName=" + stationTo + "&fromName=" + stationFrom;
        loadHtmlImmediate = new LoadHtml();
        loadHtmlImmediate.setFlagLoad(LoadHtml.LOAD_FOR_IMMEDIATE);
        loadHtmlImmediate.execute(url);
    }

    public void loadTrainsForDay(String stationFrom, String stationTo){
      //  if (listTrainForDay.isEmpty()) {
        if (listTrainForDay == null) {
            if (loadHtmlImmediate != null && loadHtmlImmediate.getStatus() != AsyncTask.Status.RUNNING){
                updViewVisible(pbLoad, tvInformFragment, rvSelection);
                String url = "https://t.rasp.yandex.ru/search/suburban/?toName=" + stationTo + "&fromName=" + stationFrom;
                loadHtmlDay = new LoadHtml();
                loadHtmlDay.setFlagLoad(LoadHtml.LOAD_FOR_DAY);
                loadHtmlDay.execute(url);
            }
        }
        else {
            updViewVisible(rvSelection, pbLoad, tvInformFragment);
            adapter.swapData(listTrainForDay);
        }
    }

    public void loadTrainsChoiceDate(String stationFrom, String stationTo, String dayDeparture, String monthDeparture){
        int sizeListTrainForDate = listTrainForDate.size() - 1;
    //    if (!listTrainForDate.isEmpty()){
        if (listTrainForDate != null){
            if (listTrainForDate.get(sizeListTrainForDate).get(DAY_DEPARTURE).equals(dayDeparture)
                    && listTrainForDate.get(sizeListTrainForDate).get(MONTH_DEPARTURE).equals(monthDeparture)){
                updViewVisible(rvSelection, pbLoad, tvInformFragment);
                adapter.swapData(listTrainForDate);
                return;
            }
        }
        updViewVisible(pbLoad, tvInformFragment, rvSelection);
        String url = "https://t.rasp.yandex.ru/search/suburban/?toName=" + stationTo+ "&fromName=" +
                stationFrom + "&when=" + dayDeparture + "+" + monthDeparture;
        loadHtmlDate = new LoadHtml();
        loadHtmlDate.setDate(dayDeparture, monthDeparture);
        loadHtmlDate.setFlagLoad(LoadHtml.LOAD_FOR_DATE);
        loadHtmlDate.execute(url);
    }

    private void updViewVisible(View viewVisible, View viewGone1, View viewGone2){
        viewVisible.setVisibility(View.VISIBLE);
        viewGone1.setVisibility(View.GONE);
        viewGone2.setVisibility(View.GONE);
    }

    /*public void setChoiceImmediate(){
        choiceImmediate = true;
        choiceDay = false;
        choiceDate = false;
    }

    public void setChoiceDay(){
        choiceDay = true;
        choiceImmediate = false;
        choiceDate = false;
    }

    public void setChoiceDate(){
        choiceDate = true;
        choiceDay = false;
        choiceImmediate = false;
    }*/

    public void setChoiceTimetable(int choiceTimetable){
        this.choiceTimetable = choiceTimetable;
    }

    public void clearDataCache(){
        /*listTrainForDate = new ArrayList<>();
        listTrainForDay = new ArrayList<>();
        listTrainForImmediate = new ArrayList<>();*/
        listTrainForDate = null;
        listTrainForDay = null;
        listTrainForImmediate = null;
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
        protected void onPreExecute() {
            super.onPreExecute();
            searching = true;
            nothing = false;
            shows = false;
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
                    if (choiceTimetable == ConstProject.CHOICE_FOR_IMMEDIATE) {
                        //listTrainForDay.addAll(maps);
                        listTrainForDay = new ArrayList<>(maps);
                        for (int i = 0; i < maps.size() - 1; i++) {
                            if (!maps.get(i).get("timeBeforeDeparture").equals("")) {
                                maps.subList(0, i).clear();
                                //listTrainForImmediate.addAll(maps);
                                listTrainForImmediate = new ArrayList<>(maps);
                                break;
                            }
                        }
                        updViewVisible(rvSelection, pbLoad, tvInformFragment);
                        adapter.swapData(listTrainForImmediate);
                    }
                    break;
                case LOAD_FOR_DAY:
                    //listTrainForDay.addAll(listTrainForImmediate);
                    listTrainForDay = new ArrayList<>(maps);
                    if (choiceTimetable == ConstProject.CHOICE_FOR_DAY) {
                        updViewVisible(rvSelection, pbLoad, tvInformFragment);
                        adapter.swapData(maps);
                    }
                    break;
                case LOAD_FOR_DATE:
                    //listTrainForDate.addAll(maps);
                    listTrainForDate = new ArrayList<>(maps);
                    Map<String, String> madDate = new HashMap<>();
                    madDate.put(DAY_DEPARTURE, dayDeparture);
                    madDate.put(MONTH_DEPARTURE, monthDeparture);
                    listTrainForDate.add(madDate);
                    if (choiceTimetable == ConstProject.CHOICE_FOR_DATE) {
                        updViewVisible(rvSelection, pbLoad, tvInformFragment);
                        adapter.swapData(maps);
                    }
                    break;
            }
            shows = true;
            nothing = false;
            searching = false;
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
