package com.igordubrovin.trainstimetable.utils;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

/**
 * Created by Игорь on 21.02.2017.
 */

public class SearchStationDB extends SQLiteAssetHelper {

    public SearchStationDB(Context context) {
        super(context, ConstProject.DATABASE_NAME, null, ConstProject.DATABASE_VERSION);
    }

    public Cursor query(String partStationName){
        SQLiteDatabase db = getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        String selectionCondition;
        Cursor cursor;

        selectionCondition = createSelectionCondition(partStationName);

        qb.setTables(ConstProject.DB_TABLE_NAME);

        cursor = qb.query(db, null, selectionCondition, null,
                null, null, null);

        return cursor;
    }

    private String createSelectionCondition(String selectionArg){
        return ConstProject.SELECTION_DB + "'" + selectionArg + "%'";
    }
}
