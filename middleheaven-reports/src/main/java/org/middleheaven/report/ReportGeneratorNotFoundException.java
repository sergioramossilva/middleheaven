
package org.middleheaven.report;

public class ReportGeneratorNotFoundException extends ReportException {

	public ReportGeneratorNotFoundException(String reportName) {
		super("No generator was found for report '" + reportName + "'");
	}

}
