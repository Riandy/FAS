package com.riandy.fas.Alert;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.riandy.fas.MainActivity;

/**
 * Created by Riandy on 4/2/15.
 */
public class AlertService extends Service {

    public static String TAG = AlertService.class.getSimpleName();

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Intent alarmIntent = new Intent(getBaseContext(), MainActivity.class);
        alarmIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if(intent!=null) {
            alarmIntent.putExtras(intent);
            Log.d("READING", "" + intent.getExtras().getInt("id"));
            getApplication().startActivity(alarmIntent);
            Log.d("riandy", "setting alert");
            AlertDBHelper dbHelper = new AlertDBHelper(this);
            AlertModel model = dbHelper.getAlert(intent.getExtras().getInt("id"));
            AlertManagerHelper.setAlert(this, model);
        }
        //AlertManagerHelper.setAlerts(this);

        return super.onStartCommand(intent, flags, startId);
    }

}

