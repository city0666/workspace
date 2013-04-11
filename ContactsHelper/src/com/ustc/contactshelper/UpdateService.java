/** 
 * ��˵����   ����������������
 * @author  �Գ�
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

	// �������1�δ���ʱ���ø÷���
	@Override
	public void onCreate() {
		Log.d("UpdateService", "onCreate");
		super.onCreate();
	}

	// ����������ʱ���ø÷���
	@Override
	public void onDestroy() {
		Log.d("UpdateService", "onDestroy");
		super.onDestroy();
	}

	// ����ʼ����ʱ���ø÷���

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
