package com.riandy.fas.Alert;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.graphics.drawable.Drawable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class PInfo {

	private String appname = "";
	private String pname = "";
	private String versionName = "";
	private int versionCode = 0;
	private Drawable icon;
	private Context _ctx;
	
	public PInfo(Context ctx) {
		_ctx = ctx;
	}
	
	public PInfo(){
		
	}
	private void prettyPrint() {
		//Log.d("HELLO",appname + "\t" + pname + "\t" + versionName + "\t" + versionCode);
	}

	public ArrayList<PInfo> getPackages() {
		ArrayList<PInfo> apps = getInstalledApps(); /* false = no system packages */
		final int max = apps.size();
		for (int i=0; i<max; i++) {
			apps.get(i).prettyPrint();
		}
		return apps;
	}
	
	private boolean isSystemPackage(PackageInfo pkgInfo) {
	    return ((pkgInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0) ? true
	            : false;
	}
	
	private ArrayList<PInfo> getInstalledApps() {
		ArrayList<PInfo> res = new ArrayList<PInfo>();        
		List<PackageInfo> packs = _ctx.getPackageManager().getInstalledPackages(0);
		for(int i=0;i<packs.size();i++) {
			PackageInfo p = packs.get(i);
			
			if (isSystemPackage(p)) {
				continue ;
			}
			PInfo newInfo = new PInfo();
			newInfo.appname = p.applicationInfo.loadLabel(_ctx.getPackageManager()).toString();
			newInfo.pname = p.packageName;
			newInfo.versionName = p.versionName;
			newInfo.versionCode = p.versionCode;
			newInfo.icon= p.applicationInfo.loadIcon(_ctx.getPackageManager());
			res.add(newInfo);
		}
		Collections.sort(res,new Comparator<PInfo>(){
			public int compare(PInfo p1, PInfo p2){
				return p1.getAppname().compareToIgnoreCase(p2.getAppname());
			}
		});

		return res; 
	}
	
	public String getAppname() {
		return appname;
	}

	public Drawable getIcon() {
		return icon;
	}
}