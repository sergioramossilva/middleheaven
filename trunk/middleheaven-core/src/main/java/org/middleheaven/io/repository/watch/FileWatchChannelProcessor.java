package org.middleheaven.io.repository.watch;

import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;


/**
 * Handles {@link WatchEventChannel} events using a given {@link FileChangeStrategy}. 
 */
public class FileWatchChannelProcessor {


	private final Set<WatchEventChannel> channels = new CopyOnWriteArraySet<WatchEventChannel>();

	private final FileChangeStrategy stategy;

	public FileWatchChannelProcessor(FileChangeStrategy fileChangeStratey){
		this.stategy = fileChangeStratey;
	}

	public void add(WatchEventChannel channel){
		channels.add(channel);
	}


	public void start (){
		for (Iterator<WatchEventChannel> it = channels.iterator(); it.hasNext();){
			new ProcessorThread(it.next()).start();
		}
	}

	private class ProcessorThread extends Thread {

		private WatchEventChannel channel;

		ProcessorThread (WatchEventChannel channel){
			this.setDaemon(true);
			this.setName("Processing Thread for watchable " + channel.watchable());
			this.channel = channel;
		}

		public void run (){
			while (true) {
				try {
					if (channel.isValid()) {
						
						WatchEvent event = channel.pollEvents().take();
						
						stategy.onChange(event);
					} else {
						break;
					}
				} catch (InterruptedException e) {
					break;
				}

			}

		}
	}

}