package org.middleheaven.report;

import java.util.Map;

/**
 * Central report service. 
 * 
 * The report service acts as a {@code ReportGenerator} registry
 * and a  report factory.
 */
public interface ReportService {

	
	public Report generateReport(String reportName, Map<String,Object> parameters) throws ReportException;
	public void registerReportGenerator( String reportName, ReportGenerator generator);
}
