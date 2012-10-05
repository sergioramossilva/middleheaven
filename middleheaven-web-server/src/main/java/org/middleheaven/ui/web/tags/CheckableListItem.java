package org.middleheaven.ui.web.tags;

public class CheckableListItem implements Comparable<CheckableListItem>{
	
	private boolean check;
	private String value;
	private String id;
	
	public CheckableListItem(String value, String id) {
		this(value,id,false);
	}
	
	public CheckableListItem(String value, int id,boolean check) {
		this(value, String.valueOf(id), check);
	}
	
	public CheckableListItem(String value, String id,boolean check) {
		this.check = check;
		this.value = value;
		this.id = id;
	}
	
	public boolean isCheck() {
		return check;
	}
	
	public String getValue() {
		return value;
	}
	
	public String getId() {
		return id;
	}

	@Override
	public int compareTo(CheckableListItem other) {
		return this.value.compareTo(other.value);
	}
	
	public boolean equals(Object other) {
		return other instanceof CheckableListItem
				&& equalsOther((CheckableListItem) other);
	}
	
	private boolean equalsOther(CheckableListItem other) {
		return other == null ? false : this.id.equals(other.id);
	}
	
	public int hashCode() {
		return id.hashCode();
	}
}
