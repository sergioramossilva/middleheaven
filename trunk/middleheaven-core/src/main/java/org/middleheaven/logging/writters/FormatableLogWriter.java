package org.middleheaven.logging.writters;

/**
 * @author  Sergio M. M. Taborda 
 */
public interface FormatableLogWriter {

 
    public void setLogFormat(LogFormat format);
    public LogFormat getLogFormat();
}
