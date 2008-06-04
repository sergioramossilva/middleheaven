/*
 * Created on 2006/09/07
 *
 */
package org.middleheaven.io.xml;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;
import org.middleheaven.io.ManagedIOException;
import org.middleheaven.io.repository.FileNotFoundManagedException;
import org.middleheaven.io.repository.ManagedFile;

public abstract class XMLObjectContructor<T> {

    private T object;
    protected URL url;
    
    protected T getConstructedObject(){
        return object;
    }
    
    protected void setConstructedObject(T object){
        this.object = object;
    }
    
    protected void constructFrom(CharSequence text) throws ManagedIOException , XMLException {
        if (text ==null || text.toString().trim().length()==0){
            throw new IllegalArgumentException("Parameter 'text' cannot be null nor empty");
        }
       constructFrom(new ByteArrayInputStream(text.toString().getBytes()));
    }
    
    protected void constructFrom(File file) throws FileNotFoundManagedException, ManagedIOException , XMLException {
        try {
            constructFrom(new FileInputStream(file));
        } catch (FileNotFoundException e) {
            throw new FileNotFoundManagedException(file);
        }
    }
    
    protected void constructFrom(URL sourceURL) throws ManagedIOException , XMLException{
        try {
            url = sourceURL;
            constructFrom(sourceURL.openStream());
        } catch (IOException e) {
            throw ManagedIOException.manage(e);
        }
    }
    
    protected void constructFrom(ManagedFile file){
       url = file.getURL();
       constructFrom(file.getContent().getInputStream());
    }
    
    protected void constructFrom(InputStream sourceStream) throws ManagedIOException , XMLException{
        SAXReader reader = new SAXReader();

        try {
            Document doc = reader.read(sourceStream);
            constructFrom(doc);
        } catch (DocumentException e) {
            throw new XMLException(e);
        }
    }
    
    protected abstract void constructFrom(Document document) throws ManagedIOException , XMLException;
}
