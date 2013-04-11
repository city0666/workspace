/** 
 * 类说明：   主类，显示欢迎界面。
 * @author  袁意
 * @date    
 * @version 1.0
 */

package com.ustc.contactshelper;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity {

	private Button btn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		btn = (Button) this.findViewById(R.id.btn);

		btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(MainActivity.this, ActionBarTestActivity.class);
				startActivity(intent);
				MainActivity.this.finish();
			}

		});

	}

}
