package org.renpy.android;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class Utils extends Activity {
	static Context context;
	
	/**
	 * 判断当前网络是否连接上
	 * 
	 * @return
	 */
	public static boolean isNetWorkAvailable() {
		ConnectivityManager connectivity = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity != null) {
			NetworkInfo netWorkInfo = connectivity.getActiveNetworkInfo();
			return netWorkInfo != null && netWorkInfo.isAvailable();
		}
		return false;
	}

}
