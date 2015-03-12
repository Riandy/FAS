package com.riandy.fas.Alert;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Riandy on 1/1/15.
 */
public class AlertSpecs implements Parcelable{

    public static final String TAG_ALERT_SPECS = "alertSpecs";
    public static final String TAG_DAY_SPECS = "daySpecs";
    public static final String TAG_HOUR_SPECS = "hourSpecs";

    private DaySpecs daySpecs;
    private HourSpecs hourSpecs;

    public AlertSpecs(){
        daySpecs = new DaySpecs();
        hourSpecs = new HourSpecs();
    }

    AlertSpecs(DaySpecs daySpecs, HourSpecs hourSpecs){
        this.daySpecs = daySpecs;
        this.hourSpecs = hourSpecs;
    }

    public DaySpecs getDaySpecs() {
        return daySpecs;
    }

    public void setDaySpecs(DaySpecs daySpecs) {
        this.daySpecs = daySpecs;
    }

    public HourSpecs getHourSpecs() {
        return hourSpecs;
    }

    public void setHourSpecs(HourSpecs hourSpecs) {
        this.hourSpecs = hourSpecs;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        //daySpecs
        dest.writeInt(daySpecs.getDayType().ordinal());
        dest.writeString(daySpecs.getStartDate().toString());
        dest.writeString(daySpecs.getEndDate().toString());
        dest.writeBooleanArray(daySpecs.getDayOfWeek());
        dest.writeInt(daySpecs.isRepeatWeekly() ? 1 : 0);
        dest.writeInt(daySpecs.getEveryNDays());
        //hourSpecs
        dest.writeInt(hourSpecs.getHourType().ordinal());
        dest.writeString(hourSpecs.getStartTime().toString());
        dest.writeString(hourSpecs.getEndTime().toString());
        dest.writeString(hourSpecs.getLastAlertTime().toString());
        dest.writeInt(hourSpecs.getIntervalInHour());
        dest.writeInt(hourSpecs.getNumOfTimes());
        dest.writeInt(hourSpecs.getCurrentCounter());
    }
}
