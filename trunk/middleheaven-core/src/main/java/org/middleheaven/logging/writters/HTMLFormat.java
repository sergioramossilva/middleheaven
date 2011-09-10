package org.middleheaven.logging.writters;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.middleheaven.logging.LogBookWriter;
import org.middleheaven.logging.LoggingEvent;
import org.middleheaven.util.StringUtils;


public class HTMLFormat implements LogFormat {

    protected static final SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm");
    public void setWriter(LogBookWriter writer) {}

    public void writerHeader(OutputStream stream) {
        PrintWriter writer = new PrintWriter(stream);
        writer.println("<HTML>");
        writer.println("<BODY>");
        writer.println("<TABLE>");
        writer.println("<TR>");
        writer.println("<TH>Time</TH>");
        writer.println("<TH>Level</TH>");
        writer.println("<TH>Message</TH>");
        writer.println("</TR>");
        writer.flush();
    }

    public void format(LoggingEvent event, OutputStream stream) {
        PrintWriter writer = new PrintWriter(stream);
        writer.println("<TR>");
        writer.println("<TD align='center' class='event'>");
        writer.println(format.format(new Date(event.getTime())));
        writer.println("</TD>");
        writer.println("<TD align='center' class='event'>");
        writer.println(event.getLevel().toString());
        writer.println("</TD>");
        writer.println("<TD align='justified' class='event'>");
        writer.println(event.getMessage().toString());
        writer.println("</TD>");
        writer.println("</TR>");

        if (event.hasThrowable()){
            writer.println("<TR>");
            writer.println("<TD colspan='3' class='error'>");
            writer.println(StringUtils.getStackStrace(event.getThrowable()));
            writer.println("</TD>");
            writer.println("</TR>");
        }
        writer.flush();
    }

    public void writerFooter(OutputStream stream) {
        PrintWriter writer = new PrintWriter(stream);
        writer.println("</TABLE>");
        writer.println("</BODY>");
        writer.println("</HTML>");
        writer.flush();
    }

    public String getContentType() {
        return "text/html";
    }


}
