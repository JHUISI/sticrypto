package edu.jhu.isi.hms.visavis;

import java.net.URL;
import java.util.List;

public class StdTestResult {
	
	public StdTestResult(){}
	public StdTestResult(String name, URL photo, List<StdTest> tests) {
		this.name = name;
		this.photo = photo;
		this.tests = tests;
	}
	public String name;
	public URL photo;
	public List<StdTest> tests; 
}
