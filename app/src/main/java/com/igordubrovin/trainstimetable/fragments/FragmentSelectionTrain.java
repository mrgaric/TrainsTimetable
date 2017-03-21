package com.igordubrovin.trainstimetable.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.igordubrovin.trainstimetable.Interface.OnChangeTabListener;
import com.igordubrovin.trainstimetable.R;
import com.igordubrovin.trainstimetable.adapters.ViewPagerAdapter;

public class FragmentSelectionTrain extends Fragment {

    private OnChangeTabListener tabChangeListener;

    private ViewPagerAdapter adapter;
    private ViewPager vpContainer;
    View view;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        adapter = new ViewPagerAdapter(getChildFragmentManager());
        adapter.addFragment(new FragmentTrainsImmediate(), "1");
        adapter.addFragment(new FragmentTrainsDay(), "2");
        adapter.addFragment(new FragmentTrainsDate(), "3");
        this.setRetainInstance(true);
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null)
            view = inflater.inflate(R.layout.fragment_selection_train, container, false);
        vpContainer = (ViewPager) view.findViewById(R.id.vpContainer);
        if (vpContainer.getAdapter() == null)
            vpContainer.setAdapter(adapter);
        vpContainer.setOffscreenPageLimit(3);
        vpContainer.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (tabChangeListener != null)
                    tabChangeListener.onChangeTab(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        return view;
    }

    public static void setStations(String stationFrom, String stationTo){
        FragmentTrains.setStationFrom(stationFrom);
        FragmentTrains.setStationTo(stationTo);
    }

    public static void setDateDeparture(String day, String month){
        FragmentTrainsDate.setNewDayDeparture(day);
        FragmentTrainsDate.setNewMonthDeparture(month);
    }

    public ViewPager getVpContainer() {
        return vpContainer;
    }

    public void setOnChangeTebListener(OnChangeTabListener l){
        tabChangeListener = l;
    };
}


/*



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
import com.igordubrovin.trainstimetable.utils.Train;
import com.igordubrovin.trainstimetable.utils.UrlDirector;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

*/
/**
 * Created by Игорь on 21.02.2017.
 *//*


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
    LoadHtml loadHtmlImmediate;
    LoadHtml loadHtmlDay;
    LoadHtml loadHtmlDate;
    List<Map<String, String>> listTrainForImmediate;
    List<Map<String, String>> listTrainForDay;
    List<Map<String, String>> listTrainForDate;
    UrlDirector urlDirector = new UrlDirector();
    private int choiceTimetable;

    //LoadHtmlNew loadHtmlNew;
    //Button btn;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new AdapterSelectionTrain();
        nothing = true;
        shows = false;
        searching = false;
        */
/*listTrainForImmediate = new ArrayList<>();
        listTrainForDay = new ArrayList<>();
        listTrainForDate = new ArrayList<>();*//*

        this.setRetainInstance(true);


        */
/*loadHtmlImmediate = new LoadHtml(LoadHtml.LOAD_FOR_IMMEDIATE);
        loadHtmlImmediate.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "s");
        loadHtmlNew = new LoadHtmlNew();
        loadHtmlNew.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "a");*//*

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_selection_train, container, false);

        tvInformFragment = (TextView)view.findViewById(R.id.tvInformFragment);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rvSelection = (RecyclerView) view.findViewById(R.id.rvSelection);
        rvSelection.setLayoutManager(linearLayoutManager);
        rvSelection.setAdapter(adapter);

        */
/*if (nothing)
            updViewVisible(tvInformFragment, pbLoad, rvSelection);
        else if (searching)
            updViewVisible(pbLoad, tvInformFragment, rvSelection);
        else if (shows)
            updViewVisible(rvSelection, pbLoad, tvInformFragment);*//*


        */
/*btn = (Button) view.findViewById(R.id.btn1);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadHtmlImmediate = new LoadHtml(LoadHtml.LOAD_FOR_IMMEDIATE);
                loadHtmlImmediate.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "s");
                loadHtmlNew = new LoadHtmlNew();
                loadHtmlNew.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "a");
            }
        });*//*


        return view;
    }

    @Override
    public void onDestroy() {
        stopThreads();
        super.onDestroy();
    }

    public void loadTrainsImmediate(String stationFrom, String stationTo){
        //updViewVisible(pbLoad, tvInformFragment, rvSelection);
        if (loadHtmlImmediate != null) {
            if (loadHtmlImmediate.getStatus() == AsyncTask.Status.RUNNING)
                return;
        }
        String url = urlDirector
                .createUrlAddressOnlyStation(stationFrom, stationTo)
                .getUrlAddress()
                .getUrl();
        loadHtmlImmediate = new LoadHtml(LoadHtml.LOAD_FOR_IMMEDIATE);
        loadHtmlImmediate.execute(url);
    }

    public void loadTrainsForDay(String stationFrom, String stationTo){
        if (loadHtmlDay != null) {
            if (loadHtmlDay.getStatus() == AsyncTask.Status.RUNNING)
                return;
        }
        if (listTrainForDay == null) {
            if (loadHtmlImmediate != null){
                if (loadHtmlImmediate.getStatus() == AsyncTask.Status.RUNNING)
                    return;
            }
            String url = urlDirector
                    .createUrlAddressOnlyStation(stationFrom, stationTo)
                    .getUrlAddress()
                    .getUrl();
           // updViewVisible(pbLoad, tvInformFragment, rvSelection);
            loadHtmlDay = new LoadHtml(LoadHtml.LOAD_FOR_DAY);
            loadHtmlDay.execute(url);
        }
        else {
           // updViewVisible(rvSelection, pbLoad, tvInformFragment);
         //   adapter.swapData(listTrainForDay);
        }
    }

    public void loadTrainsChoiceDate(String stationFrom, String stationTo, String dayDeparture, String monthDeparture){
        if (listTrainForDate != null){
            int sizeListTrainForDate = listTrainForDate.size() - 1;
            if (listTrainForDate.get(sizeListTrainForDate).get(DAY_DEPARTURE).equals(dayDeparture)
                    && listTrainForDate.get(sizeListTrainForDate).get(MONTH_DEPARTURE).equals(monthDeparture)){
                //updViewVisible(rvSelection, pbLoad, tvInformFragment);
            //    adapter.swapData(listTrainForDate);
                return;
            }
        }
        String url = urlDirector
                .createUrlAddressDateDeparture(stationFrom, stationTo, dayDeparture, monthDeparture)
                .getUrlAddress()
                .getUrl();
      //  updViewVisible(pbLoad, tvInformFragment, rvSelection);
        loadHtmlDate = new LoadHtml(LoadHtml.LOAD_FOR_DATE, dayDeparture, monthDeparture);
        loadHtmlDate.execute(url);
    }

    private void stopThreads(){
        if (loadHtmlImmediate != null)
            loadHtmlImmediate.cancel(false);
        if (loadHtmlDate != null)
            loadHtmlDate.cancel(false);
        if (loadHtmlDay != null)
            loadHtmlDay.cancel(false);
    }

    */
/*private void updViewVisible(View viewVisible, View viewGone1, View viewGone2){
        viewVisible.setVisibility(View.VISIBLE);
        viewGone1.setVisibility(View.GONE);
        viewGone2.setVisibility(View.GONE);
    }*//*


    public void setChoiceTimetable(int choiceTimetable){
        this.choiceTimetable = choiceTimetable;
    }

    public void clearDataCache(){
        listTrainForDate = null;
        listTrainForDay = null;
        listTrainForImmediate = null;
        stopThreads();
    }

     private class LoadHtml extends AsyncTask<String, Void, List<Train>>{
        static final int LOAD_FOR_IMMEDIATE = 0;
        static final int LOAD_FOR_DAY = 1;
        static final int LOAD_FOR_DATE = 2;

        private int flagLoad;

        private String dayDeparture;
        private String monthDeparture;

        LoadHtml(int flagLoad){
            this.flagLoad = flagLoad;
        }

        LoadHtml(int flagLoad, String dayDeparture, String monthDeparture){
            this.flagLoad = flagLoad;
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
        protected List<Train> doInBackground(String... params) {
            String url;
            Document doc;
            List<Train> trainsList = new ArrayList<>();
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
        protected void onPostExecute(List<Train> trains) {
            */
/*super.onPostExecute(maps);
            switch (flagLoad) {
                case LOAD_FOR_IMMEDIATE:
                    if (choiceTimetable == ConstProject.CHOICE_FOR_IMMEDIATE) {
                        listTrainForDay = new ArrayList<>(maps);
                        for (int i = 0; i < maps.size() - 1; i++) {
                            if (!maps.get(i).get("timeBeforeDeparture").equals("")) {
                                maps.subList(0, i).clear();
                                listTrainForImmediate = new ArrayList<>(maps);
                                break;
                            }
                        }
                        updViewVisible(rvSelection, pbLoad, tvInformFragment);
                        adapter.swapData(listTrainForImmediate);
                        break;
                    }
                case LOAD_FOR_DAY:
                    listTrainForDay = new ArrayList<>(maps);
                    if (choiceTimetable == ConstProject.CHOICE_FOR_DAY) {
                        updViewVisible(rvSelection, pbLoad, tvInformFragment);
                        adapter.swapData(maps);
                    }
                    break;
                case LOAD_FOR_DATE:
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
            searching = false;*//*

        }
    }

    */
/*private class LoadHtml extends AsyncTask<String, Void, Void>{
        static final int LOAD_FOR_IMMEDIATE = 0;
        static final int LOAD_FOR_DAY = 1;
        static final int LOAD_FOR_DATE = 2;

        LoadHtml(int flagLoad){

        }

        LoadHtml(int flagLoad, String dayDeparture, String monthDeparture){

        }

        @Override
        protected Void doInBackground(String... params) {
            Log.d("myLog", params[0]);
                Train train = Train.getTrainBuilder()
                        .setPrice(params[0])
                        .createTrain();
            return null;
        }
    }

    private class LoadHtmlNew extends AsyncTask<String, Void, Void>{
        LoadHtmlNew(){}

        @Override
        protected Void doInBackground(String... params) {
                Log.d("myLog", params[0]);
                Train train = Train.getTrainBuilder()
                        .setPrice(params[0])
                        .createTrain();
            return null;
        }
    }*//*


}
*/
