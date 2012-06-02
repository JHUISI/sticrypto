package edu.jhu.isi.hms.visavis;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.util.Log;

import org.jivesoftware.smack.*;
import org.jivesoftware.smack.packet.Message;

public class VisavisActivity extends Activity {
	 VerifierTask vt;
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Resources res = getResources();
		vt = new VerifierTask("ian",res.getString(R.string.username),res.
				getString(R.string.password), 
				res.getString(R.string.XMPPServer),
				this);
		
		setContentView(R.layout.main);
		final Button share = (Button) findViewById(R.id.share);
		share.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				Log.v("STD", "changing intents");
				share.setBackgroundColor(20);
				Intent i = new Intent(v.getContext(), StatusActivity.class);
				startActivity(i);
			}

		});
		Log.v("STD", "adding button event");
		final Button send = (Button) findViewById(R.id.notify);
		send.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				Log.v("STD", "starting background task");
				vt.execute();
			}

		});
	}
}