package org.middleheaven.logging.writters;

import java.util.HashSet;
import java.util.Set;

import org.middleheaven.logging.LogBookWriter;

public abstract class AbstractWriterAttachable extends LogBookWriter implements AttachableWriter {


    protected final Set<LogBookWriter> writers = new HashSet<LogBookWriter>();

    public void attachWriter(LogBookWriter writer) {
        writers.add(writer);
    }

    public void removeWriter(LogBookWriter writer) {
        writers.remove(writer);
    }

}
