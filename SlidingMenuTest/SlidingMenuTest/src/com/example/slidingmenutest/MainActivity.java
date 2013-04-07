package com.example.slidingmenutest;

import com.slidingmenu.lib.SlidingMenu;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity {

	private SlidingMenu menu;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		menu = new SlidingMenu(this);
		menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		menu.setFadeEnabled(true);

		//menu.setFadeDegree(0.35f);
		menu.setFadeDegree(0.7f);
		menu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT | SlidingMenu.SLIDING_WINDOW);

		menu.setMenu(R.layout.behindview);
		menu.setBehindOffset(300);
		//menu.setAboveOffset(100);
		//menu.setContent(R.layout.activity_main);
		Button btn = (Button)menu.findViewById(R.id.button1);
		btn.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(MainActivity.this,LeftAndRightActivity.class));
			}});

	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
}
