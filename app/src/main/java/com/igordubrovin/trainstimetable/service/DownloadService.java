/*
package com.igordubrovin.trainstimetable.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.igordubrovin.trainstimetable.utils.HtmlHelper;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.List;

*/
/**
 * Created by Игорь on 13.03.2017.
 *//*


public class DownloadService extends Service {

    public static final int LOAD_FOR_DATE = 0;
    public static final int LOAD_FOR_DAY = 1;
    public static final String FLAG_LOAD = "flag_load";
    public static final String STATION_FROM = "stationFrom";
    public static final String STATION_TO = "stationTo";
    public static final String DAY_DEPARTURE = "day";
    public static final String MONTH_DEPARTURE = "day";

    private List<Integer> idLoadStarted;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int flagLoad = intent.getIntExtra(FLAG_LOAD, 0);
        if (!idLoadStarted.contains(flagLoad)) {

            idLoadStarted.add(flagLoad);

            if (flagLoad == LOAD_FOR_DAY) {
                LoadThread loadThread = new LoadThread(intent.getStringExtra(STATION_FROM), intent.getStringExtra(STATION_TO));
                loadThread.run();
            }
            if (flagLoad == LOAD_FOR_DATE){
                LoadThread loadThread = new LoadThread(intent.getStringExtra(STATION_FROM), intent.getStringExtra(STATION_TO),
                        intent.getStringExtra(DAY_DEPARTURE), intent.getStringExtra(MONTH_DEPARTURE));
                loadThread.run();
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private class LoadThread extends Thread{

        private String url;
        private int flagParse;

        LoadThread(String stationFrom, String stationTo){
            url = "https://t.rasp.yandex.ru/search/suburban/?toName=" + stationFrom + "&fromName=" + stationTo;
            flagParse = HtmlHelper.PARSE_FOR_DAY;
        }

        LoadThread(String stationFrom, String stationTo, String day, String month){
            url = "https://t.rasp.yandex.ru/search/suburban/?fromName=" + stationFrom + "&toName=" + stationTo + "&when=" + day + "+" + month;
            flagParse = HtmlHelper.PARSE_FOR_DATE;
        }

        @Override
        public void run() {
            super.run();
            Document doc;
            List[] trainsListsArray;
            try {
                doc = Jsoup.connect(url)
                        .userAgent("Mozilla/5.0 (Windows NT 6.2; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/32.0.1667.0 Safari/537.36")
                        .referrer("http://www.google.com")
                        .timeout(5000)
                        .get();

                HtmlHelper htmlHelper = new HtmlHelper();
                trainsListsArray = htmlHelper.htmlParse(doc, flagParse);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
*/
