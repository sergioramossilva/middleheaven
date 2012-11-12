package org.middleheaven.ui;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;


/**
 * Holds information to find another UIComponent from a given finder
 *
 * 
 *
 *
 */
public final class UITreeCriteria {

	private static UIComponent findRoot(UIComponent component){
		if (component.getUIParent()==null){
			return component;
		} else {
			return findRoot (component.getUIParent());
		}
	}

	/**
	 * Search for components that match a given expression.
	 * 
	 * <code>id<code> for a component with graphic identification equal to <code>id</code>
	 * <code>id1/id2<code> for a component with graphic identification equal to <code>id2</code> whose 
	 * parent has graphic identification equal to <code>id1</code>
	 * <code>/id1/id2<code> absolute search for a component with graphic identification equal to <code>id2</code> whose 
	 * parent has graphic identification equal to <code>id1</code> whose parent is the root component.
	 * <code>.<code> the component it self
	 * <code>..</code> the parent component
	 * <code>../id</code> a sibling component with graphic identification equal to <code>id</code>
	 * @param expr the expression to use in the search
	 * @return
	 */
	public static UITreeCriteria search(String expr) {
		return new UITreeCriteria(expr);
	}

	private String expression;
	private UITreeCriteria(String expression){
		this.expression = expression;
	}

	public UIQuery execute ( UIComponent currentComponent){
		return new ListUIQuery(currentComponent, expression);
	}

	static List<UIComponent> findByExpression(UIComponent component, String expr){

		if (!component.isRendered()){
			throw new IllegalArgumentException("Cannot perform tree search on non rendered components");
		}

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

		LinkedList<UIComponent> searchStack = new LinkedList<UIComponent>();
		searchStack.add(base);

		Queue<String> pathStack = new LinkedList<String>(Arrays.asList(expr.split("/")));

		resolveStack (searchStack , pathStack );

		return searchStack;
	}

	private static void resolveStack(Queue<UIComponent> searchStack, Queue<String> pathStack) {

		if (pathStack.isEmpty()){
			return; // stop
		}

		String id = pathStack.poll();

		if ("#".equals(id)){
			// no-op , the base is the same
		} else if ("##".equals(id)){
			// add the parent to the stack
			searchStack.add(searchStack.poll().getUIParent()); // the parent
		} else { // by id

			UIComponent current = searchStack.poll();


			if (current instanceof NamingContainer){
				UIComponent uic = ((NamingContainer)current).findContainedComponent(id);
				if (uic != null){
					searchStack.clear();
					pathStack.clear();
					searchStack.add(uic);
					return;
				}
			} else {
				List<UIComponent> all = current.getChildrenComponents();
				for (UIComponent child : all){
					if (child.getGID().equals(id)){
						searchStack.add(child);
						break;
					}
				}
				// not found , break process
				pathStack.clear();
			}

		}

		// iterate 
		resolveStack (searchStack , pathStack );
	}



}
