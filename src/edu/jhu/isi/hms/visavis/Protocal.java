package edu.jhu.isi.hms.visavis;

import org.jivesoftware.smack.Connection;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.ConnectionConfiguration.SecurityMode;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

public class Protocal extends AsyncTask<Void, Void, Void> {
	final String username;
	final String password;
	final String chatServerIp;
	final String chatServerHost;
	private Activity activity;
	private Connection con;
	private final String tag= Protocal.class.getName();
	private boolean connectedToServer=false;
	private String verifyPending=null;
	
	public Protocal(String username, String password, String chatServerIp, String chatServerHost, Activity activity){
		this.username=username;
		this.password=password;
		this.chatServerIp=chatServerIp;
		this.chatServerHost=chatServerHost;
		this.activity=activity;
	}

	ProverTask pt;
	public void verify(String proverId){
		if(connectedToServer){
			VerifierTask vt=new VerifierTask(proverId, chatServerHost,con, activity);
			vt.execute();
		}else{
			verifyPending=proverId;
		}
	}
	protected  Void doInBackground(Void... params)  {
//		Connect to server
//		TODO handle down server
		
		ConnectionConfiguration config = new ConnectionConfiguration(this.chatServerIp, 5222, this.chatServerHost);
		config.setKeystoreType("bks"); 
		config.setTruststoreType("bks");
		config.setTruststorePath("/system/etc/security/cacerts.bks");
		config.setSecurityMode(SecurityMode.disabled);//TODO, debug code, remove
		Log.wtf(tag, "Chat security disabled");
		Log.i(tag, "attempting to connect to server");
		con = new XMPPConnection(config);
		try {
			con.connect();
			con.login(this.username, this.password);
			Log.i(tag,"Connected to server");
		} catch (XMPPException e) {
			Log.e(tag,"Error loging in to server:"+e.getMessage());
			ErrorActivity.report("Cannot log into server\n\n"+e.getMessage(), activity);
		}
		
		
		connectedToServer=true;
		
		//If we tried running verify before we were connected, we need to do so now
		if(verifyPending!=null){
			verify(verifyPending);
			verifyPending=null;
		}
		
		//run prover
		pt =new ProverTask(con);
		pt.execute();
		return null;
	}

}
