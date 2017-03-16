package com.igordubrovin.trainstimetable.utils;

/**
 * Created by Игорь on 14.03.2017.
 */

public class UrlAddress {
    private final String url;

    private UrlAddress(String url){
        this.url = url;
    }

    public String getUrl(){
        return url;
    }

    static abstract class UrlBuilder{

        protected String stationTo;
        protected String stationFrom;
        protected String url;

        UrlBuilder setStationFrom(String stationFrom){
            this.stationFrom = stationFrom;
            return this;
        }

        UrlBuilder setStationTo(String stationTo){
            this.stationTo = stationTo;
            return this;
        }

        UrlAddress createNewUrlAddress(){
            return new UrlAddress(url);
        }

        abstract UrlBuilder buildUrl();
    }
}
