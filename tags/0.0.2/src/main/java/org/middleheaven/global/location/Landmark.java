package org.middleheaven.global.location;

import org.middleheaven.global.address.Address;
import org.middleheaven.quantity.coordinate.GeoCoordinate;

public class Landmark implements Locatable{
	
	private String name;
	private String description;
	private GeoCoordinate geoCoordinate;
	private Address address;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}

	
	public void setGeoCoordinate(GeoCoordinate GeoCoordinate) {
		this.geoCoordinate = GeoCoordinate;
	}
	public Address getAddress() {
		return address;
	}
	public void setAddress(Address address) {
		this.address = address;
	}
	@Override
	public GeoCoordinate getCoordinates() {
		return geoCoordinate;
	}

	
}
