package org.middleheaven.report;


/**
 * General contract for a class that generates reports.
 * 
 * This contract is to be used in order to extend the report toolbox
 */
public interface ReportGenerator {

	public Report generateReport(ReportContext context) throws ReportException;
}
