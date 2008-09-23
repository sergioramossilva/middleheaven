package org.middleheaven.progress;

/**
 * Something that is connected to a progress. Normally a process.
 * 
 * @author Sérgio M.M. Taborda
 */
public interface Progressable {

    public Progress getProgress();
}
