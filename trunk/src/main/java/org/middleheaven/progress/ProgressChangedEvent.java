package org.middleheaven.progress;

public class ProgressChangedEvent {

    private Progress p;
    public ProgressChangedEvent(Progress p){
        this.p = p;
    }
    
    public Progress getProgress(){
        return p;
    }
}
