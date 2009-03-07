package org.middleheaven.global.location;

import java.io.Serializable;

import org.middleheaven.global.address.Address;
import org.middleheaven.quantity.coordinate.GeoCoordinate;
import org.middleheaven.quantity.measurables.Velocity;
import org.middleheaven.quantity.measure.AngularMeasure;
import org.middleheaven.quantity.measure.DecimalMeasure;

/**
 * Represents the standard set of basic location information. 
 * This includes the coordinates, accuracy, speed and bearing 
 * 
 */
public class Location implements Serializable,Locatable{

	private Address address;
	private GeoCoordinate coordinates;
	private DecimalMeasure<Velocity> speed;
	private AngularMeasure bearing;
	
	public Address getAddress() {
		return address;
	}
	public void setAddress(Address address) {
		this.address = address;
	}
	public GeoCoordinate getCoordinates() {
		return coordinates;
	}
	public void setCoordinates(GeoCoordinate coordinates) {
		this.coordinates = coordinates;
	}
	public DecimalMeasure<Velocity> getSpeed() {
		return speed;
	}
	public void setSpeed(DecimalMeasure<Velocity> speed) {
		this.speed = speed;
	}
	public AngularMeasure getBearing() {
		return bearing;
	}
	public void setBearing(AngularMeasure bearing) {
		this.bearing = bearing;
	}
	
}
