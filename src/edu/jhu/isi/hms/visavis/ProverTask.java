package edu.jhu.isi.hms.visavis;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManagerListener;
import org.jivesoftware.smack.Connection;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;

import android.os.AsyncTask;
import android.util.Log;

public class ProverTask extends AsyncTask<Void, Void, Void> {

	private final Connection connection;
//	private final Notifier<Message> mutex = new Notifier<Message>();
	private final String tag= ProverTask.class.getName();
	
	public ProverTask(Connection connection){
		this.connection=connection;
	}
	
	protected Void doInBackground(Void... params) {
		Log.i(tag,"running prover");
		try {
			Chat chat=waitForChat();
			prove(chat,mutex);
		} catch (XMPPException e) {
			Log.e(tag,"Error in waitForChat");
			e.printStackTrace();
		}
		return null;
	}

	final Notifier<Message> mutex = new Notifier<Message>();
	private  Chat waitForChat() throws XMPPException  {
		Log.i(tag,"waiting for chat");
		final Notifier<Chat> chatNotifier= new Notifier<Chat>();
		this.connection.getChatManager().addChatListener(new ChatManagerListener() {
			public void chatCreated(Chat chat, boolean createdLocally) {
				if(createdLocally){
					Log.v(tag,"chatCreatedLocal");
					return;
				} 
				Log.v(tag,"chatCreated");
				chat.addMessageListener(new MessageListener() {
					public void processMessage(Chat chat, Message message) {
						Log.v(tag,String.format("message recieved: %s", message.getBody()));
						mutex.messageArived(message);
					}
				});
				chatNotifier.messageArived(chat);
			}
		});
		return (chatNotifier.waitForMessage());
	}
	
	private void prove(Chat chat,Notifier<Message> mutex) throws XMPPException{
		Log.v(tag,Integer.toString(chat.getListeners().size()));
		Message m=mutex.waitForMessage();
		chat.sendMessage("You said:"+m.getBody());
		m=mutex.waitForMessage();
		chat.sendMessage("You said:"+m.getBody());
	}
	
}
