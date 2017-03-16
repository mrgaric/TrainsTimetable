package com.igordubrovin.trainstimetable.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.igordubrovin.trainstimetable.R;
import com.igordubrovin.trainstimetable.utils.Train;

import java.util.List;

/**
 * Created by Игорь on 21.02.2017.
 */

public class AdapterSelectionTrain extends RecyclerView.Adapter<AdapterSelectionTrain.SelectionViewHolder> {

    private List<Train> listData;

    public void swapData(List<Train> listData){
        this.listData = listData;
        this.notifyDataSetChanged();
    }

    @Override
    public SelectionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_selection_recycler, parent, false);
        return new SelectionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SelectionViewHolder holder, int position) {
        holder.tvSelectionTrain.setText(listData.get(position).getStations());
    }

    @Override
    public int getItemCount() {
        return listData!= null ? listData.size() : 0;
    }

    class SelectionViewHolder extends RecyclerView.ViewHolder{

        TextView tvSelectionTrain;

        SelectionViewHolder(View itemView) {
            super(itemView);
            tvSelectionTrain = (TextView)itemView.findViewById(R.id.tvSelectionTrain);
        }

    }
}
