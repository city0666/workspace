package liusir.test.listviewtest;

import java.util.*;

import android.app.*;
import android.content.ContentResolver;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.CommonDataKinds.Photo;
import android.util.Log;
import android.widget.*;

public class MySimpleAdapter extends ListActivity{
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		
		/*
		String[] from = {"name","phone_number"};
		int[] to = {R.id.title_textview,R.id.message_textview};
		SimpleAdapter adapter = new SimpleAdapter(this,prepareData(),
				R.layout.my_simple_adapter,from,to);
		setListAdapter(adapter);
		*/
		String[] projection = {Phone.DISPLAY_NAME, Phone.NUMBER};
		
		Cursor peoplesCur = getContentResolver().query(Phone.CONTENT_URI, null,
				null, null, "sort_key"+" collate NOCASE ASC");
		int[] toLayouts = {R.id.title_textview,R.id.message_textview};
		SimpleCursorAdapter adapter;
		adapter = new SimpleCursorAdapter(this,R.layout.my_simple_adapter, peoplesCur, projection, 
				toLayouts, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
		setListAdapter(adapter);
	}
	
//	private static final String[] PHONES_PROJECTION = new String[]{
//		Phone.DISPLAY_NAME, Phone.NUMBER, Photo.PHOTO_ID, Phone.CONTACT_ID};
	
	
	
	private List<Map<String,Object>> prepareData()
	{
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
	
		
		ContentResolver content_resolver = this.getContentResolver();
		Cursor contactCursor = content_resolver.query(ContactsContract.Contacts.CONTENT_URI,
				null, null, null, "sort_key" + " collate NOCASE ASC");
		
		contactCursor.moveToFirst();
		int contactIdIdx = contactCursor.getColumnIndex(ContactsContract.Contacts._ID);
		int nameIdx = contactCursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
		int photoIdx = contactCursor.getColumnIndex(ContactsContract.Contacts.PHOTO_FILE_ID);
		
		String contactId = null;
		String name = null;
		
		
		while (contactCursor.moveToNext()) {
			Map<String,Object> map = new HashMap<String,Object>();
			name = contactCursor.getString(nameIdx);
			Log.v("通讯录", name);
			if (name == "" || name == null)
				continue;
			contactId = contactCursor.getString(contactIdIdx);
			
			/**获取手机号码**/
			Cursor phonesCursor = content_resolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
					null, 
					ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=" + contactId,
					null,null);
			phonesCursor.moveToFirst();
			do{
				String phone_number = phonesCursor.getString(phonesCursor.getColumnIndex(
						ContactsContract.CommonDataKinds.Phone.NUMBER));
				Log.v("通讯录", name+phone_number+":---->"+contactId);
				map.put("name", name);
				map.put("phone_number", phone_number);
				list.add(map);
			}while (phonesCursor.moveToNext()) ;
			phonesCursor.close();
			
		}
		
		return list;
	}
	
	
	private List<Map<String,Object>> getData()
	{
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("title", "calculator");
		map.put("message","11011098");
		map.put("image", R.drawable.calculator);
		list.add(map);
		
		map = new HashMap<String,Object>();
		map.put("title", "calendar");
		map.put("message","11011099");
		map.put("image", R.drawable.calendar);
		list.add(map);
		
		map = new HashMap<String,Object>();
		map.put("title", "camera");
		map.put("message","110110100");
		map.put("image", R.drawable.camera);
		list.add(map);
		
		return list;
	}
}
