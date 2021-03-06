package com.igordubrovin.trainstimetable.utils;

/**
 * Created by Игорь on 21.02.2017.
 */

public class ConstProject {
    public static final String DELETE = "Удалить";
    //активности

    //фрагменты
    public static final String INF_ENTER_FROM = "Введите станцию отправления";
    public static final String INF_ENTER_TO = "ведите станцию назначения";
    public static final String ERROR_CONNECTION_INTERNET = "Ошибка подключения. Проверьте соединение с интернетом";
    public static final String FRAGMENT_SEARCH_STATION = "fragmentSearchStation";
    public static final String FRAGMENT_ROUTES = "fragmentRoutes";
    public static final String FRAGMENT_SELECTION_TRAIN = "fragmentSelectionTrain";
    public static final String FRAGMENT_LIKED_ROUTE = "fragmentLikedRoute";
    public static final String STATION_FROM = "stationFrom";
    public static final String STATION_TO = "stationTo";


    //база данных
    public static final String DATABASE_NAME = "DbTest.db";
    public static final int DATABASE_VERSION = 1;
    public static final String DB_TABLE_NAME = "Test";
    public static final String COLUMN_NAME_STATION = "Name";
    public static final String COLUMN_CODE_STATION = "Code";
    public static final String SELECTION_DB = COLUMN_NAME_STATION + " LIKE ";
    public static final String PART_STATION_NAME = "partStationName";


    public static final int CHOICE_FOR_IMMEDIATE = 0;
    public static final int CHOICE_FOR_DAY = 1;
    public static final int CHOICE_FOR_DATE = 2;

}
