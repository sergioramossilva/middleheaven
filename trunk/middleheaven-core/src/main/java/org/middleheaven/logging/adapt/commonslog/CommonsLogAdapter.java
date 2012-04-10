package org.middleheaven.logging.adapt.commonslog;

import org.middleheaven.logging.Logger;


/**
 * Adpater class for Apache Commons Logging.
 * This class implements <code>org.apache.commons.logging.Log</code>.
 * 
 * 
 */
public class CommonsLogAdapter implements org.apache.commons.logging.Log {


	final Logger logger;

	public CommonsLogAdapter(Logger logger){
		this.logger = logger;
	}

	public boolean isDebugEnabled() {
		return true;
	}

	public boolean isErrorEnabled() {
		return true;
	}

	public boolean isFatalEnabled() {
		return true;
	}

	public boolean isInfoEnabled() {
		return true;
	}

	public boolean isTraceEnabled() {
		return true;
	}

	public boolean isWarnEnabled() {
		return true;
	}

	public void trace(Object message) {
		logger.trace("{0}", message);
	}

	public void trace(Object message, Throwable e) {
		logger.trace(e,"{0}", message);
	}

	public void debug(Object message) {
		logger.debug("{0}", message);
	}

	public void debug(Object message, Throwable e) {
		logger.debug(e,"{0}", message);
	}

	public void info(Object message) {
		logger.info("{0}", message);
	}

	public void info(Object message, Throwable e) {
		logger.info(e,"{0}", message);
	}

	public void warn(Object message) {
		logger.warn("{0}", message);
	}

	public void warn(Object message, Throwable e) {
		logger.warn(e,"{0}", message);
	}

	public void error(Object message) {
		logger.error("{0}", message);
	}

	public void error(Object message, Throwable e) {
		logger.error(e,"{0}", message);
	}

	public void fatal(Object message) {
		logger.fatal("{0}", message);
	}

	public void fatal(Object message, Throwable e) {
		logger.fatal(e,"{0}", message);
	}

}
