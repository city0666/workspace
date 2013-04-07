package liusir.test.listviewtest;

import java.util.*;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MyListView extends Activity{
	private ListView listView;
//	private List<String> data = new ArrayList<String>();

	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		listView = new ListView(this);
		listView.setAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_expandable_list_item_1,getData()));
		setContentView(listView);
	}
	
	private List<String> getData()
	{
		List<String> data = new ArrayList<String>();
		data.add("data1");
		data.add("data2");
		data.add("data3");
		
		return data;
	}
}
