package com.igordubrovin.trainstimetable.adapters;

import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.igordubrovin.trainstimetable.R;
import com.igordubrovin.trainstimetable.utils.ConstProject;

/**
 * Created by Игорь on 21.02.2017.
 */

public class AdapterSearchStation extends RecyclerView.Adapter<AdapterSearchStation.AssViewHolder> {

    private Cursor cursorItem = null;

    @Override
    public AssViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search_recycler, parent, false);
        return new AssViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AssViewHolder holder, int position) {
        cursorItem.moveToPosition(position);
        holder.tvStation.setText(cursorItem.getString(cursorItem.getColumnIndex(ConstProject.COLUMN_NAME_STATION)));
    }

    @Override
    public int getItemCount() {
        return cursorItem != null ? cursorItem.getCount() : 0;
    }

    public void swapCursor(Cursor cursor){
        cursorItem = cursor;
        this.notifyDataSetChanged();
    }

    class AssViewHolder extends RecyclerView.ViewHolder{

        TextView tvStation;

        AssViewHolder(View itemView) {
            super(itemView);

            tvStation = (TextView)itemView.findViewById(R.id.tvSearchStation);

        }
    }
}
