package com.riandy.fas;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import junit.framework.Assert;

import org.joda.time.LocalDate;
import org.joda.time.LocalTime;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        testAlert();
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

    public void testAlert(){
        AlertModel model = new AlertModel();

        model.setEnabled(true);

        AlertFeature alertFeature = new AlertFeature();
        AlertSpecs alertSpecs = new AlertSpecs();

        alertFeature.setVibrationEnabled(true);
        alertFeature.setVoiceInstructionStatusEnabled(false);
        alertFeature.setSoundEnabled(true);
        alertFeature.setLaunchAppEnabled(false);
        alertFeature.setNotificationEnabled(true);
        alertFeature.setTone("testTone.mp3");
        alertFeature.setDescription("This is a test alert");
        alertFeature.setAppToLaunch("Whatsapp");

        alertSpecs.getDaySpecs().setDayType(DaySpecs.DayTypes.DATERANGE);
        alertSpecs.getDaySpecs().setDate(new LocalDate());
        alertSpecs.getDaySpecs().setDayOfWeek(new boolean[]{false,false,true,true,true,false,false});
        alertSpecs.getDaySpecs().setRepeatWeekly(true);

        alertSpecs.getHourSpecs().setHourType(HourSpecs.HourTypes.EXACTTIME);
        alertSpecs.getHourSpecs().setTimeRange(new LocalTime(),new LocalTime(),5);
        alertSpecs.getHourSpecs().setNumOfTimes(10);

        model.setAlertFeature(alertFeature);
        model.setAlertSpecs(alertSpecs);

        Log.d("SQL","HELLO");
        AlertDBHelper db = new AlertDBHelper(getApplicationContext());
        Log.d("SQL",db.getDatabaseName());

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
