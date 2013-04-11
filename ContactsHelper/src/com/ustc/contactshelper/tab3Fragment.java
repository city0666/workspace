package com.ustc.contactshelper;

/*******************************************************
 * Title：绑定管理
 * Function：绑定联系人和社交好友的信息，并存入数据库中。
 * ****************************************************/
import java.util.ArrayList;
import java.util.HashMap;

import com.ustc.contactshelper.DatabaseUtil;
import com.ustc.contactshelper.Pinyin;
import com.ustc.contactshelper.R;

import android.app.Activity;
import android.app.Fragment;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;

public class tab3Fragment extends Fragment implements OnItemClickListener,
		OnScrollListener {
	private static final String HELPER_CONTENT = "com.ustc.contactsHelper.data";
	private static final String TAG = "tab3Fragment";
	public MyListAdapter adapter;
	public SimpleAdapter listItemAdapter;
	public ArrayList<String> webNameArr;
	public ArrayList<HashMap<String, Object>> listItem;
	private WindowManager windowManager;
	private TextView txtOverlay; // 用来放在WindowManager中显示提示字符
	private Handler handler;
	private DisapearThread disapearThread;
	private int scrollState; // 滚动的状态
	public ListView list, listview, list_soc;
	int item_selected, item_selected_soc;
	DatabaseUtil dbUtil;
	private String py[] = { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J",
			"K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W",
			"X", "Y", "Z" };

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		return inflater.inflate(R.layout.fragment_tab3, container, false);
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
		// 将txtOverlay删除。
		txtOverlay.setVisibility(View.INVISIBLE);
		windowManager.removeView(txtOverlay);
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
		StringBuilder sb = new StringBuilder();
		item_selected = -1;
		item_selected_soc = -1;
		dbUtil = new DatabaseUtil(this.getActivity());
		// 得到通讯录的内容提供者的URI
		Uri uri = ContactsContract.Contacts.CONTENT_URI;

		ContentResolver provider = this.getActivity().getContentResolver();
		// 通过内容提供者查询得到通讯录联系人基本信息列表
		Cursor cursor = provider.query(uri, null, null, null, "sort_key"
				+ " collate NOCASE ASC");

		SharedPreferences settings = this.getActivity().getSharedPreferences(
				HELPER_CONTENT, 0);
		String soc_data = new String();
		// while(soc_data=="")
		{
			soc_data = settings.getString("content", "");
			Log.i("String", "waiting");
		}
		if (soc_data != "")
			Log.i("String", soc_data);

		txtOverlay = (TextView) LayoutInflater.from(this.getActivity())
				.inflate(R.layout.list_popup_char_hint, null);
		// 默认设置为不可见。
		txtOverlay.setVisibility(View.INVISIBLE);
		// 设置WindowManager
		WindowManager.LayoutParams lp = new WindowManager.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT,
				WindowManager.LayoutParams.TYPE_APPLICATION,
				// 设置为无焦点状态
				WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
						| WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
				// 半透明效果
				PixelFormat.TRANSLUCENT);
		windowManager = (WindowManager) this.getActivity().getSystemService(
				this.getActivity().WINDOW_SERVICE);
		windowManager.addView(txtOverlay, lp);

		handler = new Handler();
		disapearThread = new DisapearThread();

		// Arrays.sort(stringArr, String.CASE_INSENSITIVE_ORDER); // 大小写不敏感
		webNameArr = new ArrayList<String>();
		// 将列表中的数据加入到ArrayList中
		/*********
		 * //ADD 从联系人获取姓名和联系人id
		 * 
		 * //ADD 将姓名分别插入到webNameArr
		 * 
		 **********/
		int i = 0;
		if (cursor == null)
			return;
		while (cursor.moveToNext()) {
			sb.delete(0, sb.length());

			// 联系人主键ID
			String contactId = cursor.getString(cursor
					.getColumnIndex(ContactsContract.Contacts._ID));
			String name = cursor.getString(cursor
					.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
			webNameArr.add(name);

			// stringArr[i]=name;
			i++;
			sb.append("contact_id=");
			sb.append(contactId);
			sb.append(",name=");
			sb.append(name);

		}
		cursor.close();

		Log.i("联系人", sb.toString());

		/*
		 * listItem = new ArrayList<HashMap<String, Object>>(); for(int
		 * i1=0;i1<10;i1++) { HashMap<String, Object> map = new HashMap<String,
		 * Object>(); // map.put("ItemImage", R.drawable.checked);//图像资源的ID
		 * map.put("ItemTitle", "Level "+i1); map.put("ItemText", "快乐的青蛙王 ");
		 * listItem.add(map); }
		 */
		if (soc_data != "")
			listItem = FriendArr.getFriendsArr(soc_data);
		Log.i("ItemText", listItem.get(0).get("ItemText").toString());
		// 生成适配器的Item和动态数组对应的元素
		listItemAdapter = new SimpleAdapter(this.getActivity(), listItem,// 数据源
				R.layout.bind,// ListItem的XML实现
				// android.R.layout.simple_list_item_single_choice,
				// 动态数组与ImageItem对应的子项
				new String[] { "ItemTitle", "ItemText" },
				// ImageItem的XML文件里面的两个TextView ID
				new int[] { R.id.soc_name, R.id.soc_text });
		Log.i("listItemAdapter", listItemAdapter.getItem(0).toString());
		// 添加并且显示
		list_soc = (ListView) this.getActivity().findViewById(R.id.list1); // 社交好友列表
		Log.i("list_soc", getString(list_soc.getId()));
		list_soc.setAdapter(listItemAdapter);

		list = (ListView) this.getActivity().findViewById(R.id.list); // 联系人ListView
		listview = (ListView) this.getActivity().findViewById(R.id.listview); // 拼音查询ListView
		Log.i("list", getString(list.getId()));
		Log.i("listview", getString(listview.getId()));
		adapter = new MyListAdapter(this.getActivity());
		list.setAdapter(adapter);// 将数据适配器与Activity进行绑定
		list.setOnScrollListener(this);

		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				RadioButton selected = (RadioButton) arg1
						.findViewById(R.id.selected);
				RadioButton pre;

				if (item_selected_soc == -1) {
					if (item_selected != -1) {
						pre = (RadioButton) list.getChildAt(item_selected)
								.findViewById(R.id.selected);
						pre.setVisibility(View.INVISIBLE);
						selected.setSelected(false);
					}
					if (selected.getVisibility() == View.VISIBLE) {
						Log.i("Selected", "make invisible");
						selected.setVisibility(View.INVISIBLE);
						selected.setSelected(false);
					} else {
						selected.setVisibility(View.VISIBLE);
						selected.setSelected(true);
						item_selected = arg2;
					}
				} else {

					item_selected = arg2;
					Log.i("选中", "item_selected:" + item_selected
							+ " item_selected_soc:" + item_selected_soc);
					selected = (RadioButton) list_soc.getChildAt(
							item_selected_soc).findViewById(R.id.soc_selected);
					selected.setVisibility(View.INVISIBLE);
					selected.setSelected(false);
					dbUtil.open();
					Log.i("Relation",
							"webNameArr:"
									+ webNameArr.get(item_selected)
									+ " listItem:"
									+ listItem.get(item_selected_soc)
											.get("ItemText").toString());
					dbUtil.createRelation(webNameArr.get(item_selected),
							listItem.get(item_selected_soc).get("ItemTitle")
									.toString(), "Weibo");
					dbUtil.close();

					item_selected = -1;
					item_selected_soc = -1;
					Toast.makeText(tab3Fragment.this.getActivity(), "绑定成功", 2)
							.show();

				}
				//tab3Fragment.this.getActivity().setTitle("点击第" + arg2 + "个项目");
		

			}

		});

		ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(
				this.getActivity(), R.layout.textview, py);
		listview.setAdapter(adapter1);

		listview.setDivider(null);
		listview.setOnItemClickListener(this);

		list_soc.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				RadioButton selected = (RadioButton) arg1
						.findViewById(R.id.soc_selected);
				RadioButton pre;

				if (item_selected == -1) {
					if (item_selected_soc != -1) {
						pre = (RadioButton) list_soc.getChildAt(
								item_selected_soc).findViewById(
								R.id.soc_selected);
						pre.setVisibility(View.INVISIBLE);
						selected.setSelected(false);
					}
					if (selected.getVisibility() == View.VISIBLE) {
						Log.i("Selected", "make invisible");
						selected.setVisibility(View.INVISIBLE);
						selected.setSelected(false);
					} else {
						selected.setVisibility(View.VISIBLE);
						selected.setSelected(true);
						item_selected_soc = arg2;
					}
				} else {

					item_selected_soc = arg2;
					Log.i("选中", "item_selected:" + item_selected
							+ " item_selected_soc:" + item_selected_soc);
					selected = (RadioButton) list.getChildAt(item_selected)
							.findViewById(R.id.selected);
					selected.setVisibility(View.INVISIBLE);
					selected.setSelected(false);

					dbUtil.open();
					Log.i("Relation",
							"webNameArr:"
									+ webNameArr.get(item_selected)
									+ " listItem:"
									+ listItem.get(item_selected_soc)
											.get("ItemText").toString());
					dbUtil.createRelation(webNameArr.get(item_selected),
							listItem.get(item_selected_soc).get("ItemTitle")
									.toString(), "Weibo");
					dbUtil.close();

					item_selected = -1;
					item_selected_soc = -1;
					Toast.makeText(tab3Fragment.this.getActivity(), "绑定成功", 2)
							.show();

				}
				//tab3Fragment.this.getActivity().setTitle("点击第" + arg2 + "个项目");


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

	

	public class DisapearThread implements Runnable {
		public void run() {
			// 避免在1.5s内，用户再次拖动时提示框又执行隐藏命令。
			if (scrollState == ListView.OnScrollListener.SCROLL_STATE_IDLE) {
				txtOverlay.setVisibility(View.INVISIBLE);
			}

		}
	}

	public final class ViewHolder {
		// public TextView firstCharHintTextView;
		public TextView orderTextView;
		public TextView nameTextView;
	}

	public class MyListAdapter extends BaseAdapter {
		private LayoutInflater inflater;

		public MyListAdapter(Context context) {
			this.inflater = LayoutInflater.from(context);
		}

		public int getCount() {
			return webNameArr.size();
		}

		public Object getItem(int position) {
			return webNameArr.get(position);
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(final int position, View convertView,
				ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.list_item, null);
				holder = new ViewHolder();
				// holder.firstCharHintTextView = (TextView) convertView
				// .findViewById(R.id.text_first_char_hint);
				holder.orderTextView = (TextView) convertView
						.findViewById(R.id.order);
				holder.nameTextView = (TextView) convertView
						.findViewById(R.id.content);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.orderTextView.setText(String.valueOf(position + 1) + ".");
			holder.nameTextView.setText(webNameArr.get(position));
			/*
			 * int idx = position - 1; // 判断前后Item是否匹配，如果不匹配则设置并显示，匹配则取消 char
			 * previewChar = idx >= 0 ? webNameArr.get(idx).charAt(0) : ' ';
			 * char currentChar = webNameArr.get(position).charAt(0); //
			 * 将小写字符转换为大写字符 char newPreviewChar =
			 * Character.toUpperCase(previewChar); char newCurrentChar =
			 * Character.toUpperCase(currentChar); if (newCurrentChar !=
			 * newPreviewChar) {
			 * holder.firstCharHintTextView.setVisibility(View.VISIBLE);
			 * holder.firstCharHintTextView.setText(String
			 * .valueOf(newCurrentChar)); } else { //
			 * 此段代码不可缺：实例化一个CurrentView后，会被多次赋值并且只有最后一次赋值的position是正确
			 * holder.firstCharHintTextView.setVisibility(View.GONE); }
			 */
			return convertView;
		}
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		txtOverlay.setText(String.valueOf(
				webNameArr.get(firstVisibleItem + (visibleItemCount >> 1))
						.charAt(0)).toUpperCase());
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		this.scrollState = scrollState;
		if (scrollState == ListView.OnScrollListener.SCROLL_STATE_IDLE) {
			handler.removeCallbacks(disapearThread);
			// 提示延迟1.5s再消失
			handler.postDelayed(disapearThread, 1500);
		} else {
			txtOverlay.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		String s = ((TextView) arg1).getText().toString();
		txtOverlay.setText(s);
		txtOverlay.setVisibility(View.VISIBLE);
		handler.removeCallbacks(disapearThread);
		// 提示延迟1.5s再消失
		handler.postDelayed(disapearThread, 1500);

		int localPosition = binSearch(webNameArr, s); // 接收返回值

		if (localPosition != -1) {
			txtOverlay.setVisibility(View.INVISIBLE); // 防止点击出现的txtOverlay与滚动出现的txtOverlay冲突
			list.setSelection(localPosition); // 让List指向对应位置的Item
		}

	}

	// 需重新查找函数，获取每个姓名的首字母，然后进行比较
	public static int binSearch(ArrayList<String> webNameArr, String s) {
		String word = new String();
		// ChineseSpelling finder = ChineseSpelling.getInstance();

		// System.out.println(finder.getSpelling());
		for (int i = 0; i < webNameArr.size(); i++) {
			// word = PinyinToolkit.cn2FirstSpell(string[i]);
			// finder.setResource(string[i]);
			// word = finder.getSpelling();
			// word=string[i];
			// word = Firstletter.getSpells(string[i]);
			word = Pinyin.getPinyin(webNameArr.get(i));
			// System.out.println("l:" + i);
			Log.i(word, "first:" + word.toUpperCase().charAt(0));

			if (s.equalsIgnoreCase("" + word.toUpperCase().charAt(0))) // 不区分大小写
			{
				// System.out.println("location:" + i);
				return i;
			}
		}
		return -1;

	}

}
