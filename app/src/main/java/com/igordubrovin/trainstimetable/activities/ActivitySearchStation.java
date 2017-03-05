package com.igordubrovin.trainstimetable.activities;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.igordubrovin.trainstimetable.R;
import com.igordubrovin.trainstimetable.adapters.AdapterSearchStation;
import com.igordubrovin.trainstimetable.customView.CustomEditText;
import com.igordubrovin.trainstimetable.utils.ConstProject;
import com.igordubrovin.trainstimetable.utils.CLForSearchStationDB;

/**
 * Created by Игорь on 02.03.2017.
 */

public class ActivitySearchStation extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>, AdapterSearchStation.OnItemRecyclerViewClickListener{

    RecyclerView rvSearchStation;
    AdapterSearchStation adapterSearchStation;

    private CustomEditText cetSearchFrom;
    private CustomEditText cetSearchTo;

    int codeStationFrom;
    int codeStationTo;

    protected void onCreate(Bundle savedInstanceState) {

        String stationFrom;
        String stationTo;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_station);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_activity_search_station);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        adapterSearchStation = new AdapterSearchStation();
        adapterSearchStation.setOnItemRecyclerViewClickListener(this);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        rvSearchStation = (RecyclerView) findViewById(R.id.rvSearchStation);
        rvSearchStation.setLayoutManager(linearLayoutManager);
        rvSearchStation.setAdapter(adapterSearchStation);

        startSearch("");

        cetSearchFrom = (CustomEditText) findViewById(R.id.etSearchFromStation);
        cetSearchTo = (CustomEditText) findViewById(R.id.etSearchToStation);

        cetSearchFrom.setOnTextChangedListener(textChangedListener);
        cetSearchTo.setOnTextChangedListener(textChangedListener);

        cetSearchFrom.setOnFocusChangeListener(onFocusChangeCet);
        cetSearchTo.setOnFocusChangeListener(onFocusChangeCet);

        stationFrom = getIntent().getStringExtra(ConstProject.STATION_FROM);
        stationTo = getIntent().getStringExtra(ConstProject.STATION_TO);

        cetSearchFrom.setText(stationFrom);
        cetSearchTo.setText(stationTo);

    }

    View.OnFocusChangeListener onFocusChangeCet = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (hasFocus){
                startSearch(((CustomEditText)v).getText().toString());
            }
        }
    };

    private CustomEditText.OnTextChangedListener textChangedListener = new CustomEditText.OnTextChangedListener() {
        @Override
        public void onTextChangedListener(View v, String s) {
            startSearch(s);
        }
    };

    @Override
    public void onItemRecyclerViewClickListener(String s, int code) {
        boolean cetFromIsFocused = cetSearchFrom.isFocused();
        boolean cetToIsFocused = cetSearchTo.isFocused();

        if (cetFromIsFocused){
            cetSearchFrom.setText(s);
            codeStationFrom = code;
            clearFocusET();
            cetFromIsFocused = false;
            if (cetSearchTo.getText().toString().equals("")){
                cetSearchTo.requestFocus();
                cetToIsFocused = true;
                //  showKeyboard(cetSearchTo);
            }
        } else if (cetToIsFocused){
            cetSearchTo.setText(s);
            codeStationTo = code;
            clearFocusET();
            cetToIsFocused = false;
            if (cetSearchFrom.getText().toString().equals("")){
                cetSearchFrom.requestFocus();
                cetFromIsFocused = true;
                //  showKeyboard(cetSearchFrom);
            }
        }
        if (!cetFromIsFocused && !cetToIsFocused){
            Intent intent = new Intent();
            intent.putExtra(ConstProject.STATION_FROM, cetSearchFrom.getText().toString());
            intent.putExtra(ConstProject.STATION_TO, cetSearchTo.getText().toString());
            setResult(RESULT_OK, intent);
            finish();
        }
    }

    private void startSearch(String s){
        Bundle bundle = new Bundle();
        bundle.putString(ConstProject.PART_STATION_NAME, s);
        searchStation(bundle);
    }

    public void searchStation(Bundle bundle){
        getSupportLoaderManager().restartLoader(0, bundle, this);
    }

    private void clearFocusET(){
        cetSearchFrom.clearFocus();
        cetSearchTo.clearFocus();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CLForSearchStationDB(getApplicationContext(), args);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        adapterSearchStation.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
