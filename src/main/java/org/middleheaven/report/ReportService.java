package org.middleheaven.report;

import java.util.Map;

public interface ReportService {

	
	public Report generateReport(String reportName, Map<String,Object> parameters) throws ReportException;
	public void registerReportGenerator( String reportName, ReportGenerator generator);
}
