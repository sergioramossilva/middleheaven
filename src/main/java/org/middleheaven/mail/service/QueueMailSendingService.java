package org.middleheaven.mail.service;

import java.util.Comparator;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;

import org.middleheaven.mail.MailAsynchrounsCallbak;
import org.middleheaven.mail.MailAsynchrounsCallbakDecorator;
import org.middleheaven.mail.MailException;
import org.middleheaven.mail.MailMessage;
import org.middleheaven.mail.MailSendingService;
import org.middleheaven.mail.MailSendingServiceDecorator;

public class QueueMailSendingService extends MailSendingServiceDecorator {

	BlockingQueue<QueuedMessage> queue = new PriorityBlockingQueue<QueuedMessage>(100,new Comparator<QueuedMessage>(){

		@Override
		public int compare(QueuedMessage m1, QueuedMessage m2) {
			return m1.message.getPriority().compareTo(m2.message.getPriority());
		}

	});

	private MailSenderThread mailSenderThread;

	public QueueMailSendingService(MailSendingService original) {
		super(original);

		MailSenderThread mailSenderThread = new MailSenderThread();
		mailSenderThread.start();
	}

	public QueueMailSendingService(MailSendingService original, BlockingQueue<QueuedMessage> queue) {
		this(original);
		setQueue(queue);
	}

	public final void setQueue(BlockingQueue<QueuedMessage> queue){
		BlockingQueue<QueuedMessage> oldQueue = this.queue; 
		this.queue = queue;
		oldQueue.drainTo(queue);
	}

	@Override
	public void send(MailMessage message) throws MailException {
		send(message, null);
	}

	@Override
	public void send(MailMessage message, MailAsynchrounsCallbak callback)throws MailException {
		queue.offer(new QueuedMessage(message,callback));
	}


	private class MailSenderThread extends Thread {

		public MailSenderThread() {
			super();
		}

		public void run(){
			while (true){
				try{
					QueuedMessage qm = queue.take();
					try{
						getOriginal().send(qm.getMessage(), new MailAsynchrounsCallbakDecorator(qm.getCallback()));

					} catch (Exception e){
						qm.getCallback().onSent(qm.getMessage(), false);
					}
				} catch (InterruptedException e){
					break;
				}
			}
		}
	}

	private static class QueuedMessage{
		MailMessage message;
		MailAsynchrounsCallbak callback;

		public QueuedMessage(MailMessage message,
				MailAsynchrounsCallbak callback) {
			this.message = message;
			this.callback = callback;
		}

		public MailMessage getMessage() {
			return message;
		}

		public MailAsynchrounsCallbak getCallback() {
			return callback;
		}

	}

}
