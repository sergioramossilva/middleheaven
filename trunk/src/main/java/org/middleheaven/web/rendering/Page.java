package org.middleheaven.web.rendering;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;

public interface Page {

	public static final String PAGE = "_page";
	
	void setRequest(HttpServletRequest request);

	void writePage(PrintWriter writer);

	int getContentLength();

	String getBody();

	String getHead();

	String getTitle();

}
