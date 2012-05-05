/**
 * 
 */
package org.middleheaven.core.dependency;

import org.middleheaven.graph.DirectGraph;
import org.middleheaven.graph.EdgeTraversalEvent;
import org.middleheaven.graph.GraphTranverseListener;
import org.middleheaven.graph.TopologicOrderTransversor;
import org.middleheaven.graph.VertexTraversalEvent;

/**
 * 
 */
public class DependencyGraph extends DirectGraph<DependencyGraphEdge, DependencyGraphNode> {

	
	public DependencyGraph addDependency(DependencyGraphNode dependent, DependencyGraphNode dependency){
		return this.addDependency(dependent, dependency, true);
	}

	public DependencyGraph addDependency( DependencyGraphNode dependency,DependencyGraphNode dependent, boolean mandatory){
		
		DependencyGraphEdge edge = new DependencyGraphEdge();
		edge.isRequired = mandatory;
		
		addEdge(edge,dependency, dependent); // the dependency points to the dependent
		
		return this;
	}

	/**
	 * 
	 */
	public void resolve() {
		TopologicOrderTransversor t = new TopologicOrderTransversor();
		
		t.addListener(new GraphTranverseListener<DependencyGraphNode, DependencyGraphEdge>() {

			@Override
			public void beginEdgeTraversed(
					EdgeTraversalEvent<DependencyGraphEdge, DependencyGraphNode> e) {
				
			}

			@Override
			public void endEdgeTraversed(
					EdgeTraversalEvent<DependencyGraphEdge, DependencyGraphNode> e) {
				
			}

			@Override
			public void endVertex(
					VertexTraversalEvent<DependencyGraphNode, DependencyGraphEdge> e) {
				
			}

			@Override
			public void beginVertex(
					VertexTraversalEvent<DependencyGraphNode, DependencyGraphEdge> e) {
				e.getVertex().getObject().inicialize();
			}
		});
		
		t.transverse(this, null);
		
	}
}
