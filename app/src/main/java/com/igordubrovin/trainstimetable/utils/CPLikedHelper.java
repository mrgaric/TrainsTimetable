package com.igordubrovin.trainstimetable.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;

/**
 * Created by Игорь on 10.03.2017.
 */

public class CPLikedHelper implements LoaderManager.LoaderCallbacks<Cursor> {

    public static final int CHECK_LIKED = 0;
    public static final int LOAD_LIKED = 1;

    private LoadListener listener;

    private Context context;
    private Uri uri;

    public CPLikedHelper(Context context){
        this.context = context;
    }

    public CPLikedHelper setUri(Uri uri){
        this.uri = uri;
        return this;
    }

    public void startLoadItemDB(LoaderManager mLoaderManager, int id, Bundle args){
        mLoaderManager.restartLoader(id, args, this);
    }

    public Uri insertItemDB(ContentValues cv){
        return context.getContentResolver().insert(uri, cv);
    }

    public int deleteItemDB(String selection, String[] selectionArgs){
        return context.getContentResolver().delete(uri, selection, selectionArgs);
    }

    public void setLoadListener(LoadListener l){
        listener = l;
    }

    public interface LoadListener{
        void loadEnd(Loader<Cursor> loader, Cursor cursor);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String selection = null;
        String[] selectionArgs = null;
        if (args != null){
            selection = args.getString("selection");
            selectionArgs = args.getStringArray("selectionArgs");
        }
        return new CursorLoader(context,
                uri,
                null,
                selection,
                selectionArgs,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        data.moveToFirst();
        if (listener != null)
            listener.loadEnd(loader, data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
