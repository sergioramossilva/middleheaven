package org.middleheaven.quantity.time.clocks.official;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import org.middleheaven.quantity.time.Clock;


/**
 * NtpClient - an NTP client for Java.  This program connects to an NTP server
 * and prints the response to the console.
 * 
 * The local clock offset calculation is implemented according to the SNTP
 * algorithm specified in RFC 2030.  
 * 
 * Note that on windows platforms, the curent time-of-day timestamp is limited
 * to an resolution of 10ms and adversely affects the accuracy of the results.
 * 
 * 
 * This code is copyright (c) Adam Buckley 2004
 *
 * This program is free software; you can redistribute it and/or modify it 
 * under the terms of the GNU General Public License as published by the Free 
 * Software Foundation; either version 2 of the License, or (at your option) 
 * any later version.  A HTML version of the GNU General Public License can be
 * seen at http://www.gnu.org/licenses/gpl.html
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT 
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or 
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for 
 * more details.
 *  
 * @author Adam Buckley
 */
public class SNTPClient {

	private final static long OFFSET_FROM_JAVA_EPOC = 2208988800000L;

	private InetAddress serverAdress;

	private double roundTripDelay;

	private long localClockOffset;
	private Clock reference;
	
	public SNTPClient(Clock reference ,InetAddress serverAdress){
		this.serverAdress = serverAdress;
		this.reference = reference;
	}


	public long now() throws IOException{
		// Send request
		DatagramSocket socket = new DatagramSocket();
		socket.setSoTimeout(1000); // 1 second
		
		try {
			byte[] buf = new NtpMessage().toByteArray();
			DatagramPacket requestPacket = new DatagramPacket(buf, buf.length, serverAdress, 123);
			DatagramPacket packet = new DatagramPacket(buf, buf.length);
			// Set the transmit timestamp *just* before sending the packet
			// ToDo: Does this actually improve performance or not?
			//NtpMessage.encodeTimestamp(requestPacket.getData(), 40,
			//	(System.currentTimeMillis()/1000.0) + OFFSET_FROM_JAVA_EPOC);

			long before = reference.getTime().milliseconds();
			// Send Request
			socket.send(requestPacket);
	
			// Get response
			socket.receive(packet);

			// Immediately record the incoming timestamp
			long after =  reference.getTime().milliseconds();
			double destinationTimestamp =(after + OFFSET_FROM_JAVA_EPOC)/1000.0;


			// Process response
			NtpMessage msg = new NtpMessage(packet.getData());

			// Corrected, according to RFC2030 errata
			//double roundTripDelay = (destinationTimestamp-msg.originateTimestamp) -
			//	(msg.transmitTimestamp-msg.receiveTimestamp);

			roundTripDelay = (after-before) - (msg.transmitTimestamp-msg.receiveTimestamp) * 1000;

			localClockOffset = (long)(((msg.receiveTimestamp - msg.originateTimestamp) + 
					(msg.transmitTimestamp - destinationTimestamp)) / 2)*1000;

			return (long)(msg.transmitTimestamp  * 1000) + localClockOffset - OFFSET_FROM_JAVA_EPOC;
		} finally {
			socket.close();
		}
	}



	/**
	 * @return
	 */
	public double getRoundTripDelay() {
		return roundTripDelay;
	}



	/**
	 * @return
	 */
	public long getLocalClockOffset() {
		return localClockOffset;
	}
}


