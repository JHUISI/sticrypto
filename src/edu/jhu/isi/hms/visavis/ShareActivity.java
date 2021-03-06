package edu.jhu.isi.hms.visavis;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;


public class ShareActivity extends Activity {

	private Protocal protocal;
	/** Called when the activity is first created. */
	
	final String scannerIntent="com.google.zxing.client.android.SCAN";
	private final String tag= Protocal.class.getName();
	@Override
	public void onResume(){
		super.onResume();
		
		//check for scanner app
	    Intent scanner = new Intent(scannerIntent);
	    PackageManager pkg=getPackageManager();
	    List scannerApps=pkg.queryIntentActivities(scanner, PackageManager.MATCH_DEFAULT_ONLY);
	    if(scannerApps.size()==0){
	    	//If no app is installed, provide link to market, and wait for the button to be pushed
		    final Notifier<Integer> pauser= new Notifier<Integer>();
	    	setContentView(R.layout.scanner_download);
	    	final Button install = (Button) findViewById(R.id.readerInstall);
	    	install.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					Intent i = new Intent(Intent.ACTION_VIEW);
				    i.setData(Uri.parse("market://details?id=com.google.zxing.client.android"));
				    i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
				    startActivity(i);
				}
			});
	    	return;
	    }
	    
	    //set layout based on orientation 
	    WindowManager winMan = (WindowManager) getBaseContext().getSystemService(Context.WINDOW_SERVICE);
	    int orientation = winMan.getDefaultDisplay().getOrientation();
	    if (orientation == 0)
	    	setContentView(R.layout.share);
	    else if (orientation == 1)
	    	setContentView(R.layout.share_landscape);
	    else
	    	setContentView(R.layout.share);
	    	Log.e(tag,"Unknown phone orientation: "+orientation);
	    	
	    //set the qrCode img
		QRCodeWriter writer=new QRCodeWriter();
		BitMatrix qrCode=null;
		String data=dataForQrCode();
		try {
			qrCode=writer.encode(data, BarcodeFormat.QR_CODE, 0, 0);
		} catch (WriterException e) {
			e.printStackTrace();
			return;
		}
		int width=qrCode.getWidth();
		int height=qrCode.getHeight();
		Bitmap qrCodeBmp=Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
		for (int y=0; y<height; y++){
			for (int x=0; x<width; x++){
				qrCodeBmp.setPixel(x, y, qrCode.get(x, y)?0:0xFFFFFFFF);
			}
		}
		Display display = getWindowManager().getDefaultDisplay();
		int DispWidth=display.getWidth();
		int scaleFactor=DispWidth/qrCodeBmp.getHeight();
		qrCodeBmp=Bitmap.createScaledBitmap(qrCodeBmp, qrCodeBmp.getWidth()*scaleFactor, qrCodeBmp.getHeight()*scaleFactor, false);
		ImageView out=(ImageView) findViewById(R.id.qrCode);
		out.setImageBitmap(qrCodeBmp);
		
		//create the scan button
		final Button scan = (Button) findViewById(R.id.scanButton);
		scan.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
					//TODO check that a suitable app is installed, or use integrated code
					Intent intent = new Intent(scannerIntent);
					intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
					startActivityForResult(intent, 0);
				}
		});
		
		//Start protocol in the background
		Resources res= getResources();
		String username=res.getString(R.string.username);
		String password=res.getString(R.string.password);
		String chatServerIp= res.getString(R.string.XMPPServer);
		String chatServerHost= res.getString(R.string.XMPPVirtualhost);
		protocal=new Protocal(username, password, chatServerIp,chatServerHost, this);
		protocal.execute();
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
		if (requestCode == 0) {//qrCode scan
			if (resultCode == RESULT_OK) {
				protocal.verify(intent.getStringExtra("SCAN_RESULT"));
			} else {
				// Handle cancel
			}
		}
	}
	
	protected String dataForQrCode(){
		Resources res= getResources();
		return (res.getString(R.string.username));
	}
	
}
