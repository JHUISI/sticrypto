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
				Intent i = new Intent(v.getContext(), ShareActivity.class);
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

//	class XMPPtest extends AsyncTask<String, Void, Void> {
//
//
//		@Override
//		protected Void doInBackground(String... params) {
//			Connection con = new XMPPConnection("128.220.247.239");
//			try {
//				con.connect();
//				con.login("christina", "password");
//			} catch (XMPPException e1) {
//				// TODO Auto-generated catch block
//				Log.e("STD", e1.toString());
//				e1.printStackTrace();
//			}
//			// Assume we've created a Connection name "connection".
//			ChatManager chatmanager = con.getChatManager();
//		//	chatmanager.addChatListener(arg0)
//			Chat newChat = chatmanager.createChat("ian@128.220.247.239",
//					new MessageListener() {
//						public void processMessage(Chat chat, Message message) {
//							Log.v("STD", message.getBody());
//						}
//					});
//
//			try {
//				Log.v("STD", "sending message");
//				newChat.sendMessage(params[0]);
//				Log.v("STD", "message sent");
//			} catch (XMPPException e) {
//				Log.e("STD", e.toString());
//				System.out.println("Error Delivering block");
//			}
//			return null;
//		}
//	}
}