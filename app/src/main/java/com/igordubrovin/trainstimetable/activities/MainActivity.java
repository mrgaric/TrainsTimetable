package com.igordubrovin.trainstimetable.activities;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.igordubrovin.trainstimetable.R;
import com.igordubrovin.trainstimetable.dialogs.DateDialogFragment;
import com.igordubrovin.trainstimetable.fragments.FragmentLiked;
import com.igordubrovin.trainstimetable.fragments.FragmentSelectionTrain;
import com.igordubrovin.trainstimetable.utils.CPLikedHelper;
import com.igordubrovin.trainstimetable.utils.ConstProject;
import com.igordubrovin.trainstimetable.utils.ContentProviderLikedDB;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

public class MainActivity extends AppCompatActivity implements CPLikedHelper.LoadListener{

    private CPLikedHelper likedHelper;

    private Drawer drawer;
    private String[] selectFragment;

    private TextView tvSearchStation;

    private String stationFrom = "";
    private String stationTo = "";

    private Toolbar toolbar;
    private FrameLayout containerItemToolbar;
    private LinearLayout llSelection;
    private TextView tvImmediate;
    private TextView tvForDay;
    private TextView tvChoiceDate;
    private ImageView ivLiked;

    private boolean liked;
    private int likedId;

    private FragmentSelectionTrain fragmentSelectionTrain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        llSelection = (LinearLayout)findViewById(R.id.llSelection);
        llSelection.setVisibility(View.GONE);

        tvImmediate = (TextView) findViewById(R.id.tvImmediate);
        tvForDay = (TextView) findViewById(R.id.tvForDay);
        tvChoiceDate = (TextView) findViewById(R.id.tvChoiceDate);
        setColorEditText(tvImmediate, tvForDay, tvChoiceDate);

        tvImmediate.setOnClickListener(clickSelectTimetable);
        tvForDay.setOnClickListener(clickSelectTimetable);
        tvChoiceDate.setOnClickListener(clickSelectTimetable);

        containerItemToolbar = (FrameLayout) findViewById(R.id.toolbarItemContainer);
        getLayoutInflater().inflate(R.layout.toolbar_item_1, containerItemToolbar, true);

        tvSearchStation = (TextView) containerItemToolbar.findViewById(R.id.tvSearchStation);
        tvSearchStation.setOnClickListener(tvSearchStationClick);

        ivLiked = (ImageView) findViewById(R.id.ivLiked);
        ivLiked.setOnClickListener(clickIvLiked);

        selectFragment = getResources().getStringArray(R.array.selectionFragment);

        PrimaryDrawerItem[] primaryDrawerItems = new PrimaryDrawerItem[selectFragment.length];

        for (int i = 0; i < selectFragment.length; i++) {
            primaryDrawerItems[i] = new PrimaryDrawerItem()
                    .withName(selectFragment[i])
                    .withIdentifier(i)
                    .withSelectable(true);
        }

        AccountHeader accountHeader = new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.drawable.train)
                .withHeightDp(100)
                .build();

        drawer = new DrawerBuilder(this)
                .withActivity(this)
                .withAccountHeader(accountHeader)
                .withDisplayBelowStatusBar(true)
                .withToolbar(toolbar)
                .withActionBarDrawerToggleAnimated(true)
                .withSavedInstance(savedInstanceState)
                .addDrawerItems((IDrawerItem[]) primaryDrawerItems)
                .withSavedInstance(savedInstanceState)
                .withShowDrawerOnFirstLaunch(true)
                .withOnDrawerItemClickListener(drawerItemClickListener)
                .build();

        likedHelper = new CPLikedHelper(getApplicationContext());
        likedHelper.setLoadListener(this);

        Fragment fragment = getCurrentFragment(ConstProject.FRAGMENT_SELECTION_TRAIN);
        if (fragment == null) {
            fragment = getCurrentFragment(ConstProject.FRAGMENT_LIKED_ROUTE);
            if (fragment == null) {
                fragmentSelectionTrain = new FragmentSelectionTrain();
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.fragmentContainer, fragmentSelectionTrain, ConstProject.FRAGMENT_SELECTION_TRAIN)
                        .commit();
            }
        } else fragmentSelectionTrain = (FragmentSelectionTrain)fragment;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1){
            if (resultCode == RESULT_OK){
                String selection;
                String[] selectionArgs;
                stationFrom = data.getStringExtra(ConstProject.STATION_FROM);
                stationTo = data.getStringExtra(ConstProject.STATION_TO);
                tvSearchStation.setText(stationFrom + " - " + stationTo);
                llSelection.setVisibility(View.VISIBLE);

                selection = ContentProviderLikedDB.LIKED_DB_COLUMN_NAME_STATION_FROM + " = ? AND " + ContentProviderLikedDB.LIKED_DB_COLUMN_NAME_STATION_TO + " = ?";
                selectionArgs = new String[]{stationFrom, stationTo};

                Bundle bundle = new Bundle();
                bundle.putString("selection", selection);
                bundle.putStringArray("selectionArgs", selectionArgs);
                likedHelper.setUri(ContentProviderLikedDB.URI_LIKED_ROUTES_CONTENT)
                        .startLoadItemDB(getSupportLoaderManager(), CPLikedHelper.CHECK_LIKED, bundle);
            }
        }
    }

    @Override
    public void onBackPressed() {
        if(drawer.isDrawerOpen()){
            drawer.closeDrawer();
        }else super.onBackPressed();
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putBoolean("liked", liked);
        outState.putInt("likedId", likedId);
        outState.putString("StationFrom", stationFrom);
        outState.putString("StationTo", stationTo);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null){
            stationFrom = savedInstanceState.getString("stationFrom");
            stationTo = savedInstanceState.getString("stationTo");
            liked = savedInstanceState.getBoolean("liked");
            if (liked)
                setLiked(savedInstanceState.getInt("likedId"));

        }
    }

    //View listener

    Drawer.OnDrawerItemClickListener drawerItemClickListener = new Drawer.OnDrawerItemClickListener() {
        @Override
        public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
            if (drawerItem != null){
                switch (position){
                    case 1:
                        containerItemToolbar.setVisibility(View.VISIBLE);
                        FragmentSelectionTrain fragmentSelectionTrain = new FragmentSelectionTrain();
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.fragmentContainer, fragmentSelectionTrain, ConstProject.FRAGMENT_SELECTION_TRAIN)
                                .commit();
                        break;
                    case 2:
                        containerItemToolbar.setVisibility(View.GONE);
                        llSelection.setVisibility(View.GONE);
                        FragmentLiked fragmentLiked = new FragmentLiked();
                        fragmentLiked.setActionListener(listenerActionLikedFragment);
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.fragmentContainer, fragmentLiked, ConstProject.FRAGMENT_LIKED_ROUTE)
                                .commit();
                        break;
                    case 3:

                        break;
                }
            }
            return false;
        }
    };

    View.OnClickListener tvSearchStationClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(MainActivity.this, ActivitySearchStation.class);
            intent.putExtra(ConstProject.STATION_FROM, stationFrom);
            intent.putExtra(ConstProject.STATION_TO, stationTo);
            startActivityForResult(intent, 1);
        }
    };

    private Fragment getCurrentFragment(String fragmentTag){
        return  getSupportFragmentManager().findFragmentByTag(fragmentTag);
    }

    View.OnClickListener clickSelectTimetable = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.tvImmediate:
                    fragmentSelectionTrain.immediate("Москва", "Зеленоград");
                    setColorEditText(tvImmediate, tvForDay, tvChoiceDate);
                    break;
                case R.id.tvForDay:
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragmentContainer, fragmentSelectionTrain, ConstProject.FRAGMENT_SELECTION_TRAIN)
                            .commit();
                    fragmentSelectionTrain.forDay("Москва", "Одинцово");
                    setColorEditText(tvForDay, tvChoiceDate, tvImmediate);
                    break;
                case R.id.tvChoiceDate:
                    setColorEditText(tvChoiceDate, tvForDay, tvImmediate);
                    DateDialogFragment dateDialogFragment = new DateDialogFragment();
                    dateDialogFragment.setActionListener(listenerDateDialog);
                    dateDialogFragment.show(getSupportFragmentManager(), "dateDialog");
                    break;
            }
        }
    };

    View.OnClickListener clickIvLiked = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (liked){
                String selection = ContentProviderLikedDB.LIKED_DB_COLUMN_NAME_STATION_FROM + " = ? AND " + ContentProviderLikedDB.LIKED_DB_COLUMN_NAME_STATION_TO + " = ?";
                String[] selectionArgs = new String[]{stationFrom, stationTo};

                likedHelper.setUri(ContentProviderLikedDB.URI_LIKED_ROUTES_CONTENT)
                        .deleteItemDB(selection, selectionArgs);
               // getContentResolver().delete(ContentProviderLikedDB.URI_LIKED_ROUTES_CONTENT, selection, selectionArgs);
                resetLiked();
            }
            else {
                Uri resultUri;

                ContentValues cv = new ContentValues();
                cv.put(ContentProviderLikedDB.LIKED_DB_COLUMN_NAME_STATION_FROM, stationFrom);
                cv.put(ContentProviderLikedDB.LIKED_DB_COLUMN_NAME_STATION_TO, stationTo);

                resultUri = likedHelper.setUri(ContentProviderLikedDB.URI_LIKED_ROUTES_CONTENT)
                        .insertItemDB(cv);
                setLiked(Integer.parseInt(resultUri.getLastPathSegment()));
            }
        }
    };

    // callback fragment

    DateDialogFragment.ActionListener listenerDateDialog = new DateDialogFragment.ActionListener() {
        @Override
        public void clickPositiveButton(int year, int month, int dayOfMonth) {
          //  fragmentSelectionTrain.choiceDate();
        }

        @Override
        public void cancelWithoutDate() {
            Snackbar snackbar;
            Snackbar.make(getWindow().getDecorView().getRootView(), "Дата не выбрана", BaseTransientBottomBar.LENGTH_SHORT).show();
            tvImmediate.callOnClick();
        }
    };

    FragmentLiked.ActionLikedFragment listenerActionLikedFragment = new FragmentLiked.ActionLikedFragment() {
        @Override
        public void actionDel(int id) {
            if (likedId == id){
                resetLiked();
            }
        }
    };

    //вспомогательные методы

    private void setColorEditText(TextView tvChoice, TextView tvNonChoice1, TextView tvNonChoice2){
        tvChoice.setHintTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorAccent));
        tvNonChoice1.setHintTextColor(ContextCompat.getColor(getApplicationContext(), R.color.secondary_text_material_dark));
        tvNonChoice2.setHintTextColor(ContextCompat.getColor(getApplicationContext(), R.color.secondary_text_material_dark));
    }

    private void setLiked(int id){
        ivLiked.setImageResource(R.drawable.star_liked);
        liked = true;
        likedId = id;
    }

    private void resetLiked(){
        ivLiked.setImageResource(R.drawable.star_not_liked);
        liked = false;
    }

    //LoaderCallback

    @Override
    public void loadEnd(Loader<Cursor> loader, Cursor cursor) {
        if (cursor.getCount() != 0){
            setLiked(cursor.getInt(cursor.getColumnIndex(ContentProviderLikedDB.LIKED_DB_COLUMN_NAME_ID)));
        } else {
            resetLiked();
        }
        cursor.close();
    }
}