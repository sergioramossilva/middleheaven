package org.middleheaven.ui;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Holds information to find another UIComponent from a given finder
 *
 */
public final class UIQuery {

	public static UIQuery findRoot(){
		return find("/");
	}
	
	public static UIQuery find(String expression){
	     return new UIQuery(expression); 
	}
	
	private String expression;
	private UIQuery(String expression){
		this.expression = expression;
	}
	
	public Set<UIComponent> execute ( UIComponent currentComponent){
		return findByExpression(currentComponent, expression);
	}
	
    private static Set<UIComponent> findByExpression(UIComponent component, String expr){
        
        String[] path = expr.trim().split("/");
          
        boolean isRoot = false;
        if (path.length==0 || path[0].length()==0){
            // root if the first char was a '/'
            expr = expr.substring(1);
            isRoot= true;
        }
        UIComponent base = searchBase (component,isRoot);  
        
        if (path.length>1){
            for (int i=0;base!=null && i<path.length-1;i++){
                base = searchChildContainer(base,path[i]);
            }
        }
        
        if (isRoot && UIClient.class.equals( base.getType()) ){
            return Collections.singleton(base);
        }
        
        return searchContainerChilds(base,path[path.length-1]);
   
    }
    
    /**
     * Retrieve the base component for the search. 
     * @param component
     * @param isRoot
     * @return
     */
    private static UIComponent searchBase(UIComponent component, boolean isRoot){
        if (component.getUIParent()==null) return component; // cannot go up
        
        // not is root and is a naming container
        if (!isRoot && component instanceof NamingContainer){
            return component;
        }
        
        return searchBase(component.getUIParent(),isRoot);
    }
    
    private static UIComponent searchChildContainer(UIComponent base, String id){
        List<UIComponent> components = base.getChildrenComponents();
        
        for (UIComponent component : components){
            if (component instanceof NamingContainer && component.getGID().equals(id)){
                return component;
            }
        }
        
        for (UIComponent component : components){
            UIComponent c = searchChildContainer(component,id); 
            if (c!=null){
                return c;
            }
        }
        
        return null;
    }
    
    private static void searchChildren(UIComponent parent , Collection<UIComponent> foundChilds , String id){
        if (parent ==null){
        	return;
        }
    	boolean found = false;
        List<UIComponent> components = parent.getChildrenComponents();

        for (UIComponent component : components){
      
            if (component.getGID().equals(id)){
                foundChilds.add(component);
                found = true;
            }
        }
        
        if (!found){
        	// not foun as a child of the component.
        	// search as a child of the children
            for (UIComponent component : components){
                searchChildren(component,foundChilds ,id); 
            }
        }
    }
    
    private static Set<UIComponent> findContainerChildren(UIComponent base, String id){
        // brut-force search
        Set<UIComponent> list = new LinkedHashSet<UIComponent>();
        
        searchChildren(base, list,id);
        
        // could not be found
        return list;
    }
    
    private static Set<UIComponent> searchContainerChilds(UIComponent base, String id){
        if (base instanceof NamingContainer){
            return ((NamingContainer)base).findContainedComponent(id);
        }
        
        return findContainerChildren(base,id);
    }
}
