package org.middleheaven.notification.jms;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import javax.jms.QueueSender;
import javax.jms.Session;
import javax.jms.TopicPublisher;

import org.middleheaven.notification.BroadcastAdapter;
import org.middleheaven.notification.InterceptorChain;
import org.middleheaven.notification.Notification;
import org.middleheaven.notification.NotificationBroadcastIntercepter;
import org.middleheaven.notification.NotificationBroadcaster;
import org.middleheaven.notification.RemoteNotification;
import org.middleheaven.notification.RemoteNotificationException;


/**
 * 
 * Add an intercepter that captures <code>RemoteNotification</code> 
 * and sends then as a JMS message to specified <code>MessageProducer</code>
 */
public class JMSSenderAdapter implements BroadcastAdapter, NotificationBroadcastIntercepter{

	GenericMesssageSender sender;
	Session session;
	
	public JMSSenderAdapter(Session session , QueueSender sender){
		this.sender = new QueueMesssageSender(sender);
		this.session = session;
	}
	
	public JMSSenderAdapter(Session session ,TopicPublisher sender){
		this.sender = new TopicMesssageSender(sender);
		this.session = session;
	}
	
	@Override
	public void adapt(NotificationBroadcaster broadcaster) {
		broadcaster.addFilter(this);
	}

	@Override
	public void onIntercept(Notification notification, InterceptorChain chain) {
		if (notification instanceof RemoteNotification){
			try {
				ObjectMessage message = session.createObjectMessage();
				message.setObject((RemoteNotification)notification);
				this.sender.send(message);
			} catch (JMSException e) {
				throw new RemoteNotificationException(e);
			}
		} else {
			chain.doChain(notification);
		}
	}

	private static interface GenericMesssageSender {
		public void send(Message message) throws JMSException;
	}
	
	private static class QueueMesssageSender implements GenericMesssageSender{
		QueueSender sender;
		public QueueMesssageSender(QueueSender sender) {
			this.sender = sender;
		}

		public void send(Message message) throws JMSException{
			this.sender.send(message);
		}
	}
	
	private static class TopicMesssageSender implements GenericMesssageSender{
		private TopicPublisher sender;

		public TopicMesssageSender(TopicPublisher sender) {
			this.sender = sender;
		}

		public void send(Message message) throws JMSException{
			this.sender.publish(message);
		}
	}
	
}
