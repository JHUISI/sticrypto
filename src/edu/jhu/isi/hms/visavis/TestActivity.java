package edu.jhu.isi.hms.visavis;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;


public class TestActivity extends Activity {

	private Protocal protocal;
	private final String tag= Protocal.class.getName();
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    Log.i(tag,"running test");
	    super.onCreate(savedInstanceState);
	    
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
	    setContentView(R.layout.share);
		ImageView out=(ImageView) findViewById(R.id.qrCode);
		out.setImageBitmap(qrCodeBmp);
		
		//create the scan button
		final Button scan = (Button) findViewById(R.id.scanButton);
		scan.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
					//TODO check that a suitable app is installed, or use integrated code
					Intent intent = new Intent("com.google.zxing.client.android.SCAN");
					intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
					startActivityForResult(intent, 0);
				}
		});
		
		Resources res= getResources();
		String username="bob";
		String password="password";
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
		return ("bob");
	}
	
}
