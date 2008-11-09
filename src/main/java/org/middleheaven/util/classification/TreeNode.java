package org.middleheaven.util.classification;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class TreeNode<E> implements Iterable<TreeNode<E>>{

	private List<TreeNode<E>> nodes = new LinkedList<TreeNode<E>>();
	E value;
	
	public void addNode(TreeNode<E> node){
		nodes.add(node);
	}
	
	public void removeNode(TreeNode<E> node){
		nodes.remove(node);
	}
	
	public Iterator<TreeNode<E>> iterator(){
		return nodes.iterator();
	}
	
	public int size(){
		return nodes.size();
	}
	
	public E getValue(){
		return value;
	}
	
	public void setValue(E value){
		this.value = value;
	}
}
