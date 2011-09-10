/*
 * Created on 2006/09/07
 *
 */
package org.middleheaven.io.xml;

public class XMLAttributemissingException extends XMLException {

    protected XMLAttributemissingException(String attributeName, String elementName) {
        super("Required attribute " + attributeName + " for element " + elementName + " is missing");
    }

}
