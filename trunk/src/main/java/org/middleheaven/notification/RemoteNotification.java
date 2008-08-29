package org.middleheaven.notification;

import java.io.Serializable;

public class RemoteNotification extends Notification implements Serializable{

	public RemoteNotification(Notification other) {
		super("remote."+other.type, other.timestamp, other.sequenceNumber);
		this.setUserObject(other.getUserObject());
	}

}
