/*
 * Created on 2006/08/12
 *
 */
package org.middleheaven.classification;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

public abstract class ClassificationUtils {

    private ClassificationUtils(){};
    
    public static <C,T> Classification<C,T> classify(Collection<T> elements, Classifier<C,T> classifier){
    	MapClassification<C,T> classification = new MapClassification<C,T>();
    	
    	for (T element : elements){
    		classification.addElement(classifier.classify(element), element);
        }
    	return classification;
    }
    
    /**
     * Determines if any element of the array matched the filter
     * @param <T>
     * @param set
     * @param matcher
     * @return
     */
    public static <T> boolean isInArray(T[] set, BooleanClassifier<T> matcher){

        for (T element : set){
            if (matcher.classify(element)){
                return true;
            }
        }
        return false;
    }
    
    /**
     * Filters a map by removing the element that do not pass the filter
     * @param <K>
     * @param <T>
     * @param filter
     * @param map
     */
    public static <K,T> void filter ( Map<K,T> map,BooleanClassifier<T> filter){
        if (map.isEmpty()){
            return; // nothing to do
        }
        for (Iterator<Map.Entry<K, T>> it = map.entrySet().iterator();it.hasNext();){
            if (!filter.classify(it.next().getValue())){
                it.remove();
            }
        }
    }
    
    /**
     * Filters a collection by removing the elements that do not pass the filter
     * @param <T>
     * @param filter
     * @param collection
     */
    public  static <T> void filter (Iterable<T> collection,BooleanClassifier<T> filter){
        for (Iterator<T> it = collection.iterator();it.hasNext();){
            if (!filter.classify(it.next())){
                it.remove();
            }
        }
    }
    
    /**
     * Filters a collection by adding the element that pass the filter into another collection
     * @param <T>
     * @param filter
     * @param collection
     * @param result
     */
    public  static <T> void filter (BooleanClassifier<T> filter, Collection<T> collection, Collection<T> result){
        if (collection.isEmpty()){
            return; // nothing to do
        }
        for (T o:collection){
            if (filter.classify(o)){
                result.add(o);
            }
        }
    }
}
