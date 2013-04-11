package com.ustc.contactshelper;

/*******************************************************
 * Title：同步管理
 * Function：进行同步，并可设置自动同步时间
 * ****************************************************/

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Fragment;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class tab4Fragment extends Fragment {
	private static final String TAG = "tab4Fragment";
	private static final String HELPER_CONTENT = "com.ustc.contactsHelper.data";

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		return inflater.inflate(R.layout.fragment_tab4, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		Log.i(TAG, "onActivityCreated()");
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		Log.i(TAG, "onAttach()");
		super.onAttach(activity);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		Log.i(TAG, "onCreate()");
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		Log.i(TAG, "onDestroy()");
		super.onDestroy();
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		Log.i(TAG, "onPause()");
		super.onPause();
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		Log.i(TAG, "onResume()");
		super.onResume();
		ImageButton refresh = (ImageButton) this.getActivity().findViewById(
				R.id.syncnow);
		final CheckBox autosync = (CheckBox) this.getActivity().findViewById(
				R.id.autosync);
		final RadioGroup syncrate = (RadioGroup) this.getActivity()
				.findViewById(R.id.syncrate);
		final RadioButton synchour = (RadioButton) this.getActivity()
				.findViewById(R.id.radiohour);
		RadioButton syncday = (RadioButton) this.getActivity().findViewById(
				R.id.radioday);
		RadioButton syncweek = (RadioButton) this.getActivity().findViewById(
				R.id.radioweek);
		SharedPreferences userInfo;
		AlarmManager alarmManager;
		PendingIntent pendingActivityIntent;
		userInfo = this.getActivity().getSharedPreferences(HELPER_CONTENT, 0);
		String isauto = userInfo.getString("autosync", "");
		String rate = userInfo.getString("syncrate", "");
		Intent intent = new Intent(this.getActivity(), UpdateService.class);
		pendingActivityIntent = PendingIntent.getService(this.getActivity(), 0,
				intent, 0);
		alarmManager = (AlarmManager) this.getActivity().getSystemService(
				Context.ALARM_SERVICE);

		if (isauto.equals("yes"))
			autosync.setChecked(true);
		else
			autosync.setChecked(false);
		if (rate.equals("hour")) {
			synchour.setChecked(true);

		} else if (rate.equals("day")) {
			syncday.setChecked(true);
		} else if (rate.equals("week")) {
			syncweek.setChecked(true);
		} else
			syncrate.setEnabled(false);

		autosync.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				SharedPreferences userInfo;
				AlarmManager alarmManager;
				PendingIntent pendingActivityIntent;
				userInfo = tab4Fragment.this.getActivity()
						.getSharedPreferences(HELPER_CONTENT, 0);
				Intent intent = new Intent(tab4Fragment.this.getActivity(),
						UpdateService.class);
				intent.putExtra("type", "Update");
				pendingActivityIntent = PendingIntent.getService(
						tab4Fragment.this.getActivity(), 0, intent,
						PendingIntent.FLAG_UPDATE_CURRENT);
				alarmManager = (AlarmManager) tab4Fragment.this.getActivity()
						.getSystemService(Context.ALARM_SERVICE);
				if (autosync.isChecked())// 选中自动更新
				{

					userInfo.edit().putString("autosync", "yes").commit();
					syncrate.setEnabled(true);
					synchour.setChecked(true);
					Log.i("AutoSync", "Start");
				} else {
					userInfo = tab4Fragment.this.getActivity()
							.getSharedPreferences(HELPER_CONTENT, 0);
					userInfo.edit().putString("autosync", "").commit();
					alarmManager.cancel(pendingActivityIntent);// 取消全局定时器
					Log.i("AutoSync", "Stop");
				}

			}
		});
		syncrate.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				SharedPreferences userInfo;
				AlarmManager alarmManager;
				PendingIntent pendingActivityIntent;
				userInfo = tab4Fragment.this.getActivity()
						.getSharedPreferences(HELPER_CONTENT, 0);

				Intent intent = new Intent(tab4Fragment.this.getActivity(),
						UpdateService.class);
				intent.putExtra("type", "Update");
				pendingActivityIntent = PendingIntent.getService(
						tab4Fragment.this.getActivity(), 0, intent,
						PendingIntent.FLAG_UPDATE_CURRENT);
				alarmManager = (AlarmManager) tab4Fragment.this.getActivity()
						.getSystemService(Context.ALARM_SERVICE);
				// TODO Auto-generated method stub
				if (checkedId == R.id.radiohour)// 设置每小时更新
				{
					userInfo = tab4Fragment.this.getActivity()
							.getSharedPreferences(HELPER_CONTENT, 0);
					userInfo.edit().putString("syncrate", "hour").commit();
					alarmManager.setRepeating(AlarmManager.RTC, 0,
							60 * 60 * 1000, pendingActivityIntent);// 设置全局定时器
					Log.i("AutoSync", "hour");
				} else if (checkedId == R.id.radioday)// 设置每天更新
				{
					userInfo = tab4Fragment.this.getActivity()
							.getSharedPreferences(HELPER_CONTENT, 0);
					userInfo.edit().putString("syncrate", "day").commit();
					alarmManager.setRepeating(AlarmManager.RTC, 0,
							24 * 60 * 60 * 1000, pendingActivityIntent);// 设置全局定时器
					Log.i("AutoSync", "day");
				} else if (checkedId == R.id.radioweek)// 设置每周更新
				{
					userInfo = tab4Fragment.this.getActivity()
							.getSharedPreferences(HELPER_CONTENT, 0);
					userInfo.edit().putString("syncrate", "week").commit();
					alarmManager.setRepeating(AlarmManager.RTC, 0, 7 * 24 * 60
							* 60 * 1000, pendingActivityIntent);// 设置全局定时器
					Log.i("AutoSync", "week");
				} else {
					userInfo = tab4Fragment.this.getActivity()
							.getSharedPreferences(HELPER_CONTENT, 0);
					userInfo.edit().putString("syncrate", "").commit();
				}

			}
		});
		refresh.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {// 按下刷新按钮后
				// TODO Auto-generated method stub
				Intent updateService = new Intent(tab4Fragment.this
						.getActivity(), UpdateService.class);
				updateService.putExtra("type", "Update");
				tab4Fragment.this.getActivity().startService(updateService);

			}
		});

	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		Log.i(TAG, "onStart()");
		super.onStart();
	}

	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		Log.i(TAG, "onStop()");
		super.onStop();
	}

	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		Log.i(TAG, "onDestroyView()");
		super.onDestroyView();
	}
}
