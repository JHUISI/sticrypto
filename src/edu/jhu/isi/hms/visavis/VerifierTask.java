package edu.jhu.isi.hms.visavis;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.Connection;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.gson.Gson;

public class VerifierTask extends AsyncTask<String, Void, StdTestResult> {
	
	private final String proverId;
	private final Connection connection;
	private final String chatServerHost;
	private final String tag= VerifierTask.class.getName();
	private final Activity activity;
	private final Notifier<Message> mutex = new Notifier<Message>();
	private final Gson gson = new Gson();
	
	public VerifierTask (String proverId, String chatServerHost,
			Connection connection, Activity activity) {
		super();
		this.proverId = proverId;
		this.connection = connection;
		this.activity = activity;
		this.chatServerHost=chatServerHost;
	}

	@Override
	protected StdTestResult doInBackground(String... params) {
		Log.v(tag,"Verify called");
		Chat chat;
		try {
			chat = setupChat();
			return verify(chat);
		} catch (XMPPException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	private StdTestResult verify(Chat chat)throws XMPPException {
		chat.sendMessage("hello");
		Message m=mutex.waitForMessage();
		chat.sendMessage("You said:"+m.getBody());
		m=mutex.waitForMessage();
		chat.sendMessage("You said:"+m.getBody());
		return null;
		//return gson.fromJson(m.getBody(), StdTestResult.class);
	}
	
	@Override
	protected void onPostExecute(final StdTestResult t){
		Log.v("STD", "changing intents");
		Intent i = new Intent(activity.getApplicationContext(), StatusActivity.class);
		System.out.println(t+" postExecute");
		if (t!=null){
			Bundle b = new Bundle();
			b.putSerializable(VisavisActivity.data_tag, t);
			i.putExtras(b);
			activity.startActivity(i);
		}
	
	}
	

	private  Chat setupChat() throws XMPPException  {
		final Chat newChat = connection.getChatManager().createChat(makeaddress(),
				new MessageListener() {
					public void processMessage(Chat chat, Message message) {
						Log.v(tag,String.format("message recieved: %s", message.getBody()));
						mutex.messageArived(message);
					}
				});
		return newChat;	
	} 
	
	private String makeaddress(){
		return proverId +"@"+chatServerHost+"/Smack";
	}
}
