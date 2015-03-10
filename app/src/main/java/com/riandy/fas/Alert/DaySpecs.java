package com.riandy.fas.Alert;

import org.joda.time.LocalDate;

import java.util.Calendar;

/**
 * Created by Riandy on 12/1/15.
 */
public class DaySpecs {

    public static final String TAG_STARTDATE = "startDate";
    public static final String TAG_ENDDATE = "endDate";

    public static final int SUNDAY = 0;
    public static final int MONDAY = 1;
    public static final int TUESDAY = 2;
    public static final int WEDNESDAY = 3;
    public static final int THURSDAY = 4;
    public static final int FRIDAY = 5;
    public static final int SATURDAY = 6;

    public enum DayTypes {DATERANGE, DATEONLY, UNLIMITED};
    public enum Frequency {EVERYDAY, WEEKDAY, WEEKEND, CUSTOM};
    
    private DayTypes type;
    private LocalDate startDate,endDate;
    private boolean dayOfWeek[];
    private boolean repeatWeekly;

    DaySpecs(){
        startDate = new LocalDate();
        endDate = new LocalDate();
        dayOfWeek = new boolean[7];
        type = DayTypes.DATEONLY;
    }

    public void setDayType(DayTypes type){
        this.type = type;
    }

    public DayTypes getDayType(){
        return type;
    }

    //this method is used to set date only alert (one-off)
    public void setDate(LocalDate date){
        startDate = date;
        endDate = date;
    }

    //this method is used to set date range alert
    public void setDateRange(LocalDate startDate, LocalDate endDate){
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public void setDayFrequency(Frequency frequency, boolean[] days, int everyNDays){
        //everyday
        //weekday
        //weekend
        //custom (accept array of booleans) --> (sun-sat)
        if (frequency == Frequency.EVERYDAY){
            for (int day = SUNDAY; day <= SATURDAY; day++){
                dayOfWeek[day] = true;
            }
        } else if (frequency == Frequency.WEEKDAY){
            for (int day = MONDAY; day <= FRIDAY; day++){
                dayOfWeek[day] = true;
            }
        } else if (frequency == Frequency.WEEKEND){
            for (int day = SATURDAY; day <= SUNDAY; day++){
                dayOfWeek[day] = true;
            }
        } else {
            //custom type
            if (days != null){
                for (int day = SUNDAY; day <= SATURDAY; day++) {
                    dayOfWeek[day] = days[day];
                }
            } else{
                // TODO: everyNDays
                // get the day of the week (1 = Monday, 2 = Tuesday)
                Calendar c = Calendar.getInstance();
                int today = c.get(Calendar.DAY_OF_WEEK);
                dayOfWeek[today + everyNDays - 1] = true;
            }
        }
    }


    public boolean[] getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(boolean[] dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public boolean isRepeatWeekly() {
        return repeatWeekly;
    }

    public void setRepeatWeekly(boolean status){
        repeatWeekly = status;
    }

}
