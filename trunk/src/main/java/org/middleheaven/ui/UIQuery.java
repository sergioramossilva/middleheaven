package org.middleheaven.ui;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;


/**
 * Holds information to find another UIComponent from a given finder
 *
 */
public final class UIQuery {

	private static UIComponent findRoot(UIComponent component){
		if (component.getUIParent()==null){
			return component;
		} else {
			return findRoot (component.getUIParent());
		}
	}
	public static UIQuery search(String expr) {
		return new UIQuery(expr);
	}
	
	private String expression;
	private UIQuery(String expression){
		this.expression = expression;
	}
	
	public List<UIComponent> execute ( UIComponent currentComponent){
		return findByExpression(currentComponent, expression);
	}
	
    private static List<UIComponent> findByExpression(UIComponent component, String expr){
    	
    	// component to wich the paths is relative
    	UIComponent base = component;
    	expr = expr.replaceAll("\\.", "#");
    	
    	if (expr.startsWith("/")){ // absolute path
    		base = findRoot (component); // is relative to the top parent. find it
    		// remove the / from the expression to make it relative
    		expr = expr.substring(1);
    		
    		if (expr.length()==0){
    			return Collections.singletonList(base); // no other search needed
    		}
    	} 
    	
    	LinkedList<UIComponent> baseStack = new LinkedList<UIComponent>();
    	baseStack.add(base);
    	
    	Queue<String> pathStack = new LinkedList<String>(Arrays.asList(expr.split("/")));
    	
    	resolveStack (baseStack , pathStack );

    	return baseStack;
    }
    
	private static void resolveStack(Queue<UIComponent> baseStack, Queue<String> pathStack) {
		
		if (pathStack.isEmpty()){
			return; // stop
		}
		
		String id = pathStack.poll();
		
		if ( id.equals("#")){
			// no-op , the base is the same
		} else if (id.equals("##")){
			baseStack.offer(baseStack.poll().getUIParent()); // the parent
		} else { // by id
			
			UIComponent current = baseStack.poll();
			
			if (current instanceof NamingContainer){
				UIComponent uic= ((NamingContainer)current).findContainedComponent(id);
				if (uic!=null){
					baseStack.add(uic);
				}
			} else {
				List<UIComponent> all = current.getChildrenComponents();
				for (UIComponent child : all){
					if (child.getGID().equals(id)){
						baseStack.offer(child);
						break;
					}
				}
				// not found , break process
				pathStack.clear();
			}
		}
		
		// iterate 
		resolveStack (baseStack , pathStack );
	}

	
    
}
