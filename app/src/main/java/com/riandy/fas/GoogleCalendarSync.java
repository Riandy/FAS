package com.riandy.fas;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.provider.CalendarContract;
import android.util.Log;

import com.riandy.fas.Alert.AlertDBHelper;
import com.riandy.fas.Alert.AlertModel;

import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

import java.util.Calendar;
import java.util.List;

/**
 * Created by Riandy on 23/3/15.
 */
public class GoogleCalendarSync {

    long calID;

    Context context;
    public GoogleCalendarSync(Context ctx,long calID){
        context = ctx;
        this.calID = calID;
    }
    public long addAlert(AlertModel model) {

        long startMillis;
        long endMillis;
        LocalDate startDate = model.getAlertSpecs().getDaySpecs().getStartDate();
        LocalDate endDate = model.getAlertSpecs().getDaySpecs().getEndDate();

        LocalTime startTime = model.getAlertSpecs().getHourSpecs().getStartTime();
        LocalTime endTime = model.getAlertSpecs().getHourSpecs().getEndTime();

        Calendar begin = Calendar.getInstance();
        begin.set(startDate.getYear(), startDate.getMonthOfYear()-1, startDate.getDayOfMonth(), startTime.getHourOfDay(), startTime.getMinuteOfHour());
        startMillis = begin.getTimeInMillis();
        Calendar end = Calendar.getInstance();
        end.set(endDate.getYear(), endDate.getMonthOfYear()-1, endDate.getDayOfMonth(), endTime.getHourOfDay(), endTime.getMinuteOfHour());
        endMillis = end.getTimeInMillis();

        ContentResolver cr = context.getContentResolver();
        ContentValues values = new ContentValues();
        values.put(CalendarContract.Events.DTSTART, startMillis);
        values.put(CalendarContract.Events.DTEND, endMillis);
        values.put(CalendarContract.Events.TITLE, model.getAlertFeature().getName());
        values.put(CalendarContract.Events.DESCRIPTION, model.getAlertFeature().getDescription());
        values.put(CalendarContract.Events.CALENDAR_ID, calID);
        values.put(CalendarContract.Events.EVENT_TIMEZONE, "Singapore");
        Uri uri = cr.insert(CalendarContract.Events.CONTENT_URI, values);

        // get the event ID that is the last element in the Uri
        long eventID = Long.parseLong(uri.getLastPathSegment());
        Log.d("eventID", "id=" + eventID);
        model.syncId = eventID;
        return eventID;
    }

    public boolean deleteAlert(long id){
        Uri deleteUri;
        deleteUri = ContentUris.withAppendedId(CalendarContract.Events.CONTENT_URI, id);
        int rows = context.getContentResolver().delete(deleteUri, null, null);
        return rows==1;
    }

    public void deleteAllAlerts(){
        List<AlertModel> list =  AlertDBHelper.getInstance(context).getAlerts();
        for(AlertModel model:list){
            deleteAlert(model.syncId);
        }
    }

    public void addAllAlerts(){
        List<AlertModel> list =  AlertDBHelper.getInstance(context).getAlerts();
        for(AlertModel model:list){
            addAlert(model);
            AlertDBHelper.getInstance(context).updateAlert(model);
        }
    }

    public boolean updateAlert(AlertModel model){

        long startMillis;
        long endMillis;
        LocalDate startDate = model.getAlertSpecs().getDaySpecs().getStartDate();
        LocalDate endDate = model.getAlertSpecs().getDaySpecs().getEndDate();

        LocalTime startTime = model.getAlertSpecs().getHourSpecs().getStartTime();
        LocalTime endTime = model.getAlertSpecs().getHourSpecs().getEndTime();

        Calendar begin = Calendar.getInstance();
        begin.set(startDate.getYear(), startDate.getMonthOfYear()-1, startDate.getDayOfMonth(), startTime.getHourOfDay(), startTime.getMinuteOfHour());
        startMillis = begin.getTimeInMillis();
        Calendar end = Calendar.getInstance();
        end.set(endDate.getYear(), endDate.getMonthOfYear()-1, endDate.getDayOfMonth(), endTime.getHourOfDay(), endTime.getMinuteOfHour());
        endMillis = end.getTimeInMillis();

        ContentValues values = new ContentValues();
        Uri updateUri = null;
        // The new title for the event
        values.put(CalendarContract.Events.DTSTART, startMillis);
        values.put(CalendarContract.Events.DTEND, endMillis);
        values.put(CalendarContract.Events.TITLE, model.getAlertFeature().getName());
        values.put(CalendarContract.Events.DESCRIPTION, model.getAlertFeature().getDescription());
        values.put(CalendarContract.Events.CALENDAR_ID, calID);
        values.put(CalendarContract.Events.EVENT_TIMEZONE, "Singapore");
        updateUri = ContentUris.withAppendedId(CalendarContract.Events.CONTENT_URI, model.syncId);
        int rows = context.getContentResolver().update(updateUri, values, null, null);
        return rows==1;
    }

}