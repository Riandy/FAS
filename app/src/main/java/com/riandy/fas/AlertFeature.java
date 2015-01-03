package com.riandy.fas;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
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
    MediaPlayer mPlayer;
    String tone;

    AlertFeature(Context context){
        this.context = context;

        vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        mPlayer = new MediaPlayer();

        instructionToReadWhenAlertSounds = "";
        isVibrationEnabled = true;
        isSoundEnabled = true;
        isLaunchAppEnabled = false;
        isVoiceInstructionStatusEnabled = false;
    }

    // used to launch all type of alerts enabled
    public void launchAlerts(){
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

    public void stopAlerts(){
        mPlayer.pause();
        mPlayer.stop();
        vibrator.cancel();
    }

    public void launchVibration(){
        //TODO : make the pattern customizable and also repeat or no repeat
        long[] vibratePattern = {200,500};
        vibrator.vibrate(vibratePattern, 0);
    }

    public void launchVoiceInstruction(){

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

    }


    // all the data required for the alert feature are stored here

    // vibration status

}
