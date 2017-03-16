package com.igordubrovin.trainstimetable.fragments;

import android.os.AsyncTask;

import com.igordubrovin.trainstimetable.utils.HtmlHelper;
import com.igordubrovin.trainstimetable.utils.LoaderHtml;
import com.igordubrovin.trainstimetable.utils.Train;

import org.jsoup.nodes.Document;

import java.util.ArrayList;
import java.util.List;

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

    class LoaderTrains extends LoaderHtml {

        @Override
        protected void onPreExecute() {
            setDataDownloadStarted(true);
            setViewVisible();
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Document document) {
            super.onPostExecute(document);
            HtmlHelper htmlHelper = new HtmlHelper();
            List<Train> trainsList = new ArrayList<>();
            trainsList = htmlHelper.htmlParse(document);
            for (int i = 0; i < trainsList.size() - 1; i++) {
                if (!trainsList.get(i).getTimeBeforeDeparture().equals("")) {
                    trainsList.subList(0, i).clear();
                    break;
                }
            }
            updateAdapter(trainsList);
        }
    }
}
