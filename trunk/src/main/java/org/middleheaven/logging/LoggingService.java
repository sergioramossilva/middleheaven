
package org.middleheaven.logging;

import org.middleheaven.logging.config.LoggingConfiguration;



public interface LoggingService {

    public LogBook getLogBook(String bookName);
    
    public void addBook(LogBook book);
    public void removeBook(LogBook book);
    
    public LoggingConfiguration getConfiguration();
}
