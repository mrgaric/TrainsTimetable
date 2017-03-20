package com.igordubrovin.trainstimetable.activities;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.igordubrovin.trainstimetable.Interface.OnChangeTabListener;
import com.igordubrovin.trainstimetable.R;
import com.igordubrovin.trainstimetable.dialogs.DateDialogFragment;
import com.igordubrovin.trainstimetable.fragments.FragmentLiked;
import com.igordubrovin.trainstimetable.fragments.FragmentSelectionTrain;
import com.igordubrovin.trainstimetable.utils.CPLikedHelper;
import com.igordubrovin.trainstimetable.utils.ConstProject;
import com.igordubrovin.trainstimetable.utils.ContentProviderLikedDB;
import com.igordubrovin.trainstimetable.utils.DateHelper;
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

    private ImageView ivLiked;

    private boolean liked;
    private int likedId;

    private MenuItem calendarMenuItem;



    private FragmentSelectionTrain fragmentSelectionTrain;

    private int choiceTimetable = ConstProject.CHOICE_FOR_IMMEDIATE;

    AppBarLayout appBarLayout;

    FrameLayout fragmentContainer;
    TabLayout tabLayoutToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        createToolbar();
        tabLayoutToolbar = (TabLayout) findViewById(R.id.tabToolbar);
        createMaterialDrawer(savedInstanceState);

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
        fragmentSelectionTrain.setOnChangeTebListener(onChangeTabListener);

        likedHelper = new CPLikedHelper(getApplicationContext());
        likedHelper.setLoadListener(this);
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

                selection = ContentProviderLikedDB.LIKED_DB_COLUMN_NAME_STATION_FROM + " = ? AND " + ContentProviderLikedDB.LIKED_DB_COLUMN_NAME_STATION_TO + " = ?";
                selectionArgs = new String[]{stationFrom, stationTo};

                Bundle bundle = new Bundle();
                bundle.putString("selection", selection);
                bundle.putStringArray("selectionArgs", selectionArgs);
                likedHelper.setUri(ContentProviderLikedDB.URI_LIKED_ROUTES_CONTENT)
                        .startLoadItemDB(getSupportLoaderManager(), CPLikedHelper.CHECK_LIKED, bundle);

                FragmentSelectionTrain.setStations(stationFrom, stationTo);
                setTabLayout();
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
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("liked", liked);
        outState.putInt("likedId", likedId);
        outState.putString("StationFrom", stationFrom);
        outState.putString("StationTo", stationTo);
        outState.putInt("choiceTimetable", choiceTimetable);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null){
            stationFrom = savedInstanceState.getString("StationFrom");
            stationTo = savedInstanceState.getString("StationTo");
            liked = savedInstanceState.getBoolean("liked");
            choiceTimetable = savedInstanceState.getInt("choiceTimetable");
            if (liked)
                setLiked(savedInstanceState.getInt("likedId"));
            if (stationFrom != null){
                tvSearchStation.setText(stationFrom + " - " + stationTo);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main_activity, menu);
        calendarMenuItem = menu.findItem(R.id.menu_calendar);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        DateDialogFragment dateDialogFragment = new DateDialogFragment();
        dateDialogFragment.setActionListener(listenerDateDialog);
        dateDialogFragment.show(getSupportFragmentManager(), "dateDialog");
        return super.onOptionsItemSelected(item);
    }

    //View listener

    Drawer.OnDrawerItemClickListener drawerItemClickListener = new Drawer.OnDrawerItemClickListener() {
        @Override
        public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
            if (drawerItem != null){
                switch (position){
                    case 1:
                        containerItemToolbar.setVisibility(View.VISIBLE);
                        getSupportFragmentManager().popBackStack();
                        setTabLayout();
                        break;
                    case 2:
                        containerItemToolbar.setVisibility(View.GONE);
                        FragmentLiked fragmentLiked = new FragmentLiked();
                        fragmentLiked.setActionListener(listenerActionLikedFragment);
                        getSupportFragmentManager().beginTransaction()
                                .addToBackStack(ConstProject.FRAGMENT_SELECTION_TRAIN)
                                .replace(R.id.fragmentContainer, fragmentLiked, ConstProject.FRAGMENT_LIKED_ROUTE)
                                .commit();
                        collapseTabLayout();
                        break;
                    case 3:

                        break;
                }
                appBarLayout.setExpanded(true);
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



    View.OnClickListener clickIvLiked = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (liked){
                String selection = ContentProviderLikedDB.LIKED_DB_COLUMN_NAME_STATION_FROM + " = ? AND " + ContentProviderLikedDB.LIKED_DB_COLUMN_NAME_STATION_TO + " = ?";
                String[] selectionArgs = new String[]{stationFrom, stationTo};
                likedHelper.setUri(ContentProviderLikedDB.URI_LIKED_ROUTES_CONTENT)
                        .deleteItemDB(selection, selectionArgs);
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
            String dayDeparture;
            String monthDeparture;
            dayDeparture = String.valueOf(dayOfMonth);
            monthDeparture = DateHelper.getMonthStr(month);
            FragmentSelectionTrain.setDateDeparture(dayDeparture, monthDeparture);
        }

        @Override
        public void cancelWithoutDate() {
            Snackbar.make(getWindow().getDecorView().getRootView(), "Дата не выбрана", BaseTransientBottomBar.LENGTH_SHORT).show();
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

    private Fragment getCurrentFragment(String fragmentTag){
        return  getSupportFragmentManager().findFragmentByTag(fragmentTag);
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

    private void createToolbar(){
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        fragmentContainer = (FrameLayout) findViewById(R.id.fragmentContainer);
        containerItemToolbar = (FrameLayout) findViewById(R.id.toolbarItemContainer);
        getLayoutInflater().inflate(R.layout.toolbar_item_1, containerItemToolbar, true);
        appBarLayout = (AppBarLayout) findViewById(R.id.appBarLayout);
        tvSearchStation = (TextView) containerItemToolbar.findViewById(R.id.tvSearchStation);
        tvSearchStation.setOnClickListener(tvSearchStationClick);
        ivLiked = (ImageView) findViewById(R.id.ivLiked);
        ivLiked.setOnClickListener(clickIvLiked);
    }

    private void createMaterialDrawer(Bundle savedInstanceState){
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
    }

    private void setTabLayout(){
        tabLayoutToolbar.setVisibility(View.VISIBLE);
        tabLayoutToolbar.setupWithViewPager(fragmentSelectionTrain.getVpContainer());
    }

    private void collapseTabLayout(){
        tabLayoutToolbar.setVisibility(View.GONE);
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

    // fragment callback

    OnChangeTabListener onChangeTabListener = new OnChangeTabListener() {
        @Override
        public void onChangeTab(int position) {
            if (position == 2){
                calendarMenuItem.setVisible(true);
            }
            else calendarMenuItem.setVisible(false);
        }
    };
}