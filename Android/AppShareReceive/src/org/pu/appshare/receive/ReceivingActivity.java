package org.pu.appshare.receive;

import java.util.ArrayList;


import org.pu.android.appshare.receive.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

public class ReceivingActivity extends Activity{
	
	TextView textView;
	ListView listView;
	
	
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.receive);
		textView=(TextView) findViewById(R.id.textView1);
		listView=(ListView) findViewById(R.id.listView1);
		// Get intent, action and MIME type
	    Intent intent = getIntent();
	    String action = intent.getAction();
	    String type = intent.getType();
	    getActionBar().hide();
	    if (Intent.ACTION_SEND.equals(action) && type != null) {
	       handleSendImage(intent);
	       handleSendText(intent);
	       if(type.equals("*/*")){
	    	   Uri uri = (Uri) intent.getParcelableExtra(Intent.EXTRA_STREAM);
	    	   textView.setText(uri.toString());
	        }
	    } else if (Intent.ACTION_SEND_MULTIPLE.equals(action) && type != null) {
	        if (type.startsWith("image/")) {
	        	// Handle multiple images being sent
	            handleSendMultipleImages(intent); 
	            handleSendText(intent);
	        }
	        if(type.equals("*/*")){
	        	Uri uri = (Uri) intent.getParcelableExtra(Intent.EXTRA_STREAM);
	        	textView.setText(uri.toString());
	        }
	    } else {
	        // Handle other intents, such as being started from the home screen
	    	
	    }
	}
	
	void handleSendText(Intent intent) {
	    String sharedText = intent.getStringExtra(Intent.EXTRA_TEXT);
	    if (sharedText != null) {
	        // Update UI to reflect text being shared
	    	textView.setText(sharedText);
	    }
	}

	void handleSendImage(Intent intent) {
	    Uri imageUri = (Uri) intent.getParcelableExtra(Intent.EXTRA_STREAM);
	    if (imageUri != null) {
	        // Update UI to reflect image being shared
	    	ArrayList<Uri> imageUris=new ArrayList<Uri>();
	    	imageUris.add(imageUri);
	    	ImageAdapter adapter=new ImageAdapter(imageUris, this);
	    	listView.setAdapter(adapter);
	    }
	}

	void handleSendMultipleImages(Intent intent) {
	    ArrayList<Uri> imageUris = intent.getParcelableArrayListExtra(Intent.EXTRA_STREAM);
	    if (imageUris != null) {
	        // Update UI to reflect multiple images being shared
	    	ImageAdapter adapter=new ImageAdapter(imageUris, this);
	    	listView.setAdapter(adapter);
	    }
	}

}
