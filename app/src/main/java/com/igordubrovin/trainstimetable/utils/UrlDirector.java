package com.igordubrovin.trainstimetable.utils;

/**
 * Created by Игорь on 15.03.2017.
 */

public class UrlDirector {
        private UrlAddress urlAddress;

    public UrlDirector createUrlAddressOnlyStation(String stationFrom, String stationTo){
        UrlBuilderOnlyStation urlBuilder = new UrlBuilderOnlyStation();
        urlAddress = urlBuilder.setStationFrom(stationFrom)
                .setStationTo(stationTo)
                .buildUrl()
                .createNewUrlAddress();
        return this;
    }

    public UrlDirector createUrlAddressDateDeparture(String stationFrom, String stationTo, String day, String month){
        UrlBuilderDataDeparture urlBuilder = new UrlBuilderDataDeparture();
        urlAddress = urlBuilder
                .setDateDeparture(day, month)
                .setStationFrom(stationFrom)
                .setStationTo(stationTo)
                .buildUrl()
                .createNewUrlAddress();
        return this;
    }

    public UrlAddress getUrlAddress(){
        return urlAddress;
    }
}
