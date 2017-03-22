package com.igordubrovin.trainstimetable.Interfaces;

/**
 * Created by Игорь on 22.03.2017.
 */

public interface OnItemAdapterClickListener {
    void onItemClick(int position);
    void onItemClick(int position, String stationFrom, String stationTo);
}
