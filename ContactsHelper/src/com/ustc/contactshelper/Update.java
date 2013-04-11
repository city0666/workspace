/** 
 * 类说明：   更新微博好友数据到通讯录
 * @author  袁意、杨胜、赵晨
 * @date    
 * @version 2.1
 */
package com.ustc.contactshelper;

import java.io.IOException;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.weibo.sdk.android.Oauth2AccessToken;
import com.weibo.sdk.android.Weibo;
import com.weibo.sdk.android.WeiboAuthListener;
import com.weibo.sdk.android.WeiboDialogError;
import com.weibo.sdk.android.WeiboException;
import com.weibo.sdk.android.api.AccountAPI;
import com.weibo.sdk.android.api.FriendshipsAPI;
import com.weibo.sdk.android.keep.AccessTokenKeeper;
import com.weibo.sdk.android.net.RequestListener;

import android.content.ContentProviderOperation;
import android.database.Cursor;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.Data;
import android.provider.ContactsContract.CommonDataKinds.Note;
import android.app.Activity;
import android.util.Log;

import android.widget.ProgressBar;
import android.widget.Toast;

public class Update extends Activity {

	private Weibo mWeibo;
	private static final String CONSUMER_KEY = "223992660";// 替换为开发者的appkey，例如"1646212860";
	private static final String REDIRECT_URL = "http://open.weibo.com/apps/223992660/info/advanced";

	public static Oauth2AccessToken accessToken;

	public static long UID;
	private static DatabaseUtil dbUtil;
	Cursor cursor;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.i("Update", "onCreate()");
		setContentView(R.layout.activity_update);
		ProgressBar process = (ProgressBar) findViewById(R.id.progressBar1);
		process.setIndeterminate(true);

		// /////////////////////Weibo//////////////////////////////////
		mWeibo = Weibo.getInstance(CONSUMER_KEY, REDIRECT_URL);

		// mWeibo.authorize(this, new AuthDialogListener());

		accessToken = AccessTokenKeeper.readAccessToken(this);

		// String token = ViewContacts.accessToken.getToken();
		AccountAPI account = new AccountAPI(accessToken);

		account.getUid(new RequestListener() {

			@Override
			public void onComplete(String response) {
				try {
					JSONObject jsObject = new JSONObject(response);

					UID = Long.parseLong(jsObject.getString("uid"));
					System.out.println(UID);
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

		FriendshipsAPI friends = new FriendshipsAPI(accessToken);

		// friends.friends("颜风", 100, 0, true, new RequestListener(){
		friends.friends(UID, 100, 0, false, new RequestListener() {
			@Override
			public void onComplete(String response) {
				dbUtil = new DatabaseUtil(Update.this);
				dbUtil.open();
				cursor = dbUtil.fetchAllRelation();
				JSONObject jsObject = null;
				Log.i("content", response);
				try {
					jsObject = new JSONObject(response);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				JSONArray users;

				try {
					users = jsObject.getJSONArray("users");
					for (int i = 0; i < users.length(); i++) {
						JSONObject user = users.getJSONObject(i);
						// 将社交好友信息更新到通讯录中
						cursor.moveToFirst();
						if (cursor != null) {
							do {
								Log.i("遍历Relation", cursor.getString(2) + "&"
										+ user.getString("screen_name"));
								if (cursor
										.getString(2)
										.trim()
										.equals(user.getString("screen_name")
												.trim())) {
									Log.i("getString(1)", cursor.getString(1));
									Log.i("display name",
											ContactsContract.Contacts.DISPLAY_NAME);
									Cursor cursor1 = getContentResolver()
											.query(ContactsContract.Data.CONTENT_URI,
													null,
													ContactsContract.Contacts.DISPLAY_NAME
															+ "=?",
													new String[] { cursor
															.getString(1) },
													null);
									cursor1.moveToFirst();

									Log.v("screen_name",
											cursor.getString(2)
													+ "&"
													+ user.getString("screen_name")
													+ " match!!!!");

									String id = cursor1.getString(cursor1
											.getColumnIndex(Data.RAW_CONTACT_ID));

									// cursor1.close();

									Log.v("raw_id", id);

									ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();

									// 更新address信息
									cursor1 = getContentResolver()
											.query(ContactsContract.Data.CONTENT_URI,
													null,
													Data.RAW_CONTACT_ID + "=?"
															+ " AND "
															+ Data.MIMETYPE
															+ "=?",
													new String[] {
															String.valueOf(id),
															ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_ITEM_TYPE },
													null);

									if (cursor1.moveToFirst()) {// 存在则修改
										ops.add(ContentProviderOperation
												.newUpdate(
														ContactsContract.Data.CONTENT_URI)
												.withSelection(
														Data.RAW_CONTACT_ID
																+ "=?"
																+ " AND "
																+ Data.MIMETYPE
																+ "=?"
																+ "AND "
																+ ContactsContract.CommonDataKinds.StructuredPostal.TYPE
																+ "=?",
														new String[] {
																String.valueOf(id),
																ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_ITEM_TYPE,
																String.valueOf(ContactsContract.CommonDataKinds.StructuredPostal.TYPE_WORK) })
												.withValue(
														ContactsContract.CommonDataKinds.StructuredPostal.STREET,
														user.getString("location"))
												.build());
									} else {
										// 没有则添加
										if (!user.getString("location").equals(
												"")) {
											ops.add(ContentProviderOperation
													.newInsert(
															ContactsContract.Data.CONTENT_URI)
													.withValue(
															ContactsContract.Data.RAW_CONTACT_ID,
															String.valueOf(id))
													.withValue(
															ContactsContract.Data.MIMETYPE,
															ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_ITEM_TYPE)
													.withValue(
															ContactsContract.CommonDataKinds.StructuredPostal.STREET,
															user.getString("location"))
													.withValue(
															ContactsContract.CommonDataKinds.StructuredPostal.TYPE,
															ContactsContract.CommonDataKinds.StructuredPostal.TYPE_WORK)
													.build());
										}
									}

									// 更新note(备注)信息
									cursor1 = getContentResolver().query(
											ContactsContract.Data.CONTENT_URI,
											null,
											Data.RAW_CONTACT_ID + "=?"
													+ " AND " + Data.MIMETYPE
													+ "=?",
											new String[] { String.valueOf(id),
													Note.CONTENT_ITEM_TYPE },
											null);

									if (cursor1.moveToFirst()) {// 存在则修改

										ops.add(ContentProviderOperation
												.newUpdate(
														ContactsContract.Data.CONTENT_URI)
												.withSelection(
														Data.RAW_CONTACT_ID
																+ "=?"
																+ " AND "
																+ ContactsContract.Data.MIMETYPE
																+ " = ?"
																+ "AND ",
														new String[] {
																String.valueOf(id),
																Note.CONTENT_ITEM_TYPE })
												.withValue(
														Note.NOTE,
														user.getString("description"))
												.build());
									} else {
										// 没有则添加
										// 添加note
										if (!user.getString("description")
												.equals("")) {
											ops.add(ContentProviderOperation
													.newInsert(
															ContactsContract.Data.CONTENT_URI)
													.withValue(
															ContactsContract.Data.RAW_CONTACT_ID,
															String.valueOf(id))
													.withValue(
															ContactsContract.Data.MIMETYPE,
															Note.CONTENT_ITEM_TYPE)
													.withValue(
															Note.NOTE,
															user.getString("description"))
													.build());
										}

									}

									// 更新IM信息(最新微博)

									JSONObject statuse = user
											.getJSONObject("status");
									String WEI_BO = statuse.getString("text");
									cursor1 = getContentResolver()
											.query(ContactsContract.Data.CONTENT_URI,
													null,
													Data.RAW_CONTACT_ID + "=?"
															+ " AND "
															+ Data.MIMETYPE
															+ "=?",
													new String[] {
															String.valueOf(id),
															ContactsContract.CommonDataKinds.Im.CONTENT_ITEM_TYPE },
													null);

									if (cursor1.moveToFirst()) {// 存在则修改
										ops.add(ContentProviderOperation
												.newUpdate(
														ContactsContract.Data.CONTENT_URI)
												.withSelection(
														Data.RAW_CONTACT_ID
																+ "=?"
																+ " AND "
																+ ContactsContract.Data.MIMETYPE
																+ " = ?",
														new String[] {
																String.valueOf(id),
																ContactsContract.CommonDataKinds.Im.CONTENT_ITEM_TYPE })
												.withValue(
														ContactsContract.CommonDataKinds.Im.DATA1,
														WEI_BO)
												.withValue(
														ContactsContract.CommonDataKinds.Im.PROTOCOL,
														ContactsContract.CommonDataKinds.Im.PROTOCOL_CUSTOM)
												.build());
									} else {
										// add IM
										ops.add(ContentProviderOperation
												.newInsert(
														ContactsContract.Data.CONTENT_URI)
												.withValue(
														ContactsContract.Data.RAW_CONTACT_ID,
														String.valueOf(id))
												.withValue(
														ContactsContract.Data.MIMETYPE,
														ContactsContract.CommonDataKinds.Im.CONTENT_ITEM_TYPE)
												.withValue(
														ContactsContract.CommonDataKinds.Im.DATA1,
														WEI_BO)
												.withValue(
														ContactsContract.CommonDataKinds.Im.PROTOCOL,
														ContactsContract.CommonDataKinds.Im.PROTOCOL_CUSTOM)
												.build());
									}
									try {
										getContentResolver()
												.applyBatch(
														ContactsContract.AUTHORITY,
														ops);
									} catch (Exception e) {
									}

								}
							} while (cursor.moveToNext());

						}
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				dbUtil.close();
				Update.this.finish();

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
			accessToken = new Oauth2AccessToken(token, expires_in);
			if (accessToken.isSessionValid()) {
				AccessTokenKeeper.keepAccessToken(Update.this, accessToken);
				Toast.makeText(Update.this, "认证成功", Toast.LENGTH_SHORT).show();
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
