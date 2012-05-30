package edu.jhu.isi.hms.visavis;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManager;
import org.jivesoftware.smack.Connection;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

public class VerifierTask extends AsyncTask<String, Void, StdTestResult> {
	private final String proverId;
	private final String verifierId;
	private final String chatServer;
	private final String password;
	private final String tag= VerifierTask.class.getName();
	private final Activity activity;
	private Message message;
	public VerifierTask(String proverId, String verifierId, String password,
			String chatServer, Activity activity) {
		super();
		this.proverId = proverId;
		this.verifierId = verifierId;
		this.chatServer = chatServer;
		this.password = password;
		this.activity = activity;
		
	}

	@Override
	protected StdTestResult doInBackground(String... params) {
		try {
			final Object mutex = new Object();
			ChatManager cm = setup();
	
			final Chat newChat = cm.createChat(makeaddress(),
					new MessageListener() {
						public void processMessage(Chat chat, Message message) {
						}
					});
			
			newChat.addMessageListener(		new MessageListener() {
						public void processMessage(Chat chat, Message message) {
							Log.v(tag, message.getBody());
							VerifierTask.this.message = message; 
							synchronized (chat) {
							    chat.notifyAll();
							}
							Log.v(tag,"notified");
						}
					});
			try{
				newChat.sendMessage("hello");
				
				Log.v(tag,"done waiting");
				waitOnMessage(newChat);
				StdTestResult t = new StdTestResult();
				t.name=message.getBody();
				return t;
			}catch(XMPPException e){
				Log.e(this.tag,"Unable to send hello message",e);
			}
		} catch (XMPPException e) {
			Log.e(this.tag,"Unable to set up connection",e);
		}
		return null;
	}
	protected void onPostExecute(StdTestResult t){
		((TextView) activity.findViewById(R.id.t)).setText(t.name);
	}
	
	private void waitOnMessage(final Object mutex){
		Log.v(tag,"at sychronized block");
		//FIXME add retry limit and timeout
		synchronized (mutex) {
			while(message == null){
				Log.v(tag,"waiting");
				try {
					mutex.wait();
				} catch (InterruptedException e) {
					Log.wtf(tag,"Waiting interupted",e);
					return;
				}	
			}
		}
	}
	
	private  ChatManager setup() throws XMPPException  {
		Connection con = new XMPPConnection(this.chatServer);
		con.connect();
		con.login(this.verifierId, this.password);
		return con.getChatManager();
	}
	
	private String makeaddress(){
		return proverId +"@"+chatServer;
	}

}
