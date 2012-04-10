/**
 * 
 */
package org.middleheaven.graph;

import org.middleheaven.graph.Graph.Edge;
import org.middleheaven.graph.Graph.Vertex;

/**
 * 
 */
public class GraphVisitorAdapter<E, V> implements GraphVisitor<E, V> {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onBeginGraph(Graph<E, V> g) {
		//no-op
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onEndGraph(Graph<E, V> g) {
		//no-op
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onBeginVertex(Vertex<V, E> vertex) {
		//no-op
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onEndVertex(Vertex<V, E> vertex) {
		//no-op
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onEdge(Edge<V, E> edge) {
		//no-op
	}

}
