package edu.jhu.isi.hms.visavis;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class ErrorActivity extends Activity {
	
	static String error;
	public static void report (String error, Activity activity){
		Log.e("Error",error);
		ErrorActivity.error=error;
		
		Intent i = new Intent(activity.getApplicationContext(), ErrorActivity.class);
		activity.startActivity(i);
	}
	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		System.out.println(savedInstanceState);

		setContentView(R.layout.error);
		TextView out = (TextView)findViewById(R.id.errorReport);
		out.setText(error);
	}
}
