package com.igordubrovin.trainstimetable.utils;

import android.content.Context;
import android.content.CursorLoader;
import android.database.Cursor;
import android.os.Bundle;

/**
 * Created by Игорь on 21.02.2017.
 */

public class CursorLoaderForDB extends CursorLoader{

    private Bundle bundle;

    public CursorLoaderForDB(Context context, Bundle bundle) {
        super(context);
        this.bundle = bundle;
    }

    @Override
    protected Cursor onLoadInBackground()
    {
        SearchStationDB db = new SearchStationDB(getContext());
        Cursor cursor;
        cursor = db.query(bundle.getString("upd"));
        return cursor;
    }
}
