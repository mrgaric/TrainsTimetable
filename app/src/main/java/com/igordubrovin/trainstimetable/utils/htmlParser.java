package com.igordubrovin.trainstimetable.utils;

import android.os.AsyncTask;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Игорь on 10.03.2017.
 */

public class htmlParser {

    public static final int NUM_STATION_FROM = 0;
    public static final int NUM_STATION_TO = 1;
    public static final int NUM_STATION_CODE_FROM = 2;
    public static final int NUM_STATION_CODE_TO = 3;


    private class TimetableLoad extends AsyncTask<List<String>, Void, List<HashMap<String, String>>>{

        @Override
        protected List<HashMap<String, String>> doInBackground(List<String>... params) {

            Elements elem;
            Document doc;

            String url;

            if (params != null)
                url = "https://t.rasp.yandex.ru/search/suburban/?toName=" + params[0].get(NUM_STATION_FROM) + "&fromName=" + params[0].get(NUM_STATION_TO);
            else
                throw new IllegalArgumentException("Error URL:");
            try {
                doc = Jsoup.connect(url)
                        .userAgent("Mozilla/5.0 (Windows NT 6.2; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/32.0.1667.0 Safari/537.36")
                        .referrer("http://www.google.com")
                        .timeout(5000)
                        .get();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<HashMap<String, String>> hashMaps) {
            super.onPostExecute(hashMaps);
        }
    }
}
