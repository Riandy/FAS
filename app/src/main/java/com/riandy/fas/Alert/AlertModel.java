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

    private AlertFeature alertFeature;
    private AlertSpecs alertSpecs;

    private boolean isEnabled;
    long id;

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

    }

    private void readFromParcel(Parcel in){

    }
}
