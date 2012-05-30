package edu.jhu.isi.hms.visavis;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManager;
import org.jivesoftware.smack.Connection;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;

import android.util.Log;

public class VerifierProtocol {
	public final String  verifier;

	public VerifierProtocol(String verifier) {
		this.verifier = verifier;
	}
	
	public StdTestResult verify(){
		
		try{
			ChatManager c = setup();

			Chat newChat = c.createChat("ASd",
					new MessageListener() {
						public void processMessage(Chat chat, Message message) {
							Log.v("STD", message.getBody());
						}
					});
		} catch (XMPPException e) {
			Log.e("STD","Unable to set up connection",e);
		}
//		try {
//			Log.v("STD", "sending message");
//		//	newChat.sendMessage("test");
//			Log.v("STD", "message sent");
//		} catch (XMPPException e) {
//			Log.e("STD", "cannot send message",e);
//		}
		return null;
	}
	
	public ChatManager setup() throws XMPPException  {
		Connection con = new XMPPConnection("128.220.247.239");
		con.connect();
		con.login("christina", "password");
		return con.getChatManager();
	}
}
