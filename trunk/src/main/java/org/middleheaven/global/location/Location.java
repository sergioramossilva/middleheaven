package org.middleheaven.global.location;

import java.io.Serializable;

import org.middleheaven.global.address.Address;
import org.middleheaven.util.measure.AngularPosition;
import org.middleheaven.util.measure.DecimalMeasure;

/**
 * Represents the standard set of basic location information. 
 * This includes the coordinates, accuracy, speed and bearing 
 * 
 */
public class Location implements Serializable,Locatable{

	private Address address;
	private Coordinates coordinates;
	private DecimalMeasure speed;
	private AngularPosition bearing;
	
	public Address getAddress() {
		return address;
	}
	public void setAddress(Address address) {
		this.address = address;
	}
	public Coordinates getCoordinates() {
		return coordinates;
	}
	public void setCoordinates(Coordinates coordinates) {
		this.coordinates = coordinates;
	}
	public DecimalMeasure getSpeed() {
		return speed;
	}
	public void setSpeed(DecimalMeasure speed) {
		this.speed = speed;
	}
	public AngularPosition getBearing() {
		return bearing;
	}
	public void setBearing(AngularPosition bearing) {
		this.bearing = bearing;
	}
	
}
