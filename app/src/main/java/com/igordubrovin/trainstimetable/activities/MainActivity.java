package com.igordubrovin.trainstimetable.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.igordubrovin.trainstimetable.R;
import com.igordubrovin.trainstimetable.customView.CustomEditText;
import com.igordubrovin.trainstimetable.fragments.FragmentSearchStation;
import com.igordubrovin.trainstimetable.fragments.FragmentSelectionTrain;
import com.igordubrovin.trainstimetable.utils.ConstProject;
import com.igordubrovin.trainstimetable.utils.CurrentDate;

public class MainActivity extends AppCompatActivity implements FragmentSearchStation.OnSelectStationListener {

    private CustomEditText cetSearchFrom;
    private CustomEditText cetSearchTo;

    private TextView tvCurrentDay;

    private boolean cetFromIsClear;
    private boolean cetToIsClear;

  //  private ImageButton imgBtnSearchTrain;

    int codeStationFrom;
    int codeStationTo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null){
            cetFromIsClear = true;
            cetToIsClear = true;
        } else {
            cetFromIsClear = savedInstanceState.getBoolean("cetFromIsClear");
            cetToIsClear = savedInstanceState.getBoolean("cetToIsClear");
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null)
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        /*if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }*/

        tvCurrentDay = (TextView) findViewById(R.id.tvCurrentDay);
        updDate();

        cetSearchFrom = (CustomEditText) findViewById(R.id.etSearchFromStation);
        cetSearchTo = (CustomEditText) findViewById(R.id.etSearchToStation);

     //   imgBtnSearchTrain = (ImageButton) findViewById(R.id.imgBtnSearchTrain);

      //  imgBtnSearchTrain.setOnClickListener(onClickImgBtnSearch);

        cetSearchFrom.setOnEditorActionListener(hideVirtualKeyboard);
        cetSearchTo.setOnEditorActionListener(hideVirtualKeyboard);

        cetSearchFrom.setOnFocusChangeListener(onFocusChangeCetReplaceFragment);
        cetSearchTo.setOnFocusChangeListener(onFocusChangeCetReplaceFragment);

        cetSearchFrom.setOnTextChangedListener(textChangedListener);
        cetSearchTo.setOnTextChangedListener(textChangedListener);

        Fragment fragment = getCurrentFragment(ConstProject.FRAGMENT_SELECTION_TRAIN);

        if (fragment == null) {
            FragmentSelectionTrain fragmentSelectionTrain = new FragmentSelectionTrain();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragmentContainer, fragmentSelectionTrain, ConstProject.FRAGMENT_SELECTION_TRAIN)
                    .commit();

        }

        fragment = getCurrentFragment(ConstProject.FRAGMENT_SEARCH_STATION);

        if (fragment != null) {
            ((FragmentSearchStation)fragment).setOnSelectStation(this);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("cetFromIsClear", cetFromIsClear);
        outState.putBoolean("cetToIsClear", cetToIsClear);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        clearFocusET();
    }

    /*View callback*/

    private CustomEditText.OnTextChangedListener textChangedListener = new CustomEditText.OnTextChangedListener() {
        @Override
        public void onTextChangedListener(View v, String s) {
            searchInDB(s);
            switch (v.getId()){
                case R.id.etSearchFromStation:
                        cetFromIsClear = s.equals("");
                    break;
                case R.id.etSearchToStation:
                        cetToIsClear = s.equals("");
                    break;
            }
        }
    };

    /*private View.OnClickListener onClickImgBtnSearch = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            clearFocusET();
            hideKeyboard(v);
            Fragment fragment = getCurrentFragment(ConstProject.FRAGMENT_SEARCH_STATION);
            if (fragment != null && fragment.isVisible()) {
                getSupportFragmentManager().popBackStack();
            }
        }
    };*/

    private TextView.OnEditorActionListener hideVirtualKeyboard = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            hideKeyboard(v);
            return true;
        }
    };

    private View.OnFocusChangeListener onFocusChangeCetReplaceFragment = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            String partStationName = ((EditText)v).getText().toString();
            if (hasFocus){
                Fragment fragment = getCurrentFragment(ConstProject.FRAGMENT_SELECTION_TRAIN);
                if (fragment != null && fragment.isVisible()) {
                    FragmentSearchStation fragmentSearchStation = FragmentSearchStation.newInstance(partStationName);
                    fragmentSearchStation.setOnSelectStation(MainActivity.this);
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragmentContainer, fragmentSearchStation, ConstProject.FRAGMENT_SEARCH_STATION)
                            .addToBackStack(ConstProject.FRAGMENT_SELECTION_TRAIN)
                            .commit();
                }
                else searchInDB(partStationName);
            }

        }
    };

    /*Вспомогательные методы*/

    private Fragment getCurrentFragment(String fragmentTag){
        return  getSupportFragmentManager().findFragmentByTag(fragmentTag);
    }

    private void hideKeyboard(View v){
        InputMethodManager imm =(InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    /*private void showKeyboard(EditText et){
        InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
        imm.showSoftInput(et, InputMethodManager.SHOW_IMPLICIT);
    }*/

    private void clearFocusET(){
        cetSearchFrom.clearFocus();
        cetSearchTo.clearFocus();
    }

    private void searchInDB(String partStationName){
        Fragment fragment = getCurrentFragment(ConstProject.FRAGMENT_SEARCH_STATION);
        if (fragment != null){
            Bundle bundle = new Bundle();
            bundle.putString(ConstProject.PART_STATION_NAME, partStationName);
            ((FragmentSearchStation)fragment).searchStation(bundle);
        }
    }

    private void updDate(){
        tvCurrentDay.setText(CurrentDate.getCurrentData());
    }

    public boolean getCetFromIsClear(){
        return cetFromIsClear;
    }

    public boolean getCetToIsClear(){
        return cetToIsClear;
    }

    /*Fragments callback*/

    @Override
    public void onSelectStation(View view, String station, int code) {
        boolean cetFromIsFocused = cetSearchFrom.isFocused();
        boolean cetToIsFocused = cetSearchTo.isFocused();

        if (cetFromIsFocused){
            cetSearchFrom.setText(station);
            codeStationFrom = code;
            clearFocusET();
            cetFromIsFocused = false;
            if (cetSearchTo.getText().toString().equals("")){
                cetSearchTo.requestFocus();
                cetToIsFocused = true;
              //  showKeyboard(cetSearchTo);
            }
        } else if (cetToIsFocused){
            cetSearchTo.setText(station);
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
            hideKeyboard(view);
            getSupportFragmentManager().popBackStack();
        }
    }
}