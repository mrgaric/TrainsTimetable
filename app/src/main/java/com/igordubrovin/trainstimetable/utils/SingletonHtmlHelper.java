package com.igordubrovin.trainstimetable.utils;

/**
 * Created by Игорь on 11.03.2017.
 */

public class SingletonHtmlHelper {

    private static SingletonHtmlHelper instance;
    private static HtmlHelper.LoadEndListener listener;

    private SingletonHtmlHelper(){

    }

    public static synchronized SingletonHtmlHelper getInstance(HtmlHelper.LoadEndListener l){
        if (instance == null){
            instance = new SingletonHtmlHelper();
        }
        listener = l;
        return instance;
    }

    public void startLoad(String stationFrom, String stationTo){
        HtmlHelper htmlHelper = new HtmlHelper(listener);
        htmlHelper.startLoad(stationFrom, stationTo);
    }

}
