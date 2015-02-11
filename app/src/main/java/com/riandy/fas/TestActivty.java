package com.riandy.fas;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.riandy.fas.Alert.AlertContract;
import com.riandy.fas.Alert.AlertDBHelper;
import com.riandy.fas.Alert.AlertModel;


public class TestActivty extends ActionBarActivity {

    AlertModel alert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_activty);
        Log.d("YEAH","I am being called");

        Bundle bundle = getIntent().getExtras();
        Log.d("Output",bundle.getString(AlertContract.Alert.COLUMN_NAME_ALERT_DESCRIPTION));
        Log.d("Output","ID="+bundle.getInt("id"));

        AlertDBHelper dbHelper = new AlertDBHelper(getApplicationContext());
        alert  = dbHelper.getAlert(bundle.getInt("id"));
        alert.getAlertFeature().setContext(getApplicationContext());
        alert.getAlertFeature().launchVoiceInstruction();
        alert.getAlertFeature().launchSound();

    }

    @Override
    protected void onPause() {
        super.onPause();
        alert.getAlertFeature().stopAlerts();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_test_activty, menu);
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
}
