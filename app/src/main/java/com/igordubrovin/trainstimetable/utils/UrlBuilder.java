package com.igordubrovin.trainstimetable.utils;

/**
 * Created by Игорь on 14.03.2017.
 */

public class UrlBuilder {
    private String stationTo;
    private String stationFrom;
    private String dayDeparture;
    private String monthDeparture;

    public UrlBuilder setStationFrom(String stationFrom){
        this.stationFrom = stationFrom;
        return this;
    }

    public UrlBuilder setStationTo(String stationTo){
        this.stationTo = stationTo;
        return this;
    }

    public UrlBuilder setDateDeparture(String day, String month){
        this.dayDeparture = day;
        this.monthDeparture = month;
        return this;
    }

    public String createUrl(){
        String url = "https://t.rasp.yandex.ru/search/suburban/?toName=" + stationTo+ "&fromName=" + stationFrom;
        if (dayDeparture != null && monthDeparture != null){
            url = url + "&when=" + dayDeparture + "+" + monthDeparture;
        }
        return url;
    }

}
