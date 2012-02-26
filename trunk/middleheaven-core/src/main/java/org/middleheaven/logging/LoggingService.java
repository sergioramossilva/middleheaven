
package org.middleheaven.logging;

import org.middleheaven.core.wiring.service.Service;
import org.middleheaven.logging.config.LoggingConfiguration;


@Service
public interface LoggingService {

    public LogBook getLogBook(String bookName);
    
    public void addBook(LogBook book);
    public void removeBook(LogBook book);
    
    public LoggingConfiguration getConfiguration();
}
