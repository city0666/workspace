package liusir.test.listviewtest;

import android.app.ListActivity;
import android.database.Cursor;
import android.os.Bundle;

import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.support.v4.app.LoaderManager;
import android.support.v4.widget.SimpleCursorAdapter;
import android.widget.ListAdapter;

public class SimpleCursorAdapterActivity extends ListActivity {
    @SuppressWarnings("deprecation")
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //���һ��ָ��ϵͳͨѶ¼���ݿ��Cursor������������Դ
        Cursor cur = getContentResolver().query(Phone.CONTENT_URI, null, null, null, null);
        startManagingCursor(cur);
        //LoaderManager.
        //ʵ�����б�������
        
        ListAdapter adapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_1, cur, new String[] {ContactsContract.Contacts.DISPLAY_NAME}, new int[] {android.R.id.text1});
        setListAdapter(adapter);
    }
}
