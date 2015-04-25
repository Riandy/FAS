README
By Riandy (riandy@windowslive.com)
Last updated: April 2015


You can use FAS in 2 ways:
- Standalone app.
- Integrate it with other apps.

Flow of the app:
FAS use single activity with multiple fragments design. So everything starts from MainActivity.java. You should start from there to understand the flow. It will then call the SplashScreen fragment and then proceed to the Homepage fragment.

Adding alerts:
AddAlert.java, AddAlertOneOffEvent.java, AddAlertSpecs.java, AddAlertFeature.java
They are all fragments and initially AddAlert will only load AddAlertOneOffEvent fragment. If the user changes the toggle button, then AddAlertOneOffEvent fragment will be replaced with the AddAlertSpecs fragment. The AddAlertFeature fragment will only load when the user selects the "customize alert feature" option.

AddAlert has two important methods onAddFeatureData and onAddSpecsData. Those are the two-callback methods that will be called by AddAlertOneOffEvent.java, AddAlertSpecs.java, AddAlertFeature.java to update the Alert Model (Alert model is the object used to store all the alert information). 
AddAlert class also must implement the interface of those three classes which acts as a callback method that guarantees that the caller fragment has some ways to process the data. If you look at the onCreateView method in AddAlertSpecs (line 70-73), you will see that we do the check first before doing anything.

AlertManagerHelper.java
Most complex class will be the AlertManagerHelper class which deals with the scheduling algorithm. This class checks and determines whether a particular alarm expires or need to be scheduled again in future sessions. We don't schedule repeat alarm all at one go. It is only one instance at a time and when the alarm sounds, we then check whether there is a need to schedule any future reminders of the same alert.

AlertModel.java
This is how all alerts are stored. It has alertSpecs and alertFeature. See the report for detailed explanation of it. This should be trivial.

AlertDBHelper.java
Deals with all the database stuff. CRUD things. Whenever you update the AlertModel object, don't forget to update this class too.

Other classes are quite self-explanatory. Feel free to email me if you are confused.

Using FAS as a library

Steps are quite simple, if confused, just Google on how to include another project in existing project.
-	Add the project as a library through the project properties.
-	Add permissions that FAS uses in the project to the other app AndroidManifest.xml (look at FAS androidManifest and copy the permission to the other app manifest)
-	Add the FAS service to the other app manifest (look at FAS androidManifest file)

<service
    android:name=".Alert.AlertService"
    android:enabled="true" />
<receiver android:name=".Alert.AlertManagerHelper" >
    <intent-filter>
        <action android:name="android.intent.action.BOOT_COMPLETED" />
    </intent-filter>
</receiver>

<uses-permission android:name="android.permission.RECEIVE_BOOT_COMPLETED" />
<uses-permission android:name="android.permission.WAKE_LOCK" />
<uses-permission android:name="android.permission.VIBRATE" />
<uses-permission android:name="android.permission.WRITE_CALENDAR" />
<uses-permission android:name="android.permission.READ_CALENDAR" />
<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
-	In your app, just create the alert in the form of AlertModel Object and then call the alertDBHelper class to insert the alert into the database and then call the AlertManagerHelper class method to schedule all the alerts. You can choose to schedule individual alert or all at one go. (setAlert or setAlerts method).
-	You are done! ☺

Some Tips:
-	If in future development, app becomes slow, you can consider running the scheduling part as a separate thread (AsyncTask, runnable, etc).
-	Explore the iCal standard and consider replacing the existing XML standard. (This makes it more applicable in other apps)
-	Add a more flexible option like (every first Monday of the month, etc.). You can review the existing Google calendar options.
-	Use Material design
