package liusir.test.listviewtest;

import android.app.ListActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;

public class ArrayAdapterActivity extends ListActivity{
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		String[] str = {"1", "2","3"};
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, 
				android.R.layout.simple_expandable_list_item_1,str);
		setListAdapter(adapter);
	}

}
