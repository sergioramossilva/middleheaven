package org.middleheaven.quantity.time.clocks.official;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import org.middleheaven.io.ManagedIOException;
import org.middleheaven.quantity.time.LocalTimePoint;
import org.middleheaven.quantity.time.TimePoint;
import org.middleheaven.quantity.time.clocks.Clock;
import org.middleheaven.quantity.time.clocks.MachineClock;
import org.middleheaven.quantity.time.clocks.WrappedClock;

/**
 * 
 * Experimental.
 * 
 * A Simple  Network Time Protocol (SNTP) synchronous clock. The NTP server must be selected at construction time
 * but can be changed afterwards 
 * 
 * @see The Network Time Protocol (<a href="http://www.ntp.org/">http://www.ntp.org/</a>) 
 */
public final class SNTPUniversalTimeClock extends WrappedClock {
	// Test Server "200.20.186.75"
	
	InetAddress ntpServerAddress;
	
	public static SNTPUniversalTimeClock forServer(Clock reference, String ntpServerAddress)throws UnknownHostException{
		return new SNTPUniversalTimeClock(reference,InetAddress.getByName(ntpServerAddress));
	}
	
	public static SNTPUniversalTimeClock forServer(Clock reference,InetAddress ntpServerAddress) {
		return new SNTPUniversalTimeClock(reference,ntpServerAddress);
	}
	
	public static SNTPUniversalTimeClock forServer(String ntpServerAddress)throws UnknownHostException{
		return new SNTPUniversalTimeClock(new MachineClock(),InetAddress.getByName(ntpServerAddress));
	}
	
	public static SNTPUniversalTimeClock forServer(InetAddress ntpServerAddress) {
		return new SNTPUniversalTimeClock(new MachineClock(),ntpServerAddress);
	}
	
	private SNTPUniversalTimeClock(Clock reference,InetAddress ntpServerAddress){
		super(reference);
		this.ntpServerAddress = ntpServerAddress;
	}
	
	@Override
	public TimePoint getTime() {
		try {
			SNTPClient client = new SNTPClient(this.getReferenceClock(),getNTPServerAdress()); 
			return  LocalTimePoint.fromUniversalTime(client.now(), this.getTimeZone());
		} catch (IOException e){
			throw ManagedIOException.manage(e);
		}
	}

	public InetAddress getNTPServerAdress() throws UnknownHostException{
		return ntpServerAddress;
	}
	
	public void setNTPServerAdress(InetAddress ntpServerAddress){
		this.ntpServerAddress = ntpServerAddress;
	}

	@Override
	public double getCadence() {
		return 1;
	}


}
