package org.middleheaven.logging.writters;

public class EmailSendingTriggerMessage implements EmailSendingTrigger {

    protected String message;
    public EmailSendingTriggerMessage(String message){
        this.message = message;
    }
    
    public String toString(){
        return this.message;
    }
}
