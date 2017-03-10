package com.igordubrovin.trainstimetable.fragments;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.igordubrovin.trainstimetable.R;
import com.igordubrovin.trainstimetable.adapters.AdapterLikedRoute;
import com.igordubrovin.trainstimetable.utils.CPLikedHelper;
import com.igordubrovin.trainstimetable.utils.ContentProviderLikedDB;

/**
 * Created by Игорь on 09.03.2017.
 */

public class FragmentLiked extends Fragment implements CPLikedHelper.LoadListener, AdapterLikedRoute.OnItemContextMenuClickListener {

    private AdapterLikedRoute adapter;
    private CPLikedHelper likedHelper;

    private TextView tvNotLiked;
    private RecyclerView rvLiked;

    private ActionLikedFragment listener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new AdapterLikedRoute();
        adapter.setOnItemContextMenuClickListener(this);
        likedHelper = new CPLikedHelper(getContext());
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

        likedHelper.setLoadListener(this);
        likedHelper.setUri(ContentProviderLikedDB.URI_LIKED_ROUTES_CONTENT)
                .startLoadItemDB(getActivity().getSupportLoaderManager(), CPLikedHelper.LOAD_LIKED, null);

        return view;
    }

    @Override
    public void onDestroyView() {
        adapter.closeCursor();
        super.onDestroyView();
    }

    @Override
    public void onItemContextMenuClickListener(int id) {
        String uri = ContentProviderLikedDB.URI_LIKED_ROUTES_CONTENT.toString() + "/" + id;
        Uri newUri = Uri.parse(uri);
        likedHelper.setUri(newUri)
                .deleteItemDB(null, null);
        if (listener != null)
            listener.actionDel(id);
    }

    @Override
    public void loadEnd(Loader<Cursor> loader, Cursor cursor) {
        if (cursor.getCount() != 0) {
            tvNotLiked.setVisibility(View.GONE);
            rvLiked.setVisibility(View.VISIBLE);
            adapter.swapCursor(cursor);
        } else {
            tvNotLiked.setVisibility(View.VISIBLE);
            rvLiked.setVisibility(View.GONE);
        }
    }

    public void setActionListener(ActionLikedFragment l){
        listener = l;
    }

    public interface ActionLikedFragment{
        void actionDel(int id);
    }
}
