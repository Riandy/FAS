package com.riandy.fas.Alert;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Riandy on 1/1/15.
 */
public class AlertModel implements Parcelable{

    //AlertModel has alert feature and alert specs
    //AlertModel feature controls what happens when the alarm goes of (Vibration, Sound, Launch App, etc).
    //AlertModel specs controls the frequency of the alarm (daily, weekly, etc).

    public static final String TAG_ALERT_MODEL = "alertModel";

    private AlertFeature alertFeature;
    private AlertSpecs alertSpecs;

    private boolean isEnabled;
    public long id;

    public AlertModel() {
        alertFeature = new AlertFeature();
        alertSpecs = new AlertSpecs();
        isEnabled = true;
    }

    public AlertModel(AlertFeature alertFeature, AlertSpecs alertSpecs, boolean isEnabled){
        this.alertFeature = alertFeature;
        this.alertSpecs = alertSpecs;
        this.isEnabled = isEnabled;
    }

    public AlertFeature getAlertFeature() {
        return alertFeature;
    }

    public void setAlertFeature(AlertFeature alertFeature) {
        this.alertFeature = alertFeature;
    }

    public AlertSpecs getAlertSpecs() {
        return alertSpecs;
    }

    public void setAlertSpecs(AlertSpecs alertSpecs) {
        this.alertSpecs = alertSpecs;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean isEnabled) {
        this.isEnabled = isEnabled;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(isEnabled ? 1 : 0);
        dest.writeLong(id);

        //AlertSpecs
        //daySpecs
        dest.writeInt(alertSpecs.getDaySpecs().getDayType().ordinal());
        dest.writeString(alertSpecs.getDaySpecs().getStartDate().toString());
        dest.writeString(alertSpecs.getDaySpecs().getEndDate().toString());
        dest.writeBooleanArray(alertSpecs.getDaySpecs().getDayOfWeek());
        dest.writeInt(alertSpecs.getDaySpecs().isRepeatWeekly() ? 1 : 0);
        dest.writeInt(alertSpecs.getDaySpecs().getEveryNDays());

        //hourSpecs
        dest.writeInt(alertSpecs.getHourSpecs().getHourType().ordinal());
        dest.writeString(alertSpecs.getHourSpecs().getStartTime().toString());
        dest.writeString(alertSpecs.getHourSpecs().getEndTime().toString());
        dest.writeString(alertSpecs.getHourSpecs().getLastAlertTime().toString());
        dest.writeInt(alertSpecs.getHourSpecs().getIntervalInHour());
        dest.writeInt(alertSpecs.getHourSpecs().getNumOfTimes());
        dest.writeInt(alertSpecs.getHourSpecs().getCurrentCounter());

        //AlertFeature
        dest.writeString(alertFeature.getName());
        dest.writeString(alertFeature.getDescription());
        dest.writeInt(alertFeature.isVibrationEnabled() ? 1 : 0);
        dest.writeInt(alertFeature.isVoiceInstructionStatusEnabled() ? 1 : 0);
        dest.writeInt(alertFeature.isSoundEnabled() ? 1 : 0);
        dest.writeInt(alertFeature.isLaunchAppEnabled() ? 1 : 0);
        dest.writeInt(alertFeature.isNotificationEnabled() ? 1 : 0);
        dest.writeString(alertFeature.getTone());
        dest.writeString(alertFeature.getAppToLaunch());

    }

//    public AlertModel(Parcel in){
//        alertFeature = new AlertFeature();
//        alertSpecs = new AlertSpecs();
//
//        isEnabled = in.readInt() == 1;
//        id = in.readLong();
//
//        //alertSpecs
//        //daySpecs
//        alertSpecs.getDaySpecs().setDayType(DaySpecs.DayTypes.values()[in.readInt()]);
//        alertSpecs.getDaySpecs().setDateRange(LocalDate.parse(in.readString()),LocalDate.parse(in.readString()));
//        boolean[] dayOfWeek = new boolean[7];
//        in.readBooleanArray(dayOfWeek);
//        alertSpecs.getDaySpecs().setDayOfWeek(dayOfWeek);
//        alertSpecs.getDaySpecs().setRepeatWeekly(in.readInt() == 1);
//
//        //hourSpecs
//        alertSpecs.getHourSpecs().setHourType(HourSpecs.HourTypes.values()[in.readInt()]);
//        alertSpecs.getHourSpecs().setTimeRangeWithoutInterval(LocalTime.parse(in.readString()),LocalTime.parse(in.readString()));
//        alertSpecs.getHourSpecs().setLastAlertTime(LocalTime.parse(in.readString()));
//        alertSpecs.getHourSpecs().setIntervalInHour(in.readInt());
//        alertSpecs.getHourSpecs().setNumOfTimes(in.readInt());
//        alertSpecs.getHourSpecs().setCurrentCounter(in.readInt());
//
//
//        //AlertFeature
//        alertFeature.setName(in.readString());
//        alertFeature.setName(in.readString());
//        alertFeature.setVibrationEnabled(in.readInt() == 1);
//        alertFeature.setVoiceInstructionStatusEnabled(in.readInt() == 1);
//        alertFeature.setSoundEnabled(in.readInt() == 1);
//        alertFeature.setLaunchAppEnabled(in.readInt() == 1);
//        alertFeature.setNotificationEnabled(in.readInt() == 1);
//        alertFeature.setTone(in.readString());
//        alertFeature.setAppToLaunch(in.readString());
//    }

//    private AlertModel readFromParcel(Parcel in){
//        return new AlertModel(in);
//    }
}
