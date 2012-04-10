/**
 * 
 */
package org.middleheaven.graph;

import org.middleheaven.graph.Graph.Edge;
import org.middleheaven.graph.Graph.Vertex;

/**
 * 
 */
public interface GraphFilter<V, E> {

	public boolean accepts (Vertex<V, E> vertex);
	public boolean accepts (Edge<V, E> edge);

}
