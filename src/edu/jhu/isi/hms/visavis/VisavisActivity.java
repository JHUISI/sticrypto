package edu.jhu.isi.hms.visavis;

import java.util.Date;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

public class VisavisActivity extends Activity {
	 VerifierTask vt;
	 public static  final String data_tag = "StdTestResult";
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		View v = findViewById(R.id.main);
		
		Intent i = new Intent(v.getContext(), ShareActivity.class);
		startActivity(i);

	}
}