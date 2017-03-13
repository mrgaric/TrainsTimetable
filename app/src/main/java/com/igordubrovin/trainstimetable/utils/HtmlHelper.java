package com.igordubrovin.trainstimetable.utils;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Игорь on 10.03.2017.
 */

public class HtmlHelper {

    public static final int PARSE_FOR_DATE = 0;
    public static final int PARSE_FOR_DAY= 1;

    public List<Map<String, String>> htmlParse(Document doc){
        List<Map<String, String>> trainsList = new ArrayList<>();
        Map<String, String> itemTrain;
        boolean first = true;
        Elements trainsElements = doc.body().getElementsByTag("tr");
        for (Element train : trainsElements) {
            String timeBeforeDeparture;
            String timeDeparture;
            String timeArrival;
            String specialTrain;
            String station;
            String travelTime;
            String price;
            itemTrain = new HashMap<>();
            if (train.attr("class").equals("b-calendar__days-week"))
                break;
            if (first) {
                first = false;
                continue;
            }
            timeDeparture = train.getElementsByAttributeValue("class", "b-routers__time b-routers__time_with-icon_false b-routers__time_type_departure")
                    .text();
            timeArrival = train.getElementsByAttributeValue("class", "b-routers__time b-routers__time_with-icon_false b-routers__time_type_arrival")
                    .text();
            specialTrain = train.getElementsByAttributeValue("class", "b-routers__title-special")
                    .text();
            station = train.getElementsByTag("a").text();
            travelTime = train.getElementsByAttributeValue("class", "b-routers__time b-routers__time_type_in-path").text();
            price = train.getElementsByAttributeValue("class", "b-routers__item b-routers__item_type_last-column")
                    .text();
            itemTrain.put("timeDeparture", timeDeparture);
            itemTrain.put("timeArrival", timeArrival);
            itemTrain.put("specialTrain", specialTrain);
            itemTrain.put("station", station);
            itemTrain.put("travelTime", travelTime);
            itemTrain.put("price", price);
            if (price.equals("ушёл") || price.equals("")) {
                timeBeforeDeparture = "";
            } else {
                String s = train.attr("data-bem");
                String[] token = s.split("\"");
                timeBeforeDeparture = token[39] + " " + token[41];
            }
            itemTrain.put("timeBeforeDeparture", timeBeforeDeparture);
            trainsList.add(itemTrain);
        }
        return trainsList;
    }

    /*public static final int NUM_STATION_FROM = 0;
    public static final int NUM_STATION_TO = 1;
    public static final int NUM_STATION_CODE_FROM = 2;
    public static final int NUM_STATION_CODE_TO = 3;
    private LoadEndListener listener;

    public HtmlHelper(LoadEndListener l){
        listener = l;
    }

    public void startLoad(String stationFrom, String stationTo){
        TimetableLoad timetableLoad = new TimetableLoad();
        timetableLoad.execute(stationFrom, stationTo);
    }

    public interface LoadEndListener{
        void loadEnd(List<Map<String, String>> trainsList, List<Map<String, String>> trainsGoneList);
    }

    private class TimetableLoad extends AsyncTask<String, Void, Void>{
        List<Map<String, String>> trainsList = new ArrayList<>();
        List<Map<String, String>> trainsGoneList = new ArrayList<>();

        @Override
        protected Void doInBackground(String... params) {
            Elements trainsElements;
            Document doc = null;
            String url;
            boolean first = true;
            Map<String, String> itemTrain;

            //проверка заданных станций

            if (params != null)
                url = "https://t.rasp.yandex.ru/search/suburban/?toName=" + params[0] + "&fromName=" + params[1];
            else
                throw new IllegalArgumentException("Error URL:");

            //загрузка html

            try {
                doc = Jsoup.connect(url)
                        .userAgent("Mozilla/5.0 (Windows NT 6.2; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/32.0.1667.0 Safari/537.36")
                        .referrer("http://www.google.com")
                        .timeout(5000)
                        .get();
            } catch (IOException e) {
                e.printStackTrace();
            }

            //парсинг загруженной html

            if (doc != null) {
                trainsElements = doc.body().getElementsByTag("tr");

                for (Element train : trainsElements) {
                    String timeBeforeDeparture;
                    String timeDeparture;
                    String timeArrival;
                    String specialTrain;
                    String station;
                    String travelTime;
                    String price;
                    itemTrain = new HashMap<>();

                    if (train.attr("class").equals("b-calendar__days-week"))
                        break;

                    if (first) {
                        first = false;
                        continue;
                    }

                    timeDeparture = train.getElementsByAttributeValue("class", "b-routers__time b-routers__time_with-icon_false b-routers__time_type_departure")
                            .text();
                    timeArrival = train.getElementsByAttributeValue("class", "b-routers__time b-routers__time_with-icon_false b-routers__time_type_arrival")
                            .text();

                    specialTrain = train.getElementsByAttributeValue("class", "b-routers__title-special")
                            .text();

                    station = train.getElementsByTag("a").text();

                    travelTime = train.getElementsByAttributeValue("class", "b-routers__time b-routers__time_type_in-path").text();

                    price = train.getElementsByAttributeValue("class", "b-routers__item b-routers__item_type_last-column")
                            .text();

                    itemTrain.put("timeDeparture", timeDeparture);
                    itemTrain.put("timeArrival", timeArrival);
                    itemTrain.put("specialTrain", specialTrain);
                    itemTrain.put("station", station);
                    itemTrain.put("travelTime", travelTime);
                    itemTrain.put("price", price);

                    if (price.equals("ушёл")) {
                        trainsGoneList.add(itemTrain);
                    } else {
                        String s = train.attr("data-bem");
                        String[] token = s.split("\"");
                        timeBeforeDeparture = token[39] + " " + token[41];

                        itemTrain.put("timeBeforeDeparture", timeBeforeDeparture);

                        trainsList.add(itemTrain);
                    }
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void arg) {
            super.onPostExecute(arg);
            listener.loadEnd(trainsList, trainsGoneList);
        }
    }*/
}
