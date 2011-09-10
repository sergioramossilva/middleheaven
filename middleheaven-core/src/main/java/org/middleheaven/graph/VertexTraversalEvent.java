package org.middleheaven.graph;

public class VertexTraversalEvent<V> {

	
	private V vertex;
	
	public VertexTraversalEvent (V vertex){
		this.vertex = vertex;
	}

	public V getVertex() {
		return vertex;
	}


	
	
}
