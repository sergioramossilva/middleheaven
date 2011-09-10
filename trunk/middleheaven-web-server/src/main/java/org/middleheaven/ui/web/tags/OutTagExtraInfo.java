package org.middleheaven.ui.web.tags;

import javax.servlet.jsp.tagext.TagData;
import javax.servlet.jsp.tagext.TagExtraInfo;
import javax.servlet.jsp.tagext.VariableInfo;

public class OutTagExtraInfo extends TagExtraInfo {

	
	public VariableInfo[] getVariableInfo(TagData data){
		
		String exportedName = (String) data.getAttribute("exportTo");
		
		if (exportedName==null){
			return new VariableInfo[0];
		}
		
		VariableInfo[] rc = new VariableInfo[1];

		
		rc[0] = new VariableInfo(exportedName, "java.lang.String", true , VariableInfo.AT_BEGIN);

	
		return rc;
	}
}
