package com.igordubrovin.trainstimetable.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;
import android.widget.TextView;

import com.igordubrovin.trainstimetable.R;
import com.igordubrovin.trainstimetable.customView.CustomEditText;
import com.igordubrovin.trainstimetable.fragments.FragmentSearchStation;
import com.igordubrovin.trainstimetable.fragments.FragmentSelectionTrain;

public class MainActivity extends AppCompatActivity {

    Toolbar toolbar;
    CustomEditText cetSearchFrom;
    CustomEditText cetSearchTo;

    ImageButton imgBtnSearchTrain;

    FragmentSearchStation fragmentSearchStation;
    FragmentSelectionTrain fragmentSelectionTrain;

    String stationFrom;
    String stationTo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        /*if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }*/

        cetSearchFrom = (CustomEditText) findViewById(R.id.etSearchFromStation);
        cetSearchTo = (CustomEditText) findViewById(R.id.etSearchToStation);

        stationFrom = cetSearchFrom.getText().toString();
        stationTo = cetSearchTo.getText().toString();

        imgBtnSearchTrain = (ImageButton) findViewById(R.id.imgBtnSearchTrain);

        fragmentSearchStation = new FragmentSearchStation();
        fragmentSelectionTrain = new FragmentSelectionTrain();

        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragmentContainer, fragmentSelectionTrain, "fragmentSelectionTrain")
                .commit();

        imgBtnSearchTrain.setOnClickListener(onClickImgBtnSearch);

        cetSearchFrom.setOnEditorActionListener(hideVirtualKeyboard);
        cetSearchTo.setOnEditorActionListener(hideVirtualKeyboard);

        cetSearchFrom.setOnFocusChangeListener(onFocusChangeCetReplaceFragment);
        cetSearchTo.setOnFocusChangeListener(onFocusChangeCetReplaceFragment);

        cetSearchTo.addTextChangedListener(textWatcher);
    }

    TextView.OnEditorActionListener hideVirtualKeyboard = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            clickSearch();
            return false;
        }
    };

    private View.OnFocusChangeListener onFocusChangeCetReplaceFragment = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (hasFocus){
                Fragment fragment = getSupportFragmentManager().findFragmentByTag("fragmentSelectionTrain");
                if (fragment != null && fragment.isVisible()) {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragmentContainer, fragmentSearchStation, "fragmentSearchStation")
                            .addToBackStack(null)
                            .commit();
                }
            }
        }
    };

    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            Bundle bundle = new Bundle();
            bundle.putString("partStationName", s.toString());
            fragmentSearchStation.searchStation(bundle);
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    private View.OnClickListener onClickImgBtnSearch = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            clickSearch();
        }
    };

    private void clickSearch(){
        Fragment fragment = getSupportFragmentManager().findFragmentByTag("fragmentSearchStation");
        if (fragment != null && fragment.isVisible()) {
            Bundle bundle = new Bundle();
            stationFrom = cetSearchFrom.getText().toString();
            stationTo = cetSearchTo.getText().toString();
            bundle.putString("stationFrom", stationFrom);
            bundle.putString("stationTo", stationTo);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentContainer, fragmentSelectionTrain, "fragmentSelectionTrain")
                    .commit();
            fragmentSelectionTrain.changeMainView(stationFrom, stationTo);
        }
        if (cetSearchFrom.isFocusable()) hideKeyboard(cetSearchFrom);
        if (cetSearchTo.isFocusable()) hideKeyboard(cetSearchTo);
    }

    private void hideKeyboard(CustomEditText cet){
        InputMethodManager imm =(InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(cet.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        cet.clearFocus();
    }
}
