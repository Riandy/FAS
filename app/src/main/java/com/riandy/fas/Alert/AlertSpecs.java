package com.riandy.fas.Alert;

/**
 * Created by Riandy on 1/1/15.
 */
public class AlertSpecs {

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
