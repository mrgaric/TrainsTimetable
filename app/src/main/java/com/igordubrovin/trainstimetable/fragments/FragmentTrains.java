package com.igordubrovin.trainstimetable.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.igordubrovin.trainstimetable.R;
import com.igordubrovin.trainstimetable.adapters.AdapterTrainsRecycler;
import com.igordubrovin.trainstimetable.utils.HtmlHelper;
import com.igordubrovin.trainstimetable.utils.Train;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.List;

import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter;
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;

/**
 * Created by Игорь on 16.03.2017.
 */

public abstract class FragmentTrains <T extends FragmentTrains.LoaderHtml> extends Fragment implements SwipeRefreshLayout.OnRefreshListener{

    private SwipeRefreshLayout refreshTrains;
    private RecyclerView rvSelection;
    private AdapterTrainsRecycler adapterRvSelection;
    private AlphaInAnimationAdapter alphaInAnimationAdapter;
    private ScaleInAnimationAdapter scaleInAnimationAdapter;
    private View view;

    protected T loaderTrains;
    protected String newStationFrom = "";
    protected String newStationTo = "";

    protected List<Train> trainList;

    // переопределенные методы фрагмента

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapterRvSelection = new AdapterTrainsRecycler();
        alphaInAnimationAdapter = new AlphaInAnimationAdapter(adapterRvSelection);
        alphaInAnimationAdapter.setDuration(200);
        alphaInAnimationAdapter.setFirstOnly(false);
        scaleInAnimationAdapter = new ScaleInAnimationAdapter(alphaInAnimationAdapter);
        scaleInAnimationAdapter.setDuration(200);
        scaleInAnimationAdapter.setFirstOnly(false);
        this.setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_trains, container, false);
        initRefresh();
        initRecyclerSelection();
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (loaderTrains != null)
            loaderTrains.cancel(false);
        stopRefresh();
    }

    // init

    private void initRefresh(){
        refreshTrains = (SwipeRefreshLayout) view.findViewById(R.id.refreshTrains);
        refreshTrains.setOnRefreshListener(this);
        refreshTrains.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimary);
        TypedValue tv = new TypedValue();
        int actionBarSize = 0;
        if (getActivity().getApplicationContext().getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)){
            actionBarSize = TypedValue.complexToDimensionPixelSize(tv.data, getResources().getDisplayMetrics());
        }
        refreshTrains.setProgressViewOffset(true, -100, actionBarSize);
        if (loaderTrains != null)
            if (loaderTrains.getStatus() == AsyncTask.Status.RUNNING)
                refreshTrains.setRefreshing(true);
    }

    private void initRecyclerSelection(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rvSelection = (RecyclerView) view.findViewById(R.id.rvSelection);
        rvSelection.setLayoutManager(linearLayoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(), linearLayoutManager.getOrientation());
        rvSelection.addItemDecoration(dividerItemDecoration);
        rvSelection.setAdapter(scaleInAnimationAdapter);
    }

    // обновление

    @Override
    public void onRefresh() {
        refreshTrains.setRefreshing(true);
        startLoaderTrains();
    }

    public void stopRefresh(){
        refreshTrains.setRefreshing(false);
    }

    // Вспомогательные методы

    public void setStationFrom(String station) {
        newStationFrom = station;
    }

    public void setStationTo(String station) {
        newStationTo = station;
    }

    public void startLoaderTrains(){
        String url = getUrl();
        loaderTrains = createLoader();
        loaderTrains.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, url);
    }

    protected void updateDataAdapter(List<Train> trains){
        stopRefresh();
        adapterRvSelection.swapData(trains);
        alphaInAnimationAdapter.notifyDataSetChanged();
    }

    protected void makeSnackbar(){
        Snackbar.make(getActivity().getWindow().getDecorView().getRootView(), "Ошибка загрузки", BaseTransientBottomBar.LENGTH_SHORT).show();
        stopRefresh();
    }

    //абстрактные методы

    protected abstract String getUrl();
    protected abstract T createLoader();

    // класс Лоадер

    static abstract class LoaderHtml extends AsyncTask<String, Void, Document>{

        @Override
        protected Document doInBackground(String... params) {
            String url;
            Document doc = null;
            if (params != null) {
                url = params[0];
            } else {
                return null;
            }
            try {
                doc = Jsoup.connect(url)
                        .userAgent("Mozilla/5.0 (Windows NT 6.2; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/32.0.1667.0 Safari/537.36")
                        .referrer("http://www.google.com")
                        .timeout(3000)
                        .get();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
            return doc;
        }

        @Override
        protected void onPostExecute(Document document) {
            super.onPostExecute(document);
            if (document != null)
                onPostParse(new HtmlHelper().htmlParse(document));
            else onPostError();
        }

        protected abstract void onPostParse(List<Train> trains);
        protected abstract void onPostError();
    }

}
