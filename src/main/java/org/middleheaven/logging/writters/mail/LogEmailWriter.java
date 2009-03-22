package org.middleheaven.logging.writters.mail;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;


import org.middleheaven.core.services.ServiceRegistry;
import org.middleheaven.email.Email;
import org.middleheaven.email.EmailService;
import org.middleheaven.io.network.NetUtils;
import org.middleheaven.logging.LogBookWriter;
import org.middleheaven.logging.LogWritingIOExcepiton;
import org.middleheaven.logging.LoggingEvent;
import org.middleheaven.logging.config.LoggingConfiguration;
import org.middleheaven.logging.writters.FormatableLogWriter;
import org.middleheaven.logging.writters.LogFormat;
import org.middleheaven.util.StringUtils;

/**
 * TODO implements Trigger functionality
 * @author  Sergio M. M. Taborda
 */
public class LogEmailWriter extends LogBookWriter implements FormatableLogWriter {

    private List<LoggingEvent> buffer = new ArrayList<LoggingEvent>();
    private int bufferMax = 1;
    private String emailTo = null;
    private DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH'h'mm");

    private LogFormat format = new LogFormat(){

        public void setWriter(LogBookWriter writer) {}

        public void writerHeader(OutputStream stream) throws LogWritingIOExcepiton {
            PrintWriter message = new PrintWriter(new OutputStreamWriter(stream));
            message.println("Esta é uma mensagem automática, por favor não responda.");
        }

        public void format(LoggingEvent event, OutputStream stream) throws LogWritingIOExcepiton {
            PrintWriter message = new PrintWriter(new OutputStreamWriter(stream));
            message.append(dateFormat.format(new Date(event.getTime())));
            message.append(" - ");
            message.append(event.getLevel().toString());
            message.append(" - ");
            message.append(event.getMessage().toString());
            message.append("\n");
            if (event.hasThrowable()){
                message.append("\n");
                message.append("Stacktrace:");
                message.append("\n");
                message.append(StringUtils.getStackStrace(event.getThrowable()));
                message.append("\n");
            }

        }

        public void writerFooter(OutputStream stream) throws LogWritingIOExcepiton {}

        public String getContentType() {
            return "text/plain";
        }

    };



    public LogEmailWriter(){

    }

    public void config(Map<String,String> params, LoggingConfiguration configuration) {
        String s = (String)params.get("buffer.size");
        if (s!=null){
            bufferMax = Integer.parseInt(s.trim());
        }
        emailTo = (String)params.get("mail.to");

    }

    public void setLogFormat(LogFormat format) {
        this.format = format;

    }

    public LogFormat getLogFormat() {
        return format;
    }

    public void write(LoggingEvent event) {
        buffer.add(event);
        if (buffer.size() >= bufferMax ){
            this.sendEmail();
        }
    }

    protected void sendEmail()  {
        if (emailTo!=null && buffer.size()>0){

        	EmailService emailService = ServiceRegistry.getService(EmailService.class);
        	

            Email email = new Email();
            String[] mails = emailTo.trim().split(",");
            email.addToAdress(mails);

            email.setSubject("Log Events" + dateFormat.format(new Date()));
            email.setFrom("midleheave.logging@" + NetUtils.getPrimaryAddress().toString());

            ByteArrayOutputStream message  = new ByteArrayOutputStream();

            format.writerHeader(message);
            for (int i =0; i < buffer.size();i++){
                LoggingEvent event = (LoggingEvent)buffer.get(i);
                format.format(event,message);
            }
            format.writerFooter(message);

            email.setBody(message.toString());
            emailService.sendEmail(email);
            buffer.clear();
        }

    }


}
