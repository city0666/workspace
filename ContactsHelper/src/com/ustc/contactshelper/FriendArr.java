/** 
 * ��˵����   ����΢�������б�
 * @author  ��ʤ
 * @date    
 * @version 1.0
 */

package com.ustc.contactshelper;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;


public class FriendArr {

	public static JSONObject jsObject;

	//����΢�������б��ArrayList
	public static ArrayList<HashMap<String, Object>> getFriendsArr(String data) {
		ArrayList<HashMap<String, Object>> socarr = new ArrayList<HashMap<String, Object>>();
		try {
			jsObject = new JSONObject(data);
			Log.i("jsObject", data);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		JSONArray users;
		try {
			users = jsObject.getJSONArray("users");
			for (int i = 0; i < users.length(); i++) {
				JSONObject user = users.getJSONObject(i);
				HashMap<String, Object> map = new HashMap<String, Object>();
				// map.put("ItemImage", R.drawable.checked);//ͼ����Դ��ID
				map.put("ItemTitle", user.getString("screen_name"));
				Log.i("screen_name", user.getString("screen_name"));
				map.put("ItemText", "Weibo ");
				socarr.add(map);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return socarr;

	}
}
