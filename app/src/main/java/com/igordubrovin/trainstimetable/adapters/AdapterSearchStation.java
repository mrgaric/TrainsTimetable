package com.igordubrovin.trainstimetable.adapters;

import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.igordubrovin.trainstimetable.R;
import com.igordubrovin.trainstimetable.utils.ConstProject;

import static com.igordubrovin.trainstimetable.utils.ConstProject.COLUMN_CODE_STATION;

/**
 * Created by Игорь on 21.02.2017.
 */

public class AdapterSearchStation extends RecyclerView.Adapter<AdapterSearchStation.AssViewHolder> {

    private Cursor cursorItem = null;

    private OnItemRecyclerViewClickListener itemClickListener;


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

    public void setOnItemRecyclerViewClickListener(OnItemRecyclerViewClickListener clickListener){
        itemClickListener = clickListener;
    }

    public interface OnItemRecyclerViewClickListener{
        void onItemRecyclerViewClickListener(View view, int code);
    }

    class AssViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView tvStation;

        AssViewHolder(View itemView) {
            super(itemView);

            tvStation = (TextView)itemView.findViewById(R.id.tvSearchStation);
            tvStation.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            cursorItem.moveToPosition(getAdapterPosition());
            itemClickListener.onItemRecyclerViewClickListener(v, Integer.parseInt(cursorItem.getString(cursorItem.getColumnIndex(COLUMN_CODE_STATION))));
        }
    }
}
