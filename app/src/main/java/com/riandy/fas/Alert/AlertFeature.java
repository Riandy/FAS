package com.riandy.fas.Alert;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Vibrator;
import android.speech.tts.TextToSpeech;

import java.util.Locale;

/**
 * Created by Riandy on 1/1/15.
 */

/*
    Remember to add vibrate permission:
    <uses-permission android:name="android.permission.VIBRATE"/>
 */
public class AlertFeature {

    private Context context;
    private Vibrator vibrator;
    private String name;
    private String description;
    private boolean isVibrationEnabled, isVoiceInstructionStatusEnabled, isSoundEnabled, isLaunchAppEnabled, isNotificationEnabled;
    private MediaPlayer mPlayer;
    private String tone;
    private String appToLaunch;
    private TextToSpeech textToSpeechObj;
    private final String TTS_ID = "TextToSpeech id=1";

    public AlertFeature(){

        description = "";
        isVibrationEnabled = true;
        isSoundEnabled = true;
        isLaunchAppEnabled = false;
        isVoiceInstructionStatusEnabled = false;
        isNotificationEnabled = true;
    }

    // used to launch all type of alerts enabled
    public void launchAlerts(Context context){

        this.context = context;

        vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        mPlayer = new MediaPlayer();


        if (isVibrationEnabled){
            launchVibration();
        }
        if (isVoiceInstructionStatusEnabled){
            launchVoiceInstruction();
        }
        if (isSoundEnabled){
            launchSound();
        }
        /*
        if(isLaunchAppEnabled){
            launchApp();
        }
        */
        if(isNotificationEnabled){
            launchNotification();
        }
    }

    public void stopAlerts(){

        if (isVibrationEnabled && vibrator != null)
            vibrator.cancel();

        if (isSoundEnabled && mPlayer != null) {
            mPlayer.pause();
            mPlayer.stop();
        }
    }

    public void launchVibration(){
        //TODO : make the pattern customizable and also repeat or no repeat
        long[] vibratePattern = {200,500};
        vibrator.vibrate(vibratePattern, 0);
    }

    public void launchVoiceInstruction(){
        textToSpeechObj = new TextToSpeech(context, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                textToSpeechObj.setLanguage(Locale.UK);
                textToSpeechObj.speak(description,TextToSpeech.QUEUE_FLUSH,null);
            }
        }
        );
    }

    public void launchSound(){
        //Play alarm tone
        mPlayer = new MediaPlayer();
        try {
            if (tone != null && !tone.equals("")) {
                Uri toneUri = Uri.parse(tone);
                if (toneUri != null) {
                    mPlayer.setDataSource(context, toneUri);
                    mPlayer.setAudioStreamType(AudioManager.STREAM_ALARM);
                    mPlayer.setLooping(true);
                    mPlayer.prepare();
                    mPlayer.start();
                }
            }else{
                AssetFileDescriptor descriptor = context.getAssets().openFd("elegant_ringtone.mp3");
                mPlayer.setDataSource(descriptor.getFileDescriptor(),descriptor.getStartOffset(),descriptor.getLength());
                mPlayer.setLooping(true);
                mPlayer.prepare();
                mPlayer.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void launchApp(){
        Intent i;
        PackageManager manager = context.getPackageManager();
        try {
            i = manager.getLaunchIntentForPackage(appToLaunch);
            if (i == null)
                throw new PackageManager.NameNotFoundException();
            i.addCategory(Intent.CATEGORY_LAUNCHER);
            context.startActivity(i);
        } catch (PackageManager.NameNotFoundException e) {

        }
    }

    public void launchNotification(){
        Notif notif = new Notif(context);
        notif.set_title(name);
        notif.set_content(description);
        notif.setAppToRun(appToLaunch);
        PInfo pInfo = new PInfo(context);
        pInfo.getPackages();
        notif.setNotification();

    }
    // all the data required for the alert feature are stored here

    // vibration status

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public Vibrator getVibrator() {
        return vibrator;
    }

    public void setVibrator(Vibrator vibrator) {
        this.vibrator = vibrator;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isVibrationEnabled() {
        return isVibrationEnabled;
    }

    public void setVibrationEnabled(boolean isVibrationEnabled) {
        this.isVibrationEnabled = isVibrationEnabled;
    }

    public boolean isVoiceInstructionStatusEnabled() {
        return isVoiceInstructionStatusEnabled;
    }

    public void setVoiceInstructionStatusEnabled(boolean isVoiceInstructionStatusEnabled) {
        this.isVoiceInstructionStatusEnabled = isVoiceInstructionStatusEnabled;
    }

    public boolean isSoundEnabled() {
        return isSoundEnabled;
    }

    public void setSoundEnabled(boolean isSoundEnabled) {
        this.isSoundEnabled = isSoundEnabled;
    }

    public boolean isLaunchAppEnabled() {
        return isLaunchAppEnabled;
    }

    public void setLaunchAppEnabled(boolean isLaunchAppEnabled) {
        this.isLaunchAppEnabled = isLaunchAppEnabled;
    }

    public boolean isNotificationEnabled() {
        return isNotificationEnabled;
    }

    public void setNotificationEnabled(boolean isNotificationEnabled) {
        this.isNotificationEnabled = isNotificationEnabled;
    }

    public String getTone() {
        return tone;
    }

    public void setTone(String tone) {
        this.tone = tone;
    }

    public String getAppToLaunch() {
        return appToLaunch;
    }

    public void setAppToLaunch(String appToLaunch) {
        this.appToLaunch = appToLaunch;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}