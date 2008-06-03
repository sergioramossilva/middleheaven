package org.middleheaven.report;

public interface ReportGenerator {

	
	public Report generateReport(ReportContext context) throws ReportException;
}
