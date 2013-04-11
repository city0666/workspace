package com.ustc.contactshelper;

/*******************************************************
 * Title：账户管理
 * Function：用户登录，身份验证
 * ****************************************************/

import org.json.JSONException;
import org.json.JSONObject;

//import com.weibo.sdk.android.Oauth2AccessToken;
import com.weibo.sdk.android.keep.AccessTokenKeeper;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
//import android.widget.ImageView;
import android.widget.TextView;

public class tab1Fragment extends Fragment {

	private static final String TAG = "tab1Fragment";
	// private static final String PREFERENCES_NAME = "com_weibo_sdk_android";
	private static final String HELPER_CONTENT = "com.ustc.contactsHelper.data";
	public static long UID;

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		Log.i(TAG, "onAttach()");
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Log.i(TAG, "onCreate()");

	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		Log.i(TAG, "onActivityCreated()");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		Log.i(TAG, "onCreateView()");
		return inflater.inflate(R.layout.fragment_tab1, container, false);
		// return null;

	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		Log.i(TAG, "onDestroy()");
	}

	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		super.onDestroyView();
		Log.i(TAG, "onDestroyView()");
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		Log.i(TAG, "onPause()");
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Log.i(TAG, "onResume()");
		Button refresh = (Button) this.getActivity().findViewById(
				R.id.detail_refresh);
		//ImageView detail_image = (ImageView) this.getActivity().findViewById(
		//		R.id.detail_image);
		TextView detail_name = (TextView) this.getActivity().findViewById(
				R.id.detail_name);
		TextView detail_current = (TextView) this.getActivity().findViewById(
				R.id.detail_current);
		TextView detail_intro = (TextView) this.getActivity().findViewById(
				R.id.detail_intro);
		Button logout = (Button) this.getActivity().findViewById(R.id.logout);
		logout.setVisibility(Button.INVISIBLE);
		SharedPreferences settings = this.getActivity().getSharedPreferences(
				HELPER_CONTENT, 0);
		String soc_user = new String("");
		logout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				AccessTokenKeeper.clear(tab1Fragment.this.getActivity());
//				Oauth2AccessToken accessToken = null;

			}
		});

		soc_user = settings.getString("personal", "");
		Log.i("personal", soc_user);

		JSONObject user;
		try {
			user = new JSONObject(soc_user);
			System.out.println(user.getString("id"));
			System.out.println(user.getString("screen_name")); // 用户名
			detail_name.setText(user.getString("screen_name"));
			System.out.println(user.getString("location")); // 位置
			System.out.println(user.getString("description")); // 描述
			detail_intro.setText("简介：  " + user.getString("description"));
			JSONObject statuse = user.getJSONObject("status");
			System.out.println(statuse.getString("text")); // 最新微博
			detail_current.setText("更新： " + statuse.getString("text"));
			System.out.println(user.toString());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		refresh.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent updateService = new Intent(tab1Fragment.this
						.getActivity(), UpdateService.class);
				updateService.putExtra("type", "Autho_Weibo");
				tab1Fragment.this.getActivity().startService(updateService);

			}
		});

	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		Log.i(TAG, "onStart()");
	}

	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		Log.i(TAG, "onStop()");
	}

}
