package org.middleheaven.core.wiring;

import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.middleheaven.core.wiring.activation.EmptyCallPoint;
import org.middleheaven.core.wiring.activation.PostCreatePoint;
import org.middleheaven.core.wiring.activation.PreDestroiPoint;
import org.middleheaven.core.wiring.activation.PublishPoint;

/**
 * A model for wiring.
 * The wiring model consists of construct-point where the object is created/retrieved and multiple after-points where the object is injected.
 * 
 */
public class BeanModel {

	private final Map<String, Object > params = new HashMap<String, Object>();
	private final List<Class<?>> scopes = new LinkedList<Class<?>>();
	private Collection<PublishPoint> publishPoints = new LinkedList<PublishPoint>();

	private ProducingWiringPoint point;
	private final Collection<AfterWiringPoint> afterpoints = new HashSet<AfterWiringPoint>();
	private Class<?> type;
	private Class<?> contractType;
	private PostCreatePoint postCreatePoint = EmptyCallPoint.getInstance();
	private PreDestroiPoint preDestroiPoint = EmptyCallPoint.getInstance();;

	/**
	 * 
	 * Constructor.
	 */
	public BeanModel(Class<?> type){
		this.type = type;
		this.contractType = type;
	}



	/**
	 * Obtains {@link Class<?>}.
	 * @return the contractType
	 */
	public Class<?> getContractType() {
		return contractType;
	}



	/**
	 * Atributes {@link Class<?>}.
	 * @param contractType the contractType to set
	 */
	public void setContractType(Class<?> contractType) {
		this.contractType = contractType;
	}



	/**
	 * Obtains the {@link ProducingWiringPoint} that will obtain the object.
	 * @return the {@link ProducingWiringPoint}
	 */
	public ProducingWiringPoint getProducingWiringPoint(){
		return point;
	}

	/**
	 * Attributes the {@link ProducingWiringPoint} to use.
	 * @param point the {@link ProducingWiringPoint} to use.
	 */
	public void setProducingWiringPoint(ProducingWiringPoint point){
		if (this.point == null){
			this.point = point;
		} else {
			this.point = this.point.merge(point);
		}
	}

	/**
	 * 
	 * @return all the {@link AfterWiringPoint}s in this model.
	 */
	public Collection<AfterWiringPoint> getAfterPoints(){
		return Collections.unmodifiableCollection(afterpoints);
	}

	/**
	 * Add an {@link AfterWiringPoint}. A {@link AfterWiringPoint} is used to inject other objects after creation.
	 * @param point the {@link AfterWiringPoint} to add to this model.
	 */
	public void addAfterWiringPoint(AfterWiringPoint point){
		this.afterpoints.add(point);
	}

	/**
	 * Remove an {@link AfterWiringPoint}. A {@link AfterWiringPoint} is used to inject other objects after creation.
	 * @param point the {@link AfterWiringPoint} to remove to this model.
	 */
	public void removeAfterWiringPoint(AfterWiringPoint point){
		this.afterpoints.remove(point);
	}

	/**
	 * 
	 * @return <code>true</code> if any of the after-points is required, <code>false</code> if all after-points are optional.
	 */
	public boolean isRequired() {
		for (AfterWiringPoint ap : afterpoints){
			if(ap.isRequired()){
				return true;
			}
		}
		return false;
	}

	/**
	 * @return
	 */
	public Map<String , Object> getParams() {
		return this.params;
	}

	/**
	 * @param params
	 */
	public void addParams(Map<String, Object> all) {
		this.params.putAll(all);
	}

	/**
	 * @param annotationType
	 */
	public void addScope(Class<? extends Annotation> scopeAnnotation) {
		this.scopes.add(scopeAnnotation);
	}

	public List<Class<?>> getScopes(){
		return this.scopes;
	}

	/**
	 * @return
	 */
	public Class<?> getBeanClass() {
		return type;
	}

	public int hashCode(){
		return type.getName().hashCode();
	}

	public boolean equals(Object other){
		return other instanceof BeanModel && 
				((BeanModel)other).type.getName().equals(this.type.getName());
	}

	public Collection<PublishPoint> getPublishPoints() {
		return Collections.unmodifiableCollection(publishPoints);
	}

	public void addPublishPoint(PublishPoint element) {
		this.publishPoints.add(element);
	}

	public void removePublishPoint(PublishPoint element) {
		this.publishPoints.remove(element);
	}

	public String toString(){
		return publishPoints.toString();
	}

	
	public void setPostCreatePoint(PostCreatePoint postCreatePoint) {
		this.postCreatePoint = postCreatePoint;
	}

	public PostCreatePoint getPostCreatePoint() {
		return postCreatePoint;
	}

	public PreDestroiPoint getpreDestroiPoint(){
		return preDestroiPoint;
	}

	/**
	 * @param methodPostCreatePoint
	 */
	public void setPreDestroiPoint(PreDestroiPoint preDestroiPoint) {
		this.preDestroiPoint = preDestroiPoint;
	}


}
