/** 
 * 类说明：   服务，用于启动更新
 * @author  赵晨
 * @date    
 * @version 1.0
 */

package com.ustc.contactshelper;

import android.app.Service;

import android.content.Intent;

import android.os.IBinder;
import android.util.Log;

public class UpdateService extends Service {

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	// 当服务第1次创建时调用该方法
	@Override
	public void onCreate() {
		Log.d("UpdateService", "onCreate");
		super.onCreate();
	}

	// 当服务销毁时调用该方法
	@Override
	public void onDestroy() {
		Log.d("UpdateService", "onDestroy");
		super.onDestroy();
	}

	// 当开始服务时调用该方法

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {

		Log.d("UpdateService", "onStartCommand");
		String stringValue = intent.getStringExtra("type");
		Intent it = null;

		if (stringValue.equals("Update"))
			it = new Intent(this, Update.class);
		else if (stringValue.equals("Autho_Weibo"))
			it = new Intent(this, Autho_Weibo.class);

		it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

		startActivity(it);

		return super.onStartCommand(intent, flags, startId);

	}

}
