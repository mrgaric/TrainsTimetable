package com.igordubrovin.trainstimetable.fragments;

import android.os.AsyncTask;

import com.igordubrovin.trainstimetable.utils.LoaderHtml;

import org.jsoup.nodes.Document;

/**
 * Created by Игорь on 16.03.2017.
 */

public class FragmentTrainsImmediate extends FragmentTrains<FragmentTrainsImmediate.LoaderTrains> {

    @Override
    public void startLoaderTrains() {
        if (loaderTrains != null) {
            if (loaderTrains.getStatus() == AsyncTask.Status.RUNNING)
                return;
        }
        String url = urlDirector
                .createUrlAddressOnlyStation(stationFrom, stationTo)
                .getUrlAddress()
                .getUrl();
        loaderTrains = new LoaderTrains();
        loaderTrains.execute(url);
    }

    class LoaderTrains extends LoaderHtml{

        @Override
        protected void onPreExecute() {
            setInfoFlag(true);
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Document document) {

            super.onPostExecute(document);
        }
    }
}
