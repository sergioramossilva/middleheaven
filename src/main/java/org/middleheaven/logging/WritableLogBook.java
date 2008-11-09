
package org.middleheaven.logging;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;


public class WritableLogBook extends LogBook {

    protected final Set<LogBookWriter> writers = new CopyOnWriteArraySet<LogBookWriter>();

    public WritableLogBook(String name, LoggingLevel level) {
        super(name, level);
    }

    public WritableLogBook addWriter(LogBookWriter writer){
        // add the writer only if its level could pass the books level
        if (writer.getLevel().canLog(level)){
            writers.add(writer);
        }
        return this;
    }

    public void log(LoggingEvent event){
        for (LogBookWriter writer : writers ){
            try {
                writer.log(event);
            }catch (LogWritingException e){
                // do not stop the other writers to do theirs job
                continue;
            }
        }

    }

}
