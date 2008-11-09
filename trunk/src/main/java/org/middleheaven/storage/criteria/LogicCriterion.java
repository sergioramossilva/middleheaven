package org.middleheaven.storage.criteria;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

import org.middleheaven.util.CollectionUtils;
import org.middleheaven.util.HashCodeUtils;
import org.middleheaven.util.classification.LogicOperator;


public class LogicCriterion implements BooleanCriterion , Iterable<Criterion>{


	private static final long serialVersionUID = 6060765610996634126L;
	
	private final LogicOperator operator;
	protected final LinkedList<Criterion> criteria;


	public LogicOperator getOperator() {
		return operator;
	}

	public LogicCriterion(LogicCriterion other){
		this.operator = other.operator;
		this.criteria = new LinkedList<Criterion>(other.criteria);
	}

	public LogicCriterion(LogicOperator operator){
		this.operator = operator;
		this.criteria = new LinkedList<Criterion>();
	}

	public LogicCriterion(){
		this(LogicOperator.and());
	}



	private void addCriterion(Criterion c){

		if (c instanceof JuntionCriterion){
			// coloca o JuntionRestriction no topo da lista,
			// mas respeita a ordem se insersão dos critérios a this


			// procura o primeiro item que não é um JuntionRestriction
			// e insere antes dele.
			for (ListIterator<Criterion> it = criteria.listIterator();it.hasNext();){
				Object o = it.next();
				if (!(o instanceof JuntionCriterion)){
					it.previous();
					it.add(c);
					return;
				}
			}
			// se nenhum item é diferente de um JuntionRestriction
			// ( ou a lista está vazia)
			criteria.addLast(c);

		} else {
			criteria.addLast(c);
		}

	}

	public Criterion simplify(){
		if (this.criteria.isEmpty()){
			return EmptyCriterion.empty();
		} else if (this.criteria.size()==1){
			return ((Criterion)criteria.get(0)).simplify();
		} 

		// 2 ou mais
		Set<Criterion> set = new LinkedHashSet<Criterion>();
		int count=0;
		for ( Criterion c  : this.criteria){
			if (!c.isEmpty()){
				set.add(c);
				count++;
			} 
		}

		if (set.isEmpty()){
			return  EmptyCriterion.empty();
		} else if (count==1){
			return (Criterion)set.iterator().next();
		}else {

			LogicCriterion lc = new LogicCriterion(this.operator);
			lc.criteria.clear();
			lc.criteria.addAll(set);
			return lc;
		}
	}

	public Object clone(){
		return new LogicCriterion(this);
	}

	public BooleanCriterion and(LogicCriterion a){
		return addComposedCriterion(a,LogicOperator.and());
	}

	public BooleanCriterion and(Criterion a){
		if (a instanceof LogicCriterion){
			return addComposedCriterion((LogicCriterion)a,LogicOperator.and());
		} 

		return addSimpleCriterion(a,LogicOperator.and());

	}
	
	public BooleanCriterion or(LogicCriterion a){
		return addComposedCriterion(a,LogicOperator.or());
	}

	public BooleanCriterion or(Criterion a){
		if (a instanceof LogicCriterion){
			return addComposedCriterion((LogicCriterion)a,LogicOperator.or());
		}

		return addSimpleCriterion(a,LogicOperator.or());

	}
	
	public LogicCriterion add(Criterion criterion){
		if (!criterion.isEmpty()){
			addCriterion(criterion);
		}
		return this;
	}

	protected BooleanCriterion addSimpleCriterion(Criterion a , LogicOperator op ){
		if (this.operator.equals(op)){
			add(a);
			return this;
		} else {
			// different operator
			return new LogicCriterion(op).add(this).add(a);
		}
	}

	protected BooleanCriterion addComposedCriterion(LogicCriterion a , LogicOperator op ){

		if (a.criteriaCount()==0){
			return this;
		}

		if (a.getOperator().equals(op) && a.getOperator().equals(this.operator)){
			// AND <AND> AND  
			// OR <OR> OR
			for (Criterion c : criteria){
				addCriterion(c);
			}
		}

		
		return  new LogicCriterion(op).add(this).add(a);

	}


	public boolean equals(Object other){
		return other instanceof LogicCriterion && equals(((LogicCriterion)other));
	}

	public boolean equals(LogicCriterion other){
		return this.operator.equals(other.operator) && 
		CollectionUtils.equals( this.criteria , other.criteria);

	}

	public int hashCode(){
		return operator.hashCode() ^ HashCodeUtils.hash(HashCodeUtils.SEED,criteria);
	}

	public int criteriaCount(){
		return criteria.size();
	}


	public List<Criterion> criterias(){
		return Collections.unmodifiableList(criteria);
	}

	 public String toString(){
		 StringBuffer buffer = new StringBuffer("(");
		 for (int i=0;i<criteria.size();i++){
			 buffer.append(criteria.get(i).toString());
			 buffer.append(" ");
			 if (i!=criteria.size()-1){
				 buffer.append(operator.toString());
				 buffer.append(" ");
			 }
		 }
		 return buffer.append(")").toString();
	 }

	 public boolean isEmpty() {
		 return this.criteria.isEmpty();
	 }

	@Override
	public Iterator<Criterion> iterator() {
		return criteria.iterator();
	}



}
