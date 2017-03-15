package com.igordubrovin.trainstimetable.utils;

/**
 * Created by Игорь on 15.03.2017.
 */

public class UrlBuilderDataDeparture extends UrlAddress.UrlBuilder {
    private String dayDeparture;
    private String monthDeparture;

    @Override
    public UrlAddress createUrlAddress() {
        return new UrlAddress("https://t.rasp.yandex.ru/search/suburban/?toName=" + stationTo+ "&fromName=" + stationFrom
                + "&when=" + dayDeparture + "+" + monthDeparture);
    }

    UrlAddress.UrlBuilder setDateDeparture(String day, String month){
        this.dayDeparture = day;
        this.monthDeparture = month;
        return this;
    }
}
