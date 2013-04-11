/** 
 * 类说明：  刷新当前登录账号信息及好友列表 
 * @author  杨胜
 * @date    
 * @version 1.0
 */

package com.ustc.contactshelper;

import android.app.Activity;
import org.json.JSONException;
import org.json.JSONObject;

import com.weibo.sdk.android.api.AccountAPI;
import com.weibo.sdk.android.api.FriendshipsAPI;
import com.weibo.sdk.android.api.UsersAPI;

import android.os.Bundle;
import java.io.IOException;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.weibo.sdk.android.Oauth2AccessToken;
import com.weibo.sdk.android.Weibo;
import com.weibo.sdk.android.WeiboAuthListener;
import com.weibo.sdk.android.WeiboDialogError;
import com.weibo.sdk.android.WeiboException;
import com.weibo.sdk.android.keep.AccessTokenKeeper;
import com.weibo.sdk.android.net.RequestListener;

public class Autho_Weibo extends Activity {
	/** Called when the activity is first created. */
	private Weibo mWeibo;
	private static final String HELPER_CONTENT = "com.ustc.contactsHelper.data";
	private static final String CONSUMER_KEY = "223992660";
	private static final String REDIRECT_URL = "http://open.weibo.com/apps/223992660/info/advanced";

	public static Oauth2AccessToken accessToken;

	public static long UID;
	public JSONObject user;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.i("Autho_Weibo", "onCreate()");
		setContentView(R.layout.activity_update);
		ProgressBar process = (ProgressBar) findViewById(R.id.progressBar1);
		process.setIndeterminate(true);
		mWeibo = Weibo.getInstance(CONSUMER_KEY, REDIRECT_URL);

		mWeibo.authorize(Autho_Weibo.this, new AuthDialogListener());

		Autho_Weibo.accessToken = AccessTokenKeeper
				.readAccessToken(Autho_Weibo.this);

		// String token = ViewContacts.accessToken.getToken();
		AccountAPI account = new AccountAPI(Autho_Weibo.accessToken);

		account.getUid(new RequestListener() {

			@Override
			public void onComplete(String response) {
				try {
					JSONObject jsObject = new JSONObject(response);

					Autho_Weibo.UID = Long.parseLong(jsObject.getString("uid"));
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onError(WeiboException e) {
				e.printStackTrace();
			}

			@Override
			public void onIOException(IOException e) {
				e.printStackTrace();
			}

		});

		UsersAPI users = new UsersAPI(Autho_Weibo.accessToken);

		// users.show(1924318283, new RequestListener(){//// 账号ID
		// Autho_Weibo.UID
		users.show(UID, new RequestListener() {
			@Override
			public void onComplete(String response) {
				// TODO Auto-generated method stub

				SharedPreferences pref = Autho_Weibo.this.getSharedPreferences(
						HELPER_CONTENT, Context.MODE_APPEND);
				Editor editor = pref.edit();
				editor.putString("personal", response);
				editor.commit();
				Autho_Weibo.this.finish();

			}

			@Override
			public void onError(WeiboException arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onIOException(IOException arg0) {
				// TODO Auto-generated method stub

			}

		});

		FriendshipsAPI friends = new FriendshipsAPI(Autho_Weibo.accessToken);

		// friends.friends("颜风", 100, 0, true, new RequestListener(){
		friends.friends(UID, 100, 0, false, new RequestListener() {
			@Override
			public void onComplete(String response) {
				Log.i("content", response);
				SharedPreferences pref = Autho_Weibo.this.getSharedPreferences(
						HELPER_CONTENT, Context.MODE_APPEND);
				Editor editor = pref.edit();
				editor.putString("content", response);
				editor.commit();
				// Autho_Weibo.this.finish();
			}

			@Override
			public void onIOException(IOException e) {
				e.printStackTrace();
			}

			@Override
			public void onError(WeiboException e) {
				e.printStackTrace();

			}
		});

	}

	class AuthDialogListener implements WeiboAuthListener {

		@Override
		public void onComplete(Bundle values) {
			String token = values.getString("access_token");
			String expires_in = values.getString("expires_in");
			Autho_Weibo.accessToken = new Oauth2AccessToken(token, expires_in);
			if (Autho_Weibo.accessToken.isSessionValid()) {
				AccessTokenKeeper
						.keepAccessToken(Autho_Weibo.this, accessToken);
				Toast.makeText(Autho_Weibo.this, "认证成功", Toast.LENGTH_SHORT)
						.show();
			}
		}

		@Override
		public void onError(WeiboDialogError e) {
			Toast.makeText(getApplicationContext(),
					"Auth error : " + e.getMessage(), Toast.LENGTH_LONG).show();
		}

		@Override
		public void onCancel() {
			Toast.makeText(getApplicationContext(), "Auth cancel",
					Toast.LENGTH_LONG).show();
		}

		@Override
		public void onWeiboException(WeiboException e) {
			Toast.makeText(getApplicationContext(),
					"Auth exception : " + e.getMessage(), Toast.LENGTH_LONG)
					.show();
		}

	}

}