package org.middleheaven.logging.writters;

import java.io.OutputStream;

import org.middleheaven.logging.LogBookWriter;
import org.middleheaven.logging.LogWritingIOExcepiton;
import org.middleheaven.logging.LoggingEvent;

public interface LogFormat {

    public void setWriter(LogBookWriter writer);
    public void writerHeader(OutputStream stream) throws LogWritingIOExcepiton;
    public void format(LoggingEvent event, OutputStream stream) throws LogWritingIOExcepiton;
    public void writerFooter(OutputStream stream) throws LogWritingIOExcepiton;
    public String getContentType();
}
