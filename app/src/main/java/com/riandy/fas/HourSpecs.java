package com.riandy.fas;

import org.joda.time.LocalTime;

/**
 * Created by Riandy on 12/1/15.
 */
public class HourSpecs {

    public enum HourTypes {EXACTTIME, TIMERANGE, RANDOM};

    HourTypes type;
    LocalTime startTime, endTime;
    int intervalInHour;
    int numOfTimes;

    // Exact time = one timing

    // Time Range = startTime, endTime, interval / numOfTimes

    // Random = startTime, endTime, numOfTimes

    HourSpecs(HourTypes type){
        this.type = type;
    }

    public void setExactTime(LocalTime time){
        startTime = time;
        endTime = time;
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

    public HourTypes getHourTypes(){
        return type;
    }

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
}
