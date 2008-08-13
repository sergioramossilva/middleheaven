package org.middleheaven.global.location;

public class LocationUpdateEvent {

	private LocationProvider provider;
	private Location location;

	public LocationUpdateEvent(LocationProvider provider, Location location) {
		super();
		this.provider = provider;
		this.location = location;
	}
	
	public LocationProvider getProvider() {
		return provider;
	}
	public Location getLocation() {
		return location;
	}
}
