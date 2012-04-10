package org.middleheaven.graph;


/**
 * 
 */
public interface GraphTransversor {

	/**
	 * Transversed the graph in the order given by this transversor.
	 * @param <E> The edge object type
	 * @param <V> The vertex object type
	 * @param graph the graph to transversed
	 * @param startVertex the start vertex.
	 * 
	 */
	public <E,V> void transverse(Graph<E, V> graph, V startVertex);
	
	
	public void addListener(GraphTranverseListener listener);
	
	public void removeListener(GraphTranverseListener listener);
}
