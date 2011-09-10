package org.middleheaven.logging.writters;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

import org.middleheaven.logging.LogBookWriter;
import org.middleheaven.logging.LogWritingException;
import org.middleheaven.logging.LogWritingIOExcepiton;
import org.middleheaven.logging.LoggingEvent;


/**
 * @author  Sergio M. M. Taborda 
 */
public abstract class StreamLogBookWriter extends LogBookWriter implements FormatableLogWriter{

    protected OutputStream out;
    protected LogFormat format = new DirectLogFormat();

    protected class DirectLogFormat implements LogFormat{

        public void setWriter(LogBookWriter writer) {}

        public void format(LoggingEvent event, OutputStream stream) {
            PrintStream out = new PrintStream(stream);
            out.println("[" + event.getLevel().toString() + "]" + formatToText(event));
            if (event.hasThrowable()){
                out.println("----");
                event.getThrowable().printStackTrace(System.out);
                out.println("----");
                out.println("");
            }
        }

        public String getContentType() {
            return "text/plain";
        }

        public void writerHeader(OutputStream stream) {}

        public void writerFooter(OutputStream stream) {}

    }

    @Override public void write(LoggingEvent event) throws LogWritingException {
        this.format.format(event,out);
        try {
            out.flush();
        } catch (IOException e) {
            throw new LogWritingIOExcepiton(e);
        }
    }

    public void setLogFormat(LogFormat format) {
        this.format = format;
        this.format.setWriter(this);
    }

    public LogFormat getLogFormat() {
        return format;
    }

}
