package com.riandy.fas.Alert;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import com.riandy.fas.Alert.AlertContract.Alert;

import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

import java.util.Calendar;
import java.util.List;
import java.util.Random;

/**
 * Created by Riandy on 22/1/15.
 */
public class AlertManagerHelper extends BroadcastReceiver {

    public static final String ID = "id";
    public static final String NAME = "name";
    public static final String TIME_HOUR = "timeHour";
    public static final String TIME_MINUTE = "timeMinute";
    public static final String TONE = "alarmTone";
    public static boolean counterReset = false;
    public static boolean isNextDay = false;

    static LocalTime localTime;
    static LocalDate localDate;

    @Override
    public void onReceive(Context context, Intent intent) {
        setAlerts(context);
    }

    public static void setAlert(Context context,AlertModel alert){

        if(alert==null) {
            Log.d("riandy ","ALERT NULL");
            return;
        }
        cancelAlert(context, alert);

        Calendar calendar = Calendar.getInstance();

        if(alert.isEnabled()) {
            //Log.d("Alert", "alert enabled");
            counterReset = false;
            isNextDay = false;
            Log.d("riandy ",alert.toString());
            localTime = getValidTime(alert.getAlertSpecs().getDaySpecs(),alert.getAlertSpecs().getHourSpecs());
            localDate = getValidDate(alert.getAlertSpecs().getDaySpecs(),alert.getAlertSpecs().getHourSpecs());

            if(localDate == null || localTime == null) {
                Log.d("riandy " , " alarm expired "+localDate + " " +localTime);
                return;
            }
            calendar.set(localDate.getYear(), localDate.getMonthOfYear()-1, localDate.getDayOfMonth(),
                    localTime.getHourOfDay(), localTime.getMinuteOfHour(), localTime.getSecondOfMinute());
            if(calendar.before(Calendar.getInstance())) { // date has passed. just continue;
                Log.d("riandy Alarm expired",calendar.getTime().toString()+" "+Calendar.getInstance().getTime().toString());
                return;
            }
            AlertDBHelper.getInstance(context).updateAlert(alert);
            PendingIntent pIntent = createPendingIntent(context, alert);
            setAlert(context,calendar,pIntent);
        }else{
            Log.d("riandy Alert "+alert.id, "alert not enabled");
        }
    }

    public static void setAlerts(Context context) {
        Log.d("riandy","setting all alerts");

        cancelAlerts(context);

        AlertDBHelper dbHelper = AlertDBHelper.getInstance(context);
        List<AlertModel> alerts =  dbHelper.getAlerts();

        if(alerts==null)
            return;

        for (AlertModel alert : alerts) {
            setAlert(context,alert);
        }

    }

    public static LocalDate getValidDate(DaySpecs daySpecs, HourSpecs hourSpecs){
        //check if its between today is between the startDate and endDate
        LocalDate startDate,endDate,today;
        today = new LocalDate();
        startDate = daySpecs.getStartDate();
        endDate = daySpecs.getEndDate();

        LocalTime lastAlertTime = hourSpecs.getStartTime();
        boolean startTimeIsAfterNow = lastAlertTime.isAfter(new LocalTime());
        boolean nowIsBeforeEndTime = new LocalTime().isBefore(hourSpecs.getEndTime());
        Log.d("riandy nowIsBeforeETime",""+nowIsBeforeEndTime);
        boolean[] dayOfWeek = daySpecs.getDayOfWeek();

        if(daySpecs.getDayType() == DaySpecs.DayTypes.UNLIMITED){ //unlimited event
            Log.d("riandy","alert type = unlimited "+startTimeIsAfterNow);
            if(daySpecs.getEveryNDays()==0){
                //look from dayOfWeek (1..7)
                int day = today.getDayOfWeek();
                for (int i = 0; i < 7 ; i++) {
                    if(dayOfWeek[(day+i)%7] == true && (startTimeIsAfterNow || nowIsBeforeEndTime) && !isNextDay)
                        return today.plusDays(i);
                    else {
                        startTimeIsAfterNow = true;
                        isNextDay = false;
                    }
                }
            }else{
                if(localTime!=null && localTime.isAfter(new LocalTime())){
                    return today;
                }else {
                    return today.plusDays(daySpecs.getEveryNDays());
                }
            }
        } else if(daySpecs.getDayType() == DaySpecs.DayTypes.DATEONLY || daySpecs.getStartDate().isEqual(daySpecs.getEndDate())){ //one off event && exact date
            return startDate;

        } else if((today.isAfter(startDate) || today.isEqual(startDate)) &&
                (today.isBefore(endDate) || today.isEqual(endDate))){ //within range of startDate and endDate
            //find the nearest available date
            Log.d("riandy ","within startDate and endDate");
            if(daySpecs.getEveryNDays()==0) {
                int i, day = today.getDayOfWeek();
                for (i = 0; i < 7; i++) {
                    if (dayOfWeek[(day + i) % 7] == true && (startTimeIsAfterNow || nowIsBeforeEndTime) && !counterReset && !isNextDay)
                        break;
                    else {
                        startTimeIsAfterNow = true;
                        counterReset = false;
                        isNextDay = false;
                    }
                }
                today = today.plusDays(i);
            }else{
                if(localTime!=null && localTime.isAfter(new LocalTime())) {

                }else {
                    today = today.plusDays(daySpecs.getEveryNDays());
                }
            }
            //check if its within date range
            if((today.isAfter(startDate) || today.isEqual(startDate)) &&
                    (today.isBefore(endDate) || today.isEqual(endDate))) {
                return today;
            }
        } else if (startDate.isAfter(today)){
            return startDate;
        }
        Log.e("ERROR getValidDate", "return null value");
        return null;
    }

    public static LocalTime getValidTime(DaySpecs daySpecs, HourSpecs hourSpecs){
        LocalTime startTime,endTime,today;
        today = new LocalTime();

        if(daySpecs.getStartDate().isAfter(new LocalDate())){
            today = new LocalTime(0,0,0);
        }
        startTime = hourSpecs.getStartTime();
        endTime = hourSpecs.getEndTime();
        Log.d("riandy getVaidTime","ok");

        if(hourSpecs.getHourType() == HourSpecs.HourTypes.EXACTTIME){
            Log.d("riandy exacttime","ok");
            today = startTime;
        }else if(hourSpecs.getHourType() == HourSpecs.HourTypes.RANDOM){
            Log.d("riandy ","Random");
            int currentCounter = hourSpecs.getCurrentCounter();
            int numOfTimes = hourSpecs.getNumOfTimes();
            if (numOfTimes >  currentCounter){
                Random r = new Random();
                if(hourSpecs.getLastAlertTime().isBefore(startTime)){
                    if(startTime.isAfter(today)){
                        //lastAlertTime is start time
                        hourSpecs.setLastAlertTime(startTime);
                    }else{
                        //lastAlertTime is today
                        hourSpecs.setLastAlertTime(today);
                    }
                }

                Log.d("riandy ",endTime.toString() +" "+ hourSpecs.getLastAlertTime().toString());
                Log.d("riandy ",hourSpecs.getLastAlertTime().toString());
                int time = r.nextInt(endTime.getMillisOfDay()-hourSpecs.getLastAlertTime().getMillisOfDay());
                today = hourSpecs.getLastAlertTime().plusMillis(time);
                hourSpecs.setLastAlertTime(today);
                hourSpecs.setCurrentCounter(++currentCounter);
            }else{
                Log.d("riandy numOfTimes ","over");
                //reset startTime and counter
                hourSpecs.setLastAlertTime(hourSpecs.getStartTime());
                hourSpecs.setCurrentCounter(1);
                Random r = new Random();
                int time = r.nextInt(endTime.getMillisOfDay()-hourSpecs.getStartTime().getMillisOfDay());
                today = hourSpecs.getLastAlertTime().plusMillis(time);
                counterReset = true;
            }
        }else if(hourSpecs.getHourType() == HourSpecs.HourTypes.TIMERANGE){
            Log.d("riandy timerange","ok");
            LocalTime lastAlertTime = new LocalTime(hourSpecs.getLastAlertTime());
            int intervalInMinutes = hourSpecs.getIntervalInHour() * 60;
            Log.d("riandy lastAlertTime",hourSpecs.getLastAlertTime().toString()+" interval = " +intervalInMinutes);

            //today time is before start time
            if(today.isBefore(hourSpecs.getStartTime())){
                Log.d("riandy ","is before start time");
                today = hourSpecs.getStartTime();
                hourSpecs.setLastAlertTime(today);
            }else if( (lastAlertTime.isAfter(hourSpecs.getStartTime()) || lastAlertTime.isEqual(hourSpecs.getStartTime()))
                    && lastAlertTime.isBefore(hourSpecs.getEndTime())){
                //last alert time between start time and end time
                Log.d("riandy ", "is between start time and end time");
                if(hourSpecs.getLastAlertTime().plusMinutes(intervalInMinutes).isAfter(hourSpecs.getEndTime())){
                    Log.d("riandy ", "THE NEXT DAY");
                    isNextDay = true;
                    hourSpecs.setLastAlertTime(hourSpecs.getStartTime());
                }else{
                    hourSpecs.setLastAlertTime(hourSpecs.getLastAlertTime().plusMinutes(intervalInMinutes));
                }
                today = hourSpecs.getLastAlertTime();
                Log.d("riandy today",today.toString());
            }else if( lastAlertTime.isBefore(endTime) && today.isAfter(startTime)){
                Log.d("riandy ","alert set after start time");
                int minutesDiff = ( endTime.getMillisOfDay() - startTime.getMillisOfDay() ) / 1000 / 60 ;
                Log.d("riandy ","hourDiff "+ minutesDiff/60 );
                LocalTime temp = new LocalTime(startTime);
                for(int i = 0; i <= minutesDiff; i+=intervalInMinutes){
                    temp = temp.plusMinutes(intervalInMinutes);
                    Log.d("riandy ","i="+i+" "+temp.toString());
                    if(temp.isAfter(today)) {
                        hourSpecs.setLastAlertTime(temp);
                        today = hourSpecs.getLastAlertTime();
                        Log.d("riandy ","time set = "+ today.toString());
                        break;
                    }
                }
            }else{
                //reset the lastAlertTime into startTime
                Log.d("riandy ","else case "+lastAlertTime.isAfter(hourSpecs.getStartTime())+" "+lastAlertTime.isEqual(hourSpecs.getStartTime())+" "
                + lastAlertTime.isBefore(hourSpecs.getEndTime()));
                hourSpecs.setLastAlertTime(hourSpecs.getStartTime());
                today = hourSpecs.getLastAlertTime();
            }
        }

        if(today.isBefore(hourSpecs.getEndTime())||today.isEqual(hourSpecs.getEndTime()))
            return today;
        else
            return hourSpecs.getStartTime();
    }

    public static void cancelAlerts(Context context) {
        Log.d("Alert", "all alarms cancelled");
        AlertDBHelper dbHelper = AlertDBHelper.getInstance(context);

        List<AlertModel> alerts =  dbHelper.getAlerts();

        if (alerts != null) {
            for (AlertModel alert : alerts) {
                if (alert.isEnabled()) {
                    PendingIntent pIntent = createPendingIntent(context, alert);
                    AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                    alarmManager.cancel(pIntent);
                }
            }
        }
    }

    public static void cancelAlert(Context context,AlertModel alert){
        if(alert.isEnabled()){
            PendingIntent pIntent = createPendingIntent(context, alert);
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            alarmManager.cancel(pIntent);
        }
    }

    private static PendingIntent createPendingIntent(Context context, AlertModel model) {
        Intent intent = new Intent(context, AlertService.class);

        Bundle bundle = new Bundle();
        bundle.putString(Alert.COLUMN_NAME_ALERT_DESCRIPTION, model.getAlertFeature().getDescription());
        bundle.putInt(ID, (int) model.id);
        Log.d("READING","model id "+model.id);
        bundle.putString(Constant.FRAGMENT_TAG,Constant.FRAGMENT_ALERT_SCREEN);
        intent.putExtras(bundle);

        /*intent.putExtra(NAME, model.getAlertFeature().getName());
        intent.putExtra(TIME_HOUR, model.getAlertSpecs().getHourSpecs().getStartTime().getHourOfDay());
        intent.putExtra(TIME_MINUTE, model.timeMinute);
        intent.putExtra(TONE, model.alarmTone.toString());
        */
        return PendingIntent.getService(context, (int) model.id, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    @SuppressLint("NewApi")
    private static void setAlert(Context context, Calendar calendar, PendingIntent pIntent) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pIntent);
        } else {
            alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pIntent);
        }
        Log.d("riandy You set alarm ",calendar.getTime().toString());
    }
}
