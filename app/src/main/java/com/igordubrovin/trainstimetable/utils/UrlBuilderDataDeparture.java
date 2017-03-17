package com.igordubrovin.trainstimetable.utils;

/**
 * Created by Игорь on 15.03.2017.
 */

public class UrlBuilderDataDeparture extends UrlAddress.UrlBuilder {

    private String dayDeparture;
    private String monthDeparture;

    @Override
    UrlAddress.UrlBuilder buildUrl() {
        url = "https://t.rasp.yandex.ru/search/suburban/?toName=" + stationTo+ "&fromName=" + stationFrom
                + "&when=" + dayDeparture + "+" + monthDeparture;
        return this;
    }

    UrlAddress.UrlBuilder setDateDeparture(String day, String month){
        this.dayDeparture = day;
        this.monthDeparture = month;
        return this;
    }
}
