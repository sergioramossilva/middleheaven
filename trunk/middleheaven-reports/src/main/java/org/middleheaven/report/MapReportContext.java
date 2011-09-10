package org.middleheaven.report;

import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

public class MapReportContext implements ReportContext {

	private Map<String, Object> parameters = new TreeMap<String, Object>();
	private String reportName;
	
	public MapReportContext(String reportName, Map<String, Object> parameters){
		this.parameters.putAll(parameters);
	}
	
	@Override
	public <P> P getParameter(String name, Class<P> type) {
		return type.cast(parameters.get(name));
	}

	@Override
	public String getReportName() {
		return reportName;
	}

	@Override
	public void setParameter(String name, Object value) {
		parameters.put(name, value);
	}

	@Override
	public Map<String, Object> getParameters() {
		return Collections.unmodifiableMap(parameters);
	}

}
