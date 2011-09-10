package org.middleheaven.logging.writters;

import org.middleheaven.logging.LogBookWriter;

/**
 * Identifies a Writer that can contain other writers 
 * The real writing operation is delegated to the children writers
 */
public interface AttachableWriter {


    public void attachWriter(LogBookWriter writer);
    public void removeWriter(LogBookWriter writer);

}
