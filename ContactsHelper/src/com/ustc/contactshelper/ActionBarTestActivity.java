/** 
 * 类说明：   实现导航栏
 * @author  袁意
 * @date    
 * @version 1.1
 */

package com.ustc.contactshelper;


import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ActionBar.Tab;

import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;


public class ActionBarTestActivity extends Activity {
	// private Fragment tab1Fragment;
	/** Called when the activity is first created. */

	private static final String TAG = "ActionBarTestActivity";

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		Log.i(TAG, "onSaveInstanceState()");
		super.onSaveInstanceState(outState);
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// TODO Auto-generated method stub
		Log.i(TAG, "onConfigurationChanged()");
		super.onConfigurationChanged(newConfig);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.i(TAG, "onCreate()");
		setContentView(R.layout.contents);
		// 资源布局的获取必须在调用之前actionBar.setListNavigationCallbacks()定义，否则会报空指针异常
		// SpinnerAdapter mSpinnerAdapter =
		// ArrayAdapter.createFromResource(this, R.array.action_list,
		// android.R.layout.simple_spinner_dropdown_item);
		// 获取ActionBar代码需写在setContentView方法后
		ActionBar actionBar = getActionBar();
		// 设置操作栏导航下拉列表模式
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
		// 设置导航列表回调响应
		// actionBar.setListNavigationCallbacks(mSpinnerAdapter, new
		// mNavigationCallback());
		// 设置Tab标签导航模式
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		// 隐藏应用标题
		// actionBar.setDisplayShowTitleEnabled(false);
		// 实现用户点击ActionBar图标后返回前一个activity
		// actionBar.setDisplayHomeAsUpEnabled(true);
		// 实现返回主页需添加
		actionBar.setHomeButtonEnabled(true);
		Log.i(TAG, "height" + actionBar.getHeight());
		System.out.println(actionBar.getHeight());

		Tab tab = actionBar
				.newTab()
				.setText(R.string.tab1)
				.setTabListener(
						new TabListener<tab1Fragment>(this, "tab1",
								tab1Fragment.class));

		actionBar.addTab(tab);

		tab = actionBar
				.newTab()
				.setText(R.string.tab2)
				.setTabListener(
						new TabListener<tab2Fragment>(this, "tab2",
								tab2Fragment.class));

		actionBar.addTab(tab);

		tab = actionBar
				.newTab()
				.setText(R.string.tab3)
				.setTabListener(
						new TabListener<tab3Fragment>(this, "tab3",
								tab3Fragment.class));

		actionBar.addTab(tab);

		tab = actionBar
				.newTab()
				.setText(R.string.tab4)
				.setTabListener(
						new TabListener<tab4Fragment>(this, "tab4",
								tab4Fragment.class));

		actionBar.addTab(tab);

	}

	public class TabListener<T extends Fragment> implements
			ActionBar.TabListener {

		private Fragment mFragment;
		private final Activity mActivity;
		private final String mTag;
		private final Class<T> mClass;

		public TabListener(Activity activity, String tag, Class<T> cls) {
			mActivity = activity;
			mTag = tag;
			mClass = cls;
		}

		@Override
		public void onTabReselected(Tab tab, FragmentTransaction ft) {
			// TODO Auto-generated method stub
			Log.i(TAG, "onTabReselected()");

		}

		@Override
		public void onTabSelected(Tab tab, FragmentTransaction ft) {
			// TODO Auto-generated method stub
			Log.i(TAG, "onTabSelected()");
			if (mFragment == null) {
				mFragment = Fragment.instantiate(mActivity, mClass.getName());
				ft.add(android.R.id.content, mFragment, mTag);
			} else {
				ft.attach(mFragment);

			}

		}

		@Override
		public void onTabUnselected(Tab tab, FragmentTransaction ft) {
			// TODO Auto-generated method stub
			Log.i(TAG, "onTabUnselected()");
			if (mFragment != null) {
				ft.detach(mFragment);
			}
		}

	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		Log.i(TAG, "onDestroy()");
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		Log.i(TAG, "onPause()");
		super.onPause();
	}

	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		Log.i(TAG, "onRestart()");
		super.onRestart();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		Log.i(TAG, "onResume()");

		super.onResume();
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		Log.i(TAG, "onStart()");
		super.onStart();
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		Log.i(TAG, "onStop()");
		super.onStop();
	}

}