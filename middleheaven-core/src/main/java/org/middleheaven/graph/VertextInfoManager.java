package org.middleheaven.graph;

import java.util.HashMap;
import java.util.Map;

import org.middleheaven.graph.Graph.Edge;
import org.middleheaven.graph.Graph.Vertex;

public class VertextInfoManager {

	public class VertexInfo { 
		
		protected int scratch =0;
		protected double dist = Double.MAX_VALUE;
		protected Vertex<? , ?> prev;
		protected Edge<?,?> connectingEdge;

	}

	public interface VertexInfoVisitor<E, V> { 
		
	
		public void beginVisitVertex(Vertex<V,E> vertex);
		
		public void endVisitVertex(Vertex<V,E> vertex);

		public void visitEdge(Edge<V,E> connectingEdge);

	}

	
	private final Map<Vertex , VertexInfo> infos = new HashMap<Vertex, VertexInfo>();
	
	public VertexInfo info(Vertex<?, ?> vertex){
		VertexInfo info = infos.get(vertex);
		if (info == null){
			info = new VertexInfo();
			infos.put(vertex, info);
		}
		return info;
	}
	
	/**
	 * Used the acumulated info to visit
	 * @param graph
	 * @param visitor
	 */
	public <E,V> void doVisit(VertexInfoVisitor <E,V> visitor, Vertex<V, E> endVertex){
		
		doVisitVertex(visitor, endVertex);

	}

	private <E,V> void doVisitVertex(VertexInfoVisitor <E,V> visitor, Vertex<V, E> v){
		
		
		visitor.beginVisitVertex(v);
		
		VertexInfo info = info(v);
		if ( info.prev != null) {
			
			if(info.connectingEdge != null){
				visitor.visitEdge((Edge<V, E>) info.connectingEdge);
			}
			
			doVisitVertex(visitor, (Vertex<V, E>) info.prev);
		}
		
		visitor.endVisitVertex(v);

	}
}
