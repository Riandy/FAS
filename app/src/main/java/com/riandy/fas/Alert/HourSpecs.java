package com.riandy.fas.Alert;

import android.util.Log;

import org.joda.time.LocalTime;

/**
 * Created by Riandy on 12/1/15.
 */
public class HourSpecs {

    public static final String TAG_STARTTIME = "startTime";
    public static final String TAG_ENDTIME = "endTime";
    public static final String TAG_HOURTYPE = "hourType";
    public static final String TAG_INTERVAL_OR_NUMOFTIMES = "intervalOrNumOfTimes";

    public enum HourTypes {EXACTTIME, TIMERANGE, RANDOM};

    private HourTypes type;
    private LocalTime startTime, endTime, lastAlertTime;
    private int intervalInHour;
    private int numOfTimes,currentCounter;


    HourSpecs() {
        startTime = new LocalTime();
        endTime = new LocalTime();
        type = HourTypes.EXACTTIME;
    }

    // Exact time = one timing

    // Time Range = startTime, endTime, interval / numOfTimes

    // Random = startTime, endTime, numOfTimes

    public void setHourType(HourTypes type){
        this.type = type;
    }

    public HourTypes getHourType(){
        return type;
    }
    public void setExactTime(LocalTime time){
        startTime = time;
        endTime = time;
        Log.d("startTime",time.toString());
    }

    public void setTimeRange(LocalTime startTime, LocalTime endTime, int intervalInHour ){
        this.startTime = startTime;
        this.endTime = endTime;
        this.intervalInHour = intervalInHour;
    }

    public void setRandomTime(LocalTime startTime, LocalTime endTime, int numOfTimes){
        this.startTime = startTime;
        this.endTime = endTime;
        this.numOfTimes = numOfTimes;
    }

    public void setTimeRangeWithoutInterval(LocalTime startTime, LocalTime endTime){
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public void setIntervalInHour(int interval) { intervalInHour = interval; }

    public void setNumOfTimes(int num) { numOfTimes = num; }

    public LocalTime getStartTime(){
        return startTime;
    }

    public LocalTime getEndTime(){
        return endTime;
    }

    public int getIntervalInHour(){
        return intervalInHour;
    }

    public int getNumOfTimes(){
        return numOfTimes;
    }

    public LocalTime getLastAlertTime() {
        return lastAlertTime;
    }

    public void setLastAlertTime(LocalTime lastAlertTime) {
        this.lastAlertTime = lastAlertTime;
    }

    public int getCurrentCounter() {
        return currentCounter;
    }

    public void setCurrentCounter(int currentCounter) {
        this.currentCounter = currentCounter;
    }
}
