/**
 * 
 */
package org.middleheaven.logging;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * 
 */
public class CompositeLogBook extends LogBook{


    protected final Set<Logger> books = new CopyOnWriteArraySet<Logger>();

	public CompositeLogBook(String name) {
		super(name, LoggingLevel.ALL);
	}
	
	/**
	 * Constructor.
	 * @param name
	 * @param level
	 */
	public CompositeLogBook(String name, LoggingLevel level) {
		super(name, level);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void doLog(LoggingEvent event) {
		for (Logger book : books){
			book.log(event);
		}
	}

	
	public CompositeLogBook addLogBook(Logger book){
		books.add(book);
		return this;
	}

	public CompositeLogBook removeLogBook(Logger book){
		books.remove(book);
		return this;
	}
}
