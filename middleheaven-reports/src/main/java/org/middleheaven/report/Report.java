package org.middleheaven.report;

import java.io.Serializable;

public class Report implements Serializable{

	
	private String name;
	private Serializable reportObject;
	
	
	public Report(String name, Serializable reportObject) {
		super();
		this.name = name;
		this.reportObject = reportObject;
	}
	
	public String getName() {
		return name;
	}
	public Serializable getReportObject() {
		return reportObject;
	}
}
