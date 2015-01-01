package com.riandy.fas;

import android.content.Context;
import android.os.Vibrator;

/**
 * Created by Riandy on 1/1/15.
 */

/*
    Remember to add vibrate permission:
    <uses-permission android:name="android.permission.VIBRATE"/>
 */
public class AlertFeature {

    Context context;
    Vibrator vibrator;
    String instructionToReadWhenAlertSounds;
    boolean isVibrationEnabled, isVoiceInstructionStatusEnabled, isSoundEnabled, isLaunchAppEnabled;

    AlertFeature(Context context){
        this.context = context;
        vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        instructionToReadWhenAlertSounds = "";
        isVibrationEnabled = true;
        isSoundEnabled = true;
        isLaunchAppEnabled = false;
        isVoiceInstructionStatusEnabled = false;
    }

    // used to launch all type of alerts enabled
    public void launchAlert(){
        if (isVibrationEnabled){
            launchVibration();
        }
        if (isVoiceInstructionStatusEnabled){
            launchVoiceInstruction();
        }
        if (isSoundEnabled){
            launchSound();
        }
        if(isLaunchAppEnabled){
            launchApp();
        }
    }

    public void launchVibration(){

    }

    public void launchVoiceInstruction(){

    }

    public void launchSound(){

    }

    public void launchApp(){

    }


    // all the data required for the alert feature are stored here

    // vibration status

}
