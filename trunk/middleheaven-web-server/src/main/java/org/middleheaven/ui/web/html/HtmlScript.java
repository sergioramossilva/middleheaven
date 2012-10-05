/**
 * 
 */
package org.middleheaven.ui.web.html;

import java.io.IOException;

/**
 * 
 */
public class HtmlScript implements Appendable , CharSequence{

	private StringBuilder content = new StringBuilder();
	private String type;
	private String name;
	private String src = null;


	public HtmlScript (String name, String type){
		this.type = type;
		this.name = name;
	}

	public HtmlScript (String name ){
		this(name, "text/javascript");
	}
	
	public String toString(){
		return this.content.toString();
	}
	
	/**
	 * @param string
	 */
	public HtmlScript setRelativeSource(String src) {
		this.src= src;
		return this;
	}
	
	public String getRelativeSource(){
		return src;
	}
	
	public boolean isSource(){
		return src != null;
	}
	
	public int hashCode(){
		return name.hashCode();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object obj) {
		return (obj instanceof HtmlScript) && equalsHtmlScript((HtmlScript)obj); 
	}


	private boolean equalsHtmlScript(HtmlScript other) {
		return this.name.equals(other.name);
	}

	/**
	 * Obtains {@link String}.
	 * @return the name
	 */
	public String getName() {
		return name;
	}


	/**
	 * Obtains {@link String}.
	 * @return the language
	 */
	public String getType() {
		return type;
	}



	/**
	 * {@inheritDoc}
	 */
	@Override
	public char charAt(int index) {
		return content.charAt(index);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int length() {
		return content.length();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public CharSequence subSequence(int arg0, int arg1) {
		return content.subSequence(arg0, arg1);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Appendable append(CharSequence arg0) throws IOException {
		content.append(arg0);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Appendable append(char arg0) throws IOException {
		content.append(arg0);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Appendable append(CharSequence arg0, int arg1, int arg2)
			throws IOException {
		content.append(arg0, arg1, arg2);
		return this;
	}



}
