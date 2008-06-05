package org.middleheaven.util.measure.time.zones;

import org.middleheaven.util.measure.time.TimeZone;
import org.middleheaven.util.measure.time.TimeZoneTable;

public class JavaTimeZoneTable extends TimeZoneTable {

	@Override
	public TimeZone convertFromJavaTimeZone(java.util.TimeZone zone) {
		return new WrappedTimeZone(zone);
	}

	@Override
	public TimeZone getTimeZone(String reference) {
		return convertFromJavaTimeZone(java.util.TimeZone.getTimeZone(reference));
	}

	private static class WrappedTimeZone extends TimeZone{
		java.util.TimeZone zone;
		
		public WrappedTimeZone(java.util.TimeZone zone) {
			this.zone = zone;
		}

		@Override
		public long getRawOffset() {
			return zone.getRawOffset();
		}

		@Override
		public long getOffset(long universalTime) {
			return zone.getOffset(universalTime);
		}

		@Override
		public java.util.TimeZone toTimeZone() {
			return zone;
		}
		
	}
}
