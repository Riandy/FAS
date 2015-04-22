package com.riandy.fas;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;

import com.riandy.fas.Alert.AlertDBHelper;
import com.riandy.fas.Alert.AlertModel;
import com.riandy.fas.Alert.DaySpecs;
import com.riandy.fas.Alert.HourSpecs;

import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormat;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
/**
 * Created by Riandy on 30/3/15.
 */
public class AlertParser {

    static Context context;

    AlertParser(Context context){
        this.context = context;
    }

    public List<AlertModel> parse(Uri uri){

        List<AlertModel> list = new ArrayList<AlertModel>();

        try {
            File fXmlFile = new File(getPath(context, uri));
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(fXmlFile);

            //doc.getDocumentElement().normalize();

            NodeList nList = doc.getElementsByTagName("alert");
            Log.d("riandy","nList "+nList.getLength());
            for (int temp = 0; temp < nList.getLength(); temp++) {

                Log.d("riandy","parsing");
                Node nNode = nList.item(temp);

                System.out.println("\nCurrent Element :" + nNode.getNodeName());

                if (nNode.getNodeType() == Node.ELEMENT_NODE) {

                    Element eElement = (Element) nNode;

                    AlertModel model = new AlertModel();
                    model.id = Long.parseLong(eElement.getAttribute("id"));
                    model.getAlertFeature().setName(eElement.getElementsByTagName("name").item(0).getTextContent());
                    model.getAlertFeature().setDescription(eElement.getElementsByTagName("description").item(0).getTextContent());
                    model.setEnabled(eElement.getElementsByTagName("isEnabled").item(0).getTextContent().equals("true"));
                    model.getAlertFeature().setVibrationEnabled(eElement.getElementsByTagName("isVibrationEnabled").item(0).getTextContent().equals("true"));
                    model.getAlertFeature().setVoiceInstructionStatusEnabled(eElement.getElementsByTagName("isVoiceInstructionEnabled").item(0).getTextContent().equals("true"));
                    model.getAlertFeature().setSoundEnabled(eElement.getElementsByTagName("isSoundEnabled").item(0).getTextContent().equals("true"));
                    model.getAlertFeature().setLaunchAppEnabled(eElement.getElementsByTagName("isLaunchAppEnabled").item(0).getTextContent().equals("true"));
                    model.getAlertFeature().setNotificationEnabled(eElement.getElementsByTagName("isNotificationEnabled").item(0).getTextContent().equals("true"));
                    model.getAlertFeature().setTone(eElement.getElementsByTagName("tone").item(0).getTextContent());
                    model.getAlertFeature().setAppToLaunch(eElement.getElementsByTagName("appToLaunch").item(0).getTextContent());
                    model.getAlertSpecs().getDaySpecs().setDayType(DaySpecs.DayTypes.values()[Integer.parseInt(eElement.getElementsByTagName("dayTypes").item(0).getTextContent())]);
                    LocalDate startDate = LocalDate.parse(eElement.getElementsByTagName("startDate").item(0).getTextContent(), DateTimeFormat.forPattern("d MMM yyyy"));
                    LocalDate endDate = LocalDate.parse(eElement.getElementsByTagName("endDate").item(0).getTextContent(),DateTimeFormat.forPattern("d MMM yyyy"));
                    model.getAlertSpecs().getDaySpecs().setDateRange(startDate,endDate);
                    model.getAlertSpecs().getDaySpecs().setDayOfWeek(AlertDBHelper.convertStringToBooleanArray(eElement.getElementsByTagName("dayOfWeek").item(0).getTextContent()));
                    model.getAlertSpecs().getDaySpecs().setRepeatWeekly(eElement.getElementsByTagName("repeatWeekly").item(0).getTextContent().equals("true"));
                    model.getAlertSpecs().getHourSpecs().setHourType(HourSpecs.HourTypes.values()[Integer.parseInt(eElement.getElementsByTagName("hourTypes").item(0).getTextContent())]);
                    LocalTime startTime = LocalTime.parse(eElement.getElementsByTagName("startTime").item(0).getTextContent());
                    LocalTime endTime = LocalTime.parse(eElement.getElementsByTagName("endTime").item(0).getTextContent());
                    int intervalHour = Integer.parseInt(eElement.getElementsByTagName("intervalHour").item(0).getTextContent());
                    model.getAlertSpecs().getHourSpecs().setTimeRange(startTime,endTime,intervalHour);
                    model.getAlertSpecs().getHourSpecs().setNumOfTimes(Integer.parseInt(eElement.getElementsByTagName("numberOfTimes").item(0).getTextContent()));
                    model.getAlertSpecs().getDaySpecs().setEveryNDays(Integer.parseInt(eElement.getElementsByTagName("everyNDays").item(0).getTextContent()));
                    model.getAlertSpecs().getHourSpecs().setLastAlertTime(LocalTime.parse(eElement.getElementsByTagName("lastAlertTime").item(0).getTextContent()));
                    model.getAlertSpecs().getHourSpecs().setCurrentCounter(Integer.parseInt(eElement.getElementsByTagName("currentCounter").item(0).getTextContent()));

                    list.add(model);
                    Log.d("Alert model",model.toString());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("riandy","error "+e.getMessage());
        }


    return list;
    }

    /**
     * Get a file path from a Uri. This will get the the path for Storage Access
     * Framework Documents, as well as the _data field for the MediaStore and
     * other file-based ContentProviders.
     *
     * @param context The context.
     * @param uri The Uri to query.
     * @author paulburke
     */
    public static String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[] {
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {

            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context The context.
     * @param uri The Uri to query.
     * @param selection (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }


    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }
}
