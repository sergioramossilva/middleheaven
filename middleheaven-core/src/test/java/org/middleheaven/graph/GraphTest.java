package org.middleheaven.graph;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.middleheaven.graph.Graph.Edge;
import org.middleheaven.graph.Graph.Vertex;


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
	public void textRemoveVertex(){


		Graph<String, String>  g = createAcyclicGraph();

		g.filter(new GraphFilter<String, String>() {

			@Override
			public boolean accepts(Edge<String, String> edge) {
				return true;
			}

			@Override
			public boolean accepts(Vertex<String, String> vertex) {
				return !vertex.getObject().equals("V2");
			}
		});

		
		assertNull(g.getVertex("V2"));
		assertNotNull(g.getVertex("V0"));
		assertNotNull(g.getVertex("V1"));
		assertNotNull(g.getVertex("V3"));
		assertNotNull(g.getVertex("V4"));
		assertNotNull(g.getVertex("V5"));
		assertNotNull(g.getVertex("V6"));
		
		assertFalse(g.containsEdge("a"));
		assertFalse(g.containsEdge("b"));
		assertFalse(g.containsEdge("k"));
		
		assertTrue(g.containsEdge("c"));
		assertTrue(g.containsEdge("d"));
		assertTrue(g.containsEdge("e"));
		assertTrue(g.containsEdge("f"));
		assertTrue(g.containsEdge("g"));
		assertTrue(g.containsEdge("h"));
		assertTrue(g.containsEdge("i"));
		assertTrue(g.containsEdge("j"));
		assertTrue(g.containsEdge("l"));
		
		
	}

	@Test
	public void textRemoveEdge(){

		Graph<String, String>  g = createAcyclicGraph();

		g.filter(new GraphFilter<String, String>() {

			@Override
			public boolean accepts(Edge<String, String> edge) {
				return !edge.getObject().equals("a") && !edge.getObject().equals("b") && !edge.getObject().equals("k");
			}

			@Override
			public boolean accepts(Vertex<String, String> vertex) {
				return true;
			}
		});

		
		assertNull("V2 is present", g.getVertex("V2"));
		assertNotNull(g.getVertex("V0"));
		assertNotNull(g.getVertex("V1"));
		assertNotNull(g.getVertex("V3"));
		assertNotNull(g.getVertex("V4"));
		assertNotNull(g.getVertex("V5"));
		assertNotNull(g.getVertex("V6"));
		
		assertFalse(g.containsEdge("a"));
		assertFalse(g.containsEdge("b"));
		assertFalse(g.containsEdge("k"));
		
		assertTrue(g.containsEdge("c"));
		assertTrue(g.containsEdge("d"));
		assertTrue(g.containsEdge("e"));
		assertTrue(g.containsEdge("f"));
		assertTrue(g.containsEdge("g"));
		assertTrue(g.containsEdge("h"));
		assertTrue(g.containsEdge("i"));
		assertTrue(g.containsEdge("j"));
		assertTrue(g.containsEdge("l"));
		
		

	}

	
	@Test
	public void testVisitorAcyclic(){

		Graph<String, String>  g = createAcyclicGraph();


		final Map<String, Integer> visited = new HashMap<String, Integer>();
		
		g.visit(new GraphVisitor<String, String>() {
			
			@Override
			public void onEndVertex(Vertex<String, String> vertex) {
				
			}
			
			@Override
			public void onEndGraph(Graph<String, String> g) {
				
			}
			
			@Override
			public void onEdge(Edge<String, String> edge) {
				Integer count = visited.get(edge.getObject());
				
				if (count == null){
					count = 1;
				} else {
					count = count + 1;
				}
				
				visited.put(edge.getObject(), count);
			}
			
			@Override
			public void onBeginVertex(Vertex<String, String> vertex) {
				Integer count = visited.get(vertex.getObject());
				
				if (count == null){
					count = 1;
				} else {
					count = count + 1;
				}
				
				visited.put(vertex.getObject(), count);
			}
			
			@Override
			public void onBeginGraph(Graph<String, String> g) {
				
			}
		});
		
		String[] vertex = new String[]{"V0", "V1", "V2", "V3", "V4", "V5", "V6"};
		String[] edges = new String[]{"a", "b", "c", "d", "e", "f", "g","h","i","j","k","l"};
		
		for (String v : vertex){
			final Integer count = visited.get(v);
			assertNotNull("Vertex " + v + " not visited" ,count);
			assertEquals( "Vertex " + v + " visited more than once" ,1 , count);
		}
		for (String e : edges){
			final Integer count = visited.get(e);
			assertNotNull("Edge " + e + " not visited" , count);
			assertEquals( "Edge " + e + " visited more than once" ,1 , count);
		}
		
		assertNull(visited.get("V9"));
	}
	
	@Test
	public void testVisitorCyclic(){

		Graph<String, String>  g = createCyclicGraph();


		final Map<String, Integer> visited = new HashMap<String, Integer>();
		
		g.visit(new GraphVisitor<String, String>() {
			
			@Override
			public void onEndVertex(Vertex<String, String> vertex) {
				
			}
			
			@Override
			public void onEndGraph(Graph<String, String> g) {
				
			}
			
			@Override
			public void onEdge(Edge<String, String> edge) {
				Integer count = visited.get(edge.getObject());
				
				if (count == null){
					count = 1;
				} else {
					count = count + 1;
				}
				
				visited.put(edge.getObject(), count);
			}
			
			@Override
			public void onBeginVertex(Vertex<String, String> vertex) {
				Integer count = visited.get(vertex.getObject());
				
				if (count == null){
					count = 1;
				} else {
					count = count + 1;
				}
				
				visited.put(vertex.getObject(), count);
			}
			
			@Override
			public void onBeginGraph(Graph<String, String> g) {
				
			}
		});
		
		String[] vertex = new String[]{"V0", "V1", "V2", "V3", "V4", "V5", "V6"};
		String[] edges = new String[]{"a", "b", "c", "d", "e", "f", "g","h","i","j","k","l"};
		
		for (String v : vertex){
			final Integer count = visited.get(v);
			assertNotNull("Vertex " + v + " not visited" ,count);
			assertEquals( "Vertex " + v + " visited more than once" ,1 , count);
		}
		for (String e : edges){
			final Integer count = visited.get(e);
			assertNotNull("Edge " + e + " not visited" , count);
			assertEquals( "Edge " + e + " visited more than once" ,1 , count);
		}
		
		assertNull(visited.get("V9"));
	}
	
	@Test
	public void testTopologic(){


		Graph<String, String>  g = createAcyclicGraph();
		TopologicOrderTransversor t = new TopologicOrderTransversor();

		assertEquals(transverse(g, t, "V2").toArray(), new String[]{"V2", "V0","V1", "V3", "V4", "V6", "V5"});
		assertEquals(transverse(g, t, null).toArray(), new String[]{"V2", "V0","V1", "V3", "V4", "V6", "V5"});

	}

	private <V, E> List<V> transverse( Graph<E, V> g, GraphTransversor t, V start) {
		final LinkedList<V> result = new LinkedList<V>();


		t.addListener(new GraphTranverseListener<V, E>() {

			@Override
			public void beginEdgeTraversed(EdgeTraversalEvent<E, V> e) {}

			@Override
			public void endEdgeTraversed(EdgeTraversalEvent<E, V> e) {}

			@Override
			public void endVertex(VertexTraversalEvent<V, E> e) {}

			@Override
			public void beginVertex(VertexTraversalEvent<V,E> e) {
				result.add(e.getVertex().getObject());
			}
		});

		t.transverse(g, start);

		return result;
	}

	private <V, E> List<V> transverse( Graph<E, V> g, ShortestPathInspector t, V start, V end) {
		final LinkedList<V> result = new LinkedList<V>();

		t.getPath(g, start, end).visit(new GraphPathVisitor<E, V>() {


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
