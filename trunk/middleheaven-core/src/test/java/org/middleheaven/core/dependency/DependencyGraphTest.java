/**
 * 
 */
package org.middleheaven.core.dependency;

import static org.junit.Assert.assertEquals;

import java.util.LinkedList;

import org.junit.Test;

/**
 * 
 */
public class DependencyGraphTest {
	
	
	@Test
	public void testCreation(){
		
		
		DependencyGraph graph = new DependencyGraph();
		
		final LinkedList<DependencyGraphNode> result = new LinkedList<DependencyGraphNode>();
		
		DependencyInicilizer init = new DependencyInicilizer(){

			@Override
			public void inicialize(DependencyGraphNode e) {
				result.addFirst(e);
			}
			
		};
		
		DependencyGraphNode S1 = new DependencyGraphNode("S1", init);
		DependencyGraphNode S2 = new DependencyGraphNode("S2", init);
		DependencyGraphNode S3 = new DependencyGraphNode("S3", init);
		DependencyGraphNode S4 = new DependencyGraphNode("S4", init);
		
		DependencyGraphNode A = new DependencyGraphNode("A", init);
		DependencyGraphNode B = new DependencyGraphNode("B", init);
		DependencyGraphNode C = new DependencyGraphNode("C", init);
		DependencyGraphNode D = new DependencyGraphNode("D", init);
		DependencyGraphNode E = new DependencyGraphNode("E", init);
		
		graph.addDependency(S1, A);
		graph.addDependency(S2, A);
		
		graph.addDependency(B, S1);
		
		graph.addDependency(C, S1);
		graph.addDependency(C, S4);
		
		graph.addDependency(D, S2);
		graph.addDependency(S3, D);
		
		graph.addDependency(E, S3);
		graph.addDependency(S4, E);
		
	
		graph.resolve();
		
		assertEquals(A,result.removeFirst());
		assertEquals(S2,result.removeFirst());
		assertEquals(D,result.removeFirst());
		assertEquals(S3,result.removeFirst());
		assertEquals(E,result.removeFirst());
	
		assertEquals(S4,result.removeFirst());
		assertEquals(S1,result.removeFirst());
		
		assertEquals(C,result.removeFirst());
		assertEquals(B,result.removeFirst());

	
	
	}

}
