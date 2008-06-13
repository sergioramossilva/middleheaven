package org.middleheaven.chart;

import java.util.Map;

public interface ChartContext {

	public String getChartName();
	public <P> P getParameter(String name, Class<P> type);
	public void setParameter(String name, Object value);
	public Map<String,Object> getParameters();
	
}
