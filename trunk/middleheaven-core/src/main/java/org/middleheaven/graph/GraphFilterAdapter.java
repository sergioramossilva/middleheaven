/**
 * 
 */
package org.middleheaven.graph;

import org.middleheaven.graph.Graph.Edge;
import org.middleheaven.graph.Graph.Vertex;

/**
 * 
 */
public class GraphFilterAdapter<V, E> implements  GraphFilter< V, E>{

	public boolean accepts (Vertex<V, E> vertex){
		return true;
	}
	
	public boolean accepts (Edge<V, E> edge){
		return true;
	}

}
