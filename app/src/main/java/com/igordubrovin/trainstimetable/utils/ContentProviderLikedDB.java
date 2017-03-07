package com.igordubrovin.trainstimetable.utils;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.support.annotation.Nullable;

/**
 * Created by Игорь on 05.03.2017.
 */

public class ContentProviderLikedDB extends ContentProvider {

    //база данных Liked
    //бд
    private static final String LIKED_DB_NAME = "LikedDB";
    //таблица
    private static final String LIKED_DB_TABLE_NAME = "LikedRoutes";
    private static final int LIKED_DB_VERSION = 1;
    //поля
    private static final String LIKED_DB_COLUMN_NAME_ID = "_id";
    public static final String LIKED_DB_COLUMN_NAME_STATION_FROM = "StationFrom";
    public static final String LIKED_DB_COLUMN_NAME_STATION_TO = "StationTo";
    //создание таблицы
    private static final String LIKED_DB_CREATE_TABLE = "create table " + LIKED_DB_TABLE_NAME + " ("
            + LIKED_DB_COLUMN_NAME_ID + " integer primary key autoincrement,"
            + LIKED_DB_COLUMN_NAME_STATION_FROM + " text,"
            + LIKED_DB_COLUMN_NAME_STATION_TO + " text"
            + ");";

    //Uri
    //scheme
    static final String SCHEME = "content://";
    //authority
    static final String AUTHORITY_LIKED_DB = "com.igordubrovin.providers.LikedDB";
    //path
    static final String PATH_LIKED_ROUTES = "LikedRoutes";

    //uri
    public static final Uri URI_LIKED_ROUTES_CONTENT = Uri.parse(SCHEME + AUTHORITY_LIKED_DB + "/" + PATH_LIKED_ROUTES);

    //набор строк
    static final String LIKED_ROUTES_CONTENT_TYPE = "vnd.android.cursor.dir/vnd."
            + AUTHORITY_LIKED_DB + "."
            + PATH_LIKED_ROUTES;

    //одна строка
    static final String LIKED_ROUTES_CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd."
            + AUTHORITY_LIKED_DB + "."
            + PATH_LIKED_ROUTES;

    //uriMatcher
    //общий uri
    static final int LIKED_ROUTES_URI = 1;
    //uri с id
    static final int LIKED_ROUTES_ID_URI = 2;

    //создание uriMatcher
    private static final UriMatcher uriMatcher;
    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY_LIKED_DB, PATH_LIKED_ROUTES, LIKED_ROUTES_URI);
        uriMatcher.addURI(AUTHORITY_LIKED_DB, PATH_LIKED_ROUTES + "/#", LIKED_ROUTES_ID_URI);
    }

    private LikedDB mLikedDB;
    private SQLiteDatabase db;

    @Override
    public boolean onCreate() {
        mLikedDB = new LikedDB(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        switch (uriMatcher.match(uri)){
            case LIKED_ROUTES_URI:
                //проверка на сортировку
                if (sortOrder.isEmpty()){
                    sortOrder = LIKED_DB_COLUMN_NAME_STATION_FROM + " ASC";
                }
                break;
            case LIKED_ROUTES_ID_URI:
                //добавление id к условию выборки
                String id = uri.getLastPathSegment();
                if (selection.isEmpty()){
                    selection = LIKED_DB_COLUMN_NAME_ID + " = " + id;
                }
                else {
                    selection = selection + " AND " + LIKED_DB_COLUMN_NAME_ID + " = " + id;
                }
                break;
            default:
                throw new IllegalArgumentException("Error Uri:" + uri);
        }
        db = mLikedDB.getWritableDatabase();
        Cursor cursor = db.query(LIKED_DB_TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(), URI_LIKED_ROUTES_CONTENT);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)){
            case LIKED_ROUTES_URI:
                return LIKED_ROUTES_CONTENT_TYPE;
            case LIKED_ROUTES_ID_URI:
                return LIKED_ROUTES_CONTENT_ITEM_TYPE;
        }
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        db = mLikedDB.getWritableDatabase();
        long idRow;
        Uri resultUri;
        switch (uriMatcher.match(uri)){
            case LIKED_ROUTES_URI:
                idRow = db.insert(LIKED_DB_TABLE_NAME, null, values);
                resultUri = ContentUris.withAppendedId(URI_LIKED_ROUTES_CONTENT, idRow);
                break;
            default:
                throw new IllegalArgumentException("Error Uri:" + uri);
        }
        getContext().getContentResolver().notifyChange(resultUri, null);
        return resultUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        switch (uriMatcher.match(uri)){
            case LIKED_ROUTES_ID_URI:
                String id = uri.getLastPathSegment();
                if (selection.isEmpty()){
                    selection = LIKED_DB_COLUMN_NAME_ID + " = " + id;
                }else {
                    selection = selection + " AND " + LIKED_DB_COLUMN_NAME_ID + " = " + id;
                }
                break;
            default:
                throw new IllegalArgumentException("Error Uri:" + uri);
        }
        db = mLikedDB.getWritableDatabase();
        int cnt = db.delete(LIKED_DB_TABLE_NAME, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return cnt;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }

    private class LikedDB extends SQLiteOpenHelper {

        LikedDB(Context context) {
            super(context, LIKED_DB_NAME, null, LIKED_DB_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(LIKED_DB_CREATE_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }
}
