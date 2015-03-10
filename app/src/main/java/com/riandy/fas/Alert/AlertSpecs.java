package com.riandy.fas.Alert;

/**
 * Created by Riandy on 1/1/15.
 */
public class AlertSpecs {

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
}
