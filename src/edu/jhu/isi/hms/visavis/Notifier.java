package edu.jhu.isi.hms.visavis;

import org.jivesoftware.smack.packet.Message;

import android.util.Log;

public  class Notifier<T> {
	private  T t;

	public synchronized void messageArived(final T t){
		this.t = t;
		this.notifyAll();
	}
	public synchronized T waitForMessage(){
		try{
			//fixme add timeouts and retry limits
			while(null == t){
				try {
					wait();
				} catch (InterruptedException e) {
					Log.wtf(Notifier.class.getName(),"Interupted well waiting for message",e );
				}
			}
			return t;
		} finally 
		{
			t = null; 
		}
	}
}
