package org.middleheaven.graph;

import java.util.Collection;
import java.util.LinkedList;

import org.middleheaven.graph.Graph.Edge;
import org.middleheaven.graph.Graph.Vertex;
import org.middleheaven.graph.VertextInfoManager.VertexInfo;

/**
 * 
 */
public class TopologicOrderTransversor extends AbstractGraphTransversor {

	@Override
	public <E, V> void transverse(Graph<E, V> graph, V startVertex) {
		Collection<Vertex<V, E>> all = graph.getVertices();
		
		VertextInfoManager manager = new VertextInfoManager();
		
		if (!all.isEmpty()){

			
			Vertex<V, E> start = graph.getVertex(startVertex);

			LinkedList<Vertex<V, E>> q = new LinkedList<Vertex<V, E>>();
		
			manager.info(start).dist = 0;
			
			// compute ingree

			for (Vertex<V, E> v: all ){
				for (Edge<V, E> e : v.getOutjacentEdges()){
					manager.info(e.getTargetVertex()).scratch++;
				}
			}
			
			// enqueue those with ingree zero
			for (Vertex<V, E> v : all){
				if (manager.info(v).scratch == 0){
					q.add(v);
				}
			}
			
			GraphTranverseListener broadcastEvent = this.getListenerSet().broadcastEvent();
			
			int iterations;
			for (iterations = 0; !q.isEmpty(); iterations++ ){
				
				Vertex<V, E> v = q.remove();
				
				broadcastEvent.beginVertex(new VertexTraversalEvent<V>(v.getObject()));
			
				for (Edge<V, E> e : v.getOutjacentEdges() ){
					
					broadcastEvent.beginEdgeTraversed(new EdgeTraversalEvent(e.getObject()));
					
					Vertex<V, E> w = e.getTargetVertex();
					double cvw = e.getCost();
					
					VertexInfo infoW = manager.info(w);
					
					if ( --infoW.scratch == 0 ) {
						q.add(w);
					}
						
					VertexInfo infoV = manager.info(v);
					
					broadcastEvent.endEdgeTraversed(new EdgeTraversalEvent(e.getObject()));
					
					if (Double.compare(infoV.dist, Double.MAX_VALUE) == 0){
						continue;
					}
					if (Double.compare(infoW.dist, infoV.dist + cvw) > 0){
						infoW.dist = infoV.dist + cvw;
						infoW.prev = v;
						infoW.connectingEdge = e;
					}
				}
				
				broadcastEvent.endVertex(new VertexTraversalEvent<V>(v.getObject()));
			}
			
		
			if (iterations != all.size()){
				throw new IllegalStateException("Graph as a cycle");
			}

	
		}
		
		
		
	}


}
