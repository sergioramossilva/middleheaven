package org.middleheaven.graph;

import static org.junit.Assert.*;

import java.util.LinkedList;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;


public class GraphTest {

	
	private Graph<String, String>  createAcyclicGraph(){
		Graph<String, String> g = new DirectGraph<String, String>();
		
		g.addEdge("a", "V2", "V0", 0);
		g.addEdge("b", "V2", "V5", 0);
		g.addEdge("d", "V0", "V3", 0);
		g.addEdge("c", "V0", "V1", 0);
		g.addEdge("e", "V1", "V3", 0);
		g.addEdge("f", "V1", "V4", 0);
		g.addEdge("g", "V4", "V6", 0);
		g.addEdge("h", "V6", "V5", 0);
		g.addEdge("i", "V3", "V5", 0);
		g.addEdge("j", "V3", "V6", 0);
		g.addEdge("k", "V2", "V3", 0);
		g.addEdge("l", "V3", "V4", 0);
		
		return g;
	}
	
	private Graph<String, String>  createCyclicGraph(){
		Graph<String, String> g = new DirectGraph<String, String>();
		
		g.addEdge("b", "V2", "V5", 5);
		g.addEdge("a", "V2", "V0", 2);
		g.addEdge("d", "V0", "V3", 1);
		g.addEdge("c", "V0", "V1", 2);
		g.addEdge("e", "V1", "V3", 3);
		g.addEdge("f", "V1", "V4", 10);
		g.addEdge("g", "V4", "V6", 6);
		g.addEdge("h", "V6", "V5", 1);
		g.addEdge("i", "V3", "V5", 8);
		g.addEdge("j", "V3", "V6", 4);
		g.addEdge("k", "V3", "V2", 2);
		g.addEdge("l", "V3", "V4", 2);
		
		return g;
	}
	
	@Test 
	public void testUnweigth(){
		
		
		Graph<String, String>  g = createCyclicGraph();
		UnWeigthedShortestPathInspector t = new UnWeigthedShortestPathInspector();
		
		
		assertEquals(transverse(g, t, "V2","V2").toArray(), new String[0]);
		assertEquals(transverse(g, t, "V2","V0").toArray(), new String[]{"V2", "V0"});
		assertEquals(transverse(g, t, "V2","V1").toArray(), new String[]{"V2", "V0", "V1"});
		assertEquals(transverse(g, t, "V2","V3").toArray(), new String[]{"V2", "V0", "V3"});
		assertEquals(transverse(g, t, "V2","V5").toArray(), new String[]{"V2", "V5"});
		assertEquals(transverse(g, t, "V4","V2").toArray(), new String[0]); // impossible
	}
	
	@Test 
	public void testWeigth(){
		
		
		Graph<String, String>  g = createCyclicGraph();
		PositiveWeigthedShortestPathInspector t = new PositiveWeigthedShortestPathInspector();
		

		
		assertEquals(transverse(g, t, "V2","V2").toArray(), new String[0]);
		assertEquals(transverse(g, t, "V2","V0").toArray(), new String[]{"V2", "V0"});
		assertEquals(transverse(g, t, "V2","V1").toArray(), new String[]{"V2", "V0", "V1"});
		assertEquals(transverse(g, t, "V2","V3").toArray(), new String[]{"V2", "V0", "V3"});
		assertEquals(transverse(g, t, "V2","V5").toArray(), new String[]{"V2", "V5"});
	}
	
	@Test
	public void testTopologic(){
		
		
		Graph<String, String>  g = createAcyclicGraph();
		TopologicOrderTransversor t = new TopologicOrderTransversor();
		
		assertEquals(transverse(g, t, "V2").toArray(), new String[]{"V2", "V0","V1", "V3", "V4", "V6", "V5"});

	}
	
	private <V, E> List<V> transverse( Graph<E, V> g, GraphTransversor t, V start) {
		final LinkedList<V> result = new LinkedList<V>();
		
		
		t.addListener(new GraphTranverseListener<E, V>() {

			@Override
			public void beginEdgeTraversed(EdgeTraversalEvent<E, V> e) {}

			@Override
			public void endEdgeTraversed(EdgeTraversalEvent<E, V> e) {}

			@Override
			public void endVertex(VertexTraversalEvent<V> e) {}

			@Override
			public void beginVertex(VertexTraversalEvent<V> e) {
				result.add(e.getVertex());
			}
		});
		
		t.transverse(g, start);
		
		return result;
	}
	
	private <V, E> List<V> transverse( Graph<E, V> g, ShortestPathInspector t, V start, V end) {
		final LinkedList<V> result = new LinkedList<V>();
		
		t.getPath(g, start, end).visit(new GraphVisitor<E, V>() {


			@Override
			public void beginVisitVertex(V vertex) {
				result.add(vertex);
			}

			@Override
			public void endVisitVertex(V vertex) {}

			@Override
			public void endVisitGraph(Graph<E,V> graph) {}

			@Override
			public void visitEdge(E connectingEdge) {}

			@Override
			public void beginVisitGraph(Graph<E, V> graph) {}});
		
		return result;
	}
}
