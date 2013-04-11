package com.ustc.contactshelper;

/*******************************************************
 * Title：关联管理
 * Function：显示已关联的账户，可删除关联
 * ****************************************************/
import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.app.Fragment;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnCreateContextMenuListener;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class tab2Fragment extends Fragment {

	private static final String TAG = "tab2Fragment";
	public ArrayList<HashMap<String, Object>> listItem;
	public SimpleAdapter listItemAdapter;
	public ListView list;
	DatabaseUtil dUtil;
	Cursor cursor;
	int mListPos;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		Log.i(TAG, "onCreateView()");
		return inflater.inflate(R.layout.fragment_tab2, container, false);
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
		mListPos = -1;
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

	private void binddata() {
		listItem = new ArrayList<HashMap<String, Object>>();
		dUtil = new DatabaseUtil(this.getActivity());
		dUtil.open();
		cursor = dUtil.fetchAllRelation();
		list = (ListView) this.getActivity().findViewById(R.id.relation); // 社交好友列表
		if (cursor.moveToFirst()) {
			do {
				HashMap<String, Object> map = new HashMap<String, Object>();
				// map.put("ItemImage", R.drawable.checked);//图像资源的ID
				map.put("ReCon", cursor.getString(1));
				map.put("ReSoc", cursor.getString(2));
				map.put("ReType", cursor.getString(3));
				listItem.add(map);
				Log.i("Relation", "Contact Name: " + cursor.getString(1)
						+ " Sociality: " + cursor.getString(2) + " Type: "
						+ cursor.getString(3));
			} while (cursor.moveToNext());

		} else {
			/*
			 * HashMap<String, Object> map1 = new HashMap<String, Object>(); //
			 * map.put("ItemImage", R.drawable.checked);//图像资源的ID
			 * map1.put("ReCon","空"); map1.put("ReSoc","空" ); map1.put("ReType",
			 * "空");
			 * 
			 * listItem.add(map1); Log.i("Relation", "Contact Name: " + "kong" +
			 * " Sociality: " + "kong" + " Type: " + "kong");
			 */

		}

		dUtil.close();

		// Log.i("ItemText",listItem.get(0).get("ReCon").toString());
		// 生成适配器的Item和动态数组对应的元素
		listItemAdapter = new SimpleAdapter(this.getActivity(), listItem,// 数据源
				R.layout.relation_item,// ListItem的XML实现
				// android.R.layout.simple_list_item_single_choice,
				// 动态数组与ImageItem对应的子项
				new String[] { "ReCon", "ReSoc", "ReType" },
				// ImageItem的XML文件里面的三个TextView ID
				new int[] { R.id.rcon, R.id.rsoc, R.id.rtype });
		// Log.i("listItemAdapter", listItemAdapter.getItem(0).toString());
		// 添加并且显示
		list = (ListView) this.getActivity().findViewById(R.id.relation); // 社交好友列表
		// Log.i("list", getString(list.getId()));
		list.setAdapter(listItemAdapter);
		// 添加长按点击

		list.setOnCreateContextMenuListener(new OnCreateContextMenuListener() {

			@Override
			public void onCreateContextMenu(ContextMenu menu, View v,
					ContextMenuInfo menuInfo) {
				// TODO Auto-generated method stub
				menu.setHeaderTitle("长按菜单-ContextMenu");
				menu.add(0, 0, 0, "解除绑定");
				menu.add(0, 1, 0, "取消");
				final AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;

				mListPos = info.position;
			}
		});
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		Log.i(TAG, "onResume()");
		super.onResume();

		binddata();
	}

	// 长按菜单响应函数
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		// tab2Fragment.this.getActivity().setTitle("点击了长按菜单里面的第"+item.getItemId()+"个项目");
		dUtil = new DatabaseUtil(this.getActivity());
		dUtil.open();

		if (mListPos != -1
				&& dUtil.deleteRelation(listItem.get(mListPos).get("ReSoc")
						.toString(), listItem.get(mListPos).get("ReCon")
						.toString())) {
			binddata();
			Toast.makeText(this.getActivity(), "已删除", 2).show();

		}
		dUtil.close();
		return super.onContextItemSelected(item);
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
