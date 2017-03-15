package com.igordubrovin.trainstimetable.utils;

/**
 * Created by Игорь on 14.03.2017.
 */

public class UrlAddress {
    private final String url;

    UrlAddress(String url){
        this.url = url;
    }

    public String getUrl(){
        return url;
    }

    static abstract class UrlBuilder{

        protected String stationTo;
        protected String stationFrom;

        public UrlBuilder setStationFrom(String stationFrom){
            this.stationFrom = stationFrom;
            return this;
        }

        public UrlBuilder setStationTo(String stationTo){
            this.stationTo = stationTo;
            return this;
        }

        public abstract UrlAddress createUrlAddress();
    }

}
