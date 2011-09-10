package org.middleheaven.text.indexing;

public interface DocumentFieldModel {

	public abstract String getName();

	public abstract boolean isStorable();

	public abstract boolean isIndexable();

	public abstract boolean isAnalizable();

	public abstract boolean isNormUsable();

}