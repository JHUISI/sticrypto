package edu.jhu.isi.hms.visavis;

import java.util.Date;

public class StdTest implements java.io.Serializable {
	public StdTest(String testname, Date testtime, Boolean isPositive) {
		this.testname = testname;
		this.testtime = testtime;
		this.isPositive = isPositive;
	}
	public String testname; 
	public Date testtime;
	public Boolean isPositive;
}
