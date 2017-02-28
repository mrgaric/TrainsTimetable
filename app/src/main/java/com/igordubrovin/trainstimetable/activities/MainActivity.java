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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.igordubrovin.trainstimetable.R;
import com.igordubrovin.trainstimetable.customView.CustomEditText;
import com.igordubrovin.trainstimetable.fragments.FragmentSearchStation;
import com.igordubrovin.trainstimetable.fragments.FragmentSelectionTrain;
import com.igordubrovin.trainstimetable.utils.ConstProject;

public class MainActivity extends AppCompatActivity implements FragmentSearchStation.OnSelectStationListener {

    Toolbar toolbar;
    CustomEditText cetSearchFrom;
    CustomEditText cetSearchTo;

    ImageButton imgBtnSearchTrain;

    int codeStationFrom;
    int codeStationTo;

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

        imgBtnSearchTrain = (ImageButton) findViewById(R.id.imgBtnSearchTrain);

        imgBtnSearchTrain.setOnClickListener(onClickImgBtnSearch);

        cetSearchFrom.setOnEditorActionListener(hideVirtualKeyboard);
        cetSearchTo.setOnEditorActionListener(hideVirtualKeyboard);

        cetSearchFrom.setOnFocusChangeListener(onFocusChangeCetReplaceFragment);
        cetSearchTo.setOnFocusChangeListener(onFocusChangeCetReplaceFragment);

        cetSearchFrom.addTextChangedListener(textWatcher);
        cetSearchTo.addTextChangedListener(textWatcher);

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
    public void onBackPressed() {
        super.onBackPressed();
        clearFocusET();
    }

    /*View callback*/

    private View.OnClickListener onClickImgBtnSearch = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            clearFocusET();
            hideKeyboard(v);
            Fragment fragment = getCurrentFragment(ConstProject.FRAGMENT_SEARCH_STATION);
            if (fragment != null && fragment.isVisible()) {
                getSupportFragmentManager().popBackStack();
            }
        }
    };

    TextView.OnEditorActionListener hideVirtualKeyboard = new TextView.OnEditorActionListener() {
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

    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            searchInDB(s.toString());
        }

        @Override
        public void afterTextChanged(Editable s) {

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

    private void showKeyboard(EditText et){
        InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
        imm.showSoftInput(et, InputMethodManager.SHOW_IMPLICIT);
    }

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

    /*Fragments callback*/

    @Override
    public void onSelectStation(String station, int code) {
        if (cetSearchFrom.isFocused()){
            cetSearchFrom.setText(station);
            codeStationFrom = code;
            clearFocusET();
            if (cetSearchTo.getText().toString().equals("")){
                cetSearchTo.requestFocus();
              //  showKeyboard(cetSearchTo);
            }
        } else if (cetSearchTo.isFocused()){
            cetSearchTo.setText(station);
            codeStationTo = code;
            clearFocusET();
            if (cetSearchFrom.getText().toString().equals("")){
                cetSearchFrom.requestFocus();
              //  showKeyboard(cetSearchFrom);
            }
        }
    }
}