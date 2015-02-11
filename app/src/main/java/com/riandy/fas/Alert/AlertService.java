package com.riandy.fas.Alert;

import android.content.Intent;
import android.os.IBinder;
import android.app.Service;

import com.riandy.fas.TestActivty;

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

        Intent alarmIntent = new Intent(getBaseContext(), TestActivty.class);
        alarmIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        alarmIntent.putExtras(intent);
        getApplication().startActivity(alarmIntent);

        AlertManagerHelper.setAlerts(this);

        return super.onStartCommand(intent, flags, startId);
    }

}

