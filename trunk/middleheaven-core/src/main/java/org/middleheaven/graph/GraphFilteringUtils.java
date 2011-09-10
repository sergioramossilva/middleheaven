package org.middleheaven.graph;

import org.middleheaven.core.reflection.inspection.Introspector;
import org.middleheaven.graph.Graph.Edge;
import org.middleheaven.graph.Graph.Vertex;
import org.middleheaven.util.classification.BooleanClassifier;

/**
 * 
 * 
 */
public final class GraphFilteringUtils {

	
	private GraphFilteringUtils (){}
	
	/**
	 * Fills a new graph by coping another graph and BooleanClassifier to eliminate Edges and Vertices.
	 * @param <E>
	 * @param <V>
	 * @param sourceGraph
	 * @param verticesClassifier
	 * @param edgeClassifier
	 * @return
	 */
	public static <E, V, G extends Graph<E,V> > G  filter(G sourceGraph, BooleanClassifier<V> verticesClassifier , BooleanClassifier<E> edgeClassifier ) {
		
		G targetGraph = Introspector.of(sourceGraph).introspectClass().newInstance();
		
		for ( Edge<V,E> edge : sourceGraph.getEdges()){

			Vertex<V,E> s = edge.getSourceVertex();
			Vertex<V,E> t = edge.getTargetVertex();
			
			if ((verticesClassifier.classify(s.getObject()) || verticesClassifier.classify(t.getObject())) && edgeClassifier.classify(edge.getObject())){
				targetGraph.addEdge(edge.getObject(), s.getObject(), t.getObject(), edge.getCost());
			}
		}
		
		return targetGraph;
	}
	
	public static <E, V, G extends Graph<E,V> > G  filter(G sourceGraph, BooleanClassifier<V> verticesClassifier ) {
		return filter(sourceGraph, verticesClassifier, new BooleanClassifier<E>(){

			@Override
			public Boolean classify(E obj) {
				return Boolean.TRUE;
			}});
	}
		
	
	
}
