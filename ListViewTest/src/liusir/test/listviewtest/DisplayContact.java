package liusir.test.listviewtest;

import java.util.ArrayList;

import android.app.ListActivity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.CommonDataKinds.Photo;
import android.provider.ContactsContract.Contacts;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class DisplayContact extends ListActivity{
	
	Context mContext = null;
	
	/**获取phone表字段**/
	private static final String[] PHONES_PROJECTION = new String[]{
		Phone.DISPLAY_NAME, Phone.NUMBER,Phone.PHOTO_ID, Phone.RAW_CONTACT_ID};
	
	/**联系人信息**/
	private ArrayList<String> mContactsName = new ArrayList<String>();
	private ArrayList<String> mContactsNumber = new ArrayList<String>();
	private ArrayList<Bitmap> mContactsPhoto = new ArrayList<Bitmap>();
	
	ListView mListView = null;
	MyListAdapter adapter = null;
	
	Cursor phoneCursor;
	
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		Toast.makeText(this, "super.onCreate completed!", Toast.LENGTH_LONG).show();
		
		mContext = this;
		//mListView = getListView();
		mListView = this.getListView();
		
		/**获取通讯录信息**/
		//getPhoneContacts();
		
		ContentResolver resolver = mContext.getContentResolver();
		
		// 获取手机联系人
		phoneCursor = resolver.query(Phone.CONTENT_URI, PHONES_PROJECTION, 
				null, null, "sort_key" + " collate NOCASE ASC");
		
		Toast.makeText(this, "get phone contacts completed!", Toast.LENGTH_LONG).show();
		adapter = new MyListAdapter(this);
		setListAdapter(adapter);
		
		mListView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> adapterView, View view,  
		            int position, long id) {  
		        //调用系统方法拨打电话  
		        Intent dialIntent = new Intent(Intent.ACTION_CALL, Uri  
		            .parse("tel:" + mContactsNumber.get(position)));  
		        startActivity(dialIntent);  
		        }
		});
	}
	
	/**获取通讯录联系人信息**/
	
	private void getPhoneContacts() {
		ContentResolver resolver = mContext.getContentResolver();
		
		// 获取手机联系人
		phoneCursor = resolver.query(Phone.CONTENT_URI, PHONES_PROJECTION, 
				null, null, "sort_key" + " collate NOCASE ASC");
		
		if(phoneCursor != null) {
			while (phoneCursor.moveToNext()){
				String name = phoneCursor.getString(0);
				String number = phoneCursor.getString(1);
				long photo_id = phoneCursor.getLong(2);
				
				if (TextUtils.isEmpty(number))
					continue;
				
				//String contactName = phoneCursor.getString(PHONES_DISPLAY_NAME_INDEX);
				//Long contactId = phoneCursor.getLong(PHONES_CONTACT_ID_INDEX);
				//Long photoId = phoneCursor.getLong(PHONES_PHOTO_ID_INDEX);
				Bitmap nail_photo = null;
				
				if (photo_id > 0) {
					byte[] photo_bytes = getNailPhoto(photo_id);
					nail_photo = BitmapFactory.decodeByteArray(photo_bytes, 0, photo_bytes.length);
					
				} else {
					nail_photo = BitmapFactory.decodeResource(getResources(), R.drawable.photos);
				}
				
				mContactsName.add(name);
				mContactsNumber.add(number);
				mContactsPhoto.add(nail_photo);
				//Log.v("联系人", contactName+":"+phoneNumber);
			}
		phoneCursor.close();
		}
	}
	
	public byte[] getNailPhoto(long photo_id) {
	     Uri photoUri = ContentUris.withAppendedId(ContactsContract.Data.CONTENT_URI, photo_id);
	     Cursor cursor = getContentResolver().query(photoUri,
	          new String[] {Contacts.Photo.PHOTO}, null, null, null);
	     if (cursor == null) {
	         return null;
	     }
	     try {
	         if (cursor.moveToFirst()) {
	             byte[] data = cursor.getBlob(0);
	             if (data != null) {
	                 return data;
	             }
	         }
	     } finally {
	         cursor.close();
	     }
	     return null;
	 }
	
	
	class MyListAdapter extends BaseAdapter {  
		Bitmap nail_photo = null;
		Bitmap mDefaultPhoto = BitmapFactory.decodeResource(getResources(), R.drawable.photos);
	    public MyListAdapter(Context context) {  
	        mContext = context;  
	    }  
	  
	    public int getCount() {  
	        //设置绘制数量  
	        //return mContactsName.size();
	    	return phoneCursor.getCount();
	    }  
	  
	    @Override  
	    public boolean areAllItemsEnabled() {  
	        return false;  
	    }  
	  
	    public Object getItem(int position) {  
	        return position;  
	    }  
	  
	    public long getItemId(int position) {  
	        return position;  
	    }  
	  
	    public View getView(int position, View convertView, ViewGroup parent) {  
	        ImageView image = null;  
	        TextView title = null;  
	        TextView text = null;  
	        if (convertView == null || position < phoneCursor.getCount()) {  
	        convertView = LayoutInflater.from(mContext).inflate(  
	            R.layout.my_contact, null);  
	        image = (ImageView) convertView.findViewById(R.id.color_image);  
	        title = (TextView) convertView.findViewById(R.id.color_title);  
	        text = (TextView) convertView.findViewById(R.id.color_text);  
	        }  
	        
	        phoneCursor.moveToPosition(position);
	        //绘制联系人名称  
	        //title.setText(mContactsName.get(position));
	        title.setText(phoneCursor.getString(0));
	        //绘制联系人号码  
	        text.setText(phoneCursor.getString(1));  
	        Log.v("联系人", phoneCursor.getString(0));
	        //绘制联系人头像  
	        long photo_id = phoneCursor.getLong(2);
	        
	        if (nail_photo!= null)
	        {
	        	Log.v("photo:","输出");
	      //  	nail_photo.recycle();
	        }

			if (photo_id > 0) {
				byte[] photo_bytes = getNailPhoto(photo_id);
				nail_photo = BitmapFactory.decodeByteArray(photo_bytes, 0, photo_bytes.length);
				
			} else {
				nail_photo = mDefaultPhoto;
			}
	        image.setImageBitmap(nail_photo);  	        
	        
	        return convertView;  
	    }  
	    }  
}
