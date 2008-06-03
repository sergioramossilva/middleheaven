package org.middleheaven.util.measure.time.clocks;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.middleheaven.util.measure.time.LocalTimePoint;
import org.middleheaven.util.measure.time.TimePoint;
import org.middleheaven.util.measure.time.clocks.official.SNTPClient;

public class NTPUniversalTimeClock extends UniversalTimeClock {

	@Override
	public TimePoint now() {
		try {
			SNTPClient client = new SNTPClient(getNTPServerAdress()); 
			return  LocalTimePoint.fromUniversalTime(client.now(), this.getTimeZone());
		} catch (Exception e){
			// TODO log to book
			throw new RuntimeException(e);
		}
	}

	protected InetAddress getNTPServerAdress() throws UnknownHostException{
		return InetAddress.getByName("200.20.186.75");
	}
}
