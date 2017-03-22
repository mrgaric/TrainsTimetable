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

public class AdapterTrainsRecycler extends RecyclerView.Adapter<AdapterTrainsRecycler.SelectionViewHolder> {

    private List<Train> listData;

    public void swapData(List<Train> listData){
        this.listData = listData;
        this.notifyDataSetChanged();
    }

    @Override
    public SelectionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_trains_recycler, parent, false);
        return new SelectionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SelectionViewHolder holder, int position) {
        holder.tvStations.setText(listData.get(position).getStations());
        holder.tvTimeDeparture.setText(listData.get(position).getTimeDeparture());
        holder.tvTimeArrival.setText(listData.get(position).getTimeArrival());
        holder.tvTravelTime.setText("Время в пути: " + listData.get(position).getTravelTime());
        holder.tvPrice.setText(listData.get(position).getPrice());
        holder.tvSpecialTrain.setText(listData.get(position).getSpecialTrain());
        holder.tvTimeBeforeDeparture.setText(listData.get(position).getTimeBeforeDeparture());
    }

    @Override
    public int getItemCount() {
        return listData!= null ? listData.size() : 0;
    }

    class SelectionViewHolder extends RecyclerView.ViewHolder{

        TextView tvStations;
        TextView tvTimeDeparture;
        TextView tvTimeArrival;
        TextView tvSpecialTrain;
        TextView tvTravelTime;
        TextView tvPrice;
        TextView tvTimeBeforeDeparture;

        SelectionViewHolder(View itemView) {
            super(itemView);
            tvStations = (TextView)itemView.findViewById(R.id.tvStations);
            tvTimeDeparture = (TextView)itemView.findViewById(R.id.tvTimeDeparture);
            tvTimeArrival = (TextView)itemView.findViewById(R.id.tvTimeArrival);
            tvSpecialTrain = (TextView)itemView.findViewById(R.id.tvSpecialTrain);
            tvTravelTime = (TextView)itemView.findViewById(R.id.tvTravelTime);
            tvPrice = (TextView)itemView.findViewById(R.id.tvPrice);
            tvTimeBeforeDeparture = (TextView)itemView.findViewById(R.id.tvTimeBeforeDeparture);
        }

    }
}
