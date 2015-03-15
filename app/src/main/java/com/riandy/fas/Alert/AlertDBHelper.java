package com.riandy.fas.Alert;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.riandy.fas.Alert.AlertContract.Alert;

import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Riandy on 15/1/15.
 */
public class AlertDBHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 11;
    public static final String DATABASE_NAME = "alertclock.db";

    private static final String SQL_CREATE_ALARM = "CREATE TABLE " + Alert.TABLE_NAME + " (" +
            Alert._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            Alert.COLUMN_NAME_ALERT_NAME + " TEXT," +
            Alert.COLUMN_NAME_ALERT_ENABLED + " BOOLEAN," +
            Alert.COLUMN_NAME_ALERT_IS_VIBRATION_ENABLED + " BOOLEAN," +
            Alert.COLUMN_NAME_ALERT_IS_VOICE_INSTRUCTION_ENABLED + " BOOLEAN," +
            Alert.COLUMN_NAME_ALERT_IS_SOUND_ENABLED + " BOOLEAN," +
            Alert.COLUMN_NAME_ALERT_IS_LAUNCH_APP_ENABLED + " BOOLEAN," +
            Alert.COLUMN_NAME_ALERT_IS_NOTIFICATION_ENABLED + " BOOLEAN," +
            Alert.COLUMN_NAME_ALERT_TONE + " TEXT," +
            Alert.COLUMN_NAME_ALERT_DESCRIPTION + " TEXT," +
            Alert.COLUMN_NAME_ALERT_APP_TO_LAUNCH + " TEXT," +
            //Columns for AlertModel Specs
            Alert.COLUMN_NAME_ALERT_DAY_TYPES + " INTEGER," +
            Alert.COLUMN_NAME_ALERT_STARTDATE + " TEXT," +
            Alert.COLUMN_NAME_ALERT_ENDDATE + " TEXT," +
            Alert.COLUMN_NAME_ALERT_DAYOFWEEK + " TEXT," +
            Alert.COLUMN_NAME_ALERT_REPEAT_WEEKLY + " BOOLEAN," +
            Alert.COLUMN_NAME_ALERT_HOUR_TYPES + " INTEGER," +
            Alert.COLUMN_NAME_ALERT_STARTTIME + " TEXT," +
            Alert.COLUMN_NAME_ALERT_ENDTIME + " TEXT," +
            Alert.COLUMN_NAME_ALERT_INTERVAL_HOUR + " TEXT," +
            Alert.COLUMN_NAME_ALERT_NUM_OF_TIMES + " INTEGER " +
        " )";

    private static final String SQL_DELETE_ALARM =
            "DROP TABLE IF EXISTS " + Alert.TABLE_NAME;

    private static AlertDBHelper mInstance = null;

    public AlertDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static AlertDBHelper getInstance(Context ctx) {
        if (mInstance == null) {
            mInstance = new AlertDBHelper(ctx.getApplicationContext());
        }
        return mInstance;
    }

    @Override
    public synchronized void close() {
        super.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d("SQL", "creating alarm");
        Log.d("SQL", SQL_CREATE_ALARM);
        db.execSQL(SQL_CREATE_ALARM);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ALARM);
        Log.d("SQL","Upgrade database");
        onCreate(db);
    }

    private AlertModel populateModel(Cursor c) {

        DaySpecs daySpecs = new DaySpecs();
        HourSpecs hourSpecs = new HourSpecs();

        LocalDate startDate,endDate;

        startDate = LocalDate.parse(c.getString(c.getColumnIndex(Alert.COLUMN_NAME_ALERT_STARTDATE)));
        endDate = LocalDate.parse(c.getString(c.getColumnIndex(Alert.COLUMN_NAME_ALERT_ENDDATE)));

        //DAY SPECS
        daySpecs.setDayType(DaySpecs.DayTypes.values()[c.getInt(c.getColumnIndex(Alert.COLUMN_NAME_ALERT_DAY_TYPES))]);
        daySpecs.setDateRange(startDate, endDate);
        boolean[] dayFrequencyArr = convertStringToBooleanArray(c.getString(c.getColumnIndex(Alert.COLUMN_NAME_ALERT_DAYOFWEEK)));
        daySpecs.setDayFrequency(DaySpecs.Frequency.CUSTOM,dayFrequencyArr,0);
        // 1==x (just converting int to boolean). Replace it if you can find better and more elegant way.
        daySpecs.setRepeatWeekly((1==c.getInt(c.getColumnIndex(Alert.COLUMN_NAME_ALERT_REPEAT_WEEKLY))));

        //HOUR SPECS
        hourSpecs.setHourType(HourSpecs.HourTypes.values()[c.getInt(c.getColumnIndex(Alert.COLUMN_NAME_ALERT_HOUR_TYPES))]);
        LocalTime startTime, endTime;
        startTime = LocalTime.parse(c.getString(c.getColumnIndex(Alert.COLUMN_NAME_ALERT_STARTTIME)));
        endTime = LocalTime.parse(c.getString(c.getColumnIndex(Alert.COLUMN_NAME_ALERT_ENDTIME)));
        hourSpecs.setTimeRangeWithoutInterval(startTime, endTime);
        hourSpecs.setIntervalInHour(c.getInt(c.getColumnIndex(Alert.COLUMN_NAME_ALERT_INTERVAL_HOUR)));
        hourSpecs.setNumOfTimes(c.getInt(c.getColumnIndex(Alert.COLUMN_NAME_ALERT_NUM_OF_TIMES)));

        AlertSpecs alertSpecs = new AlertSpecs(daySpecs ,hourSpecs);

        //ALERT FEATURE
        AlertFeature alertFeature = new AlertFeature();

        alertFeature.setName(c.getString(c.getColumnIndex(Alert.COLUMN_NAME_ALERT_NAME)));
        alertFeature.setDescription(c.getString(c.getColumnIndex(Alert.COLUMN_NAME_ALERT_DESCRIPTION)));
        alertFeature.setLaunchAppEnabled(1==c.getInt(c.getColumnIndex(Alert.COLUMN_NAME_ALERT_IS_LAUNCH_APP_ENABLED)));
        alertFeature.setSoundEnabled(1==c.getInt(c.getColumnIndex(Alert.COLUMN_NAME_ALERT_IS_SOUND_ENABLED)));
        alertFeature.setVibrationEnabled(1==c.getInt(c.getColumnIndex(Alert.COLUMN_NAME_ALERT_IS_VIBRATION_ENABLED)));
        alertFeature.setVoiceInstructionStatusEnabled(1==c.getInt(c.getColumnIndex(Alert.COLUMN_NAME_ALERT_IS_VOICE_INSTRUCTION_ENABLED)));
        alertFeature.setNotificationEnabled(1==c.getInt(c.getColumnIndex(Alert.COLUMN_NAME_ALERT_IS_NOTIFICATION_ENABLED)));
        alertFeature.setTone(c.getString(c.getColumnIndex(Alert.COLUMN_NAME_ALERT_TONE)));
        alertFeature.setAppToLaunch(c.getString(c.getColumnIndex(Alert.COLUMN_NAME_ALERT_APP_TO_LAUNCH)));

        boolean isEnabled = 1==c.getInt(c.getColumnIndex(Alert.COLUMN_NAME_ALERT_ENABLED));

        AlertModel model = new AlertModel(alertFeature,alertSpecs,isEnabled);
        model.id = c.getInt(c.getColumnIndex(Alert._ID));

        return model;
    }

    private boolean[] convertStringToBooleanArray(String arr){
        String[] repeatingDays = arr.split(",");
        boolean[] result = new boolean[repeatingDays.length];

        for (int i = 0; i < repeatingDays.length; ++i) {
            result[i] = (repeatingDays[i].equals("false") ? false : true);
        }
        return result;
    }

    private String convertBooleanArrayToString(boolean[] arr){

        String repeatingDays = "";
        for (int i = 0; i < 7; ++i) {
        	repeatingDays += arr[i] + ",";
        }

        return repeatingDays;
    }

    private ContentValues populateContent(AlertModel model){
        ContentValues values = new ContentValues();

        values.put(Alert.COLUMN_NAME_ALERT_NAME,model.getAlertFeature().getName());
        values.put(Alert.COLUMN_NAME_ALERT_ENABLED,model.isEnabled());
        values.put(Alert.COLUMN_NAME_ALERT_IS_VIBRATION_ENABLED,model.getAlertFeature().isVibrationEnabled());
        values.put(Alert.COLUMN_NAME_ALERT_IS_VOICE_INSTRUCTION_ENABLED,model.getAlertFeature().isVoiceInstructionStatusEnabled());
        values.put(Alert.COLUMN_NAME_ALERT_IS_SOUND_ENABLED,model.getAlertFeature().isSoundEnabled());
        values.put(Alert.COLUMN_NAME_ALERT_IS_LAUNCH_APP_ENABLED,model.getAlertFeature().isLaunchAppEnabled());
        values.put(Alert.COLUMN_NAME_ALERT_IS_NOTIFICATION_ENABLED,model.getAlertFeature().isNotificationEnabled());
        values.put(Alert.COLUMN_NAME_ALERT_TONE,model.getAlertFeature().getTone());
        values.put(Alert.COLUMN_NAME_ALERT_DESCRIPTION,model.getAlertFeature().getDescription());
        values.put(Alert.COLUMN_NAME_ALERT_APP_TO_LAUNCH,model.getAlertFeature().getAppToLaunch());

        values.put(Alert.COLUMN_NAME_ALERT_DAY_TYPES,model.getAlertSpecs().getDaySpecs().getDayType().ordinal());
        values.put(Alert.COLUMN_NAME_ALERT_STARTDATE,model.getAlertSpecs().getDaySpecs().getStartDate().toString());
        values.put(Alert.COLUMN_NAME_ALERT_ENDDATE,model.getAlertSpecs().getDaySpecs().getEndDate().toString());
        values.put(Alert.COLUMN_NAME_ALERT_DAYOFWEEK,convertBooleanArrayToString(model.getAlertSpecs().getDaySpecs().getDayOfWeek()));
        values.put(Alert.COLUMN_NAME_ALERT_REPEAT_WEEKLY,model.getAlertSpecs().getDaySpecs().isRepeatWeekly());
        values.put(Alert.COLUMN_NAME_ALERT_HOUR_TYPES,model.getAlertSpecs().getHourSpecs().getHourType().ordinal());
        values.put(Alert.COLUMN_NAME_ALERT_STARTTIME,model.getAlertSpecs().getHourSpecs().getStartTime().toString());
        values.put(Alert.COLUMN_NAME_ALERT_ENDTIME,model.getAlertSpecs().getHourSpecs().getEndTime().toString());
        values.put(Alert.COLUMN_NAME_ALERT_INTERVAL_HOUR,model.getAlertSpecs().getHourSpecs().getIntervalInHour());
        values.put(Alert.COLUMN_NAME_ALERT_NUM_OF_TIMES,model.getAlertSpecs().getHourSpecs().getNumOfTimes());

        return values;
    }

    public long createAlert(AlertModel model){
        ContentValues values = populateContent(model);
        return getWritableDatabase().insert(Alert.TABLE_NAME,null,values);
    }

    public long updateAlert(AlertModel model){
        ContentValues values = populateContent(model);
        return getWritableDatabase().update(Alert.TABLE_NAME, values, Alert._ID + " = ?", new String[] { String.valueOf(model.id)});
    }

    public AlertModel getAlert(long id){
        SQLiteDatabase db = this.getReadableDatabase();

        String select = "SELECT * FROM " + Alert.TABLE_NAME + " WHERE " + Alert._ID + " = " + id;

        Cursor c = db.rawQuery(select, null);

        if (c.moveToNext()) {
            return populateModel(c);
        }

        c.close();
        db.close();
        return null;

    }

    public List<AlertModel> getAlerts() {
        SQLiteDatabase db = this.getReadableDatabase();

        String select = "SELECT * FROM " + Alert.TABLE_NAME;

        Cursor c = db.rawQuery(select, null);

        List<AlertModel> alertList = new ArrayList<AlertModel>();

        while (c.moveToNext()) {
            alertList.add(populateModel(c));
        }

        if (!alertList.isEmpty()) {
            return alertList;
        }

        c.close();
        db.close();
        return new ArrayList<>();
    }

    public int deleteAlert(long id) {
        return getWritableDatabase().delete(Alert.TABLE_NAME, Alert._ID + " = ?", new String[] { String.valueOf(id) });
    }

}
