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

    @Override
    public void onReceive(Context context, Intent intent) {
        setAlerts(context);
    }

    public static void setAlerts(Context context) {
        cancelAlerts(context);

        AlertDBHelper dbHelper = new AlertDBHelper(context);
        List<AlertModel> alerts =  dbHelper.getAlerts();
        Calendar calendar = Calendar.getInstance();

        for (AlertModel alert : alerts) {

            if(alert.isEnabled()) {
                LocalDate localDate = getValidDate(alert.getAlertSpecs().getDaySpecs());
                LocalTime localTime = getValidTime(alert.getAlertSpecs().getHourSpecs());
                if(localDate == null || localTime == null)
                    continue;
                calendar.set(localDate.getYear(), localDate.getMonthOfYear()-1, localDate.getDayOfMonth(),
                        localTime.getHourOfDay(), localTime.getMinuteOfHour(), localTime.getSecondOfMinute());
                if(calendar.before(Calendar.getInstance())) // date has passed. just continue;
                    continue;
                PendingIntent pIntent = createPendingIntent(context, alert);
                setAlert(context,calendar,pIntent);
            }
        }

    }

    private static LocalDate getValidDate(DaySpecs daySpecs){
        //check if its between today is between the startDate and endDate
        LocalDate startDate,endDate,today;
        today = new LocalDate();
        startDate = daySpecs.getStartDate();
        endDate = daySpecs.getEndDate();

        boolean[] dayOfWeek = daySpecs.getDayOfWeek();

        if(daySpecs.getDayType() == DaySpecs.DayTypes.UNLIMITED){ //unlimited event
            int i, day = today.getDayOfWeek()-1;
            for( i = 0; i < 6; i++ ){
                if(dayOfWeek[(day+i)%7] == true)
                    break;
            }
            today.plusDays(i);
            return today;
        } else if(daySpecs.getDayType() == DaySpecs.DayTypes.DATEONLY){ //one off event
            return startDate;

        } else if((today.isAfter(startDate) || today.isEqual(startDate)) &&
                (today.isBefore(endDate) || today.isEqual(endDate))){ //within range of startDate and endDate
            //find the nearest available date
            int i, day = today.getDayOfWeek()-1;
            for( i = 0; i < 6; i++ ){
                if(dayOfWeek[(day+i)%7] == true)
                    break;
            }

            today.plusDays(i);
            //check if its within date range
            if((today.isAfter(startDate) || today.isEqual(startDate)) &&
                    (today.isBefore(endDate) || today.isEqual(endDate))) {
                return today;
            }
        }
        Log.e("ERROR getValidDate", "return null value");
        return null;
    }

    private static LocalTime getValidTime(HourSpecs hourSpecs){
        LocalTime startTime,endTime,today;
        today = new LocalTime();

        startTime = hourSpecs.getStartTime();
        endTime = hourSpecs.getEndTime();

        if(hourSpecs.getHourType() == HourSpecs.HourTypes.EXACTTIME){
            today = startTime;
        }else if(hourSpecs.getHourType() == HourSpecs.HourTypes.RANDOM){
            // RANDOM, NUM OF TIMES --> need currentCounter
            int currentCounter = hourSpecs.getCurrentCounter();
            int numOfTimes = hourSpecs.getNumOfTimes();
            if (numOfTimes > currentCounter){
                if(today.isBefore(endTime)){
                    Random r = new Random();
                    int time = r.nextInt(endTime.getMillisOfDay()-today.getMillisOfDay());
                    today.plusMillis(time);
                    currentCounter++;
                }
            }
        }else if(hourSpecs.getHourType() == HourSpecs.HourTypes.TIMERANGE){
            // TIME RANGE, INTERVAL --> need lastAlertTime
            int interval = hourSpecs.getIntervalInHour();
            today = hourSpecs.getLastAlertTime();
            today.plusHours(interval);
        }

        return today;
    }

    public static void cancelAlerts(Context context) {
        Log.d("Alert", "all alarms cancelled");
        AlertDBHelper dbHelper = new AlertDBHelper(context);

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

    private static PendingIntent createPendingIntent(Context context, AlertModel model) {
        Intent intent = new Intent(context, AlertService.class);

        Bundle bundle = new Bundle();
        bundle.putString(Alert.COLUMN_NAME_ALERT_DESCRIPTION, model.getAlertFeature().getDescription());
        bundle.putInt(ID,(int)model.id);
        bundle.putString("Fragment","123");
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
        Log.d("You set alarm ",calendar.getTime().toString());
    }
}

/*

	public static void setAlarms(Context context) {
		cancelAlarms(context);

		AlarmDBHelper dbHelper = new AlarmDBHelper(context);

		List<AlarmModel> alarms =  dbHelper.getAlarms();

		for (AlarmModel alarm : alarms) {
			if (alarm.isEnabled) {

				PendingIntent pIntent = createPendingIntent(context, alarm);

				Date startDate = alarm.startDate;
				Date endDate = alarm.endDate;
				//				Log.d("StartDate endDate :",startDate.toString()+" "+endDate.toString());

				Calendar calendar = Calendar.getInstance();
				calendar.set(Calendar.HOUR_OF_DAY, alarm.timeHour);
				calendar.set(Calendar.MINUTE, alarm.timeMinute);
				calendar.set(Calendar.SECOND, 00);
				final int nowDay = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
				final int nowHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
				final int nowMinute = Calendar.getInstance().get(Calendar.MINUTE);

				if(startDate.getTime()==0 && endDate.getTime()==0){ //infinite alarm

					//Find next time to set
					boolean alarmSet = false;

					//First check if it's later in the week & within date range (startDate & endDate)
					for (int dayOfWeek = Calendar.SUNDAY; dayOfWeek <= Calendar.SATURDAY; ++dayOfWeek) {
						if (alarm.getRepeatingDay(dayOfWeek - 1) && dayOfWeek >= nowDay &&
								!(dayOfWeek == nowDay && alarm.timeHour < nowHour) &&
								!(dayOfWeek == nowDay && alarm.timeHour == nowHour && alarm.timeMinute <= nowMinute) &&
								(calendar.compareTo(dateToCalendar(startDate)) > 0) &&
								(calendar.compareTo(dateToCalendar(endDate)) < 0) ) {
							calendar.set(Calendar.DAY_OF_WEEK, dayOfWeek);

							setAlarm(context, calendar, pIntent);
							alarmSet = true;
							Log.d("You set alarm","I SET ALARM!!!");
							break;
						}
					}

					//Else check if it's earlier in the week
					if (!alarmSet) {
						for (int dayOfWeek = Calendar.SUNDAY; dayOfWeek <= Calendar.SATURDAY; ++dayOfWeek) {
							if (alarm.getRepeatingDay(dayOfWeek - 1) && dayOfWeek <= nowDay && alarm.repeatWeekly &&
									(calendar.compareTo(dateToCalendar(startDate)) > 0) &&
									(calendar.compareTo(dateToCalendar(endDate)) < 0) )  {
								calendar.set(Calendar.DAY_OF_WEEK, dayOfWeek);
								calendar.add(Calendar.WEEK_OF_YEAR, 1);

								setAlarm(context, calendar, pIntent);
								alarmSet = true;
								Log.d("You set alarm","I SET ALARM2!!!");
								break;
							}
						}
					}
					//TODO remove this later
					if(alarmSet==false) {
						//						Log.d("SQL","I NOT SET ALARM2!!!");
						//						Log.d("SQL start",Integer.toString(calendar.compareTo(dateToCalendar(startDate))));
						//						Log.d("SQL start",calendar.getTime().toString()+ " " +startDate.toString() + " " + endDate.toString());
						//						Log.d("SQL end",Integer.toString(calendar.compareTo(dateToCalendar(endDate))));

					}
				}

				else if(startDate.getTime()==endDate.getTime()){//one off event
					//if it is later than today, tomorrow onwards.. go ahead schedule it
					Calendar cal = Calendar.getInstance();
					cal.setTime(startDate);
					cal.set(Calendar.HOUR_OF_DAY, alarm.timeHour);
					cal.set(Calendar.MINUTE,alarm.timeMinute);
					cal.set(Calendar.SECOND,00);
					if(cal.after(Calendar.getInstance())){
						setAlarm(context,cal,pIntent);
						//Log.d("You set alarm","one off event found startDate="+startDate.toString()+" currentTime="+calendar.getTime().toString());
						//Log.d("You set alarm","successful in setting alarm");
					}else{
						//Log.d("You set alarm",cal.getTimeInMillis()+" "+calendar.getTimeInMillis());
						//Log.d("You set alarm",cal.getTime().toString()+" "+calendar.getTime().toString());
					}
				}else{//within startDate and endDate
                    Log.d("You","it is infinite case 3");

                    //TODO improve this part later. consider using joda time library
					startDate.setHours(0);
					startDate.setMinutes(0);
					startDate.setSeconds(0);

					if( (calendar.compareTo(dateToCalendar(startDate)) > 0) && //within the date range
							(calendar.compareTo(dateToCalendar(endDate)) < 0) ){

						if(!(alarm.timeHour < nowHour) && //today not yet over
							!(alarm.timeHour == nowHour && alarm.timeMinute <= nowMinute)) {
							//nothing to set. everything ready
							Log.d("You set alarm","today not over yet");
						}else{ //schedule tomorrow
							Log.d("You set alarm","today over");
							calendar.add(Calendar.DATE, 1);
						}
						setAlarm(context,calendar,pIntent);
						Log.d("You set alarm","within start and endDate");
					}else{
                        Log.d("You","it is infinite case 3 not within range");
                    }
				}
			}
		}
	}

	@SuppressLint("NewApi")
	private static void setAlarm(Context context, Calendar calendar, PendingIntent pIntent) {
		AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		//if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
		//alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pIntent);
		//} else {
		alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pIntent);
		//}
		Log.d("You set alarm ",calendar.getTime().toString());
	}

	public static void cancelAlarms(Context context) {
		Log.d("Alarms","all alarms cancelled");
		AlarmDBHelper dbHelper = new AlarmDBHelper(context);

		List<AlarmModel> alarms =  dbHelper.getAlarms();

		if (alarms != null) {
			for (AlarmModel alarm : alarms) {
				if (alarm.isEnabled) {
					PendingIntent pIntent = createPendingIntent(context, alarm);

					AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
					alarmManager.cancel(pIntent);
				}
			}
		}
	}

	private static PendingIntent createPendingIntent(Context context, AlarmModel model) {
		Intent intent = new Intent(context, AlarmService.class);
		intent.putExtra(ID, model.id);
		intent.putExtra(NAME, model.name);
		intent.putExtra(TIME_HOUR, model.timeHour);
		intent.putExtra(TIME_MINUTE, model.timeMinute);
		intent.putExtra(TONE, model.alarmTone.toString());

		return PendingIntent.getService(context, (int) model.id, intent, PendingIntent.FLAG_UPDATE_CURRENT);
	}
}

 */