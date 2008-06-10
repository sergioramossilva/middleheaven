package org.middleheaven.email;


/**
 * Adapter to send email in HTML format
 * 
 * @author Sérgio M.M. Taborda
 */
public class HTMLDataSource extends PlainTextDataSource  {
 
    
    public HTMLDataSource() {
        super();
    }
    
	public HTMLDataSource(String html,String name) {
        super(html,name);
	}
	 
	public HTMLDataSource(StringBuffer htmlbuffer) {
       super(htmlbuffer); 
	}
	 
	/**
	 *@see br.com.gnk.fw.io.Streamable#getContentLength()
	 */
	public int getContentLength() {
		return super.text.getBytes().length;
	}

    /** 
     * @see javax.activation.DataSource#getContentType()
     */
    public String getContentType() {
        return "text/html";
    }

	 
}
 
