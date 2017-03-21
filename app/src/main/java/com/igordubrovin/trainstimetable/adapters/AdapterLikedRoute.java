package com.igordubrovin.trainstimetable.adapters;

import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.igordubrovin.trainstimetable.R;
import com.igordubrovin.trainstimetable.utils.ConstProject;
import com.igordubrovin.trainstimetable.utils.ContentProviderLikedDB;

/**
 * Created by Игорь on 09.03.2017.
 */

public class AdapterLikedRoute extends RecyclerView.Adapter<AdapterLikedRoute.ViewHolder>{

    OnItemContextMenuClickListener listener;
    Cursor mCursor;

    public Cursor swapCursor(Cursor newCursor){
        if (newCursor == mCursor) {
            return null;
        }
        Cursor oldCursor = mCursor;
        mCursor = newCursor;
        if (newCursor != null) {
            notifyDataSetChanged();
        } else {
            notifyItemRangeRemoved(0, getItemCount());
            mCursor = null;
        }
        return oldCursor;
    }

    public void closeCursor(){
        if (mCursor != null)
            mCursor.close();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_liked_route_adapter, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        mCursor.moveToPosition(position);
        String a = mCursor.getString(mCursor.getColumnIndex(ContentProviderLikedDB.LIKED_DB_COLUMN_NAME_STATION_FROM));
        String b = mCursor.getString(mCursor.getColumnIndex(ContentProviderLikedDB.LIKED_DB_COLUMN_NAME_STATION_TO));;
        holder.tvItemFrom.setText(a);
        holder.tvItemTo.setText(b);
    }

    @Override
    public int getItemCount() {
        return mCursor != null ? mCursor.getCount() : 0;
    }

    public void setOnItemContextMenuClickListener(OnItemContextMenuClickListener l){
        listener = l;
    }

    public interface OnItemContextMenuClickListener{
        void onItemContextMenuClickListener(int id);
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener, MenuItem.OnMenuItemClickListener{
        LinearLayout llItemLiked;
        TextView tvItemFrom;
        TextView tvItemTo;
        ViewHolder(View itemView) {
            super(itemView);
            tvItemFrom = (TextView) itemView.findViewById(R.id.tvItemLikedFrom);
            tvItemTo = (TextView) itemView.findViewById(R.id.tvItemLikedTo);
            llItemLiked = (LinearLayout) itemView.findViewById(R.id.llItemLiked);

            llItemLiked.setOnCreateContextMenuListener(this);

        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.add(Menu.NONE, 0, Menu.NONE, ConstProject.DELETE).setOnMenuItemClickListener(this);
        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            if (listener != null){
                int pos = getAdapterPosition();
                mCursor.moveToPosition(pos);
                int id = mCursor.getInt(mCursor.getColumnIndex(ContentProviderLikedDB.LIKED_DB_COLUMN_NAME_ID));
                listener.onItemContextMenuClickListener(id);
                return true;
            }
            return false;
        }
    }



}
