/**
 * 
 */
package org.middleheaven.core.wiring;

import static org.junit.Assert.*;

import org.junit.Test;
import org.middleheaven.core.bootstrap.ContainerFileSystem;
import org.middleheaven.core.wiring.annotations.Wire;
import org.middleheaven.logging.LoggingService;
import org.middleheaven.tool.test.MiddleHeavenTestCase;


/**
 * 
 */
public class ScopeWiringTest extends MiddleHeavenTestCase{

	
	ContainerFileSystem a;
	ContainerFileSystem b;
	
	LoggingService c;
	LoggingService d;
	
	@Wire
	public void setA(ContainerFileSystem fs){
		a = fs;
	}
	
	@Wire
	public void setB(ContainerFileSystem fs){
		b = fs;
	}
	
	@Wire
	public void setC(LoggingService fs){
		c = fs;
	}
	
	@Wire
	public void setD(LoggingService fs){
		d = fs;
	}
	
	@Test
	public void testSameObject(){
		
		assertNotNull(a);
		assertTrue (a == b);
	}
	
	
	@Test
	public void testSameFromActivator(){
		
		assertNotNull(c);
		assertTrue (c == d);
	}
}
