package com.igordubrovin.trainstimetable.utils;

/**
 * Created by Игорь on 15.03.2017.
 */

public class UrlBuilderOnlyStation extends UrlAddress.UrlBuilder {
    @Override
    public UrlAddress createUrlAddress() {
        return new UrlAddress("https://t.rasp.yandex.ru/search/suburban/?toName=" + stationTo+ "&fromName=" + stationFrom);
    }
}
