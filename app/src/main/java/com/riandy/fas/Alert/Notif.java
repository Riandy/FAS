package com.riandy.fas.Alert;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.riandy.fas.R;

public class Notif {

	private  Context _ctx;
	private String _title,_content;
	private int _icon;
	private Intent _resultIntent;
	
	/**
	 * Create notification with the default icon,text and title. You can set the individual elements later by calling
	 * the setter and getter methods separately
	 */
	public void setNotification(){
		_icon = R.drawable.ic_launcher;

		NotificationCompat.Builder mBuilder =
		        new NotificationCompat.Builder(_ctx)
		        .setSmallIcon(_icon)
		        .setContentTitle(_title)
		        .setContentText(_content);

        if(_resultIntent!=null) {
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(_ctx);
            stackBuilder.addNextIntent(_resultIntent);
            PendingIntent resultPendingIntent =
                    stackBuilder.getPendingIntent(
                            0,
                            PendingIntent.FLAG_UPDATE_CURRENT
                    );

            mBuilder.setContentIntent(resultPendingIntent);
        }

        NotificationManager mNotificationManager =
		    (NotificationManager) _ctx.getSystemService(Context.NOTIFICATION_SERVICE);
		// mId allows you to update the notification later on.
		int mId = 012314;
		mNotificationManager.notify(mId, mBuilder.build());		
	}
	
	public void setNotification(Class<?> callback, String title, String content, int icon) {
		
		_icon = icon != 0 ? icon : R.drawable.ic_launcher;
		NotificationCompat.Builder mBuilder =
		        new NotificationCompat.Builder(_ctx)
		        .setSmallIcon(_icon)
		        .setContentTitle(title)
		        .setContentText(content);
		
		// Creates an explicit intent for an Activity in your app
		if(callback!=null)
		_resultIntent = new Intent(_ctx, callback);

		// The stack builder object will contain an artificial back stack for the
		// started Activity.
		// This ensures that navigating backward from the Activity leads out of
		// your application to the Home screen.
		TaskStackBuilder stackBuilder = TaskStackBuilder.create(_ctx);
		// Adds the back stack for the Intent (but not the Intent itself)
		stackBuilder.addParentStack(callback);
		// Adds the Intent that starts the Activity to the top of the stack
		stackBuilder.addNextIntent(_resultIntent);
		PendingIntent resultPendingIntent =
		        stackBuilder.getPendingIntent(
		            0,
		            PendingIntent.FLAG_UPDATE_CURRENT
		        );
		mBuilder.setContentIntent(resultPendingIntent);
		NotificationManager mNotificationManager =
		    (NotificationManager) _ctx.getSystemService(Context.NOTIFICATION_SERVICE);
		// mId allows you to update the notification later on.
		int mId = 01234;
		mNotificationManager.notify(mId, mBuilder.build());		
		
	}	

	public void setAppToRun(String packageName) {
		PackageManager manager = _ctx.getPackageManager();
		try {
		    _resultIntent = manager.getLaunchIntentForPackage(packageName);
		    if (_resultIntent == null)
		        throw new PackageManager.NameNotFoundException();
		    _resultIntent.addCategory(Intent.CATEGORY_LAUNCHER);
		} catch (PackageManager.NameNotFoundException e) {
			Log.d("ERROR", "package not found.");
		}		
	}
	
	public String getAppToRun(){
		return _resultIntent.getPackage();
	}
	
	public Context get_ctx() {
		return _ctx;
	}

	public void set_ctx(Context _ctx) {
		this._ctx = _ctx;
	}

	public String get_title() {
		return _title;
	}

	public void set_title(String _title) {
		this._title = _title;
	}

	public String get_content() {
		return _content;
	}

	public void set_content(String _content) {
		this._content = _content;
	}

	public int get_icon() {
		return _icon;
	}

	public void set_icon(int _icon) {
		this._icon = _icon;
	}

	public Notif(Context ctx) {
		_ctx = ctx;
	}	
}
