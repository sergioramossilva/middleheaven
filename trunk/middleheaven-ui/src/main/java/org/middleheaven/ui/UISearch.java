/**
 * 
 */
package org.middleheaven.ui;

import java.util.Collection;
import java.util.LinkedList;

import org.middleheaven.util.classification.Predicate;

/**
 * 
 */
public class UISearch {


	public interface UISearchFilter {
		
		public boolean accept(UIComponent c);
		
		public boolean canContinueSearch(UIComponent c);
	}
	/**
	 * Search downn from a component for a component with a given id.
	 * @param top
	 * @param gid
	 * @return
	 */
	public static UIComponent searchDown(UIComponent top, String gid) {

		LinkedList <UIComponent> components = new LinkedList <UIComponent>();

		components.add(top);

		return doSearch(gid, components);

	}
	
	/**
	 * Applies a {@link UISearchFilter} to the {@code bottom} component and up to all parents until the root component,
	 *  the filter returns false from {@link UISearchFilter#canContinueSearch(UIComponent)} or a match is found.
	 * 
	 * @param bottom the start component.
	 * @param filter the filter to apply
	 * @return the found component that matches the filter
	 */
	public static UIComponent searchFirstUp(UIComponent bottom,UISearchFilter filter) {
		
		UIComponent candidate = bottom;
		
		while (candidate != null && filter.canContinueSearch(candidate)){
			
			if (filter.accept(candidate)){
				return candidate;
			}
			
			candidate = candidate.getUIParent();
		}
		
		return null;
		
	}

		
	/**
	 * Applies a {@link UISearchFilter} to the {@code bottom} component and up to all parents until the root component
	 * of the filter returns false from {@link UISearchFilter#canContinueSearch(UIComponent)}.
	 * All components that are accepted by the filter are added to the provided {@code result} collection.
	 * 
	 * The filter is applied to the bottom component as well.
	 * 
	 * @param bottom the start component.
	 * @param filter the filter to apply
	 * @param result the result collection
	 */
	public static void searchUp(UIComponent bottom,UISearchFilter filter, Collection<UIComponent> result) {

		UIComponent candidate = bottom;
		
		while (candidate != null && filter.canContinueSearch(candidate)){
			
			if (filter.accept(candidate)){
				result.add(candidate);
			}
			
			candidate = candidate.getUIParent();
		}
		

	}

	/**
	 * @param gid
	 * @param components
	 */
	private static UIComponent doSearch(String gid, LinkedList<UIComponent> components) {


		while (!components.isEmpty()) {

			UIComponent c = components.removeFirst();

			if (c.getGID().equals(gid)){
				return c;
			} else {
				if (c.getChildrenCount() > 0){
					for (UIComponent uic : c.getChildrenComponents()){
						components.addLast(uic);
					}
				}

			}

		}
		
		return null;
	}
}
