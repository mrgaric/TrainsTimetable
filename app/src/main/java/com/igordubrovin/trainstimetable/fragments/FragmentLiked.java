package com.igordubrovin.trainstimetable.fragments;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.igordubrovin.trainstimetable.R;
import com.igordubrovin.trainstimetable.adapters.AdapterLikedRoute;
import com.igordubrovin.trainstimetable.utils.ContentProviderLikedDB;

/**
 * Created by Игорь on 09.03.2017.
 */

public class FragmentLiked extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, AdapterLikedRoute.OnItemContextMenuClickListener {

    private final static int LOAD_LIKED = 1;

    AdapterLikedRoute adapter;

    TextView tvNotLiked;
    RecyclerView rvLiked;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new AdapterLikedRoute();
        adapter.setOnItemContextMenuClickListener(this);
        this.setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_liked_route, container, false);

        tvNotLiked = (TextView) view.findViewById(R.id.tvNotLiked);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rvLiked = (RecyclerView) view.findViewById(R.id.rvLiked);
        rvLiked.setLayoutManager(linearLayoutManager);
        rvLiked.setAdapter(adapter);
        getActivity().getSupportLoaderManager().restartLoader(LOAD_LIKED, null, this);
        return view;
    }

    @Override
    public void onItemContextMenuClickListener(int id) {
        String uri = ContentProviderLikedDB.URI_LIKED_ROUTES_CONTENT.toString() + "/" + id;
        Uri newUri = Uri.parse(uri);
        getContext().getContentResolver().delete(newUri, null, null);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getContext(),
                ContentProviderLikedDB.URI_LIKED_ROUTES_CONTENT,
                null,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (loader.getId() == LOAD_LIKED){
            if (data.getCount() != 0){
                tvNotLiked.setVisibility(View.GONE);
                rvLiked.setVisibility(View.VISIBLE);
                adapter.swapCursor(data);
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
