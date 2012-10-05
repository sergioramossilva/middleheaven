package org.middleheaven.util.collections;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.middleheaven.util.classification.Classifier;
import org.middleheaven.util.classification.Predicate;


public class LinkedTreeNode<E> implements TreeNode<E>{

	private List<TreeNode<E>> nodes = new LinkedList<TreeNode<E>>();
	E value;
	private TreeNode<E> parent;
	
	public void addChild(TreeNode<E> node){
		nodes.add(node);
	}
	
	public void removeChild(TreeNode<E> node){
		nodes.remove(node);
	}
	
	public Iterator<TreeNode<E>> iterator(){
		return nodes.iterator();
	}
	
	public E getValue(){
		return value;
	}
	
	public void setValue(E value){
		this.value = value;
	}

	@Override
	public int childCount() {
		return nodes.size();
	}

	@Override
	public Collection<TreeNode<E>> children() {
		return nodes;
	}

	@Override
	public TreeNode<E> getParent() {
		return this.parent;
	}

	@Override
	public boolean isEmpty() {
		return this.nodes.isEmpty();
	}

	@Override
	public void setParent(TreeNode<E> parent) {
		if(this.parent !=null && !this.parent.equals(parent)){
			throw new IllegalStateException("Parent already set");
		}
		this.parent = parent;
	}

	@Override
	public void forEachParent(Walker<TreeNode<E>> walker) {
		if (parent != null){
			walker.doWith(this.parent);
			parent.forEachParent(walker);
		}
	}

	@Override
	public void forEachRecursive(Walker<TreeNode<E>> walker) {
		for (TreeNode<E> t : this){
			walker.doWith(t);
			t.forEachRecursive(walker);
		}
	}

	@Override
	public void forEach(Walker<TreeNode<E>> walker) {
		for (TreeNode<E> t : this){
			walker.doWith(t);
		}
	}
	


	/**
	 * {@inheritDoc}
	 */
	@Override
	public <L extends Collection<TreeNode<E>>> L into(L collection) {
		for (TreeNode<E> t : this){
			collection.add(t);
		}
		
		return collection;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Walkable<TreeNode<E>> filter(Predicate<TreeNode<E>> predicate) {
		return new IterableWalkable<TreeNode<E>>(this).filter(predicate);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <C> Walkable<C> map(Classifier<C, TreeNode<E>> classifier) {
		return new IterableWalkable<TreeNode<E>>(this).map(classifier);
	}


}
