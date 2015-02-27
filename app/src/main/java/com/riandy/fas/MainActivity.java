package com.riandy.fas;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.riandy.fas.Alert.AlertDBHelper;
import com.riandy.fas.Alert.AlertFeature;
import com.riandy.fas.Alert.AlertManagerHelper;
import com.riandy.fas.Alert.AlertModel;
import com.riandy.fas.Alert.AlertScreenFragment;
import com.riandy.fas.Alert.AlertSpecs;
import com.riandy.fas.Alert.Constant;
import com.riandy.fas.Alert.DaySpecs;
import com.riandy.fas.Alert.HourSpecs;

import junit.framework.Assert;

import org.joda.time.LocalDate;
import org.joda.time.LocalTime;


public class MainActivity extends FragmentActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Fragment fragment = findFragmentToLaunch();
        if(fragment!=null){
            fragment.setArguments(getIntent().getExtras());
            getFragmentManager().beginTransaction().replace(R.id.fragment_container,fragment).commit();
        }else{
            testAlert();
            AlertManagerHelper.setAlerts(getApplicationContext());
        }

        /*Bundle bundle = getIntent().getExtras();
        if(bundle!=null) {
            Log.d("Output",""+bundle.get("Fragment"));
            String fragmentToLaunch = bundle.getString("Fragment");

            if(fragmentToLaunch==null){

            }else if(fragmentToLaunch.equals("123")){
                bundle.remove("Fragment");
                Log.d("Output", ""+bundle.getString(AlertContract.Alert.COLUMN_NAME_ALERT_DESCRIPTION));
                Log.d("Output", "ID=" + bundle.getInt("id"));
                // Create a new Fragment to be placed in the activity layout
                Fragment firstFragment = new AlertScreenFragment();

                // In case this activity was started with special instructions from an
                // Intent, pass the Intent's extras to the fragment as arguments
                firstFragment.setArguments(getIntent().getExtras());

                // Add the fragment to the 'fragment_container' FrameLayout
                getFragmentManager().beginTransaction()
                        .add(R.id.fragment_container, firstFragment).commit();
            }
        }else{
            testAlert();
            AlertManagerHelper.setAlerts(getApplicationContext());
        }
        */
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
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
        Log.d("SQL","adding id = "+id);

        //verify that database is working and able to retrieve all values

        AlertModel modelTest = db.getAlert(id);
        Assert.assertEquals(modelTest.getAlertFeature().getAppToLaunch(),model.getAlertFeature().getAppToLaunch());
        Assert.assertEquals(modelTest.getAlertFeature().getDescription(),model.getAlertFeature().getDescription());
        Assert.assertEquals(modelTest.getAlertFeature().getName(),model.getAlertFeature().getName());
        Assert.assertEquals(modelTest.getAlertFeature().getTone(),model.getAlertFeature().getTone());
        Assert.assertEquals(modelTest.getAlertFeature().isLaunchAppEnabled(),model.getAlertFeature().isLaunchAppEnabled());
        Assert.assertEquals(modelTest.getAlertFeature().isNotificationEnabled(),model.getAlertFeature().isNotificationEnabled());
        Assert.assertEquals(modelTest.getAlertFeature().isSoundEnabled(),model.getAlertFeature().isSoundEnabled());
        Assert.assertEquals(modelTest.getAlertFeature().isVoiceInstructionStatusEnabled(),model.getAlertFeature().isVoiceInstructionStatusEnabled());
        Assert.assertEquals(convertBooleanArrayToString(modelTest.getAlertSpecs().getDaySpecs().getDayOfWeek()),convertBooleanArrayToString(model.getAlertSpecs().getDaySpecs().getDayOfWeek()));
        Assert.assertEquals(modelTest.getAlertSpecs().getDaySpecs().getStartDate().toString(),model.getAlertSpecs().getDaySpecs().getStartDate().toString());
        Assert.assertEquals(modelTest.getAlertSpecs().getHourSpecs().getNumOfTimes(),model.getAlertSpecs().getHourSpecs().getNumOfTimes());

    }

    //For Testing only. can be removed later
    private String convertBooleanArrayToString(boolean[] arr){

        String repeatingDays = "";
        for (int i = 0; i < 7; ++i) {
            repeatingDays += arr[i] + ",";
        }

        return repeatingDays;
    }
}
