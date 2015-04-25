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
    public long syncId; //used for googleCal

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
        dest.writeLong(syncId);
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

    @Override
    public String toString(){

        StringBuilder sb = new StringBuilder();

        sb.append("id="+id);
        sb.append(" syncId="+syncId);
        sb.append(" name="+alertFeature.getName());
        sb.append(" desc="+alertFeature.getDescription());
        sb.append(" appToLaunch="+alertFeature.getAppToLaunch());
        sb.append(" tone="+alertFeature.getTone());
        sb.append(" Notif,sound,app,vibrate,voice="+alertFeature.isNotificationEnabled()+alertFeature.isSoundEnabled()+alertFeature.isLaunchAppEnabled()
            +alertFeature.isVibrationEnabled()+alertFeature.isVoiceInstructionStatusEnabled());
        sb.append(" dayType="+alertSpecs.getDaySpecs().getDayType().ordinal());
        sb.append(" dayOfWeek="+alertSpecs.getDaySpecs().getDayOfWeek().toString());
        sb.append(" startDate="+alertSpecs.getDaySpecs().getStartDate().toString());
        sb.append(" endDate="+alertSpecs.getDaySpecs().getEndDate());
        sb.append(" everyNDays="+alertSpecs.getDaySpecs().getEveryNDays());
        sb.append(" isRepeatWeekly"+alertSpecs.getDaySpecs().isRepeatWeekly());
        sb.append(" hourType="+alertSpecs.getHourSpecs().getHourType().ordinal());
        sb.append(" startTime"+alertSpecs.getHourSpecs().getStartTime().toString());
        sb.append(" endTime="+alertSpecs.getHourSpecs().getEndTime().toString());
        sb.append(" currentCounter="+alertSpecs.getHourSpecs().getCurrentCounter());
        sb.append(" intervalInHour="+alertSpecs.getHourSpecs().getIntervalInHour());
        sb.append(" lastAlertTime"+alertSpecs.getHourSpecs().getLastAlertTime().toString());
        sb.append(" numOfTimes"+alertSpecs.getHourSpecs().getNumOfTimes());
        return sb.toString();
    }
}
