package org.middleheaven.io.repository.machine;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.LinkedBlockingQueue;

import org.middleheaven.io.repository.ManagedFile;
import org.middleheaven.io.repository.ManagedFilePath;
import org.middleheaven.io.repository.watch.SimpleWatchEvent;
import org.middleheaven.io.repository.watch.StandardWatchEvent;
import org.middleheaven.io.repository.watch.WatchEvent;
import org.middleheaven.io.repository.watch.WatchEvent.Kind;
import org.middleheaven.io.repository.watch.WatchEventChannel;
import org.middleheaven.io.repository.watch.WatchService;
import org.middleheaven.io.repository.watch.Watchable;
import org.middleheaven.util.collections.CollectionUtils;
import org.middleheaven.util.collections.Walker;


/**
 * Watches a folder for updates.
 * The listeners are informed if changes are made to the folder content, namely :
 * <li>
 * <ul>The number of files changes</ul>
 * </li>
 */
public class FileIOWatchService implements WatchService {

	private final static int SIXTY_SECONDS = 60000;

	private Thread monitorThread;

	private long period = SIXTY_SECONDS;

	private final List<LegacyStrategyWatchEventChannel> channels = new CopyOnWriteArrayList<LegacyStrategyWatchEventChannel>();

	/**
	 * 
	 * Constructor.
	 * @param folder
	 */
	public FileIOWatchService (){
		monitorThread = new Thread(new Dog());
		monitorThread.setDaemon(true);
		monitorThread.setName("file watching dog");
	}

	public interface EventsReader {
		public Collection<WatchEvent>  readEvents(Set<Kind> kinds);
	}

	public class FileEventsReader implements EventsReader{

		public FileEventsReader (ManagedFile file){
			
		}

		@Override
		public Collection<WatchEvent> readEvents(Set<Kind> kinds) {
			// TODO Auto-generated method stub
			return null;
		}
		
	}

	public class FolderEventsReader implements EventsReader{

		private ManagedFile folder;
		private long lastTimeChecked = 0;
		private Set<ManagedFile> oldFiles= new HashSet<ManagedFile>();
		private boolean existed;

		public FolderEventsReader (ManagedFile folder){
			
			this.folder = folder;
			
			existed = folder.exists();
			
			if (folder.exists()){
				if (!folder.getType().isFolder()){
					throw new IllegalArgumentException("managedFile must correspond with a folder");
				}

				this.folder = folder;

				folder.each(new Walker<ManagedFile>(){

					@Override
					public void doWith(ManagedFile file) {
						oldFiles.add(file);
					}

				});
			} 
			
			lastTimeChecked = System.currentTimeMillis();
		}

		public Collection<WatchEvent>  readEvents(Set<Kind> kinds){
			
			final Collection<WatchEvent> events = new LinkedList<WatchEvent>();
			
			if (existed){
				if(folder.exists()){
					// continues to exist. monitor change in files.
					
					// the number of files can be the same but the files different

					final Set<ManagedFile> allFiles = new HashSet<ManagedFile>();
					final Set<ManagedFile> newFiles = new HashSet<ManagedFile>();
					
					folder.each(new Walker<ManagedFile>(){

						@Override
						public void doWith(ManagedFile object) {
							newFiles.add(object);
							allFiles.add(object);
						}

					});

					Collection<ManagedFile> common = CollectionUtils.intersect(newFiles, oldFiles);

					oldFiles.removeAll(common);
					newFiles.removeAll(common);

					// oldFiles contains now the deleted files.
					if (kinds.contains(StandardWatchEvent.ENTRY_DELETED)){
						for (ManagedFile file : oldFiles){
							events.add(new SimpleWatchEvent(file, StandardWatchEvent.ENTRY_DELETED));
						}
					}
					
					// newFiles contains now the recently added files.
					if (kinds.contains(StandardWatchEvent.ENTRY_CREATED)){
						for (ManagedFile file : newFiles){
							events.add(new SimpleWatchEvent(file, StandardWatchEvent.ENTRY_CREATED));
						}
					}
					
					// check the remaining files for modification.
					
					if (kinds.contains(StandardWatchEvent.ENTRY_MODIFIED)){
						for (ManagedFile file : common){
							if ((file instanceof FileIOManagedFileAdapter) && (((FileIOManagedFileAdapter) file).lastModified() > this.lastTimeChecked)) {
								events.add(new SimpleWatchEvent(file,StandardWatchEvent.ENTRY_MODIFIED));
							}	
						}
						
						// if events contain any event then the folder was modified
						if (!events.isEmpty()) {
							events.add(new SimpleWatchEvent(folder, StandardWatchEvent.ENTRY_MODIFIED));
						} 
					}
					
					
					oldFiles = allFiles;
					
				} else {
					// was removed
					if (kinds.contains(StandardWatchEvent.ENTRY_DELETED)){
						events.add(new SimpleWatchEvent(folder,StandardWatchEvent.ENTRY_DELETED));
					}
				}
			} else {
				// if didn't exist previously.
				
				if(folder.exists() && kinds.contains(StandardWatchEvent.ENTRY_CREATED)){
					// was created and creation is informed
					events.add(new SimpleWatchEvent(folder,StandardWatchEvent.ENTRY_CREATED));
					
					// iterate all files 
					
					folder.each(new Walker<ManagedFile>(){

						@Override
						public void doWith(ManagedFile file) {
							events.add(new SimpleWatchEvent(file ,StandardWatchEvent.ENTRY_CREATED));
						}

					});

				}
			}
			
			return events;
		}

	}

	public long getPeriod() {
		return period;
	}

	public void setPeriod(long period) {
		this.period = period;
	}

	public void start(){
		if (!monitorThread.isAlive()){
			monitorThread.start();
		}
	}


	private class Dog implements Runnable{

		@Override
		public void run() {
			while(true) {
				try {

					for (LegacyStrategyWatchEventChannel w : channels){
						w.update();
					}

					Thread.currentThread().sleep(period);

				} catch(InterruptedException e) {
					break;
				}
			}
		
			channels.clear();
		}

	}


	@Override
	public WatchEventChannel watch(Watchable watchable, Kind... events) {
		
		ManagedFile managedFile = fileForWatchable(watchable);
		
		EventsReader reader;
		
		
		if (managedFile.getType().isFile()) {
			reader = new FileEventsReader(managedFile);
		} else {
			reader = new FolderEventsReader(managedFile);
		}
		
		LegacyStrategyWatchEventChannel c = new LegacyStrategyWatchEventChannel(watchable , new HashSet<Kind>(Arrays.asList(events)) , reader);
		
		this.channels.add(c);
		
		return c;
	}
	
	private ManagedFile fileForWatchable(Watchable watchable){
		if (watchable instanceof ManagedFilePath) {
			ManagedFilePath path = (ManagedFilePath) watchable;
			return path.getManagedFileRepository().retrive(path);
		} else if (watchable instanceof ManagedFile) {
			return (ManagedFile) watchable;
		} else {
			throw new IllegalArgumentException(watchable.getClass() + " is not an acceptable watchable for this  watch service");
		}
	}

	@Override
	public void close() {
		
		monitorThread.interrupt();
	
	}

	
	private class LegacyStrategyWatchEventChannel implements WatchEventChannel {

		private Watchable watchable;
		private boolean closed = false;
		private BlockingQueue<WatchEvent> events = new LinkedBlockingQueue<WatchEvent>();
		private Set<Kind> kinds;
		private EventsReader reader;

		public LegacyStrategyWatchEventChannel(Watchable watchable, Set<Kind> kinds, EventsReader reader){
			this.watchable = watchable;
			this.kinds = kinds;
			this.reader = reader;
		}
		
		@Override
		public synchronized void close() {
			this.closed = true;
			channels.remove(this);
		}
		
		@Override
		public boolean isValid() {
			return !closed;
		}

		@Override
		public Watchable watchable() {
			return watchable;
		}
		
		@Override
		public BlockingQueue<WatchEvent> pollEvents() {
			return events;
		}

		public void update(){
			
			if (this.kinds.isEmpty()){
				return;
			}
		
			if (!closed){
				// only update after the previous events where consumed
				
				Collection<WatchEvent> newEvents = reader.readEvents(this.kinds);
				
				events.addAll(newEvents);
				
			}
		}


	}

}
