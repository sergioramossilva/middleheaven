package org.middleheaven.report;


/**
 * General contract for a class that generates reports
 * 
 *
 */
public interface ReportGenerator {

	public Report generateReport(ReportContext context) throws ReportException;
}
