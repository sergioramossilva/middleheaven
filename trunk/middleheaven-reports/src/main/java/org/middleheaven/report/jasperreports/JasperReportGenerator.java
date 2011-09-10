package org.middleheaven.report.jasperreports;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;

import org.middleheaven.io.ManagedIOException;
import org.middleheaven.io.repository.ManagedFile;
import org.middleheaven.report.Report;
import org.middleheaven.report.ReportContext;
import org.middleheaven.report.ReportException;
import org.middleheaven.report.ReportGenerator;

/**
 * Extension ReportGenerator the uses Jasper Reports facilities
 */
public abstract class JasperReportGenerator implements ReportGenerator {

	ManagedFile reportDefinitionFile;
	public JasperReportGenerator(ManagedFile reportDefinitionFile){
		this.reportDefinitionFile = reportDefinitionFile;
	}
	
	@Override
	public Report generateReport(ReportContext context) throws ReportException {
		
		JRDataSource dataSource = generateDataSource(context);
		JasperReport jasperReport;
		try {
			jasperReport = JasperCompileManager.compileReport(reportDefinitionFile.getContent().getInputStream());
			JasperPrint jrPrint = JasperFillManager.fillReport(jasperReport, context.getParameters(), dataSource);
			
			return new Report(context.getReportName(),jrPrint);
		} catch (ManagedIOException e) {
			throw new ReportException(e);
		} catch (JRException e) {
			throw new ReportException(e);
		}
		
	
	}

	public abstract JRDataSource generateDataSource(ReportContext context )  throws ReportException;
}
