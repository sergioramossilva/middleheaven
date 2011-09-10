package org.middleheaven.graph;

public interface GraphTranverseListener<E,V> {

	
	void beginEdgeTraversed(EdgeTraversalEvent<E,V> e);
	
	void endEdgeTraversed(EdgeTraversalEvent<E,V> e);
	
	void endVertex(VertexTraversalEvent<V> e);
	   
	void beginVertex(VertexTraversalEvent<V> e);
	   
}
