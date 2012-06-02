package edu.jhu.isi.hms.visavis;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class StatusActivity extends Activity {

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.status);
	    ListView lv = (ListView) this.findViewById(R.id.listView1);
	    //FIXME handle null
	    StdTestResult t = (StdTestResult) getIntent().getSerializableExtra(VisavisActivity.data_tag);
	    if(null == t){
	    	Log.wtf(this.getClass().getName(), "Serialized STD result is null");
	    }
	    //lv.setAdapter(new ArrayAdapter<StdTest>(this, R.layout.std_tests,s));
	    lv.setAdapter(new StdTestAdapter(this,  R.layout.std_tests, t.tests));
	}

}
