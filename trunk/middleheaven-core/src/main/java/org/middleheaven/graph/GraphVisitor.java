package org.middleheaven.graph;


public interface GraphVisitor<E, V> {

	public void beginVisitGraph(Graph<E, V> graph);
	
	public void beginVisitVertex(V vertex);
	
	public void endVisitVertex(V vertex);
	
	public void endVisitGraph(Graph<E, V> graph);

	public void visitEdge(E connectingEdge);
	
}
