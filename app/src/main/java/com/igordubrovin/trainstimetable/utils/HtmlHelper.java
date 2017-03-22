package com.igordubrovin.trainstimetable.utils;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Игорь on 10.03.2017.
 */

public class HtmlHelper {

    public List<Train> htmlParse(Document doc){
        Train train;
        String timeBeforeDeparture;
        String timeDeparture;
        String timeArrival;
        String specialTrain;
        String station;
        String travelTime;
        String price;

        List<Train> trainsList = new ArrayList<>();
        boolean first = true;
        Elements trainsElements = doc.body().getElementsByTag("tr");

        for (Element trainElement : trainsElements) {
            if (trainElement.attr("class").equals("b-calendar__days-week"))
                break;
            if (first) {
                first = false;
                continue;
            }
            if (trainElement.getElementsByAttributeValue("class", "b-routers__ad").text().equals("реклама")){
                continue;
            }
            timeDeparture = trainElement.getElementsByAttributeValue("class", "b-routers__time b-routers__time_with-icon_false b-routers__time_type_departure")
                    .text();
            timeArrival = trainElement.getElementsByAttributeValue("class", "b-routers__time b-routers__time_with-icon_false b-routers__time_type_arrival")
                    .text();
            specialTrain = trainElement.getElementsByAttributeValue("class", "b-routers__title-special")
                    .text();
            station = trainElement.getElementsByTag("a").text();
            travelTime = trainElement.getElementsByAttributeValue("class", "b-routers__time b-routers__time_type_in-path").text();
            price = trainElement.getElementsByAttributeValue("class", "b-routers__item b-routers__item_type_last-column")
                    .text();
            if (price.equals("ушёл") || price.equals("")) {
                timeBeforeDeparture = "";
            } else {
                String s = trainElement.attr("data-bem");
                String[] token = s.split("\"");
                timeBeforeDeparture = token[39] + " " + token[41];
            }

            train = Train.getTrainBuilder()
                    .setTimeDeparture(timeDeparture)
                    .setTimeArrival(timeArrival)
                    .setSpecialTrain(specialTrain)
                    .setStations(station)
                    .setTravelTime(travelTime)
                    .setPrice(price)
                    .setTimeBeforeDeparture(timeBeforeDeparture)
                    .createTrain();

            trainsList.add(train);
        }
        return trainsList;
    }
}
