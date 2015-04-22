package com.riandy.fas;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.SearchManager;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;

import com.riandy.fas.Alert.AlertDBHelper;
import com.riandy.fas.Alert.AlertFeature;
import com.riandy.fas.Alert.AlertModel;
import com.riandy.fas.Alert.AlertSpecs;
import com.riandy.fas.Alert.Constant;
import com.riandy.fas.Alert.DaySpecs;
import com.riandy.fas.Alert.HourSpecs;

import org.joda.time.LocalDate;
import org.joda.time.LocalTime;


public class MainActivity extends Activity {

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;

    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private String[] mOptionTitles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        showFileChooser();

        mTitle = mDrawerTitle = getTitle();
        mOptionTitles = getResources().getStringArray(R.array.menu_array);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        // set a custom shadow that overlays the main content when the drawer opens
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        // set up the drawer's list view with items and click listener
        mDrawerList.setAdapter(new ArrayAdapter<String>(this,
                R.layout.drawer_list_item, mOptionTitles));
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        // enable ActionBar app icon to behave as action to toggle nav drawer
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

        // ActionBarDrawerToggle ties together the the proper interactions
        // between the sliding drawer and the action bar app icon
        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                R.drawable.ic_drawer,  /* nav drawer image to replace 'Up' caret */
                R.string.drawer_open,  /* "open drawer" description for accessibility */
                R.string.drawer_close  /* "close drawer" description for accessibility */
        ) {
            public void onDrawerClosed(View view) {
                getActionBar().setTitle(mTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            public void onDrawerOpened(View drawerView) {
                getActionBar().setTitle(mDrawerTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        if (savedInstanceState == null) {
            selectItem(0);
        }

        Fragment fragment = findFragmentToLaunch();
        if(fragment!=null){
            fragment.setArguments(getIntent().getExtras());
            getFragmentManager().beginTransaction().replace(R.id.fragment_container,fragment).commit();
        }else{
            fragment = new SplashScreen();
            getFragmentManager().beginTransaction().replace(R.id.fragment_container,fragment).commit();
        }

//        SchedulerTest test = new SchedulerTest();
//        test.runTest();
    }

    /* Called whenever we call invalidateOptionsMenu() */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, hide action items related to the content view
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
        menu.findItem(R.id.action_search).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {

            SearchManager manager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);

            SearchView search = (SearchView) menu.findItem(R.id.action_search).getActionView();

            search.setSearchableInfo(manager.getSearchableInfo(getComponentName()));

            search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String query) {
                    Fragment fragment = getFragmentManager().findFragmentById(R.id.fragment_container);
                    if(fragment!=null && fragment instanceof HomePageFragment && !query.equals(""))
                        ((HomePageFragment)fragment).showAlertBySearch(query);
                    return true;

                }

            });

        }
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        int id = item.getItemId();

        switch(id){
            case R.id.action_settings:
                    //customize various settings
                break;
            case R.id.action_addAlert:
                    //launch new fragment to add alert
                Fragment fragment = new AddAlert();
                getFragmentManager().beginTransaction().replace(R.id.fragment_container,fragment).addToBackStack(null).
                        commit();
                break;

            case R.id.action_search:

                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private Fragment findFragmentToLaunch(){
        Fragment fragment = null;

        Bundle bundle = getIntent().getExtras();
        if(bundle!=null) {
            String fragmentToLaunch = bundle.getString(Constant.FRAGMENT_TAG);

            if(fragmentToLaunch==null)
                return fragment; // no specific fragment to launch

            switch(fragmentToLaunch){
                case Constant.FRAGMENT_ALERT_SCREEN:
                    fragment = new AlertScreenFragment();
                    break;

                default:
                    fragment = null;
            }
        }

        return fragment;
    }

    /* The click listner for ListView in the navigation drawer */
    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }
    }

    private void selectItem(int position) {

        Fragment fragment=null;

        switch (position){
            case 0: //Home
                fragment = new HomePageFragment();
                break;
            case 1: //Settings
                fragment = new SettingsFragment();
                break;
            case 2: //About
                fragment = new AboutFragment();
                break;
            case 3: //Exit
                this.finish();
                return;
            default:
                break;
        }

        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.fragment_container, fragment).commit();

        // update selected item and title, then close the drawer
        mDrawerList.setItemChecked(position, true);
        setTitle(mOptionTitles[position]);
        mDrawerLayout.closeDrawer(mDrawerList);
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getActionBar().setTitle(mTitle);
    }

    /**
     * When using the ActionBarDrawerToggle, you must call it during
     * onPostCreate() and onConfigurationChanged()...
     */

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    public void testAlert(){
        AlertModel model = new AlertModel();

        model.setEnabled(true);

        AlertFeature alertFeature = new AlertFeature();
        AlertSpecs alertSpecs = new AlertSpecs();

        alertFeature.setVibrationEnabled(true);
        alertFeature.setVoiceInstructionStatusEnabled(true);
        alertFeature.setSoundEnabled(true);
        alertFeature.setLaunchAppEnabled(false);
        alertFeature.setNotificationEnabled(true);
        //alertFeature.setTone("elegant_ringtone.mp3");
        alertFeature.setName("Buy lunch!");
        alertFeature.setDescription("This is a test alert testing the Text to speech feature!");
        alertFeature.setAppToLaunch("com.riandy.fas");

        alertSpecs.getDaySpecs().setDayType(DaySpecs.DayTypes.DATEONLY);
        alertSpecs.getDaySpecs().setDate(new LocalDate());
        alertSpecs.getDaySpecs().setDayOfWeek(new boolean[]{true,true,true,true,true,true,true});
        alertSpecs.getDaySpecs().setRepeatWeekly(true);

        alertSpecs.getHourSpecs().setHourType(HourSpecs.HourTypes.EXACTTIME);
        alertSpecs.getHourSpecs().setExactTime(new LocalTime().plusMinutes(2));
        alertSpecs.getHourSpecs().setNumOfTimes(1);

        model.setAlertFeature(alertFeature);
        model.setAlertSpecs(alertSpecs);

        AlertDBHelper db = AlertDBHelper.getInstance(getApplicationContext());

        long id = db.createAlert(model);
        Log.d("model id",""+id);

//        Log.d("SQL", "adding id = " + id);
//
//        //verify that database is working and able to retrieve all values
//
//        AlertModel modelTest = db.getAlert(id);
//        Assert.assertEquals(modelTest.getAlertFeature().getAppToLaunch(),model.getAlertFeature().getAppToLaunch());
//        Assert.assertEquals(modelTest.getAlertFeature().getDescription(),model.getAlertFeature().getDescription());
//        Assert.assertEquals(modelTest.getAlertFeature().getName(),model.getAlertFeature().getName());
//        Assert.assertEquals(modelTest.getAlertFeature().getTone(),model.getAlertFeature().getTone());
//        Assert.assertEquals(modelTest.getAlertFeature().isLaunchAppEnabled(),model.getAlertFeature().isLaunchAppEnabled());
//        Assert.assertEquals(modelTest.getAlertFeature().isNotificationEnabled(),model.getAlertFeature().isNotificationEnabled());
//        Assert.assertEquals(modelTest.getAlertFeature().isSoundEnabled(),model.getAlertFeature().isSoundEnabled());
//        Assert.assertEquals(modelTest.getAlertFeature().isVoiceInstructionStatusEnabled(),model.getAlertFeature().isVoiceInstructionStatusEnabled());
//        Assert.assertEquals(convertBooleanArrayToString(modelTest.getAlertSpecs().getDaySpecs().getDayOfWeek()),convertBooleanArrayToString(model.getAlertSpecs().getDaySpecs().getDayOfWeek()));
//        Assert.assertEquals(modelTest.getAlertSpecs().getDaySpecs().getStartDate().toString(),model.getAlertSpecs().getDaySpecs().getStartDate().toString());
//        Assert.assertEquals(modelTest.getAlertSpecs().getHourSpecs().getNumOfTimes(),model.getAlertSpecs().getHourSpecs().getNumOfTimes());

    }

    //For Testing only. can be removed later
    public static String convertBooleanArrayToString(boolean[] arr){

        String repeatingDays = "";
        for (int i = 0; i < 7; ++i) {
            repeatingDays += arr[i] + ",";
        }

        return repeatingDays;
    }
}
