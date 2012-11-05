package org.middleheaven.io.repository.machine;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.junit.Test;
import org.middleheaven.io.repository.ManagedFile;
import org.middleheaven.io.repository.ManagedFileRepository;
import org.middleheaven.io.repository.watch.FileChangeStrategy;
import org.middleheaven.io.repository.watch.FileWatchChannelProcessor;
import org.middleheaven.io.repository.watch.StandardWatchEvent;
import org.middleheaven.io.repository.watch.WatchEvent;
import org.middleheaven.io.repository.watch.WatchEventChannel;
import org.middleheaven.tool.test.MiddleHeavenTestCase;

/**
 * 
 */
public class WatchDogTest extends MiddleHeavenTestCase{

	@Test
	public void testFileWatchDog () throws InterruptedException, IOException{
		
		
		File f  = new File(".");
		
		FileIOStrategy strategy = new FileIOStrategy();
		
		final ManagedFileRepository openFileRepository = strategy.openFileRepository(f.toURI());
		
		ManagedFile file = openFileRepository.retrive(openFileRepository.getRoots().iterator().next());
		
		FileIOWatchService dogService = (FileIOWatchService) strategy.getWatchService();
		
		dogService.setPeriod(2000);
		
		WatchEventChannel channel = file.register(dogService, StandardWatchEvent.ENTRY_CREATED, StandardWatchEvent.ENTRY_DELETED, StandardWatchEvent.ENTRY_MODIFIED);
		
		final List<WatchEvent> events = new LinkedList<WatchEvent>();
		
		FileWatchChannelProcessor processor = new FileWatchChannelProcessor(new FileChangeStrategy() {
			
			@Override
			public void onChange(WatchEvent event) {
				events.add(event);
			}
		});
		
		processor.add(channel);
		processor.start();
		
		File n = new File(f, "test.txt");
		
		n.createNewFile();

		Thread.currentThread().sleep(6000);
		
		n.delete();
		
		Thread.currentThread().sleep(6000);
		assertEquals(2 , events.size());
		
		Thread.currentThread().sleep(90000);
	}
	
}
