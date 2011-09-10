package org.middleheaven.graph;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 
 * @param <E> object at the edge
 * @param <V> object class at the vertex.
 */
public class DirectGraph<E,V> implements Graph<E,V>{


	private class BeanEdge<V, E> implements Edge<V, E>{
		
		private E object;
		private Vertex<V, E> targetVertex;
		private double cost;
		private Vertex<V, E> sourceVertex;
		
		public BeanEdge(E object, Vertex<V, E> sourceVertex, Vertex<V, E> targetVertex, double cost) {
			this.object = object;
			this.targetVertex = targetVertex;
			this.sourceVertex = sourceVertex;
			this.cost = cost;
		}

		@Override
		public E getObject() {
			return object;
		}

		@Override
		public Vertex<V, E> getTargetVertex() {
			return targetVertex;
		}
		
		@Override
		public Vertex<V, E> getSourceVertex() {
			return sourceVertex;
		}

		@Override
		public double getCost() {
			return cost;
		}
		
		public String toString(){
			return object.toString();
		}


	}
	
	private class BeanVertex<V, E> implements Vertex<V, E> {
		
		private V object;
		private List<Edge<V,E>> outjacentEdges = new LinkedList<Edge<V,E>>();
		private List<Edge<V,E>> incidentEdges = new LinkedList<Edge<V,E>>();
		
		private double dist;
		public Vertex<V, E> previous;
		private int scratch;
		
		public BeanVertex(V object){
			this.object = object;
			reset();
		}
		
		public final void reset(){
			dist = Double.MAX_VALUE;
			previous = null;
			scratch = 0;
		}

		@Override
		public V getObject() {
			return object;
		}



		@Override
		public List<org.middleheaven.graph.Graph.Edge<V,E>> getOutjacentEdges() {
			return outjacentEdges;
		}

		@Override
		public List<org.middleheaven.graph.Graph.Edge<V, E>> getIncidentEdges() {
			return incidentEdges;
		}


	}
	

	private final Map<V, Vertex<V, E>> vertexes = new HashMap<V, Vertex<V, E>>();
	private final Set<Edge<V, E>> edges = new HashSet<Edge<V, E>>();

	/**
	 * 
	 */
	public DirectGraph (){}
	
	@Override
	public Collection<org.middleheaven.graph.Graph.Vertex<V, E>> getVertices() {
		return vertexes.values();
	}


	@Override
	public Collection<org.middleheaven.graph.Graph.Edge<V, E>> getEdges() {
		return edges;
	}


	@Override
	public void addEdge(E edgeObject,
			V sourceVertex,
			V targetVertex, double cost) {
		
		Vertex<V, E> v = vertexes.get(sourceVertex);
		
		if (v == null){
			v = new BeanVertex<V, E>(sourceVertex);
			vertexes.put(sourceVertex,v);
		}
		
		Vertex<V, E> w = vertexes.get(targetVertex);
		
		if (w == null){
			w = new BeanVertex<V, E>(targetVertex);
			vertexes.put(targetVertex, w);
		}
		
		BeanEdge<V, E> edge = new BeanEdge<V, E>(edgeObject, v , w, cost);
		v.getOutjacentEdges().add(edge);
		w.getIncidentEdges().add(edge);
		
		this.edges.add(edge);
	}

	@Override
	public org.middleheaven.graph.Graph.Vertex<V, E> getVertex(V vertex) {
		return this.vertexes.get(vertex);
	}

	
	
}
