package com.riandy.fas.Alert;

import android.provider.BaseColumns;

/**
 * Created by Riandy on 14/1/15.
 */
public class AlertContract {

    public AlertContract() {}

    public static abstract class Alert implements BaseColumns {
        public static final String TABLE_NAME = "alert";

        public static final String COLUMN_NAME_ALERT_NAME = "name";
        public static final String COLUMN_NAME_ALERT_ENABLED = "enabled";
        // columns for AlertModel Feature
        public static final String COLUMN_NAME_ALERT_IS_VIBRATION_ENABLED = "isVibrationEnabled";
        public static final String COLUMN_NAME_ALERT_IS_VOICE_INSTRUCTION_ENABLED = "isVoiceInstructionEnabled";
        public static final String COLUMN_NAME_ALERT_IS_SOUND_ENABLED = "isSoundEnabled";
        public static final String COLUMN_NAME_ALERT_IS_LAUNCH_APP_ENABLED = "isLaunchAppEnabled";
        public static final String COLUMN_NAME_ALERT_IS_NOTIFICATION_ENABLED = "isNotificationEnabled";
        public static final String COLUMN_NAME_ALERT_TONE = "tone";
        public static final String COLUMN_NAME_ALERT_DESCRIPTION = "description";
        public static final String COLUMN_NAME_ALERT_APP_TO_LAUNCH = "appToLaunch";

        // columns for AlertModel Specs
        public static final String COLUMN_NAME_ALERT_DAY_TYPES = "dayTypes";
        public static final String COLUMN_NAME_ALERT_STARTDATE = "startDate";
        public static final String COLUMN_NAME_ALERT_ENDDATE = "endDate";
        public static final String COLUMN_NAME_ALERT_DAYOFWEEK = "dayOfWeek";
        public static final String COLUMN_NAME_ALERT_REPEAT_WEEKLY = "repeatWeekly";
        public static final String COLUMN_NAME_ALERT_HOUR_TYPES = "hourTypes";
        public static final String COLUMN_NAME_ALERT_STARTTIME = "startTime";
        public static final String COLUMN_NAME_ALERT_ENDTIME = "endTime";
        public static final String COLUMN_NAME_ALERT_INTERVAL_HOUR = "intervalHour";
        public static final String COLUMN_NAME_ALERT_NUM_OF_TIMES = "numOfTimes";
    }

}
