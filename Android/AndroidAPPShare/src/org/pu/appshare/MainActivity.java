package org.pu.appshare;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.pu.android.appshare.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ShareActionProvider;

@SuppressLint("NewApi")
public class MainActivity extends Activity implements OnClickListener {

	// Keep reference to the ShareActionProvider from the menu
	private ShareActionProvider shareActionProvider;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		findViewById(R.id.button_text).setOnClickListener(this);
		findViewById(R.id.button_img_exteranl).setOnClickListener(this);
		findViewById(R.id.button_img_assets).setOnClickListener(this);
		findViewById(R.id.button_img_interanl).setOnClickListener(this);
		findViewById(R.id.button_file).setOnClickListener(this);
		findViewById(R.id.button_pack).setOnClickListener(this);
		getActionBar().show();
		try {
			FileOutputStream ou= openFileOutput("aa.png", Context.MODE_PRIVATE);
			Bitmap bitmap=BitmapFactory.decodeResource(getResources(), R.drawable.not);
			bitmap.compress(CompressFormat.PNG, 50, ou);
			ou.flush();
			ou.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu resource
		getMenuInflater().inflate(R.menu.main_menu, menu);

		// Retrieve the share menu item
		MenuItem shareItem = menu.findItem(R.id.menu_share);

		// Now get the ShareActionProvider from the item
		shareActionProvider = (ShareActionProvider) shareItem
				.getActionProvider();
		ContentItem contentitem = new ContentItem(ContentItem.CONTENT_TYPE_TEXT,
				"My Share Text");
		Intent shareIntent = contentitem.getShareIntent();
		shareActionProvider.setShareIntent(shareIntent);
		return super.onCreateOptionsMenu(menu);
	}
	
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		if(item.getItemId()==R.id.menu_share){
			
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.button_text:
			Intent intent1=new ContentItem(ContentItem.CONTENT_TYPE_TEXT, "我分享的内容   http://www.baidu.com/").getShareIntent();
			startActivity(Intent.createChooser(intent1, "Share To...."));// 设置选择框的标题
			break;
		
		case R.id.button_img_exteranl:
			Intent intent2=new ContentItem(ContentItem.CONTENT_TYPE_TELETEXT,"图文分享的标题", "mnt/sdcard/DCIM/Camera/q5.jpg","mnt/sdcard/DCIM/Camera/q4.jpg").getShareIntent();
			startActivity(Intent.createChooser(intent2, "Share To...."));// 设置选择框的标题
			break;
			
		case R.id.button_img_interanl:
			Intent intent3=ContentItem.IntentFactory.decFileTeletext("title","aa.png");
			startActivity(Intent.createChooser(intent3, "Share To...."));// 设置选择框的标题
			break;
		
		case R.id.button_img_assets:
			Intent intent4=ContentItem.IntentFactory.decAssetTeletextIntent("title","win.png");
			startActivity(Intent.createChooser(intent4, "Share To...."));// 设置选择框的标题
			break;
			
		case R.id.button_file:
			Intent intent5=ContentItem.IntentFactory.decAssetFileIntent("AssetProvider.txt");
			startActivity(Intent.createChooser(intent5, "Share To...."));// 设置选择框的标题
			
		case R.id.button_pack:
			Intent intent6=ContentItem.IntentFactory.getTextIntentTo("org.pu.android.appshare.receive", "share to AppShareReceive");
			startActivity(intent6);
		}
	}

}
