/*
 * Created on 02/08/2005
 * @percent 100
 */
package org.middleheaven.email;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.activation.DataSource;

/**
 * A DataSource for plain text message body
 *
 * @author Sérgio M.M. Taborda
 */
public class PlainTextDataSource implements DataSource{

    
    protected String text;
    protected String name;
    
    public PlainTextDataSource(){
        text = "";
    }
    
    /**
     * @param text - text to insert into the message
     * @param name -  referecence name for the text
     */
    public PlainTextDataSource(String text,String name){
        this.text = text;
        this.name = name;
    }
    
    public PlainTextDataSource(StringBuffer textBuffer){
        this.text = textBuffer.toString();
    }
    
    public void setText(String text){
        this.text = text;
    }
    
    public void setName(String name){
        this.name = name;
    }
    /** 
     * @see javax.activation.DataSource#getContentType()
     */
    public String getContentType() {
        return "text/plain";
    }

    /** 
     * @see javax.activation.DataSource#getInputStream()
     */
    public InputStream getInputStream() throws IOException {
        return new ByteArrayInputStream(text.getBytes());
    }

    /** 
     * @see javax.activation.DataSource#getName()
     */
    public String getName() {
        return name;
    }

    /** 
     * @see javax.activation.DataSource#getOutputStream()
     */
    public OutputStream getOutputStream() throws IOException {
        throw new UnsupportedOperationException();
    }

}
