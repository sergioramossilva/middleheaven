package org.middleheaven.report;

import java.util.Map;
import java.util.TreeMap;

public class CentralReportService implements ReportService{

	Map<String ,ReportGenerator> generators = new TreeMap<String ,ReportGenerator >();
	
	@Override
	public Report generateReport(String reportName, Map<String, Object> parameters) throws ReportException {
		ReportGenerator generator = generators.get(reportName);
		if (generator == null){
			 throw new ReportGeneratorNotFoundException(reportName);
		}
		
		MapReportContext context = new MapReportContext(reportName, parameters);
		return generator.generateReport(context);
	}

	@Override
	public void registerReportGenerator(String reportName,ReportGenerator generator) {
		generators.put(reportName, generator);
	}

}
