package com.igordubrovin.trainstimetable.utils;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.content.CursorLoader;

/**
 * Created by Игорь on 05.03.2017.
 */

public class CLForLikedDB extends CursorLoader {

    public CLForLikedDB(Context context) {
        super(context);
    }

    @Override
    protected Cursor onLoadInBackground() {
        return super.onLoadInBackground();
    }
}
