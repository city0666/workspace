package liusir.test.listviewtest;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		final Context mContext = this;
		
		/**获取通讯录信息A**/
        Button botton0 = (Button)findViewById(R.id.look_contact);
        botton0.setOnClickListener(new OnClickListener() {
	    @Override
	    public void onClick(View arg0) {
	    //	Toast.makeText(mContext, "label 1", Toast.LENGTH_LONG).show();
	    	Intent intent = new Intent(mContext,DisplayContact.class);
	    	//Intent intent = new Intent(mContext,SimpleCursorAdapterActivity.class);
	    	startActivity(intent);
	    }
        });
        
        
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	/* called after look_list button was clicked*/
	public void gotoListView(View view)
	{
		Intent intent = new Intent(this,ArrayAdapterActivity.class);
		startActivity(intent);
	}
	
	public void gotoMyListView(View view)
	{
		Intent intent = new Intent(this,MyListView.class);
		startActivity(intent);
	}
	
	public void gotoMySimpleAdapter(View view)
	{
		Intent intent = new Intent(this,MySimpleAdapter.class);
		startActivity(intent);
	}
	
	public void gotoDisplayContact(View view)
	{
		//Intent intent = new Intent(this,DisplayContact.class);
		Intent intent = new Intent(this,DisplayContact.class);		
		startActivity(intent);
	}
}
