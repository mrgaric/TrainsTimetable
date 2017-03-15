package com.igordubrovin.trainstimetable.utils;

/**
 * Created by Игорь on 15.03.2017.
 */

public class UrlDirector {
    protected UrlAddress.UrlBuilder urlBuilder;
    protected UrlAddress urlAddress;

    public UrlDirector setUrlBuilder(UrlAddress.UrlBuilder urlBuilder){
        this.urlBuilder = urlBuilder;
        return this;
    }

    public UrlDirector createUrlAddress(String stationFrom, String stationTo){
        if (urlBuilder != null) {
            if (urlBuilder instanceof UrlBuilderOnlyStation) {
                urlAddress = urlBuilder.setStationFrom(stationFrom)
                        .setStationTo(stationTo)
                        .createUrlAddress();
                return this;
            }
            else throw new ClassCastException("UrlBuilder");
        }
        else throw new NullPointerException("UrlBuilder");
    }

    public UrlDirector createUrlAddress(String stationFrom, String stationTo, String day, String month){
        if (urlBuilder != null) {
            if (urlBuilder instanceof UrlBuilderDataDeparture) {
                urlAddress = ((UrlBuilderDataDeparture) urlBuilder)
                        .setDateDeparture(day, month)
                        .setStationFrom(stationFrom)
                        .setStationTo(stationTo)
                        .createUrlAddress();
                return this;
            }
            else throw new ClassCastException("UrlBuilder");
        }
        else throw new NullPointerException("UrlBuilder");
    }

    public UrlAddress getUrlAddress(){
        return urlAddress;
    }
}
